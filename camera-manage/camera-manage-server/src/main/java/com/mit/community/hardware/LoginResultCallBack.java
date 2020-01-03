package com.mit.community.hardware;

import com.sun.jna.Pointer;

/**
 * @Author qishengjun
 * @Date Created in 15:04 2019/11/23
 * @Company: mitesofor </p>
 * @Description:~
 */
public class LoginResultCallBack implements HCNetSDK.FLoginResultCallBack {
    private LoginResultCallBack() {}

    private static class LoginResultCallBackHolder {
        private static final LoginResultCallBack realDataCB = new LoginResultCallBack();
    }

    public static final LoginResultCallBack getInstance() {
        return LoginResultCallBack.LoginResultCallBackHolder.realDataCB;
    }
    @Override
    public int invoke(int lUserID, int dwResult, HCNetSDK.NET_DVR_DEVICEINFO_V30 lpDeviceinfo, Pointer pUser) {
        return 0;
    }
}
