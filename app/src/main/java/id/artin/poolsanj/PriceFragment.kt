package id.artin.poolsanj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.swiperefresh.SwipeRefreshLayout
import kotlinx.coroutines.launch

/** Price list for a category (summary/arz/gold_coin/crypto). */
class PriceFragment : Fragment() {

    companion object {
        fun newInstance(cat: String) = PriceFragment().apply {
            arguments = Bundle().apply { putString("cat", cat) }
        }
    }

    private val titles = mapOf(
        "summary" to "📋 خلاصه بازار",
        "arz" to "💱 ارز — بازار آزاد",
        "gold_coin" to "🥇 طلا و سکه",
        "crypto" to "🌐 کریپتو"
    )

    private fun slugsFor(cat: String): List<Pair<String, String>> = when (cat) {
        "summary" -> Cat.SUMMARY.mapNotNull { s ->
            Cat.titleOf(s)?.let { s to it }
        }
        "arz" -> Cat.ARZ
        "gold_coin" -> Cat.TALA + Cat.SEKKE
        else -> Cat.CRYPTO
    }

    override fun onCreateView(i: LayoutInflater, p: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_prices, p, false)

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        val cat = requireArguments().getString("cat") ?: "summary"
        v.findViewById<TextView>(R.id.title).text = titles[cat] ?: ""
        v.findViewById<SwipeRefreshLayout>(R.id.swipe).setOnRefreshListener { load(v, true) }
        load(v, false)
    }

    private fun load(v: View, force: Boolean) {
        val swipe = v.findViewById<SwipeRefreshLayout>(R.id.swipe)
        val box = v.findViewById<ViewGroup>(R.id.list)
        val status = v.findViewById<TextView>(R.id.status)
        swipe.isRefreshing = !Repo.lastRows.isEmpty()
        viewLifecycleOwner.lifecycleScope.launch {
            if (force || Repo.lastRows.isEmpty()) Repo.loadAll()
            status.text = getString(
                R.string.updated_fmt,
                NumFa.faDigits(java.text.SimpleDateFormat("HH:mm:ss",
                    java.util.Locale.US).format(java.util.Date(Repo.lastUpdated))),
                Repo.lastSource
            )
            box.removeAllViews()
            val inf = layoutInflater
            var shown = 0
            for ((slug, title) in slugsFor(cat)) {
                val r = Repo.lastRows[slug] ?: continue
                val item = inf.inflate(R.layout.item_price, box, false)
                item.findViewById<TextView>(R.id.name).text = title
                item.findViewById<TextView>(R.id.arrow).text = r.arrow
                val priceV = item.findViewById<TextView>(R.id.price)
                priceV.text = NumFa.grouped(r.price)
                val pctV = item.findViewById<TextView>(R.id.pct)
                pctV.text = if (r.pct.isEmpty()) "" else NumFa.faDigits("(%${r.pct})")
                pctV.setTextColor(if (r.dir > 0) 0xFFFF5A5F.toInt()
                    else if (r.dir < 0) 0xFF4ADE80.toInt() else 0xFF94A3B8.toInt())
                box.addView(item)
                shown++
            }
            if (shown == 0) status.text = getString(R.string.no_data)
            swipe.isRefreshing = false
        }
    }
}
