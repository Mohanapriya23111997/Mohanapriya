package com.example.androidtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar!!.hide()
        handler = Handler()
        handler!!.postDelayed(Runnable {
            val intent = Intent(this@MainActivity, productlist::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}