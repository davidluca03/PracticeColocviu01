package ro.pub.cs.systems.eim.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PracticeTest01Service : Service() {
    private var processingThread: ProcessingThread = ProcessingThread(this, 0, 0)

    override fun onBind(intent: Intent?): IBinder? {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            var firstNum = intent.getIntExtra(Constants.NUM_CLICKS_1, -1)
            var secondNum = intent.getIntExtra(Constants.NUM_CLICKS_2, -1)

            processingThread = ProcessingThread(this, firstNum, secondNum)
            processingThread.start()
        }
        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        processingThread.stopThread()
    }
}