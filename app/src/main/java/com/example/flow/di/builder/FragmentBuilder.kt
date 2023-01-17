package com.example.flow.di.builder

import com.example.flow.di.module.ApiModule
import com.example.flow.view.FlowFragment
import com.example.flow.view.FlowRecentSearchedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuilder{
    @ContributesAndroidInjector(
        modules = [
            ApiModule::class
        ]
    )
    abstract fun flowFragment(): FlowFragment

    @ContributesAndroidInjector(
        modules = [
            ApiModule::class
        ]
    )
    abstract fun flowRecentSearchedFragment(): FlowRecentSearchedFragment

}