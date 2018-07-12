package com.easy.cloud.core.authority.filter.login;

import com.easy.cloud.core.common.log.utils.EcLogUtils;
import com.easy.cloud.core.operator.sysuser.service.SysUserService;
import org.crazycake.shiro.AuthCachePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author daiqi
 * @create 2018-06-27 9:04
 */
@Component
public class EcLoginEmailPasswordFilter extends EcBaseLoginFilter {
    @Autowired
    protected SysUserService sysUserService;

    @Override
    protected AuthCachePrincipal getAuthCachePrincipal(String loginName) {
        return null;
    }
}
