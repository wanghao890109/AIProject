package com.hao.core.viewmodel

/**
 * Created by wanghao 2022/8/1
 */

open class BaseActionEvent

class ShowLoadingEvent(val message: String) : BaseActionEvent()

object DismissLoadingEvent : BaseActionEvent()

class FinishViewEvent() : BaseActionEvent()

class ShowToastEvent(val message: String) : BaseActionEvent()