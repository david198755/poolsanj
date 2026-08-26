package id.artin.poolsanj

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val glow = findViewById<View>(R.id.glow)
        val icon = findViewById<View>(R.id.icon)
        val title = findViewById<View>(R.id.title)
        val subtitle = findViewById<View>(R.id.subtitle)
        val dots = findViewById<View>(R.id.dots)

        // Pulse glow
        ObjectAnimator.ofFloat(glow, "alpha", 0f, 1f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(glow, "scaleX", 0.9f, 1.1f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(glow, "scaleY", 0.9f, 1.1f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Fade-in icon
        icon.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(800)
            .setStartDelay(100)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()

        // Fade-in title
        title.animate().alpha(1f).translationY(0f)
            .setDuration(800)
            .setStartDelay(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Fade-in subtitle
        subtitle.animate().alpha(1f).translationY(0f)
            .setDuration(800)
            .setStartDelay(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Fade-in dots
        dots.animate().alpha(1f)
            .setDuration(600)
            .setStartDelay(500)
            .start()

        // Bounce dots
        bounceDot(findViewById(R.id.dot1), 0)
        bounceDot(findViewById(R.id.dot2), 160)
        bounceDot(findViewById(R.id.dot3), 320)

        // Navigate after 3.5s
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 3500)
    }

    private fun bounceDot(view: View, delay: Long) {
        view.animate()
            .scaleY(0.3f)
            .setDuration(350)
            .setStartDelay(delay)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                view.animate()
                    .scaleY(1f)
                    .setDuration(350)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        if (!isFinishing) bounceDot(view, 0)
                    }
                    .start()
            }
            .start()
    }
}
