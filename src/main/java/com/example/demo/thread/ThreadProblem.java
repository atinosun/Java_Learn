package com.example.demo.thread;

import lombok.SneakyThrows;
import org.springframework.util.StopWatch;

/**
 * 线程带来的风险
 *
 * @author atinosun
 */
public class ThreadProblem {
    private static int innerCounter = 0;
    private final static String LOCK_A = "lock_a";
    private final static String LOCK_B = "lock_b";
    private static final int CALCULATE_CIRCLE_NUM = 100000;

    @SneakyThrows
    public static void main(String[] args) {
        testThreadSafe();
        //testThreadDeadLock();
        //testThreadPerformance();

    }

    /**
     * 线程安全的问题
     */
    @SneakyThrows
    public static void testThreadSafe() {
        Thread threadA = new Thread(new RepeatCalculator());
        Thread threadB = new Thread(new RepeatCalculator());
        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println("主线程运行的结果是:" + innerCounter);
        //主线程运行的结果是:120994
    }

    /**
     * 线程死锁问题
     */
    @SneakyThrows
    public static void testThreadDeadLock() {
        Thread threadA = new DeadLockThreadA();
        Thread threadB = new DeadLockThreadB();
        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println("主线程运行结束了");
        /**
         * 结果，卡死了
         * com.example.demo.thread.ThreadProblem$DeadLockThreadA开始运行了
         * com.example.demo.thread.ThreadProblem$DeadLockThreadB开始运行了
         * com.example.demo.thread.ThreadProblem$DeadLockThreadA已经获取了锁：lock_a
         * com.example.demo.thread.ThreadProblem$DeadLockThreadB已经获取了锁：lock_b
         */

    }

    /**
     * 线程性能问题
     */
    @SneakyThrows
    public static void testThreadPerformance() {
        StopWatch stopWatch = new StopWatch("性能问题");
        stopWatch.start("多个线程，每个线程只执行1次自增操作");
        Thread threadA = new Thread(new OnceCalculator());
        threadA.start();
        threadA.join();
        stopWatch.stop();

        stopWatch.start("单个线程，内部累计执行多次自增操作");
        Thread threadB = new Thread(new RepeatCalculator());
        threadB.start();
        threadB.join();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        /**
         * StopWatch '性能问题': running time = 2300123 ns
         * ---------------------------------------------
         * ns         %     Task name
         * ---------------------------------------------
         * 000413105  018%  多个线程，每个线程只执行1次自增操作
         * 001887018  082%  单个线程，内部累计执行多次自增操作
         */
    }

    private static class DeadLockThreadA extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getClass().getName() + "开始运行了");
            synchronized (LOCK_A) {
                System.out.println(Thread.currentThread().getClass().getName() + "已经获取了锁：" + LOCK_A);
                Thread.sleep(1000);
                synchronized (LOCK_B) {
                    System.out.println(Thread.currentThread().getClass().getName() + "已经获取了锁：" + LOCK_B);
                }
            }
        }
    }

    private static class DeadLockThreadB extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getClass().getName() + "开始运行了");
            synchronized (LOCK_B) {
                System.out.println(Thread.currentThread().getClass().getName() + "已经获取了锁：" + LOCK_B);
                Thread.sleep(1000);
                synchronized (LOCK_A) {
                    System.out.println(Thread.currentThread().getClass().getName() + "已经获取了锁：" + LOCK_A);
                }
            }
        }
    }

    private static class RepeatCalculator implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < CALCULATE_CIRCLE_NUM; i++) {
                innerCounter++;
            }
        }
    }

    private static class OnceCalculator implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            innerCounter++;
        }
    }
}
