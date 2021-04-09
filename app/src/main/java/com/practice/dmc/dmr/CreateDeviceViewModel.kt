package com.practice.dmc.dmr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.dmc.common.KLog
import com.practice.dmc.common.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cybergarage.upnp.Device
import org.cybergarage.upnp.control.ActionListener

/**
 * Created by zxf on 2021/4/1
 */
class CreateDeviceViewModel : ViewModel() {

    private var device: Device? = null

    fun createDevice(deviceName: String) {
        if (device != null) return

        try {
            Utils.app?.run {
                val des = assets.open("device.xml")
                val service = assets.open("AVTransport.xml")
                device = MediaRenderer(deviceName, des, service)
            }
        } catch (e: Exception) {
            KLog.d(e)
            e.printStackTrace()
        }

        KLog.d("device is null:${device == null}")
        device?.run {
            KLog.d("name:$friendlyName,type:$deviceType")
        }
    }

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            KLog.t("start")
            device?.start()
        }
    }

    fun setActionListener(listener: ActionListener) {
        if (device == null)
            KLog.e("please create device first")
        else
            device?.setActionListener(listener)
    }

    override fun onCleared() {
        KLog.d("onCleared")
        GlobalScope.launch(Dispatchers.IO) {
            KLog.t("stop")
            device?.stop()
        }
    }

}