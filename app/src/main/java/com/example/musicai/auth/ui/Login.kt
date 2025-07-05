package com.example.musicai.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.musicai.R
import com.example.musicai.main.home.HomeActivity
import com.google.android.material.button.MaterialButton
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {


    var isLogin : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val create_btn =  findViewById<MaterialButton>(R.id.login_button)
        val creatate_account_btn = findViewById<TextView>(R.id.create_acconut_text)
        val emailEditText = findViewById<EditText>(R.id.email_login_textedit)
        val passwordEditText = findViewById<EditText>(R.id.password_login_textedit)


        create_btn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    loginUser(email, password)
                }
            }


        }

        creatate_account_btn.setOnClickListener {
            // Navigate to the Register activity
            startActivity(Intent(this, Register::class.java))
        }
    }

    data class LoginRequest(var email: String, var password: String);
    suspend fun loginUser(email: String, password: String) {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response = client.post("http://192.168.1.5:8080/auth/login") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }
        if (response.status == io.ktor.http.HttpStatusCode.OK) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else if (response.status == io.ktor.http.HttpStatusCode.Unauthorized   ) {
            Toast.makeText(this,"Wrong user or password!" , Toast.LENGTH_SHORT).show();

        } else if (response.status == io.ktor.http.HttpStatusCode.BadRequest){
            Toast.makeText(this,"Bad Request" , Toast.LENGTH_SHORT).show();
        }else if ( response.status == io.ktor.http.HttpStatusCode.NotFound){
            Toast.makeText(this,"Not Found" , Toast.LENGTH_SHORT).show();
        }
    }

}