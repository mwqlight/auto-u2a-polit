package com.auto.u2a.polit.enums;

/**
 * 认证协议枚举
 * 支持多种认证协议，实现全协议认证中心
 */
public enum AuthProtocol {
    
    /**
     * 用户名密码认证
     */
    PASSWORD("password", "用户名密码认证"),
    
    /**
     * OAuth 2.0 认证
     */
    OAUTH2("oauth2", "OAuth 2.0 认证"),
    
    /**
     * OIDC (OpenID Connect) 认证
     */
    OIDC("oidc", "OpenID Connect 认证"),
    
    /**
     * SAML 2.0 认证
     */
    SAML2("saml2", "SAML 2.0 认证"),
    
    /**
     * LDAP 认证
     */
    LDAP("ldap", "LDAP 认证"),
    
    /**
     * CAS 认证
     */
    CAS("cas", "CAS 认证"),
    
    /**
     * 微信认证
     */
    WECHAT("wechat", "微信认证"),
    
    /**
     * 钉钉认证
     */
    DINGTALK("dingtalk", "钉钉认证"),
    
    /**
     * 飞书认证
     */
    FEISHU("feishu", "飞书认证"),
    
    /**
     * 短信验证码认证
     */
    SMS("sms", "短信验证码认证"),
    
    /**
     * 邮箱验证码认证
     */
    EMAIL("email", "邮箱验证码认证"),
    
    /**
     * 生物特征认证
     */
    BIOMETRIC("biometric", "生物特征认证"),
    
    /**
     * 多因素认证
     */
    MFA("mfa", "多因素认证"),
    
    /**
     * 无密码认证
     */
    PASSWORDLESS("passwordless", "无密码认证"),
    
    /**
     * 社交登录认证
     */
    SOCIAL("social", "社交登录认证");
    
    private final String code;
    private final String description;
    
    AuthProtocol(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据code获取枚举
     */
    public static AuthProtocol fromCode(String code) {
        for (AuthProtocol protocol : values()) {
            if (protocol.getCode().equals(code)) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("未知的认证协议: " + code);
    }
    
    /**
     * 检查是否为社交登录协议
     */
    public boolean isSocialProtocol() {
        return this == WECHAT || this == DINGTALK || this == FEISHU || this == SOCIAL;
    }
    
    /**
     * 检查是否为标准协议
     */
    public boolean isStandardProtocol() {
        return this == PASSWORD || this == OAUTH2 || this == OIDC || this == SAML2 || this == LDAP || this == CAS;
    }
    
    /**
     * 检查是否为现代协议
     */
    public boolean isModernProtocol() {
        return this == SMS || this == EMAIL || this == BIOMETRIC || this == MFA || this == PASSWORDLESS;
    }
}