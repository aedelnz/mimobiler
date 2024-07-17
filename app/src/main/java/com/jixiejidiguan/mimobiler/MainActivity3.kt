package com.jixiejidiguan.mimobiler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class MainActivity3 : AppCompatActivity() {

    private lateinit var cookieInput: EditText
    private lateinit var getVerificationCodeButton: Button
    private lateinit var getllqbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cookieInput = findViewById(R.id.cookie_input)
        getVerificationCodeButton = findViewById(R.id.get_verification_code_button)
        getllqbutton = findViewById(R.id.get_llq_button)

        main1()
        getVerificationCodeButton.setOnClickListener { main() }
        getllqbutton.setOnClickListener { openWebBrowser("https://service.10046.mi.com/") }


    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed() // 调用父类的onBackPressed()方法
        startActivity(Intent(this@MainActivity3, MainActivity2::class.java))
        finish() // 结束当前Activity
    }

    private fun main() {

        val filename = "cookie.txt"
        val file = File(this@MainActivity3.filesDir, filename)

        val input = cookieInput.text.toString()
        if (isValidInput(input)) {
            try {
                val writer = file.bufferedWriter()
                writer.write(input)
                writer.close()
                // 文件写入成功
                Toast.makeText(this@MainActivity3, "文件写入成功！", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                // 文件写入失败
                e.printStackTrace()
                Toast.makeText(this@MainActivity3, "文件写入失败！", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 输入验证失败
            Toast.makeText(this@MainActivity3, "输入包含非法字符！", Toast.LENGTH_SHORT).show()
        }

    }
    private fun  main1() {
        val filename = "cookie.txt"
        val file = File(this@MainActivity3.filesDir, filename)

        try {
            val reader = BufferedReader(FileReader(file))
            val text = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                text.append(line)
                line = reader.readLine()
            }
            reader.close()
            // 文件读取成功
            cookieInput.text = Editable.Factory.getInstance().newEditable(text.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun isValidInput(input: String): Boolean {
        val regex = "^[^\\u4e00-\\u9fa5]+$".toRegex()
        return regex.matches(input)
    }
    private fun openWebBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // 没有应用可以处理这个Intent
            Toast.makeText(this, "没有找到可以打开该链接的应用", Toast.LENGTH_SHORT).show()
        }
    }
}