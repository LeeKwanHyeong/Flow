package com.example.flow.network.Paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.example.flow.network.api.FlowApi
import com.example.flow.network.dto.response.ApiResponse
import com.example.flow.network.dto.response.ItemResponse
import com.example.flow.network.dto.response.MovieResponse
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
private const val clientID = "vDs4tCsBy4DXncs97PgU"
private const val clientSecret = "A6k9DuRVt4"
class MoviePagingSource (private val service: FlowApi, private val query: String): PagingSource<Int, MovieResponse>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        var pageIndex = params.key ?: STARTING_PAGE_INDEX
        Log.e("pageIndex == ", "$pageIndex")
        return try {
            val response = service.getMovie(
                id = clientID,
                pw = clientSecret,
                query = query,
                start = pageIndex,
                display = 10
            )

            val movies = if(response is ApiResponse.ApiSuccessResponse){
                Log.e("responseBody:: ", "${response.body}")
                response.body
            } else {
                null
            }

            LoadResult.Page(
                data = movies?.item as List<MovieResponse>,
                prevKey = if(pageIndex > 10) {
                    pageIndex - 10
                } else null,
                nextKey = if(pageIndex < movies.total) {
                    pageIndex += 10
                    pageIndex
                } else null
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (e: HttpException){
            return LoadResult.Error(e)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}