package com.example.flow.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.flow.data.MovieDao
import com.example.flow.data.MovieModel
import com.example.flow.network.Paging.MoviePagingSource
import com.example.flow.network.api.FlowApi
import com.example.flow.viewModel.baseViewModel.BaseViewModel
import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
class FlowViewModel @Inject constructor(
    private val application: Application,
    private val flowApi: FlowApi,
    private val movieDao: MovieDao
): BaseViewModel() {

//    val movies = Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
//        MoviePagingSource(flowApi, "a")
//    }).flow.cachedIn(viewModelScope)

    private val _movieSearchedList = MutableLiveData<List<MovieModel>>()
    val movieSearchedList: LiveData<List<MovieModel>> = _movieSearchedList

    private val _pageCheck = MutableLiveData<String>()
    val pageCheck: LiveData<String> = _pageCheck
    fun updatePageCheck(string: String){
        _pageCheck.postValue(string)
    }

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText
    fun updateSearchText(string: String){
        _searchText.postValue(string)
    }


    fun moviesFlow(query: String) = Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
        MoviePagingSource(flowApi, query)
    }).flow.cachedIn(CoroutineScope(Dispatchers.IO))

    fun updateDatabase(data: String){
        viewModelScope.launch(Dispatchers.IO){
            movieDao.upsert(MovieModel(data))
        }
    }

    fun getDatabase(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movieSearchedList.postValue(movieDao.getMovies())
                Log.e("getDataBase", "${movieDao.getMovies()}")
            }catch (e: Exception){
                Log.e("EmptyList", "${e.message}")
            }
        }
    }



}