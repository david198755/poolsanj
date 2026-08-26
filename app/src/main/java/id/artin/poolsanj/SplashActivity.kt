package id.artin.poolsanj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val glow = findViewById<FrameLayout>(R.id.glow)
        val icon = findViewById<ImageView>(R.id.icon)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.subtitle)
        val dots = findViewById<LinearLayout>(R.id.dots)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        // Pulse glow
        val glowAnim = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
        glow.startAnimation(glowAnim)

        // Staggered fade-in-up
        val fadeUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)

        icon.postDelayed({
            val a1 = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
            icon.alpha = 1f
            icon.startAnimation(a1)
        }, 100)

        title.postDelayed({
            val a2 = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
            title.alpha = 1f
            title.startAnimation(a2)
        }, 300)

        subtitle.postDelayed({
            val a3 = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
            subtitle.alpha = 1f
            subtitle.startAnimation(a3)
        }, 300)

        dots.postDelayed({
            val a4 = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
            dots.alpha = 1f
            dots.startAnimation(a4)

            // Bounce dots
            val b1 = AnimationUtils.loadAnimation(this, R.anim.bounce_dot)
            b1.startOffset = 0
            dot1.postDelayed({ dot1.animate().alpha(0.4f).setDuration(350).withEndAction {
                dot1.animate().alpha(1f).setDuration(350).start()
            }.start() }
            , 0)

            val b2 = AnimationUtils.loadAnimation(this, R.anim.bounce_dot)
            b2.startOffset = 160
            dot2.postDelayed({ dot2.animate().alpha(0.7f).setDuration(350).withEndAction {
                dot2.animate().alpha(1f).setDuration(350).start()
            }.start() }, 160)

            val b3 = AnimationUtils.loadAnimation(this, R.anim.bounce_dot)
            b3.startOffset = 320
            dot3.postDelayed({ dot3.animate().alpha(1f).setDuration(350).withEndAction {
                dot3.animate().alpha(0.4f).setDuration(350).start()
            }.start() }, 320)

        }, 500)

        // Navigate after 3.5s
        title.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3500)
    }
}
