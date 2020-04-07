package com.kevin.cermatitest.viewholder

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kevin.cermatitest.R
import com.kevin.cermatitest.model.User.User
import com.kevin.cermatitest.utils.ImageLoader

class SearchViewHolder(view : View) : RecyclerView.ViewHolder(view), bindSearchView{

    private val userIcon = view.findViewById<AppCompatImageView>(R.id.tv_user_icon)
    private val userName = view.findViewById<AppCompatTextView>(R.id.tv_user_name)

    override fun bindView(user: User?, context: Context?) {
        if(user!=null && context!=null){
            ImageLoader().loadImage(context,user.iconUrl,userIcon,"")
            userName.setText(user.name)
        }
    }

}

interface bindSearchView{
    fun bindView(user : User?, context : Context?)
}