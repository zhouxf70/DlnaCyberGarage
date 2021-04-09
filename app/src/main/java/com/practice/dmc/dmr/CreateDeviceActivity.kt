package com.practice.dmc.dmr

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.practice.dmc.R
import com.practice.dmc.common.KLog
import com.practice.dmc.common.Utils
import com.practice.dmc.dmc.DeviceController
import com.practice.dmc.mvvm.VMBaseActivity
import org.cybergarage.upnp.Action
import org.cybergarage.upnp.control.ActionListener

class CreateDeviceActivity :
    VMBaseActivity<CreateDeviceViewModel>(CreateDeviceViewModel::class.java),
    View.OnClickListener, ActionListener {

    private var etDeviceName: EditText? = null
    private var mSurfaceView: SurfaceView? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mCurrentPos = 0
    private var isPrepared = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_device)

        title = "Create Device"
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        findViewById<Button>(R.id.bt_create).setOnClickListener(this)
        findViewById<Button>(R.id.bt_play).setOnClickListener(this)

        etDeviceName = findViewById(R.id.et_device_name)
        etDeviceName?.setText("Media Render1")

        mSurfaceView = findViewById(R.id.surface)
        mMediaPlayer = MediaPlayer().apply {
            setOnCompletionListener {
                KLog.d("onCompleted")
            }
            setOnPreparedListener {
                KLog.d("onPrepared")
                isPrepared = true
                setDisplay(mSurfaceView?.holder)
            }
            setOnErrorListener { mp, what, extra ->
                KLog.d("onError,$what,$extra")
                true
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_create -> {
                viewModel?.createDevice(etDeviceName?.text.toString())
                viewModel?.setActionListener(this)
            }
            R.id.bt_play -> viewModel?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
        isPrepared = false
        mMediaPlayer?.pause()
        mMediaPlayer?.release()
    }

    /**
     * 响应参数需要在这里返回
     */
    override fun actionControlReceived(action: Action?): Boolean {
        KLog.t(action?.name)
        return when (action?.name) {
            DeviceController.SET_URL -> setUrl(action)
            DeviceController.PAUSE -> play(false)
            DeviceController.PLAY -> play(true)
            DeviceController.GET_POSITION_INFO -> getPositionInfo(action)
            else -> false
        }
    }

    /**
     * AndroidP9.0后限制了明文流量的网络请求，非加密的流量请求都会被系统禁止掉
     * 导致mediaplayer 播放uri发生Error (1,-2147483648)
     */
    private fun setUrl(action: Action?): Boolean {
        val url = action?.getArgumentValue(DeviceController.CURRENT_URL)
        KLog.d("setUrl:$url")

        mMediaPlayer?.run {
            reset()
            setDataSource(this@CreateDeviceActivity, Uri.parse(url))
            prepare()
            mCurrentPos = 0
        }

        return true
    }

    private fun play(play: Boolean): Boolean {
        KLog.d("play:$play")
        mMediaPlayer?.run {
            if (isPrepared && play && !isPlaying) {
                seekTo(mCurrentPos)
                start()
            } else if (isPrepared && !play && isPlaying) {
                pause()
                mCurrentPos = currentPosition
            }
            return true
        }
        return false
    }

    private fun getPositionInfo(action: Action?): Boolean {
        val duration = Utils.getTimeStr(mMediaPlayer?.duration)
        val currentPosition = Utils.getTimeStr(mMediaPlayer?.currentPosition)
        KLog.d("getPositionInfo,$duration,$currentPosition")
        action?.setArgumentValue(DeviceController.TRACK_DURATION, duration)
        action?.setArgumentValue(DeviceController.ABS_TIME, currentPosition)
        return true
    }


}