package id.artin.poolsanj

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) show(PriceFragment.newInstance("summary"))
    }

    private fun show(f: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit()

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.bottom_nav_conv -> { show(ConvFragment()); true }
        R.id.bottom_nav_summary -> { show(PriceFragment.newInstance("summary")); true }
        R.id.bottom_nav_arz -> { show(PriceFragment.newInstance("arz")); true }
        R.id.bottom_nav_tala -> { show(PriceFragment.newInstance("tala")); true }
        R.id.bottom_nav_crypto -> { show(PriceFragment.newInstance("crypto")); true }
        else -> false
    }
}
