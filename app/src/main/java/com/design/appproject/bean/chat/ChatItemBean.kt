package com.design.appproject.bean.chat

data class ChatItemBean(
    var id:Long=0L,
    var adminid:Long=0,
    var ask:String="",
    var reply:String="",
    var isreply:Int=0,
    var userid:Long=0,
    var addtime:String="",
)