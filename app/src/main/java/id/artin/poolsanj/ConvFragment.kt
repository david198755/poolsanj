package id.artin.poolsanj

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/** Converter: rial <-> toman <-> dollar + words + gold affordability. */
class ConvFragment : Fragment() {

    private var busy = false

    override fun onCreateView(i: LayoutInflater, p: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_conv, p, false)

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        v.findViewById<TextView>(R.id.dollar_rate).setOnClickListener { loadRate(v) }
        loadRate(v)
        for (id in intArrayOf(R.id.rial, R.id.toman, R.id.dollar)) {
            v.findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(e: Editable?) = convert(v, id)
                override fun beforeTextChanged(p0: CharSequence?, a: Int, b: Int, c: Int) {}
                override fun onTextChanged(p0: CharSequence?, a: Int, b: Int, c: Int) {}
            })
        }
    }

    private fun dollarToman(): Double =
        Repo.lastRows["price_dollar_rl"]?.price?.replace(",", "")?.toDoubleOrNull()?.div(10.0) ?: 0.0

    private fun loadRate(v: View) {
        val rateV = v.findViewById<TextView>(R.id.dollar_rate)
        rateV.text = getString(R.string.loading)
        viewLifecycleOwner.lifecycleScope.launch {
            if (Repo.lastRows.isEmpty()) Repo.loadAll()
            val r = dollarToman()
            rateV.text = if (r > 0)
                getString(R.string.rate_fmt, NumFa.grouped("%.0f".format(r * 10)))
            else getString(R.string.no_data)
            convert(v, 0)
            renderGold(v)
        }
    }

    /** Convert the edited field into the other two; empty source -> clear all. */
    private fun convert(v: View, srcId: Int) {
        if (busy) return
        busy = true
        try {
            val rialE = v.findViewById<EditText>(R.id.rial)
            val tomanE = v.findViewById<EditText>(R.id.toman)
            val dollarE = v.findViewById<EditText>(R.id.dollar)
            val rate = dollarToman()

            fun num(id: Int): Long = v.findViewById<EditText>(id)
                .text.toString().replace(",", "").replace("٬", "").toLongOrNull() ?: -1L

            fun set(id: Int, n: Long) {
                val e = v.findViewById<EditText>(id)
                if (num(id) != n) e.setText(NumFa.grouped(n.toString()))
            }

            when (srcId) {
                R.id.rial -> {
                    val r = num(R.id.rial)
                    if (r < 0) return
                    set(R.id.toman, r / 10)
                    if (rate > 0) set(R.id.dollar, Math.round((r / 10.0) / rate))
                    words(v, R.id.rial_text, r, "ریال")
                }
                R.id.toman -> {
                    val t = num(R.id.toman)
                    if (t < 0) return
                    set(R.id.rial, t * 10)
                    if (rate > 0) set(R.id.dollar, Math.round(t.toDouble() / rate))
                    words(v, R.id.toman_text, t, "تومان")
                }
                else -> {
                    val d = num(R.id.dollar)
                    if (d < 0) return
                    if (rate > 0) {
                        set(R.id.toman, Math.round(d * rate))
                        set(R.id.rial, Math.round(d * rate) * 10)
                        words(v, R.id.dollar_text, d, "دلار")
                    }
                }
            }
            renderGold(v)
        } finally {
            busy = false
        }
    }

    private fun words(v: View, id: Int, n: Long, unit: String) {
        val tv = v.findViewById<TextView>(id)
        tv.text = if (n <= 0) getString(R.string.enter_amount)
        else NumFa.words(n) + " " + unit
    }

    /** Gold/coin affordability like his web page. */
    private fun renderGold(v: View) {
        val box = v.findViewById<ViewGroup>(R.id.gold_items) ?: return
        val budget = v.findViewById<EditText>(R.id.toman)
            .text.toString().replace(",", "").replace("٬", "").toLongOrNull() ?: 0L
        box.removeAllViews()
        val items = listOf(
            "geram18" to "طلای ۱۸ (گرم)", "mesghal" to "مثقال",
            "sekee" to "سکه امامی", "nim" to "نیم سکه", "rob" to "ربع سکه"
        )
        val inf = layoutInflater
        var any = false
        for ((slug, title) in items) {
            val price = Repo.lastRows[slug]?.price?.replace(",", "")?.toLongOrNull() ?: continue
            val row = inf.inflate(R.layout.item_gold, box, false)
            row.findViewById<TextView>(R.id.g_name).text = title
            val qty = if (price > 0) budget / price else 0
            row.findViewById<TextView>(R.id.g_qty).text =
                "${NumFa.faDigits(qty.toString())} عدد"
            row.findViewById<TextView>(R.id.g_price).text =
                NumFa.grouped(price.toString())
            box.addView(row)
            any = true
        }
        if (!any) box.addView(inf.inflate(R.layout.item_gold_loading, box, false))
        v.findViewById<TextView>(R.id.gold_update)?.text =
            if (Repo.lastUpdated == 0L) "" else
                getString(R.string.gold_updated_fmt, NumFa.faDigits(
                    java.text.SimpleDateFormat("HH:mm", java.util.Locale.US)
                        .format(java.util.Date(Repo.lastUpdated))))
    }
}
