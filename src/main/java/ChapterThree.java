import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Slf4j
class ChapterThree {
    public static Comparator<ChapterOneAndTwo.Apple> byWeight = new Comparator<ChapterOneAndTwo.Apple>() {
        @Override
        public int compare(ChapterOneAndTwo.Apple o1, ChapterOneAndTwo.Apple o2) {
            return Integer.compare(o1.getWeight(), o2.getWeight());
        }
    };

    @FunctionalInterface
    public interface MyCallable extends Callable<String> {
        @Override
        String call();
    }

    public static Comparator<ChapterOneAndTwo.Apple> byWeightWithLambda =
            (ChapterOneAndTwo.Apple a1, ChapterOneAndTwo.Apple a2) -> Integer.compare(a1.getWeight(), a2.getWeight());

    public static Callable<String> fetch() {
        return () -> "fetch Called.";
    }

    public static String processFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/a.txt"))) {
            return br.readLine();
        }
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/a.txt"))) {
            return p.process(br);
        }
    }

    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader b) throws IOException;
    }

    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
    }

    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for(T t: list) {
            c.accept(t);
        }
    }

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T t : list) {
            result.add(f.apply(t));
        }

        return result;
    }

    public static void run() throws Exception {
        List<ChapterOneAndTwo.Apple> apples = ChapterOneAndTwo.appleGenerator(20);
        apples.sort(byWeightWithLambda);
        System.out.println(apples.toString());

        System.out.println(fetch().call());

        System.out.println(processFile());

        System.out.println(processFile(BufferedReader::readLine));
        System.out.println(processFile((BufferedReader br) -> br.readLine() + br.readLine()));

        forEach(apples, (a) -> System.out.println(a.getWeight()));

        List<String> appleStrings = map(apples, (a) -> Integer.toString(a.getWeight()) + a.getColor().toString());
        System.out.println(appleStrings);

        Predicate<ChapterOneAndTwo.Apple> p = apples::add;
        Consumer<ChapterOneAndTwo.Apple> c = (a) -> apples.toString();

    }

}
