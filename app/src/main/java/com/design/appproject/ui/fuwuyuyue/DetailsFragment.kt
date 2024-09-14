package com.design.appproject.ui.fuwuyuyue

import com.design.appproject.logic.repository.UserRepository
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import android.annotation.SuppressLint
import com.union.union_basic.utils.StorageUtil
import com.design.appproject.utils.ArouterUtils
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.design.appproject.base.*
import com.design.appproject.ext.postEvent
import android.media.MediaPlayer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import com.google.gson.Gson
import android.view.Gravity
import android.view.ViewGroup
import com.design.appproject.widget.DetailBannerAdapter
import android.widget.*
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.ColorUtils
import com.design.appproject.R
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.union.union_basic.ext.*
import androidx.core.view.setMargins
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.*
import com.union.union_basic.network.DownloadListener
import com.union.union_basic.network.DownloadUtil
import java.io.File
import androidx.core.view.setPadding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.utils.Utils
import java.util.*
import kotlin.concurrent.timerTask
import com.design.appproject.ext.load
import com.design.appproject.logic.viewmodel.fuwuyuyue.DetailsViewModel
import androidx.activity.viewModels
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.databinding.FuwuyuyuecommonDetailsLayoutBinding
import com.design.appproject.bean.*
import com.design.appproject.ui.CommentsAdatper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.view.isVisible
import android.text.Html
import com.design.appproject.widget.MyTextView
import com.design.appproject.widget.MyFlexBoxLayout
import com.design.appproject.widget.MyImageView
import android.view.ContextThemeWrapper
import com.google.android.flexbox.FlexWrap
import com.union.union_basic.image.loader.GlideLoader.load
/**
 * 服务预约详情页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_FUWUYUYUE)
class DetailsFragment : BaseBindingFragment<FuwuyuyuecommonDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*id*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    private val mDetailsViewModel by viewModels<DetailsViewModel>()

    private var mFuwuyuyueItemBean=FuwuyuyueItemBean()/*详情内容*/


    @SuppressLint("SuspiciousIndentation")
    override fun initEvent() {
        setBarTitle("服务预约详情页")
        setBarColor("#C6EBF1","black")
        binding.apply{
            srv.setOnRefreshListener {
                loadData()
            }
            mIsBack.yes {
                sfshBtn.isVisible = Utils.isAuthBack("fuwuyuyue","审核")
            }.otherwise {
                sfshBtn.isVisible = Utils.isAuthFront("fuwuyuyue","审核")
            }
            sfshBtn.setOnClickListener {
                XPopup.Builder(context).asInputConfirm("",""){
                    if (mFuwuyuyueItemBean.sfsh.isNullOrEmpty()){
                        "请选择审核状态".showToast()
                        return@asInputConfirm
                    }
                    when(it){
                        "通过"-> mFuwuyuyueItemBean.sfsh = "是"
                        "不通过"->mFuwuyuyueItemBean.sfsh = "否"
                        else->mFuwuyuyueItemBean.sfsh = it
                    }
                    if (mFuwuyuyueItemBean.sfsh.isNullOrEmpty()){
                        "请填写审核回复".showToast()
                    }else{
                        mDetailsViewModel.update("fuwuyuyue",mFuwuyuyueItemBean,"shhf")
                    }
                }.show()
            }
        val fuwuzhuangtaiCrossSelect = "已完成,未完成".split(",")
            mIsBack.yes {
                crossOptButtonBtn0.isVisible = Utils.isAuthBack("fuwuyuyue","完成")
            }.otherwise {
                crossOptButtonBtn0.isVisible = Utils.isAuthFront("fuwuyuyue","完成")
            }
            crossOptButtonBtn0.setOnClickListener{/*跨表*/
            if (mFuwuyuyueItemBean.sfsh!="是"){
                "请审核通过后再操作".showToast()
                return@setOnClickListener
            }
            if (mFuwuyuyueItemBean.fuwuzhuangtai=="已完成"){
                "已完成".showToast()
                return@setOnClickListener
            }
            ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUWANCHENG)
                .withString("mCrossTable","fuwuyuyue")
                .withObject("mCrossObj",mFuwuyuyueItemBean)
                .withString("mStatusColumnName","fuwuzhuangtai")
                .withString("mStatusColumnValue","已完成")
                .withString("mTips","已完成")
                .navigation()
        }
    }
    }

    override fun initData() {
        super.initData()
        showLoading()
        loadData()
        mDetailsViewModel.infoLiveData.observeKt(errorBlock = {binding.srv.isRefreshing =false}) {
            it.getOrNull()?.let { info->
                binding.srv.isRefreshing =false
                mFuwuyuyueItemBean = info.data
                 binding.setInfo()
            }
        }
        mDetailsViewModel.updateLiveData.observeKt {
            it.getOrNull()?.let {
                if (it.callBackData=="shhf"){
                    "审核成功".showToast()
                }
            }

        }
    }

    private fun loadData(){
        mDetailsViewModel.info("fuwuyuyue",mId.toString())
    }


    private fun FuwuyuyuecommonDetailsLayoutBinding.setInfo(){
        yuyuebianhaoTv.text = "${mFuwuyuyueItemBean.yuyuebianhao}"
        fuwumingchengTv.text = "${mFuwuyuyueItemBean.fuwumingcheng}"
        banner.setAdapter(DetailBannerAdapter(mFuwuyuyueItemBean.fengmian.split(","))).setOnBannerListener { data, position ->
            data.toConversion<String>()?.let {
                it.showToast()
            }
        }
        fuwuleixingTv.text = "${mFuwuyuyueItemBean.fuwuleixing}"
        fuwujiageTv.text = "${mFuwuyuyueItemBean.fuwujiage}"
        yuyueleixingTv.text = "${mFuwuyuyueItemBean.yuyueleixing}"
        yuyueshijianTv.text = "${mFuwuyuyueItemBean.yuyueshijian}"
        shangmendizhiTv.text = "${mFuwuyuyueItemBean.shangmendizhi}"
        beizhuTv.text = "${mFuwuyuyueItemBean.beizhu}"
        yonghuzhanghaoTv.text = "${mFuwuyuyueItemBean.yonghuzhanghao}"
        yonghuxingmingTv.text = "${mFuwuyuyueItemBean.yonghuxingming}"
        shoujihaomaTv.text = "${mFuwuyuyueItemBean.shoujihaoma}"
        dianpumingchengTv.text = "${mFuwuyuyueItemBean.dianpumingcheng}"
        fuwurenyuanTv.text = "${mFuwuyuyueItemBean.fuwurenyuan}"
        fuwuzhuangtaiTv.text = "${mFuwuyuyueItemBean.fuwuzhuangtai}"
        var sfshStatus = if(mFuwuyuyueItemBean.sfsh =="是"){
            "通过"
        }else if(mFuwuyuyueItemBean.sfsh =="否"){
            "不通过"
        }else{
            "待审核"
        }
        sfshTv.isVisible = mIsBack
        sfshFbl.isVisible = mIsBack
        sfshContentTv.isVisible = mIsBack
        sfshContentFbl.isVisible = mIsBack
        sfshTv.text = "${sfshStatus}"
        sfshContentTv.text =mFuwuyuyueItemBean.shhf
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){
            loadData()
        }
    }

}