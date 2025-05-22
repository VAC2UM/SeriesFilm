package com.example.seriesfilm.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class SignUp : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var etLogin: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etEmail: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        registerBtn = findViewById(R.id.registerBtn)

        registerBtn.setOnClickListener {
            val email = etEmail.text.toString()
            val login = etLogin.text.toString()
            val password = etPassword.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                registerUser(login, password, email)
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(
        login: String,
        password: String,
        email: String,
    ) {
        val call = ApiClient.authApi.register(AuthModels.RegisterRequest(login, password, email))
        call.enqueue(
            object : Callback<AuthModels.AuthResponse> {
                override fun onResponse(
                    call: Call<AuthModels.AuthResponse>,
                    response: Response<AuthModels.AuthResponse>,
                ) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@SignUp, Login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignUp, "Ошибка регистрации!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<AuthModels.AuthResponse>,
                    t: Throwable,
                ) {
                    Toast.makeText(this@SignUp, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "ERROR: ${t.message}")
                }
            },
        )
    }
}
