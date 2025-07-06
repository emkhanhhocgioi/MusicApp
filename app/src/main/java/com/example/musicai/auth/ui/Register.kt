package com.example.musicai.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.musicai.R

import com.example.musicai.main.home.HomeActivity
import com.google.android.material.button.MaterialButton

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch



class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        var createbtn = findViewById<MaterialButton>(R.id.create_account_button)
        createbtn.setOnClickListener {
            val username = findViewById<EditText>(R.id.username_textedit).text.toString();
            val password = findViewById<EditText>(R.id.password_textedit).text.toString();
            val email = findViewById<EditText>(R.id.email_textedit).text.toString();
            if (username == null ||  password == null || email == null ){
                Toast.makeText(this, "Vui lòng nhập đủ", Toast.LENGTH_SHORT).show()
            }else{
                lifecycleScope.launch {
                    RegisterUser(username,email,password)
                }
            }

        }
    }
    data class RegisterRequset(var username: String,var email : String, var password : String  );
    suspend fun RegisterUser (username: String, password: String , email: String)  {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response = client.post("http://192.168.1.5:8080/auth/register") {
            contentType(io.ktor.http.ContentType.Application.Json);

            setBody(RegisterRequset(username,email,password))

        }
        if (response.status == io.ktor.http.HttpStatusCode.OK) {
            runOnUiThread {
                Toast.makeText(this,"Sign up successfully", Toast.LENGTH_SHORT).show();
                startActivity(Intent(this, Login::class.java))
                finish()
            }
            client.close()

        } else {
            client.close()
            throw Exception("Failed to register user: ${response.status}")
        }

       }

}