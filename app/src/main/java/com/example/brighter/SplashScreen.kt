package com.example.brighter

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 4 seconds
                    sleep(3 * 780.toLong())
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                }
            }
        }
        // start thread
        background.start()
    }
}