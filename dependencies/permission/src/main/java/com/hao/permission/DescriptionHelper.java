package com.hao.permission;

import android.content.Context;
import android.text.TextUtils;

import com.hjq.permissions.permission.base.IPermission;
import com.hjq.permissions.tools.PermissionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DescriptionHelper {

    private List<String> descriptionList = null;
    private long descSetTime = 0L;

    /**
     * 实时描述文案
     * key：权限名
     * value：自定义文案
     *
     * @return
     */
    public Map<String, String> getDescription(Context context, IPermission... permission) {
        if (descriptionList == null || descriptionList.isEmpty()) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < permission.length; i++) {
            if (i < descriptionList.size()) {
                IPermission iPermission = permission[i];
                String customDesc = descriptionList.get(i);
                if (iPermission != null && !TextUtils.isEmpty(customDesc)) {
                    map.put(iPermission.getPermissionName(), customDesc);
                    List<IPermission> oldPermissionList = iPermission.getOldPermissions(context);
                    if (oldPermissionList != null) {
                        for (IPermission oldPermission : oldPermissionList) {
                            map.put(oldPermission.getPermissionName(), customDesc);
                        }
                    }
                }
            }
        }
        //有效期只有1s，防止业务方设置文案后，一直不申请权限，切到其它场景下申请其它权限时会导致权限和文案不匹配
        if (System.currentTimeMillis() - descSetTime < 1000) {
            return map;
        }
        descriptionList = null;
        return null;
    }

    /**
     * 临时存储描述内容
     *
     * @param desc
     */
    public void setDescriptionList(String... desc) {
        descriptionList = PermissionUtils.asArrayList(desc);
        descSetTime = System.currentTimeMillis();
    }

}
