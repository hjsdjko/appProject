package com.design.appproject.ui.chat

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.ActivityChatLayoutBinding
import com.design.appproject.logic.viewmodel.chat.ChatModel
import com.union.union_basic.ext.showToast
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File

/**
 * 客服聊天界面
 * */
@Route(path = CommonArouteApi.PATH_FRAGMENT_CHAT_LIST)
class ChatFragment: BaseBindingFragment<ActivityChatLayoutBinding>() {

    private val mChatModel by viewModels<ChatModel>()

    override fun initEvent() {
        setBarTitle("客服中心")
        binding.apply {
            chatSrv.isEnabled =false
            chatSrv.setAdapter(ChatListAdapter())
            sendBtn.setOnClickListener { /*发送内容*/
                if (replyEt.text.toString().isNullOrEmpty()){
                    "请输入内容".showToast()
                }else{
                    mChatModel.addChat(replyEt.text.toString().trim())
                }
            }
            replyEt.setOnEditorActionListener { textView, i, keyEvent ->
                sendBtn.callOnClick()
                true
            }
            KeyboardUtils.registerSoftInputChangedListener(requireActivity()) { height ->
                chatLl.setPadding(0, 0, 0, height)
            }
            jiahaoIfv.setOnClickListener {
                jiaLl.isVisible = !jiaLl.isVisible
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            fileTv.setOnClickListener {
                SmartPictureSelector.openPicture(requireActivity() as AppCompatActivity){
                    val path = it[0]
                    showLoading("发送中...")
                    mChatModel.upload(File(path))
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        mChatModel.saveChat("您好，在线客服很高兴为您服务！")
        mChatModel.saveChatLiveData.observeKt {
            mChatModel.chatList()
        }
        mChatModel.chatListLiveData.observeKt {
            it.getOrNull()?.let {
                binding.chatSrv.setData(it.data.list,it.data.total)
                binding.chatSrv.mRecyclerView.postDelayed({ binding.chatSrv.mRecyclerView.scrollToPosition(it.data.total-1) },200)
            }
        }
        mChatModel.addChatLiveData.observeKt {
            it.getOrNull()?.let {
                mChatModel.chatList()
                KeyboardUtils.hideSoftInput(requireActivity())
                binding.replyEt.setText("")
            }
        }
        mChatModel.uploadLiveData.observeKt {
            it.getOrNull()?.let {
                mChatModel.addChat("file/" + it.file)
            }
        }
    }
}