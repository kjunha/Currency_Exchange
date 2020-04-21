package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var receiverTextView:TextView
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
        receiverTextView = findViewById(R.id.receiver)
        exchangeRateTextView = findViewById(R.id.exchange_rate)
        requestTimeTextView = findViewById(R.id.request_time)
        amountEditText = findViewById(R.id.amount)

        receiverTextView.setText(countryList[0])
        requestTimeTextView.setText(LocalDateTime.now().format(dateTimeFormat))
    }
}
