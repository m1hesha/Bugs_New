package com.example.bugsnew

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*



class MainActivity : AppCompatActivity() {
    private var view: BugView? = null
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = BugView(this)
        setContentView(view)
        handler = Handler()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler!!.post { view!!.invalidate() }
            }
        }, 0, interval.toLong())
        if (intent.hasExtra("score")) {
            val score = intent.getIntExtra("score", 0)
            showScoreDialog(score)
        }
    }

    private fun showScoreDialog(score: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Ваш счёт: $score")
            .setCancelable(false)
            .setPositiveButton("Новая игра") { dialog, id ->
                val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Выйти") { dialog, id ->
                finish()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Результат")
        alert.show()
    }



    companion object {
        private const val interval = 1000 / 60 // 60 fps
    }
}