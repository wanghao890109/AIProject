package com.hao.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.hjq.permissions.permission.base.IPermission;
import com.hjq.permissions.tools.PermissionUtils;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * 权限辅助类
 */
class XMPermissionsHelper {

    public static void setPermissionCancel(Activity activity, List<IPermission> cancel) {
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView != null) {
            contentView.setTag(R.id.ids_permission_cancel, cancel);
        }
    }

    public static List<IPermission> getPermissionCancel(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView != null) {
            Object tag = contentView.getTag(R.id.ids_permission_cancel);
            if (tag != null && tag instanceof List<?>) {
                return (List<IPermission>) tag;
            }
        }
        return null;
    }

    public static boolean equalsPermission(@NonNull IPermission permission1, @NonNull IPermission permission2) {
        return PermissionUtils.equalsPermission(permission1, permission2);
    }

    public static boolean equalsPermission(@NonNull List<IPermission> list1, @NonNull List<IPermission> list2) {
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        int equalsSize = 0;
        for (IPermission item1 : list1) {
            for (IPermission item2 : list2) {
                if (equalsPermission(item1, item2)) {
                    equalsSize ++;
                }
            }
        }
        if (equalsSize == list1.size()) {
            return true;
        }
        return false;
    }


}
