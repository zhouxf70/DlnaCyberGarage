package com.practice.dmc.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.dmc.common.KLog

/**
 * Created by zxf on 2021/3/13
 */
class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var t: T? = null
        try {
            t = modelClass.newInstance()
        } catch (e: Exception) {
            KLog.d(e)
        }
        return t!!
    }
}