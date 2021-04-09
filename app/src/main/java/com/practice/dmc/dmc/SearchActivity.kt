package com.practice.dmc.dmc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.dmc.R
import com.practice.dmc.mvvm.VMBaseActivity
import org.cybergarage.util.KLog

class SearchActivity : VMBaseActivity<SearchViewModel>(SearchViewModel::class.java),
    View.OnClickListener {

    private var mAdapter: DeviceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<Toolbar>(R.id.action_bar)
        title = "Search"

        viewModel?.searchLiveDate?.observe(this, Observer { event ->
            KLog.d("${event.add},${event.device.friendlyName}")
            if (event.add) {
                mAdapter?.addDevice(event.device)
            } else {
                mAdapter?.removeDevice(event.device)
            }
            val msg = if (event.add) "device [${event.device.friendlyName}] is added"
            else "device [${event.device.friendlyName}] is removed"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.bt_play).setOnClickListener(this)
        findViewById<Button>(R.id.bt_pause).setOnClickListener(this)

        mAdapter = DeviceAdapter(this)
        val rv: RecyclerView = findViewById(R.id.rv_device)
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_play -> viewModel?.start()
            R.id.bt_pause -> viewModel?.pause()
        }
    }

}
