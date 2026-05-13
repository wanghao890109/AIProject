package com.hao.container.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.container.R
import com.hao.container.databinding.ActivityMainBinding
import com.hao.common.utils.AppLifecycleManager
import com.hao.ui.widget.bottomnavbar.BottomNavigationBar
import com.hao.ui.widget.bottomnavbar.BottomNavigationItem
import com.hao.core.base.CoreBaseActivity

/**
 * Created by wanghao 2022/7/27
 */
@Route(path = "/main/container")
class MainActivity : CoreBaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainFragmentAdapter by lazy {
        MainFragmentAdapter(supportFragmentManager)
    }

    override fun initView() {


        binding?.viewPager?.apply {
            adapter = mainFragmentAdapter
        }
        binding?.viewPager?.offscreenPageLimit = mainFragmentAdapter.count

        initNavigator()
    }

    override fun initListener() {

    }


    private fun initNavigator() {
        var homeTabItem = BottomNavigationItem(getDrawable(R.drawable.bottom_navigation_game_selector), getString(R.string.game_tab_text))
        binding?.bottomBar?.addItem(homeTabItem)
        val dynamicTabItem =
            BottomNavigationItem(getDrawable(R.drawable.bottom_navigation_room_selector), getString(R.string.room_tab_text))
        binding?.bottomBar?.addItem(dynamicTabItem)
        val messageTabItem =
            BottomNavigationItem(getDrawable(R.drawable.bottom_navigation_message_selector), getString(R.string.message_tab_text))
        binding?.bottomBar?.addItem(messageTabItem)
        val mineTabItem = BottomNavigationItem(getDrawable(R.drawable.bottom_navigation_mine_selector), getString(R.string.mine_tab_text))
        binding?.bottomBar?.addItem(mineTabItem)
        binding?.bottomBar?.setSelectedPosition(0)
        binding?.bottomBar?.setActiveColor(com.hao.ui.R.color.purple_font_color)
        binding?.bottomBar?.setInActiveColor(com.hao.ui.R.color.detail_font_color)
        binding?.bottomBar?.setupWithViewPager(binding?.viewPager!!)
        binding?.bottomBar?.addTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabSelected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabSelectedBefore(positon: Int): Boolean {
                return false
            }
        })
        binding?.bottomBar?.initialise()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        AppLifecycleManager.getInstance().clearAllActivities()
        super.onBackPressed()
    }
}

