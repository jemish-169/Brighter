package com.example.brighter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brighter.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*

class Second_Activity : AppCompatActivity() {
    lateinit var activitySecondBinding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySecondBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(activitySecondBinding.root)
        val imgRes = intent.getParcelableExtra<Uri>("ImageUri")
        activitySecondBinding.imageView3.setImageURI(imgRes)
        backButton.setOnClickListener {
            finish()
        }
        sharebutton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "share via.")
                type = "*/*"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}