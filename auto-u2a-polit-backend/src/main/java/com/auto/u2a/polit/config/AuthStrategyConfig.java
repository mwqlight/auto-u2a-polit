package com.auto.u2a.polit.config;

import com.auto.u2a.polit.security.*;
import com.auto.u2a.polit.enums.AuthProtocol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 认证策略配置类
 * 注册所有支持的认证策略
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthStrategyConfig {
    
    private final PasswordAuthStrategy passwordAuthStrategy;
    private final OAuth2AuthStrategy oauth2AuthStrategy;
    private final SMSAuthStrategy smsAuthStrategy;
    
    @Bean
    public AuthContext authContext() {
        AuthContext authContext = new AuthContext();
        
        // 注册所有认证策略
        authContext.registerStrategy(AuthProtocol.PASSWORD, passwordAuthStrategy);
        authContext.registerStrategy(AuthProtocol.OAUTH2, oauth2AuthStrategy);
        authContext.registerStrategy(AuthProtocol.SMS, smsAuthStrategy);
        
        // 可以继续注册其他认证策略
        // authContext.registerStrategy(AuthProtocol.OIDC, oidcAuthStrategy);
        // authContext.registerStrategy(AuthProtocol.SAML2, saml2AuthStrategy);
        // authContext.registerStrategy(AuthProtocol.WECHAT, wechatAuthStrategy);
        // authContext.registerStrategy(AuthProtocol.DINGTALK, dingtalkAuthStrategy);
        // authContext.registerStrategy(AuthProtocol.FEISHU, feishuAuthStrategy);
        // authContext.registerStrategy(AuthProtocol.EMAIL, emailAuthStrategy);
        
        log.info("认证策略配置完成，已注册 {} 种认证协议", authContext.getSupportedProtocols().length);
        
        return authContext;
    }
}