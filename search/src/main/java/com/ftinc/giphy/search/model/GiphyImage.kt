package com.ftinc.giphy.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GiphyImage(
        val url: String,
        val width: Int,
        val height: Int,
        val size: Int?,
        val mp4: String?,
        val mp4Size: Int?,
        val webp: String?,
        val webpSize: Int?
) : Parcelable