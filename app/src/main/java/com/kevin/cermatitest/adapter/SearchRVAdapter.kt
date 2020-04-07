package com.kevin.cermatitest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevin.cermatitest.R
import com.kevin.cermatitest.model.User.User
import com.kevin.cermatitest.viewholder.LoadingViewHolder
import com.kevin.cermatitest.viewholder.SearchViewHolder


class SearchRVAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<User?> = arrayListOf()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
            return SearchViewHolder(view)
        } else {
            val loadingView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(loadingView)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ITEM -> {
                val itemHolder = holder as SearchViewHolder
                holder.bindView(items[position], context)
            }
        }
    }

    fun addLoadingView() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingView() {
        if (items.size != 0) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) == null) {
            return VIEW_TYPE_LOADING
        } else {
            return VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(newItem: List<User>?) {
        if (newItem != null) {
            clearItems()
            items.addAll(newItem)
            notifyItemRangeInserted(itemCount, newItem.size)
        }
    }

    fun addItem(newItem: List<User>?) {
        if (newItem != null) {
            items.addAll(newItem)
            notifyItemRangeInserted(itemCount, newItem.size)
        }

    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }


}