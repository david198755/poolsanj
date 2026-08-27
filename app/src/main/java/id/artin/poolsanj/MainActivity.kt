package id.artin.poolsanj

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var activeTab: LinearLayout? = null
    private var activeIcon: ImageView? = null
    private var activeText: TextView? = null

    private val timeUpdater = object : Runnable {
        override fun run() {
            val tv = findViewById<TextView>(R.id.header_time)
            tv?.text = "آخرین بروزرسانی: ${timeFormat.format(Date())}"
            handler.postDelayed(this, 30000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup custom bottom nav
        setupBottomNav()

        if (savedInstanceState == null) {
            selectTab(findViewById(R.id.nav_summary), "summary")
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_conv).setOnClickListener {
            selectTab(it as LinearLayout, "conv")
        }
        findViewById<LinearLayout>(R.id.nav_summary).setOnClickListener {
            selectTab(it as LinearLayout, "summary")
        }
        findViewById<LinearLayout>(R.id.nav_arz).setOnClickListener {
            selectTab(it as LinearLayout, "arz")
        }
        findViewById<LinearLayout>(R.id.nav_tala).setOnClickListener {
            selectTab(it as LinearLayout, "tala")
        }
        findViewById<LinearLayout>(R.id.nav_crypto).setOnClickListener {
            selectTab(it as LinearLayout, "crypto")
        }
    }

    private fun selectTab(tab: LinearLayout, category: String) {
        // Reset previous active tab
        activeTab?.setBackgroundResource(R.drawable.bg_nav_item)
        activeIcon?.setColorFilter(0xFFC6C6CC.toInt())
        activeText?.setTextColor(0xFFC6C6CC.toInt())

        // Set new active tab
        tab.setBackgroundResource(R.drawable.bg_nav_item_active)
        val iconResId = when (category) {
            "conv" -> R.id.icon_conv
            "summary" -> R.id.icon_summary
            "arz" -> R.id.icon_arz
            "tala" -> R.id.icon_tala
            "crypto" -> R.id.icon_crypto
            else -> R.id.icon_summary
        }
        val textResId = when (category) {
            "conv" -> R.id.text_conv
            "summary" -> R.id.text_summary
            "arz" -> R.id.text_arz
            "tala" -> R.id.text_tala
            "crypto" -> R.id.text_crypto
            else -> R.id.text_summary
        }
        val icon = findViewById<ImageView>(iconResId)
        val text = findViewById<TextView>(textResId)
        icon.setColorFilter(0xFFF7BE36.toInt())
        text.setTextColor(0xFFF7BE36.toInt())

        activeTab = tab
        activeIcon = icon
        activeText = text

        // Load fragment
        when (category) {
            "conv" -> show(ConvFragment())
            "summary" -> show(PriceFragment.newInstance("summary"))
            "arz" -> show(PriceFragment.newInstance("arz"))
            "tala" -> show(PriceFragment.newInstance("tala"))
            "crypto" -> show(PriceFragment.newInstance("crypto"))
        }
    }

    private fun show(f: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()

    override fun onResume() {
        super.onResume()
        handler.post(timeUpdater)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdater)
    }
}
