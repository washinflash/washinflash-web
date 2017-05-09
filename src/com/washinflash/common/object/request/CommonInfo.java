package com.washinflash.common.object.request;

/**
 * Created by esardat on 6/7/2016.
 */
public class CommonInfo {

    private String deviceUniqueIdentifier;
    private String appVersion;


    public String getDeviceUniqueIdentifier() {
        return deviceUniqueIdentifier;
    }

    public void setDeviceUniqueIdentifier(String deviceUniqueIdentifier) {
        this.deviceUniqueIdentifier = deviceUniqueIdentifier;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
