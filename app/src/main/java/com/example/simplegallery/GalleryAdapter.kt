package com.example.simplegallery

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*

class GalleryAdapter :
    ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
        inflater.imageView.setOnClickListener {  }
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.shimmerLayout.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position).previewURL)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .addListener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.shimmerLayout?.stopShimmerAnimation() }
                }

            })
            .into(holder.itemView.imageView)
    }

    object DIFFCALLBACK:DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem  //注意三个=表示对象的比较，看地址是否相同
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)