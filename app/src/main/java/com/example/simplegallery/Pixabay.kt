package com.example.simplegallery

import com.google.gson.annotations.SerializedName


data class Pixabay (
    var total:Int,
    var totalHints:Int,
    var hints:Array<PhotoItem>
)

data class PhotoItem (
    @SerializedName("id") var photoId:Int,
    @SerializedName("previewURL") var previewURL:String,
    @SerializedName("largeImageURL") var largeImageURL:String
)