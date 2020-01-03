package com.mit.community.hardware;

import com.sun.jna.Pointer;

/**
 * @Author qishengjun
 * @Date Created in 14:10 2019/11/28
 * @Company: mitesofor </p>
 * @Description:~
 */
public class RemoteConfigCallback implements HCNetSDK.FRemoteConfigCallback {

    @Override
    public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {

    }
}
