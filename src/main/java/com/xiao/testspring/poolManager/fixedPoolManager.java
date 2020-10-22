package com.xiao.testspring.poolManager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 功能描述:   定长线程池
 * newFixedThreadPool   fixed--固定，确定
 * 创建可容纳固定数量的线程池子
 * 池子满了就不再添加线程，如果池中的所有线程均处于繁忙状态，新任务就会进入阻塞队列
 * 适用：执行长期的任务
 *
 * 底层：
 *    return new ThreadPoolExecutor(nThreads, nThreads,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
 * @Params:
 * @Author: LiuMZ
 * @Date: 2020/10/22 15:19
 */
public final class fixedPoolManager {

    private static fixedPoolManager fixedPoolManager=new fixedPoolManager();

    private static final int corePoolSize=5;

    private static final int maximumPoolSize=5;

    private static final long keepAliveTime=0L;

    private final BlockingQueue<Runnable> workQueue=new LinkedBlockingQueue<Runnable>();

    private final ThreadFactory factory=new ThreadFactoryBuilder().setNameFormat("fixedPool-thread-%d").build();

    private final RejectedExecutionHandler header=new ThreadPoolExecutor.AbortPolicy();

    private final ThreadPoolExecutor fixedThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,keepAliveTime, TimeUnit.MILLISECONDS,workQueue,factory,header);

    /**
     * 线程池的单例创建方法
     * @return
     */
    public static fixedPoolManager getNewInstance(){
        return fixedPoolManager;
    }

    /**
     * 构造方法私有化 禁止任意实例化
     */
    private fixedPoolManager() {
    }

    /**
     * 向线程池添加方法
     * @param task
     */
    public void addExecuteTask(Runnable task){
        if (task != null) {
            fixedThreadPool.execute(task);
        }
    }
}
