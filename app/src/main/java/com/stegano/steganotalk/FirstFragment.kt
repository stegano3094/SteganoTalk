package com.stegano.steganotalk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_first.*
import okhttp3.*
import java.io.IOException


class FirstFragment : Fragment() {
    companion object {
        val TAG: String = "FirstFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView.text = "dsfndsjk"

        // 테스트
        getData()
    }

    private fun getData() {
        // OkHttp 사용 방법
        // 1. 클라이언트 만들기
        val client = OkHttpClient()
        // 2. 요청 코드
        val getUrl: String = "https://raw.githubusercontent.com/stegano3094/imageStorage/main/career.json"
        val request = Request.Builder().url(getUrl).build()
        // 3. 응답 코드, enqueue() - 비동기, execute() - 동기
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "onFailure e : ${e.printStackTrace()}")
            }

            override fun onResponse(call: Call, response: Response) {
                val receiveData = response.body?.string()  // json을 String 형식으로 받아옴
                Log.e(TAG, "onResponse : $receiveData")

                activity?.runOnUiThread {  // Fragment 에서 UI변경 시 activity에서 변경해야 함
                    textView.text = receiveData
                }
            }
        })
    }
}