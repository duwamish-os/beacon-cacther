package com.duwamish.radio.transmitter.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.duwamish.radio.transmitter.R
import java.io.BufferedReader
import java.io.InputStreamReader
import android.webkit.WebViewClient
import android.webkit.WebSettings



public class CreativeTab() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.creative_tab, container, false)
        val myWebView: WebView = view.findViewById(com.duwamish.radio.transmitter.R.id.webView)
        myWebView.setWebViewClient(WebViewClient())

        val webSettings = myWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setAllowFileAccess(true)
        webSettings.setAppCacheEnabled(true)

        val content = BufferedReader(
                InputStreamReader(context?.getAssets()?.open("content.html"))).use { bufferedReader: BufferedReader ->
            bufferedReader.readText()
        }

        myWebView.loadData(content, "text/html", "UTF-8")

        return view
    }
}
