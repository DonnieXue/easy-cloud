package com.easy.cloud.core.authority.conditional;

import org.springframework.stereotype.Component;

/**
 * @author 无心
 */
@Component
public class EcAuthorityConditional {


    /**
     *  校验用户是否存在
     * @return
     */
    public boolean isExitsWithUser(String userAccount){
        return true;
    }
    /**
     *  校验用户token有效性
     * @return
     */
    public boolean isEffectiveWithToken(String token){
        return true;
    }

    /**
     *  校验code有效性
     * @return
     */
    public boolean isEffectiveWithCode(String code){
        return true;
    }


    /**
     * 校验系统有效性
     * @return
     */
    public boolean isEffectiveWithSystem(String systemId){
        return true;
    }
}
