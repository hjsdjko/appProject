package com.design.appproject.bean

data class BaiKeBean(
    var name:String,
    var baike_info:BaikeInfo,
)

data class BaikeInfo(
    var baike_url:String,
    var description:String,
)