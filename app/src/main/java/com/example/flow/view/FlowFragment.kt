package com.example.flow.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flow.R
import com.example.flow.databinding.FragmentFlowBinding
import com.example.flow.di.Injectable
import com.example.flow.injectViewModel
import com.example.flow.network.dto.response.MovieResponse
import com.example.flow.view.baseView.BaseFragment
import com.example.flow.viewModel.FlowViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FlowFragment : BaseFragment<FragmentFlowBinding, FlowViewModel>(), Injectable, View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var moviesAdapter: FlowMoviesAdapter


    override val viewModel: FlowViewModel by lazy { injectViewModel(viewModelFactory) }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFlowBinding =
        FragmentFlowBinding.inflate(inflater, container, false)


    override fun observe() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener(this)
        binding.beforeSearchButton.setOnClickListener(this)
        binding.searchEditText.setOnClickListener(this)

        setupView()

        viewModel.searchText.observe(viewLifecycleOwner){ it ->
            binding.searchEditText.setText(it)
            lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.moviesFlow(it)
                    .collectLatest { pagedData ->
                        moviesAdapter.submitData(pagedData)
                    }
            } }
        }


        moviesAdapter.setItemClickListener(object: FlowMoviesAdapter.FlowClickCallback{
            override fun itemClickListener(item: MovieResponse) {
                // 웹뷰 띄우기
            }
        })
    }

    private fun setupView(){
        moviesAdapter = FlowMoviesAdapter()
        binding.searchRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter
            setHasFixedSize(true)
        }
    }

//    private fun setupList(){
//        lifecycleScope.launch {
//            viewModel.movies.collectLatest { pagedData ->
//                moviesAdapter.submitData(pagedData)
//            }
//        }
//    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.search_button -> {
                    if(binding.searchEditText.text.isNullOrEmpty()){
                        Toast.makeText(requireContext(), "검색어를 작성하시오.", Toast.LENGTH_SHORT).show()
                    }else{
                        lifecycleScope.launch {
                            viewModel.updateDatabase(binding.searchEditText.text.toString())
                            viewModel.updateSearchText(binding.searchEditText.text.toString())
                        }
                    }
                }
                R.id.before_search_button -> {
                    viewModel.getDatabase()
                        .also {
                            NavHostFragment.findNavController(this@FlowFragment)
                                .navigate(R.id.action_flow_fragment_to_flow_recent_searched_fragment)                        }
                }
            }
        }
    }

}