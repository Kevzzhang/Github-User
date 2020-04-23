package com.kevin.cermatitest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.cermatitest.R
import com.kevin.cermatitest.model.GithubSearch
import com.kevin.cermatitest.model.User.User
import com.kevin.cermatitest.model.errorResponse.ErrorResponse
import com.kevin.cermatitest.repo.SearchRepo
import com.kevin.cermatitest.utils.LiveDataResult
import com.kevin.cermatitest.utils.Status
import kotlinx.coroutines.launch

class SearchViewmodel : ViewModel() {

    lateinit var mRepo: SearchRepo

    var successResp: MutableLiveData<List<User>>
    var errorResp: MutableLiveData<Any?>
    var showProgressBar: MutableLiveData<Boolean?>

    private val userList: MutableList<User> = arrayListOf()

    private var pages = 1
    private var isHasMore = true
    private var isRefresh = false

    init {
        successResp = MutableLiveData()
        errorResp = MutableLiveData()
        showProgressBar = MutableLiveData()
    }

    fun setupRepo(repo: SearchRepo) {
        this.mRepo = repo
    }

    fun setRefresh(refresh: Boolean) {
        isRefresh = refresh
    }

    fun getRefresh(): Boolean {
        return isRefresh
    }

    fun resetPages() {
        pages = 1
    }

    private fun handleProgressBar() {
        showProgressBar.value = pages == 1
    }

    fun fetchRepoList(searchText: String) {
        handleProgressBar()

        if (isHasMore) {
            viewModelScope.launch {
                val result = mRepo.getUserList(searchText, pages)

                if (result.status == Status.SUCCESS) {
                    showProgressBar.value = false
                    handleSuccessFetchUserList(result)
                } else {
                    showProgressBar.value = false
                    handleErrorResponse(result)
                }
            }
        } else {
            errorResp.value = R.string.no_data_available
        }
    }

    fun shouldShowErrorLayout(): Boolean {
        return pages == 1
    }

    fun handleSuccessFetchUserList(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as GithubSearch
                isHasMore = data.items.isNotEmpty()
                pages++

                updateUserListLocal(data.items)
                successResp.value = data.items
            } catch (e: Exception) {
                errorResp.value = e.message
            }
        } else {
            errorResp.value = R.string.error_api_message
        }
    }

    fun handleErrorResponse(resp: LiveDataResult<Any?>) {
        try {
            val error = resp.data as ErrorResponse

            if (!error.message.isNullOrEmpty()) {
                errorResp.value = error.message
            } else {
                errorResp.value = R.string.error_api_message
            }
        } catch (e: Exception) {
            errorResp.value = e.message
        }
    }

    fun updateUserListLocal(item: List<User>?) {
        if (!item.isNullOrEmpty()) {
            if (isRefresh)
                resetUpdateUserList(item)
            else {
                updateUserList(item)
            }
        }

    }

    fun reinitData() {
        if (!userList.isNullOrEmpty()) {
            successResp.value = userList
        }
    }

    fun updateUserList(item: List<User>?) {
        if (!item.isNullOrEmpty()) {
            userList.addAll(item)
        }
    }

    fun resetUpdateUserList(item: List<User>?) {
        if (item != null && item.isNotEmpty()) {
            userList.clear()
            userList.addAll(userList)
        }

    }

    override fun onCleared() {
        super.onCleared()
    }
}