package com.example.demo.thread;

import java.util.concurrent.*;

/**
 * 线程
 *
 * @author atinosun
 */
public class RunnableThread {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 50, 300, TimeUnit.SECONDS
        , new ArrayBlockingQueue<>(50)
        , new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "runnable_task_pool_" + r.hashCode());
        }
    }, new ThreadPoolExecutor.DiscardOldestPolicy());

    // 提交callable任务
    public static void callableTest() {
        int a = 1;
        Future<Boolean> future = threadPoolExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int b = a + 100;
                System.out.println(b);
                return true;
            }
        });

        try {
            System.out.println("future.get");
            Boolean boolV = future.get();
            System.out.println("callable return:" + boolV);
        } catch (Exception e) {
            System.out.println("exception" + e.getMessage());
            e.printStackTrace();
        }

    }

    // 提交runnable任务
    public static void runnableTest() {
        int a = 1;
        Future<?> future = threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                int b = a + 100;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(b);
            }
        });

        try {
            System.out.println("future.get");
            Object v = future.get(900, TimeUnit.MILLISECONDS);
            System.out.println("runnable return:" + v);
        } catch (Exception e) {
            System.out.println("exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runnableResultTest(){
        Person p = new Person(1,"person");
        Future<Person> future = threadPoolExecutor.submit(new RunnableTask(p),p);
        try {
            System.out.println("future.get");
            Person personRes = future.get(900, TimeUnit.MILLISECONDS);
            System.out.println("runnable result return:" + personRes);
        } catch (Exception e) {
            System.out.println("exception" + e.getMessage());
            e.printStackTrace();
        }

    }

}


class RunnableTask implements Runnable {

    Person p;

    RunnableTask(Person p) {
        this.p = p;
    }

    @Override
    public void run() {
        p.setId(2);
        p.setName("Runnable Task " + Thread.currentThread().getName());
    }
}

class Person {
    private Integer id;
    private String name;

    public Person(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + "]";
    }
}
