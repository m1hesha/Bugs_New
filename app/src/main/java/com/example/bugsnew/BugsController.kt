package com.example.bugsnew

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.media.MediaPlayer
import android.view.View
import java.util.concurrent.ThreadLocalRandom


class BugsController(bugsCount: Int, view: View) {
    private val view: View
    private var bugsList: ArrayList<Bug>
    private val bugsCount: Int
    var points = 0
    private fun removeDeads() {
        val newBugsList = ArrayList<Bug>(5)
        for (bug in bugsList) {
            if (bug.alive) {
                newBugsList.add(bug)
            }
        }
        bugsList = newBugsList
    }

    fun drawBugs(canvas: Canvas) {
        for (bug in bugsList) {
            canvas.drawBitmap(bug.texture!!, bug.matrix, null)
        }
    }

    private fun createBug() {
        val bug = Bug()
        when (ThreadLocalRandom.current().nextInt(0, 3)) {
            0 -> {
                bug.texture = BitmapFactory.decodeResource(view.resources, R.drawable.bug1)
                bug.bug_type = 0
            }
            1 -> {
                bug.texture = BitmapFactory.decodeResource(view.resources, R.drawable.bug2)
                bug.bug_type = 1
            }
            2 -> {
                bug.texture = BitmapFactory.decodeResource(view.resources, R.drawable.bug3)
                bug.bug_type = 2
            }
        }
        bugsList.add(bug)
        bug.matrix.setRotate(
            0F, (bug.texture!!.width / 2).toFloat(),
            (bug.texture!!.height / 2).toFloat()
        )
        bug.matrix.reset()
        bug.p = 0
        bug.isRunning = false
        val ty: Float
        val tx: Float
        val temp = Math.floor(Math.random() * 4).toInt()
        when (temp) {
            0 -> {
                ty = Math.random().toFloat() * view.height
                bug.x = 0f
                bug.y = ty
            }
            1 -> {
                ty = Math.random().toFloat() * view.height
                bug.x = view.width.toFloat()
                bug.y = ty
            }
            2 -> {
                tx = Math.random().toFloat() * view.width
                bug.x = tx
                bug.y = 0f
            }
            3 -> {
                tx = Math.random().toFloat() * view.width
                bug.x = tx
                bug.y = view.height.toFloat()
            }
        }
        bug.matrix.postTranslate(bug.x, bug.y)
    }

    private fun handleBug(bug: Bug) {
        if (!bug.isRunning) {
            bug.destX = Math.random().toFloat() * view.width
            bug.destY = Math.random().toFloat() * view.height
            if (bug.bug_type === 0) {
                bug.stepX = (bug.destX - bug.x) / 125
                bug.stepY = (bug.destY - bug.y) / 125
            } else if (bug.bug_type === 1) {
                bug.stepX = (bug.destX - bug.x) / 75
                bug.stepY = (bug.destY - bug.y) / 75
            } else if (bug.bug_type === 2) {
                bug.stepX = (bug.destX - bug.x) / 325
                bug.stepY = (bug.destY - bug.y) / 325
            }
            val tp: Int
            tp = if (bug.x <= bug.destX && bug.y >= bug.destY) Math.floor(
                Math.toDegrees(
                    Math.atan(
                        (Math.abs(bug.x - bug.destX) / Math.abs(bug.y - bug.destY)).toDouble()
                    )
                )
            ).toInt() else if (bug.x <= bug.destX && bug.y <= bug.destY) 90 + Math.floor(
                Math.toDegrees(
                    Math.atan(
                        (Math.abs(bug.y - bug.destY) / Math.abs(bug.x - bug.destX)).toDouble()
                    )
                )
            ).toInt() else if (bug.x >= bug.destX && bug.y <= bug.destY) 180 + Math.floor(
                Math.toDegrees(
                    Math.atan(
                        (Math.abs(bug.x - bug.destX) / Math.abs(bug.y - bug.destY)).toDouble()
                    )
                )
            ).toInt() else 270 + Math.floor(
                Math.toDegrees(
                    Math.atan(
                        (Math.abs(bug.y - bug.destY) / Math.abs(bug.x - bug.destX)).toDouble()
                    )
                )
            ).toInt()
            bug.matrix.preRotate(
                (tp - bug.p).toFloat(),
                (bug.texture!!.width / 2).toFloat(), (bug.texture!!.height / 2).toFloat()
            )
            bug.p = tp
            bug.isRunning = true
        } else {
            if (Math.abs(bug.x - bug.destX) < 0.1 &&
                Math.abs(bug.y - bug.destY) < 0.1
            ) bug.isRunning = false
            bug.matrix.postTranslate(bug.stepX!!, bug.stepY!!)
            bug.x += bug.stepX!!
            bug.y += bug.stepY!!
        }
    }

    fun touchEvent(x: Float, y: Float) {
        var hit = false
        for (bug in bugsList) {
            if (Math.abs(bug.x - x + 60) < 140 && Math.abs(bug.y - y + 60) < 150) {
                MediaPlayer.create(view.context, R.raw.hit).start()
                if (bug.bug_type === 0) {
                    points += 50
                    bug.die()
                } else if (bug.bug_type === 1) {
                    points += 150
                    bug.die()
                } else if (bug.bug_type === 2) {
                    bug.bug_hits++
                    if (bug.bug_hits === 3) {
                        points += 100
                        bug.die()
                    }
                }
                hit = true
                break
            }
        }
        if (!hit) {
            MediaPlayer.create(view.context, R.raw.miss).start()
            if (points - 50 >= 0) {
                points -= 50
            }
        }
    }

    init {
        points = 0
        this.view = view
        this.bugsCount = bugsCount
        bugsList = ArrayList(5)
    }

    fun update() {
        removeDeads()
        while (bugsList.size < bugsCount) {
            createBug()
        }
        for (bug in bugsList) {
            Thread { handleBug(bug) }.start()
        }
    }
}