package com.design.appproject.ui.dianpu

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
import com.design.appproject.bean.DianpuItemBean
import com.design.appproject.databinding.DianpuaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 店铺新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_DIANPU)
class AddOrUpdateActivity:BaseBindingActivity<DianpuaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mDianpuItemBean = DianpuItemBean()

    override fun initEvent() {
        setBarTitle("店铺")
        setBarColor("#C6EBF1","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mDianpuItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mDianpuItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mDianpuItemBean,mRefid)
                }
            }
            if (mDianpuItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mDianpuItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mDianpuItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mDianpuItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mDianpuItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mDianpuItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun DianpuaddorupdateLayoutBinding.initView(){
             dianputupianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "dianputupian").observeKt{
                    it.getOrNull()?.let {
                        dianputupianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mDianpuItemBean.dianputupian = "file/" + it.file
                    }
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
            HomeRepository.info<DianpuItemBean>("dianpu",mId).observeKt {
                it.getOrNull()?.let {
                    mDianpuItemBean = it.data
                    mDianpuItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun DianpuaddorupdateLayoutBinding.submit() {
        mDianpuItemBean.dianpumingcheng = dianpumingchengEt.text.toString()
        mDianpuItemBean.dianpumima = dianpumimaEt.text.toString()
        mDianpuItemBean.yingyeshijian = yingyeshijianEt.text.toString()
        mDianpuItemBean.lianxidianhua = lianxidianhuaEt.text.toString()
        mDianpuItemBean.dianpudizhi = dianpudizhiEt.text.toString()
        if(mDianpuItemBean.dianpumingcheng.toString().isNullOrEmpty()){
            "店铺名称不能为空".showToast()
            return
        }
        if(mDianpuItemBean.dianpumima.toString().isNullOrEmpty()){
            "店铺密码不能为空".showToast()
            return
        }
        RegexUtils.isTel(mDianpuItemBean.lianxidianhua).no {
            "联系电话应输入电话格式".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mDianpuItemBean.id>0){
            UserRepository.update("dianpu",mDianpuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<DianpuItemBean>("dianpu",mDianpuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun DianpuaddorupdateLayoutBinding.setData(){
        if (mDianpuItemBean.dianpumingcheng.isNotNullOrEmpty()){
            dianpumingchengEt.setText(mDianpuItemBean.dianpumingcheng)
        }
        if (mDianpuItemBean.dianpumima.isNotNullOrEmpty()){
            dianpumimaEt.setText(mDianpuItemBean.dianpumima)
        }
        if (mDianpuItemBean.dianputupian.isNotNullOrEmpty()){
            dianputupianIfv.load(this@AddOrUpdateActivity, mDianpuItemBean.dianputupian)
        }
        if (mDianpuItemBean.yingyeshijian.isNotNullOrEmpty()){
            yingyeshijianEt.setText(mDianpuItemBean.yingyeshijian)
        }
        if (mDianpuItemBean.lianxidianhua.isNotNullOrEmpty()){
            lianxidianhuaEt.setText(mDianpuItemBean.lianxidianhua)
        }
        if (mDianpuItemBean.dianpudizhi.isNotNullOrEmpty()){
            dianpudizhiEt.setText(mDianpuItemBean.dianpudizhi)
        }
    }
}