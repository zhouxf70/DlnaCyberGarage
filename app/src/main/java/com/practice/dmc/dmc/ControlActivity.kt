package com.practice.dmc.dmc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.practice.dmc.R
import com.practice.dmc.mvvm.VMBaseActivity

class ControlActivity : VMBaseActivity<ControlViewModel>(ControlViewModel::class.java),
    View.OnClickListener {

    companion object {
        const val DEVICE_NAME = "device_name"
    }

    private val url = "http://video19.ifeng.com/video07/2013/11/11/281708-102-007-1138.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val name = intent.getStringExtra(DEVICE_NAME)
        title = "Control: $name"
        viewModel?.controlLiveDate?.observe(this, Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.bt_start).setOnClickListener(this)
        findViewById<Button>(R.id.bt_play).setOnClickListener(this)
        findViewById<Button>(R.id.bt_pause).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_start -> viewModel?.start(url)
            R.id.bt_pause -> viewModel?.pause()
            R.id.bt_play -> viewModel?.play()
        }
    }
}