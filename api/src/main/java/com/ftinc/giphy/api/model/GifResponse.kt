package com.ftinc.giphy.api.model



data class GifResponse(
        val data: List<Gif>,
        val meta: Meta,
        val pagination: Pagination?
)