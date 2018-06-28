package com.ftinc.giphy.search.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GiphyImages(
        val fixedHeight: GiphyImage,
        val fixedHeightStill: GiphyImage,
        val fixedHeightDownsampled: GiphyImage,
        val fixedWidth: GiphyImage,
        val fixedWidthStill: GiphyImage,
        val fixedWidthDownsampled: GiphyImage,
        val fixedHeightSmall: GiphyImage,
        val fixedHeightSmallStill: GiphyImage,
        val fixedWidthSmall: GiphyImage,
        val fixedWidthSmallStill: GiphyImage,
        val downsized: GiphyImage,
        val downsizedStill: GiphyImage,
        val downsizedLarge: GiphyImage,
        val original: GiphyImage,
        val originalStill: GiphyImage
) : Parcelable