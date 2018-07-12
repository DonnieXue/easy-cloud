package com.easy.cloud.core.reptile.dynamicbean.service.impl;

import com.easy.cloud.core.basic.constant.error.EcBaseErrorCodeEnum;
import com.easy.cloud.core.basic.pojo.dto.EcBaseServiceResult;
import com.easy.cloud.core.basic.service.EcBaseService;
import com.easy.cloud.core.basic.utils.EcAssert;
import com.easy.cloud.core.basic.utils.EcBaseUtils;
import com.easy.cloud.core.common.collections.utils.EcCollectionsUtils;
import com.easy.cloud.core.common.json.utils.EcJSONUtils;
import com.easy.cloud.core.common.log.utils.EcLogUtils;
import com.easy.cloud.core.exception.bo.EcBaseBusinessException;
import com.easy.cloud.core.reptile.common.config.EcReptileConfig;
import com.easy.cloud.core.reptile.common.pojo.dto.EcReptileDataDTO;
import com.easy.cloud.core.reptile.common.utils.EcReptileUtils;
import com.easy.cloud.core.reptile.datafield.pojo.dto.EcReptileDataFieldDTO;
import com.easy.cloud.core.reptile.datafield.service.EcReptileDataFieldService;
import com.easy.cloud.core.reptile.dynamicbean.dao.EcReptileDynamicBeanDAO;
import com.easy.cloud.core.reptile.dynamicbean.pojo.bo.EcReptileDynamicBeanBO;
import com.easy.cloud.core.reptile.dynamicbean.pojo.dto.EcReptileDynamicBeanDTO;
import com.easy.cloud.core.reptile.dynamicbean.pojo.entity.EcReptileDynamicBeanEntity;
import com.easy.cloud.core.reptile.dynamicbean.pojo.query.EcReptileDynamicBeanQuery;
import com.easy.cloud.core.reptile.dynamicbean.service.EcReptileDynamicBeanService;
import com.easy.cloud.core.reptile.engine.pojo.dto.EcReptileEngineBeanClassDTO;
import com.easy.cloud.core.reptile.engine.pojo.query.EcReptileEngineQuery;
import com.easy.cloud.core.reptile.engine.service.EcReptileEngineService;
import com.geccocrawler.gecco.GeccoEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 描述：服务实现类
 *
 * @author THINK
 * @date 2018-06-07 17:20:59
 */
@Service(value = "ecReptileDynamicBeanService")
public class EcReptileDynamicBeanServiceImpl extends EcBaseService implements EcReptileDynamicBeanService {
    /**
     * 数据处理接口
     */
    @Autowired
    private EcReptileDynamicBeanDAO reptileDynamicBeanDAO;
    @Autowired
    private EcReptileDataFieldService reptileDynamicBeanService;
    @Autowired
    private EcReptileEngineService reptileEngineService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public EcBaseServiceResult reptileData(EcReptileDataDTO reptileDataDTO) {
        EcReptileEngineBeanClassDTO reptileEngineBeanClassDTOFromCache = EcReptileConfig.getReptileEngineBeanClassDTO();
        EcAssert.verifyObjNull(reptileEngineBeanClassDTOFromCache, "缓存reptileEngineBeanClassDTOFromCache");
        EcReptileDynamicBeanDTO dynamicBeanDTO = reptileEngineBeanClassDTOFromCache.getReptileDynamicBeanDTO();
        // 获取爬虫引擎
        GeccoEngine geccoEngine = reptileEngineBeanClassDTOFromCache.getGeccoEngine();
        EcAssert.verifyDataNotExistent(geccoEngine, "缓存中geccoEngine");
        EcReptileUtils.intoScheduler(geccoEngine, dynamicBeanDTO, reptileDataDTO);
        return EcBaseServiceResult.newInstanceOfSuccess();
    }

