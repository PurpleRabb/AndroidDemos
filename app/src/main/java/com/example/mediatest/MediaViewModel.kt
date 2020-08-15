package com.example.mediatest

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import androidx.lifecycle.*

class MediaViewModel(application: Application) : AndroidViewModel(application),LifecycleObserver {
    private val _visibility : MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    private var _controllerBarVisiblity : MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    private val _videoResolution = MutableLiveData(Pair(0,0))
    var controllerBarVisiblity : LiveData<Int> = _controllerBarVisiblity
    val barVisibility : LiveData<Int> = _visibility
    val videoResolution : LiveData<Pair<Int,Int>> = _videoResolution
    val mediaPlay: MediaPlayer = MediaPlayer()
    private val myApplication = application

    init {
        loadVideo()
    }

    private fun loadVideo() {
        val file : AssetFileDescriptor = myApplication.getResources().openRawResourceFd(R.raw.cup)
        mediaPlay.apply {
            setDataSource(file)
            prepare()
            setOnPreparedListener {
                _visibility.value = View.INVISIBLE
                it.start()
            }
            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width,height)
            }
        }
    }

    fun toggleControllerBar() {
        if (_controllerBarVisiblity.value == View.VISIBLE) {
            _controllerBarVisiblity.value = View.INVISIBLE
        } else {
            _controllerBarVisiblity.value = View.VISIBLE
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer() {
        Log.i("111111", "pausePlayer: ")
        mediaPlay.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer() {
        Log.i("111111", "resumePlayer: ")
        mediaPlay.start()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlay.release()
    }
}