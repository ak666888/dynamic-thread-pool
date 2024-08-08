package cn.paradox.sdk.registry.redis;

import cn.paradox.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.paradox.sdk.domain.model.vo.RegistryEnumVO;
import cn.paradox.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;

import java.time.Duration;
import java.util.List;

/**
 * 注册中心
 */
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissionClient;

    public RedisRegistry(RedissonClient redissionClient) {
        this.redissionClient = redissionClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        RList<ThreadPoolConfigEntity> list = redissionClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.addAll(threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissionClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
