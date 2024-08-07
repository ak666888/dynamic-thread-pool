package cn.paradox.sdk.domain;

import cn.paradox.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态线程池服务
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService {

    private Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    private final String applicationName;

    public DynamicThreadPoolService(Map<String, ThreadPoolExecutor> threadPoolExecutorMap, String applicationName) {
        this.threadPoolExecutorMap = threadPoolExecutorMap;
        this.applicationName = applicationName;
    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        Set<String> threadPoolBeanNames = threadPoolExecutorMap.keySet();
        List<ThreadPoolConfigEntity> threadPoolVOS = new ArrayList<>(threadPoolBeanNames.size());
        for (String threadPoolBeanName : threadPoolBeanNames){
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolBeanName);
            ThreadPoolConfigEntity threadPoolConfigVO = new ThreadPoolConfigEntity(applicationName, threadPoolBeanName);
            threadPoolConfigVO.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolConfigVO.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolConfigVO.setActiveCount(threadPoolExecutor.getActiveCount());
            threadPoolConfigVO.setPoolSize(threadPoolExecutor.getPoolSize());
            threadPoolConfigVO.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
            threadPoolConfigVO.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolConfigVO.setReamainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
            threadPoolVOS.add(threadPoolConfigVO);
        }
        return threadPoolVOS;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (null == threadPoolExecutor) return new ThreadPoolConfigEntity(applicationName, threadPoolName);

        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity(applicationName, threadPoolName);
        threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
        threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolConfigEntity.setReamainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());

        if (logger.isDebugEnabled()) {
            logger.info("动态线程池， 配置查询 应用名：{} 线程名：{} 池化配置：{}", applicationName, threadPoolName, JSON.toJSONString(threadPoolConfigEntity));

        }
        return threadPoolConfigEntity;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        if (null == threadPoolConfigEntity || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (null == threadPoolExecutor) return;

        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }
}
