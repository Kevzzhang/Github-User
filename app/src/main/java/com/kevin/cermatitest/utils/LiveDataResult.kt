package com.kevin.cermatitest.utils

data class LiveDataResult<T>(
    val status: Status? = null,
    val data: Any? = null,
    val message: String = ""
) {}