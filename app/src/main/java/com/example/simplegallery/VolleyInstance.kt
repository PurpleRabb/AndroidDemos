package com.example.simplegallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyInstance private constructor(context: Context) {
    companion object { //相当于java的static
        private var INSTANCE: VolleyInstance? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                VolleyInstance(context).also { INSTANCE = it }
            }
    }

    var requestQueue:RequestQueue = Volley.newRequestQueue(context.applicationContext)
}