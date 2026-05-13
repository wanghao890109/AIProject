package com.hao.permission;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;
import com.hjq.permissions.tools.PermissionApi;
import com.hjq.permissions.tools.PermissionUtils;

import java.util.List;
import java.util.Map;

/**
 * 基于XXPermissions:28.0
 */
public class XMPermissions {
    private static Application application;
    private static DescriptionHelper descriptionHelper = new DescriptionHelper();

    /**
     * 初始化
     * @param application
     */
    public static void init(Application application) {
        XMPermissions.application = application;
        ActivityHelper.init(application);
    }

    /**
     * 申请权限
     * @param callback
     * @param permission
     */
    public static void requestPermissions(PermissionCallback callback, @NonNull IPermission... permission) {
        Activity activity = ActivityHelper.getCurrentActivity();
        if (activity == null) {
            return;
        }
        Map<String, String> customDesc = descriptionHelper.getDescription(activity, permission);
        XXPermissions.with(activity)
                .permissions(permission)
                .interceptor(new PermissionInterceptor())
                .description(new PermissionDescription(customDesc))
                .request(new OnPermissionCallback() {
                    @Override
                    public void onResult(@NonNull List<IPermission> grantedList, @NonNull List<IPermission> deniedList) {
                        boolean allGranted = deniedList.isEmpty();
                        if (callback != null) {
                            callback.onResult(allGranted);
                        }
                    }
                });
    }

    /**
     * 判断权限是否已有
     * @param permissions
     * @return
     */
    public static boolean isGranted(String... permissions) {
        if (application == null) {
            return false;
        }
        List<String> list = PermissionUtils.asArrayList(permissions);
        for (String permission : list) {
            if (ContextCompat.checkSelfPermission(application, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断权限是否已有<br>
     * 尽量使用{@link com.xm.permission.XMPermissions#isGranted(IPermission... permission1)}，内部做了权限适配
     * @param permission
     * @return
     */
    public static boolean isGranted(IPermission... permission) {
        if (application == null) {
            return false;
        }
        List<IPermission> list = PermissionUtils.asArrayList(permission);
        return PermissionApi.isGrantedPermissions(application, list);
    }

    /**
     * 自定义描述文案（如果不想用默认通用文案的话）<br>
     * 必须在request前使用（有效期1秒钟）<br>
     * 默认描述文案在这个类里{@link PermissionConverter}
     *
     * @param desc  request申请几个权限，就填几个desc参数，如果不填or少填就会使用兜底文案填充
     */
    public static void setDescription(String... desc) {
        descriptionHelper.setDescriptionList(desc);
    }

    /**
     * 获取存储权限（如果只是访问媒体文件，请使用 {@link com.xm.permission.XMPermissions#requestMedias}）
     * @param callback
     */
    public static void requestStorage(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(callback,
                    PermissionLists.getReadMediaImagesPermission(),
                    PermissionLists.getReadMediaAudioPermission(),
                    PermissionLists.getReadMediaVideoPermission(),
                    PermissionLists.getWriteExternalStoragePermission());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalStorage(callback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(callback,
                    PermissionLists.getReadMediaImagesPermission(),
                    PermissionLists.getReadMediaAudioPermission(),
                    PermissionLists.getReadMediaVideoPermission(),
                    PermissionLists.getWriteExternalStoragePermission());
        } else {
            requestPermissions(callback,
                    PermissionLists.getReadExternalStoragePermission(),
                    PermissionLists.getWriteExternalStoragePermission());
        }
    }
    /**
     * 打开录音权限
     * @param callback
     */
    public static void requestRecordAudio(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getRecordAudioPermission());
    }

    /**
     * 打开相机和录音权限
     * @param callback
     */
    public static void requestCameraAudio(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getCameraPermission(),
                PermissionLists.getRecordAudioPermission());
    }

    /**
     * 打开相机
     * @param callback
     */
    public static void requestCamera(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getCameraPermission());
    }

    /**
     * 打开多媒体（图片、音频、视频）
     * @param callback
     */
    public static void requestMedias(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadMediaImagesPermission(),
                PermissionLists.getReadMediaAudioPermission(),
                PermissionLists.getReadMediaVideoPermission());
    }

    /**
     * 打开多媒体（图片）
     * @param callback
     */
    public static void requestMediaImages(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadMediaImagesPermission());
    }

    /**
     * 打开多媒体（音频）
     * @param callback
     */
    public static void requestMediaAudio(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadMediaAudioPermission());
    }

    /**
     * 打开多媒体（视频）
     * @param callback
     */
    public static void requestMediaVideo(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadMediaVideoPermission());
    }

    /**
     * 读取手机号码权限
     * @param callback
     */
    public static void requestPhoneNumbers(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadPhoneNumbersPermission());
    }

    /**
     * 读取电话状态权限
     * @param callback
     */
    public static void requestPhoneState(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getReadPhoneStatePermission());
    }

    /**
     * 所有文件访问权限（特殊权限，Android 11 新增的权限）
     * @param callback
     */
    public static void requestManageExternalStorage(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getManageExternalStoragePermission());
    }

    /**
     * 安装应用权限（特殊权限，Android 8.0 新增的权限）
     * @param callback
     */
    public static void requestInstallPackages(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getRequestInstallPackagesPermission());
    }

    /**
     * 悬浮窗权限
     * @param callback
     */
    public static void requestSystemAlertWindow(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getSystemAlertWindowPermission());
    }

    /**
     * 通知栏监听权限
     * @param callback
     */
    public static void requestNotificationListenerService(PermissionCallback callback, @NonNull Class<? extends NotificationListenerService> notificationListenerServiceClass) {
        requestPermissions(callback,
                PermissionLists.getBindNotificationListenerServicePermission(notificationListenerServiceClass));
    }

    /**
     * 通知栏权限
     * @param callback
     */
    public static void requestPostNotifications(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getPostNotificationsPermission());
    }

    /**
     * 获取定位权限
     * @param callback
     */
    public static void requestLocation(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getAccessCoarseLocationPermission(),
                PermissionLists.getAccessFineLocationPermission());
    }

    /**
     * 视频通话权限
     * @param callback
     */
    public static void requestVideoCall(PermissionCallback callback) {
        requestPermissions(callback,
                PermissionLists.getCameraPermission(),
                PermissionLists.getRecordAudioPermission(),
                PermissionLists.getReadPhoneStatePermission());
    }


    /**
     * 打开权限设置页
     */
    public static void startPermissionActivity(@NonNull Context context, @NonNull IPermission... permissions) {
        XXPermissions.startPermissionActivity(context, permissions);
    }
}
