package com.ftinc.giphy.search


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.ftinc.giphy.api.model.Rendition
import com.ftinc.giphy.search.model.GiphyGif


object GiphySearch {

    private const val RC_GIPHY_SEARCH = 1557


    /**
     * Create a new configuration instance for launching the Giphy search activity for searching
     * GIFs and selecting and returning them
     */
    fun create(apiKey: String, init: Builder.() -> Unit): Builder {
        val builder = Builder(apiKey)
        init.invoke(builder)
        return builder
    }


    /**
     * Create a new configuration instance for launching the Giphy search activity for searching
     * GIFs and selecting and returning them
     */
    fun create(apiKey: String): Builder {
        val builder = Builder(apiKey)
        return builder
    }


    /**
     * Parse the result of a [GiphySearchActivity] from the user previously calling [create]
     */
    fun parseResult(resultCode: Int, requestCode: Int, data: Intent?): GiphyGif? {
        if (resultCode == RESULT_OK && requestCode == RC_GIPHY_SEARCH) {
            return data?.getParcelableExtra(GiphySearchActivity.EXTRA_GIF)
        }
        return null
    }


    class Builder(val apiKey: String,
                  var url: String? = null,
                  var rendition: Rendition? = null,
                  var limit: Int? = null,
                  var lang: String? = null) {

        fun start(activity: Activity) {
            val intent = GiphySearchActivity.createIntent(activity, apiKey, url, rendition, limit, lang)
            activity.startActivityForResult(intent, RC_GIPHY_SEARCH)
        }
    }
}