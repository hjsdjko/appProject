package com.design.appproject.ui.fuwurenyuan

import android.Manifest
import com.union.union_basic.permission.PermissionUtil
import com.design.appproject.ext.UrlPrefix
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.core.view.children
import com.design.appproject.utils.Utils
import com.design.appproject.bean.BaiKeBean
import androidx.core.app.ActivityCompat.startActivityForResult
import com.blankj.utilcode.util.UriUtils
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.*
import com.blankj.utilcode.util.RegexUtils
import com.union.union_basic.utils.StorageUtil
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.design.appproject.widget.BottomSpinner
import com.design.appproject.base.CommonBean
import com.blankj.utilcode.util.TimeUtils
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import java.text.SimpleDateFormat
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.bean.FuwurenyuanItemBean
import com.design.appproject.databinding.FuwurenyuanaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 服务人员新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWURENYUAN)
class AddOrUpdateActivity:BaseBindingActivity<FuwurenyuanaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mFuwurenyuanItemBean = FuwurenyuanItemBean()

    override fun initEvent() {
        setBarTitle("服务人员")
        setBarColor("#C6EBF1","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mFuwurenyuanItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mFuwurenyuanItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mFuwurenyuanItemBean,mRefid)
                }
            }
            if (mFuwurenyuanItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mFuwurenyuanItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mFuwurenyuanItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mFuwurenyuanItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mFuwurenyuanItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mFuwurenyuanItemBean,Utils.getUserId())
            }
        }
        binding.initView()

        binding.gerenjianjieRichLayout.apply{
            actionBold.setOnClickListener {
                richEt.setBold()
            }
            actionItalic.setOnClickListener {
                richEt.setItalic()
            }
            actionStrikethrough.setOnClickListener {
                richEt.setStrikeThrough()
            }
            actionUnderline.setOnClickListener {
                richEt.setUnderline()
            }
            actionHeading1.setOnClickListener {
                richEt.setHeading(1)
            }
            actionHeading2.setOnClickListener {
                richEt.setHeading(2)
            }
            actionHeading3.setOnClickListener {
                richEt.setHeading(3)
            }
            actionHeading4.setOnClickListener {
                richEt.setHeading(4)
            }
            actionHeading5.setOnClickListener {
                richEt.setHeading(5)
            }
            actionIndent.setOnClickListener {
                richEt.setIndent()
            }
            actionOutdent.setOnClickListener {
                richEt.setOutdent()
            }
            actionAlignCenter.setOnClickListener {
                richEt.setAlignCenter()
            }
            actionAlignLeft.setOnClickListener {
                richEt.setAlignLeft()
            }
            actionAlignRight.setOnClickListener {
                richEt.setAlignRight()
            }
            actionInsertBullets.setOnClickListener {
                richEt.setBullets()
            }
            actionInsertNumbers.setOnClickListener {
                richEt.setNumbers()
            }
            actionInsertImage.setOnClickListener {
                SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                    val path = it[0]
                    UserRepository.upload(File(path),"").observeKt {
                        it.getOrNull()?.let {
                            richEt.insertImage(UrlPrefix.URL_PREFIX+"file/" + it.file, "dachshund", 320)
                        }
                    }
                }
            }
        }
    }

    fun FuwurenyuanaddorupdateLayoutBinding.initView(){
             touxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        touxiangIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mFuwurenyuanItemBean.touxiang = "file/" + it.file
                    }
                }
            }
        }
            xingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    xingbieBs.text = content
                    mFuwurenyuanItemBean.xingbie = content
                }
            })
            submitBtn.setOnClickListener{/*提交*/
                submit()
            }
            setData()
    }

    lateinit var mUserBean:LinkedTreeMap<String, Any>/*当前用户数据*/

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mUserBean = it
                    it["touxiang"]?.let { it1 -> StorageUtil.encode(CommonBean.HEAD_URL_KEY, it1) }
                    /**ss读取*/
                    mFuwurenyuanItemBean.dianpumingcheng = it["dianpumingcheng"].toString()
                    binding.dianpumingchengEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<FuwurenyuanItemBean>("fuwurenyuan",mId).observeKt {
                it.getOrNull()?.let {
                    mFuwurenyuanItemBean = it.data
                    mFuwurenyuanItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun FuwurenyuanaddorupdateLayoutBinding.submit() {
        mFuwurenyuanItemBean.yuangongxingming = yuangongxingmingEt.text.toString()
        mFuwurenyuanItemBean.yuangongmima = yuangongmimaEt.text.toString()
        mFuwurenyuanItemBean.lianxidianhua = lianxidianhuaEt.text.toString()
        mFuwurenyuanItemBean.dianpumingcheng = dianpumingchengEt.text.toString()
        mFuwurenyuanItemBean.gerenjianjie = gerenjianjieRichLayout.richEt.html
        if(mFuwurenyuanItemBean.yuangongxingming.toString().isNullOrEmpty()){
            "员工姓名不能为空".showToast()
            return
        }
        if(mFuwurenyuanItemBean.yuangongmima.toString().isNullOrEmpty()){
            "员工密码不能为空".showToast()
            return
        }
        RegexUtils.isMobileExact(mFuwurenyuanItemBean.lianxidianhua).no {
            "联系电话应输入手机格式".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mFuwurenyuanItemBean.id>0){
            UserRepository.update("fuwurenyuan",mFuwurenyuanItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<FuwurenyuanItemBean>("fuwurenyuan",mFuwurenyuanItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun FuwurenyuanaddorupdateLayoutBinding.setData(){
        if (mFuwurenyuanItemBean.yuangongxingming.isNotNullOrEmpty()){
            yuangongxingmingEt.setText(mFuwurenyuanItemBean.yuangongxingming)
        }
        if (mFuwurenyuanItemBean.yuangongmima.isNotNullOrEmpty()){
            yuangongmimaEt.setText(mFuwurenyuanItemBean.yuangongmima)
        }
        if (mFuwurenyuanItemBean.touxiang.isNotNullOrEmpty()){
            touxiangIfv.load(this@AddOrUpdateActivity, mFuwurenyuanItemBean.touxiang)
        }
        if (mFuwurenyuanItemBean.xingbie.isNotNullOrEmpty()){
            xingbieBs.text =mFuwurenyuanItemBean.xingbie
        }
        if (mFuwurenyuanItemBean.lianxidianhua.isNotNullOrEmpty()){
            lianxidianhuaEt.setText(mFuwurenyuanItemBean.lianxidianhua)
        }
        if (mFuwurenyuanItemBean.dianpumingcheng.isNotNullOrEmpty()){
            dianpumingchengEt.setText(mFuwurenyuanItemBean.dianpumingcheng)
        }
        gerenjianjieRichLayout.richEt.setHtml(mFuwurenyuanItemBean.gerenjianjie)
    }
}