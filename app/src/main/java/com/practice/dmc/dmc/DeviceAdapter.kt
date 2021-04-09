package com.practice.dmc.dmc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.dmc.R
import org.cybergarage.upnp.Device
import org.cybergarage.util.KLog

/**
 * Created by zxf on 2021/4/6
 */
class DeviceAdapter(private val mContext: Context) :
    RecyclerView.Adapter<DeviceAdapter.VH>() {

    private val mDeviceList = ArrayList<Device>()

    fun setData(deviceList: List<Device>, clear: Boolean = true) {
        if (clear)
            mDeviceList.clear()
        mDeviceList.addAll(deviceList)
        notifyDataSetChanged()
    }

    fun addDevice(device: Device) {
        mDeviceList.add(device)
        notifyItemInserted(mDeviceList.size - 1)
    }

    fun removeDevice(device: Device) {
        val listIterator = mDeviceList.iterator()
        while (listIterator.hasNext()) {
            val next = listIterator.next()
            if (next.friendlyName == device.friendlyName) {
                listIterator.remove()
                KLog.d("remove:${next.friendlyName}")
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val root = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false)
        return VH(root)
    }

    override fun getItemCount(): Int {
        return mDeviceList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val device = mDeviceList[position]
        holder.tvName.text = device.friendlyName
        holder.btOpen.setOnClickListener {
            DeviceController.INSTANCE.device = device
            val intent = Intent(mContext, ControlActivity::class.java)
            intent.putExtra(ControlActivity.DEVICE_NAME, device.friendlyName)
            mContext.startActivity(intent)
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
            get() = itemView.findViewById(R.id.tv_name)
        val btOpen: TextView
            get() = itemView.findViewById(R.id.bt_open)
    }

}