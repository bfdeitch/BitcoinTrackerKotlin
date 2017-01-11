package com.teamtreehouse.bitcointrackerkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
  var numericDisplay: AlphanumericDisplay? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    numericDisplay = AlphanumericDisplay("I2C1")
    numericDisplay?.setEnabled(true)

    thread {
      while (true) {
        Fuel.get("https://api.gdax.com/products/BTC-USD/ticker").responseJson { request, response, result ->
          result.fold({ json ->
            val price = json.obj()["price"] as String
            Log.d("Price", price)
            numericDisplay?.display(price)
          }, {
            Log.e("Error", it.message)
          })
        }
        Thread.sleep(30 * 1000)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    numericDisplay?.close()
  }
}
