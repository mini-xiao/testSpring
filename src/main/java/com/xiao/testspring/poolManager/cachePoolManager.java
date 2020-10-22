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
    /**
     * 实例化一个对象（仅此一个）
     */
    private static cachePoolManager cachePoolManager=new cachePoolManager();

    /**
     * 线程池基本大小
     * 当向线程池提交一个任务时，若线程池已创建的线程数小于corePoolSize，即便此时存在空闲线程也会通过创建一个新线程来执行该任务
     * 直到已创建的线程数大于或等于corePoolSize时，新任务才会交给队列进行处理
     * 除了利用提交新任务来创建和启动线程（按需构造），也可以通过 prestartCoreThread() 或 prestartAllCoreThreads() 方法来提前启动线程池中的基本线程
     */
    private static final int corePoolSize=0;

    /**
     * 线程池最大大小
     * 线程池所允许的最大线程个数。当队列满了，且已创建的线程数小于maximumPoolSize，
     * 则线程池会创建新的线程来执行任务。另外，对于无界队列，可忽略该参数。
     */
    private static final int maximumPoolSize=Integer.MAX_VALUE;

    /**
     * 线程存活保持时间
     * 当线程池中线程数大于核心线程数时，线程的空闲时间如果超过线程存活时间，那么这个线程就会被销毁
     * 直到线程池中的线程数小于等于核心线程数
     */
    private static final long keepAliveTime=60L;

    /**
     * 任务队列
     * 用于传输和保存等待执行任务的阻塞队列
     */
    private final BlockingQueue<Runnable> workQueue=new SynchronousQueue<Runnable>();

    /**
     * 线程工厂
     * 用于创建新线程，threadFactory创建的线程也是采用new Thread()方式
     * threadFactory创建的线程名都具有统一的风格：pool-m-thread-n（m为线程池的编号，n为线程池内的线程编号）。
     */
    private final ThreadFactory factory=new ThreadFactoryBuilder().setNameFormat("cachePool-thread-%d").build();

    /**
     * 线程饱和策略
     * 当线程池和队列都满了，再加入线程会执行此策略
     */
    private final RejectedExecutionHandler header=new ThreadPoolExecutor.AbortPolicy();

    /**
     * 线程池构造初始化
     */
    private final ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS,workQueue,factory,header);

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
