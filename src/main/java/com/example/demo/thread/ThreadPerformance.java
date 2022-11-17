package com.example.demo.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPerformance {
    private static final int MAX_CIRCLE_NUM = 100000;
    private static final int MAX_THREADS_NUM = 1000;
    private static final int MAX_THREADS_POOL_NUM = 100;

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("线程串行调用");
        ThreadPerformance.threadSerialPerformance();
        stopWatch.stop();
        stopWatch.start("线程并行调用");
        ThreadPerformance.threadParallelPerformance();
        stopWatch.stop();
        stopWatch.start("线程池调用");
        ThreadPerformance.threadPoolPerformance();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
    /**
     * 线程池
     */
    @SneakyThrows
    public static void threadPoolPerformance() {
        StopWatch stopWatch = new StopWatch("线程池初始化");
        stopWatch.start();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_THREADS_POOL_NUM, MAX_THREADS_POOL_NUM,
                                                                       1, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (int i = 0; i < MAX_THREADS_NUM; i++) {
            threadPoolExecutor.submit(new TestCalculator());
        }

        System.out.println("线程池数量："+threadPoolExecutor.getPoolSize());
        threadPoolExecutor.shutdown(); // 不接受新任务
        threadPoolExecutor.awaitTermination(10,TimeUnit.SECONDS); // 主线程等子线程执行完毕或超时
    }

    /**
     * 线程串行测试
     */
    @SneakyThrows
    public static void threadSerialPerformance() {
        for (int i = 0; i < MAX_THREADS_NUM; i++) {
            Thread thread = new Thread(new TestCalculator());
            thread.start();
            thread.join(); // 运行完再进入下一个循环
        }
    }

    /**
     * 线程并行测试
     */
    @SneakyThrows
    public static void threadParallelPerformance() {
        List<Thread> threadList = new ArrayList<>(MAX_THREADS_NUM);
        for (int i = 0; i < MAX_THREADS_NUM; i++) {
            Thread thread = new Thread(new TestCalculator());
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join(); // 在这里的目的不是为了保证线程执行顺序，只是为了保证执行完毕run方法
        }
    }

    private static class TestCalculator implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < MAX_CIRCLE_NUM; i++) {
                int res = i * i;
                //System.out.println(Thread.currentThread().getName() + "的执行结果是：" + res);
            }
        }
    }
}
