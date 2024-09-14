package com.design.appproject.ui.chat

import android.widget.ImageView
import androidx.core.view.isVisible
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.chat.ChatItemBean
import com.design.appproject.ext.load
import com.design.appproject.widget.LoadMoreAdapter

/**
 * 客服聊天列表适配器
 * */
class ChatListAdapter: LoadMoreAdapter<ChatItemBean>(R.layout.item_chat_list_layout) {

    override fun convert(holder: BaseViewHolder, item: ChatItemBean) {
        holder.setGone(R.id.reply_llc,item.reply.isNullOrEmpty())
        holder.setGone(R.id.ask_llc,item.ask.isNullOrEmpty())
        holder.getView<ImageView>(R.id.reply_avatar_ifv).load(context,"https://ossweb-img.qq.com/images/lol/web201310/skin/big143004.jpg", needPrefix = false)
        holder.getView<ImageView>(R.id.ask_avatar_ifv).load(context,"https://ossweb-img.qq.com/images/lol/web201310/skin/big107000.jpg", needPrefix = false)
        if (item.ask.startsWith("upload")){
            holder.getView<ImageView>(R.id.ask_iv).isVisible =true
            holder.getView<ImageView>(R.id.ask_iv).load(context,item.ask)
            holder.setText(R.id.ask_tv,"")
        }else{
            holder.getView<ImageView>(R.id.ask_iv).isVisible =false
            holder.setText(R.id.ask_tv,item.ask)
        }

        if (item.reply.startsWith("upload")){
            holder.getView<ImageView>(R.id.reply_iv).isVisible =true
            holder.getView<ImageView>(R.id.reply_iv).load(context,item.reply)
            holder.setText(R.id.reply_tv,"")
        }else{
            holder.getView<ImageView>(R.id.reply_iv).isVisible =false
            holder.setText(R.id.reply_tv,item.reply)
        }
    }
}