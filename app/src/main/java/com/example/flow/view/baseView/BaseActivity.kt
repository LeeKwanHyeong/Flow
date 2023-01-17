package com.example.flow.view.baseView

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import androidx.window.layout.WindowMetricsCalculator

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

    var heightWindowSizeClass: WindowSizeClass = WindowSizeClass.MEDIUM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = initBinding(inflater = layoutInflater)
        setContentView(binding.root)

        init()

        // Replace with a known container that you can safely add a
        // view to where it won't affect the layout and the view
        // won't be replaced.
        val container: ViewGroup = binding.root.rootView as ViewGroup

        // Add a utility view to the container to hook into
        // View.onConfigurationChanged. This is required for all
        // activities, even those that don't handle configuration
        // changes. We also can't use Activity.onConfigurationChanged,
        // since there are situations where that won't be called when
        // the configuration changes. View.onConfigurationChanged is
        // called in those scenarios.
        container.addView(object : View(this) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                computeWindowSizeClasses()
            }
        })

        computeWindowSizeClasses()
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density

        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density

        heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }


        val density = resources.displayMetrics.density
        val densityDpi = resources.displayMetrics.densityDpi
        val scaledDensity = resources.displayMetrics.scaledDensity

        Log.e("BaseActivity", " density $density")
        Log.e("BaseActivity", " densityDpi $densityDpi")
        Log.e("BaseActivity", " scaledDensity $scaledDensity")
        Log.e("BaseActivity", " width $widthDp height $heightDp")
        Log.e("BaseActivity", " w $widthWindowSizeClass b $heightWindowSizeClass")

        // Use widthWindowSizeClass and heightWindowSizeClass.
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    abstract fun initBinding(inflater: LayoutInflater): T

    open fun init() {}

}