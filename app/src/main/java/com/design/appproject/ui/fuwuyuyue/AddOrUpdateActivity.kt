package com.design.appproject.ui.fuwuyuyue

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
import com.design.appproject.bean.FuwuyuyueItemBean
import com.design.appproject.ext.afterTextChanged
import com.design.appproject.bean.FuwuxiangmuItemBean
import com.design.appproject.databinding.FuwuyuyueaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 服务预约新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUYUYUE)
class AddOrUpdateActivity:BaseBindingActivity<FuwuyuyueaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mCrossTable: String = "" /*跨表表名*/

    @JvmField
    @Autowired
    var mCrossObj: FuwuxiangmuItemBean = FuwuxiangmuItemBean() /*跨表表内容*/

    @JvmField
    @Autowired
    var mStatusColumnName: String = "" /*列名*/

    @JvmField
    @Autowired
    var mStatusColumnValue: String = "" /*列值*/

    @JvmField
    @Autowired
    var mTips: String = "" /*提示*/
    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mFuwuyuyueItemBean = FuwuyuyueItemBean()

    override fun initEvent() {
        setBarTitle("服务预约")
        setBarColor("#C6EBF1","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mFuwuyuyueItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mFuwuyuyueItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mFuwuyuyueItemBean,mRefid)
                }
            }
            if (mFuwuyuyueItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mFuwuyuyueItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mFuwuyuyueItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mFuwuyuyueItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mFuwuyuyueItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mFuwuyuyueItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun FuwuyuyueaddorupdateLayoutBinding.initView(){
             fengmianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "fengmian").observeKt{
                    it.getOrNull()?.let {
                        fengmianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mFuwuyuyueItemBean.fengmian = "file/" + it.file
                    }
                }
            }
        }
            fuwuleixingBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mFuwuyuyueItemBean.fuwuleixing = content
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
            yuyueleixingBs.setOptions("到店服务,上门服务".split(","),"请选择预约类型",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    yuyueleixingBs.text = content
                    mFuwuyuyueItemBean.yuyueleixing = content
                }
            })
            val myuyueshijianPicker = DatimePicker(this@AddOrUpdateActivity).apply {
            wheelLayout.setDateFormatter(BirthdayFormatter())
            wheelLayout.setTimeFormatter(UnitTimeFormatter())
            wheelLayout.setRange(DatimeEntity.yearOnFuture(-100), DatimeEntity.yearOnFuture(50), DatimeEntity.now())
            setOnDatimePickedListener { year, month, day, hour, minute, second ->
                yuyueshijianTv.text = "$year-$month-$day $hour:$minute:$second"
                mFuwuyuyueItemBean.yuyueshijian="$year-$month-$day $hour:$minute:$second"
            }
        }
            yuyueshijianTv.setOnClickListener {
            myuyueshijianPicker.show()
        }
            dianpumingchengBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mFuwuyuyueItemBean.dianpumingcheng = content
                }
            })
            spinner.setOnClickListener {
                spinner.options.isNullOrEmpty().yes {
                    UserRepository.option("dianpu", "dianpumingcheng", "",null,"",false).observeKt{
                        it.getOrNull()?.let {
                            spinner.setOptions(it.data, "请选择店铺名称", false)
                            spinner.dialogShow()
                        }
                    }
                }.otherwise {
                    spinner.dialogShow()
                }
            }
        }
            fuwurenyuanBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mFuwuyuyueItemBean.fuwurenyuan = content
                }
            })
            spinner.setOnClickListener {
                UserRepository.option("fuwurenyuan","yuangongxingming",conditionColumn = "dianpumingcheng",
                    conditionValue = dianpumingchengBs.text.toString().trim(),"",false).observeKt{
                it.getOrNull()?.let {
                    spinner.setOptions(it.data, "请选择服务人员", false)
                    spinner.dialogShow()
                }
            }
            }
        }
            fuwuzhuangtaiBs.isEnabled =false
            fuwuzhuangtaiBs.setOptions("已完成,未完成".split(","),"请选择服务状态",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    fuwuzhuangtaiBs.text = content
                    mFuwuyuyueItemBean.fuwuzhuangtai = content
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
                    mFuwuyuyueItemBean.yonghuzhanghao = it["yonghuzhanghao"].toString()
                    binding.yonghuzhanghaoEt.keyListener = null
                    mFuwuyuyueItemBean.yonghuxingming = it["yonghuxingming"].toString()
                    binding.yonghuxingmingEt.keyListener = null
                    mFuwuyuyueItemBean.shoujihaoma = it["shoujihaoma"].toString()
                    binding.shoujihaomaEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<FuwuyuyueItemBean>("fuwuyuyue",mId).observeKt {
                it.getOrNull()?.let {
                    mFuwuyuyueItemBean = it.data
                    mFuwuyuyueItemBean.id = mId
                    binding.setData()
                }
            }
        }
        if (mCrossTable.isNotNullOrEmpty()){/*跨表*/
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyuebianhao"}.yes {
                mFuwuyuyueItemBean.yuyuebianhao = mCrossObj.javaClass.getDeclaredField("yuyuebianhao").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwumingcheng"}.yes {
                mFuwuyuyueItemBean.fuwumingcheng = mCrossObj.javaClass.getDeclaredField("fuwumingcheng").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fengmian"}.yes {
                mFuwuyuyueItemBean.fengmian = mCrossObj.javaClass.getDeclaredField("fengmian").also { it.isAccessible=true }.get(mCrossObj).toString().split(",")[0]
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwuleixing"}.yes {
                mFuwuyuyueItemBean.fuwuleixing = mCrossObj.javaClass.getDeclaredField("fuwuleixing").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwujiage"}.yes {
                mFuwuyuyueItemBean.fuwujiage = mCrossObj.javaClass.getDeclaredField("fuwujiage").also { it.isAccessible=true }.get(mCrossObj) as Double
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyueleixing"}.yes {
                mFuwuyuyueItemBean.yuyueleixing = mCrossObj.javaClass.getDeclaredField("yuyueleixing").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyueshijian"}.yes {
                mFuwuyuyueItemBean.yuyueshijian = mCrossObj.javaClass.getDeclaredField("yuyueshijian").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "shangmendizhi"}.yes {
                mFuwuyuyueItemBean.shangmendizhi = mCrossObj.javaClass.getDeclaredField("shangmendizhi").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "beizhu"}.yes {
                mFuwuyuyueItemBean.beizhu = mCrossObj.javaClass.getDeclaredField("beizhu").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yonghuzhanghao"}.yes {
                mFuwuyuyueItemBean.yonghuzhanghao = mCrossObj.javaClass.getDeclaredField("yonghuzhanghao").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yonghuxingming"}.yes {
                mFuwuyuyueItemBean.yonghuxingming = mCrossObj.javaClass.getDeclaredField("yonghuxingming").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "shoujihaoma"}.yes {
                mFuwuyuyueItemBean.shoujihaoma = mCrossObj.javaClass.getDeclaredField("shoujihaoma").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "dianpumingcheng"}.yes {
                mFuwuyuyueItemBean.dianpumingcheng = mCrossObj.javaClass.getDeclaredField("dianpumingcheng").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwurenyuan"}.yes {
                mFuwuyuyueItemBean.fuwurenyuan = mCrossObj.javaClass.getDeclaredField("fuwurenyuan").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwuzhuangtai"}.yes {
                mFuwuyuyueItemBean.fuwuzhuangtai = mCrossObj.javaClass.getDeclaredField("fuwuzhuangtai").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
        }
        mFuwuyuyueItemBean.sfsh = "待审核"
        mFuwuyuyueItemBean.fuwuzhuangtai = "未完成"
        binding.setData()
    }

    /**验证*/
    private fun FuwuyuyueaddorupdateLayoutBinding.submit() {
        mFuwuyuyueItemBean.yuyuebianhao = yuyuebianhaoEt.text.toString()
        mFuwuyuyueItemBean.fuwumingcheng = fuwumingchengEt.text.toString()
        fuwujiageEt.inputType = InputType.TYPE_CLASS_NUMBER
        mFuwuyuyueItemBean.fuwujiage = fuwujiageEt.text.toString().toDoubleOrNull()?:0.0
        mFuwuyuyueItemBean.shangmendizhi = shangmendizhiEt.text.toString()
        mFuwuyuyueItemBean.beizhu = beizhuEt.text.toString()
        mFuwuyuyueItemBean.yonghuzhanghao = yonghuzhanghaoEt.text.toString()
        mFuwuyuyueItemBean.yonghuxingming = yonghuxingmingEt.text.toString()
        mFuwuyuyueItemBean.shoujihaoma = shoujihaomaEt.text.toString()
        if(mFuwuyuyueItemBean.fuwumingcheng.toString().isNullOrEmpty()){
            "服务名称不能为空".showToast()
            return
        }
        if(mFuwuyuyueItemBean.fuwuleixing.toString().isNullOrEmpty()){
            "服务类型不能为空".showToast()
            return
        }
        if(mFuwuyuyueItemBean.fuwujiage<=0){
            "服务价格不能为空".showToast()
            return
        }
        var crossuserid:Long = 0
        var crossrefid:Long = 0
        var crossoptnum:Int = 0
        if (mStatusColumnName.isNotNullOrEmpty()){
            if (!mStatusColumnName.startsWith("[")){
                mCrossObj.javaClass.declaredFields.any{it.name == mStatusColumnName}.yes {
                    mCrossObj.javaClass.getDeclaredField(mStatusColumnName).also { it.isAccessible=true }.set(mCrossObj,mStatusColumnValue)
                    UserRepository.update(mCrossTable,mCrossObj).observeForever {  }
                }
            }else{
                crossuserid = Utils.getUserId()
                mCrossObj.javaClass.declaredFields.any{it.name == "id"}.yes {
                    crossrefid =mCrossObj.javaClass.getDeclaredField("id").also { it.isAccessible=true }.get(mCrossObj).toString().toLong()
                }
                crossoptnum = mStatusColumnName.replace("[","").replace("]","").toIntOrNull()?:0
            }
        }

        if (crossuserid>0 && crossrefid>0){
            mFuwuyuyueItemBean.javaClass.declaredFields.any{it.name == "crossuserid"}.yes {
                mFuwuyuyueItemBean.javaClass.getDeclaredField("crossuserid").also { it.isAccessible=true }.set(mFuwuyuyueItemBean,crossuserid)
            }
            mFuwuyuyueItemBean.javaClass.declaredFields.any{it.name == "crossrefid"}.yes {
                mFuwuyuyueItemBean.javaClass.getDeclaredField("crossrefid").also { it.isAccessible=true }.set(mFuwuyuyueItemBean,crossrefid)
            }
            HomeRepository.list<FuwuyuyueItemBean>("fuwuyuyue", mapOf("page" to "1","limit" to "10","crossuserid" to crossuserid.toString(),"crossrefid" to crossrefid.toString())).observeKt{
                it.getOrNull()?.let {
                    if (it.data.list.size>=crossoptnum){
                        mTips.showToast()
                    }else{
                        crossCal()
                    }
                }
            }
        }else{
            crossCal()
        }

}
    private fun crossCal(){/*更新跨表数据*/
        addOrUpdate()
    }
    private fun addOrUpdate(){/*更新或添加*/
        if (mFuwuyuyueItemBean.id>0){
            UserRepository.update("fuwuyuyue",mFuwuyuyueItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<FuwuyuyueItemBean>("fuwuyuyue",mFuwuyuyueItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun FuwuyuyueaddorupdateLayoutBinding.setData(){
        if (mFuwuyuyueItemBean.yuyuebianhao.isNotNullOrEmpty()){
            yuyuebianhaoEt.setText(mFuwuyuyueItemBean.yuyuebianhao)
        }
        yuyuebianhaoEt.setText(Utils.genTradeNo())
        if (mFuwuyuyueItemBean.fuwumingcheng.isNotNullOrEmpty()){
            fuwumingchengEt.setText(mFuwuyuyueItemBean.fuwumingcheng)
        }
        if (mFuwuyuyueItemBean.fengmian.isNotNullOrEmpty()){
            fengmianIfv.load(this@AddOrUpdateActivity, mFuwuyuyueItemBean.fengmian)
        }
        if (mFuwuyuyueItemBean.fuwuleixing.isNotNullOrEmpty()){
            fuwuleixingBs.text =mFuwuyuyueItemBean.fuwuleixing
        }
        if (mFuwuyuyueItemBean.fuwujiage>=0){
            fuwujiageEt.setText(mFuwuyuyueItemBean.fuwujiage.toString())
        }
        if (mFuwuyuyueItemBean.yuyueleixing.isNotNullOrEmpty()){
            yuyueleixingBs.text =mFuwuyuyueItemBean.yuyueleixing
        }
        yuyueshijianTv.text = mFuwuyuyueItemBean.yuyueshijian
        if (mFuwuyuyueItemBean.shangmendizhi.isNotNullOrEmpty()){
            shangmendizhiEt.setText(mFuwuyuyueItemBean.shangmendizhi)
        }
        if (mFuwuyuyueItemBean.beizhu.isNotNullOrEmpty()){
            beizhuEt.setText(mFuwuyuyueItemBean.beizhu)
        }
        if (mFuwuyuyueItemBean.yonghuzhanghao.isNotNullOrEmpty()){
            yonghuzhanghaoEt.setText(mFuwuyuyueItemBean.yonghuzhanghao)
        }
        if (mFuwuyuyueItemBean.yonghuxingming.isNotNullOrEmpty()){
            yonghuxingmingEt.setText(mFuwuyuyueItemBean.yonghuxingming)
        }
        if (mFuwuyuyueItemBean.shoujihaoma.isNotNullOrEmpty()){
            shoujihaomaEt.setText(mFuwuyuyueItemBean.shoujihaoma)
        }
        if (mFuwuyuyueItemBean.dianpumingcheng.isNotNullOrEmpty()){
            dianpumingchengBs.text =mFuwuyuyueItemBean.dianpumingcheng
        }
        if (mFuwuyuyueItemBean.fuwurenyuan.isNotNullOrEmpty()){
            fuwurenyuanBs.text =mFuwuyuyueItemBean.fuwurenyuan
        }
        if (mFuwuyuyueItemBean.fuwuzhuangtai.isNotNullOrEmpty()){
            fuwuzhuangtaiBs.text =mFuwuyuyueItemBean.fuwuzhuangtai
        }
    }
}