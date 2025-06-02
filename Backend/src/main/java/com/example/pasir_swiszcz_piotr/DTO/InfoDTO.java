package com.example.pasir_swiszcz_piotr.DTO;

public class InfoDTO {
    String appName;
    String version;
    String message;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InfoDTO(String appName, String version, String message) {
        this.appName = appName;
        this.version = version;
        this.message = message;
    }
}
