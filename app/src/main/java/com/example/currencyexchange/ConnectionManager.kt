package com.example.currencyexchange

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class ConnectionManager constructor(context: Context) {
    //Singleton Object (Composition Model)
    companion object {
        @Volatile
        private var instance: ConnectionManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ConnectionManager(context).also {
                    instance = it
                }
            }
    }

    //Fields
    private var lastUpdate:LocalDateTime? = null
    private val context:Context = context
    private val allSenders:Array<String> = context.resources.getStringArray(R.array.sending_countries_array)
    private val allReceivers:Array<String> = context.resources.getStringArray(R.array.receiving_countries_array)
    private val currencyMap:HashMap<String,Double> = HashMap<String, Double>()
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    public fun updateCurrency(time: LocalDateTime) {
        if(lastUpdate == null || ChronoUnit.MINUTES.between(lastUpdate, time) > 15) {
            lastUpdate = time
            //val url = context.getString(R.string.url)
            //TODO Debugging Line
            val url = "http://www.apilayer.net/api/live?access_key=ee50cd7cc73c9b7a7bb3d9617cfb6b9c"
            val jsonObject = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                        response ->
                    val quotes:JSONObject = response.getJSONObject("quotes")
                    for(senders in allSenders) {
                        for(receivers in allReceivers) {
                            val apiKey = getCurrencyCode(senders) + getCurrencyCode(receivers)
                            currencyMap.put(apiKey, quotes.getDouble(apiKey))
                            Log.d("json-success", "Key: " + apiKey + " value: " + currencyMap.get(apiKey).toString())
                        }
                    }
                },
                Response.ErrorListener {
                        error -> Log.d("json-error", error.toString())
                })
            getInstance(context).addToRequestQueue(jsonObject)
        }
    }

    public fun getCurrencyValue(sender:String, receiver:String):Double {
        val hashKey:String = getCurrencyCode(sender) + getCurrencyCode(receiver)
        return currencyMap.get(hashKey) ?: 0.0
    }

    private fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
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
}