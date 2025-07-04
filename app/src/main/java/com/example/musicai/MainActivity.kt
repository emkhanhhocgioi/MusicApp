package com.example.musicai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicai.auth.ui.Login

class MainActivity : AppCompatActivity() {
    lateinit var  loginbutton: Button;
    lateinit var  signupbutton: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var thread = Thread {
            Thread.sleep(2000) // Simulate loading time
            runOnUiThread {

                startActivity(Intent(this, Login::class.java))
            }
        }
        thread.start()




    }
}