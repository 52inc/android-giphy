package com.ftinc.giphy.api


import com.ftinc.giphy.api.internal.GiphyService
import com.ftinc.giphy.api.model.GifResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * TODO: Add the remaining API endpoints and features
 */
class Giphy(
        private val apiKey: String,
        url: String = DEFAULT_URL,
        logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
) {

    private val service: GiphyService


    init {
        val client = OkHttpClient.Builder()
                .addInterceptor {
                    val newUrl = it.request().url().newBuilder()
                            .addQueryParameter("api_key", apiKey)
                            .build()
                    it.proceed(it.request().newBuilder()
                            .url(newUrl)
                            .build())
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(GiphyService::class.java)
    }


    /*
     * API Methods
     */


    fun search(query: String,
               limit: Int? = null,
               offset: Int? = null,
               rating: String? = null,
               lang: String? = null): Observable<GifResponse> {
        return service.search(query, limit, offset, rating, lang)
    }


    fun search(query: String, config: Params.() -> Unit): Observable<GifResponse> {
        val params = Params()
        config.invoke(params)
        return service.search(query, params.limit, params.offset, params.rating, params.lang)
    }


    fun trending(limit: Int? = null,
                 offset: Int? = null,
                 rating: String? = null): Observable<GifResponse> {
        return service.trending(limit, offset, rating)
    }


    fun trending(config: Params.() -> Unit): Observable<GifResponse> {
        val params = Params()
        config.invoke(params)
        return service.trending(params.limit, params.offset, params.rating)
    }


    class Params(var limit: Int? = null,
                 var offset: Int? = null,
                 var rating: String? = null,
                 var lang: String? = null)


    companion object {
        const val DEFAULT_URL = "https://api.giphy.com/"
    }
}
