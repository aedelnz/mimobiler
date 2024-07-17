@file:Suppress("DEPRECATION")

package com.jixiejidiguan.mimobiler

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

data class ResponseData(
    @SerializedName("rtnMsg") val rtnMsg: String,
    @SerializedName("result") val result: Result
)

data class Result(
    @SerializedName("details") val details: List<Detail>
)

data class Detail(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("user_category") val userCategory: String,
    @SerializedName("business_code") val businessCode: String,
    @SerializedName("status") val status: Int,
    @SerializedName("limit_type") val limitType: Int,
    @SerializedName("limit_traffic") val limitTraffic: Int,
    @SerializedName("limit_status") val limitStatus: Int,
    @SerializedName("limit_policy") val limitPolicy: Int,
    @SerializedName("limit_biz_desc") val limitBizDesc: String,
    @SerializedName("balance") val balance: Int,
    @SerializedName("current_month_consumption") val currentMonthConsumption: Int,
    @SerializedName("current_traffic_amount") val currentTrafficAmount: Int,
    @SerializedName("current_month_traffic_consumption") val currentMonthTrafficConsumption: Int,
    @SerializedName("current_total_calls_time") val currentTotalCallsTime: Int,
    @SerializedName("vpn_total_calls_time") val vpnTotalCallsTime: Int,
    @SerializedName("current_month_call_consumption") val currentMonthCallConsumption: Int,
    @SerializedName("current_month_day_auto_consumption") val currentMonthDayAutoConsumption: Int,
    @SerializedName("current_month_last_cdr_timestamp") val currentMonthLastCdrTimestamp: Long,
    @SerializedName("call_price") val callPrice: Int,
    @SerializedName("traffic_price") val trafficPrice: Int,
    @SerializedName("mno_code") val mnoCode: String,
    @SerializedName("order_packages") val orderPackages: List<List<String>>,
    @SerializedName("business_detail") val businessDetail: Int,
    @SerializedName("package_assistant") val packageAssistant: String
)

class MainActivity2 : AppCompatActivity() {

    private lateinit var cardview: CardView
    private lateinit var cardview1: CardView
    private lateinit var textview4: TextView
    private lateinit var textview5: TextView
    private lateinit var textview6: TextView
    private lateinit var textview11: TextView
    private lateinit var textview12: TextView
    private lateinit var textview13: TextView
    private lateinit var textview14: TextView
    private lateinit var textview15: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cardview = findViewById(R.id.cardview)
        cardview1 = findViewById(R.id.cardview1)
        textview4 = findViewById(R.id.textview4)
        textview5 = findViewById(R.id.textview5)
        textview6 = findViewById(R.id.textview6)
        textview11 = findViewById(R.id.textview11)
        textview12 = findViewById(R.id.textview12)
        textview13 = findViewById(R.id.textview13)
        textview14 = findViewById(R.id.textview14)
        textview15 = findViewById(R.id.textview15)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)

        cardview.setOnClickListener {
            startActivity(Intent(this@MainActivity2, MainActivity3::class.java))
            finish()
        }

        val packageManager: PackageManager = this.packageManager
        val packageName: String = this.packageName
        val versionName: String = packageManager.getPackageInfo(packageName, 0).versionName
        val versionCode: Int = packageManager.getPackageInfo(packageName, 0).versionCode
        textview4.text = "在线版本: 无法获取"
        textview5.text = "本地版本: $versionName"
        textview6.text = "版本号: $versionCode"

        sendRequest()
    }

    private fun sendRequest() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://service.10046.mi.com/miuser/info/v2")
            .addHeader("Cookie", cookies())
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 请求失败处理
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                // 请求成功处理
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    // 处理响应体
                    runOnUiThread {
                        gsonjson(responseBody)
                        selectPay()
                    }
                }
            }
        })
    }

    private fun cookies(): String {
        val filename = "cookie.txt"
        val file = File(this@MainActivity2.filesDir, filename)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return "JSESSIONID=-;"
            }
        }
        return try {
            val reader = BufferedReader(FileReader(file))
            val text = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                text.append(line)
                line = reader.readLine()
            }
            reader.close()
            text.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            "JSESSIONID=-;"
        }
    }
    @SuppressLint("SetTextI18n")
    private fun gsonjson(responseBody: String?) {
        val gson = Gson()
        val responseData = gson.fromJson(responseBody, ResponseData::class.java)
        Toast.makeText(this@MainActivity2, responseData.rtnMsg, Toast.LENGTH_SHORT).show()
        responseData.result.details.forEach { detail ->
            textview11.text = detail.phoneNumber
            textview12.text = "账户余额(元): %.2f".format(detail.balance.toString().substring(0, 4).toInt() / 100.0)
            textview13.text = "当月消费(元): %.2f".format(detail.currentMonthConsumption.toString().substring(0, 4).toInt() / 100.0)
            textview14.text = "本月剩余流量(GB): %.2f".format( megabytesToGigabytes(detail.trafficPrice.toDouble() - bytesToMegabytes(detail.currentTrafficAmount.toDouble())))
            textview15.text = "本月通话时长(分钟): "+ (detail.currentTotalCallsTime / 60)
        }
    }
    private fun bytesToMegabytes(bytes: Double): Double {
        val bytesPerMegabyte = 1024L * 1024L
        return bytes / bytesPerMegabyte
    }
    private fun megabytesToGigabytes(megabytes: Double): Double {
        val megabytesPerGigabyte = 1000L
        return megabytes / megabytesPerGigabyte
    }

    private fun selectPay() {
        val phoneNumber = textview11.text
        val intent = Intent(this, MainActivity4::class.java)
        button1.setOnClickListener {startActivity(intent.putExtra("postData", "amount=10&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button2.setOnClickListener {startActivity(intent.putExtra("postData", "amount=100&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button3.setOnClickListener {startActivity(intent.putExtra("postData", "amount=1000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button4.setOnClickListener {startActivity(intent.putExtra("postData", "amount=2000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button5.setOnClickListener {startActivity(intent.putExtra("postData", "amount=3000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button6.setOnClickListener {startActivity(intent.putExtra("postData", "amount=5000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button7.setOnClickListener {startActivity(intent.putExtra("postData", "amount=10000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button8.setOnClickListener {startActivity(intent.putExtra("postData", "amount=20000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
        button9.setOnClickListener {startActivity(intent.putExtra("postData", "amount=30000&phoneNumber=$phoneNumber&op_wxpay=1&type=0&channel=0&promotionCode="))}
    }

}