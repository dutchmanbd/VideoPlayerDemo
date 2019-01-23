package com.zxdmjr.videoplayerdemo.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.widget.MediaController
import android.widget.SeekBar
import com.zxdmjr.videoplayerdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception





class MainActivity : AppCompatActivity(),Callback {

    companion object {
        const val TAG = "MainActivity"
    }

    private val player : MediaPlayer by lazy {
        MediaPlayer()
    }
    private lateinit var videoHolder: SurfaceHolder
    private val filePath: String
        get() = "android.resource://"+packageName+"/raw/"+R.raw.let_her_go

    private var isStart = false

    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVideoHolder()

        ivPlayPause.setOnClickListener {
            /**
             * when player already started not need to initialize video player again
             * "isStart" true when first time hit play button
             */
            if(isStart){
                if(player.isPlaying){
                    player.pause()
                    ivPlayPause.setImageResource(R.drawable.ic_play)
                } else{
                    player.start()
                    ivPlayPause.setImageResource(R.drawable.ic_pause)
                }
                initializeSeekBar()
            } else{
                initVideoPlayer()
                player.start()
                isStart = true
                ivPlayPause.setImageResource(R.drawable.ic_pause)
                initializeSeekBar()
            }
        }

        ivRewind.setOnClickListener {
            /**
             * Rewind 5000 miliseconds single clicked
             */
            var pos = player.currentPosition
            pos -= 5000
            player.seekTo(pos)
        }

        ivForward.setOnClickListener {
            /**
             * Forward 5000 miliseconds single clicked
             */
            var pos = player.currentPosition
            pos += 5000
            player.seekTo(pos)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                /**
                 * when user update seekbar progress
                 * then player current position need to change
                 */
                if (fromUser) {
                    player.seekTo(progress)
                }
            }
        })

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    /**
     * initial video holder with surface callback
     */
    private fun initVideoHolder(){
        videoHolder = videoSurface.holder
        videoHolder.addCallback(this)
        videoHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    /**
     * initial video player
     */
    private fun initVideoPlayer(){
        try {
            player.setDataSource(this@MainActivity, Uri.parse(filePath))
            player.setDisplay(videoHolder)
            player.prepare()
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)

            /**
             * when complete full video
             * then remove callbacks
             * and reset duration
             *
             */
            player.setOnCompletionListener {
                ivPlayPause.setImageResource(R.drawable.ic_play)
                handler.removeCallbacks(runnable)
                seekBar.progress = 0
                tvCurrentDuration.text = "0.0"
                tvDuration.text = "0.0"
            }

        } catch (e : Exception){
            Log.d(TAG, e.localizedMessage)
        }
    }


    /**
     * Method to initialize seek bar
     */
    private fun initializeSeekBar() {
        seekBar.max = player.duration
        val duartion = String.format("%.02f", player.duration/(1000.0*60))
        tvDuration.text = duartion
        runnable = Runnable {
            seekBar.progress = player.currentPosition

            val currentDuration = String.format("%.02f", player.currentPosition/(1000.0*60))
            tvCurrentDuration.text = currentDuration

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

}
