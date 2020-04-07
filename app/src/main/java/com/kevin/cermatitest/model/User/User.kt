package com.kevin.cermatitest.model.User

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val name : String = "",
    @SerializedName("id")
    val id : Long = 0,
    @SerializedName("avatar_url")
    val iconUrl : String = ""
) {}
