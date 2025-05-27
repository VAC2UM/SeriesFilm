package com.example.seriesfilm.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seriesfilm.ApiClient
import com.example.seriesfilm.AuthModels
import com.example.seriesfilm.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var etLogin: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var questionButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val userNameKey = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        loginButton = findViewById(R.id.loginButton)
        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        questionButton = findViewById(R.id.questionButton)
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            val login = etLogin.text.toString()
            val password = etPassword.text.toString()

            if (validateInput(login, password)) {
                loginUser(login, password)
            }
        }

        questionButton.setOnClickListener {
            startActivity(Intent(this@Login, SignUp::class.java))
        }
    }

    private fun validateInput(login: String, password: String): Boolean {
        return when {
            login.isEmpty() -> {
                Toast.makeText(this@Login, "Введите логин", Toast.LENGTH_SHORT).show()
                false
            }

            password.isEmpty() -> {
                Toast.makeText(this@Login, "Введите пароль", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }

    private fun loginUser(
        login: String,
        password: String,
    ) {
        val call =
            ApiClient.authApi.login(
                AuthModels.LoginRequest(
                    login,
                    password,
                ),
            )
        call.enqueue(
            object : Callback<AuthModels.AuthResponse> {
                override fun onResponse(
                    call: Call<AuthModels.AuthResponse>,
                    response: Response<AuthModels.AuthResponse>,
                ) {
                    if (response.isSuccessful) {
                        saveUserData(login)
                        Toast.makeText(this@Login, "Успешный вход!", Toast.LENGTH_SHORT).show()
                        startMainActivity()
                    } else {
                        Toast.makeText(this@Login, "Ошибка входа!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<AuthModels.AuthResponse>,
                    t: Throwable,
                ) {
                    Toast.makeText(this@Login, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            },
        )
    }

    private fun saveUserData(username: String) {
        sharedPreferences.edit()
            .putString(userNameKey, username)
            .apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
