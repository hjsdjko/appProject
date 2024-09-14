package com.design.appproject.ui.fuwurenyuan
import com.union.union_basic.ext.*
import android.text.InputType
import android.annotation.SuppressLint
import android.text.method.PasswordTransformationMethod
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.RegexUtils
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.FuwurenyuanactivityRegisterLayoutBinding
import com.design.appproject.ext.load
import androidx.core.view.children
import com.design.appproject.widget.BottomSpinner
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.blankj.utilcode.util.TimeUtils
import com.design.appproject.bean.FuwurenyuanItemBean
import com.design.appproject.logic.viewmodel.fuwurenyuan.RegisterViewModel
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import java.text.SimpleDateFormat
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File
import java.util.*

/**
 * Fuwurenyuan注册界面
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_REGISTER_FUWURENYUAN)
class RegisterActivity : BaseBindingActivity<FuwurenyuanactivityRegisterLayoutBinding>() {

    private val mRegisterViewModel by viewModels<RegisterViewModel>()

    /**注册请求参数*/
    var reqisterBean = FuwurenyuanItemBean()

    @SuppressLint("SimpleDateFormat")
    override fun initEvent() {
        binding.apply {
            setBarTitle("注册")
            setBarColor("#C6EBF1","black")
            initView()
        }
    }

    /**初始化控件状态*/
    private fun FuwurenyuanactivityRegisterLayoutBinding.initView() {
        titleIv.load(this@RegisterActivity,"http://codegen.caihongy.cn/20201114/7856ba26477849ea828f481fa2773a95.jpg",needPrefix=false)
        registerLl.findViewWithTag<EditText>("yuangongxingming")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.yuangongxingming = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<EditText>("yuangongmima")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.yuangongmima = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<LinearLayout>("touxiang").let {
            it.setOnClickListener {
                SmartPictureSelector.openPicture(this@RegisterActivity) {
                    val path = it[0]
                    showLoading("上传中...")
                    mRegisterViewModel.upload(File(path), "touxiang")
                }
            }
        }
        registerLl.findViewWithTag<BottomSpinner>("xingbie")
            .let { spinner ->
                spinner.setOptions(
                    "男,女".split(","),
                    "请选择性别",
                    listener = object : BottomSpinner.OnItemSelectedListener {
                        override fun onItemSelected(position: Int, content: String) {
                            super.onItemSelected(position, content)
                            spinner.text = content
                            reqisterBean.xingbie = content
                        }
                    })
            }
        registerLl.findViewWithTag<EditText>("lianxidianhua")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.lianxidianhua = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<EditText>("dianpumingcheng")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.dianpumingcheng = et?.toString() ?: ""
                }
            })
        }
        registerBtn.setOnClickListener {
            verify().yes {
                showLoading("注册中...")
                mRegisterViewModel.register("fuwurenyuan", reqisterBean)
            }
        }
    }

    /**验证*/
    private fun verify(): Boolean {
        binding.registerLl.findViewWithTag<EditText>("yuangongxingming").let {
            if (it.text.toString().isNullOrEmpty()) {
                "员工姓名不能为空".showToast()
                return@verify false
            } else {
                reqisterBean.yuangongxingming = it.text.toString()
            }
        }
        binding.registerLl.findViewWithTag<EditText>("yuangongmima").let {
            if (it.text.toString().isNullOrEmpty()) {
                "员工密码不能为空".showToast()
                return@verify false
            } else {
                reqisterBean.yuangongmima = it.text.toString()
            }
        }
        return true
    }

    override fun initData() {
        super.initData()
        mRegisterViewModel.optionLiveData.observeKt {
            it.getOrNull()?.let {
                it.callBackData?.toConversion<Pair<String, Boolean>>()?.let { callData ->
                    val spinnerView =
                        binding.registerLl.findViewWithTag<BottomSpinner>(callData.first)
                    spinnerView.setOptions(it.data, spinnerView.hint.toString(), callData.second)
                    spinnerView.dialogShow()
                }
            }
        }
        mRegisterViewModel.uploadLiveData.observeKt {
            it.getOrNull()?.let {
                it.callBackData?.let { tag ->
                    val imageView =
                        binding.registerLl.findViewWithTag<LinearLayout>(tag).getChildAt(1)
                            .toConversion<ImageView>()
                    imageView?.load(this, "file/"+it.file)
                    if (tag.toString()=="touxiang") {
                        reqisterBean.touxiang = "file/" + it.file
                    }
                }
            }
        }

        mRegisterViewModel.registerLiveData.observeKt {
            it.getOrNull()?.let {
                "注册成功".showToast()
                finish()
            }
        }
    }
}