package com.example.flow.view.baseView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.flow.viewModel.baseViewModel.BaseViewModel

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel?> : Fragment() {

    protected abstract val viewModel: V

    private var _binding: B? = null
    val binding: B get() = _binding!!




    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()
        viewModel!!.init()

    }

    open fun init() {}
    abstract fun observe()

    open fun onProgressStart() {}
    open fun onComplete() {}
    open fun onSuccess() {}
    open fun onFailure() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}