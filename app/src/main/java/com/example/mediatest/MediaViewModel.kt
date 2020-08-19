package com.example.mediatest

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PlayStatus {
    PLAYING,
    PAUSE,
    REPLAY,
    NOT_READY,
}

class MediaViewModel(application: Application) : AndroidViewModel(application),LifecycleObserver {
    private val _visibility : MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    private var _controllerBarVisiblity : MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    private val _videoResolution = MutableLiveData(Pair(0,0))
    private val _playStatus : MutableLiveData<PlayStatus> = MutableLiveData(PlayStatus.NOT_READY)
    private var controllerShowTime = 0L
    var controllerBarVisiblity : LiveData<Int> = _controllerBarVisiblity
    val barVisibility : LiveData<Int> = _visibility
    val videoResolution : LiveData<Pair<Int,Int>> = _videoResolution
    var playStatus : LiveData<PlayStatus> = _playStatus
    val mediaPlay: MediaPlayer = MediaPlayer()
    private val myApplication = application

    init {
        loadVideo()
    }

    private fun loadVideo() {
        val file : AssetFileDescriptor = myApplication.getResources().openRawResourceFd(R.raw.cup)
        _playStatus.value = PlayStatus.NOT_READY
        mediaPlay.apply {
            setDataSource(file)
            //prepare()
            setOnPreparedListener {
                _visibility.value = View.INVISIBLE
                it.start()
                _playStatus.value = PlayStatus.PLAYING
            }
            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width,height)
            }
            setOnCompletionListener {
                _playStatus.value = PlayStatus.REPLAY
            }
            prepareAsync()
        }
    }

    fun toggleControllerBar() {
        if (_controllerBarVisiblity.value == View.VISIBLE) {
            _controllerBarVisiblity.value = View.INVISIBLE
            controllerShowTime = System.currentTimeMillis()
        } else {
            _controllerBarVisiblity.value = View.VISIBLE
            viewModelScope.launch {
                delay(3000)
                if(System.currentTimeMillis() - controllerShowTime > 3000) {//保证3s后消失
                    _controllerBarVisiblity.value = View.INVISIBLE
                }
            }
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

    fun togglePlayStatus() {
        when (_playStatus.value) {
            PlayStatus.PLAYING -> {
                mediaPlay.pause()
                _playStatus.value = PlayStatus.PAUSE
            }
            PlayStatus.PAUSE -> {
                mediaPlay.start()
                _playStatus.value = PlayStatus.PLAYING
            }
            PlayStatus.REPLAY -> {
                mediaPlay.start()
                _playStatus.value = PlayStatus.PLAYING
            }
            else -> {
                return
            }
        }
    }
}