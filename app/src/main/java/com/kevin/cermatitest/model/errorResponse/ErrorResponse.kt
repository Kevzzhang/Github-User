package com.kevin.cermatitest.model.errorResponse

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    val message : String ="",
    @SerializedName("errors")
    val errors : List<Error> = arrayListOf(),
    @SerializedName("documentation_url")
    val doc_url : String =""
) {}
