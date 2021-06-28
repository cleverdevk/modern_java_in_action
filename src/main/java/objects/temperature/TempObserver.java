package objects.temperature;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class TempObserver implements Observer<TempInfo> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println("TempObserver Subscribe is Started.");
    }

    @Override
    public void onNext(@NonNull TempInfo tempInfo) {
        System.out.println(tempInfo);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        System.out.println("Failed to fetch temperature : " + e.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}
