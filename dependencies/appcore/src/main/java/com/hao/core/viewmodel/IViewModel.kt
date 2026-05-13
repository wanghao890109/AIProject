package com.hao.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by wanghao 2022/8/1
 */

open class IViewModel : ViewModel(), IUIViewModelActionEvent {

    override val showLoadingLD = MutableLiveData<ShowLoadingEvent>()

    override val dismissLoadingLD = MutableLiveData<DismissLoadingEvent>()

    override val showToastEventLD = MutableLiveData<ShowToastEvent>()

    override val finishViewEventLD = MutableLiveData<FinishViewEvent>()
}