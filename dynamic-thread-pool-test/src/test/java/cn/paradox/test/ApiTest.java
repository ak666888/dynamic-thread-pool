package cn.paradox.test;

import cn.paradox.sdk.domain.model.entity.ThreadPoolConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {
    @Resource
    private RTopic dynamicThreadRedisTopic;

    @Test
    public void test_dynamicThreadRedisTopic () throws InterruptedException {
        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity("dynamic-thread-pool-test-app", "threadPoolExecutor01");
        threadPoolConfigEntity.setPoolSize(100);
        threadPoolConfigEntity.setMaximumPoolSize(100);
        dynamicThreadRedisTopic.publish(threadPoolConfigEntity);

        new CountDownLatch(1).await();
    }
}
