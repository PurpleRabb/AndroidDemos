package com.example.mediatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.controller_bar.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : MediaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this)
        ).get(MediaViewModel::class.java).apply {
            barVisibility.observe(this@MainActivity, Observer {
                progressBar.visibility = it
            })

            videoResolution.observe(this@MainActivity, Observer {
                seekBar.max = mediaPlay.duration
                //注意这里要用post方式，待frameLayout准备好后再调用
                frameLayout.post { resizeVideoSurface(it.first,it.second) }
            })

            controllerBarVisiblity.observe(this@MainActivity, Observer {
                controller_bar.visibility = it
            })
        }
        updateMediaProgress()
        lifecycle.addObserver(viewModel)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                viewModel.mediaPlay.setDisplay(surfaceView.holder)
                viewModel.mediaPlay.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
            }

        })

        frameLayout.setOnClickListener {
            viewModel.toggleControllerBar()
        }
    }

    private fun resizeVideoSurface(width: Int, height: Int) {
        if ( width == 0 || height == 0) return
        surfaceView.layoutParams = FrameLayout.LayoutParams (
            frameLayout.height * width/height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
    }

    private fun updateMediaProgress() {
        lifecycleScope.launch {
            while(true) {
                delay(500)
                //seekBar.max要提前设置
                seekBar.progress = viewModel?.mediaPlay.currentPosition
            }
        }
    }
}