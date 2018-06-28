package com.ftinc.giphy.search.adapter

import android.content.Context
import android.view.ViewGroup
import com.ftinc.giphy.api.model.Gif
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter


class GifRecyclerAdapter(context: Context) : ListRecyclerAdapter<Gif, GifViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder.create(inflater, parent)
    }


    override fun onBindViewHolder(vh: GifViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        val item = items[i]
        vh.bind(item)
    }


    fun setGifs(gifs: List<Gif>) {

    }
}