package com.ylfcf.ppp.Permission;

/**
 * 权限请求结果的封装
 * Created by Administrator on 2017/5/10.
 */

public interface PermissionCallBackM {
    void onPermissionGrantedM(int requestCode, String... perms);
    void onPermissionDeniedM(int requestCode, String... perms);
}
