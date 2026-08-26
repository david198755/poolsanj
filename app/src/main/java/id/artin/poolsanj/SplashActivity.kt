package id.artin.poolsanj

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val icon = findViewById<ImageView>(R.id.icon)
        icon.scaleX = 0.8f; icon.scaleY = 0.8f; icon.alpha = 0f
        icon.animate().scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(800).setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }.start()
    }
}
