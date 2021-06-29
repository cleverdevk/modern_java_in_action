package chapters;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import lombok.extern.slf4j.Slf4j;
import objects.temperature.*;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ChapterSeventeen {
    public static void run() {
        // getTemperature("New York").subscribe(new TempSubscriber());

        //************** RxJava **************************

        // onNext(first) -> onNext(second) -> onComplete()
        Observable<String> s = Observable.just("first", "second");

        // 0 to infinite per second
        Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);

        onePerSec.blockingSubscribe(i -> System.out.println(TempInfo.fetch("New York")));
        Observable<TempInfo> tempObservable =  getTemperatureWithRxJava("new york", 5);
        tempObservable.blockingSubscribe(new TempObserver());
        Observable<TempInfo> celsiusTempObservable = getCelsiusTemperatureWithRxJava("new york", 5);
        celsiusTempObservable.blockingSubscribe(new TempObserver());

        Observable<TempInfo> observable = getCelsiusTemperatures(5, "NEWYORK", "SEOUL");
        observable.blockingSubscribe(new TempObserver());
    }

    private static Flow.Publisher<TempInfo> getTemperature(String town) {
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }

    public static Observable<TempInfo> getTemperatureWithRxJava(String town, int n) {
        return Observable.create(observableEmitter -> {
            Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(i -> {
                        if (!observableEmitter.isDisposed()) {
                            if (i >= n) {
                                observableEmitter.onComplete();
                            } else {
                                try {
                                    observableEmitter.onNext(TempInfo.fetch(town));
                                } catch (Exception e) {
                                    observableEmitter.onError(e);
                                }
                            }
                        }
                    });
        });
    }

    public static Observable<TempInfo> getCelsiusTemperatureWithRxJava(String town, int n) {
        return getTemperatureWithRxJava(town, n)
                .map(temp -> new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 /9));
    }
    public static Observable<TempInfo> getCelsiusTemperaturePositiveWithRxJava(String town, int n) {
        return getTemperatureWithRxJava(town, n)
                .map(temp -> new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 /9))
                .filter(temp -> temp.getTemp() > 0);
    }
    public static Observable<TempInfo> getCelsiusTemperatures(int n, String... towns) {
        return Observable.merge(Arrays.stream(towns)
                .map(town -> getCelsiusTemperatureWithRxJava(town, n))
                .collect(Collectors.toList()));
    }
}
