package com.design.appproject.ui.fuwuwancheng

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
import com.design.appproject.bean.FuwuwanchengItemBean
import com.design.appproject.ext.afterTextChanged
import com.design.appproject.bean.FuwuyuyueItemBean
import com.design.appproject.databinding.FuwuwanchengaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 服务完成新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUWANCHENG)
class AddOrUpdateActivity:BaseBindingActivity<FuwuwanchengaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mCrossTable: String = "" /*跨表表名*/

    @JvmField
    @Autowired
    var mCrossObj: FuwuyuyueItemBean = FuwuyuyueItemBean() /*跨表表内容*/

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
    var mFuwuwanchengItemBean = FuwuwanchengItemBean()

    override fun initEvent() {
        setBarTitle("服务完成")
        setBarColor("#C6EBF1","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mFuwuwanchengItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mFuwuwanchengItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mFuwuwanchengItemBean,mRefid)
                }
            }
            if (mFuwuwanchengItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mFuwuwanchengItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mFuwuwanchengItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mFuwuwanchengItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mFuwuwanchengItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mFuwuwanchengItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun FuwuwanchengaddorupdateLayoutBinding.initView(){
             fengmianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "fengmian").observeKt{
                    it.getOrNull()?.let {
                        fengmianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mFuwuwanchengItemBean.fengmian = "file/" + it.file
                    }
                }
            }
        }
            fuwuleixingBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mFuwuwanchengItemBean.fuwuleixing = content
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
                    mFuwuwanchengItemBean.yuyueleixing = content
                }
            })
            val myuyueshijianPicker = DatimePicker(this@AddOrUpdateActivity).apply {
            wheelLayout.setDateFormatter(BirthdayFormatter())
            wheelLayout.setTimeFormatter(UnitTimeFormatter())
            wheelLayout.setRange(DatimeEntity.yearOnFuture(-100), DatimeEntity.yearOnFuture(50), DatimeEntity.now())
            setOnDatimePickedListener { year, month, day, hour, minute, second ->
                yuyueshijianTv.text = "$year-$month-$day $hour:$minute:$second"
                mFuwuwanchengItemBean.yuyueshijian="$year-$month-$day $hour:$minute:$second"
            }
        }
            yuyueshijianTv.setOnClickListener {
            myuyueshijianPicker.show()
        }
            mFuwuwanchengItemBean.wanchengshijian = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
            wanchengshijianTv.text = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
            val mwanchengshijianPicker = DatimePicker(this@AddOrUpdateActivity).apply {
            wheelLayout.setDateFormatter(BirthdayFormatter())
            wheelLayout.setTimeFormatter(UnitTimeFormatter())
            wheelLayout.setRange(DatimeEntity.yearOnFuture(-100), DatimeEntity.yearOnFuture(50), DatimeEntity.now())
            setOnDatimePickedListener { year, month, day, hour, minute, second ->
                wanchengshijianTv.text = "$year-$month-$day $hour:$minute:$second"
                mFuwuwanchengItemBean.wanchengshijian="$year-$month-$day $hour:$minute:$second"
            }
        }
            wanchengshijianTv.setOnClickListener {
            mwanchengshijianPicker.show()
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
                    mFuwuwanchengItemBean.yonghuzhanghao = it["yonghuzhanghao"].toString()
                    binding.yonghuzhanghaoEt.keyListener = null
                    mFuwuwanchengItemBean.yonghuxingming = it["yonghuxingming"].toString()
                    binding.yonghuxingmingEt.keyListener = null
                    mFuwuwanchengItemBean.shoujihaoma = it["shoujihaoma"].toString()
                    binding.shoujihaomaEt.keyListener = null
                    mFuwuwanchengItemBean.fuwurenyuan = it["yuangongxingming"]?.toString()?:""
                    binding.fuwurenyuanEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<FuwuwanchengItemBean>("fuwuwancheng",mId).observeKt {
                it.getOrNull()?.let {
                    mFuwuwanchengItemBean = it.data
                    mFuwuwanchengItemBean.id = mId
                    binding.setData()
                }
            }
        }
        if (mCrossTable.isNotNullOrEmpty()){/*跨表*/
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyuebianhao"}.yes {
                mFuwuwanchengItemBean.yuyuebianhao = mCrossObj.javaClass.getDeclaredField("yuyuebianhao").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwumingcheng"}.yes {
                mFuwuwanchengItemBean.fuwumingcheng = mCrossObj.javaClass.getDeclaredField("fuwumingcheng").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fengmian"}.yes {
                mFuwuwanchengItemBean.fengmian = mCrossObj.javaClass.getDeclaredField("fengmian").also { it.isAccessible=true }.get(mCrossObj).toString().split(",")[0]
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwuleixing"}.yes {
                mFuwuwanchengItemBean.fuwuleixing = mCrossObj.javaClass.getDeclaredField("fuwuleixing").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwujiage"}.yes {
                mFuwuwanchengItemBean.fuwujiage = mCrossObj.javaClass.getDeclaredField("fuwujiage").also { it.isAccessible=true }.get(mCrossObj) as Double
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyueleixing"}.yes {
                mFuwuwanchengItemBean.yuyueleixing = mCrossObj.javaClass.getDeclaredField("yuyueleixing").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yuyueshijian"}.yes {
                mFuwuwanchengItemBean.yuyueshijian = mCrossObj.javaClass.getDeclaredField("yuyueshijian").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "shangmendizhi"}.yes {
                mFuwuwanchengItemBean.shangmendizhi = mCrossObj.javaClass.getDeclaredField("shangmendizhi").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "beizhu"}.yes {
                mFuwuwanchengItemBean.beizhu = mCrossObj.javaClass.getDeclaredField("beizhu").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yonghuzhanghao"}.yes {
                mFuwuwanchengItemBean.yonghuzhanghao = mCrossObj.javaClass.getDeclaredField("yonghuzhanghao").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "yonghuxingming"}.yes {
                mFuwuwanchengItemBean.yonghuxingming = mCrossObj.javaClass.getDeclaredField("yonghuxingming").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "shoujihaoma"}.yes {
                mFuwuwanchengItemBean.shoujihaoma = mCrossObj.javaClass.getDeclaredField("shoujihaoma").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "dianpumingcheng"}.yes {
                mFuwuwanchengItemBean.dianpumingcheng = mCrossObj.javaClass.getDeclaredField("dianpumingcheng").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "fuwurenyuan"}.yes {
                mFuwuwanchengItemBean.fuwurenyuan = mCrossObj.javaClass.getDeclaredField("fuwurenyuan").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
            mCrossObj.javaClass.declaredFields.any{it.name == "wanchengshijian"}.yes {
                mFuwuwanchengItemBean.wanchengshijian = mCrossObj.javaClass.getDeclaredField("wanchengshijian").also { it.isAccessible=true }.get(mCrossObj) as  String
            }
        }
        mFuwuwanchengItemBean.ispay = "未支付"
        binding.setData()
    }

    /**验证*/
    private fun FuwuwanchengaddorupdateLayoutBinding.submit() {
        mFuwuwanchengItemBean.yuyuebianhao = yuyuebianhaoEt.text.toString()
        mFuwuwanchengItemBean.fuwumingcheng = fuwumingchengEt.text.toString()
        fuwujiageEt.inputType = InputType.TYPE_CLASS_NUMBER
        mFuwuwanchengItemBean.fuwujiage = fuwujiageEt.text.toString().toDoubleOrNull()?:0.0
        mFuwuwanchengItemBean.shangmendizhi = shangmendizhiEt.text.toString()
        mFuwuwanchengItemBean.beizhu = beizhuEt.text.toString()
        mFuwuwanchengItemBean.yonghuzhanghao = yonghuzhanghaoEt.text.toString()
        mFuwuwanchengItemBean.yonghuxingming = yonghuxingmingEt.text.toString()
        mFuwuwanchengItemBean.shoujihaoma = shoujihaomaEt.text.toString()
        mFuwuwanchengItemBean.dianpumingcheng = dianpumingchengEt.text.toString()
        mFuwuwanchengItemBean.fuwurenyuan = fuwurenyuanEt.text.toString()
        if(mFuwuwanchengItemBean.fuwumingcheng.toString().isNullOrEmpty()){
            "服务名称不能为空".showToast()
            return
        }
        if(mFuwuwanchengItemBean.fuwuleixing.toString().isNullOrEmpty()){
            "服务类型不能为空".showToast()
            return
        }
        if(mFuwuwanchengItemBean.fuwujiage<=0){
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
            mFuwuwanchengItemBean.javaClass.declaredFields.any{it.name == "crossuserid"}.yes {
                mFuwuwanchengItemBean.javaClass.getDeclaredField("crossuserid").also { it.isAccessible=true }.set(mFuwuwanchengItemBean,crossuserid)
            }
            mFuwuwanchengItemBean.javaClass.declaredFields.any{it.name == "crossrefid"}.yes {
                mFuwuwanchengItemBean.javaClass.getDeclaredField("crossrefid").also { it.isAccessible=true }.set(mFuwuwanchengItemBean,crossrefid)
            }
            HomeRepository.list<FuwuwanchengItemBean>("fuwuwancheng", mapOf("page" to "1","limit" to "10","crossuserid" to crossuserid.toString(),"crossrefid" to crossrefid.toString())).observeKt{
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
        if (mFuwuwanchengItemBean.id>0){
            UserRepository.update("fuwuwancheng",mFuwuwanchengItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<FuwuwanchengItemBean>("fuwuwancheng",mFuwuwanchengItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun FuwuwanchengaddorupdateLayoutBinding.setData(){
        if (mFuwuwanchengItemBean.yuyuebianhao.isNotNullOrEmpty()){
            yuyuebianhaoEt.setText(mFuwuwanchengItemBean.yuyuebianhao)
        }
        yuyuebianhaoEt.setText(Utils.genTradeNo())
        if (mFuwuwanchengItemBean.fuwumingcheng.isNotNullOrEmpty()){
            fuwumingchengEt.setText(mFuwuwanchengItemBean.fuwumingcheng)
        }
        if (mFuwuwanchengItemBean.fengmian.isNotNullOrEmpty()){
            fengmianIfv.load(this@AddOrUpdateActivity, mFuwuwanchengItemBean.fengmian)
        }
        if (mFuwuwanchengItemBean.fuwuleixing.isNotNullOrEmpty()){
            fuwuleixingBs.text =mFuwuwanchengItemBean.fuwuleixing
        }
        if (mFuwuwanchengItemBean.fuwujiage>=0){
            fuwujiageEt.setText(mFuwuwanchengItemBean.fuwujiage.toString())
        }
        if (mFuwuwanchengItemBean.yuyueleixing.isNotNullOrEmpty()){
            yuyueleixingBs.text =mFuwuwanchengItemBean.yuyueleixing
        }
        yuyueshijianTv.text = mFuwuwanchengItemBean.yuyueshijian
        if (mFuwuwanchengItemBean.shangmendizhi.isNotNullOrEmpty()){
            shangmendizhiEt.setText(mFuwuwanchengItemBean.shangmendizhi)
        }
        if (mFuwuwanchengItemBean.beizhu.isNotNullOrEmpty()){
            beizhuEt.setText(mFuwuwanchengItemBean.beizhu)
        }
        if (mFuwuwanchengItemBean.yonghuzhanghao.isNotNullOrEmpty()){
            yonghuzhanghaoEt.setText(mFuwuwanchengItemBean.yonghuzhanghao)
        }
        if (mFuwuwanchengItemBean.yonghuxingming.isNotNullOrEmpty()){
            yonghuxingmingEt.setText(mFuwuwanchengItemBean.yonghuxingming)
        }
        if (mFuwuwanchengItemBean.shoujihaoma.isNotNullOrEmpty()){
            shoujihaomaEt.setText(mFuwuwanchengItemBean.shoujihaoma)
        }
        if (mFuwuwanchengItemBean.dianpumingcheng.isNotNullOrEmpty()){
            dianpumingchengEt.setText(mFuwuwanchengItemBean.dianpumingcheng)
        }
        if (mFuwuwanchengItemBean.fuwurenyuan.isNotNullOrEmpty()){
            fuwurenyuanEt.setText(mFuwuwanchengItemBean.fuwurenyuan)
        }
    }
}