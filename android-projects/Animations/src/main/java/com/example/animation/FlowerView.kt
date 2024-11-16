package com.example.animation

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import java.lang.Math.max
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.properties.Delegates

class FlowerView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View (context, attributeSet, defStyleAttr, defStyleRes) {

    private var budColor by Delegates.notNull<Int>()
    private var stemColor by Delegates.notNull<Int>()
    private lateinit var budPaint: Paint
    private lateinit var stemPaint: Paint
    private lateinit var cloudPaint: Paint

    private val fieldRect = RectF(0f, 0f, 0f, 0f)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : this(context, attributeSet, defStyleAttr, R.style.DefaultFlowerStyle)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, R.attr.flowerViewStyle)
    constructor(context: Context): this(context, null)

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        } else {
            initDefaultColor()
        }
        initPaints()
    }

    private fun initPaints() {
        budPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        budPaint.color = budColor
        budPaint.style = Paint.Style.FILL_AND_STROKE
        budPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)

        stemPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        stemPaint.color = stemColor
        stemPaint.style = Paint.Style.FILL_AND_STROKE
        stemPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)

        cloudPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        cloudPaint.color = Color.DKGRAY
        cloudPaint.style = Paint.Style.FILL
        cloudPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    }

    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FlowerView, defStyleAttr, defStyleRes)
        budColor = typedArray.getColor(R.styleable.FlowerView_budColor, BUD_DEFAULT_COLOR)
        stemColor = typedArray.getColor(R.styleable.FlowerView_stemColor, STEM_DEFAULT_COLOR)
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val safeWidth = w - paddingLeft - paddingRight
        val safeHeight = h - paddingTop - paddingBottom

        fieldRect.left = paddingLeft.toFloat()
        fieldRect.top = paddingTop.toFloat()
        fieldRect.right = fieldRect.left + safeWidth
        fieldRect.bottom = fieldRect.top + safeHeight
    }

    private lateinit var drops: Array<Drop>

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        drops = Array(12, {
            Drop(right - left, bottom - top,
                context.getDrawable(R.drawable.icons8_droplet_48))})
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drops.forEach {
            it.update()
            it.draw(canvas)
        }
        val stemX = fieldRect.right / 2
        val centerYCircle = fieldRect.bottom * 3 / 8
        val radiusCircle = fieldRect.bottom / 8
        val sheetWidth = fieldRect.right / 5
        val sheetHeight = fieldRect.right / 10
        canvas.drawLine(stemX, fieldRect.bottom, fieldRect.right / 2,
            fieldRect.bottom / 2 + sheetWidth, stemPaint)
        val oval = RectF()
        drawSheet(oval, stemX, fieldRect.right * 6 / 8, fieldRect.bottom * 16 / 20,
            fieldRect.bottom * 14 / 20, 180f,  canvas, stemPaint)
        drawSheet(oval, stemX, fieldRect.right * 6 / 8, fieldRect.bottom * 16 / 20,
            fieldRect.bottom * 14 / 20, 0f,  canvas, stemPaint)
        canvas.drawCircle(stemX, centerYCircle, radiusCircle, budPaint)
        drawSheet(oval, radiusCircle + stemX, stemX + radiusCircle + sheetWidth,
            centerYCircle - sheetHeight / 2, centerYCircle + sheetHeight / 2,
            0f, canvas, budPaint)
        drawSheet(oval, stemX - radiusCircle, stemX - radiusCircle + sheetWidth,
            centerYCircle - sheetHeight / 2, centerYCircle + sheetHeight / 2,
            180f, canvas, budPaint)
        drawSheet(oval, stemX, stemX + sheetWidth,
            centerYCircle - radiusCircle - sheetHeight / 2,
            centerYCircle - radiusCircle + sheetHeight / 2,
            270f, canvas, budPaint)
        drawSheet(oval, stemX, stemX + sheetWidth,
            centerYCircle + radiusCircle - sheetHeight / 2,
            centerYCircle + radiusCircle + sheetHeight / 2,
            90f, canvas, budPaint)
        drawSheet(oval, stemX + radiusCircle * (3.0.pow(0.5)).toFloat() / 2,
            stemX + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetWidth,
            centerYCircle + radiusCircle / 2 - sheetHeight / 2,
            centerYCircle + radiusCircle / 2 + sheetHeight / 2,
            30f, canvas, budPaint)
        drawSheet(oval, stemX + radiusCircle * (3.0.pow(0.5)).toFloat() / 2,
            stemX + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetWidth,
            centerYCircle - radiusCircle / 2 - sheetHeight / 2,
            centerYCircle - radiusCircle / 2 + sheetHeight / 2,
            330f, canvas, budPaint)
        drawSheet(oval, stemX - radiusCircle * (3.0.pow(0.5)).toFloat() / 2,
            stemX - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetWidth,
            centerYCircle + radiusCircle / 2 - sheetHeight / 2,
            centerYCircle + radiusCircle / 2 + sheetHeight / 2,
            150f, canvas, budPaint)
        drawSheet(oval, stemX - radiusCircle * (3.0.pow(0.5)).toFloat() / 2,
            stemX - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetWidth,
            centerYCircle - radiusCircle / 2 - sheetHeight / 2,
            centerYCircle - radiusCircle / 2 + sheetHeight / 2,
            210f, canvas, budPaint)
        drawSheet(oval, stemX + radiusCircle / 2,
            stemX + radiusCircle / 2 + sheetWidth,
            centerYCircle + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 - sheetHeight / 2,
            centerYCircle + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetHeight / 2,
            60f, canvas, budPaint)
        drawSheet(oval, stemX + radiusCircle / 2,
            stemX + radiusCircle / 2 + sheetWidth,
            centerYCircle - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 - sheetHeight / 2,
            centerYCircle - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetHeight / 2,
            300f, canvas, budPaint)
        drawSheet(oval, stemX - radiusCircle / 2,
            stemX - radiusCircle / 2 + sheetWidth,
            centerYCircle + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 - sheetHeight / 2,
            centerYCircle + radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetHeight / 2,
            120f, canvas, budPaint)
        drawSheet(oval, stemX - radiusCircle / 2,
            stemX - radiusCircle / 2 + sheetWidth,
            centerYCircle - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 - sheetHeight / 2,
            centerYCircle - radiusCircle * (3.0.pow(0.5)).toFloat() / 2 + sheetHeight / 2,
            240f, canvas, budPaint)
        canvas.drawArc(fieldRect.left, fieldRect.top - fieldRect.right / 12, fieldRect.right * 3 / 11,
            fieldRect.top + fieldRect.right / 12, 0f, 180f, false, cloudPaint)
        canvas.drawArc(fieldRect.left + fieldRect.right * 3 / 11, fieldRect.top - fieldRect.right / 10,
            fieldRect.right * 7 / 11, fieldRect.top + fieldRect.right / 10, 0f,
            180f, false, cloudPaint)
        canvas.drawArc(fieldRect.left + fieldRect.right * 7 / 11,
            fieldRect.top - fieldRect.right / 9, fieldRect.right,
            fieldRect.top + fieldRect.right / 9, 0f,
            180f, false, cloudPaint)
        postInvalidateOnAnimation()
    }

    private fun drawSheet(oval: RectF, left: Float, right: Float, top: Float, bot: Float, angle: Float, canvas: Canvas?, paint: Paint) {
        canvas?.rotate(angle, left, (top + bot) / 2)
        oval.set(left, top, right, bot)
        canvas?.drawOval(oval, paint)
        canvas?.rotate(-angle, left, (top + bot) / 2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        val desiredFlowerSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            DESIRED_VIEW_SIZE, resources.displayMetrics).toInt()
        val desiredWidth = max(minWidth, desiredFlowerSizeInPixels + paddingRight + paddingLeft)
        val desiredHeight = max(minHeight, desiredFlowerSizeInPixels + paddingTop + paddingBottom)
        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    private fun initDefaultColor() {
        budColor = BUD_DEFAULT_COLOR
        stemColor = STEM_DEFAULT_COLOR
    }

    companion object {
        const val BUD_DEFAULT_COLOR = Color.RED
        const val STEM_DEFAULT_COLOR= Color.GREEN

        const val DESIRED_VIEW_SIZE = 350f
    }
}

