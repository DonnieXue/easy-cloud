package com.easy.cloud.core.authority.authenticator;

import com.easy.cloud.core.authority.common.APIResponse;
import com.easy.cloud.core.authority.conditional.EcAuthorityConditional;
import com.easy.cloud.core.authority.constant.EcAuthorityConstant;
import com.easy.cloud.core.authority.entity.PermissionInfo;
import com.easy.cloud.core.authority.utils.EcIdWorker;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 用户授权
 * 用户认证后 经由用户授权后 分配访问权限
 * @author 无心
 */

@RestController
@RequestMapping("authenticate")
public class EcAuthorityAuthorization {


    private final static Logger logger = LoggerFactory.getLogger(EcAuthorityAuthorization.class);
    @Autowired
    EcAuthorityConditional ecAuthorityConditional;
    @Autowired
    RedisManager redisManager;
    /**
     *
     * 校验用户凭证 本系统分发出去用户确定用户有效唯一凭证 可以是登陆token
     *
     */
    @RequestMapping(value = "user",method = RequestMethod.POST)
    public APIResponse authenticate(@RequestParam(value = "user_account",required = true) String userAccount,
                                    @RequestParam(value = "user_proof",required = true) String userProof,
                                    @RequestParam(value = "system_id",required = true) String systemId,
                                    @RequestParam(value = "system_name",required = true) String systemName,
                                    @RequestParam(value = "system_need_permission",required = true) String systemPermission,
                                    @RequestParam(value = "code" ,defaultValue = "",required = true) String code){
        APIResponse apiResponse = APIResponse.build();

        /**
         * 第零 确定system是否有效
         * 第一确定code有效
         * 第二验证用户token
         * 第三分配数据访问权限 保存redis mq落地db
         * 第四 返回 授权令牌
         *
         * 之后第三方系统 携带 系统id 用户账号 以及想访问的资源 像我系统请求数据
         */

        if(!ecAuthorityConditional.isEffectiveWithSystem(systemId)){
            apiResponse.code(400);
            apiResponse.message(EcAuthorityConstant.INVALID);
        }
        if(!ecAuthorityConditional.isExitsWithUser(userAccount)){
            apiResponse.code(400);
            apiResponse.message(EcAuthorityConstant.NO_EXITS);
        }

        String codeCached = redisManager.get((systemId+userAccount).getBytes()).toString();
        if(codeCached==null || !codeCached.equals(code)){
            apiResponse.code(400);
            apiResponse.message(EcAuthorityConstant.INVALID);
        }

        //分配数据访问权限 保存redis mq落地db
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.setUserAccount(userAccount);
        permissionInfo.setSystemId(systemId);
        permissionInfo.setSystemName(systemName);
        permissionInfo.setSystemPermission(systemPermission);

        // 保存永久授权或者七天授权 或者 用户自定义
        Long key = EcIdWorker.getFlowIdWorkerInstance().nextId();
        redisManager.set(key.toString().getBytes(),permissionInfo.toString().getBytes(),0);

        apiResponse.code(200);
        apiResponse.message(EcAuthorityConstant.SUCCESS);
        apiResponse.data(key);
        return apiResponse;
    }


}
