package com.hao.permission;


/**
 * 权限申请的回调接口
 */
public interface PermissionCallback {
    /**
     * @param granted ture已有权限，false无权限
     */
    void onResult(Boolean granted);
}
