package com.design.appproject.ui.fuwuwancheng
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.FuwuwanchengItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 服务完成适配器列表
 */
class ListAdapter : LoadMoreAdapter<List<FuwuwanchengItemBean>>(R.layout.fuwuwancheng_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: List<FuwuwanchengItemBean>) {
        holder.setGone(R.id.list_box1_fbl,item.size<0)
        val dataOne = item[0]
        val img = dataOne.fengmian.split(",")[0]
        holder.getView<ImageView>(R.id.picture1_iv).load(context,img, needPrefix = !img.startsWith("http"))
        holder.setText(R.id.fuwumingcheng1_tv, dataOne.fuwumingcheng)
        holder.setText(R.id.yuyueshijian1_tv,"预约时间:"+ dataOne.yuyueshijian)
        mIsBack.yes {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthBack("fuwuwancheng","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthBack("fuwuwancheng","删除"))
        }.otherwise {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthFront("fuwuwancheng","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthFront("fuwuwancheng","删除"))
        }
        holder.setGone(R.id.list_box2_fbl,item.size<=1)
        holder.setGone(R.id.list_box2_left_fbl,item.size<=1)
        if (item.size>1){
            val dataTwo = item[1]
            val imgTwo = dataTwo.fengmian.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture2_iv).load(context,imgTwo, needPrefix = !imgTwo.startsWith("http"))
            holder.setGone(R.id.left_fuwumingcheng2_tv,!true)
            holder.setText(R.id.left_fuwumingcheng2_tv, dataTwo.fuwumingcheng)
            holder.setGone(R.id.left_yuyueshijian2_tv,!true)
            holder.setText(R.id.left_yuyueshijian2_tv,"预约时间:"+ dataTwo.yuyueshijian)
        }

        holder.setGone(R.id.list_box2_right_fbl,item.size<=2)
        if (item.size>2){
            val dataThree = item[2]
            val imgThree = dataThree.fengmian.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture2_iv).load(context,imgThree, needPrefix = !imgThree.startsWith("http"))
            holder.setGone(R.id.right_fuwumingcheng2_tv,!true)
            holder.setText(R.id.right_fuwumingcheng2_tv, dataThree.fuwumingcheng)
            holder.setGone(R.id.right_yuyueshijian2_tv,!true)
            holder.setText(R.id.right_yuyueshijian2_tv,"预约时间:"+ dataThree.yuyueshijian)
        }
        holder.setGone(R.id.list_box3_fbl,item.size<=3)
        if (item.size>3){
            val dataFour = item[3]
            val imgFour = dataFour.fengmian.split(",")[0]
            holder.getView<ImageView>(R.id.picture3_iv).load(context,imgFour, needPrefix = !imgFour.startsWith("http"))
            holder.setGone(R.id.fuwumingcheng3_tv,!true)
            holder.setText(R.id.fuwumingcheng3_tv, dataFour.fuwumingcheng)
            holder.setGone(R.id.yuyueshijian3_tv,!true)
            holder.setText(R.id.yuyueshijian3_tv,"预约时间:"+ dataFour.yuyueshijian)
        }

        holder.setGone(R.id.list_box4_fbl,item.size<=4)
        holder.setGone(R.id.list_box4_left_fbl,item.size<=4)
        if (item.size>4){
            val dataFive = item[4]
            val imgFive = dataFive.fengmian.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture4_iv).load(context,imgFive, needPrefix = !imgFive.startsWith("http"))
            holder.setGone(R.id.left_fuwumingcheng4_tv,!true)
            holder.setText(R.id.left_fuwumingcheng4_tv, dataFive.fuwumingcheng)
            holder.setGone(R.id.left_yuyueshijian4_tv,!true)
            holder.setText(R.id.left_yuyueshijian4_tv,"预约时间:"+ dataFive.yuyueshijian)
        }
        holder.setGone(R.id.list_box4_right_fbl,item.size<=5)
        if (item.size>5){
            val dataSix = item[5]
            val imgSix = dataSix.fengmian.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture4_iv).load(context,imgSix, needPrefix = !imgSix.startsWith("http"))
            holder.setGone(R.id.right_fuwumingcheng4_tv,!true)
            holder.setText(R.id.right_fuwumingcheng4_tv, dataSix.fuwumingcheng)
            holder.setGone(R.id.right_yuyueshijian4_tv,!true)
            holder.setText(R.id.right_yuyueshijian4_tv,"预约时间:"+ dataSix.yuyueshijian)
        }
    }
}