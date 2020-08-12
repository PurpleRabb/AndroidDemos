package com.example.mediatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.SavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : MediaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var viewModel = ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this)
        ).get(MediaViewModel::class.java)

        viewModel.barVisibility.observe(this, Observer {
            progressBar.visibility = it
        })

        viewModel.videoResolution.observe(this, Observer {
            //注意这里要用post方式，待frameLayout准备好后再调用
            frameLayout.post { resizeVideoSurface(it.first,it.second) }
        })

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
    }

    private fun resizeVideoSurface(width: Int, height: Int) {
        if ( width == 0 || height == 0) return
        surfaceView.layoutParams = FrameLayout.LayoutParams (
            frameLayout.height * width/height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
    }
}