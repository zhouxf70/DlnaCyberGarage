package com.practice.dmc

import android.app.Application
import com.practice.dmc.common.Utils

/**
 * Created by zxf on 2021/4/1
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

}