package com.practice.dmc.common

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager

/**
 * Created by zxf on 2021/4/1
 */

class Utils {

    companion object {

        var app: Application? = null

        fun init(application: Application) {
            app = application
        }

        private var multicastLock: WifiManager.MulticastLock? = null

        fun acquireMultiLock() {
            if (multicastLock == null) {
                val wm: WifiManager? = app?.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                multicastLock = wm?.createMulticastLock("multicastLock")
            }
            multicastLock?.run {
                setReferenceCounted(true)
                acquire()
            }
        }

        fun releaseMultiLock() {
            multicastLock?.release()
        }

        fun getTimeStr(ms: Int?): String {
            if (ms == null) return "00:00:00"
            val seconds = ms / 1000
            val ss = seconds % 60
            val minutes = (seconds - ss) / 60
            val mm = minutes % 60
            val hours = (minutes - mm) / 60
            return "${getStr(hours)}:${getStr(mm)}:${getStr(ss)}"
        }

        private fun getStr(l: Int): String {
            return if (l < 10) "0$l" else "$l"
        }
    }
}