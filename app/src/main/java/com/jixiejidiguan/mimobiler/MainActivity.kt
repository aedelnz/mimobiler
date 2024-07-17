package com.jixiejidiguan.mimobiler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var skipButton: Button
    private lateinit var textView: View
    private var countdown: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        skipButton = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
        textView.setOnClickListener { textViews() }
        skipButton.setOnClickListener { skipCountdown() }

        startCountdown()

    }
    private fun startCountdown() {
        countdown = object : CountDownTimer(5000, 1000) { // 10秒倒计时
            override fun onTick(millisUntilFinished: Long) {
                // 更新按钮文本为剩余时间
                "跳过 ${millisUntilFinished / 1000}".also { skipButton.text = it }
            }

            override fun onFinish() {
                // 倒计时结束，可以在这里执行跳转或其他操作
                skipButton.isEnabled = true
                skipButton.text = "跳过"
                skipCountdown()
            }
        }.start()
    }

    private fun textViews() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://qm.qq.com/q/Q2w1g5q4Qo"))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // 没有应用可以处理这个Intent
            Toast.makeText(this, "没有找到可以打开该链接的应用", Toast.LENGTH_SHORT).show()
        }

    }

    private fun skipCountdown() {
        countdown?.cancel() // 取消倒计时
        startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        finish() // 结束当前Activity
    }

    override fun onDestroy() {
        super.onDestroy()
        countdown?.cancel() // 确保倒计时被取消
    }
}