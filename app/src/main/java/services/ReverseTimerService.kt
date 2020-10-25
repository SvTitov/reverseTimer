package services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Vibrator
import java.lang.Exception

class ReverseTimerService : Service() {
    var bi = Intent(COUNTDOWN_BR)
    var cdt: CountDownTimer? = null

    override fun onDestroy() {
        cdt!!.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var value = intent?.getIntExtra("ticks", 0);
        startTimer(value!!)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(value: Int) {
        cdt = object : CountDownTimer(value.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1000)
            }
        }
        cdt?.start()
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val COUNTDOWN_BR = "com.example.watchtimer.countdown_br"
    }
}