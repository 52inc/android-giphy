package com.ftinc.giphy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ftinc.giphy.api.Giphy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val giphy = Giphy("some_key")

        giphy.search("Bacon") {
            limit = 50
            offset = 25
        }.subscribe({

        }, {

        })

    }
}
