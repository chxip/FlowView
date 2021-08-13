package com.chxip.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup


/**
 * @ClassName: FlowView
 * @Description: 流式布局
 * @Author: chxip
 * @CreateDate: 2021/8/10 10:01
 */
class FlowLayout : ViewGroup {
    //数据Adapter
    private var flowLayoutAdapter: FlowLayoutAdapter<*>? = null

    //点击事件
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, parent: FlowLayout)
    }


    //每一个Item横向间距
    private var mHorizontalSpacing = dp2px(0f)

    //每一行Item纵向间距
    private var mVerticalSpeaing = dp2px(0f)

    /**
     * 设置每个item的横向边距
     */
    fun setHorizontalSpacing(hSpacing: Float) {
        mHorizontalSpacing = dp2px(hSpacing)
    }

    /**
     * 设置每个item的纵向边距
     */
    fun setVerticalSpacing(vSpacing: Float) {
        mVerticalSpeaing = dp2px(vSpacing)
    }

    //保存每一行的view的数据
    val allLineViews: MutableList<MutableList<View>> = mutableListOf()

    //保存每一行的高度
    val allLineHeights: MutableList<Int> = mutableListOf()


    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    init {
        //初始化代码

    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (flowLayoutAdapter == null) {
            return
        }
        //清空上次保存的数据
        allLineHeights.clear()
        allLineViews.clear()
        //获取控件设置的内边距
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        //保存一行中所有的view
        var lineViews: MutableList<View> = mutableListOf()
        var lineWidthUsed = 0 //记录这行已经使用了多宽的size
        var lineHeight = 0 //一行的行高
        //解析的父ViewGroup给我的参考宽度
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        //解析的父ViewGroup给我的参考高度
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)
        //所有子view中，最宽的值
        var parentNeededWidth = 0
        //所有子view的高度
        var parentNeededHeight = 0

        //记录一行，最大的topMargin和最大的bottomMargin
        var maxTopMargin = 0
        var maxBottomMargin = 0

        //先测量子view的大小
        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            //获取子view的LayoutParams --> MarginLayoutParams
            val childLP = childView.layoutParams as MarginLayoutParams
            //将LayoutParam转变成为MeasureSpec
            val childWidthMeasureSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight + childLP.leftMargin + childLP.rightMargin, childLP.width
            )
            val childHeightMeasureSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom + childLP.topMargin + childLP.bottomMargin,
                childLP.height
            )
            //测量子view的大小
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            //获取子view测量后的宽高
            val childMeasuredWidth = childView.measuredWidth
            val childMeasuredHeight = childView.measuredHeight
            //这一行的宽度 还需要加上当前view的margin
            var lineWidth =
                lineWidthUsed + childMeasuredWidth + mHorizontalSpacing + childLP.leftMargin + childLP.rightMargin
            //当这一行的宽度大于父布局的宽度时，需要换行
            if (lineWidth > selfWidth) {
                //保存上一行的数据
                allLineViews.add(lineViews)
                allLineHeights.add(lineHeight)

                //修改ViewGroup宽高 高度需要加上这一行最大的topMargin和最大的bottomMargin
                parentNeededHeight += lineHeight + mVerticalSpeaing + maxTopMargin + maxBottomMargin
                parentNeededWidth = Math.max(parentNeededWidth, lineWidth)

                //初始化下一行的数据
                lineWidth =
                    childMeasuredWidth + mHorizontalSpacing + childLP.leftMargin + childLP.rightMargin
                lineViews = mutableListOf()
                lineHeight = 0
                maxTopMargin = 0
                maxBottomMargin = 0

            }
            //view是分行的layout，所以药记录每一行有哪些view
            lineViews.add(childView)
            //记录这一行，最大的topMargin和最大的bottomMargin
            maxTopMargin = Math.max(maxTopMargin, childLP.topMargin)
            maxBottomMargin = Math.max(maxBottomMargin, childLP.bottomMargin)
            //更新每一行的宽和高
            lineWidthUsed = lineWidth
            lineHeight = Math.max(lineHeight, childMeasuredHeight)

            //处理最后一行的数据
            if (index == childCount - 1) {
                allLineViews.add(lineViews)
                allLineHeights.add(lineHeight)
                //修改宽高
                parentNeededHeight += lineHeight + mVerticalSpeaing + maxTopMargin + maxBottomMargin
                parentNeededWidth = Math.max(parentNeededWidth, lineWidth)
            }

        }

        //根据子view度量的结果，来重新度量自己ViewGroup
        //作为一个ViewGroup,它自己也是一个View，它的大小也要根据它的父View给他提供的宽高来度量
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heigthMode = MeasureSpec.getMode(heightMeasureSpec)
        //如果是实际大小，则直接使用设置的具体值
        val realWidth = if (widthMode == MeasureSpec.EXACTLY) selfWidth else parentNeededWidth
        val realHeight = if (heigthMode == MeasureSpec.EXACTLY) selfHeight else parentNeededHeight
        //保存测量的大小
        setMeasuredDimension(realWidth, realHeight)

    }


    /**
     * 布局
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var curL = paddingLeft
        var curT = paddingTop

        for ((index, itemViewList) in allLineViews.withIndex()) {
            //这一行的高度
            val lineHeight = allLineHeights[index]
            //记录这一行，最大的topMargin和最大的bottomMargin
            var maxTopMargin = 0
            var maxBottomMargin = 0
            for (view in itemViewList) {
                //获取子view的LayoutParams --> MarginLayoutParams
                val childLP = view.layoutParams as MarginLayoutParams
                //需要加上margin
                val left = curL + childLP.leftMargin
                val top = curT + childLP.topMargin
                val right = left + view.measuredWidth
                val bottom = top + view.measuredHeight
                view.layout(left, top, right, bottom)
                //下一个view的左边距离需要加上当前view的rightMargin
                curL = right + mVerticalSpeaing + childLP.rightMargin
                //记录这一行，最大的topMargin和最大的bottomMargin
                maxTopMargin = Math.max(maxTopMargin, childLP.topMargin)
                maxBottomMargin = Math.max(maxBottomMargin, childLP.bottomMargin)
            }
            //下一行距离父布局top的距离需要加上 上一行最大的topMargin和最大的bottomMargin
            curT += lineHeight + mVerticalSpeaing + maxTopMargin + maxBottomMargin
            curL = paddingLeft
        }
    }

    /**
     * 设置Adapter
     */
    fun setAdatper(adapter: FlowLayoutAdapter<*>) {
        this.flowLayoutAdapter = adapter
        removeAllViews()
        for (index in 0 until adapter.getCount()) {
            val view = adapter.getView(this, index, adapter.getData(index)!!)
            view.setOnClickListener {
                onItemClickListener?.onItemClick(it,index,this)
            }
            addView(view)
        }
        invalidate()
    }

    /**
     * 重写generateLayoutParams方法，返回自定义的LayoutParams
     * 使其可以获取Margin值
     */
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    /**
     * dp转PX
     */
    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            getResources().getDisplayMetrics()
        ).toInt()
    }
}