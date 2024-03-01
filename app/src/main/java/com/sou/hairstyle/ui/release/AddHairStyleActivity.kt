package com.sou.hairstyle.ui.release

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sou.hairstyle.R
import com.sou.hairstyle.imaging.IMGEditActivity
import com.sou.hairstyle.imaging.IMGGalleryActivity
import com.sou.hairstyle.imaging.core.util.IMGPermissionUtils
import com.sou.hairstyle.imaging.gallery.model.IMGChooseMode
import com.sou.hairstyle.imaging.gallery.model.IMGImageInfo
import java.io.File
import java.util.Arrays
import java.util.UUID

const val REQ_IMAGE_EDIT = 1
const val REQ_IMAGE_CHOOSE = 2
class AddHairStyleActivity: AppCompatActivity() {
    private val REQ_STORAGE = 1
    private lateinit var uploadImg: ImageView
    private lateinit var description: EditText
    private lateinit var season: Spinner
    private lateinit var age: EditText
    private var mImageFile: File? = null
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "发布新发型"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.black_overlay))
        setContentView(R.layout.activity_release_hair_style)
        uploadImg = findViewById(R.id.uploadImg)
        description = findViewById(R.id.description)
        season = findViewById(R.id.season)
        age = findViewById(R.id.age)
        uploadImg.setOnClickListener {
//            if (IMGPermissionUtils.isPermissionGranted(
//                    this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            ) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    REQ_STORAGE
//                )
//            } else {
                chooseImages()
//            }
        }
    }

    private fun chooseImages(){
        startActivityForResult(
            IMGGalleryActivity.newIntent(this, IMGChooseMode.Builder()
                .setSingleChoose(true)
                .build()),
            REQ_IMAGE_CHOOSE
        )
    }
    private fun onChooseImages(images: List<IMGImageInfo>?) {
        if (!images.isNullOrEmpty()) {
            val image = images[0]
            mImageFile = File(cacheDir, UUID.randomUUID().toString() + ".jpg")

            startActivityForResult(
                Intent(this, IMGEditActivity::class.java)
                    .putExtra(IMGEditActivity.EXTRA_IMAGE_URI, image.uri)
                    .putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mImageFile?.absolutePath),
                REQ_IMAGE_EDIT
            )
        }
    }

    private fun onImageEditDone() {
        uploadImg.setImageURI(Uri.fromFile(mImageFile))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("+++++++", Arrays.toString(grantResults))
        if (!IMGPermissionUtils.isPermissionsGranted(this, permissions)) {
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请授权存储权限")
                .setPositiveButton(
                    "去授权"
                ) { dialogInterface, i ->
                    startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(
                                Uri.fromParts(
                                    "package",
                                    applicationContext.packageName,
                                    null
                                )
                            )
                    )
                }
                .setNegativeButton("取消", null)
                .show()
        } else {
            chooseImages()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_IMAGE_CHOOSE -> {
                if (resultCode == Activity.RESULT_OK) {
                    onChooseImages(IMGGalleryActivity.getImageInfos(data))
                }
            }
            REQ_IMAGE_EDIT -> {
                if (resultCode == Activity.RESULT_OK) {
                    onImageEditDone()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}