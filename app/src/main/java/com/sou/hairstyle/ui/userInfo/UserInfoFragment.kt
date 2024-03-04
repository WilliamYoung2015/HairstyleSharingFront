package com.sou.hairstyle.ui.userInfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sou.hairstyle.common.SharedPreferencesManager
import com.sou.hairstyle.databinding.ActivityUserinfoBinding
import com.sou.hairstyle.ui.login.LoginActivity
import com.sou.hairstyle.ui.setting.SettingActivity


class UserInfoFragment : Fragment() {

    companion object {
        private const val USER_INFO_REQUEST_CODE = 1
    }
    interface FragmentUserInfoListener {
        fun navigateToActivityAndFinish(intent: Intent)
    }

    private lateinit var userInfoViewModel: UserInfoViewModel
    private var userInfoListener: FragmentUserInfoListener? = null

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private var _binding: ActivityUserinfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userInfoListener = context as? FragmentUserInfoListener
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userInfoViewModel =
            ViewModelProvider(this).get(UserInfoViewModel::class.java)

        _binding = ActivityUserinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root



        // Set up the user info from ViewModel
        userInfoViewModel.userData.observe(viewLifecycleOwner) { user ->
            println("//////////////")
            println(user)
            binding.apply {
                infoNicknameText.text = if (user?.nickname.isNullOrEmpty()) "空" else user?.nickname
                infoGenderText.text = if (user?.gender.isNullOrEmpty()) "空" else user?.gender
                when(user?.gender){
                    "Male" -> infoGenderText.text = "男"
                    "Female" -> infoGenderText.text = "女"
                    else -> {
                        infoGenderText.text = ""
                    }
                }
                infoAgeText.text = user?.age?.toString() ?: "空"
                infoAddressText.text = if (user?.address.isNullOrEmpty()) "空" else user?.address
                infoStyleText.text = if (user?.mystyle.isNullOrEmpty()) "空" else user?.mystyle

                // If you have an avatar image, set it here
                // Example: avatarImage.setImageBitmap(user.avatarBitmap)
            }
        }
        val user = sharedPreferencesManager.getUser()
        if (user != null) {
            userInfoViewModel.initUserInfo(user)
        }

        // Logout button logic
        binding.logoutButton.setOnClickListener {
//            notificationsViewModel.logout()
//            sharedPreferencesManager.clearUser()
            // 创建要跳转到的Activity的Intent
            val intent = Intent(activity, LoginActivity::class.java)

            // 使用接口中定义的方法来进行跳转并关闭Activity
            userInfoListener?.navigateToActivityAndFinish(intent)
            // Handle logout, e.g., navigate back to the login screen
        }

        // Settings button logic
        binding.settingsButton.setOnClickListener {
            // Handle settings action
            val intent = Intent(activity, SettingActivity::class.java)
            startActivityForResult(intent, USER_INFO_REQUEST_CODE)

//            val intent = Intent(activity, HairstyleInfoActivity::class.java)
//            intent.putExtra("HAIR_INFO_ITEM", item) // Make sure HairInfoItem is Serializable or Parcelable
//            startActivityForResult(intent, HAIRSTYLE_INFO_REQUEST_CODE)
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == USER_INFO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val user = sharedPreferencesManager.getUser()
            if (user != null) {
                userInfoViewModel.initUserInfo(user)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        userInfoListener = null
    }
}