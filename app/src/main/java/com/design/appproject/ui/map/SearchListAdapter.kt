package com.design.appproject.ui.map

import com.amap.api.services.help.Tip
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R

class SearchListAdapter: BaseQuickAdapter<Tip, BaseViewHolder>(R.layout.map_search_list_item_layout) {

    override fun convert(holder: BaseViewHolder, item: Tip) {
        holder.setText(R.id.search_tv,"${item.name} • ${item.district}")
    }
}