package com.namnp.aidl_ipc_server.sevice

import android.content.Context
import android.media.MediaPlayer
import android.os.IBinder
import com.namnp.aidl_ipc_server.IMusicService
import com.namnp.aidl_ipc_server.R

class MediaPlayerManager(private val context: Context) : IMusicService {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var name: String

    override fun asBinder(): IBinder? {
        return null
    }

    override fun getSongName(): String = name

    override fun changeMediaStatus() {
        mediaPlayer?.let {
            if(it.isPlaying) {
                pause()
            } else play()
        }
    }

    override fun playSong() {
        if(mediaPlayer?.isPlaying == true) {
            return
        }
        playNewSong()
    }

    override fun play() {
//        if(mediaPlayer == null) {
//            playNewSong()
//            return
//        }
//        if(!mediaPlayer!!.isPlaying) {
//            mediaPlayer!!.start()
//        }

        mediaPlayer?.run {
            if(isPlaying) {
                start()
            }
            return
        }
        playNewSong()
    }

    override fun pause() {
        mediaPlayer?.run {
            if(isPlaying) {
                pause()
            }
        }
    }

    override fun getCurrentDuration(): Long {
        return try {
            mediaPlayer?.currentPosition?.toLong() ?: 0
        }catch (e: Exception) {
            0
        }
    }

    override fun getTotalDuration(): Long = mediaPlayer?.duration?.toLong() ?: 0

    private fun playNewSong() {
        mediaPlayer = MediaPlayer.create(context, R.raw.sample)
        name = "I do"
        mediaPlayer?.start()
//        mediaPlayer?.let {
//            it.start()
//        }
    }
}