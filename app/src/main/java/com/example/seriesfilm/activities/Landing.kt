package com.example.seriesfilm.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.seriesfilm.R

class Landing : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val userNameKey = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        val savedUsername = sharedPreferences.getString(userNameKey, null)
        if (savedUsername != null) {
            startMainActivity()
        }

        initViews()

        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        loginButton = findViewById(R.id.login_btn)
        signUpButton = findViewById(R.id.sign_up)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
