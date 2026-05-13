package com.hao.ui.widget.tablayout.action

import android.graphics.Canvas
import android.view.View
import com.hao.ui.widget.tablayout.base.AbsFlowLayout
import com.hao.ui.widget.tablayout.bean.TabValue

/**
 * 绘制一个圆的指示器
 */
class CircleAction : com.hao.ui.widget.tablayout.action.BaseAction() {

    override fun config(parentView: com.hao.ui.widget.tablayout.base.AbsFlowLayout) {
        super.config(parentView)
        val child: View = parentView.getChildAt(0)
        val l: Int = parentView.paddingLeft + child.measuredWidth / 2
        val t: Int =
            parentView.paddingTop + child.measuredHeight - mTabBean.tabHeight / 2 - mTabBean.tabMarginBottom
        val r: Int = mTabBean.tabWidth + l
        val b: Int = child.measuredHeight - mTabBean.tabMarginBottom
        mTabRect.set(l.toFloat(), t.toFloat(), r.toFloat(), b.toFloat())
    }

    override fun valueChange(value: com.hao.ui.widget.tablayout.bean.TabValue) {
        super.valueChange(value)
        //由于自定义的，都是从left 开始算起的，所以这里还需要加上圆的半径
        mTabRect.left = value.left + mTabBean.tabWidth / 2
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            mTabRect.left,
            mTabRect.top,
            (mTabBean.tabWidth / 2).toFloat(),
            mPaint
        )
    }
}