package com.example.watchtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import services.ReverseTimerService
import views.CircleTimer
import views.PlayStatusView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var circleTimer: CircleTimer? = null
    private var playButton: PlayStatusView? = null
    private var dateFormat = SimpleDateFormat("mm:ss", Locale.ENGLISH);


    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            update(intent!!) // or whatever method used to update your GUI fields
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        circleTimer = findViewById(R.id.circleTimer)
        playButton = findViewById(R.id.playStatusView)

        playButton!!.onClick {
            var intent = Intent(this, ReverseTimerService::class.java)
            var timeText = this.circleTimer?.text

            var ticks = dateFormat.parse(timeText).seconds * 1000 + 1000 // fix start with -1 sec

            intent.putExtra("ticks", ticks)

            if (playButton?.isClicked!!)
                startService(intent)
            else
                stopService(Intent(this, ReverseTimerService::class.java))
        }
        //int year, int month, int date, int hrs, int min, int sec
        circleTimer?.text = retrieveData(Date(1970,1,1,1,0,10))
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(br, IntentFilter(ReverseTimerService.COUNTDOWN_BR))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(br)
    }

    override fun onStop() {
        try {
            unregisterReceiver(br)
        } catch (e: Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        stopService(Intent(this, ReverseTimerService::class.java))
        super.onDestroy()
    }

    fun update(intent: Intent) {
        if (intent.extras != null) {
            val millisUntilFinished = intent.getLongExtra("countdown", 0)
            circleTimer?.text = retrieveData(Date(millisUntilFinished))
        }
    }

    private fun retrieveData(date: Date): String{
        return dateFormat.format(date)
    }
}