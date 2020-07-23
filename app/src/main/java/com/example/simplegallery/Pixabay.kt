package com.example.simplegallery

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Pixabay (
    var total:Int,
    var totalHits:Int,
    var hits:Array<PhotoItem>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        TODO("hints")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(total)
        parcel.writeInt(totalHits)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pixabay> {
        override fun createFromParcel(parcel: Parcel): Pixabay {
            return Pixabay(parcel)
        }

        override fun newArray(size: Int): Array<Pixabay?> {
            return arrayOfNulls(size)
        }
    }
}

data class PhotoItem (
    @SerializedName("id") var photoId:Int,
    @SerializedName("previewURL") var previewURL:String,
    @SerializedName("largeImageURL") var largeImageURL:String
)