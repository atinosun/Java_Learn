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

    // runnable带返回值
    public static void runnableResultTest() {
        Person p = new Person(1, "person");
        Future<Person> future = threadPoolExecutor.submit(new RunnableTask(p), p);
        try {
            System.out.println("future.get");
            Person personRes = future.get(900, TimeUnit.MILLISECONDS);
            System.out.println("runnable result return:" + personRes);
        } catch (Exception e) {
            System.out.println("exception" + e.getMessage());
            e.printStackTrace();
        }

    }

    // runnable 转换为 callable
    public static void runnableTransCallable() {
        Person p = new Person(1, "name");
        RunnableTask runnableTask = new RunnableTask(p);
        // 转换
        Callable<Person> callable = Executors.callable(runnableTask, p);
        Future<Person> personFuture = threadPoolExecutor.submit(callable);
        try {
            Person personRes = personFuture.get();
            System.out.println(personRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        Callable<Object> callable1 = Executors.callable(runnable);
        Future<Object> future1 = threadPoolExecutor.submit(callable1);
        try {
            Object furRes = future1.get();
            System.out.println(furRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // futureTask 包装 callable任务
    public static void futureTaskPackCallable() {
        int a = 1, b = 2;
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return a + b;
            }
        };

        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        // 使用单线程
       /* new Thread(futureTask).start();
        try {
            Integer res1 = futureTask.get();
            System.out.println("res1:" + res1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // 使用线程池
        threadPoolExecutor.submit(futureTask);
        try {
            Integer res2 = futureTask.get();
            System.out.println("res2:" + res2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // futureTask 包装 runnable任务
    public static void futureTaskPackRunnable(){
        Person person = new Person(1,"person");
        RunnableTask runnableTask = new RunnableTask(person);
        FutureTask<Person> futureTask = new FutureTask<>(runnableTask,person);
        // 使用单线程
        new Thread(futureTask).start();
        /*try {
            Person person1 = futureTask.get();
            System.out.println("person1:"+person1);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        // 使用线程池
        threadPoolExecutor.submit(futureTask);
        try{
            Person person2 = futureTask.get();
            System.out.println("person2:"+person2);
        }catch (Exception e){
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
