package com.practice.dmc.dmr

import com.practice.dmc.common.KLog
import org.cybergarage.net.HostInterface
import org.cybergarage.upnp.Device
import org.cybergarage.upnp.UPnP
import java.io.InputStream

/**
 * Created by zxf on 2021/4/2
 */
class MediaRenderer(name: String, des: InputStream, avTrans: InputStream) : Device(des) {

    private val port = 39520
    private val serviceType = "urn:schemas-upnp-org:service:AVTransport:1"

    init {
        KLog.d("init MediaRenderer")

        deviceNode.getNode("friendlyName").value = name
        // 默认路径是 /device.xml
        descriptionURI = "/dev/$uuid/desc.xml"

        val avTransService = getService(serviceType)
        avTransService?.run {
            loadSCPD(avTrans)
            scpdurl = "/dev/$uuid/AVTransport/desc.xml"
            controlURL = "/dev/$uuid/AVTransport/action"
        }

        // Netwroking initialization
        UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR)
        val firstIf = HostInterface.getHostAddress(0)
        HostInterface.setInterface(firstIf)
        httpPort = port
    }


}