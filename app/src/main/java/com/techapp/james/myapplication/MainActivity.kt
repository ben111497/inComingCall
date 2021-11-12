package com.techapp.james.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.extras?.getBoolean("Close")?.let { stopService(Intent(this, MyService::class.java)) }

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val voiceCall = NotificationChannel("InComingCall","InComingCall", NotificationManager.IMPORTANCE_HIGH)
            voiceCall.description = "InComingCall"
            voiceCall.lightColor = Color.GREEN
            voiceCall.enableVibration(true) //震動
            voiceCall.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            nm.createNotificationChannel(voiceCall)
        }

        val btn_start = findViewById<Button>(R.id.btn_start)
        val btn_over = findViewById<Button>(R.id.btn_over)

        val b = Bundle()
        b.putString("Name", "阿扁巴巴大霞")

        btn_start.setOnClickListener { startService(Intent(this, MyService::class.java).putExtras(b)) }
        btn_over.setOnClickListener { stopService(Intent(this, MyService::class.java)) }
    }

    override fun onDestroy() {
        val i = Intent(this, MyService::class.java)
        stopService(i)
        super.onDestroy()
    }
}
