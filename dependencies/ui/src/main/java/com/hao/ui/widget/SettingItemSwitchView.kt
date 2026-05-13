package com.hao.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.hao.ui.R

/**
 * @author 李敬卫 2022-02-16
 */
class SettingItemSwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var scSwitch: SwitchCompat
    private var tvLeftItem: AppCompatTextView
    private var tvLeftDescribeItem: AppCompatTextView
    private var leftText: String? = ""
    private var leftTextColor: Int = 0
    private var leftTextSize: Int = 0
    private var isOpen: Boolean = true
    private var backgroundCorner: Int = 3
    private var leftDescribeVisible: Int = 2
    private var leftDescribeText: String? = ""


    init {
        LayoutInflater.from(context).inflate(R.layout.setting_item_switch_view, this)
        tvLeftItem = findViewById(R.id.tv_left_item)
        tvLeftDescribeItem = findViewById(R.id.tv_left_item_describe)
        scSwitch = findViewById(R.id.sc_switch)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        leftDescribeVisible =
            typedArray.getInt(R.styleable.SettingItemView_leftDescribeVisible, View.GONE)
        leftDescribeText = typedArray.getString(R.styleable.SettingItemView_leftDescribeText)
        leftText = typedArray.getString(R.styleable.SettingItemView_leftText)
        leftTextColor = typedArray.getColor(
            R.styleable.SettingItemView_leftTextColor,
            ContextCompat.getColor(getContext(), R.color.main_font_color)
        )
        leftTextSize =
            typedArray.getDimension(R.styleable.SettingItemView_leftTextSize, 14.0F).toInt()

        isOpen = typedArray.getBoolean(R.styleable.SettingItemView_switch_, true)
        backgroundCorner =
            typedArray.getInt(R.styleable.SettingItemView_backgroundCorner, 3)
        typedArray.recycle()


        if (leftText?.isNotEmpty() == true) {
            setLeftItem(leftText!!)
        }
        if (leftDescribeText?.isNotEmpty() == true) {
            setLeftDescribeItem(leftDescribeText!!)
        }

        setLeftItemColor(leftTextColor)
        setLeftItemTextSize(leftTextSize)
        switchIsChecked(isOpen)
        isVisible()
    }

    private fun isVisible() {
        when (leftDescribeVisible) {
            0 -> {
                setLeftDescribeVisibility(VISIBLE)
            }
            1 -> {
                setLeftDescribeVisibility(INVISIBLE)
            }
            2 -> {
                setLeftDescribeVisibility(GONE)
            }
        }
        when (backgroundCorner) {
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

    fun setLeftItem(leftText: String) {
        tvLeftItem.text = leftText
    }

    fun setLeftItemColor(leftTextColor: Int) {
        tvLeftItem.setTextColor(leftTextColor)
    }

    fun setLeftItemTextSize(leftTextSize: Int) {
        tvLeftItem.textSize = leftTextSize.toFloat()
    }

    fun switchIsChecked(): Boolean {
        return scSwitch.isChecked
    }

    fun switchIsChecked(isOpen: Boolean) {
        scSwitch.isChecked = isOpen
    }

    fun switchIsEnable(isEnable: Boolean) {
        if (isEnable) {
            scSwitch.alpha = 1f
        } else {
            scSwitch.alpha = 0.5f
        }
        scSwitch.isEnabled = isEnable
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) {
        scSwitch.setOnCheckedChangeListener(listener)
    }

    fun setLeftDescribeItem(leftText: String) {
        tvLeftDescribeItem.text = leftText
    }

    fun setLeftDescribeVisibility(visibility: Int) {
        tvLeftDescribeItem.visibility = visibility
    }

}