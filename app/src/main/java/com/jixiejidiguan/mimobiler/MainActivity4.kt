package com.jixiejidiguan.mimobiler

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 设置WebView
        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        // 启用JavaScript
        webView.settings.javaScriptEnabled = true
        // 设置WebView自适应屏幕大小
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                // 可以在这里处理标题
            }
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                // 处理全屏视频等自定义视图
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                // 检测到特定的链接或动作
                if (url.startsWith("weixin://") || url.startsWith("alipay://") || url.startsWith("mi://")) {
                    // 打开其他应用程序
                    openOtherApp(url)
                    return true // 返回true表示我们已经处理了这个URL
                }
                return false // 返回false表示让WebView加载这个URL
            }
        }

        // 获取传递的数据
        val postData = intent.getStringExtra("postData")
        // 加载带有请求体的URL
        loadUrlWithPostData("https://product.10046.mi.com/charge/recharge", postData.toString())

    }

    private fun loadUrlWithPostData(url: String, postData: String) {
        webView.postUrl(url, postData.toByteArray(Charsets.UTF_8))
    }
    private fun openOtherApp(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val packageManager: PackageManager = this.packageManager
        val list: List<ResolveInfo> = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        for (info in list) {
            if (info.activityInfo.packageName == "com.tencent.mm" || // 微信
                info.activityInfo.packageName == "com.eg.android.AlipayGphone" || // 支付宝
                info.activityInfo.packageName == "com.miui.securitycenter" // 小米安全中心
            ) {
                startActivity(intent)
                break
            }
        }
    }
}