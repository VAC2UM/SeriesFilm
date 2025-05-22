package com.example.seriesfilm.activities

import android.content.Intent
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
    lateinit var loginButton: Button
    lateinit var etLogin: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var questionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        questionButton = findViewById(R.id.questionButton)

        loginButton.setOnClickListener {
            val login = etLogin.text.toString()
            val password = etPassword.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty()) {
                loginUser(login, password)
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
        questionButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
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
                        Toast.makeText(this@Login, "Успешный вход!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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
}
