package id.artin.poolsanj

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PriceFragment : Fragment() {

    companion object {
        fun newInstance(cat: String) = PriceFragment().apply {
            arguments = bundleOf("cat" to cat)
        }
    }

    private val CATALOG = mapOf(
        "summary" to listOf(
            "price_dollar_rl", "price_eur", "sekee", "bahar_azadi",
            "geram18", "mesghal", "ons",
            "crypto-bitcoin", "crypto-tether", "crypto-ethereum"
        ),
        "arz" to listOf(
            "price_dollar_rl", "price_eur", "price_gbp", "price_aed",
            "price_try", "price_cny", "price_jpy", "price_inr",
            "price_krw", "price_sar", "price_chf", "price_cad",
            "price_aud", "price_sek", "price_nok", "price_dkk",
            "price_rub", "price_try_steel", "price_iqd", "price_syp",
            "price_afn", "price_pkr", "price_inr_steel", "price_kwd",
            "price_bhd", "price_qar", "price_omr", "price_lbp",
            "price_egp", "price_php", "price_thb", "price_myr",
            "price_idr", "price_vnd", "price_hkd", "price_twd",
            "price_sgd", "price_nzd", "price_zar", "price_kzt",
            "price_uzs", "price_mmk", "price_npr", "price_lkr",
            "price_bdt", "price_mnt", "price_geg", "price_azn",
            "price_amd", "price_gel"
        ),
        "tala" to listOf(
            "geram18", "mesghal", "ons", "sekee", "bahar_azadi",
            "nim", "rob", "geram24", "melting_gold", "melting_geram18",
            "sekeb", "sekebaha"
        ),
        "crypto" to listOf(
            "crypto-bitcoin", "crypto-tether", "crypto-ethereum",
            "crypto-binancecoin", "crypto-solana", "crypto-xrp",
            "crypto-dogecoin", "crypto-cardano", "crypto-tron",
            "crypto-avalanche", "crypto-dot", "crypto-chainlink",
            "crypto-polygon", "crypto-shibainu", "crypto-litecoin",
            "crypto-uniswap", "crypto-stellar"
        )
    )

    private val EMOJI = mapOf(
        "price_dollar_rl" to "🇺🇸", "price_eur" to "🇪🇺", "price_gbp" to "🇬🇧",
        "price_aed" to "🇦🇪", "price_try" to "🇹🇷", "price_cny" to "🇨🇳",
        "price_jpy" to "🇯🇵", "price_inr" to "🇮🇳", "price_krw" to "🇰🇷",
        "price_sar" to "🇸🇦", "price_chf" to "🇨🇭", "price_cad" to "🇨🇦",
        "price_aud" to "🇦🇺", "price_sek" to "🇸🇪", "price_nok" to "🇳🇴",
        "price_dkk" to "🇩🇰", "price_rub" to "🇷🇺", "price_try_steel" to "🇹🇷",
        "price_iqd" to "🇮🇶", "price_syp" to "🇸🇾", "price_afn" to "🇦🇫",
        "price_pkr" to "🇵🇰", "price_inr_steel" to "🇮🇳", "price_kwd" to "🇰🇼",
        "price_bhd" to "🇧🇭", "price_qar" to "🇶🇦", "price_omr" to "🇴🇲",
        "price_lbp" to "🇱🇧", "price_egp" to "🇪🇬", "price_php" to "🇵🇭",
        "price_thb" to "🇹🇭", "price_myr" to "🇲🇾", "price_idr" to "🇮🇩",
        "price_vnd" to "🇻🇳", "price_hkd" to "🇭🇰", "price_twd" to "🇹🇼",
        "price_sgd" to "🇸🇬", "price_nzd" to "🇳🇿", "price_zar" to "🇿🇦",
        "price_kzt" to "🇰🇿", "price_uzs" to "🇺🇿", "price_mmk" to "🇲🇲",
        "price_npr" to "🇳🇵", "price_lkr" to "🇱🇰", "price_bdt" to "🇧🇩",
        "price_mnt" to "🇲🇳", "price_geg" to "🇬🇪", "price_azn" to "🇦🇿",
        "price_amd" to "🇦🇲", "price_gel" to "🇬🇪",
        "sekee" to "🪙", "bahar_azadi" to "🪙", "geram18" to "✨",
        "mesghal" to "⚖️", "ons" to "🌍", "nim" to "🪙", "rob" to "🪙",
        "geram24" to "✨", "melting_gold" to "🔶", "melting_geram18" to "🔶",
        "sekeb" to "🪙", "sekebaha" to "🪙",
        "crypto-bitcoin" to "₿", "crypto-tether" to "₮", "crypto-ethereum" to "⟠",
        "crypto-binancecoin" to "◆", "crypto-solana" to "◎", "crypto-xrp" to "✕",
        "crypto-dogecoin" to "🐕", "crypto-cardano" to "◇", "crypto-tron" to "T",
        "crypto-avalanche" to "🔺", "crypto-dot" to "●", "crypto-chainlink" to "⬡",
        "crypto-polygon" to "⬡", "crypto-shibainu" to "🐕", "crypto-litecoin" to "Ł",
        "crypto-uniswap" to "🦄", "crypto-stellar" to "★"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_prices, container, false)
    }

    private val TITLES = mapOf(
        "summary" to "خلاصه بازار",
        "arz" to "ارز",
        "tala" to "طلا و سکه",
        "crypto" to "کریپتو"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = requireArguments().getString("cat") ?: "summary"
        val slugs = CATALOG[cat] ?: CATALOG["summary"]!!
        view.findViewById<TextView>(R.id.title).text = TITLES[cat] ?: "خلاصه بازار"
        load(view, slugs)

        val swipe = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener { load(view, slugs) }
    }

    private fun load(view: View, slugs: List<String>) {
        val container = view.findViewById<LinearLayout>(R.id.list)
        val swipe = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe)

        viewLifecycleOwner.lifecycleScope.launch {
            val rows = withContext(Dispatchers.IO) { Repo.rowsFor(slugs) }
            container.removeAllViews()
            val ctx = requireContext()
            for (row in rows) {
                val v = LayoutInflater.from(ctx).inflate(R.layout.item_price, container, false)
                v.findViewById<TextView>(R.id.emoji).text = EMOJI[row.slug] ?: "•"
                v.findViewById<TextView>(R.id.name).text = row.title
                v.findViewById<TextView>(R.id.ticker).text = row.slug.uppercase()
                v.findViewById<TextView>(R.id.price).text = row.price

                val pctContainer = v.findViewById<LinearLayout>(R.id.pct_container)
                val pctText = v.findViewById<TextView>(R.id.pct)
                val arrowIcon = v.findViewById<ImageView>(R.id.arrow_icon)

                if (row.pct.isNotEmpty()) {
                    val isUp = row.dir == 1
                    val color = if (isUp) Color.parseColor("#4ADE80") else Color.parseColor("#FF5A5F")
                    val bgColor = if (isUp) Color.parseColor("#264ADE80") else Color.parseColor("#26FF5A5F")
                    pctText.text = if (isUp) "${row.pct}٪" else "-${row.pct}٪"
                    pctText.setTextColor(color)
                    arrowIcon.setImageResource(if (isUp) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
                    pctContainer.setBackgroundColor(bgColor)
                    pctContainer.visibility = View.VISIBLE
                } else {
                    pctContainer.visibility = View.GONE
                }
                container.addView(v)
            }
            swipe.isRefreshing = false
        }
    }
}
