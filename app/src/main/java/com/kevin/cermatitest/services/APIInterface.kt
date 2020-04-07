package com.kevin.cermatitest.services

import com.kevin.cermatitest.model.GithubSearch
import com.kevin.cermatitest.utils.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET(AppConstants.API_SEARCH_USER)
    suspend fun getUserList(
        @Query("q") username: String,
        @Query("page") page : Int
    ): Response<GithubSearch>

}