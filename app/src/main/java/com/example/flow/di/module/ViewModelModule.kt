package com.example.flow.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flow.di.ViewModelFactory
import com.example.flow.di.ViewModelKey
import com.example.flow.viewModel.FlowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlowViewModel::class)
    abstract fun bindFlowViewModel(viewModel: FlowViewModel): ViewModel
}