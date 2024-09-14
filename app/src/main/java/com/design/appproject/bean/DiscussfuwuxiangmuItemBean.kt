package com.design.appproject.bean

/**
 * 服务项目评论表实体类
 */
data class DiscussfuwuxiangmuItemBean(
    var id:Long=0L,
    var refid:Long=0,
    var userid:Long=0,
    var avatarurl:String="",
    var nickname:String="",
    var content:String="",
    var reply:String="",
    var addtime:String="",
)