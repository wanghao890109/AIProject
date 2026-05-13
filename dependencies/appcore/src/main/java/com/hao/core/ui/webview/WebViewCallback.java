package com.hao.core.ui.webview;

public interface WebViewCallback<T> {
	
	void onCallBack(T data);

	void onError(int code, String error);
}
