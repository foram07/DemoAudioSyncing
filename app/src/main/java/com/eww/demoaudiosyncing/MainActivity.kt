package com.eww.demoaudiosyncing

import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast


class MainActivity : AppCompatActivity() {

     lateinit var mHandler: Handler
    lateinit var moveSeekBarThread:Runnable
     var mediaMax:Int =0
    var mediaPos:Int = 0
     var seek:Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.eww.demoaudiosyncing.R.layout.activity_main)
        val mPlayer: MediaPlayer = MediaPlayer.create(this, com.eww.demoaudiosyncing.R.raw.intheend)
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mPlayer.isLooping=true

        mHandler = Handler()
        val btnPlay: ImageView = findViewById(com.eww.demoaudiosyncing.R.id.btn_image);
        val btnPause: ImageView = findViewById(com.eww.demoaudiosyncing.R.id.btn_pause);
        val seekbar: SeekBar = findViewById(com.eww.demoaudiosyncing.R.id.seekbar);

        mediaPos = mPlayer.getCurrentPosition();
        mediaMax = mPlayer.getDuration();

        seekbar.setMax(mediaMax); // Set the Maximum range of the
        seekbar.setProgress(mediaPos);// set current progress to song's



        btnPlay.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@MainActivity, "Playing Audio", Toast.LENGTH_SHORT).show();
            mPlayer.start()
            btnPlay.visibility=View.GONE
            btnPause.visibility=View.VISIBLE
            mHandler.removeCallbacks(moveSeekBarThread);
            mHandler.postDelayed(moveSeekBarThread, 0); //cal the thread after 100 milliseconds

        })

        btnPause.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@MainActivity, "Playing Pause", Toast.LENGTH_SHORT).show();
            btnPlay.visibility=View.VISIBLE
            btnPause.visibility=View.GONE
            mPlayer.pause()
        })


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var seek=mediaPos+ seekBar?.progress!!
                Log.e("TAG","Seek: "+seek)
                seekbar.progress = mediaPos+seekbar.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let { mPlayer.seekTo(it.progress) }
            }
        })


         moveSeekBarThread = object : Runnable {
            override fun run() {
                if (mPlayer.isPlaying()) {
                    val mediaPos_new = mPlayer.currentPosition
                    val mediaMax_new = mPlayer.duration
                    seekbar.max = mediaMax_new
                    seekbar.progress = mediaPos_new
                    Log.e("TAG", "mediaMax" + mediaMax_new + " \t mediaPos" + mediaPos_new)
                    mHandler.postDelayed(this, 100) //Looping the thread after 0.1 second
                }
            }
        }
    }
}