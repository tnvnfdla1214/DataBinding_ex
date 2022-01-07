package com.example.user_databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import androidx.lifecycle.Observer
import com.example.user_databinding.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.getViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        // 뷰모델 연결
        //mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //binding.viewModel = mainViewModel
        binding.viewModel = getViewModel() //koin(DI)으로 뷰모델 연결

        // 뷰모델을 LifeCycle 에 종속시킴, LifeCycle 동안 옵저버 역할을 함
        binding.lifecycleOwner = this
    }
}