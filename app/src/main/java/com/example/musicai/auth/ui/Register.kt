package com.example.musicai.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicai.R
import com.example.musicai.main.home.HomeActivity
import com.google.android.material.button.MaterialButton
import com.example.musicai.api.RegisterUser
class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        var createbtn = findViewById<MaterialButton>(R.id.create_account_button)
        createbtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

     suspend fun createAccount(username : String , password :String , email: String) {

          val  respone : Any = RegisterUser(username, password, email)
           if (respone === io.ktor.http.HttpStatusCode.OK) {

               startActivity(Intent(this, Login::class.java))
           }else {
               Toast.makeText(this ,"Failed to create account", Toast.LENGTH_SHORT);
           }
      }
}