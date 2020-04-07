package com.kevin.cermatitest.model

import com.google.gson.annotations.SerializedName
import com.kevin.cermatitest.model.User.User

data class GithubSearch(
    @SerializedName("total_count")
    val totalCount : Int = 0,
    @SerializedName("incomplete_result")
    val incomplete : Boolean = false,
    @SerializedName("items")
    val items : List<User> = arrayListOf()
){}