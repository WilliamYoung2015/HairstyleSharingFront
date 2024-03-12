package com.sou.hairstyle.ui.release

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sou.hairstyle.R
import com.sou.hairstyle.imaging.IMGEditActivity
import com.sou.hairstyle.imaging.IMGGalleryActivity
import com.sou.hairstyle.imaging.core.util.IMGPermissionUtils
import com.sou.hairstyle.imaging.gallery.model.IMGChooseMode
import com.sou.hairstyle.imaging.gallery.model.IMGImageInfo
import com.sou.hairstyle.network.Request.AddHairStyleRequest
import com.sou.hairstyle.network.Resource
import com.sou.hairstyle.ui.home.HomeFragment
import com.sou.hairstyle.widget.FlowLayout
import java.io.File
import java.util.UUID

const val REQ_IMAGE_EDIT = 1
const val REQ_IMAGE_CHOOSE = 2
class AddHairStyleActivity: AppCompatActivity() {
    private val REQ_STORAGE = 1
    private lateinit var uploadImg: ImageView
    private lateinit var description: EditText
    private lateinit var season: Spinner
    private lateinit var sex: Spinner
    private lateinit var age: EditText
    private lateinit var tagFlow: FlowLayout
    private lateinit var buttonRelease: AppCompatButton
    private lateinit var viewModel: AddHairStyleViewModel
    private var mImageFile: File? = null
    private var selSeason = ""  //季节
    private var selSex = ""     //性别
    private val tags = arrayListOf("超美", "初春", "春夏", "新风", "初秋", "秋冬")
    private val selTags = arrayListOf<String>()
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "发布新发型"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.black_overlay))
        setContentView(R.layout.activity_release_hair_style)
        viewModel = ViewModelProvider(this)[AddHairStyleViewModel::class.java]
        viewModel.uploadStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(this, "发布成功", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
                is Resource.Error -> {
                    // 显示错误信息
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        uploadImg = findViewById(R.id.uploadImg)
        description = findViewById(R.id.description)
        season = findViewById(R.id.season)
        age = findViewById(R.id.age)
        sex = findViewById(R.id.sex)
        tagFlow = findViewById(R.id.tagFlow)
        uploadImg.setOnClickListener {
            chooseImages()
        }
        buttonRelease = findViewById(R.id.buttonRelease)
        buttonRelease.setOnClickListener {
            if (mImageFile == null) {
                Toast.makeText(this, "请选择发型图片", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val desc = description.text.toString().trim()
            if (desc.isNullOrEmpty()) {
                Toast.makeText(this, "请填写描述", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selSeason.isNullOrEmpty()) {
                Toast.makeText(this, "请选择季节", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textAge = age.text.toString().trim()
            if (textAge.isNullOrEmpty()) {
                Toast.makeText(this, "请输入年龄", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selSex.isNullOrEmpty()) {
                Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selTags.isNullOrEmpty()) {
                Toast.makeText(this, "请选择标签", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val strTags = selTags.toString().replace("[", "").replace("]", "")
            val params = HashMap<String, String>()
            params["desc"] = desc
            params["title"] = selSeason
            params["age"] = textAge
            params["sex"] = selSex
            params["tags"] = strTags
            viewModel.addHairStyle(params, arrayListOf(mImageFile!!))
        }
        initData()
        initTags()
    }

    private fun initData(){
        val data = arrayListOf("请选择季节", "春季", "夏季", "秋季", "冬季")
        val adapter = ArrayAdapter(this, R.layout.item_spinner, data)
        season.adapter = adapter
        season.setSelection(0)
        season.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selSeason = if (position == 0 || position >= data.size){
                    ""
                } else {
                    data[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val sexArr = arrayListOf("请选择性别", "男", "女", "其他")
        val sexAdapter = ArrayAdapter(this, R.layout.item_spinner, sexArr)
        sex.adapter = sexAdapter
        sex.setSelection(0)
        sex.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selSex = when(position) {
                    1 -> "Male"
                    2 -> "Female"
                    3 -> "Other"
                    else -> ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initTags(){
        tagFlow.removeAllViews()
        tags.forEach {
            val checkBox = LayoutInflater.from(this).inflate(
                R.layout.add_hair_style_tag_select,
                tagFlow,
                false
            ) as CheckBox
            checkBox.text = it
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selTags.add(it)
                } else {
                    selTags.remove(it)
                }
//                description.setText(selTags.toString().replace("[", "").replace("]", ""))
            }
            tagFlow.addView(checkBox)
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

    override fun onBackPressed() {
        super.onBackPressed()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(HomeFragment.HAIRSTYLE_INFO_REQUEST_CODE))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 当点击返回按钮时，结束当前活动
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}