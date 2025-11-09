package ro.pub.cs.systems.eim.practicaltest01

import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

class ProcessingThread(var context: Context, var firstNum: Int, var secondNum: Int) : Thread() {
    var arithmetic = (firstNum + secondNum).toDouble() / 2.0
    var geometric = Math.sqrt((firstNum * secondNum).toDouble())
    var random: Random = Random(42)

    companion object {
        private const val SLEEP_TIME = 10000L
    }
    var isRunning = true

    fun stopThread() {
        this.isRunning = false
    }

    @Override
    override fun run() {
        Log.d("DEBUG", "THREAD START")
        while(isRunning) {
            Log.d("DEBUG", "SENDING_MSG")
            sendMessage()
            try {
                Thread.sleep(SLEEP_TIME)
            } catch (e: InterruptedException) {
                Log.d("IDK", "THREAD STOP")
                e.printStackTrace()
            }
        }
    }

    fun sendMessage() {
        var intent: Intent = Intent()
        intent.action = Constants.actions[random.nextInt(Constants.actions.size)]

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        intent.putExtra(Constants.BROADCAST_MESSAGE, currentDate + " "
                                                                + arithmetic + " "
                                                                + geometric)
        context.sendBroadcast(intent)
    }
}