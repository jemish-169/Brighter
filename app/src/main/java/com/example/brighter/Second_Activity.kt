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
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey! Check out This image, edited with Brighter app.")
            intent.putExtra(Intent.EXTRA_STREAM,imgRes)
            intent.type="image/jpeg"
            startActivity(Intent.createChooser(intent,"Share Via:"))
        }
    }
}