package com.namnp.aidl_ipc_server.sevice

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import com.namnp.aidl_ipc_server.IMusicService
import com.namnp.aidl_ipc_server.R


class MusicService: Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }
    private var notification: Notification? = null
    private lateinit var mediaPlayerManager: MediaPlayerManager

    private val mBinder: IMusicService.Stub = object : IMusicService.Stub(){

        override fun getSongName(): String = mediaPlayerManager.songName

        override fun changeMediaStatus() = mediaPlayerManager.changeMediaStatus()

        override fun playSong() = mediaPlayerManager.playSong()

        override fun play() = mediaPlayerManager.play()

        override fun pause() = mediaPlayerManager.pause()

        override fun getCurrentDuration(): Long = mediaPlayerManager.currentDuration

        override fun getTotalDuration(): Long = mediaPlayerManager.totalDuration
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayerManager = MediaPlayerManager(applicationContext)
        startForegroundService()
    }

    private fun startForegroundService() {
        startForeground(NOTIFICATION_ID, getNotification())
    }

    private fun getNotification(): Notification? {
        if (notification == null) {
            var title = "AIDL-IPC Testing"
            try {
                title = mediaPlayerManager.songName
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            notification = Notification.Builder(this).setContentTitle(title)
                .setContentText("AIDL-IPC Testing")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
        }
        return notification
    }

    override fun onBind(p0: Intent?): IBinder = mBinder
}