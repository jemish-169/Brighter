package com.tanxe.brighter

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.tanxe.brighter.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    val RESULT_CODE = 1
    val CAMERA_CODE = 2
    val GALLERY_CODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        requestPermission()
        btn_start.setOnClickListener {
            var builder = AlertDialog.Builder(this@MainActivity)
            var view: View =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.dialouge_box, null)
            builder.setView(view)
            var dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()

            var cameraButton: ImageView = view.findViewById(R.id.btn_camera)
            var galleryButton: ImageView = view.findViewById(R.id.btn_gallery)


            galleryButton.setOnClickListener {
                dialog.dismiss()
                Intent(Intent.ACTION_GET_CONTENT).also {
                    it.type = "image/*"
                    startActivityForResult(it, GALLERY_CODE)
                }
            }
            cameraButton.setOnClickListener {
                dialog.dismiss()
                var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_CODE)

            }
        }
    }

    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        var view: View =
            LayoutInflater.from(this).inflate(R.layout.exit_app, null)
        builder.setView(view)
        var dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()

        var btnNo: TextView = view.findViewById(R.id.btn_no)
        var btnYes: TextView = view.findViewById(R.id.btn_yes)

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == GALLERY_CODE && (data?.data != null)) {

            val uri: Uri? = data?.data
            var dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
            dsPhotoEditorIntent.data = uri
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                "Brighter"
            )
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
                R.color.luxurious
            )
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
                R.color.purple_200
            )

            val toolsToHide = intArrayOf(
                DsPhotoEditorActivity.TOOL_TEXT,
                DsPhotoEditorActivity.TOOL_FRAME,
                DsPhotoEditorActivity.TOOL_PIXELATE,
            )

            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                toolsToHide
            )

            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_CODE) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path =
                MediaStore.Images.Media.insertImage(this.contentResolver, photo, "Title", null)

            val uri: Uri = Uri.parse(path)

            var dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
            dsPhotoEditorIntent.data = uri
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                "Brighter"
            )
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
                R.color.luxurious
            )
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
                R.color.purple_200
            )

            val toolsToHide = intArrayOf(
                DsPhotoEditorActivity.TOOL_TEXT,
                DsPhotoEditorActivity.TOOL_FRAME,
                DsPhotoEditorActivity.TOOL_PIXELATE,
            )

            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                toolsToHide
            )
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
        }
        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val uri = data?.data
            Intent(this, Second_Activity::class.java).also {
                it.putExtra("ImageUri", uri)
                startActivity(it)
            }
        }
    }


    private fun hasWriteExternalStoragePermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasCameraPermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasWriteExternalStoragePermission()) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!hasCameraPermission()) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 50)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