    @Override
    public EcBaseServiceResult saveReptileDynamicBean(EcReptileDynamicBeanDTO dynamicBeanDTO) {
        // 1 创建爬虫动态bean逻辑对象
        EcReptileDynamicBeanBO dynamicBeanBO = new EcReptileDynamicBeanBO(dynamicBeanDTO);
        // 2 初始化
        dynamicBeanBO.initSaveReptileDynamicBeanData();
        // 3 数据校验
        dynamicBeanBO.verifySaveReptileDynamicBeanData();
        // 4 数据转换
        EcReptileDynamicBeanEntity dynamicBeanEntity = EcJSONUtils.parseObject(dynamicBeanBO.getReptileDynamicBeanDTO(),
                EcReptileDynamicBeanEntity.class);
        // 保存
        reptileDynamicBeanDAO.save(dynamicBeanEntity);
        // 返回结果
        return EcBaseServiceResult.newInstanceOfSucResult(dynamicBeanEntity);
    }

    @Override
    public EcBaseServiceResult updateReptileDynamicBean(EcReptileDynamicBeanDTO dynamicBeanDTO) {
        // 1 创建爬虫动态bean逻辑对象
        EcReptileDynamicBeanBO dynamicBeanBO = new EcReptileDynamicBeanBO(dynamicBeanDTO);
        // 2 初始化
        dynamicBeanBO.initUpdateReptileDynamicBeanData();
        // 3 数据校验
        dynamicBeanBO.verifyUpdateReptileDynamicBeanData();
        EcReptileDynamicBeanEntity reptileDynamicBeanEntity = reptileDynamicBeanDAO.findById(dynamicBeanDTO.getId());
        // 校验
        EcAssert.verifyDataNotExistent(reptileDynamicBeanEntity, "reptileDynamicBeanEntity");
        // 设置
        reptileDynamicBeanEntity.setMatchUrls(dynamicBeanDTO.getMatchUrls());
        // 更新
        reptileDynamicBeanDAO.update(reptileDynamicBeanEntity);
        // 返回结果
        return EcBaseServiceResult.newInstanceOfSucResult(reptileDynamicBeanEntity);
    }

    @Override
    public synchronized EcBaseServiceResult registerToGeccoEngine(EcReptileDynamicBeanQuery dynamicBeanQuery) {
        // 1 从数据库中获取dynamicBeanEntity列表
        List<EcReptileDynamicBeanEntity> dynamicBeanEntities = reptileDynamicBeanDAO.listByQuery(dynamicBeanQuery);
        if (EcCollectionsUtils.isEmpty(dynamicBeanEntities)) {
            throw new EcBaseBusinessException(EcBaseErrorCodeEnum.LIST_NULL, "爬虫动态bean");
        }
        // 2 根据爬虫动态bean持久化对象列表构建reptileDynamicBeanDTO列表
        List<EcReptileDynamicBeanDTO> dynamicBeanDTOs = buildDynamicBeanDTOs(dynamicBeanEntities);
        // 3 对dynamicBeanDTOs列表排序
        Collections.sort(dynamicBeanDTOs, new Comparator<EcReptileDynamicBeanDTO>() {
            @Override
            public int compare(EcReptileDynamicBeanDTO o1, EcReptileDynamicBeanDTO o2) {
                return o2.getDynamicBeanParentNo() - o1.getDynamicBeanParentNo();
            }
        });
        EcLogUtils.info("registerToGeccoEngine动态bean数据传输对象", dynamicBeanDTOs, logger);
        // 3 更新beanClass到内存
        EcReptileUtils.updateReptileEngineRule(dynamicBeanDTOs);
        // 4 加载爬虫引擎
        loadReptileEngine(dynamicBeanDTOs);
        return EcBaseServiceResult.newInstanceOfSuccess();
    }

