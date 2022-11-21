package com.oguzhandurmaz.kotlincatchthespongebob

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var score = 0
    private lateinit var images: Array<ImageView>
    private lateinit var sharedPreferences: SharedPreferences
    var handler = Handler()
    var runnable = Runnable {}
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        images = arrayOf(
            imageView1, imageView2, imageView3, imageView4, imageView5, imageView6,
            imageView7, imageView8, imageView9
        )
        sharedPreferences = getSharedPreferences(
            "com.oguzhandurmaz.kotlincatchthespongebob",
            Context.MODE_PRIVATE
        )
        countDown()
        runSpongeBob()
    }

    private fun runSpongeBob() {
        runnable = Runnable {
            hideSpongeBobs()
            //val rand=Random.nextInt(0,9)
            val randomIndex = (0..8).random()
            images[randomIndex].visibility = View.VISIBLE
            handler.postDelayed(runnable, 500)
        }
        handler.post(runnable)
    }

    private fun countDown() {
        countDownTimer = object : CountDownTimer(15200, 1000) {
            override fun onTick(p0: Long) {
                timeText.text = "Time : ${p0 / 1000}"
            }

            override fun onFinish() {
                //stop runnable
                handler.removeCallbacks(runnable)
                hideSpongeBobs()
                //check highscore
                var highScore = sharedPreferences.getInt("highScore", 0)
                if (score > highScore) {
                    highScore = score
                    sharedPreferences.edit().putInt("highScore", score).apply()
                }
                //alert dialog
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setCancelable(false) //you have to click "Play Again" button.
                alert.setTitle("Game Over")
                alert.setMessage("Your Score : $score \nHigh Score : $highScore")
                alert.setNeutralButton("Play Again") { dialog, which ->
                    //restart activity
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.show()
            }
        }.start()
    }

    private fun hideSpongeBobs() {
        images.forEach { it.visibility = View.INVISIBLE }
    }

    fun increaseScore(view: View) {
        score++
        scoreText.text = "Score : $score"
    }
}