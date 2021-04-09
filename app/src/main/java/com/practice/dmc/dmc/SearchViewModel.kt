package com.practice.dmc.dmc

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.dmc.bean.DeviceChangeEvent
import com.practice.dmc.common.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.cybergarage.upnp.ControlPoint
import org.cybergarage.upnp.Device
import org.cybergarage.upnp.UPnP
import org.cybergarage.upnp.device.DeviceChangeListener

/**
 * Created by zxf on 2021/4/1
 */
class SearchViewModel : ViewModel() {

    val searchLiveDate: MutableLiveData<DeviceChangeEvent> = MutableLiveData()
    private var searchHandler: SearchHandler? = null

    private val controlPoint by lazy(LazyThreadSafetyMode.NONE) {
        ControlPoint().apply {
            addDeviceChangeListener(object : DeviceChangeListener {
                override fun deviceRemoved(dev: Device?) {
                    KLog.d("deviceRemoved,${dev?.friendlyName}")
                    dev?.run {
                        if (friendlyName == DeviceController.INSTANCE.device?.friendlyName)
                            DeviceController.INSTANCE.device = null
                        searchLiveDate.postValue(DeviceChangeEvent(false, this))
                    }
                }

                override fun deviceAdded(dev: Device?) {
                    KLog.t("deviceAdded,${dev?.friendlyName}")
                    if (dev != null)
                        searchLiveDate.postValue(DeviceChangeEvent(true, dev))
                }
            })
        }
    }

    fun start() {
        if (searchHandler == null) {
            val handlerThread = HandlerThread("SearchThread")
            handlerThread.start()
            searchHandler = SearchHandler(handlerThread.looper, controlPoint)
        }
        pause()
        searchHandler?.sendEmptyMessage(0)
    }

    fun pause() {
        searchHandler?.removeMessages(0)
    }

    override fun onCleared() {
        KLog.d("onCleared")
        // onCleared在主线程中被调用，但是controlPoint的关闭需要发送网络请求
        viewModelScope.launch(Dispatchers.IO) {
            controlPoint.stop()
        }
        pause()
        searchHandler?.looper?.quit()
    }

    private class SearchHandler(looper: Looper, val controlPoint: ControlPoint) : Handler(looper) {

        private val normalInternalTime = 600000L
        private var start = false

        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                if (!start) {
                    UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR)
                    start = controlPoint.start()
                }
                KLog.t("search:$start")
                controlPoint.search()
                sendEmptyMessageDelayed(0, normalInternalTime)
            }
        }
    }

}