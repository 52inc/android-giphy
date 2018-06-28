package com.ftinc.giphy.api.model



data class User(
        val avatar_url: String,
        val banner_url: String,
        val profile_url: String,
        val username: String,
        val display_name: String,
        val twitter: String?
)