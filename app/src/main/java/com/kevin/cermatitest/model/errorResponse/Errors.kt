package com.kevin.cermatitest.model.errorResponse

import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("resource")
    val resource: String = "",
    @SerializedName("field")
    val field: String = "",
    @SerializedName("code")
    val code: String = ""
) {}