package geek.week4;

import java.util.Comparator;
import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 */
public class ThreadMain {

    public static void main(String[] args) {
        //继承Thread线程
//        new ThreadClass().start();
//
//        //实现runnable接口
//        new RunnableClass().run();
//
//        //实现callable
//        long start = System.currentTimeMillis();
//        callable();
//        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        //threadPool实现
//        executorService();
        threadPool();
    }

    private static void callable() {
        //警告:(31, 9) 参数化类 'FutureTask' 的原始使用
        //警告:(31, 37) 参数化类 'FutureTask' 的原始使用
        //警告:(31, 37) 作为原始类型 'java.util.concurrent.FutureTask' 的成员对
        // 'FutureTask(Callable<V>)' 进行了未经检查的调用 ?
        FutureTask futureTask = new FutureTask((Callable<Integer>) ThreadMain::sum);
        futureTask.run();
        try {
            System.out.println("callable异步计算结果为: " + futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executorService() {
        ExecutorService es = Executors.newFixedThreadPool(8);

        long time1 = System.currentTimeMillis();
        es.submit(ThreadMain::add);
        System.out.println("Runnable使用时间：" + (System.currentTimeMillis() - time1) + " ms");

        long time2 = System.currentTimeMillis();
        //老师能帮忙解答下这个警告吗？
        //警告:(48, 34) 赋值未经检查: 'java.util.concurrent.Future' 赋值给 'java.util.concurrent.Future<java.lang.Integer>'
        //警告:(48, 44) 赋值未经检查: 'java.util.concurrent.Callable' 赋值给 'java.util.concurrent.Callable<java.lang.Object>'
        //警告:(48, 45) 参数化类 'Callable' 的原始使用 ?
        Future<Integer> submit = es.submit((Callable) ThreadMain::sum);
        try {
            System.out.println("submit.callable异步计算结果为: " + submit.get());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("submit.Callable使用时间：" + (System.currentTimeMillis() - time2) + " ms");

        es.shutdown();
    }

    private static void threadPool() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                8+1, 16+1, 60, TimeUnit.SECONDS,
                //基于数组结构的有界阻塞队列，构造函数一定要传大小，FIFO（先进先出）；
//                new ArrayBlockingQueue<>(8*4),
                //一个支持优先级的无界阻塞队列,默认长度是 11
                new PriorityBlockingQueue<>(),
//                new PriorityBlockingQueue<>(32, (o1, o2) -> {
//                    //-1 o1<o2; 1 o1>o2
//                    return -1;
//                }),
                //该策略会丢弃任务队列中最老的一个任务，并尝试再次提交
//                new ThreadPoolExecutor.DiscardOldestPolicy());
                //丢弃无法处理的任务，不予任何处理
                new ThreadPoolExecutor.DiscardPolicy());
                //直接抛异常
//                new ThreadPoolExecutor.AbortPolicy());
                //主线程去执行
//                new ThreadPoolExecutor.CallerRunsPolicy());
        long time1 = System.currentTimeMillis();
        threadPool.submit(ThreadMain::add);
        System.out.println("Runnable1使用时间：" + (System.currentTimeMillis() - time1) + " ms");
        threadPool.submit(ThreadMain::add);
        long time2 = System.currentTimeMillis();
        System.out.println("Runnable2使用时间：" + (System.currentTimeMillis() - time2) + " ms");
        //lambda可以替换为方法引用？
        threadPool.submit(() -> add());
        long time3 = System.currentTimeMillis();
        System.out.println("Runnable3使用时间：" + (System.currentTimeMillis() - time3) + " ms");

//        long time2 = System.currentTimeMillis();
//        Future<Integer> submit = threadPool.submit(() -> sum());
//        try {
//            System.out.println("submit.callable异步计算结果为: " + submit.get());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("submit.Callable使用时间：" + (System.currentTimeMillis() - time2) + " ms");

    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    private static void add() {
        long start = System.currentTimeMillis();
        int result = sum(); //这是得到的返回值
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    static class ThreadClass extends Thread {
        @Override
        public void run() {
            add();
        }
    }

    static class RunnableClass implements Runnable {

        @Override
        public void run() {
            add();
        }
    }
}
