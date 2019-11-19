package com.androiddevs.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this, R.raw.example)
        seekBar.max = mp?.duration!!

        btnPlay.setOnClickListener {
            play()
        }

        btnPause.setOnClickListener {
            if (mp?.isPlaying ?: return@setOnClickListener) {
                mp?.pause()
            }
        }

        btnStop.setOnClickListener {
            stop()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, fromUser: Boolean) {
                // only seek if the user made the change
                if (fromUser) {
                    mp?.seekTo(value)
                    tvCurTime.text = getTimeStringWithMillis(value)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    fun play() {
        mp = mp ?: MediaPlayer.create(this, R.raw.example)
        mp?.start()
        seekBar.max = mp?.duration!!
        tvSongLength.text = getTimeStringWithMillis(mp?.duration!!)

        CoroutineScope(Dispatchers.Main).launch {
            while (mp != null) {
                var pos = mp?.currentPosition!!
                tvCurTime.text = getTimeStringWithMillis(pos)
                seekBar.progress = pos
                delay(100)
            }
        }
    }

    fun stop() {
        mp?.stop()
        seekBar.progress = 0
        mp = null
        tvCurTime.text = "0:00"
    }

    fun getTimeStringWithMillis(millis: Int): String {
        val minutes = millis / 1000 / 60
        val seconds = millis / 1000 % 60

        var timeString = "$minutes:"
        if (seconds < 10) {
            timeString += "0"
        }
        timeString += seconds

        return timeString
    }
}
