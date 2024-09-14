package com.design.appproject.ui.my

import android.content.Intent
import android.widget.CheckBox
import android.widget.RadioButton
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.UriUtils
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.ActivityUserInfoLayoutBinding
import com.design.appproject.logic.repository.UserRepository
import com.google.gson.internal.LinkedTreeMap
import com.design.appproject.ext.load
import androidx.core.view.isVisible
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.RegexUtils
import androidx.core.view.children
import com.blankj.utilcode.util.TimeUtils
import com.design.appproject.base.CommonBean
import com.design.appproject.base.EventBus
import com.design.appproject.ext.postEvent
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.widget.BottomSpinner
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import com.union.union_basic.ext.*
import com.union.union_basic.image.selector.SmartPictureSelector
import com.union.union_basic.utils.StorageUtil
import java.io.File
import java.text.SimpleDateFormat

/**
 *用户信息界面
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_USER_INFO)
class UserInfoActivity: BaseBindingActivity<ActivityUserInfoLayoutBinding>() {

    override fun initEvent() {
        setBarTitle("用户信息")
        binding.apply {
            yonghuyonghuzhanghaoLl.isVisible = "yonghu" == CommonBean.tableName
            yonghuyonghumimaLl.isVisible = "yonghu" == CommonBean.tableName
            yonghuyonghuxingmingLl.isVisible = "yonghu" == CommonBean.tableName
            yonghutouxiangLl.isVisible = "yonghu" == CommonBean.tableName
            yonghutouxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        yonghutouxiangIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["touxiang"] = "file/" + it.file
                    }
                }
            }
        }
            yonghuxingbieLl.isVisible = "yonghu" == CommonBean.tableName
            yonghuxingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    yonghuxingbieBs.text = content
                    mSessionInfo["xingbie"] = content
                }
            })
            yonghushoujihaomaLl.isVisible = "yonghu" == CommonBean.tableName
            dianpudianpumingchengLl.isVisible = "dianpu" == CommonBean.tableName
            dianpudianpumimaLl.isVisible = "dianpu" == CommonBean.tableName
            dianpudianputupianLl.isVisible = "dianpu" == CommonBean.tableName
            dianpudianputupianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "dianputupian").observeKt{
                    it.getOrNull()?.let {
                        dianpudianputupianIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["dianputupian"] = "file/" + it.file
                    }
                }
            }
        }
            dianpuyingyeshijianLl.isVisible = "dianpu" == CommonBean.tableName
            dianpulianxidianhuaLl.isVisible = "dianpu" == CommonBean.tableName
            dianpudianpudizhiLl.isVisible = "dianpu" == CommonBean.tableName
            fuwurenyuanyuangongxingmingLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            fuwurenyuanyuangongmimaLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            fuwurenyuantouxiangLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            fuwurenyuantouxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        fuwurenyuantouxiangIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["touxiang"] = "file/" + it.file
                    }
                }
            }
        }
            fuwurenyuanxingbieLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            fuwurenyuanxingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    fuwurenyuanxingbieBs.text = content
                    mSessionInfo["xingbie"] = content
                }
            })
            fuwurenyuanlianxidianhuaLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            fuwurenyuandianpumingchengLl.isVisible = "fuwurenyuan" == CommonBean.tableName
            saveBtn.setOnClickListener { /*保存*/
                verify().yes {
                    UserRepository.update(CommonBean.tableName,mSessionInfo).observeKt {
                        it.getOrNull()?.let {
                            StorageUtil.encode(CommonBean.HEAD_URL_KEY,mSessionInfo["touxiang"].toString())
                            if(CommonBean.tableName == "yonghu"){
                                StorageUtil.encode(CommonBean.USERNAME_KEY,mSessionInfo["yonghuzhanghao"].toString())
                            }
                            if(CommonBean.tableName == "dianpu"){
                                StorageUtil.encode(CommonBean.USERNAME_KEY,mSessionInfo["dianpumingcheng"].toString())
                            }
                            if(CommonBean.tableName == "fuwurenyuan"){
                                StorageUtil.encode(CommonBean.USERNAME_KEY,mSessionInfo["yuangongxingming"].toString())
                            }
                            postEvent(EventBus.USER_INFO_UPDATED,true)
                            "修改成功".showToast()
                        }
                    }
                }
            }
            logoutBtn.setOnClickListener { /*退出登录*/
                StorageUtil.encode(CommonBean.USER_ID_KEY, 0)
                StorageUtil.encode(CommonBean.VIP_KEY, false)
                StorageUtil.encode(CommonBean.HEAD_URL_KEY, "")
                StorageUtil.encode(CommonBean.LOGIN_USER_OPTIONS, "")
                StorageUtil.encode(CommonBean.USERNAME_KEY,"")
                StorageUtil.encode(CommonBean.TOKEN_KEY, "")
                StorageUtil.encode(CommonBean.TABLE_NAME_KEY, "")
                CommonBean.tableName = ""
                postEvent(EventBus.USER_INFO_UPDATED,false)
                finish()
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_MAIN).navigation()
            }
        }
    }

    private fun ActivityUserInfoLayoutBinding.verify():Boolean{
        if ("yonghu" == CommonBean.tableName){
            mSessionInfo["yonghuzhanghao"] = yonghuyonghuzhanghaoEt.text.toString()
        }
        if (mSessionInfo["yonghuzhanghao"].toString().isNullOrEmpty()){
            "用户账号不能为空".showToast()
            return false
        }
        if ("yonghu" == CommonBean.tableName){
            mSessionInfo["yonghumima"] = yonghuyonghumimaEt.text.toString()
        }
        if (mSessionInfo["yonghumima"].toString().isNullOrEmpty()){
            "用户密码不能为空".showToast()
            return false
        }
        if ("yonghu" == CommonBean.tableName){
            mSessionInfo["yonghuxingming"] = yonghuyonghuxingmingEt.text.toString()
        }
        if (mSessionInfo["yonghuxingming"].toString().isNullOrEmpty()){
            "用户姓名不能为空".showToast()
            return false
        }
        if ("yonghu" == CommonBean.tableName){
            mSessionInfo["shoujihaoma"] = yonghushoujihaomaEt.text.toString()
        }
        mSessionInfo["shoujihaoma"] = yonghushoujihaomaEt.text.toString()
        if ("dianpu" == CommonBean.tableName){
            mSessionInfo["dianpumingcheng"] = dianpudianpumingchengEt.text.toString()
        }
        if (mSessionInfo["dianpumingcheng"].toString().isNullOrEmpty()){
            "店铺名称不能为空".showToast()
            return false
        }
        if ("dianpu" == CommonBean.tableName){
            mSessionInfo["dianpumima"] = dianpudianpumimaEt.text.toString()
        }
        if (mSessionInfo["dianpumima"].toString().isNullOrEmpty()){
            "店铺密码不能为空".showToast()
            return false
        }
        if ("dianpu" == CommonBean.tableName){
            mSessionInfo["yingyeshijian"] = dianpuyingyeshijianEt.text.toString()
        }
        if ("dianpu" == CommonBean.tableName){
            mSessionInfo["lianxidianhua"] = dianpulianxidianhuaEt.text.toString()
        }
        mSessionInfo["lianxidianhua"] = dianpulianxidianhuaEt.text.toString()
        if ("dianpu" == CommonBean.tableName){
            mSessionInfo["dianpudizhi"] = dianpudianpudizhiEt.text.toString()
        }
        if ("fuwurenyuan" == CommonBean.tableName){
            mSessionInfo["yuangongxingming"] = fuwurenyuanyuangongxingmingEt.text.toString()
        }
        if (mSessionInfo["yuangongxingming"].toString().isNullOrEmpty()){
            "员工姓名不能为空".showToast()
            return false
        }
        if ("fuwurenyuan" == CommonBean.tableName){
            mSessionInfo["yuangongmima"] = fuwurenyuanyuangongmimaEt.text.toString()
        }
        if (mSessionInfo["yuangongmima"].toString().isNullOrEmpty()){
            "员工密码不能为空".showToast()
            return false
        }
        if ("fuwurenyuan" == CommonBean.tableName){
            mSessionInfo["lianxidianhua"] = fuwurenyuanlianxidianhuaEt.text.toString()
        }
        mSessionInfo["lianxidianhua"] = fuwurenyuanlianxidianhuaEt.text.toString()
        if ("fuwurenyuan" == CommonBean.tableName){
            mSessionInfo["dianpumingcheng"] = fuwurenyuandianpumingchengEt.text.toString()
        }
        return true
    }

    lateinit var mSessionInfo:LinkedTreeMap<String, Any>

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mSessionInfo= it
                    binding.setData(it)
                }
            }
        }
    }

    private fun ActivityUserInfoLayoutBinding.setData(session:LinkedTreeMap<String, Any>){
        yonghuyonghuzhanghaoEt.setText(session["yonghuzhanghao"].toString())
        yonghuyonghumimaEt.setText(session["yonghumima"].toString())
        yonghuyonghuxingmingEt.setText(session["yonghuxingming"].toString())
        yonghutouxiangIfv.load(this@UserInfoActivity, session["touxiang"].toString())
        yonghuxingbieBs.text = session["xingbie"].toString()
        yonghushoujihaomaEt.setText(session["shoujihaoma"].toString())
        dianpudianpumingchengEt.setText(session["dianpumingcheng"].toString())
        dianpudianpumimaEt.setText(session["dianpumima"].toString())
        dianpudianputupianIfv.load(this@UserInfoActivity, session["dianputupian"].toString())
        dianpuyingyeshijianEt.setText(session["yingyeshijian"].toString())
        dianpulianxidianhuaEt.setText(session["lianxidianhua"].toString())
        dianpudianpudizhiEt.setText(session["dianpudizhi"].toString())
        fuwurenyuanyuangongxingmingEt.setText(session["yuangongxingming"].toString())
        fuwurenyuanyuangongmimaEt.setText(session["yuangongmima"].toString())
        fuwurenyuantouxiangIfv.load(this@UserInfoActivity, session["touxiang"].toString())
        fuwurenyuanxingbieBs.text = session["xingbie"].toString()
        fuwurenyuanlianxidianhuaEt.setText(session["lianxidianhua"].toString())
        fuwurenyuandianpumingchengEt.setText(session["dianpumingcheng"].toString())
    }

}