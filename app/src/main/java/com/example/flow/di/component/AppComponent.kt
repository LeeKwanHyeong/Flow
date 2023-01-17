package com.example.flow.di.component

import android.app.Application
import com.example.flow.app.FlowApp
import com.example.flow.di.builder.ActivityBuilder
import com.example.flow.di.builder.FragmentBuilder
import com.example.flow.di.module.ApiModule
import com.example.flow.di.module.DBModule
import com.example.flow.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ApiModule::class,
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        ViewModelModule::class,
        FragmentBuilder::class,
        DBModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
    fun inject(app: FlowApp)
}