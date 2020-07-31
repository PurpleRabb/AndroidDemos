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
import kotlin.math.ceil

const val DATA_CAN_LOAD = 0
const val DATA_NO_MORE = 1
const val NETWORK_ERROR = 2

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private var _DataStatus = MutableLiveData<Int>()
    var DataStatus:LiveData<Int> = _DataStatus
    private val _photoList = MutableLiveData<List<PhotoItem>>() //私有化，不让外界获得Mutable
    private val mkey = "12472743-874dc01dadd26dc44e0801d61"
    private val perPage = 50
    private var page = 1
    private var totalPages  = 0
    private var keyword : String = "green+yellow+flower"
    private var isLoading = false
    private val TAG = "GalleryViewModel"
    //private var loadingFinish = false

    var scrollToTop = true

    val photoList:LiveData<List<PhotoItem>> //通过get，让外界获取LiveData而不是Mutable
            get() = _photoList

    fun fetchReset(keyword: String) {
        Log.i(TAG,"fetchReset=$keyword")
        page = 1
        scrollToTop = true
        this.keyword = keyword
        isLoading = false
        fetchData(keyword)
    }

    fun fetchData(keyword:String) {
        if (isLoading) {
            return
        }
        if (page == totalPages || totalPages == 1) {
            _DataStatus.value = DATA_NO_MORE
            return
        }
        Log.i(TAG,"fetchData=$keyword")
        val stringRequest = StringRequest(
            Method.GET,
            "https://pixabay.com/api/?key=${mkey}&q=${keyword}&image_type=photo&per_page=${perPage}&page=${page}",
            Response.Listener {
                //数据获取成功后的回调
                val gson = Gson().fromJson(it,Pixabay::class.java)
                if (page == 1) {
                    this._photoList.value = gson.hits.toList()
                } else {
                    _DataStatus.value = DATA_CAN_LOAD
                    this._photoList.value = this._photoList.value?.plus(gson.hits.toList())
                }
                //loadingFinish = this._photoList.value?.size == gson.totalHits
                Log.i(TAG,this._photoList.value?.size.toString() +"/" + gson.totalHits)
                totalPages  = ceil((gson.totalHits.toDouble()/perPage)).toInt()
                isLoading = false
                page++
                Log.i(TAG, "fetchData: $totalPages")
            },
            Response.ErrorListener {
                Log.d(TAG, "fetchData: $it")
                isLoading = false
                _DataStatus.value = NETWORK_ERROR
            }
        )
        VolleyInstance.getInstance(getApplication()).requestQueue.add(stringRequest)
    }


    fun getCurrentKeyWord() : String {
        return this.keyword
    }
}