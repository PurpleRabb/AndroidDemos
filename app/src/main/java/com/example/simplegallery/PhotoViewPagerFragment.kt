package com.example.simplegallery

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_photo_view_pager.*
import kotlinx.android.synthetic.main.viewpager_cell.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoViewPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_view_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("PHOTO_LIST")
        ViewPagerAdapter().apply {
            viewPager2.adapter = this
            submitList(photoList)
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                photoTag.text = "${position + 1}/${photoList?.size}"
            }
        })

        viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)
        //viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        saveButton.setOnClickListener {
            //动态权限判断，29之后写入权限放开，之前要动态申请
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    savePhoto() //携程
                }
            }
        }
    }

    private suspend fun savePhoto() {
        //Toast.makeText(requireContext(),"存储成功",Toast.LENGTH_SHORT).show()
        //注意这里的view的获取方式,viewPager2里面封装的是一个recyclerView
        withContext(Dispatchers.IO) {
            val holder =
                (viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewPager2.currentItem) as PagerViewHolder
            val bitmap = holder.itemView.imageView2.drawable.toBitmap()
            if (Build.VERSION.SDK_INT < 29) {  //存储图片 API < 29
                val result =
                    MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        bitmap,
                        "",
                        ""
                    )
                if (result != null)
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            result,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                else
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储失败",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                val saveUri = requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues()
                ) ?: kotlin.run {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储失败",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@withContext
                }
                requireContext().contentResolver.openOutputStream(saveUri).use {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                        MainScope().launch {
                            Toast.makeText(
                                requireContext(),
                                "存储成功",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        MainScope().launch {
                            Toast.makeText(
                                requireContext(),
                                "存储失败",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //授权回调
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto() //携程
                    }
                } else {
                    Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoViewPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}