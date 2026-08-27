package id.artin.poolsanj

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

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

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(this)

        if (savedInstanceState == null) show(PriceFragment.newInstance("summary"))
    }

    override fun onResume() {
        super.onResume()
        handler.post(timeUpdater)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdater)
    }

    private fun show(f: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.nav_conv -> { show(ConvFragment()); true }
        R.id.nav_summary -> { show(PriceFragment.newInstance("summary")); true }
        R.id.nav_arz -> { show(PriceFragment.newInstance("arz")); true }
        R.id.nav_tala -> { show(PriceFragment.newInstance("tala")); true }
        R.id.nav_crypto -> { show(PriceFragment.newInstance("crypto")); true }
        else -> false
    }
}
