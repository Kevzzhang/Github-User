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

    private var pages = 1
    private var isHasMore = true

    init {
        successResp = MutableLiveData()
        errorResp = MutableLiveData()
        showProgressBar = MutableLiveData()
    }

    fun setupRepo(repo: SearchRepo) {
        this.mRepo = repo
    }

    fun setPages(page: Int) {
        this.pages = page
    }

    fun getPages(): Int {
        return pages
    }

    fun setHasMore(hasMore: Boolean) {
        isHasMore = hasMore
    }

    fun getHasMore(): Boolean {
        return isHasMore
    }

    fun resetPages() {
        pages = 1
    }

    fun handleProgressBar() {
        showProgressBar.value = getPages() == 1
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
        return getPages() == 1
    }

    fun handleSuccessFetchUserList(resp: LiveDataResult<Any?>) {
        if (resp.data != null) {
            try {
                val data = resp.data as GithubSearch

                if (data.items.size > 0) {
                    isHasMore = true
                } else {
                    isHasMore = false
                }
                setPages(getPages() + 1)
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

    override fun onCleared() {
        super.onCleared()
    }
}