    /**
     * <p>
     * 更新爬虫引擎的规则
     * </p>
     *
     * @return
     * @author daiqi
     * @date 2018/6/12 17:14
     */
    private synchronized void loadReptileEngine(List<EcReptileDynamicBeanDTO> dynamicBeanDTOs) {
        // 顶级父动态bean数据传输对象需要加载爬虫引擎
        EcReptileDynamicBeanDTO topParentDynamicBeanDTO = findTopParentDynamicBeanDTO(dynamicBeanDTOs);
        if (topParentDynamicBeanDTO != null) {
            // 获取爬虫引擎
            EcReptileEngineQuery reptileEngineQuery = new EcReptileEngineQuery();
            reptileEngineQuery.setReptileEngineNo(topParentDynamicBeanDTO.getReptileEngineNo());
            reptileEngineQuery.setReptileDynamicBeanDTO(topParentDynamicBeanDTO);
            // 加载爬虫引擎
            reptileEngineService.loadReptileEngineByCore(reptileEngineQuery);
        }
    }

    /**
     * <p>
     * 从爬虫动态bean列表中获取顶级父bean---即dynamicBeanParentNo最小的对象,列表数据为空将抛出异常
     * </p>
     *
     * @param dynamicBeanDTOs
     * @return com.easy.cloud.core.reptile.dynamicbean.pojo.dto.EcReptileDynamicBeanDTO
     * @author daiqi
     * @date 2018/6/13 10:50
     */
    private EcReptileDynamicBeanDTO findTopParentDynamicBeanDTO(List<EcReptileDynamicBeanDTO> dynamicBeanDTOs) {
        EcAssert.verifyListEmpty(dynamicBeanDTOs, "dynamicBeanDTOs");
        EcReptileDynamicBeanDTO topParentDynamicBeanDTO = dynamicBeanDTOs.get(0);
        for (EcReptileDynamicBeanDTO reptileDynamicBeanDTO : dynamicBeanDTOs) {
            if (reptileDynamicBeanDTO.getDynamicBeanParentNo() < topParentDynamicBeanDTO.getDynamicBeanParentNo()) {
                topParentDynamicBeanDTO = reptileDynamicBeanDTO;
            }
        }
        return topParentDynamicBeanDTO;
    }

    /**
     * <p>
     * 根据dynamicBeanEntities列表构建EcReptileDynamicBeanDTO列表
     * </p>
     *
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param dynamicBeanEntities
     * @return
     * @author daiqi
     * @创建时间 2018年6月7日 下午8:59:01
     */
    private List<EcReptileDynamicBeanDTO> buildDynamicBeanDTOs(List<EcReptileDynamicBeanEntity> dynamicBeanEntities) {
        // 1 entity转换为dto
        List<EcReptileDynamicBeanDTO> dynamicBeanDTOs = EcJSONUtils.parseArray(dynamicBeanEntities,
                EcReptileDynamicBeanDTO.class);
        // 2 构建dynamicBeanIds
        Set<Integer> dynamicBeanNos = new HashSet<>();
        for (EcReptileDynamicBeanEntity dynamicBean : dynamicBeanEntities) {
            dynamicBeanNos.add(dynamicBean.getDynamicBeanNo());
        }
        // 3 根据dynamicBeanIds获取数据属性数据传输对象列表
        List<EcReptileDataFieldDTO> dataFieldDTOs = reptileDynamicBeanService.findDataFieldsByDynamicBeanNos(dynamicBeanNos);
        EcLogUtils.info("buildDynamicBeanDTOs获取到的属性数据为", dataFieldDTOs, logger);
        EcAssert.verifyListEmpty(dataFieldDTOs, "reptileDataField");
        // 4 将数据属性对象列表放置到对应的dynamicBeanDTO中
        for (EcReptileDynamicBeanDTO dynamicBeanDTO : dynamicBeanDTOs) {
            List<EcReptileDataFieldDTO> dataFieldDTOsTemp = new ArrayList<>();
            for (EcReptileDataFieldDTO dataFieldDTO : dataFieldDTOs) {
                if (EcBaseUtils.equals(dynamicBeanDTO.getDynamicBeanNo(), dataFieldDTO.getDynamicBeanNo())) {
                    dataFieldDTOsTemp.add(dataFieldDTO);
                }
            }
            dynamicBeanDTO.setReptileDataFieldDTOs(dataFieldDTOsTemp);
        }
        // 5 返回列表
        return dynamicBeanDTOs;
    }

}
