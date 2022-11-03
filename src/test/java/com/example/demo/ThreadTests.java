package com.example.demo;

import com.example.demo.thread.RunnableThread;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThreadTests {
    @Test
    public void testThreadPool(){
        //RunnableThread.callableTest();
        //RunnableThread.runnableTest();
        RunnableThread.runnableResultTest();

    }
}
