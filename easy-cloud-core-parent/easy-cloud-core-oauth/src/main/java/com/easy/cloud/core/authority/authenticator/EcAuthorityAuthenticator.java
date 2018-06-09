package com.easy.cloud.core.authority.authenticator;

import com.easy.cloud.core.authority.common.APIResponse;
import com.easy.cloud.core.authority.conditional.EcAuthorityConditional;
import com.easy.cloud.core.authority.constant.EcAuthorityConstant;
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
 * 用户认证
 * 用户认证 工作属性 确定用户是谁 拥有什么权限
 * @author 无心
 */

@RestController
@RequestMapping("authenticate")
public class EcAuthorityAuthenticator {

    private final static Logger logger = LoggerFactory.getLogger(EcAuthorityAuthenticator.class);
    @Autowired
    EcAuthorityConditional ecAuthorityConditional;
    @Autowired
    RedisManager redisManager;
    /**
     * 第三方请求权限 返回请求令牌
     * @return
     */
    @RequestMapping(value = "user",method = RequestMethod.POST)
    public APIResponse authenticate(@RequestParam(value = "user_account",required = true) String userAccount,
                                    @RequestParam(value = "system_id",required = true) String systemId,
                                    @RequestParam(value = "system_name",required = true) String systemName,
                                    @RequestParam(value = "system_need_permission",required = true) String systemPermission){

        APIResponse apiResponse = APIResponse.build();

        apiResponse.code(200);
        apiResponse.message(EcAuthorityConstant.SUCCESS);
        // 校验用户是否存在 落地请求数据
        if(!ecAuthorityConditional.isEffectiveWithSystem(systemId)){
            apiResponse.code(400);
            apiResponse.message(EcAuthorityConstant.INVALID);
        }
        if(!ecAuthorityConditional.isExitsWithUser(userAccount)){
            apiResponse.code(400);
            apiResponse.message(EcAuthorityConstant.NO_EXITS);
        }
        //  返回授权调用code 一般使用一次 五分钟
        Long code = EcIdWorker.getFlowIdWorkerInstance().nextId();
        redisManager.set((systemId+userAccount).getBytes(),code.toString().getBytes(),300);
        apiResponse.data(code);

        return apiResponse;
    }


}
