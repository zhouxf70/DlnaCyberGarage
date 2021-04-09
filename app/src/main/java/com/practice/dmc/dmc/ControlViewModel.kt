package com.practice.dmc.dmc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.dmc.common.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by zxf on 2021/4/1
 */
class ControlViewModel : ViewModel() {

    val controlLiveDate: MutableLiveData<String> = MutableLiveData()

    fun start(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var msg = "device disconnected"
            val setUrl = DeviceController.INSTANCE.setUrl(url)
            if (setUrl) {
                val play = DeviceController.INSTANCE.play()
                if (play) msg = "start success"
            }
            controlLiveDate.postValue(msg)
        }
    }

    fun pause() {
        viewModelScope.launch(Dispatchers.IO) {
            val pause = DeviceController.INSTANCE.pause()
            val msg = if (pause) "pause success" else "device disconnected"
            controlLiveDate.postValue(msg)
        }
    }

    fun play() {
        viewModelScope.launch(Dispatchers.IO) {

            val positionInfo = DeviceController.INSTANCE.getPositionInfo()
            KLog.d(positionInfo)

            val play = DeviceController.INSTANCE.play()
            val msg = if (play) "play success" else "device disconnected"
            controlLiveDate.postValue(msg)
        }
    }

}