package com.sou.hairstyle.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sou.hairstyle.R
import com.sou.hairstyle.common.ImageLoader
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.databinding.ActivitySettingBinding
import com.sou.hairstyle.imaging.IMGEditActivity
import com.sou.hairstyle.imaging.IMGGalleryActivity
import com.sou.hairstyle.imaging.core.util.IMGPermissionUtils
import com.sou.hairstyle.imaging.gallery.model.IMGChooseMode
import com.sou.hairstyle.imaging.gallery.model.IMGImageInfo
import com.sou.hairstyle.network.Request.UpdateUserRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.ui.release.REQ_IMAGE_CHOOSE
import com.sou.hairstyle.ui.release.REQ_IMAGE_EDIT
import java.io.File
import java.util.Arrays
import java.util.UUID

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
//    private val viewModel: SettingViewModel by viewModels()
    private lateinit var viewModel: SettingViewModel
    private var mImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setTitle(R.string.settings)
        setContentView(binding.root)

        binding.uploadImg.setOnClickListener {
            chooseImages()
        }

        binding.genderGroup.setOnCheckedChangeListener { group, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioMale -> {
                    // Handle male selected
                }
                R.id.radioFemale -> {
                    // Handle female selected
                }
            }
        }


        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.userInit.observe(this) { user ->
            binding.nicknameInput.setText(user?.nickname)
            binding.editAgeNumber.setText(user?.age.toString())
            ImageLoader.loadImage(this, user?.avatar, binding.uploadImg)
//            binding.genderInput.setText(user?.gender)
            binding.addressInput.setText(user?.address)
            binding.styleInput.setText(user?.mystyle)
            when (user?.gender) {
                "Male" -> binding.genderGroup.check(R.id.radioMale)
                "Female" -> binding.genderGroup.check(R.id.radioFemale)
                else -> {
                    // If you want to set a default value
                    binding.genderGroup.check(R.id.radioMale)
                    // Or leave both unchecked
                }
            }
        }

        binding.saveButton.setOnClickListener {
           saveSettings()
        }

        binding.buttonDecrement.setOnClickListener {
            // Decrement age logic
            val currentAge = binding.editAgeNumber.text.toString().toIntOrNull() ?: 0
            if (currentAge > 0) { // Assuming the age cannot be less than 0
                val newAge = currentAge - 1
                binding.editAgeNumber.setText(newAge.toString())
            }
        }

        binding.buttonIncrement.setOnClickListener {
            // Increment age logic
            val currentAge = binding.editAgeNumber.text.toString().toIntOrNull() ?: 0
            val newAge = currentAge + 1
            binding.editAgeNumber.setText(newAge.toString())
        }


        viewModel.userSettings.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(this, "个人信息更新成功", Toast.LENGTH_SHORT).show()
                    resource.data?.let {
                        println(it.data)
                        SharedPreferencesManager.getInstance(this).saveUser(it.data)
                        back()
                    }

                    // 可以选择显示某些成功信息
                }
                is Resource.Error -> {

                    // 显示错误信息
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                back()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun back(){
        val returnIntent = Intent()
        // Put extras if needed
        // returnIntent.putExtra("RESULT_KEY", resultData)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
    // Inside SettingActivity class, in the saveSettings() function
    private fun saveSettings() {
        val gender = when (binding.genderGroup.checkedRadioButtonId) {
            R.id.radioMale -> "Male"
            R.id.radioFemale -> "Female"
            else -> "" // Or handle error/empty state
        }


        val nickname = binding.nicknameInput.text.toString()
        val age = binding.editAgeNumber.text.toString()
//        val gender = binding.genderInput.text.toString()
        val address = binding.addressInput.text.toString()
        val my_style = binding.styleInput.text.toString()

        // Create the UpdateUserRequest object
        val updateUserRequest = UpdateUserRequest(
            nickname = nickname,
            gender = gender,
            age = age.toIntOrNull()?: 0,
            address = address,
            mystyle = my_style
        )
//        val params = HashMap<String, String>()
//        params["nickname"] = nickname
//        params["gender"] = gender
//        params["age"] = age
//        params["address"] = address
//        params["mystyle"] = my_style
        // Call the updateUser method in the ViewModel
        viewModel.updateUser(updateUserRequest, mImageFile)
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
        binding.uploadImg.setImageURI(Uri.fromFile(mImageFile))
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

}
