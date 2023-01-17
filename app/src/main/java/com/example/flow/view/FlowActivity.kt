package com.example.flow.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil.setContentView
import com.example.flow.R
import com.example.flow.databinding.ActivityFlowBinding
import com.example.flow.di.Injectable
import com.example.flow.di.ViewModelFactory
import com.example.flow.network.api.FlowApi
import com.example.flow.view.baseView.BaseActivity
import com.example.flow.viewModel.FlowViewModel
import javax.inject.Inject

class FlowActivity : BaseActivity<ActivityFlowBinding>(), Injectable {

    @Inject
    lateinit var flowApi: FlowApi

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initBinding(inflater: LayoutInflater): ActivityFlowBinding = ActivityFlowBinding.inflate(inflater)
}