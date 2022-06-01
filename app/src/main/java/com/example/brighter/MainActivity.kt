package com.example.brighter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.example.brighter.databinding.ActivityMainBinding
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

        ivEditPhoto.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, GALLERY_CODE)
            }
        }
        ivTakePhoto.setOnClickListener {
            var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_CODE)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_CODE && (data?.data != null)) {

            val uri: Uri? = data?.data
            var dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
            dsPhotoEditorIntent.data = uri
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                "Brighter"
            )

            val toolsToHide = intArrayOf(
                DsPhotoEditorActivity.TOOL_WARMTH,
                DsPhotoEditorActivity.TOOL_SATURATION,
                DsPhotoEditorActivity.TOOL_VIGNETTE,
                DsPhotoEditorActivity.TOOL_EXPOSURE
            )

            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                toolsToHide
            )
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
            val uri = data?.data
            Intent(this, Second_Activity::class.java).also {
                it.putExtra("ImageUri", uri)
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                startActivity(it)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CODE) {
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

            val toolsToHide = intArrayOf(
                DsPhotoEditorActivity.TOOL_WARMTH,
                DsPhotoEditorActivity.TOOL_SATURATION,
                DsPhotoEditorActivity.TOOL_VIGNETTE,
                DsPhotoEditorActivity.TOOL_EXPOSURE
            )

            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                toolsToHide
            )
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
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
