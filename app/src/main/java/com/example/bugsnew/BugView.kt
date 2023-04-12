package com.example.bugsnew

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View


class BugView(var context_: Context) : View(context_) {
    private val background: Bitmap
    private val score: Paint
    var sc = 0
    var bugsController: BugsController

    init {
        bugsController = BugsController(5, this)
        background = BitmapFactory.decodeResource(context_.resources, R.drawable.desk)
        score = Paint()
        score.color = Color.LTGRAY
        score.textAlign = Paint.Align.CENTER
        score.textSize = 85f
        score.typeface = Typeface.DEFAULT_BOLD
        score.isAntiAlias = true
    }

    private val gameDuration = 30000L // 30 seconds in milliseconds
    private var gameTimer: CountDownTimer? = null
    private var remainingTime = gameDuration

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bugsController.update()
        sc = bugsController.points
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("Счёт: $sc", width /6f, 100f, score)
        bugsController.drawBugs(canvas)
        drawTimer(canvas)
        if (gameTimer == null) {
            startGameTimer()
        }
    }

    private fun drawTimer(canvas: Canvas) {
        val seconds = remainingTime / 1000
        canvas.drawText("Время: $seconds", width - 222f, 100f, score)
    }

    private fun startGameTimer() {
        gameTimer = object : CountDownTimer(gameDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                invalidate()
            }

            override fun onFinish() {
                val intent = Intent(context_, MainActivity::class.java)
                intent.putExtra("score", sc)
                context_.startActivity(intent)
                (context_ as Activity).finish()
            }
        }
        gameTimer?.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val eventX = event.x
            val eventY = event.y
            bugsController.touchEvent(eventX, eventY)
        }
        return true
    }
}