package com.chxip.view.flowlayout

import android.view.View

/**
 * @ClassName: FlowLayoutAdapter
 * @Description: 流式布局 item Adatper
 * @Author: chxip
 * @CreateDate: 2021/8/10 16:09
 */
abstract class FlowLayoutAdapter<T> {

    //保存所有的item数据
    lateinit var itemDatas: List<T>

    constructor(itemDatas: List<T>) {
        this.itemDatas = itemDatas
    }

    /**
     * 获取item view 数量
     */
    fun getCount(): Int {
        return itemDatas.size
    }

    /**
     * 获取data
     */
    fun getData(position: Int): T {
        return itemDatas[position]
    }



    abstract fun getView(flowLayout: FlowLayout,position: Int, data: Any): View


}