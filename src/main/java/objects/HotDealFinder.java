package objects;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
            new Shop("oo"),
            new Shop("pp"),
            new Shop("ll")
    );

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
}
