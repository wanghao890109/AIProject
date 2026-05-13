package com.hao.ui.widget.bottomnavbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.opensource.svgaplayer.SVGAParser
import java.lang.ref.WeakReference
import java.util.*


class BottomNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "BottomNavigationBar"
        private const val DEFAULT_ANIMATION_DURATION = 200
        val INTERPOLATOR: Interpolator = LinearOutSlowInInterpolator()
    }

    private var mBottomNavigationItems = ArrayList<BottomNavigationItem>()
    private var mBottomNavigationHolders = ArrayList<BottomNavigationItemViewHolder>()
    private var mSvgaTabBindViewListener: OnSvgaTabBindViewListener? = null

    private var mActiveColor = 0
    private var mInActiveColor = 0

    private var mTranslationAnimator: ViewPropertyAnimatorCompat? = null
    var mAnimationDuration = DEFAULT_ANIMATION_DURATION.toLong()
    private var mIsHidden = false

    private val svgaParser by lazy { SVGAParser(getContext()) }

    private var mSelectedPosition = 0
    private var mLastSelectedPosition = -1
    private val mTabSelectedListeners = ArrayList<OnTabSelectedListener>()
    private var mViewPagerSelectedListener: OnTabSelectedListener? = null
    private var mOnPageChangeListener: OnPageChangeListener? = null

    private var lastClickTime: Long = 0
    private val onTabFastDoubleClickListener: OnTabFastDoubleClickListener? = null
    private var itemWidth = ViewGroup.LayoutParams.WRAP_CONTENT

    init {
        clipToPadding = false
        clipChildren = false
        orientation = HORIZONTAL
    }

    fun setSvgaTabBindViewListener(tabBindViewListener: OnSvgaTabBindViewListener?) {
        mSvgaTabBindViewListener = tabBindViewListener
    }

    fun clearItem(): BottomNavigationBar {
        mBottomNavigationItems.clear()
        return this
    }

    fun addItem(item: BottomNavigationItem): BottomNavigationBar {
        mBottomNavigationItems.add(item)
        return this
    }

    fun removeItem(item: BottomNavigationItem): BottomNavigationBar {
        mBottomNavigationItems.remove(item)
        return this
    }

    fun getTabCount(): Int {
        return mBottomNavigationItems.size
    }

    fun setActiveColor(@ColorRes activeColor: Int): BottomNavigationBar {
        this.mActiveColor = ContextCompat.getColor(context, activeColor)
        return this
    }

    fun setInActiveColor(@ColorRes inActiveColor: Int): BottomNavigationBar {
        this.mInActiveColor = ContextCompat.getColor(context, inActiveColor)
        return this
    }

    fun setSelectedPosition(firstSelectedPosition: Int): BottomNavigationBar {
        this.mSelectedPosition = firstSelectedPosition
        return this
    }

    fun refreshTabStatus(index: Int) {
        if (mBottomNavigationItems.isEmpty()) {
            return
        }
        val bottomNavigationItem = mBottomNavigationItems[index]
        val holder = mBottomNavigationHolders.getOrNull(index)
        if (holder != null) {
            updateItemHolder(holder, bottomNavigationItem, index, true)
        }
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        mViewPagerSelectedListener = ViewPagerOnTabSelectedListener(viewPager)
        addTabSelectedListener(mViewPagerSelectedListener!!)
        mOnPageChangeListener = TabLayoutOnPageChangeListener(this)
        viewPager.addOnPageChangeListener(mOnPageChangeListener!!)
    }

    fun addTabSelectedListener(tabSelectedListener: OnTabSelectedListener): BottomNavigationBar {
        mTabSelectedListeners.add(tabSelectedListener)
        return this
    }

    fun clearAll() {
        mBottomNavigationItems.clear()
    }

    private fun calculateItemWidth() {
        val total = com.hao.common.utils.UIUtils.getScreenWidth(context) - paddingLeft - paddingRight
        itemWidth = total / mBottomNavigationItems.size
    }

    fun initialise() {
        if (mBottomNavigationItems.isNotEmpty()) {

            val adjustWidth = mBottomNavigationItems.size != mBottomNavigationHolders.size
            //重新计算宽度
            if (adjustWidth) {
                calculateItemWidth()
            }
            //移除多余的view
            if (mBottomNavigationItems.size < mBottomNavigationHolders.size) {
                for (i in mBottomNavigationItems.size until mBottomNavigationHolders.size - 1) {
                    getChildAt(i)?.let {
                        removeView(it)
                    }
                    mBottomNavigationHolders.removeAt(i)
                }
            }

            mBottomNavigationItems.forEachIndexed { i, bottomNavigationItem ->
                if (bottomNavigationItem.activeColor == 0) {
                    bottomNavigationItem.activeColor = this.mActiveColor
                }
                if (bottomNavigationItem.inActiveColor == 0) {
                    bottomNavigationItem.inActiveColor = this.mInActiveColor
                }
                var bottomNavigationItemViewHolder = mBottomNavigationHolders.getOrNull(i)
                if (bottomNavigationItemViewHolder == null) {
                    bottomNavigationItemViewHolder = createItemHolder(bottomNavigationItem, i)
                    mBottomNavigationHolders.add(bottomNavigationItemViewHolder)
                    addView(bottomNavigationItemViewHolder.itemView, itemWidth, LayoutParams.MATCH_PARENT)
                } else if (bottomNavigationItemViewHolder.itemInfo != bottomNavigationItem) {
                    updateItemHolder(bottomNavigationItemViewHolder, bottomNavigationItem, i)
                    if (adjustWidth) {
                        bottomNavigationItemViewHolder.itemView?.layoutParams?.width = itemWidth
                    }
                }
            }
        }
    }

    private fun createItemHolder(bottomNavigationItem: BottomNavigationItem, index: Int): BottomNavigationItemViewHolder {
        val bottomNavigationItemViewHolder = SvgaBottomNavigationHolder(bottomNavigationItem) { itemView, iconView, labelView ->
                mSvgaTabBindViewListener?.onBindView(itemView, index)
            }
        bottomNavigationItemViewHolder.svgaParser = svgaParser
        bottomNavigationItemViewHolder.createItemView(this)
        bottomNavigationItemViewHolder.position = index
        if (index == mSelectedPosition) {
            bottomNavigationItemViewHolder.isSelected = true
        }

        bottomNavigationItemViewHolder.bindItemView()
        bottomNavigationItemViewHolder.itemView?.setOnClickListener {
            if (mSelectedPosition == bottomNavigationItemViewHolder.position && System.currentTimeMillis() - lastClickTime < 300 && onTabFastDoubleClickListener != null) {
                onTabFastDoubleClickListener.onDoubleClick(mSelectedPosition)
                lastClickTime = 0
                return@setOnClickListener
            }
            lastClickTime = System.currentTimeMillis()
            for (listener in mTabSelectedListeners) {
                if (listener.onTabSelectedBefore(bottomNavigationItemViewHolder.position)) {
                    return@setOnClickListener
                }
            }
            //这里从bottomNavigationItemViewHolder里拿itemInfo， 不能直接用bottomNavigationItem，因为updateItemHolder没有重设onclick
            if (bottomNavigationItemViewHolder.itemInfo.isShowReplace && bottomNavigationItemViewHolder.isSelected
                && bottomNavigationItemViewHolder.itemInfo.mISlidingTopListener != null
            ) {
                bottomNavigationItemViewHolder.itemInfo.mISlidingTopListener!!.onSlidingTop()
                return@setOnClickListener
            }
            val pos = bottomNavigationItemViewHolder.position
            selectTabInternal(pos, true, false)
        }
        return bottomNavigationItemViewHolder
    }

    private fun updateItemHolder(bottomNavigationItemViewHolder: BottomNavigationItemViewHolder, bottomNavigationItem: BottomNavigationItem, index: Int, isShowAnimation: Boolean = false) {
        bottomNavigationItemViewHolder.reset()
        bottomNavigationItemViewHolder.position = index
        if (index == mSelectedPosition) {
            bottomNavigationItemViewHolder.isSelected = true
            bottomNavigationItemViewHolder.isShowAnimation = !isLastSelectPosition() || isShowAnimation
        } else {
            bottomNavigationItemViewHolder.isSelected = false
            bottomNavigationItemViewHolder.isShowAnimation = true
        }
        bottomNavigationItemViewHolder.itemInfo = bottomNavigationItem
        bottomNavigationItemViewHolder.bindItemView()
    }

    private fun isLastSelectPosition(): Boolean {
        val isLast = mLastSelectedPosition != -1 && mSelectedPosition == mLastSelectedPosition
        mLastSelectedPosition = mSelectedPosition
        return isLast
    }

    fun selectTab(newPosition: Int, callListener: Boolean) {
        selectTabInternal(newPosition, callListener, callListener)
    }

    fun getItemViewHolder(position: Int):BottomNavigationItemViewHolder? {
        return mBottomNavigationHolders.getOrNull(position)
    }

    private fun selectTabInternal(newPosition: Int, callListener: Boolean, forcedSelection: Boolean) {
        val oldPosition = mSelectedPosition
        if (mSelectedPosition != newPosition) {
            mBottomNavigationHolders.getOrNull(oldPosition)?.unSelect()
            mBottomNavigationHolders.getOrNull(newPosition)?.onSelect()
            mSelectedPosition = newPosition
        }
        if (callListener) {
            sendListenerCall(oldPosition, newPosition, forcedSelection)
        }
    }

    private fun sendListenerCall(oldPosition: Int, newPosition: Int, forcedSelection: Boolean) {
        if (mTabSelectedListeners.isEmpty()) {
            return
        }
        for (listener in mTabSelectedListeners) {
            if (forcedSelection) {
                listener.onTabSelected(newPosition)
            } else {
                if (oldPosition == newPosition) {
                    listener.onTabReselected(newPosition)
                } else {
                    listener.onTabSelected(newPosition)
                    if (oldPosition != -1) {
                        listener.onTabUnselected(oldPosition)
                    }
                }
            }
        }
    }

    fun hide() {
        hide(true)
    }

    /**
     * @param animate is animation enabled for hide
     */
    fun hide(animate: Boolean) {
        mIsHidden = true
        setTranslationY(this.height, animate)
    }

    /**
     * show with animation
     */
    fun show() {
        show(true)
    }

    /**
     * @param animate is animation enabled for show
     */
    fun show(animate: Boolean) {
        mIsHidden = false
        setTranslationY(0, animate)
    }

    /**
     * @param offset  offset needs to be set
     * @param animate is animation enabled for translation
     */
    private fun setTranslationY(offset: Int, animate: Boolean) {
        if (animate) {
            animateOffset(offset)
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator?.cancel()
            }
            this.translationY = offset.toFloat()
        }
    }

    /**
     * @param offset translation offset that needs to set with animation
     */
    private fun animateOffset(offset: Int) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this)
            mTranslationAnimator!!.duration = mAnimationDuration
            mTranslationAnimator!!.interpolator = INTERPOLATOR
        } else {
            mTranslationAnimator!!.cancel()
        }
        mTranslationAnimator!!.translationY(offset.toFloat()).start()
    }

    fun setAnimationDuration(animationDuration: Long): BottomNavigationBar {
        this.mAnimationDuration = animationDuration
        return this
    }

    fun isHidden(): Boolean {
        return mIsHidden
    }

    class TabLayoutOnPageChangeListener(bottomNavigationBar: BottomNavigationBar) : OnPageChangeListener {
        private val mBottomNavigationBarRef: WeakReference<BottomNavigationBar> = WeakReference(bottomNavigationBar)
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            val bottomNavigationBar = mBottomNavigationBarRef.get()
            if (bottomNavigationBar != null && bottomNavigationBar.mSelectedPosition != position && position < bottomNavigationBar.getTabCount()) {
                bottomNavigationBar.selectTab(position, true)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener interfaces
    ///////////////////////////////////////////////////////////////////////////
    private class ViewPagerOnTabSelectedListener(private val mViewPager: ViewPager) : OnTabSelectedListener {
        override fun onTabSelected(position: Int) {
            mViewPager.setCurrentItem(position, false)
        }

        override fun onTabUnselected(position: Int) {}
        override fun onTabReselected(position: Int) {}
        override fun onTabSelectedBefore(positon: Int): Boolean {
            return false
        }
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int)

        fun onTabUnselected(position: Int)

        fun onTabReselected(position: Int)

        /**
         * 点击之前的回调
         *
         * @param positon
         * @return false 正常点击  true表示点击无响应
         */
        fun onTabSelectedBefore(positon: Int): Boolean
    }

    interface OnTabFastDoubleClickListener {
        fun onDoubleClick(pos: Int)
    }

    interface OnSvgaTabBindViewListener {
        fun onBindView(view: View, index: Int)
    }
}