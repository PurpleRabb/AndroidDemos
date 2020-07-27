package com.example.simplegallery

import android.graphics.drawable.Drawable
import android.icu.lang.UCharacter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import kotlinx.android.synthetic.main.gallery_cell.view.*
import java.util.*
import kotlin.collections.ArrayList as ArrayList1

class GalleryAdapter :
    ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {
    companion object {
        const val VIEW_NORMAL = 0
        const val VIEW_FOOTER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var holder: MyViewHolder
        if (viewType == VIEW_NORMAL) {
            val inflater =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            holder = MyViewHolder(inflater)
            inflater.imageView.setOnClickListener {
                val bundle: Bundle = Bundle().apply {
                    //putParcelable("photo_item",getItem(holder.adapterPosition))
                    putParcelableArrayList("PHOTO_LIST", ArrayList(currentList))
                    putInt("PHOTO_POSITION", holder.adapterPosition)
                }
                holder.itemView.findNavController()
                    .navigate(R.id.action_galleryFragment_to_photoViewPagerFragment, bundle)
            }
        } else {
            holder = MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_footer, parent, false).also {
                    (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                }
            )
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == itemCount - 1) {
            return
        }
        holder.itemView.shimmerLayout.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position).previewURL)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .addListener(object : RequestListener<Drawable> {
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
                    return false.also {
                        holder.itemView.textViewLikes.text = getItem(position).likes.toString()
                        holder.itemView.textViewFavorites.text =
                            getItem(position).favorites.toString()
                        holder.itemView.shimmerLayout?.stopShimmerAnimation()
                    }
                }

            })
            .into(holder.itemView.imageView)
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem  //注意三个=表示对象的比较，看地址是否相同
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }
    }

    override fun getItemViewType(position: Int): Int {
        //这里返回的类型会传递给onCreateViewHolder里的viewType
        if (position == itemCount - 1) {
            return VIEW_FOOTER
        } else {
            return VIEW_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 //增加一个位置给footer
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)