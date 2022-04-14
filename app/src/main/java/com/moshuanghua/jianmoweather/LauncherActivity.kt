package com.moshuanghua.jianmoweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission()//检查定位权限
    }

    private fun checkLocationPermission() {

    }
}