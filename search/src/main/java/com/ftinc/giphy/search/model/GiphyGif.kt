package com.ftinc.giphy.search.model

import android.os.Parcelable
import com.ftinc.giphy.api.model.Gif
import com.ftinc.giphy.api.model.Image
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GiphyGif(
        val type: String,
        val id: String,
        val slug: String,
        val url: String,
        val bitlyUrl: String,
        val embedUrl: String,
        val username: String,
        val source: String,
        val rating: String,
        val user: GiphyUser,
        val source_tld: String,
        val source_postUrl: String,
        val updateDatetime: String, /* yyy-mm-dd hh:mm:ss */
        val createDatetime: String,
        val importDatetime: String,
        val trendingDatetime: String,
        val images: GiphyImages,
        val title: String
) : Parcelable {

    internal constructor(gif: Gif) : this(gif.type,
            gif.id, gif.slug, gif.url, gif.bitly_url, gif.embed_url, gif.username, gif.source, gif.rating,
            GiphyUser(gif.user.avatar_url, gif.user.banner_url, gif.user.profile_url, gif.user.username, gif.user.display_name, gif.user.twitter),
            gif.source_tld, gif.source_post_url, gif.update_datetime, gif.create_datetime, gif.import_datetime, gif.trending_datetime,
            GiphyImages(gif.images.fixed_height.giphy(),
                    gif.images.fixed_height_still.giphy(),
                    gif.images.fixed_height_downsampled.giphy(),
                    gif.images.fixed_width.giphy(),
                    gif.images.fixed_width_still.giphy(),
                    gif.images.fixed_width_downsampled.giphy(),
                    gif.images.fixed_height_small.giphy(),
                    gif.images.fixed_height_small_still.giphy(),
                    gif.images.fixed_width_small.giphy(),
                    gif.images.fixed_width_small_still.giphy(),
                    gif.images.downsized.giphy(),
                    gif.images.downsized_still.giphy(),
                    gif.images.downsized_large.giphy(),
                    gif.images.original.giphy(),
                    gif.images.original_still.giphy()),
            gif.title)


}

internal fun Image.giphy(): GiphyImage = GiphyImage(this.url, this.width, this.height, this.size, this.mp4, this.mp4_size, this.webp, this.webp_size)
