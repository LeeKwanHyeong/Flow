package com.example.flow.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.NavHostFragment
import com.example.flow.R
import com.example.flow.databinding.FragmentFlowRecentSearchedBinding
import com.example.flow.di.Injectable
import com.example.flow.injectViewModel
import com.example.flow.view.baseView.BaseFragment
import com.example.flow.viewModel.FlowViewModel
import javax.inject.Inject

class FlowRecentSearchedFragment : BaseFragment<FragmentFlowRecentSearchedBinding, FlowViewModel>(),
    Injectable, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var adapter: FlowRecentSearchedAdapter

    override val viewModel: FlowViewModel by lazy { injectViewModel(viewModelFactory) }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFlowRecentSearchedBinding =
        FragmentFlowRecentSearchedBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movieSearchedList.observe(viewLifecycleOwner){ listData ->
            if(!listData.isNullOrEmpty()){
                adapter = FlowRecentSearchedAdapter(listData, requireContext())
                binding.searchedList.adapter = adapter

                adapter.setItemClickListener(object: FlowRecentSearchedAdapter.FlowRecentClickCallback{
                    override fun itemClickListener(name: String) {

                        viewModel.moviesFlow(name)
                            .also {
                                viewModel.updateSearchText(name)
                                NavHostFragment.findNavController(this@FlowRecentSearchedFragment)
                                    .navigate(R.id.action_flow_recent_searched_fragment_to_flow_fragment)
                            }
                    }
                })
            }
        }




    }

    override fun observe() {

    }

    override fun onClick(v: View?) {
    }

}