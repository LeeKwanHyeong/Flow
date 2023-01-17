package com.example.flow.network.dto.response

import android.util.Log
import java.io.IOException

@Suppress("unused")
sealed class ApiResponse<out T: Any, out U: Any>{
    data class ApiSuccessResponse<T: Any>(val body: T): ApiResponse<T, Nothing>()
    data class ApiErrorResponse<U: Any>(val errorCode: Int, val errorBody: U?): ApiResponse<Nothing, U>()
    data class NetworkErrorResponse(val error: IOException): ApiResponse<Nothing, Nothing>()
    data class UnknownErrorResponse(val error: Throwable?): ApiResponse<Nothing, Nothing>()

    companion object{
        fun<T: Any, U: Any> log(response: ApiResponse<T, U>, tag: String, name: String){
            when(response){
                is ApiSuccessResponse -> { Log.i("$tag", "$name, Success:: Body:: ${response.body}")}
                is ApiErrorResponse -> { Log.e("$tag", "$name, Api Error Code:: ${response.errorCode}\n" +
                        "Api Error Body:: ${response.errorBody}")}
                is NetworkErrorResponse -> { Log.e( "$tag", "$name, Network Error:: ${response.error.message}")}
                is UnknownErrorResponse -> { Log.e("$tag", "$name, Unknown Error:: ${response.error?.message}")}
            }
        }
    }
}