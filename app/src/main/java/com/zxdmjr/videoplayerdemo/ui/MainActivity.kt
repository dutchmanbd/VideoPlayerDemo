package com.zxdmjr.videoplayerdemo.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.zxdmjr.videoplayerdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val filePath = "android.resource://"+packageName+"/raw/"+R.raw.let_her_go

        videoPlayer.setVideoURI(Uri.parse(filePath))
        videoPlayer.setMediaController(MediaController(this))
        videoPlayer.start()

    }

    override fun onResume() {
        super.onResume()
        videoPlayer.resume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.pause()
    }

}
