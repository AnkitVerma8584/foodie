package com.harbhajan.foodie.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.printLog

class WelcomeActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var anim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        logo = findViewById(R.id.imgLogo)

        Handler(Looper.getMainLooper()!!).postDelayed({
            val mIntent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(mIntent)
            finish()
        }, 4000)
        anim = AnimationUtils.loadAnimation(this, R.anim.animation)
        logo.startAnimation(anim)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {

            it.printLog("FCM")
        }
    }
}