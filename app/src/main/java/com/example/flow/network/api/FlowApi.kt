package com.example.flow.network.api

import com.example.flow.network.dto.response.ApiResponse
import com.example.flow.network.dto.response.ErrorResponse
import com.example.flow.network.dto.response.ItemResponse
import com.example.flow.network.dto.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FlowApi {
    @GET("movie.json")
    suspend fun getMovie(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") pw: String,
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): ApiResponse<ItemResponse, ErrorResponse>
}