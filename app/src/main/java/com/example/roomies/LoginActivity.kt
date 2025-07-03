package com.example.roomies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roomies.fragments.LoginPage

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_fragment_container, LoginPage())
                .commit()
        }
    }
}
