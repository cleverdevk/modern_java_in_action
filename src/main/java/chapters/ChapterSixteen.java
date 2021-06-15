package chapters;

import objects.HotDealFinder;
import objects.Shop;

import java.util.List;
import java.util.concurrent.*;

public class ChapterSixteen {
    public static void run() {
        // using Future before Java 8
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Double> future = executorService.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return doSomeLongComputation();
            }
        });
        doSomething();
        try {
            Double result = future.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            // computation exception occurred
        } catch (InterruptedException e) {
            // waiting interrupt occurred in current thread
        } catch (TimeoutException e) {
            // Timeout occurred before Future completed.
        }


        // shop api
        Shop shop = new Shop();
        long start = System.nanoTime();
        Future<Double> f = shop.getPriceAsync("hello");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + "msecs");
        doSomething();
        try {
            double price = f.get();
            System.out.printf("Price is  %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + "msecs");

        // error occurred
//        Future<Double> f2 = shop.getPriceAsyncPropagateException("");
//        try {
//            System.out.println(f2.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        // HotDealFinder
        HotDealFinder hotDealFinder = new HotDealFinder();

        // * It takes about four seconds because of sequential request of blocking api
        // List<String> hotDeals = hotDealFinder.findPrice("sony 16-35 f4 lens");
        // System.out.println(hotDeals);

        start = System.nanoTime();
        List<String> hotDeals = hotDealFinder.findPriceWithParallelStream("sony a7 m III");
        // System.out.println(hotDeals);
        long diff = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Parallel Stream : " + diff);

        start = System.nanoTime();
        hotDeals = hotDealFinder.findPriceWithCompletableFuture("sony a7 m III");
        // System.out.println(hotDeals);
        diff = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("CompletableFuture : " + diff);


    }
    public static double doSomeLongComputation() throws InterruptedException {
        Thread.sleep(3_000);
        return 3.3;
    }
    public static void doSomething() {
        // do something.
    }
}
