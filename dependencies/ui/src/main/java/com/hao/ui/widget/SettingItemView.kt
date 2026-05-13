package com.hao.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.hao.common.utils.LogUtil
import com.hao.common.utils.UIUtils
import com.hao.ui.R
import com.hao.ui.ext.load
import com.hao.ui.ext.loadRoundCorner

/**
 * @author 李敬卫 2022-02-15
 */
class SettingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var ivLeftPic: AppCompatImageView
    var ivRightPic: AppCompatImageView
    var ivRightArrow: AppCompatImageView
    var tvLeftTitleText: AppCompatTextView
    var tvRightDetailText: AppCompatTextView

    private var leftText: String? = ""
    private var rightText: String? = ""
    private var rightTextColor: Int = 0
    private var leftTextColor: Int = 0
    private var leftTextSize: Int = 0
    private var rightTextSize: Int = 0
    private var leftImageSize: Int = 0
    private var rightImageSize: Int = 0
    private var rightImage: Int = 0
    private var rightImageVisible: Int = 0
    private var rightTextVisible: Int = 0
    private var rightArrowVisible: Int = 0
    private var backgroundCorner: Int = -1

    private var leftImage: Int = 0
    private var leftImageVisible: Int = 2

    init {
        LayoutInflater.from(context).inflate(R.layout.setting_item_view, this)
        ivLeftPic = findViewById(R.id.iv_left_pic)
        tvLeftTitleText = findViewById(R.id.tv_left_item)
        tvRightDetailText = findViewById(R.id.tv_right_item)
        ivRightPic = findViewById(R.id.iv_right_pic)
        ivRightArrow = findViewById(R.id.iv_right_arrow)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        leftImage =
            typedArray.getResourceId(R.styleable.SettingItemView_leftImage, 0)

        leftImageVisible =
            typedArray.getInt(R.styleable.SettingItemView_leftImageVisible, View.GONE)

        leftText = typedArray.getString(R.styleable.SettingItemView_leftText)
        rightText = typedArray.getString(R.styleable.SettingItemView_rightText)
        leftTextColor = typedArray.getColor(
            R.styleable.SettingItemView_leftTextColor,
            ContextCompat.getColor(getContext(), R.color.main_font_color)
        )
        rightTextColor = typedArray.getColor(
            R.styleable.SettingItemView_rightTextColor,
            ContextCompat.getColor(getContext(), R.color.detail_font_color)
        )

        leftTextSize =
            typedArray.getDimension(R.styleable.SettingItemView_leftTextSize, UIUtils.sp2px(context, 14f) * 1f).toInt()
        rightTextSize =
            typedArray.getDimension(R.styleable.SettingItemView_rightTextSize, UIUtils.sp2px(context, 14f) * 1f).toInt()

        leftImageSize =
            typedArray.getDimension(R.styleable.SettingItemView_leftImageSize, UIUtils.dip2px(context, 14) * 1f).toInt()
        rightImageSize =
            typedArray.getDimension(R.styleable.SettingItemView_rightImageSize, UIUtils.dip2px(context, 14) * 1f).toInt()

        rightImage =
            typedArray.getResourceId(R.styleable.SettingItemView_rightImage, 0)

        rightImageVisible =
            typedArray.getInt(R.styleable.SettingItemView_rightImageVisible, View.GONE)
        rightTextVisible =
            typedArray.getInt(R.styleable.SettingItemView_rightTextVisible, View.GONE)
        rightArrowVisible =
            typedArray.getInt(R.styleable.SettingItemView_rightArrowVisible, View.VISIBLE)
        backgroundCorner =
            typedArray.getInt(R.styleable.SettingItemView_backgroundCorner, -1)
        typedArray.recycle()



        if (leftText?.isNotEmpty() == true) {
            setLeftItem(leftText!!)
        }

        LogUtil.i("SettingItemView leftText $leftText rightText ${rightText} rightArrowVisible $rightArrowVisible")
        if (rightText?.isNotEmpty() == true) {
            setRightItem(rightText!!)
        }

        setLeftItemColor(leftTextColor)
        setRightItemColor(rightTextColor)
        setLeftItemTextSize(leftTextSize)
        setRightItemTextSize(rightTextSize)

        setLeftItemImageSize(leftImageSize)
        setRightItemImageSize(rightImageSize)

        isVisible()
    }

    private fun isVisible() {

        setLeftImage(leftImage)
        setRightImage(rightImage)

        when (leftImageVisible) {
            0 -> {
                setLeftImageVisibility(VISIBLE)
            }
            1 -> {
                setLeftImageVisibility(INVISIBLE)
            }
            2 -> {
                setLeftImageVisibility(GONE)
            }
        }
        when (rightImageVisible) {
            0 -> {
                setRightImageVisibility(VISIBLE)
            }
            1 -> {
                setRightImageVisibility(INVISIBLE)
            }
            2 -> {
                setRightImageVisibility(GONE)
            }
        }

        when (rightTextVisible) {
            0 -> {
                setRightItemVisibility(VISIBLE)
            }
            1 -> {
                setRightItemVisibility(INVISIBLE)
            }
            2 -> {
                setRightItemVisibility(GONE)
            }
        }

        when (rightArrowVisible) {
            0 -> {
                setRightArrowVisibility(VISIBLE)
            }
            1 -> {
                setRightArrowVisibility(INVISIBLE)
            }
            2 -> {
                setRightArrowVisibility(GONE)
            }
        }

        setBackgroundColorCorner(backgroundCorner)

    }

    fun setIvBackOnClickListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    fun setLeftImage(@DrawableRes resId: Int) {
        ivLeftPic.setImageResource(resId)
    }

    fun setLeftImage(url: String) {
        ivLeftPic.load(url)
    }

    fun setLeftImageVisibility(visibility: Int) {
        ivLeftPic.visibility = visibility
    }

    fun setLeftItem(leftText: String) {
        tvLeftTitleText.text = leftText
    }

    fun setLeftItemColor(leftTextColor: Int) {
        tvLeftTitleText.setTextColor(leftTextColor)
    }

    fun setLeftItemTextSize(leftTextSize: Int) {
        tvLeftTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize.toFloat())
    }

    fun setRightItemTextSize(rightTextSize: Int) {
        tvRightDetailText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
    }

    fun setLeftItemImageSize(leftImageSize: Int) {
        ivLeftPic.layoutParams.width = leftImageSize
        ivLeftPic.layoutParams.height = leftImageSize
        ivLeftPic.requestLayout()
    }

    fun setRightItemImageSize(rightTextSize: Int) {
        ivRightPic.layoutParams.width = rightTextSize
        ivRightPic.layoutParams.height = rightTextSize
        ivRightPic.requestLayout()
    }


    fun setRightItem(rightText: String) {
        tvRightDetailText.text = rightText
    }

    fun setRightItemColor(@ColorInt rightTextColor: Int) {
        tvRightDetailText.setTextColor(rightTextColor)
    }

    fun setRightItemVisibility(visibility: Int) {
        tvRightDetailText.visibility = visibility
    }

    fun setRightImage(@DrawableRes resId: Int) {
        ivRightPic.setImageResource(resId)
    }

    fun setRightImageVisibility(visibility: Int) {
        ivRightPic.visibility = visibility
    }

    fun setRightRoundCornerImage(url: String) {
        ivRightPic.loadRoundCorner(url)
    }

    fun setRightImage(url: String) {
        ivRightPic.load(url)
    }

    fun setRightArrowVisibility(visibility: Int) {
        ivRightArrow.visibility = visibility
    }

    fun setBackgroundCorner(backgroundCorner: Int) {
        this.backgroundCorner = backgroundCorner
        setBackgroundColorCorner(backgroundCorner)
    }

    /**
     *  设置背景颜色和圆角
     */
    private fun setBackgroundColorCorner(backgroundCorner: Int) {
        when (backgroundCorner) {
            -1 -> {
                setBackgroundResource(R.drawable.item_press_ripple_bg)
            }
            0 -> {
                setBackgroundResource(R.drawable.item_press_ripple_bg_radius_16dp_top)
            }
            1 -> {
                setBackgroundResource(R.drawable.item_press_ripple_bg_radius_16dp_middle)
            }
            2 -> {
                setBackgroundResource(R.drawable.item_press_ripple_bg_radius_16dp_bottom)
            }
            3 -> {
                setBackgroundResource(R.drawable.item_press_ripple_bg_radius_16dp)
            }
        }

    }

}