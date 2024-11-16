package com.example.animation

import android.content.ContentValues
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Log

internal class Drop(private val containerWidth: Int,
                     private val containerHeight: Int,
                     private val drawable: Drawable?) {

    private var size: Int = 0
    private var speed: Int = 0
    private var x: Int = 0
    private var y: Int = 0
    private var maxSize = 100
    private var maxSpeed = 20

    init {
        reset()
    }

    private fun reset() {
        size = ((Math.random() * maxSize / 2 + maxSize / 2).toInt())
        speed = ((Math.random() * maxSpeed / 2 + maxSpeed / 2).toInt())
        y = -size;
        x = ((Math.random() * containerWidth).toInt())
    }

    fun update() {
        y += speed
        if (y > containerHeight) {
            reset()
        }
    }

    fun draw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        drawable?.setBounds(x, y, (x + size), (y + size))
        drawable?.draw(canvas)
    }
}