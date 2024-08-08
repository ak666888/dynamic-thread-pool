package cn.paradox.sdk.trigger.listener;

import cn.paradox.sdk.domain.DynamicThreadPoolService;
import cn.paradox.sdk.domain.IDynamicThreadPoolService;
import cn.paradox.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.paradox.sdk.registry.IRegistry;
import com.alibaba.fastjson2.JSON;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 动态线程池变更监听
 */

public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IRegistry registry;

    private final IDynamicThreadPoolService dynamicThreadPoolService;


    public ThreadPoolConfigAdjustListener(IRegistry registry, IDynamicThreadPoolService dynamicThreadPoolService) {
        this.registry = registry;
        this.dynamicThreadPoolService = dynamicThreadPoolService;
    }


    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置，线程池名称：{} 核心线程数：{} 最大线程数：{}", threadPoolConfigEntity.getThreadPoolName(), threadPoolConfigEntity.getCorePoolSize(), threadPoolConfigEntity.getMaximumPoolSize());
        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        //更新后上报最新数据
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);


        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntityCurrent));

    }
}
