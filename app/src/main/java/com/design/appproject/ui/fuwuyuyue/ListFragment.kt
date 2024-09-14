package com.design.appproject.ui.fuwuyuyue

import com.design.appproject.base.NetRetrofitClient
import com.design.appproject.utils.ArouterUtils
import androidx.fragment.app.viewModels
import com.design.appproject.base.BaseBindingFragment
import com.alibaba.android.arouter.launcher.ARouter
import android.widget.*
import android.view.View
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.design.appproject.ext.load
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.blankj.utilcode.util.ColorUtils
import androidx.core.view.children
import androidx.core.view.isVisible
import com.design.appproject.R
import com.design.appproject.ui.fuwuyuyue.ListFilterDialog
import android.view.ViewGroup
import android.view.Gravity
import androidx.core.view.setPadding
import androidx.core.view.setMargins
import com.design.appproject.widget.SpacesItemDecoration
import com.union.union_basic.ext.*
import net.lucode.hackware.magicindicator.MagicIndicator
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.FuwuyuyuecommonListLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.viewmodel.fuwuyuyue.ListModel
import com.design.appproject.ui.fuwuyuyue.ListAdapter
import com.design.appproject.utils.Utils
import com.google.android.flexbox.*
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.widget.MagicIndexCommonNavigator
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.blankj.utilcode.util.KeyboardUtils
import com.design.appproject.bean.FuwuyuyueItemBean
import com.design.appproject.widget.MyFlexboxLayoutManager
/**
 * 服务预约列表页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_FUWUYUYUE)
class ListFragment : BaseBindingFragment<FuwuyuyuecommonListLayoutBinding>() {

    @JvmField
    @Autowired
    var mSearch: String = "" /*搜索*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    @JvmField
    @Autowired
    var mHasBack: Boolean = true /*是否有返回键*/

    private val mListAdapter by lazy {
        ListAdapter().apply {
            mIsBack = this@ListFragment.mIsBack
        }
    }

    private val mListModel by viewModels<ListModel>()

    private val params by lazy { /*请求参数*/
        mutableMapOf(
            "page" to "1",
            "limit" to "6",
        )
    }
    val queryIndex = Pair<String,String>("服务名称","fuwumingcheng")/*查询索引*/

    override fun initEvent() {
        setBarTitle("服务预约",mHasBack)
        setBarColor("#C6EBF1","black")
        mIsBack.no{
            params.put("sfsh","是")
        }
        binding.apply {
            addBtn.isVisible = (mIsBack && Utils.isAuthBack("fuwuyuyue","新增"))|| Utils.isAuthFront("fuwuyuyue","新增")
            addBtn.setOnClickListener {
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUYUYUE).navigation()
            }
            initSearch()
            srv.setOnRefreshListener {
                loadData(1)
            }
            srv.mRecyclerView.layoutManager = MyFlexboxLayoutManager(requireActivity()).apply {
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.SPACE_BETWEEN
            }
          mListAdapter.addChildClickViewIds(R.id.edit1_tv,R.id.edit2_left_tv,R.id.edit2_right_tv
              ,R.id.edit3_tv,R.id.edit4_left_tv,R.id.edit4_right_tv,R.id.delete1_tv,R.id.delete2_left_tv
              ,R.id.delete2_right_tv,R.id.delete3_tv,R.id.delete4_left_tv,R.id.delete4_right_tv,R.id.list_box1_fbl,
              R.id.list_box2_left_fbl, R.id.list_box2_right_fbl, R.id.list_box3_fbl, R.id.list_box4_left_fbl, R.id.list_box4_right_fbl
)
            mListAdapter.setOnItemChildClickListener { adapter, view, position ->
                when(view.id){
                    R.id.edit1_tv,R.id.edit2_left_tv,R.id.edit2_right_tv,
                    R.id.edit3_tv,R.id.edit4_left_tv,R.id.edit4_right_tv->ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FUWUYUYUE)
                        .withLong("mId",mListAdapter.data[position][when(view.id){
                        R.id.edit2_left_tv->1
                        R.id.edit2_right_tv->2
                        R.id.edit3_tv->3
                        R.id.edit4_left_tv->4
                        R.id.edit4_right_tv->5
                        else->0
                    }].id)
                        .navigation()
                    R.id.delete1_tv,R.id.delete2_left_tv,R.id.delete2_right_tv,
                    R.id.delete3_tv,R.id.delete4_left_tv,R.id.delete4_right_tv->XPopup.Builder(requireActivity()).asConfirm("提示","是否确认删除") {
                        HomeRepository.delete<Any>("fuwuyuyue",listOf(mListAdapter.data[position][when(view.id){
                            R.id.delete2_left_tv->1
                            R.id.delete2_right_tv->2
                            R.id.delete3_tv->3
                            R.id.delete4_left_tv->4
                            R.id.delete4_right_tv->5
                            else->0
                        }].id)).observeKt {
                            it.getOrNull()?.let {
                                "删除成功".showToast()
                                loadData(1)
                            }
                        }
                    }.show()
                    R.id.list_box1_fbl, R.id.list_box2_left_fbl, R.id.list_box2_right_fbl,
                    R.id.list_box3_fbl, R.id.list_box4_left_fbl, R.id.list_box4_right_fbl,->ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_DETAILS_FUWUYUYUE,map=mapOf("mId" to mListAdapter.data[position][when(view.id){
                        R.id.list_box2_left_fbl->1
                        R.id.list_box2_right_fbl->2
                        R.id.list_box3_fbl->3
                        R.id.list_box4_left_fbl->4
                        R.id.list_box4_right_fbl->5
                        else->0
                    }].id,"mIsBack" to mIsBack))
                }
            }
            srv.setAdapter(mListAdapter.apply {
                pageLoadMoreListener {
                    loadData(it)
                }
            })
        }
    }
    private fun FuwuyuyuecommonListLayoutBinding.initSearch() {
        searchValueEt.hint = queryIndex.first
        searchValueEt.setText(mSearch)
        searchBtn.setOnClickListener {
            loadData(1)
        }
        searchValueEt.postDelayed({ KeyboardUtils.hideSoftInput(searchValueEt) }, 200)
    }



    override fun initData() {
        super.initData()
        loadData(1)
        mListModel.pageLiveData.observeKt {
            it.getOrNull()?.let {
                addLists(it.data.list)
                binding.srv.setData(lists,Math.ceil(it.data.total / 6.0).toInt())
            }
        }

        mListModel.listLiveData.observeKt {
            it.getOrNull()?.let {
                addLists(it.data.list)
                binding.srv.setData(lists,Math.ceil(it.data.total / 6.0).toInt())
            }
        }

    }

    private val lists = mutableListOf<List<FuwuyuyueItemBean>>()

    //添加list集合到lists
    private fun addLists(list: List<FuwuyuyueItemBean>) {
        var lastList = mutableListOf<FuwuyuyueItemBean>()
        if (lists.isNotEmpty()) {
            lastList = lists.last().toMutableList()
        }
        list.forEach {
            if (lastList.isEmpty() || lastList.size >= 6) {
                lastList = mutableListOf()
                lists.add(list)
                lastList.add(it)
            } else {
                lastList.add(it)
            }
        }
    }

    private fun loadData(page:Int,searchParams:MutableMap<String,String>?=null){
        if (page==1){
            lists.clear()
            binding.srv.reload()
        }
        params.put("page",page.toString())
        binding.searchValueEt.text.toString().isNotNullOrEmpty().yes {
            params.put("fuwumingcheng","%" + binding.searchValueEt.text.toString() + "%" )
        }.otherwise {
            params.remove("fuwumingcheng")
        }
        val requestParams = mutableMapOf<String,String>()
        requestParams.putAll(params)
        searchParams?.let { requestParams.putAll(it) }
        mIsBack.yes {
                mListModel.page("fuwuyuyue", requestParams)
        }.otherwise {
                mListModel.list("fuwuyuyue", requestParams)
        }
    }
}