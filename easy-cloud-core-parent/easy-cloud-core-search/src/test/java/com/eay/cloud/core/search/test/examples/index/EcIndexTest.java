package com.eay.cloud.core.search.test.examples.index;

import com.easy.cloud.core.search.EcCoreSearchApplication;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: EcIndexTest
 * @Description:
 * @Author tudou
 * @Date 2018/7/3 15:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcCoreSearchApplication.class)
public class EcIndexTest {
    @Autowired
    private TransportClient client;


}
