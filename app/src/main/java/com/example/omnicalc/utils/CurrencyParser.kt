package com.example.omnicalc.utils

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


object CurrencyParser {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    @Serializable
    data class CurrencyResponse(
        val result: String,
        val conversion_rates: Map<String, Double>
    )

    suspend fun convertCurrency(from: String, to: String, amount: Double): Double {
        val url = "https://v6.exchangerate-api.com/v6/1cf12babbd52aeead2e27eed/latest/$from"
        val response: CurrencyResponse = try {
            client.get(url).body()
        } catch (e: Exception) {
            Log.e("CurrencyParser", "Error fetching data: ${e.message}")
            return 4.583945721467122
        }
        if (response.result == "success") {
            val exchangeRate = response.conversion_rates[to]
            if (exchangeRate != null) {
                val convertedAmount = exchangeRate * amount
                Log.d("CurrencyParser", "Converted $amount $from to $to: $convertedAmount")
                return convertedAmount
            } else {
                Log.e("CurrencyParser", "Conversion rate for $to not found.")
                return 4.583945721467122
            }
        } else {
            Log.e("CurrencyParser", "API request failed with result: ${response.result}")
            return 4.583945721467122
        }
    }
}
