package com.example.flow.viewModel.baseViewModel

import androidx.lifecycle.ViewModel
import com.example.flow.network.dto.response.ApiResponse
import com.example.flow.network.dto.response.ErrorResponse
import com.example.flow.utility.baseUtil.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {


    val apiError = SingleLiveEvent<ErrorResponse?>()
    val networkError = SingleLiveEvent<Exception>()
    val unknownError = SingleLiveEvent<Throwable?>()

    val stateStart = SingleLiveEvent<Void>()
    val stateComplete = SingleLiveEvent<Void>()
    val stateSuccess = SingleLiveEvent<Void>()
    val stateFailure = SingleLiveEvent<Void>()

    protected fun <T : Any> ApiResponse<T, ErrorResponse>.digest(): T? {
        return when(this) {
            is ApiResponse.ApiErrorResponse -> {
                if(errorBody != null) {
                    apiError.postValue(
                        errorBody
                    )
                }
                complete()
                failure()
                null
            }
            is ApiResponse.NetworkErrorResponse -> {
                networkError.postValue(this.error)
                complete()
                failure()
                null
            }
            is ApiResponse.UnknownErrorResponse -> {
                unknownError.postValue(this.error)
                complete()
                failure()
                null
            }
            is ApiResponse.ApiSuccessResponse -> {
                complete()
                success()
                this.body
            }
        }
    }

    open fun init() {}
    open fun signUp() {}
    open fun signIn() {}
    open fun registerPhone() {}
    open fun registerOlive() {}

    open fun resetBaseToken(){}

    protected fun start() { stateStart.callAsync() }
    private fun complete() { stateComplete.callAsync() }
    protected fun success() { stateSuccess.callAsync() }
    private fun failure() { stateFailure.callAsync() }

}