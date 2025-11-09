package ro.pub.cs.systems.eim.practicaltest01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity : AppCompatActivity() {
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var textElem: TextView
    private var myListener: myListener = myListener()

    private inner class myListener : View.OnClickListener {
        @Override
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.ok_button -> setResult(RESULT_OK)
                R.id.cancel_button -> setResult(RESULT_CANCELED)
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)

        okButton = findViewById<Button>(R.id.ok_button)
        cancelButton = findViewById<Button>(R.id.cancel_button)
        textElem = findViewById<TextView>(R.id.number_of_clicks_text_view)

        okButton.setOnClickListener(myListener)
        cancelButton.setOnClickListener(myListener)

        var extras = intent.extras
        if (extras != null && extras.containsKey(Constants.NUMBER_CLICKS)) {
            textElem.setText(extras.getInt(Constants.NUMBER_CLICKS).toString())
        }
    }
}