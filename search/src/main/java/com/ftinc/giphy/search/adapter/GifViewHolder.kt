package com.ftinc.giphy.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ftinc.giphy.api.model.Gif
import com.ftinc.giphy.search.R


class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image = itemView as ImageView


    fun bind(gif: Gif) {

        Glide.with(itemView)
                .load(gif)
                .into(image)

    }


    companion object {

        @JvmStatic
        fun create(inflater: LayoutInflater, parent: ViewGroup?): GifViewHolder {
            return GifViewHolder(inflater.inflate(R.layout.item_gif, parent, false))
        }
    }
}