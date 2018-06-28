package com.ftinc.giphy.api.model



data class Image(
        val url: String,
        val width: Int,
        val height: Int,
        val size: Int?,
        val mp4: String?,
        val mp4_size: Int?,
        val webp: String?,
        val webp_size: Int?
)