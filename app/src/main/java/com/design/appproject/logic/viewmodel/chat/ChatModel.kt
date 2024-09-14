package com.design.appproject.logic.viewmodel.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.chat.ChatItemBean
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.utils.Utils
import java.io.File

/**
 * 客服viewmodel
 * */
class ChatModel: ViewModel() {

    private val chatListData = MutableLiveData<Long>()

    val chatListLiveData = Transformations.switchMap(chatListData) { request ->
        chatListData.value?.let {
            HomeRepository.page<ChatItemBean>("chat", mapOf("sort" to "addtime","order" to "asc","limit" to "1000"))
        }
    }

    /**客服列表*/
    fun chatList() {
        chatListData.value = System.currentTimeMillis()
    }

    private val addChatData = MutableLiveData<String>()

    val addChatLiveData = Transformations.switchMap(addChatData) { request ->
        addChatData.value?.let {
            HomeRepository.add<Any>("chat", ChatItemBean(ask = it))
        }
    }

    /**发送消息*/
    fun addChat(ask:String) {
        addChatData.value = ask
    }

    private val saveChatData = MutableLiveData<String>()

    val saveChatLiveData = Transformations.switchMap(saveChatData) { request ->
        saveChatData.value?.let {
            HomeRepository.save<Any>("chat", ChatItemBean(reply = it,userid = Utils.getUserId()))
        }
    }

    /**发送图片文件*/
    private val uploadData = MutableLiveData<File>()

    val uploadLiveData = Transformations.switchMap(uploadData) { request ->
        uploadData.value?.let {
            UserRepository.upload(it,"")
        }
    }

    /**上传文件*/
    fun upload(file: File) {
        uploadData.value = file
    }

    /**回复消息*/
    fun saveChat(reply:String) {
        saveChatData.value = reply
    }

}