package com.hao.core.viewmodel

import android.util.Log
import com.hao.core.env.ApiException
import com.hao.core.net.ApiObserver
import com.hao.core.net.BaseObserver
import com.hao.core.thrower.ExceptionThrower
import com.hao.common.utils.LogUtil
import com.hao.ui.R
import com.hao.service.env.EnvironmentService
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

/**
 * Created by wanghao 2022/8/1
 */
open class BaseViewModel : IViewModel(),
    IUIViewModelActionEvent {
    protected var compositeDisposable = CompositeDisposable()
    private val thrower = ExceptionThrower()

    protected fun <T> subscribe(callback: ApiObserver<T>): ObservableTransformer<T, T> {
        return subscribe(callback as BaseObserver<T, T>)
    }

    private fun <I, O> subscribe(callback: BaseObserver<I, O>): ObservableTransformer<I, O> {
        return ObservableTransformer<I, O>() {
            var observable = it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { t -> callback?.apply(t)!! }
            observable.subscribe(object : Observer<O> {
                override fun onComplete() {
                    callback?.onCompleted()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(o: O & Any) {
                    if (!compositeDisposable.isDisposed) {
                        callback?.onNext(o)
                    }
                }

                override fun onError(e: Throwable) {
                    var exception: ApiException = if (e is ApiException) {
                        e
                    } else if (e is UnknownHostException || !com.hao.common.utils.NetWorkUtil.isNetConnected()) {
                        val errorMessage = EnvironmentService.getInstance().context.resources.getString(R.string.bad_net_work);
                        ApiException(ApiException.CODE_NET_NOT_CONNECTED, errorMessage)
                    } else {
                        ApiException(ApiException.CODE_UNKNOWN, e?.message ?: "")
                    }
                    LogUtil.e("HTTP-> $exception  , ${callback?.toString()}")
                    val onThrow = thrower.onThrow(exception)
                    if (onThrow) {
                        if (!callback.interruptException) {
                            //如果业务层，仍想处理，则抛给业务层
                            callback?.onError(exception)
                        } else {
                            //统一处理后，业务层不再处理
                        }
                    } else {
                        callback?.onError(exception)
                    }
                }
            })
            observable
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

        Log.i("BaseViewModel", "onCleared")
    }

    fun clear() {
        onCleared()
    }
}