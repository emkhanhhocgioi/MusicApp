package com.example.musicai.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicai.R
import com.example.musicai.main.home.HomeActivity
import com.google.android.material.button.MaterialButton

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val create_btn =  findViewById<MaterialButton>(R.id.login_button)
        val creatate_account_btn = findViewById<TextView>(R.id.create_acconut_text)

        create_btn.setOnClickListener {

            // Navigate to the Register activity
            startActivity(Intent(this, HomeActivity::class.java))

        }

        creatate_account_btn.setOnClickListener {
            // Navigate to the Register activity
            startActivity(Intent(this, Register::class.java))
        }
    }
}