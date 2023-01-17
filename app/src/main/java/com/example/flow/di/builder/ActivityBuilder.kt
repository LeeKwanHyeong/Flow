package com.example.flow.di.builder

import com.example.flow.di.module.ApiModule
import com.example.flow.view.FlowActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(
        modules = [
            ApiModule::class
        ]
    )
    abstract fun flowActivity(): FlowActivity
}