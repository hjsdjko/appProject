package com.design.appproject.ui.fuwuwancheng

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
import com.design.appproject.logic.viewmodel.fuwuwancheng.DetailsViewModel
import androidx.activity.viewModels
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.databinding.FuwuwanchengcommonDetailsLayoutBinding
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
 * 服务完成详情页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_FUWUWANCHENG)
class DetailsFragment : BaseBindingFragment<FuwuwanchengcommonDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*id*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    private val mDetailsViewModel by viewModels<DetailsViewModel>()

    private var mFuwuwanchengItemBean=FuwuwanchengItemBean()/*详情内容*/


    @SuppressLint("SuspiciousIndentation")
    override fun initEvent() {
        setBarTitle("服务完成详情页")
        setBarColor("#C6EBF1","black")
        binding.apply{
            srv.setOnRefreshListener {
                loadData()
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
                mFuwuwanchengItemBean = info.data
                 binding.setInfo()
                binding.payBtn.text = if(info.data.ispay != "已支付") "支付" else info.data.ispay
                binding.payBtn.setOnClickListener {
                    if (info.data.ispay != "已支付"){
                        ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_PAY_CONFIRM_FUWUWANCHENG,mapOf("paytable" to "fuwuwancheng","payObject" to info.data),requireActivity(),101)
                    }
                }
            }
        }
        mDetailsViewModel.updateLiveData.observeKt {
            it.getOrNull()?.let {
            }

        }
    }

    private fun loadData(){
        mDetailsViewModel.info("fuwuwancheng",mId.toString())
    }


    private fun FuwuwanchengcommonDetailsLayoutBinding.setInfo(){
        yuyuebianhaoTv.text = "${mFuwuwanchengItemBean.yuyuebianhao}"
        fuwumingchengTv.text = "${mFuwuwanchengItemBean.fuwumingcheng}"
        banner.setAdapter(DetailBannerAdapter(mFuwuwanchengItemBean.fengmian.split(","))).setOnBannerListener { data, position ->
            data.toConversion<String>()?.let {
                it.showToast()
            }
        }
        fuwuleixingTv.text = "${mFuwuwanchengItemBean.fuwuleixing}"
        fuwujiageTv.text = "${mFuwuwanchengItemBean.fuwujiage}"
        yuyueleixingTv.text = "${mFuwuwanchengItemBean.yuyueleixing}"
        yuyueshijianTv.text = "${mFuwuwanchengItemBean.yuyueshijian}"
        shangmendizhiTv.text = "${mFuwuwanchengItemBean.shangmendizhi}"
        beizhuTv.text = "${mFuwuwanchengItemBean.beizhu}"
        yonghuzhanghaoTv.text = "${mFuwuwanchengItemBean.yonghuzhanghao}"
        yonghuxingmingTv.text = "${mFuwuwanchengItemBean.yonghuxingming}"
        shoujihaomaTv.text = "${mFuwuwanchengItemBean.shoujihaoma}"
        dianpumingchengTv.text = "${mFuwuwanchengItemBean.dianpumingcheng}"
        fuwurenyuanTv.text = "${mFuwuwanchengItemBean.fuwurenyuan}"
        wanchengshijianTv.text = "${mFuwuwanchengItemBean.wanchengshijian}"
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){
            loadData()
        }
    }

}