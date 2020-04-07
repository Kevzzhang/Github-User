package com.kevin.cermatitest.repo

import com.kevin.cermatitest.services.APIClient
import com.kevin.cermatitest.services.APIInterface
import com.kevin.cermatitest.utils.BaseAPIService
import com.kevin.cermatitest.utils.LiveDataResult
import retrofit2.HttpException


interface searchInterface {
    suspend fun getUserList(key: String, page : Int): LiveDataResult<Any?>
}

class SearchRepo() : searchInterface, BaseAPIService() {

    companion object {
        private val TAG = SearchRepo::class.java.simpleName
    }

    private val apiClient = APIClient.client.create(APIInterface::class.java)

    override suspend fun getUserList(key: String, page : Int): LiveDataResult<Any?> {
        try {
            val result = apiClient.getUserList(key, page)

            if (result.isSuccessful) {
                val response = result.body()
                val data: LiveDataResult<Any?> = storeResult(response, "", TAG)
                return data
            } else {
                val exception = HttpException(result)
                val data: LiveDataResult<Any?> = storeError(exception, "", TAG)
                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val data: LiveDataResult<Any?> = storeError(e, "", TAG)
            return data
        }
    }
}