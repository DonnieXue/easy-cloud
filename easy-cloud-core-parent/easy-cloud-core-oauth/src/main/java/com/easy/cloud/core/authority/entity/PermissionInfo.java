package com.easy.cloud.core.authority.entity;

/**
 * @author 无心
 */
public class PermissionInfo {

    private String  userAccount;
    private String  systemId;
    private String  systemName;
    private String  systemPermission;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(String systemPermission) {
        this.systemPermission = systemPermission;
    }
}
