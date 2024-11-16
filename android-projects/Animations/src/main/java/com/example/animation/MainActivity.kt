package com.example.animation

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import com.example.animation.databinding.ActivityMainBinding
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var circleState by notNull<Float>()
    private var alphaValue by notNull<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            circleState = 0f
            alphaValue = 0f
        } else {
            circleState = savedInstanceState.getFloat(KEY_ROTATION)
            val k = (circleState / 360f).toInt()
            circleState -= k * 360
        }
    }

    override fun onResume() {
        super.onResume()
        val va = ValueAnimator.ofFloat(circleState, circleState + 360f)
        va.duration = 80
        va.interpolator = LinearInterpolator()
        va.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            binding.view.rotation = animatedValue
            circleState = animatedValue
        }
        va.repeatCount = INFINITE
        va.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(KEY_ROTATION, circleState)
    }

    companion object {
        @JvmStatic private val KEY_ROTATION = "ROTATION"
    }
}
