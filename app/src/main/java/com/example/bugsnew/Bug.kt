package com.example.bugsnew

import android.graphics.Bitmap
import android.graphics.Matrix


internal class Bug {
    var isRunning = false
    var matrix: Matrix
    var alive: Boolean
    var texture: Bitmap? = null
    var x: Float
    var y: Float
    var stepX: Float? = null
    var stepY: Float? = null
    var destX: Float
    var destY: Float
    var p: Int
    var bug_type = 0
    var bug_hits = 0

    init {
        matrix = Matrix()
        x = 0f
        y = 0f
        p = 0
        destX = 0f
        destY = 0f
        alive = true
    }

    fun die() {
        alive = false
    }
}
