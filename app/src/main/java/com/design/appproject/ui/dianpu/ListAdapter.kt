package com.design.appproject.ui.dianpu
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.DianpuItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 店铺适配器列表
 */
class ListAdapter : LoadMoreAdapter<List<DianpuItemBean>>(R.layout.dianpu_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: List<DianpuItemBean>) {
        holder.setGone(R.id.list_box1_fbl,item.size<0)
        val dataOne = item[0]
        val img = dataOne.dianputupian.split(",")[0]
        holder.getView<ImageView>(R.id.picture1_iv).load(context,img, needPrefix = !img.startsWith("http"))
        holder.setText(R.id.dianpumingcheng1_tv,"店铺名称:"+ dataOne.dianpumingcheng)
        holder.setText(R.id.dianpudizhi1_tv,"店铺地址:"+ dataOne.dianpudizhi)
        mIsBack.yes {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthBack("dianpu","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthBack("dianpu","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthBack("dianpu","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthBack("dianpu","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthBack("dianpu","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthBack("dianpu","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthBack("dianpu","删除"))
        }.otherwise {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthFront("dianpu","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthFront("dianpu","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthFront("dianpu","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthFront("dianpu","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthFront("dianpu","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthFront("dianpu","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthFront("dianpu","删除"))
        }
        holder.setGone(R.id.list_box2_fbl,item.size<=1)
        holder.setGone(R.id.list_box2_left_fbl,item.size<=1)
        if (item.size>1){
            val dataTwo = item[1]
            val imgTwo = dataTwo.dianputupian.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture2_iv).load(context,imgTwo, needPrefix = !imgTwo.startsWith("http"))
            holder.setGone(R.id.left_dianpumingcheng2_tv,!true)
            holder.setText(R.id.left_dianpumingcheng2_tv,"店铺名称:"+ dataTwo.dianpumingcheng)
            holder.setGone(R.id.left_dianpudizhi2_tv,!true)
            holder.setText(R.id.left_dianpudizhi2_tv,"店铺地址:"+ dataTwo.dianpudizhi)
        }

        holder.setGone(R.id.list_box2_right_fbl,item.size<=2)
        if (item.size>2){
            val dataThree = item[2]
            val imgThree = dataThree.dianputupian.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture2_iv).load(context,imgThree, needPrefix = !imgThree.startsWith("http"))
            holder.setGone(R.id.right_dianpumingcheng2_tv,!true)
            holder.setText(R.id.right_dianpumingcheng2_tv,"店铺名称:"+ dataThree.dianpumingcheng)
            holder.setGone(R.id.right_dianpudizhi2_tv,!true)
            holder.setText(R.id.right_dianpudizhi2_tv,"店铺地址:"+ dataThree.dianpudizhi)
        }
        holder.setGone(R.id.list_box3_fbl,item.size<=3)
        if (item.size>3){
            val dataFour = item[3]
            val imgFour = dataFour.dianputupian.split(",")[0]
            holder.getView<ImageView>(R.id.picture3_iv).load(context,imgFour, needPrefix = !imgFour.startsWith("http"))
            holder.setGone(R.id.dianpumingcheng3_tv,!true)
            holder.setText(R.id.dianpumingcheng3_tv,"店铺名称:"+ dataFour.dianpumingcheng)
            holder.setGone(R.id.dianpudizhi3_tv,!true)
            holder.setText(R.id.dianpudizhi3_tv,"店铺地址:"+ dataFour.dianpudizhi)
        }

        holder.setGone(R.id.list_box4_fbl,item.size<=4)
        holder.setGone(R.id.list_box4_left_fbl,item.size<=4)
        if (item.size>4){
            val dataFive = item[4]
            val imgFive = dataFive.dianputupian.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture4_iv).load(context,imgFive, needPrefix = !imgFive.startsWith("http"))
            holder.setGone(R.id.left_dianpumingcheng4_tv,!true)
            holder.setText(R.id.left_dianpumingcheng4_tv,"店铺名称:"+ dataFive.dianpumingcheng)
            holder.setGone(R.id.left_dianpudizhi4_tv,!true)
            holder.setText(R.id.left_dianpudizhi4_tv,"店铺地址:"+ dataFive.dianpudizhi)
        }
        holder.setGone(R.id.list_box4_right_fbl,item.size<=5)
        if (item.size>5){
            val dataSix = item[5]
            val imgSix = dataSix.dianputupian.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture4_iv).load(context,imgSix, needPrefix = !imgSix.startsWith("http"))
            holder.setGone(R.id.right_dianpumingcheng4_tv,!true)
            holder.setText(R.id.right_dianpumingcheng4_tv,"店铺名称:"+ dataSix.dianpumingcheng)
            holder.setGone(R.id.right_dianpudizhi4_tv,!true)
            holder.setText(R.id.right_dianpudizhi4_tv,"店铺地址:"+ dataSix.dianpudizhi)
        }
    }
}