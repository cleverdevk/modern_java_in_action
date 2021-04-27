package chapters;

import objects.Point;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChapterNine {
    public interface Task {
        void execute();
    }

    public static void doSomething(Runnable r) { r.run(); }
    public static void doSomething(Task a) { a.execute(); }

    // strategy pattern
    public interface ValidationStrategy {
        boolean execute(String s);
    }

    public static class LowercaseStrategy implements ValidationStrategy {
        public LowercaseStrategy() {

        }
        @Override
        public boolean execute(String s) {
            return s.matches("[a-z]+");
        }
    }

    public static class NumericStrategy implements ValidationStrategy {
        public NumericStrategy() {

        }
        @Override
        public boolean execute(String s) {
            return s.matches("\\d+");
        }
    }

    public static class Validator {
        private final ValidationStrategy strategy;
        public Validator(ValidationStrategy validationStrategy) {
            this.strategy = validationStrategy;
        }
        public boolean validate(String s) {
            return strategy.execute(s);
        }
    }

    // template method pattern
    static abstract class OnlineBanking {
        public void processCustomer(int id) {
            System.out.println("Customer processed : " + id);
            makeCustomerHappy(id);
        }

        abstract void makeCustomerHappy(int customerId);
    }

    public static class ShinhanOnlineBanking extends OnlineBanking {

        @Override
        void makeCustomerHappy(int customerId) {
            System.out.println("hey Customer " + customerId + ", shinhan is the best!");
        }
    }

    public static class WooriOnlineBanking extends OnlineBanking {

        @Override
        void makeCustomerHappy(int customerId) {
            System.out.println("hey Customer " + customerId + ", woori is the best!" );
        }
    }

    // template pattern with lambda
    public static class OnlineBankingWithLambda {
        public void processCustomer(int id, Consumer<Integer> makeCustomerHappy) {
            System.out.println("Customer processed : " + id);
            makeCustomerHappy.accept(id);
        }
    }

    // observer pattern
    interface Observer {
        void notify(String tweet);
    }

    static class NYTimes implements Observer {

        @Override
        public void notify(String tweet) {
            if (tweet != null && tweet.contains("money")) {
                System.out.println("Money in NY : " + tweet);
            }
        }
    }

    static class Guardian implements Observer {

        @Override
        public void notify(String tweet) {
            if (tweet != null && tweet.contains("queen")) {
                System.out.println("Queen in GD : " + tweet);
            }
        }
    }

    interface Subject {
        void registerObserver(Observer o);
        void notifyObserver(String tweet);
    }

    static class Feed implements Subject {
        private final List<Observer> observers = new ArrayList<>();

        @Override
        public void registerObserver(Observer o) {
            this.observers.add(o);
        }

        @Override
        public void notifyObserver(String tweet) {
            observers.forEach(o -> o.notify(tweet));
        }
    }

    // Chain of responsibility pattern
    public static abstract class ProcessingObject<T> {
        protected ProcessingObject<T> successor;
        public void setSuccessor(ProcessingObject<T> successor) {
            this.successor = successor;
        }
        public T handle(T input) {
            T r = handleWork(input);
            if (successor != null) {
                return successor.handle(r);
            }
            return r;
        }
        abstract protected T handleWork(T input);
    }

    public static class HeaderTextProcessing extends ProcessingObject<String> {

        @Override
        protected String handleWork(String input) {
            return "From Nick Kang : " + input;
        }
    }

    public static class SpellCheckerProcessing extends ProcessingObject<String> {

        @Override
        protected String handleWork(String input) {
            return input.replaceAll("moden", "modern");
        }
    }

    public static class FooterTextProcessing extends ProcessingObject<String> {

        @Override
        protected String handleWork(String input) {
            return input + " <EOF>";
        }
    }


    // Factory
    public static class Product {

    }

    public static class AProduct extends Product {

    }

    public static class BProduct extends Product {

    }

    public static class ProductFactory {
        public static Product createProduct(String name) {
            switch (name) {
                case "A": return new AProduct();
                case "B": return new BProduct();
                default: throw new RuntimeException("No such product : " + name);
            }
        }
    }

    public static void run() {
        // refactoring anonymous class to lambda
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };

        Runnable r1ExpressedByLambda = () -> System.out.println("Hello");

        // doSomething(() -> System.out.println("Hello")); // error
        doSomething((Task) () -> System.out.println("Hello"));

        String lower = "lowercase";
        String numeric = "1234";

        Validator lValidator = new Validator(new LowercaseStrategy());
        Validator nValidator = new Validator(new NumericStrategy());

        System.out.println(nValidator.validate(lower));
        System.out.println(nValidator.validate(numeric));

        System.out.println(lValidator.validate(lower));
        System.out.println(lValidator.validate(numeric));

        Validator nValidatorWithLambda = new Validator(s -> s.matches("\\d+"));
        Validator lValidatorWithLambda = new Validator(s -> s.matches("[a-z]+"));

        System.out.println(nValidatorWithLambda.validate(lower));
        System.out.println(nValidatorWithLambda.validate(numeric));

        System.out.println(lValidatorWithLambda.validate(lower));
        System.out.println(lValidatorWithLambda.validate(numeric));

        OnlineBanking shinhan = new ShinhanOnlineBanking();
        OnlineBanking woori = new WooriOnlineBanking();

        shinhan.processCustomer(35);
        woori.processCustomer(33);

        // template method pattern with lambda exp
        OnlineBankingWithLambda onlineBankingWithLambda = new OnlineBankingWithLambda();
        onlineBankingWithLambda.processCustomer(33, id -> {
            System.out.println("hey Customer " + id + ", shinhan is the best!");
        });

        Feed f = new Feed();
        f.registerObserver(new Guardian());
        f.registerObserver(new NYTimes());

        // add observer with lambda
        f.registerObserver(tweet -> {
            if (tweet != null && tweet.contains("korea")) {
                System.out.println("korea in KoreanTimes : " + tweet);
            }
        });

        f.notifyObserver("hello queen.");
        f.notifyObserver("queen and money");
        f.notifyObserver("korea and money");

        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        ProcessingObject<String> p3 = new FooterTextProcessing();

        p1.setSuccessor(p2);
        p2.setSuccessor(p3);

        String result = p1.handle("moden java in action");
        System.out.println(result);

        UnaryOperator<String> headerProcessing = s -> "Header " + s;
        UnaryOperator<String> spellCheckerProcessing = s -> s.replaceAll("moden", "modern");
        UnaryOperator<String> footerProcessing = s -> s + " <EOF>";

        Function<String, String> pipeline = headerProcessing
                .andThen(spellCheckerProcessing)
                .andThen(footerProcessing);

        String resultFromLambda = pipeline.apply("moden java in action");
        System.out.println(resultFromLambda);

        Product ap = ProductFactory.createProduct("A");
        Product bp = ProductFactory.createProduct("B");

        // factory with lambda
        Supplier<Product> ASupplier = AProduct::new;
        Supplier<Product> BSupplier = BProduct::new;

        Map<String, Supplier<Product>> productSupplier = Map.ofEntries(
                Map.entry("A", AProduct::new),
                Map.entry("B", BProduct::new)
        );

        Product ap2 = productSupplier.get("A").get();
        Product bp2 = productSupplier.get("B").get();


        // just for debugging
//        List<Point> points = Arrays.asList(new Point(2, 2), null);
//        points.stream().map(Point::getX).forEach(System.out::println);


        // peek
        List<Integer> ns = Stream.of(1,2,3,4)
                .peek(System.out::println)
                .map(x -> x + 1)
                .peek(System.out::println)
                .filter(x -> x % 2 == 0)
                .peek(System.out::println)
                .limit(3)
                .peek(System.out::println)
                .collect(Collectors.toList());

        System.out.println(ns);
    }
}
