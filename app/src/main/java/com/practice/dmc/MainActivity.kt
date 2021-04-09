package com.practice.dmc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practice.dmc.dmc.SearchActivity
import com.practice.dmc.dmr.CreateDeviceActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bt_dmc).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.bt_dmr).setOnClickListener {
            val intent = Intent(this, CreateDeviceActivity::class.java)
            startActivity(intent)
        }
    }
}