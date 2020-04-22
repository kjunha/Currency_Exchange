package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), TextWatcher {
    private lateinit var senderSpinner:Spinner
    private lateinit var receiverSpinner:Spinner
    private lateinit var exchangeRateTextView:TextView
    private lateinit var requestTimeTextView:TextView
    private lateinit var amountEditText:EditText
    private lateinit var resultTextView:TextView
    private lateinit var baseCurrencyTextView: TextView

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val df = DecimalFormat("#,##0.00")

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
        baseCurrencyTextView = findViewById(R.id.base_currency)

        //Apply Listener
        receiverSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                exchangeRateTextView.text = getString(R.string.exchange_rate_unit, getCurrencyCode(senderSpinner.selectedItem.toString()), getCurrencyCode(receiverSpinner.selectedItem.toString()))
                update()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        amountEditText.addTextChangedListener(this)
    }

    private fun update() {
        requestTimeTextView.text = LocalDateTime.now().format(dateTimeFormat)
        baseCurrencyTextView.text = getCurrencyCode(senderSpinner.selectedItem.toString())
        ConnectionManager.getInstance(this).updateCurrency(LocalDateTime.parse(requestTimeTextView.text, dateTimeFormat))
        val rate = ConnectionManager.getInstance(this).getCurrencyValue(senderSpinner.selectedItem.toString(), receiverSpinner.selectedItem.toString())
        val amount = if(amountEditText.text.toString().equals("")) 0.0 else amountEditText.text.toString().toDouble()
        resultTextView.text = getString(R.string.result_message, df.format(rate * amount), getCurrencyCode(receiverSpinner.selectedItem.toString()))
    }

    //Utility Functions to use CurrencyLayer service
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

    //TextWatcher Interface
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val content = if(amountEditText.text.toString().equals("")) 0.0 else amountEditText.text.toString().toDouble()
        if(content in 0.0..10000.0) {
            amountEditText.error = null
            update()
        } else {
            amountEditText.error = getString(R.string.error_message)
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}
