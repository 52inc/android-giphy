package com.ftinc.giphy.search.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ftinc.giphy.api.model.Gif
import com.ftinc.giphy.api.model.Image
import com.ftinc.giphy.api.model.Images
import com.ftinc.giphy.api.model.Rendition
import com.ftinc.giphy.search.R


class GifViewHolder(itemView: View, val rendition: Rendition?) : RecyclerView.ViewHolder(itemView) {

    private val image = itemView as ImageView


    fun bind(item: Gif) {

        val gif = gif(item.images)
        val width = itemView.width
        val scaledHeight = (gif.height.toFloat() / gif.width.toFloat()) * width

        Glide.with(itemView)
                .asGif()
                .load(gif.url)
                .apply(RequestOptions()
                        .override(Target.SIZE_ORIGINAL, scaledHeight.toInt()))
                .into(image)

    }

    fun gif(images: Images): Image = when(rendition) {
        Rendition.FIXED_HEIGHT -> images.fixed_height
        Rendition.FIXED_HEIGHT_STILL -> images.fixed_height_still
        Rendition.FIXED_HEIGHT_DOWNSAMPLED -> images.fixed_height_downsampled
        Rendition.FIXED_WIDTH -> images.fixed_width
        Rendition.FIXED_WIDTH_STILL -> images.fixed_width_still
        Rendition.FIXED_WIDTH_DOWNSAMPLED -> images.fixed_width_downsampled
        Rendition.FIXED_HEIGHT_SMALL -> images.fixed_height_small
        Rendition.FIXED_HEIGHT_SMALL_STILL -> images.fixed_height_small_still
        Rendition.FIXED_WIDTH_SMALL -> images.fixed_width_small
        Rendition.FIXED_WIDTH_SMALL_STILL -> images.fixed_width_small_still
        Rendition.DOWNSIZED -> images.downsized
        Rendition.DOWNSIZED_LARGE -> images.downsized_large
        Rendition.DOWNSIZED_STILL -> images.downsized_still
        Rendition.ORIGINAL -> images.original
        Rendition.ORIGINAL_STILL -> images.original_still
        else -> images.fixed_width
    }


    companion object {

        fun create(inflater: LayoutInflater, parent: ViewGroup?, rendition: Rendition? = null): GifViewHolder {
            return GifViewHolder(inflater.inflate(R.layout.item_gif, parent, false), rendition)
        }
    }
}