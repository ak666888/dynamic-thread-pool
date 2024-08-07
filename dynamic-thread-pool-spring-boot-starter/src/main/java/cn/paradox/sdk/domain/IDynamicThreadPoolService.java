package cn.paradox.sdk.domain;

import cn.paradox.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 动态线程池服务
 */
public interface IDynamicThreadPoolService {

    List<ThreadPoolConfigEntity> queryThreadPoolList();

    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);
}
