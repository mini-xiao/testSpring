package com.xiao.testspring.controller;

import com.xiao.testspring.poolManager.cachePoolManager;
import com.xiao.testspring.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 * 线程池管理的总结
 * @Params:
 * @Author: LiuMZ
 * @Date: 2020/10/19 11:05
 */
@RestController
@RequestMapping("/thread/pool")
public class threadPoolController {

    @ResponseBody
    @RequestMapping("/test")
    public Result test(){
        Result res=new Result("result测试");
        return res;
    }

    /** 可缓存线程池
     * @return
     */
    @RequestMapping("/cache")
    public Result cacheThreadPool(){
        //实例化一个线程池
        cachePoolManager cachePool = cachePoolManager.getNewInstance();
        for (int i = 0; i < 100; i++) {
            final int ii = i;
            try {
                //线程阻塞 让打印可以有先后顺序  不会乱
                Thread.sleep(ii);
                System.out.println(ii+"  sleep...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //往线程池添加方法
            cachePool.addExecuteTask(()-> {
                try {
                    //该线程阻塞10秒
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "，执行" + ii);
            });
        }
        //整个过程中，循坏添加任务(Runnable)100次，同步队列(SynchronousQueue)寻找可用的线程，但是线程都在阻塞(sleep)10秒中
        //所以新建一个线程进行执行任务，而可缓存线程池的最大线程池大小（maximumPoolSize）无限制，取决于JVM能够创建的最大线程大小
        //添加完任务100次后，接口返回成功数据，线程池还在阻塞(sleep)中。阻塞完，任务完成
        //所有线程空闲，达到60秒(keepAliveTime=60L)后，所有线程被销毁
        return Result.success("成功");
    }



}
