package com.example.flow.network


import com.example.flow.network.dto.response.ApiResponse
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ApiResponse<S, E>> {

    override fun enqueue(callback: Callback<ApiResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()


                /*callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(ApiResponse.create(response))
                )*/

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ApiResponse.ApiSuccessResponse(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ApiResponse.UnknownErrorResponse(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    when {
                        errorBody != null -> {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.ApiErrorResponse(code, errorBody))
                            )
                        }
                        (code == 401 || code == 409) -> {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.ApiErrorResponse(code, null))
                            )
                        }
                        else -> {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.UnknownErrorResponse(null))
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                when (throwable) {
                    is IOException -> callback.onResponse(this@NetworkResponseCall, Response.success(
                        ApiResponse.NetworkErrorResponse(throwable)
                    ))
                    else -> callback.onResponse(this@NetworkResponseCall, Response.success(
                        ApiResponse.UnknownErrorResponse(throwable)
                    ))
                }

                /*
                callback.onResponse(this@NetworkResponseCall, Response.success(
                    ApiResponse.create(
                        error = throwable
                    )
                ))*/
                /*callback.onResponse(this@NetworkResponseCall, Response.success(
                    ApiResponse.create(
                        error = throwable
                    )
                ))*/
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}