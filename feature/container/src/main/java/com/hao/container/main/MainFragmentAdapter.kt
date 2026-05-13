package com.hao.container.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Created by wanghao 2022/7/27
 */
class MainFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentPath = arrayOf("/container/gamelist", "/container/roomlist", "/message/conversation", "/mine/account")

    override fun getItem(position: Int): Fragment {
        val fragment = ARouter.getInstance().build(fragmentPath[position]).navigation() ?: return Fragment()
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return fragmentPath.size
    }
}


