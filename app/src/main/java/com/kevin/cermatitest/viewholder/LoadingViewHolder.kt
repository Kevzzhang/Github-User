package com.kevin.cermatitest.viewholder

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.kevin.cermatitest.R

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view), bindProgressBar {

    private val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
    override fun bindView() {

    }

}

interface bindProgressBar{
    fun bindView()
}