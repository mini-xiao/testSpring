package com.xiao.testspring.poolManager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 功能描述:   可缓存线程池
 *      线程池大小无限制，取决于操作系统（JVM）能够创建的最大线程大小
 *      新任务进来时，插入到同步队列（synchronousQueue）中，同步队列会在线程池中寻找可用的线程来执行，有则执行，没有则新建一个线程
 *      若池中线程空闲时间超过指定大小（60s），则该线程会被销毁
 *      适用：执行很多短期异步的小程序或者负载较轻的服务器
 *     底层↓：
 *     return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 *
 *
 * @Author: LiuMZ
 * @Date: 2020/10/22 11:30
 */
public final class cachePoolManager {

    private static cachePoolManager cachePoolManager=new cachePoolManager();

    private static final int corePoolSize=0;

    private static final int maximumPoolSize=Integer.MAX_VALUE;

    private static final long keepAliveTime=60L;

    private final BlockingQueue<Runnable> workQueue=new SynchronousQueue<Runnable>();

    private final ThreadFactory factory=new ThreadFactoryBuilder().setNameFormat("cachePool-thread-%d").build();

    private final ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS,workQueue,factory);

    /**
     * 线程池的单例创建方法
     * @return
     */
    public static cachePoolManager getNewInstance(){
        return cachePoolManager;
    }

    /**
     * 构造方法私有化 禁止任意实例化
     */
    private cachePoolManager() {
    }

    /**
     * 向线程池添加方法
     * @param task
     */
    public void addExecuteTask(Runnable task){
        if (task != null) {
            cachedThreadPool.execute(task);
        }
    }

    /**
     * 将线程池初始化，核心线程数量
     */
    public void perpare() {
        if (cachedThreadPool.isShutdown() && !cachedThreadPool.prestartCoreThread()) {
            @SuppressWarnings("unused")
            int startThread = cachedThreadPool.prestartAllCoreThreads();
        }
    }

    /**
     * 判断是否是最后一个任务
     */
    protected boolean isTaskEnd() {
        if (cachedThreadPool.getActiveCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存大小
     */
    public int getQueue(){
        return cachedThreadPool.getQueue().size();
    }

    /**
     * 获取线程池中的线程数目
     */
    public int getPoolSize(){
        return cachedThreadPool.getPoolSize();
    }

    /**
     * 获取已完成的任务数
     */
    public long getCompletedTaskCount(){
        return cachedThreadPool.getCompletedTaskCount();
    }

    /**
     * 关闭线程池，不在接受新的任务，会把已接受的任务执行玩
     */
    public void shutdown() {
        cachedThreadPool.shutdown();
    }
}
