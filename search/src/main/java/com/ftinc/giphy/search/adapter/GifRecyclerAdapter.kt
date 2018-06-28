package com.ftinc.giphy.search.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_ID
import android.view.ViewGroup
import com.ftinc.giphy.api.model.Gif
import com.ftinc.giphy.api.model.Rendition
import com.ftinc.kit.kotlin.adapter.DiffCallback
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter


class GifRecyclerAdapter(context: Context, rendition: Rendition? = null) : ListRecyclerAdapter<Gif, GifViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder.create(inflater, parent)
    }


    override fun onBindViewHolder(vh: GifViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        val item = items[i]
        vh.bind(item)
    }


    fun setGifs(gifs: List<Gif>) {
        val diff = DiffUtil.calculateDiff(GifDiffCallback(items, gifs))
        items = gifs.toMutableList()
        diff.dispatchUpdatesTo(getListUpdateCallback())
    }


    class GifDiffCallback(val oldItems: List<Gif>, val newItems: List<Gif>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].hashCode() == newItems[newItemPosition].hashCode()
        }

    }
}