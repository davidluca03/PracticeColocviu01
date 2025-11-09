package ro.pub.cs.systems.eim.practicaltest01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PracticeTest01MainActivity : AppCompatActivity() {
    private var isReceiverRegistered = false;
    private lateinit var button1: Button;
    private lateinit var button2: Button;
    private lateinit var switchButton: Button;
    private lateinit var text1: EditText;
    private lateinit var text2: EditText;
    private var numClicks1: Int = 0;
    private var numClicks2: Int = 0;
    private var myListener: myListener = myListener()
    private var serviceStatus: String = Constants.SERVICE_STOPPED
    private var broadcastReceiver: messageReceiver = messageReceiver()
    private var intentFilter: IntentFilter = IntentFilter()

    private inner class messageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("DEBUG", intent?.getStringExtra(Constants.BROADCAST_MESSAGE) ?:"NO MESSAGE")
        }
    }

    private inner class myListener: View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.button1 -> {numClicks1++
                    text1.setText(numClicks1.toString())}

                R.id.button2 -> {numClicks2++
                    text2.setText(numClicks2.toString())}

                R.id.second_activity -> {
                    var numberOfClicks: Int = numClicks1 + numClicks2
                    var intent: Intent = Intent(applicationContext,
                        PracticalTest01SecondaryActivity::class.java)
                    intent.putExtra(Constants.NUMBER_CLICKS, numberOfClicks)
                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE)
                }
            }

            if (serviceStatus == Constants.SERVICE_STOPPED &&
                    numClicks1 + numClicks2 > Constants.CLICKS_LIMIT) {
                serviceStatus = Constants.SERVICE_STARTED

                var intent: Intent = Intent(applicationContext, PracticeTest01Service::class.java)
                intent.putExtra(Constants.NUM_CLICKS_1, numClicks1)
                intent.putExtra(Constants.NUM_CLICKS_2, numClicks2)
                applicationContext.startService(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)

        for (i in 0 until Constants.actions.size) {
            intentFilter.addAction(Constants.actions[i])
        }

        button1 = findViewById<Button>(R.id.button1)
        button2 = findViewById<Button>(R.id.button2)
        switchButton = findViewById<Button>(R.id.second_activity)

        text1 = findViewById<EditText>(R.id.text1)
        text2 = findViewById<EditText>(R.id.text2)

        text1.setText(numClicks1.toString())
        text2.setText(numClicks1.toString())

        button1.setOnClickListener(myListener)
        button2.setOnClickListener(myListener)
        switchButton.setOnClickListener(myListener)
    }

    override fun onDestroy() {
        var intent: Intent = Intent(applicationContext, PracticeTest01Service::class.java)
        stopService(intent)
        super.onDestroy()
    }

    @Override
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.TEXT_1, numClicks1)
        outState.putInt(Constants.TEXT_2, numClicks2)
    }


    @Override
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        numClicks1 = 0
        numClicks2 = 0

        if (savedInstanceState.containsKey(Constants.TEXT_1))
            numClicks1 = savedInstanceState.getInt(Constants.TEXT_1)

        if (savedInstanceState.containsKey(Constants.TEXT_2))
            numClicks2 = savedInstanceState.getInt(Constants.TEXT_2)

        text1.setText(numClicks1.toString())
        text2.setText(numClicks2.toString())
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        var a = 1
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        super.onPause()
    }
}