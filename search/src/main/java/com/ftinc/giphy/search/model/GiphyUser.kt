package com.ftinc.giphy.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GiphyUser(
        val avatarUrl: String,
        val bannerUrl: String,
        val profileUrl: String,
        val username: String,
        val displayName: String,
        val twitter: String?
) : Parcelable