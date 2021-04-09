package com.practice.dmc.bean

import org.cybergarage.upnp.Device

/**
 * Created by zxf on 2021/4/8
 */
data class DeviceChangeEvent(
    val add: Boolean,
    val device: Device
)