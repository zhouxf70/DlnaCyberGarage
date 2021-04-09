package com.practice.dmc.common

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by zxf on 2021/3/12
 */

class KLog {

    companion object {

        var isDebug = true
        private const val TAG = "KLog-CyberGarage"

        fun d(msg: Any?) {
            print(Level.D, TAG, msg.toString())
        }

        fun d(tag: String, msg: Any?) {
            print(Level.D, "$TAG-$tag", msg.toString())
        }

        fun i(msg: Any?) {
            print(Level.I, TAG, msg.toString())
        }

        fun i(tag: String, msg: Any?) {
            print(Level.I, "$TAG-$tag", msg.toString())
        }

        fun w(msg: Any?) {
            print(Level.W, TAG, msg.toString())
        }

        fun w(tag: String, msg: Any?) {
            print(Level.W, "$TAG-$tag", msg.toString())
        }

        fun e(msg: Any?) {
            print(Level.E, TAG, msg.toString())
        }

        fun e(tag: String, msg: Any?) {
            print(Level.E, "$TAG-$tag", msg.toString())
        }

        fun t(msg: Any?) {
            print(Level.THREAD, TAG, msg.toString())
        }

        fun t(tag: String, msg: Any?) {
            print(Level.THREAD, "$TAG-$tag", msg.toString())
        }

        fun json(msg: Any?) {
            print(Level.JSON, TAG, msg.toString())
        }

        fun json(tag: String, msg: Any?) {
            print(Level.JSON, "$TAG-$tag", msg.toString())
        }

        private fun print(level: Level, tag: String, msg: String) {
            if (!isDebug) return

            val stackTrace = Thread.currentThread().stackTrace
            val stack = stackTrace[4].run {
                "[($fileName:$lineNumber)#$methodName] "
            }

            when (level) {
                Level.D -> Log.d(tag, stack + msg)
                Level.I -> Log.i(tag, stack + msg)
                Level.W -> Log.w(tag, stack + msg)
                Level.E -> Log.e(tag, stack + msg)
                Level.THREAD -> Log.d(tag, "$stack[${Thread.currentThread().name}]$msg")
                Level.JSON -> logJson(tag, stack, msg)
            }
        }

        private val LINE_SEPARATOR: String = System.getProperty("line.separator") ?: "\n"

        private fun logJson(tag: String, stack: String, msg: String) {

            var json = ""
            try {
                json = when {
                    msg.startsWith("{") -> JSONObject(msg).toString(4)
                    msg.startsWith("[") -> JSONArray(msg).toString(4)
                    else -> "Empty or Not json content"
                }
            } catch (e: Exception) {
                e("KLog", e)
            }

            val prefix =
                "$stack${LINE_SEPARATOR}╔════════════════════════════════════════════════════════════════════════${LINE_SEPARATOR}║ "
            val postfix =
                "${LINE_SEPARATOR}╚════════════════════════════════════════════════════════════════════════"
            json = json.split(LINE_SEPARATOR).joinToString("${LINE_SEPARATOR}║ ", prefix, postfix)

            json.apply {
                if (length > 3200) {
                    val chunkCount = length / 3200
                    for (i in 0..chunkCount) {
                        val max = 3200 * (i + 1)
                        if (max >= length) Log.d(tag, substring(3200 * i))
                        else Log.d(tag, substring(3200 * i, max))
                    }
                } else {
                    Log.d(tag, json)
                }
            }
        }
    }

    enum class Level {
        D, I, W, E, JSON, THREAD
    }

}
