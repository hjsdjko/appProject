package com.design.appproject.ui.news

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import android.widget.ImageView
import com.design.appproject.ext.load
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.bean.news.NewsItemBean
import com.union.union_basic.ext.toConversion

class NewsListAdapter: LoadMoreAdapter<NewsItemBean>(R.layout.news_list_item_layout) {

    override fun convert(holder: BaseViewHolder, item: NewsItemBean) {
        holder.setText(R.id.title_tv,item.title)
    }

}