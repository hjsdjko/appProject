package com.design.appproject.bean

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken

var roleMenusList =
    GsonUtils.fromJson<List<RoleMenusItem>>("[{\"backMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-flashlightopen\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"轮播图\",\"menuJump\":\"列表\",\"tableName\":\"config\"},{\"appFrontIcon\":\"cuIcon-cardboard\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"管理员\",\"menuJump\":\"列表\",\"tableName\":\"users\"}],\"fontClass\":\"icon-common40\",\"menu\":\"后台管理\",\"unicode\":\"&#xeebb;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-qrcode\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common19\",\"menu\":\"客服聊天管理\",\"unicode\":\"&#xee00;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-camera\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"用户\",\"menuJump\":\"列表\",\"tableName\":\"yonghu\"},{\"appFrontIcon\":\"cuIcon-goodsnew\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"店铺\",\"menuJump\":\"列表\",\"tableName\":\"dianpu\"},{\"appFrontIcon\":\"cuIcon-album\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"fontClass\":\"icon-user4\",\"menu\":\"用户管理\",\"unicode\":\"&#xef9a;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-pay\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"查看评论\",\"新增\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"},{\"appFrontIcon\":\"cuIcon-paint\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"服务类型\",\"menuJump\":\"列表\",\"tableName\":\"fuwuleixing\"},{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"店铺收入统计\",\"服务收入统计\",\"员工业绩统计\",\"首页总数\",\"首页统计\"],\"menu\":\"服务预约\",\"menuJump\":\"列表\",\"tableName\":\"fuwuyuyue\"},{\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"店铺收入统计\",\"服务收入统计\",\"员工业绩统计\",\"首页总数\",\"首页统计\"],\"menu\":\"服务完成\",\"menuJump\":\"列表\",\"tableName\":\"fuwuwancheng\"}],\"fontClass\":\"icon-common9\",\"menu\":\"服务项目管理\",\"unicode\":\"&#xedc9;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-pic\",\"buttons\":[\"查看\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"menu\":\"服务人员管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"服务预约\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"}],\"menu\":\"服务项目管理\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"roleName\":\"管理员\",\"tableName\":\"users\"},{\"backMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-qrcode\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common19\",\"menu\":\"客服聊天管理\",\"unicode\":\"&#xee00;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"查看\"],\"menu\":\"我的收藏\",\"menuJump\":\"1\",\"tableName\":\"storeup\"}],\"fontClass\":\"icon-common41\",\"menu\":\"我的收藏管理\",\"unicode\":\"&#xeede;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"支付\"],\"menu\":\"服务预约\",\"menuJump\":\"列表\",\"tableName\":\"fuwuyuyue\"},{\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"查看\",\"支付\"],\"menu\":\"服务完成\",\"menuJump\":\"列表\",\"tableName\":\"fuwuwancheng\"}],\"fontClass\":\"icon-common9\",\"menu\":\"服务项目管理\",\"unicode\":\"&#xedc9;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-pic\",\"buttons\":[\"查看\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"menu\":\"服务人员管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"服务预约\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"}],\"menu\":\"服务项目管理\"}],\"hasBackLogin\":\"否\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"是\",\"hasFrontRegister\":\"是\",\"roleName\":\"用户\",\"tableName\":\"yonghu\"},{\"backMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-album\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"fontClass\":\"icon-user4\",\"menu\":\"用户管理\",\"unicode\":\"&#xef9a;\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-pay\",\"buttons\":[\"查看\",\"查看评论\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"},{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"审核\",\"店铺收入统计\",\"服务收入统计\",\"员工业绩统计\",\"首页总数\",\"首页统计\"],\"menu\":\"服务预约\",\"menuJump\":\"列表\",\"tableName\":\"fuwuyuyue\"},{\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"查看\",\"店铺收入统计\",\"服务收入统计\",\"首页总数\",\"首页统计\",\"员工业绩统计\",\"审核\"],\"menu\":\"服务完成\",\"menuJump\":\"列表\",\"tableName\":\"fuwuwancheng\"}],\"fontClass\":\"icon-common9\",\"menu\":\"服务项目管理\",\"unicode\":\"&#xedc9;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-pic\",\"buttons\":[\"查看\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"menu\":\"服务人员管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"服务预约\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"}],\"menu\":\"服务项目管理\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"是\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"roleName\":\"店铺\",\"tableName\":\"dianpu\"},{\"backMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"完成\"],\"menu\":\"服务预约\",\"menuJump\":\"列表\",\"tableName\":\"fuwuyuyue\"},{\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"查看\"],\"menu\":\"服务完成\",\"menuJump\":\"列表\",\"tableName\":\"fuwuwancheng\"}],\"fontClass\":\"icon-common9\",\"menu\":\"服务项目管理\",\"unicode\":\"&#xedc9;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-pic\",\"buttons\":[\"查看\"],\"menu\":\"服务人员\",\"menuJump\":\"列表\",\"tableName\":\"fuwurenyuan\"}],\"menu\":\"服务人员管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"服务预约\"],\"menu\":\"服务项目\",\"menuJump\":\"列表\",\"tableName\":\"fuwuxiangmu\"}],\"menu\":\"服务项目管理\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"roleName\":\"服务人员\",\"tableName\":\"fuwurenyuan\"}]", object : TypeToken<List<RoleMenusItem>>() {}.type)

data class RoleMenusItem(
    val backMenu: List<MenuBean>,
    val frontMenu: List<MenuBean>,
    val hasBackLogin: String,
    val hasBackRegister: String,
    val hasFrontLogin: String,
    val hasFrontRegister: String,
    val roleName: String,
    val tableName: String
)

data class MenuBean(
    val child: List<Child>,
    val menu: String,
    val fontClass: String,
    val unicode: String=""
)

data class Child(
    val appFrontIcon: String,
    val buttons: List<String>,
    val menu: String,
    val menuJump: String,
    val tableName: String
)

