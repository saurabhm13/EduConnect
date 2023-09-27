package com.example.educonnect.ui.activity.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.material3.TopAppBar
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.ThemeManager
import com.example.educonnect.ThemeManager.getThemePreference
import com.example.educonnect.data.User
import com.example.educonnect.databinding.FragmentProfileBinding
import com.example.educonnect.ui.activity.EditProfileActivity
import com.example.educonnect.ui.activity.LoginActivity
import com.example.educonnect.ui.activity.MainActivity
import com.example.educonnect.ui.viewmodel.MainViewModel
import com.example.educonnect.util.Constants.Companion.EMAIL
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME
import com.example.educonnect.util.Constants.Companion.THEME_DARK
import com.example.educonnect.util.Constants.Companion.THEME_LIGHT
import com.example.educonnect.util.Constants.Companion.THEME_PREFERENCE
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var name: String
    private lateinit var image: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Fetching User data
        viewModel.getUserData()
        viewModel.observeUserData().observe(viewLifecycleOwner) {
            name = it.name
            image = it.image.toString()
            email = it.email.toString()
            addFields()
        }

        // User logout
        binding.logout.setOnClickListener {

            viewModel.logout()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Edit Profile
        binding.profileLayout.setOnClickListener {
            val intoEditProfile = Intent(activity, EditProfileActivity::class.java)
            intoEditProfile.putExtra(NAME, name)
            intoEditProfile.putExtra(EMAIL, email)
            intoEditProfile.putExtra(IMAGE, image)
            startActivity(intoEditProfile)
        }

        // Checking saved theme to toggle switch
        val savedTheme = context?.let { getThemePreference(it) }

        when (savedTheme) {
            THEME_LIGHT -> {
                binding.switchDarkMode.isChecked = false
            }
            THEME_DARK -> {
                binding.switchDarkMode.isChecked = true
            }
        }

        // Observing theme change
        viewModel.themeChanged.observe(viewLifecycleOwner) { themeChanged ->
            if (themeChanged) {
                activity?.recreate() // Recreate the activity to apply the new theme
            }
        }

        // Observe Error
        viewModel.observeError.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }

        // Theme switch
        binding.switchDarkMode.setOnClickListener {
            toggleTheme()
        }

        binding.llDarkMode.setOnClickListener {
            binding.switchDarkMode.toggle()
            toggleTheme()
        }

        return (binding.root)
    }

    private fun addFields() {
        binding.nameProfile.text = name

        if (image.isEmpty() || image == "null") {
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImageProfile)
        }else {
            Glide.with(this)
                .load(image)
                .into(binding.profileImageProfile)
        }

    }

    private fun toggleTheme() {
        viewModel.toggleTheme()
    }

}