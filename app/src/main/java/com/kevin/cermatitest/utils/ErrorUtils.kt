package com.kevin.cermatitest.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kevin.cermatitest.model.errorResponse.ErrorResponse
import retrofit2.HttpException


class ErrorUtils {
    companion object {
        fun parseError(exception: HttpException): ErrorResponse? {
            val message = ""
            try {
                val errorBody = exception.response()?.errorBody()
                if (errorBody != null) {
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? =
                        Gson().fromJson(errorBody.charStream(), type)
                    return errorResponse
                }

            } catch (e: Exception) {
                return ErrorResponse("Unable to parse JSON.")
            }

            return ErrorResponse(message)
        }
    }

}