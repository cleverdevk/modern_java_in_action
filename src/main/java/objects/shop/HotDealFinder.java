package objects.shop;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HotDealFinder {
    private List<Shop> shops = Arrays.asList(
            new Shop("BestBuy"),
            new Shop("Marshalls"),
            new Shop("Target"),
            new Shop("Walmart"),
            new Shop("asdfas"),
            new Shop("fwef"),
            new Shop("vv"),
            new Shop("qq"),
            new Shop("ww"),
            new Shop("ee"),
            new Shop("rr"),
            new Shop("tt"),
            new Shop("yy"),
            new Shop("uu"),
            new Shop("ii"),
            new Shop("iid"),
            new Shop("iig"),
            new Shop("oo"),
            new Shop("5BestBuy"),
            new Shop("5Marshalls"),
            new Shop("5Target"),
            new Shop("5Walmart"),
            new Shop("5asdfas"),
            new Shop("5fwef"),
            new Shop("5vv"),
            new Shop("5qq"),
            new Shop("5ww"),
            new Shop("5ee"),
            new Shop("5rr"),
            new Shop("5tt"),
            new Shop("5yy"),
            new Shop("5uu"),
            new Shop("5ii"),
            new Shop("5iid"),
            new Shop("5iig"),
            new Shop("5oo")
    );
    private final Executor executor = Executors.newFixedThreadPool(Math.min(getShopsCount(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    public int getShopsCount() {
        return shops.size();
    }

    public List<String> findPrice(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public List<String> findPriceWithParallelStream(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public List<String> findPriceWithCompletableFuture(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync( // calculate asynchronously
                        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
                ))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join) // wait for all tasks completed
                .collect(Collectors.toList());
    }
    public List<String> findPriceWithCompletableFutureAndCustomExecutor(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync( // calculate asynchronously
                        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product), executor)
                ))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join) // wait for all tasks completed
                .collect(Collectors.toList());
    }

    public List<String> findDiscountedPrice(String product) {
        return shops.stream()
                .map(shop -> shop.getPriceForDiscount(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    public List<String> findDiscountedPriceWithCompletableFuture(String product) {
        List<CompletableFuture<String>> futures =
                shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceForDiscount(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor
                )))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public Stream<CompletableFuture<String>> findDiscountedPricesStream(String product) {
        return shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceForDiscount(product), executor))
                        .map(future -> future.thenApply(Quote::parse))
                        .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor
                        )));
    }
}
