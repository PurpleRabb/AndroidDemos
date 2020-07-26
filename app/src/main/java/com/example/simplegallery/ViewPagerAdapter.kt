package com.example.simplegallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewpager_cell.view.*

class ViewPagerAdapter : ListAdapter<PhotoItem,PagerViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.viewpager_cell,parent,false).apply {
            return PagerViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).largeImageURL)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .into(holder.itemView.imageView2)
    }

    object DiffCallback:DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }

}

class PagerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)