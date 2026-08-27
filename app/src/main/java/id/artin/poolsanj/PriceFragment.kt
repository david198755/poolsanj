package id.artin.poolsanj

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PriceFragment : Fragment() {
    companion object {
        fun newInstance(cat: String) = PriceFragment().apply {
            arguments = bundleOf("cat" to cat)
        }
    }

    private val CATALOG = mapOf(
        "summary" to listOf(
            Triple("price_dollar_rl", "🇺🇸", "USD/IRR"),
            Triple("price_eur", "🇪🇺", "EUR/IRR"),
            Triple("sekee", "🪙", "Emami Coin"),
            Triple("bahar_azadi", "🪙", "Bahar Azadi"),
            Triple("geram18", "✨", "Gold 18k"),
            Triple("mesghal", "⚖️", "Mesghal"),
            Triple("ons", "🌍", "XAU/USD"),
            Triple("crypto-bitcoin", "₿", "BTC/USD"),
            Triple("crypto-tether", "₮", "USDT/IRR"),
            Triple("crypto-ethereum", "⟠", "ETH/USD")
        ),
        "arz" to listOf(
            Triple("price_dollar_rl", "🇺🇸", "USD/IRR"),
            Triple("price_eur", "🇪🇺", "EUR/IRR"),
            Triple("price_gbp", "🇬🇧", "GBP/IRR"),
            Triple("price_aed", "🇦🇪", "AED/IRR"),
            Triple("price_try", "🇹🇷", "TRY/IRR"),
            Triple("price_cny", "🇨🇳", "CNY/IRR"),
            Triple("price_jpy", "🇯🇵", "JPY/IRR"),
            Triple("price_inr", "🇮🇳", "INR/IRR"),
            Triple("price_krw", "🇰🇷", "KRW/IRR"),
            Triple("price_sar", "🇸🇦", "SAR/IRR"),
            Triple("price_chf", "🇨🇭", "CHF/IRR"),
            Triple("price_cad", "🇨🇦", "CAD/IRR"),
            Triple("price_aud", "🇦🇺", "AUD/IRR"),
            Triple("price_sek", "🇸🇪", "SEK/IRR"),
            Triple("price_nok", "🇳🇴", "NOK/IRR"),
            Triple("price_dkk", "🇩🇰", "DKK/IRR"),
            Triple("price_rub", "🇷🇺", "RUB/IRR"),
            Triple("price_try_steel", "🇹🇷", "TRY Steel"),
            Triple("price_iqd", "🇮🇶", "IQD/IRR"),
            Triple("price_syp", "🇸🇾", "SYP/IRR"),
            Triple("price_afn", "🇦🇫", "AFN/IRR"),
            Triple("price_pkr", "🇵🇰", "PKR/IRR"),
            Triple("price_inr_steel", "🇮🇳", "INR Steel"),
            Triple("price_kwd", "🇰🇼", "KWD/IRR"),
            Triple("price_bhd", "🇧🇭", "BHD/IRR"),
            Triple("price_qar", "🇶🇦", "QAR/IRR"),
            Triple("price_omr", "🇴🇲", "OMR/IRR"),
            Triple("price_lbp", "🇱🇧", "LBP/IRR"),
            Triple("price_egp", "🇪🇬", "EGP/IRR"),
            Triple("price_php", "🇵🇭", "PHP/IRR"),
            Triple("price_thb", "🇹🇭", "THB/IRR"),
            Triple("price_myr", "🇲🇾", "MYR/IRR"),
            Triple("price_idr", "🇮🇩", "IDR/IRR"),
            Triple("price_vnd", "🇻🇳", "VND/IRR"),
            Triple("price_hkd", "🇭🇰", "HKD/IRR"),
            Triple("price_twd", "🇹🇼", "TWD/IRR"),
            Triple("price_sgd", "🇸🇬", "SGD/IRR"),
            Triple("price_nzd", "🇳🇿", "NZD/IRR"),
            Triple("price_zar", "🇿🇦", "ZAR/IRR"),
            Triple("price_kzt", "🇰🇿", "KZT/IRR"),
            Triple("price_uzs", "🇺🇿", "UZS/IRR"),
            Triple("price_mmk", "🇲🇲", "MMK/IRR"),
            Triple("price_npr", "🇳🇵", "NPR/IRR"),
            Triple("price_lkr", "🇱🇰", "LKR/IRR"),
            Triple("price_bdt", "🇧🇩", "BDT/IRR"),
            Triple("price_mnt", "🇲🇳", "MNT/IRR"),
            Triple("price_geg", "🇬🇪", "GEG/IRR"),
            Triple("price_azn", "🇦🇿", "AZN/IRR"),
            Triple("price_amd", "🇦🇲", "AMD/IRR"),
            Triple("price_gel", "🇬🇪", "GEL/IRR")
        ),
        "tala" to listOf(
            Triple("geram18", "✨", "Gold 18k / Gram"),
            Triple("mesghal", "⚖️", "Mesghal"),
            Triple("ons", "🌍", "XAU/USD"),
            Triple("sekee", "🪙", "Emami Coin"),
            Triple("bahar_azadi", "🪙", "Bahar Azadi"),
            Triple("nim", "🪙", "Half Coin"),
            Triple("rob", "🪙", "Quarter Coin"),
            Triple("geram24", "✨", "Gold 24k"),
            Triple("melting_gold", "🔶", "Melted Gold"),
            Triple("melting_geram18", "🔶", "Melted 18k"),
            Triple("sekeb", "🪙", "Bank Coin"),
            Triple("sekebaha", "🪙", "Free Coin")
        ),
        "crypto" to listOf(
            Triple("crypto-bitcoin", "₿", "BTC/USD"),
            Triple("crypto-tether", "₮", "USDT/IRR"),
            Triple("crypto-ethereum", "⟠", "ETH/USD"),
            Triple("crypto-binancecoin", "◆", "BNB/USD"),
            Triple("crypto-solana", "◎", "SOL/USD"),
            Triple("crypto-xrp", "✕", "XRP/USD"),
            Triple("crypto-dogecoin", "🐕", "DOGE/USD"),
            Triple("crypto-cardano", "◇", "ADA/USD"),
            Triple("crypto-tron", "T", "TRX/USD"),
            Triple("crypto-avalanche", "🔺", "AVAX/USD"),
            Triple("crypto-dot", "●", "DOT/USD"),
            Triple("crypto-chainlink", "⬡", "LINK/USD"),
            Triple("crypto-polygon", "⬡", "MATIC/USD"),
            Triple("crypto-shibainu", "🐕", "SHIB/USD"),
            Triple("crypto-litecoin", "Ł", "LTC/USD"),
            Triple("crypto-uniswap", "🦄", "UNI/USD"),
            Triple("crypto-stellar", "★", "XLM/USD")
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_prices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = requireArguments().getString("cat") ?: "summary"
        load(view, cat, false)

        val swipe = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener { load(view, cat, true) }
    }

    private fun load(view: View, cat: String, force: Boolean) {
        val items = CATALOG[cat] ?: CATALOG["summary"]!!
        val container = view.findViewById<LinearLayout>(R.id.list)
        val swipe = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe)

        viewLifecycleOwner.lifecycleScope.launch {
            val rows = withContext(Dispatchers.IO) {
                items.map { (slug, emoji, ticker) ->
                    val info = PriceRepo.fetch(slug, force)
                    Triple(emoji, ticker, info)
                }
            }
            container.removeAllViews()
            val ctx = requireContext()
            for ((emoji, ticker, info) in rows) {
                val v = LayoutInflater.from(ctx).inflate(R.layout.item_price, container, false)

                v.findViewById<TextView>(R.id.emoji).text = emoji
                v.findViewById<TextView>(R.id.name).text = info?.name ?: ticker
                v.findViewById<TextView>(R.id.ticker).text = ticker
                v.findViewById<TextView>(R.id.price).text = info?.price ?: "..."

                val pctContainer = v.findViewById<LinearLayout>(R.id.pct_container)
                val pctText = v.findViewById<TextView>(R.id.pct)
                val arrowIcon = v.findViewById<ImageView>(R.id.arrow_icon)

                if (info != null && info.pct.isNotEmpty()) {
                    val isUp = info.arrow == 1
                    val color = if (isUp) Color.parseColor("#4ADE80") else Color.parseColor("#FF5A5F")
                    val bgColor = if (isUp) Color.parseColor("#264ADE80") else Color.parseColor("#26FF5A5F")

                    pctText.text = if (isUp) "${info.pct}٪" else "-${info.pct}٪"
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
