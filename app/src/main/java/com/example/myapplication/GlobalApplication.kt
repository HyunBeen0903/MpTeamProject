package com.example.myapplication

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "deca987c9241d158bb33e3562e7fca3a")


    }
}