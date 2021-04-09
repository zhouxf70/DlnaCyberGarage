package com.practice.dmc.dmc

import com.practice.dmc.bean.MediaPositionInfo
import org.cybergarage.upnp.Device
import org.cybergarage.util.KLog

/**
 * Created by zxf on 2021/4/7
 */
class DeviceController {

    var device: Device? = null

    companion object {
        const val AV_TRANSPORT = "urn:schemas-upnp-org:service:AVTransport:1"
        const val RENDERING_CONTROL = "urn:schemas-upnp-org:service:RenderingControl:1"

        const val SET_URL = "SetAVTransportURI"
        const val CURRENT_URL = "CurrentURI"

        const val PLAY = "Play"
        const val PAUSE = "Pause"

        const val GET_POSITION_INFO = "GetPositionInfo"
        const val TRACK_DURATION = "TrackDuration"
        const val ABS_TIME = "AbsTime"

        val INSTANCE: DeviceController by lazy {
            DeviceController()
        }
    }

    fun setUrl(uri: String): Boolean {

        val service = device?.getService(AV_TRANSPORT) ?: return false
        KLog.d(service.serviceNode.toString())

        val setUrl = service.getAction(SET_URL) ?: return false
        setUrl.run {
            KLog.d(actionNode.toString())
            setArgumentValue("InstanceID", 0)
            setArgumentValue(CURRENT_URL, uri)
            setArgumentValue("CurrentURIMetaData", "")
            if (!postControlAction()) {
                KLog.d("PostControl:set url failed")
                return false
            }
        }

        return true
    }

    fun pause(): Boolean {
        val service = device?.getService(AV_TRANSPORT) ?: return false
        KLog.d(service.serviceNode.toString())

        val pause = service.getAction(PAUSE) ?: return false
        pause.run {
            KLog.d(actionNode.toString())
            setArgumentValue("InstanceID", 0)
            if (!postControlAction()) {
                KLog.d("Control:pause failed")
                return false
            }
        }
        return true

    }

    fun play(): Boolean {
        val service = device?.getService(AV_TRANSPORT) ?: return false
        KLog.d(service.serviceNode.toString())

        val play = service.getAction(PLAY) ?: return false
        play.run {
            KLog.d(actionNode.toString())
            setArgumentValue("InstanceID", 0)
            if (!postControlAction()) {
                KLog.d("Control:pause failed")
                return false
            }
        }
        return true
    }

    fun getPositionInfo(): MediaPositionInfo? {
        val service = device?.getService(AV_TRANSPORT) ?: return null
        KLog.d(service.serviceNode.toString())

        val gpi = service.getAction(GET_POSITION_INFO) ?: return null

        gpi.run {
            KLog.d(actionNode.toString())
            setArgumentValue("InstanceID", 0)

            return if (!postControlAction()) {
                KLog.d("Control:getPositionInfo failed")
                null
            } else {
                val trackDuration = getArgumentValue(TRACK_DURATION)
                val absTime = getArgumentValue(ABS_TIME)
                MediaPositionInfo(trackDuration, absTime)
            }
        }
    }


}