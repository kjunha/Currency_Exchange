package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private lateinit var senderSpinner:Spinner
    private lateinit var receiverSpinner:Spinner
    private lateinit var exchangeRateTextView:TextView
    private lateinit var requestTimeTextView:TextView
    private lateinit var amountEditText:EditText

    private var countryList = arrayOf<String>("한국(KRW)", "일본(JPN)", "필리핀(PHP)")
    private var dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }
    private fun initViews() {
        senderSpinner = findViewById(R.id.sender)
        receiverSpinner = findViewById(R.id.receiver)
        exchangeRateTextView = findViewById(R.id.exchange_rate)
        requestTimeTextView = findViewById(R.id.request_time)
        amountEditText = findViewById(R.id.amount)

        requestTimeTextView.setText(LocalDateTime.now().format(dateTimeFormat))

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
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

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
}
