package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.educonnect.databinding.ActivitySignupBinding
import com.example.educonnect.ui.viewmodel.LoginSignupViewModel

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    private val viewModel: LoginSignupViewModel by viewModels()

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var conformPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnSignup.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            name = binding.nameSignup.editText?.text.toString()
            email = binding.emailSignup.editText?.text.toString()
            password = binding.passwordSignup.editText?.text.toString()
            conformPassword = binding.conformPassSignup.editText?.text.toString()

            if (name.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Name", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else if (email.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else if (password.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else if (conformPassword.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Conform Password", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else if (password != conformPassword) {
                Toast.makeText(this, "Password don't match", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else if (password!!.count() < 6) {
                Toast.makeText(this, "Password should be more than 8 digits", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }else {

                viewModel.registerUser(email!!, password!!, name!!)
                viewModel.authCallback = {
                    val intoMain = Intent(this, MainActivity::class.java)
                    startActivity(intoMain)
                    finish()
                    binding.progressBar.visibility = View.GONE
                }
            }

            viewModel.verificationError.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}