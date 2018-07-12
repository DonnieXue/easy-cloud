package com.easy.cloud.core.reptile.common.pipeline.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easy.cloud.core.common.string.utils.EcStringUtils;
import com.easy.cloud.core.reptile.common.pipeline.EcBaseJsonPipeline;
import com.geccocrawler.gecco.annotation.PipelineName;

@PipelineName(value = "redisJsonPipeline")
public class EcRedisJsonPipeline extends EcBaseJsonPipeline{

	@Override
	public void doProcess(JSONObject jo) {
		logger.info(Thread.currentThread().getName() + "---redis获取到的数据：" + JSONObject.toJSONString(jo));
		JSONArray jsonArray = jo.getJSONArray("roleDetail");
		if (jsonArray == null) {
			return;
		}
		JSONArray newJsonArray = new JSONArray();
		for (Object jsonvalue : jsonArray) {
			if (jsonvalue == null || EcStringUtils.isEmpty(jsonvalue.toString())){
				continue;
			}
			if (jsonvalue.toString().contains("var charObj")) {
				newJsonArray.add(jsonvalue);
			}
		}
		logger.info(String.valueOf(newJsonArray.size()));
	}

}
