package com.example.simplegallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoList = MutableLiveData<List<PhotoItem>>() //私有化，不让外界获得Mutable
    val photoList:LiveData<List<PhotoItem>> //通过get，让外界获取LiveData而不是Mutable
            get() = _photoList

    fun fetchData() {
        val mykey = "12472743-874dc01dadd26dc44e0801d61"
        val stringRequest = StringRequest(
            Method.GET,
            "https://pixabay.com/api/?key=${mykey}&q=yellow+flowers&image_type=photo",
            Response.Listener {
                //数据获取成功后的回调
                this._photoList.value = Gson().fromJson(it,Pixabay::class.java).hits.toList()
                Log.i("GalleryViewModel", "fetchData: "+ photoList.value?.size.toString())
            },
            Response.ErrorListener {
                Log.d("GalleryViewModel", "fetchData: $it")
            }
        )
        VolleyInstance.getInstance(getApplication()).requestQueue.add(stringRequest)
    }
}