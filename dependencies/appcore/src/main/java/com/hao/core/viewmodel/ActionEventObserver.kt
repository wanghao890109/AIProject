package com.hao.core.viewmodel

import androidx.lifecycle.*

/**
 * Created by wanghao 2022/8/1
 */

interface IActionEvent

interface IUIActionEvent : IActionEvent {

    fun showLoading(msg: String)

    fun showLoading() {
        showLoading("")
    }

    fun dismissLoading()

    fun showToast(msg: String)

    fun finishView()

}

interface IUIViewModelActionEvent : IUIActionEvent {

    val showLoadingLD: MutableLiveData<ShowLoadingEvent>

    val dismissLoadingLD: MutableLiveData<DismissLoadingEvent>

    val showToastEventLD: MutableLiveData<ShowToastEvent>

    val finishViewEventLD: MutableLiveData<FinishViewEvent>

    override fun showLoading(msg: String) {
        showLoadingLD.postValue(ShowLoadingEvent(msg))
    }

    override fun dismissLoading() {
        dismissLoadingLD.postValue(DismissLoadingEvent)
    }

    override fun showToast(msg: String) {
        showToastEventLD.postValue(ShowToastEvent(msg))
    }

    override fun finishView() {
        finishViewEventLD.postValue(FinishViewEvent())
    }
}

interface IActionEventObserver : IActionEvent {

    val actionLifecycle: LifecycleOwner

    fun <VM> getActionModel(
        clazz: Class<VM>,
        factory: ViewModelProvider.Factory? = null,
        initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): Lazy<VM> where VM : ViewModel {
        return lazy {
            getActionModelFast(clazz, factory, initializer)
        }
    }

    fun <VM> getActionModelFast(
        clazz: Class<VM>,
        factory: ViewModelProvider.Factory? = null,
        initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): VM where VM : ViewModel {
        val localValue = actionLifecycle
        return when (localValue) {
            is ViewModelStoreOwner -> {
                if (factory == null) {
                    ViewModelProvider(localValue).get(clazz)
                } else {
                    ViewModelProvider(localValue, factory).get(clazz)
                }
            }
            else -> {
                factory?.create(clazz) ?: clazz.newInstance()
            }
        }.apply {
            initializer?.invoke(this, actionLifecycle)
        }
    }
}

interface IUIEventObserver : IUIActionEvent {

    val uiLifecycleOwner: LifecycleOwner

    fun <VM> getViewModel(
        clazz: Class<VM>,
        factory: ViewModelProvider.Factory? = null,
        initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): Lazy<VM> where VM : ViewModel, VM : IUIViewModelActionEvent {
        return lazy {
            getViewModelFast(clazz, factory, initializer)
        }
    }

    fun <VM> getViewModelFast(
        clazz: Class<VM>,
        factory: ViewModelProvider.Factory? = null,
        initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): VM where VM : ViewModel, VM : IUIViewModelActionEvent {
        val localValue = uiLifecycleOwner
        return when (localValue) {
            is ViewModelStoreOwner -> {
                if (factory == null) {

                    ViewModelProvider(localValue).get(clazz)
                } else {
                    ViewModelProvider(localValue, factory).get(clazz)
                }
            }
            else -> {
                factory?.create(clazz) ?: clazz.newInstance()
            }
        }.apply {
            generateActionEvent(this)
            initializer?.invoke(this, uiLifecycleOwner)
        }
    }

    fun <VM> generateActionEvent(viewModel: VM) where VM : ViewModel, VM : IUIViewModelActionEvent {
        viewModel.showLoadingLD.observe(uiLifecycleOwner, Observer {
            this@IUIEventObserver.showLoading(it.message)
        })
        viewModel.dismissLoadingLD.observe(uiLifecycleOwner, Observer {
            this@IUIEventObserver.dismissLoading()
        })
        viewModel.showToastEventLD.observe(uiLifecycleOwner, Observer {
            if (it.message.isNotBlank()) {
                this@IUIEventObserver.showToast(it.message)
            }
        })
        viewModel.finishViewEventLD.observe(uiLifecycleOwner, Observer {
            this@IUIEventObserver.finishView()
        })
    }
}