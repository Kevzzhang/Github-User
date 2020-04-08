package com.kevin.cermatitest.ui.activity

import EndlessScrollRV
import OnLoadMoreListener
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kevin.cermatitest.R
import com.kevin.cermatitest.adapter.SearchRVAdapter
import com.kevin.cermatitest.repo.SearchRepo
import com.kevin.cermatitest.viewmodel.SearchViewmodel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.reuseable_error_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: SearchViewmodel
    private var searchAdapter: SearchRVAdapter? = null
    private lateinit var rvScrollListener: EndlessScrollRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

        initUI()
        initCallback()
        configureViewModel()
    }

    private fun setupViewModel() {
        viewmodel = ViewModelProvider(this).get(SearchViewmodel::class.java)
        viewmodel.setupRepo(SearchRepo())
    }

    private fun initUI() {
        viewmodel.reinitData()
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        val rvManager = LinearLayoutManager(this)
        searchAdapter = SearchRVAdapter(this)

        rv_search.apply {
            setHasFixedSize(true)
            layoutManager = rvManager
            adapter = searchAdapter
        }

        rvScrollListener = EndlessScrollRV(rvManager)
    }

    private fun configureViewModel() {
        viewmodel.successResp.observe(this, Observer { items ->
            if (items != null) {
                viewflipper.displayedChild = 0
                rvScrollListener.setLoaded()

                if (viewmodel.getRefresh()) {
                    searchAdapter?.addItems(items)
                } else {
                    searchAdapter?.removeLoadingView()
                    searchAdapter?.addItem(items)
                }
            }
        })

        viewmodel.errorResp.observe(this, Observer { error ->
            if (error != null) {
                rvScrollListener.setLoaded()
                searchAdapter?.removeLoadingView()

                handleShowErrorLayout()
                if (error is Int) {
                    setErrorLayoutMessage(getString(error))
                    showErrorSnackbar(layout_parent, getString(error))
                } else if (error is String) {
                    setErrorLayoutMessage(error)
                    showErrorSnackbar(layout_parent, error)
                } else {
                    setErrorLayoutMessage(getString(R.string.error_api_message))
                    showErrorSnackbar(layout_parent, getString(R.string.error_api_message))
                }
            } else {
                viewflipper.displayedChild = 0
            }
        })

        viewmodel.showProgressBar.observe(this, Observer { show ->
            if (show != null) {
                if (show)
                    viewflipper.displayedChild = 1
            }
        })
    }

    private fun setErrorLayoutMessage(error: String) {
        if (error != null)
            error_message.setText(error)
        else
            error_message.setText(getString(R.string.error_api_message))
    }

    private fun handleShowErrorLayout() {
        if (viewmodel.shouldShowErrorLayout()) {
            viewflipper.displayedChild = 2
        } else {
            viewflipper.displayedChild = 0
        }
    }

    private fun initCallback() {

        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewmodel.resetPages()
                    viewmodel.setRefresh(true)
                    handleFetchUserListData()
                }
                return false;
            }

        })

        btn_retry.setOnClickListener {
            handleFetchUserListData()
        }

        rvScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                rvScrollListener.setLoaded()
                searchAdapter?.addLoadingView()
                handleFetchUserListData()
            }

        })

        rv_search.addOnScrollListener(rvScrollListener)
    }

    private fun handleFetchUserListData() {
        val searchKey = et_search.text.toString()
        if (!searchKey.isEmpty()) {
            viewmodel.fetchRepoList(searchKey)
        }
    }


    private fun showErrorSnackbar(view: View, message: String) {
        val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val v = mSnackbar.view
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.color_text_red))
        mSnackbar.show()
    }
}
