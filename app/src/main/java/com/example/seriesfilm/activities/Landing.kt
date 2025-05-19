package com.example.seriesfilm.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.seriesfilm.R

class Landing : AppCompatActivity() {
    lateinit var login_btn: Button
    lateinit var sign_up_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        login_btn = findViewById(R.id.login_btn)
        sign_up_btn = findViewById(R.id.sign_up)

        login_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        sign_up_btn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}