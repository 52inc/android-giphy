package com.ftinc.giphy


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ftinc.giphy.api.model.Rendition
import com.ftinc.giphy.search.GiphySearch
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionSearch.setOnClickListener {

            GiphySearch.create(BuildConfig.GIPHY_API_KEY) {
                rendition = Rendition.DOWNSIZED_STILL
            }.start(this)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        GiphySearch.parseResult(resultCode, requestCode, data)?.let {
            GlideApp.with(this)
                    .asGif()
                    .load(it.images.original.url)
                    .into(image)
        }
    }
}
