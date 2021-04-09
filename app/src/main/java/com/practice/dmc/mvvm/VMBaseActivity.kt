package com.practice.dmc.mvvm

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by zxf on 2021/4/1
 */
open class VMBaseActivity<VM : ViewModel>(private val clazz: Class<VM>) : AppCompatActivity() {

    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory()).get(clazz)
    }
}