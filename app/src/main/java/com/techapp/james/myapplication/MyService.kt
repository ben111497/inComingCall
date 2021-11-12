package com.techapp.james.myapplication

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews

class MyService : Service() {
    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var second = 0
        val b = intent?.extras ?: Bundle()
        val thread = Thread {
            while (true) {
                if (second > 15) stopSelf() else second ++

                val mBuilder = NotificationCompat.Builder(this@MyService, "InComingCall")
                    .setTicker("語音電話")  //設置狀態列的顯示的資訊
                    .setSmallIcon(R.drawable.ic_launcher_background) //status bar icon
                    .setCustomContentView(getRemoteViews(b))    //自定義view
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true)   //不能滑動刪除通知
                    .setContentIntent(getPendingIntent())  //主體點擊事件
                    //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI) //聲音
                    .setPriority(Notification.PRIORITY_MAX) //優先權
                    .setAutoCancel(false)    //點擊推播後關閉
                    .setCategory(Notification.CATEGORY_CALL)

                startForeground(1, mBuilder.build())
                Thread.sleep(2000)
            }
        }
        thread.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getRemoteViews(b: Bundle): RemoteViews {
        val remoteViews = RemoteViews(packageName, R.layout.notification_voice_call)

        remoteViews.setTextViewText(R.id.tv_name, b.getString("Name") ?: "")
        remoteViews.setImageViewResource(R.id.img_avatar, R.drawable.ic_launcher_background)
        remoteViews.setOnClickPendingIntent(R.id.tv_cancel,getCancelPendingIntent())
        remoteViews.setOnClickPendingIntent(R.id.tv_answer,getAnswerPendingIntent())
        return remoteViews
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Close", true)
        return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getCancelPendingIntent(): PendingIntent? {
        val intent = Intent(this, this::class.java)
        val mNotificationManager = NotificationManagerCompat.from(this.applicationContext)
        mNotificationManager.cancel(0)
        return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getAnswerPendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
