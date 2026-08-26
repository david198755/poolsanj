package id.artin.poolsanj

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** One market row. */
data class Row(val slug: String, val title: String, val price: String,
               val pct: String, val dir: Int) {   // dir: 1 up, -1 down, 0 flat
    val arrow: String get() = if (dir > 0) "🔴" else if (dir < 0) "🟢" else "⚪️"
}

object Repo {
    private const val SERVER = "https://artin.my.id/poolsanj/prices.json"
    private const val TGJU_TICK = "https://api.tgju.org/v1/market/indicator/today-table-data/"

    /** Last successful load (offline cache). */
    @Volatile var lastRows: Map<String, Row> = emptyMap()
    @Volatile var lastSource: String = "-"
    @Volatile var lastUpdated: Long = 0L

    private fun httpGet(url: String, timeoutMs: Int = 9000): String? = try {
        val c = URL(url).openConnection() as HttpURLConnection
        c.connectTimeout = timeoutMs
        c.readTimeout = timeoutMs
        c.setRequestProperty("User-Agent", "Mozilla/5.0 PoolSanj")
        if (c.responseCode == 200) c.inputStream.bufferedReader().use { it.readText() } else null
    } catch (_: Exception) {
        null
    }

    /** Server bundle (bot-prewarmed). */
    private fun fromServer(): Map<String, Row>? = withContext(Dispatchers.IO) {
        val txt = httpGet(SERVER) ?: return@withContext null
        try {
            val root = JSONObject(txt)
            val rates = root.getJSONObject("rates")
            val out = mutableMapOf<String, Row>()
            for (key in rates.keys()) {
                val o = rates.getJSONObject(key)
                val title = Cat.titleOf(key) ?: key
                out[key] = Row(key, title,
                    o.optString("p", "-"), o.optString("pct", ""),
                    when (o.optString("dir")) {
                        "up" -> 1; "down" -> -1; else -> 0
                    })
            }
            out
        } catch (_: Exception) {
            null
        }
    }

    /** Direct tgju tick endpoint (works even when foreign net is down). */
    suspend fun fromTgju(slug: String): Row? = withContext(Dispatchers.IO) {
        val txt = httpGet(TGJU_TICK + slug) ?: return@withContext null
        try {
            val rows = JSONObject(txt).getJSONArray("data")
            if (rows.length() == 0) return@withContext null
            val top = rows.getJSONArray(0)
            val price = top.getString(0).replace(",", "").trim()
            val pctRaw = if (top.length() > 3) top.getString(3) else ""
            val pct = Regex("\\d+(?:\\.\\d+)?%").find(pctRaw)?.value ?: ""
            val dir = when {
                pctRaw.contains("high") -> 1
                pctRaw.contains("low") -> -1
                else -> 0
            }
            Row(slug, Cat.titleOf(slug) ?: slug, price, pct, dir)
        } catch (_: Exception) {
            null
        }
    }

    /** Load all slugs: server first; per-slug tgju fallback for holes. */
    suspend fun loadAll(): Map<String, Row> {
        fromServer()?.let { srv ->
            lastRows = srv
            lastSource = "server"
            lastUpdated = System.currentTimeMillis()
            return srv
        }
        // direct mode
        val out = HashMap<String, Row>()
        for ((slug, title) in Cat.ARZ + Cat.TALA + Cat.SEKKE + Cat.CRYPTO + listOf("ons" to "")) {
            fromTgju(slug)?.let { out[slug] = it }
        }
        // BTC in USD on tgju → convert via tether like the bot does
        val btc = out["crypto-bitcoin"]
        val usdt = out["crypto-tether"]
        if (btc != null && usdt != null) {
            val p = btc.price.toDoubleOrNull()
            val u = usdt.price.replace(",", "").toDoubleOrNull()
            if (p != null && u != null && u > 0)
                out["crypto-bitcoin"] = btc.copy(price = "%.0f".format(p * u))
        }
        lastRows = out
        lastSource = "tgju"
        lastUpdated = System.currentTimeMillis()
        return out
    }

    fun rowsFor(slugs: List<String>): List<Row> =
        slugs.mapNotNull { lastRows[it] }
}
