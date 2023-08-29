package com.namnp.aidl_ipc_server

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.namnp.aidl_ipc_server.sevice.MusicService
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private val DELAY_INTERVAL: Long = 1000

    private var iMusicService: IMusicService? = null
    private var isServiceConnected = false
    private lateinit var textCurrentDuration: TextView
    private lateinit var textTotalDuration: TextView
    private lateinit var textSongName: TextView
    private var handler: Handler? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            iMusicService = IMusicService.Stub.asInterface(binder)
            isServiceConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        bindService()
        updateCurrentProgress()

    }

    private fun initViews() {
        findViewById<ImageView>(R.id.image_play).setOnClickListener {
            handlePlayingMusic()
        }
        findViewById<ImageView>(R.id.image_pause).setOnClickListener {
            handlePausingMusic()
        }
        textCurrentDuration = findViewById(R.id.text_current_duration)
        textTotalDuration = findViewById(R.id.text_total_duration)
        textSongName = findViewById(R.id.text_name)
    }

    private fun bindService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun handlePlayingMusic() {
        try {
            iMusicService?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePausingMusic() {
        try {
            iMusicService?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateCurrentProgress() {
        if(handler == null) return

        handler?.postDelayed({
            try {
                iMusicService?.let {
                    textCurrentDuration.text = formatDuration(it.currentDuration)
                    textTotalDuration.text = formatDuration(it.totalDuration)
                    textSongName.text = it.songName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            updateCurrentProgress()
        }, DELAY_INTERVAL)
    }

    private fun formatDuration(duration: Long): String {
        val secondValue = abs(duration)
        val hour = (secondValue / 3600)
        val minute = (secondValue % 3600) / 60
        val second = (secondValue % 60)
        return String.format("%d:%02d:%02d", hour, minute, second)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}