package com.hao.mine

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.hao.core.base.CoreBaseFragment
import com.hao.service.account.AccountService
import com.hao.service.account.IAccountService
import com.hao.service.account.SampleAccountListener
import com.hao.service.env.EnvironmentService
import com.hao.common.utils.LogUtil
import com.hao.common.utils.NetWorkUtil
import com.hao.common.utils.ToastUtils
import com.hao.mine.databinding.MineFragmentBinding
import com.hao.mine.viewmodel.MineViewModel
import com.hao.proto.GenderType
import com.hao.proto.UserBase
import com.hao.proto.UserInfo
import com.hao.proto.UserPage
import com.hao.proto.WelthInfo
import com.hao.proto.WelthLevel


@Route(path = "/mine/account")
class MineFragment : CoreBaseFragment<MineFragmentBinding>() {

    private val viewModel by getViewModel(MineViewModel::class.java) {
        minePage.observe(it) {
            updateUI(it)
            binding?.refreshLayout?.finishRefresh()
            binding?.refreshLayout?.finishLoadMore()
        }
    }

    private var accountListener = object : SampleAccountListener() {
        override fun onUpdated(sender: IAccountService?) {
            sender?.getAccountInfo(UserInfo::class.java)?.let {
                updateUI(it.userBase)
                it.userWelth?.let {
                    updateUI(it)
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): MineFragmentBinding {
        return MineFragmentBinding.inflate(layoutInflater)
    }

    override fun initView() {

//        binding?.llBalanceInfo?.tvDiamondNumber?.typeface = TypeFaceUtil.typeFace()
//        binding?.llBalanceInfo?.tvGoldNumber?.typeface = TypeFaceUtil.typeFace()

        AccountService.getInstance().getAccountInfo(UserInfo::class.java)?.userBase?.let {
            updateUI(it)
        }
    }

    override fun initListener() {

        AccountService.getInstance().register(accountListener)

        binding?.llUserInfo?.ivUserIcon?.setOnClickListener {
            ARouter.getInstance().build("/user/edit").navigation()
        }
        binding?.llUserInfo?.llIdLayout?.setOnClickListener {
            val cm = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", "${viewModel.minePage?.value?.user?.acctId}")
            cm.setPrimaryClip(mClipData)
            ToastUtils.showShort(R.string.id_has_copied)
        }
        binding?.llUserInfo?.llUserInfoLayout?.setOnClickListener {
            ARouter.getInstance().build("/user/homepage")
                .withSerializable("userBase", AccountService.getInstance().getAccountInfo(UserBase::class.java))
                .navigation()
        }

        binding?.llMenuLayout?.sivAccessories?.setOnClickListener {

        }
        binding?.llMenuLayout?.sivDecorations?.setOnClickListener {

        }
        binding?.llMenuLayout?.sivHistorys?.setOnClickListener {

        }
        binding?.llMenuLayout?.sivSettings?.setOnClickListener {
            ARouter.getInstance().build("/page/setting").navigation()
        }
        binding?.llMenuLayout?.sivInvite?.setOnClickListener {
            ARouter.getInstance().build("/page/inviteCode").navigation()
        }

        binding?.refreshLayout?.setEnableLoadMore(false)
        binding?.refreshLayout?.setOnRefreshListener {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    /**
     * 刷新数据
     */
    private fun initData() {
        if (NetWorkUtil.isNetConnected()) {
            LogUtil.i("MineFragment getUserMyPage。。。。。")
            viewModel.getUserPage()
        } else {
            ToastUtils.showShort(com.hao.ui.R.string.bad_net_work)
            binding?.refreshLayout?.finishRefresh()
            binding?.refreshLayout?.finishLoadMore()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        AccountService.getInstance().unregister(accountListener)
    }

    private fun updateUI(userPage: UserPage?) {
        userPage ?: return
        updateUI(userPage?.user ?: return)
        updateUI(userPage?.uwelth ?: return)

    }

    private fun updateUI(userBase: UserBase) {
        updateUI(userBase?.level ?: return)
        if (EnvironmentService.getInstance().isDebug) {
            binding?.llUserInfo?.tvId?.text = "${userBase?.acctId} (${userBase?.userId})"
        } else {
            binding?.llUserInfo?.tvId?.text = "${userBase?.acctId}"
        }

        binding?.llUserInfo?.ivUserIcon?.load(userBase?.avatar)
        binding?.llUserInfo?.tvUserNickname?.text = userBase?.nickName
        binding?.llUserInfo?.tvUserNickname?.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, if (userBase?.gender == GenderType.GT_MALE.value) {
                com.hao.ui.R.mipmap.ic_sexy_boy_42x42
            } else if (userBase?.gender == GenderType.GT_FEMALE.value) {
                com.hao.ui.R.mipmap.ic_sexy_girl_42x42
            } else if (userBase?.gender == GenderType.GT_UNKNOW.value) {
                com.hao.ui.R.mipmap.ic_sexy_nonbinary_42x42
            } else {
                0
            }, 0
        )
    }

    private fun updateUI(level: WelthLevel) {
        binding?.llUserInfo?.tvLevel?.text = "Lv${level.LvId}"
        binding?.llUserInfo?.pbLevelProgress?.max = level.GapExp
        binding?.llUserInfo?.pbLevelProgress?.progress = level.LvExp
        binding?.llUserInfo?.tvLevelProgress?.text = "${level.LvExp}/${level.GapExp}"
    }

    private fun updateUI(uwelth: WelthInfo) {

    }
}