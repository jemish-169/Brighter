package com.tanxe.brighter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tanxe.brighter.databinding.ActivitySecondBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_second.*

class Second_Activity : AppCompatActivity() {
    lateinit var activitySecondBinding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySecondBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(activitySecondBinding.root)
        val imgRes = intent.getParcelableExtra<Uri>("ImageUri")
        activitySecondBinding.imageView3.setImageURI(imgRes)

        Snackbar.make(
            activitySecondBinding.root,
            "Image saved successfully!",
            Snackbar.ANIMATION_MODE_SLIDE
        ).show()

        backButton.setOnClickListener {
            finish()
        }
        sharebutton.setOnClickListener {

            val builder = AlertDialog.Builder(this@Second_Activity)
            val view: View =
                LayoutInflater.from(this@Second_Activity).inflate(R.layout.share_layout, null)
            builder.setView(view)
            val dialogS = builder.create()
            dialogS.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogS.setCancelable(false)
            dialogS.show()

            val background: Thread = object : Thread() {
                override fun run() {
                    try {
                        // Thread will sleep for 4 seconds
                        sleep(3 * 1000.toLong())
                        dialogS.dismiss()
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey! Check out This image, edited with Brighter app."
                        )
                        intent.putExtra(Intent.EXTRA_STREAM, imgRes)
                        intent.type = "image/jpeg"
                        startActivity(Intent.createChooser(intent, "Share Via:"))
                    } catch (_: Exception) {
                    }
                }
            }
            // start thread
            background.start()
        }
        mail.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:elite.apps169@gmail.com")))
        }
        linkedin.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jemish-khunt/")))
        }
        instagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/elite_apps_/")))
        }
    }
}