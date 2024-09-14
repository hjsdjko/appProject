package com.design.appproject.ui.yonghu
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.YonghuItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 用户适配器列表
 */
class ListAdapter : LoadMoreAdapter<List<YonghuItemBean>>(R.layout.yonghu_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: List<YonghuItemBean>) {
        holder.setGone(R.id.list_box1_fbl,item.size<0)
        val dataOne = item[0]
        val img = dataOne.touxiang.split(",")[0]
        holder.getView<ImageView>(R.id.picture1_iv).load(context,img, needPrefix = !img.startsWith("http"))
        mIsBack.yes {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthBack("yonghu","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthBack("yonghu","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthBack("yonghu","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthBack("yonghu","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthBack("yonghu","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthBack("yonghu","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthBack("yonghu","删除"))
        }.otherwise {
            holder.setGone(R.id.edit1_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.edit2_left_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.edit2_right_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.edit4_left_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.edit4_right_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.edit3_fl,!Utils.isAuthFront("yonghu","修改"))
            holder.setGone(R.id.delete1_fl,!Utils.isAuthFront("yonghu","删除"))
            holder.setGone(R.id.delete2_left_fl,!Utils.isAuthFront("yonghu","删除"))
            holder.setGone(R.id.delete2_right_fl,!Utils.isAuthFront("yonghu","删除"))
            holder.setGone(R.id.delete3_fl,!Utils.isAuthFront("yonghu","删除"))
            holder.setGone(R.id.delete4_left_fl,!Utils.isAuthFront("yonghu","删除"))
            holder.setGone(R.id.delete4_right_fl,!Utils.isAuthFront("yonghu","删除"))
        }
        holder.setGone(R.id.list_box2_fbl,item.size<=1)
        holder.setGone(R.id.list_box2_left_fbl,item.size<=1)
        if (item.size>1){
            val dataTwo = item[1]
            val imgTwo = dataTwo.touxiang.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture2_iv).load(context,imgTwo, needPrefix = !imgTwo.startsWith("http"))
        }

        holder.setGone(R.id.list_box2_right_fbl,item.size<=2)
        if (item.size>2){
            val dataThree = item[2]
            val imgThree = dataThree.touxiang.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture2_iv).load(context,imgThree, needPrefix = !imgThree.startsWith("http"))
        }
        holder.setGone(R.id.list_box3_fbl,item.size<=3)
        if (item.size>3){
            val dataFour = item[3]
            val imgFour = dataFour.touxiang.split(",")[0]
            holder.getView<ImageView>(R.id.picture3_iv).load(context,imgFour, needPrefix = !imgFour.startsWith("http"))
        }

        holder.setGone(R.id.list_box4_fbl,item.size<=4)
        holder.setGone(R.id.list_box4_left_fbl,item.size<=4)
        if (item.size>4){
            val dataFive = item[4]
            val imgFive = dataFive.touxiang.split(",")[0]
            holder.getView<ImageView>(R.id.left_picture4_iv).load(context,imgFive, needPrefix = !imgFive.startsWith("http"))
        }
        holder.setGone(R.id.list_box4_right_fbl,item.size<=5)
        if (item.size>5){
            val dataSix = item[5]
            val imgSix = dataSix.touxiang.split(",")[0]
            holder.getView<ImageView>(R.id.right_picture4_iv).load(context,imgSix, needPrefix = !imgSix.startsWith("http"))
        }
    }
}