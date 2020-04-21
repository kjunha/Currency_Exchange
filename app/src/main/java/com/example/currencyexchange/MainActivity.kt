package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity(), TextWatcher {
    private lateinit var senderSpinner:Spinner
    private lateinit var receiverSpinner:Spinner
    private lateinit var exchangeRateTextView:TextView
    private lateinit var requestTimeTextView:TextView
    private lateinit var amountEditText:EditText
    private lateinit var resultTextView:TextView

    private var dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        update()
    }
    private fun initViews() {
        //Late initialization
        senderSpinner = findViewById(R.id.sender)
        receiverSpinner = findViewById(R.id.receiver)
        exchangeRateTextView = findViewById(R.id.exchange_rate)
        requestTimeTextView = findViewById(R.id.request_time)
        amountEditText = findViewById(R.id.amount)
        resultTextView = findViewById(R.id.result_text)

        //Apply Listener
        receiverSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                exchangeRateTextView.setText(getCurrencyCode(receiverSpinner.selectedItem.toString())
                        + "/"
                        + getCurrencyCode(senderSpinner.selectedItem.toString()))
                update()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        amountEditText.addTextChangedListener(this)
    }

    //Utility Functions
    private fun getCurrencyCode(country: String):String {
        val builder = StringBuilder()
        for((index, char) in country.withIndex()) {
            if(char == '(') {
                builder.append(country.get(index+1))
                builder.append(country.get(index+2))
                builder.append(country.get(index+3))
                break
            }
        }
        return builder.toString()
    }

    private fun update() {
        requestTimeTextView.setText(LocalDateTime.now().format(dateTimeFormat))
        resultTextView.setText(resources.getString(R.string.result_message_1) + " 10000 " + resources.getString(R.string.result_message_2))
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        update()
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}
