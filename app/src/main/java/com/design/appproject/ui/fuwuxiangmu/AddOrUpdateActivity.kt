package com.design.appproject.ui.fuwuxiangmu

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
import com.design.appproject.bean.FuwuxiangmuItemBean
import com.design.appproject.databinding.FuwuxiangmuaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 服务项目新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUXIANGMU)
class AddOrUpdateActivity:BaseBindingActivity<FuwuxiangmuaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mFuwuxiangmuItemBean = FuwuxiangmuItemBean()

    override fun initEvent() {
        setBarTitle("服务项目")
        setBarColor("#C6EBF1","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mFuwuxiangmuItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mFuwuxiangmuItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mFuwuxiangmuItemBean,mRefid)
                }
            }
            if (mFuwuxiangmuItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mFuwuxiangmuItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mFuwuxiangmuItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mFuwuxiangmuItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mFuwuxiangmuItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mFuwuxiangmuItemBean,Utils.getUserId())
            }
        }
        binding.initView()

        binding.fuwuxiangqingRichLayout.apply{
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

    fun FuwuxiangmuaddorupdateLayoutBinding.initView(){
             fengmianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "fengmian").observeKt{
                    it.getOrNull()?.let {
                        fengmianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mFuwuxiangmuItemBean.fengmian = "file/" + it.file
                    }
                }
            }
        }
            fuwuleixingBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mFuwuxiangmuItemBean.fuwuleixing = content
                }
            })
            spinner.setOnClickListener {
                spinner.options.isNullOrEmpty().yes {
                    UserRepository.option("fuwuleixing", "fuwuleixing", "",null,"",false).observeKt{
                        it.getOrNull()?.let {
                            spinner.setOptions(it.data, "请选择服务类型", false)
                            spinner.dialogShow()
                        }
                    }
                }.otherwise {
                    spinner.dialogShow()
                }
            }
        }
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
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<FuwuxiangmuItemBean>("fuwuxiangmu",mId).observeKt {
                it.getOrNull()?.let {
                    mFuwuxiangmuItemBean = it.data
                    mFuwuxiangmuItemBean.id = mId
                    binding.setData()
                }
            }
        }
        mFuwuxiangmuItemBean.storeupnum = 0
        mFuwuxiangmuItemBean.clicknum = 0
        binding.setData()
    }

    /**验证*/
    private fun FuwuxiangmuaddorupdateLayoutBinding.submit() {
        mFuwuxiangmuItemBean.fuwumingcheng = fuwumingchengEt.text.toString()
        fuwujiageEt.inputType = InputType.TYPE_CLASS_NUMBER
        mFuwuxiangmuItemBean.fuwujiage = fuwujiageEt.text.toString().toDoubleOrNull()?:0.0
        mFuwuxiangmuItemBean.fuwushijian = fuwushijianEt.text.toString()
        mFuwuxiangmuItemBean.fuwuxiangqing = fuwuxiangqingRichLayout.richEt.html
        mFuwuxiangmuItemBean.yuyuexuzhi = yuyuexuzhiEt.text.toString()
        storeupnumEt.inputType = InputType.TYPE_CLASS_NUMBER
        mFuwuxiangmuItemBean.storeupnum = storeupnumEt.text.toString().toInt()
        if(mFuwuxiangmuItemBean.fuwumingcheng.toString().isNullOrEmpty()){
            "服务名称不能为空".showToast()
            return
        }
        if(mFuwuxiangmuItemBean.fuwuleixing.toString().isNullOrEmpty()){
            "服务类型不能为空".showToast()
            return
        }
        if(mFuwuxiangmuItemBean.fuwujiage<=0){
            "服务价格不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mFuwuxiangmuItemBean.id>0){
            UserRepository.update("fuwuxiangmu",mFuwuxiangmuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<FuwuxiangmuItemBean>("fuwuxiangmu",mFuwuxiangmuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun FuwuxiangmuaddorupdateLayoutBinding.setData(){
        if (mFuwuxiangmuItemBean.fuwumingcheng.isNotNullOrEmpty()){
            fuwumingchengEt.setText(mFuwuxiangmuItemBean.fuwumingcheng)
        }
        if (mFuwuxiangmuItemBean.fengmian.isNotNullOrEmpty()){
            fengmianIfv.load(this@AddOrUpdateActivity, mFuwuxiangmuItemBean.fengmian)
        }
        if (mFuwuxiangmuItemBean.fuwuleixing.isNotNullOrEmpty()){
            fuwuleixingBs.text =mFuwuxiangmuItemBean.fuwuleixing
        }
        if (mFuwuxiangmuItemBean.fuwujiage>=0){
            fuwujiageEt.setText(mFuwuxiangmuItemBean.fuwujiage.toString())
        }
        if (mFuwuxiangmuItemBean.fuwushijian.isNotNullOrEmpty()){
            fuwushijianEt.setText(mFuwuxiangmuItemBean.fuwushijian)
        }
        if (mFuwuxiangmuItemBean.storeupnum>=0){
            storeupnumEt.setText(mFuwuxiangmuItemBean.storeupnum.toString())
        }
        fuwuxiangqingRichLayout.richEt.setHtml(mFuwuxiangmuItemBean.fuwuxiangqing)
        if (mFuwuxiangmuItemBean.yuyuexuzhi.isNotNullOrEmpty()){
            yuyuexuzhiEt.setText("mFuwuxiangmuItemBean.yuyuexuzhi")
        }
    }
}