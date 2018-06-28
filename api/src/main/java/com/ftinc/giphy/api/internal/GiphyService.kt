package com.ftinc.giphy.api.internal

import com.ftinc.giphy.api.model.GifResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GiphyService {

    @GET("search")
    fun search(@Query("q") query: String,
               @Query("limit") limit: Int? = null,
               @Query("offset") offset: Int? = null,
               @Query("rating") rating: String? = null,
               @Query("lang") lang: String? = null) : Observable<GifResponse>


    @GET("trending")
    fun trending(@Query("limit") limit: Int? = null,
                 @Query("offset") offset: Int? = null,
                 @Query("rating") rating: String? = null) : Observable<GifResponse>

}