package com.example.demo;

import com.example.demo.thread.RunnableThread;
import com.example.demo.thread.ThreadPerformance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class ThreadTests {
    @Test
    public void testThreadPool() {
        //RunnableThread.callableTest();
        //RunnableThread.runnableTest();
        //RunnableThread.runnableResultTest();
        //RunnableThread.runnableTransCallable();
        //RunnableThread.futureTaskPackCallable();
        RunnableThread.futureTaskPackRunnable();

    }

    @Test
    public void testThreadPerformance(){
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

}
