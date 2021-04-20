import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ChapterSix {
    public static List<Transaction> initTransactions() {
        ChapterFourAndFive.Trader raoul = new ChapterFourAndFive.Trader("Raoul", "Cambridge");
        ChapterFourAndFive.Trader mario = new ChapterFourAndFive.Trader("Mario", "Milan");
        ChapterFourAndFive.Trader alan = new ChapterFourAndFive.Trader("Alan", "Cambridge");
        ChapterFourAndFive.Trader brian = new ChapterFourAndFive.Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        return transactions;
    }

    public static Map<Integer, List<Transaction>> transactionsByYear(List<Transaction> transactions) {
        Map<Integer, List<Transaction>> transactionsByYear = new HashMap<>();

        for (Transaction transaction : transactions) {
            int year = transaction.getYear();
            List<Transaction> transactionsForYear = transactionsByYear.get(year);
            if (transactionsForYear == null) {
                transactionsForYear = new ArrayList<>();
                transactionsByYear.put(year, transactionsForYear);
            }
            transactionsForYear.add(transaction);
        }

        return transactionsByYear;
    }

    public static Map<Integer, List<Transaction>> transactionsByYearWithStream(List<Transaction> transactions) {
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getYear));
    }

    // counting with reducing
    public static <T> Collector<T, ?, Long> counting() {
        return Collectors.reducing(0L, e -> 1L, Long::sum);
    }

    // grouping by type
    public static Map<Dish.Type, List<Dish>> groupingByDishType(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(Dish::getType));
    }

    // grouping by calorie
    public static enum CaloricLevel {
        DIET, NORMAL, FAT
    }

    public static Map<CaloricLevel, List<Dish>> groupingByCalorie(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(d -> {
            if (d.getCalories() < 300) return CaloricLevel.DIET;
            if (d.getCalories() < 600) return CaloricLevel.NORMAL;
            return CaloricLevel.FAT;
        }));
    }

    public static Map<Dish.Type, List<Dish>> caloricDishesByType(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(
                Dish::getType,
                Collectors.filtering(d -> d.getCalories() > 500, Collectors.toList())
        ));
    }

    // multi-level grouping
    public static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(
                Dish::getType,
                Collectors.groupingBy(d -> {
                    if (d.getCalories() < 300) return CaloricLevel.DIET;
                    if (d.getCalories() < 600) return CaloricLevel.NORMAL;
                    return CaloricLevel.FAT;
                }, Collectors.toList())
        ));
    }

    // count by Type
    public static Map<Dish.Type, Long> dishCountByType(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(
                Dish::getType,
                counting()
        ));
    }

    // max by Type without Optional
    public static Map<Dish.Type, Dish> maxDishByType(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(
                Dish::getType,
                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Dish::getCalories)), Optional::get)
        ));
    }

    public static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.partitioningBy(
                Dish::isVegetarian,
                Collectors.groupingBy(Dish::getType)
                ));
    }

    public static boolean isPrime(int cand) {
        return IntStream.rangeClosed(2, (int) Math.sqrt((double) cand))
                .noneMatch(i -> cand % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int upto) {
        return IntStream.rangeClosed(2, upto)
                .boxed() // boxing int -> Integer
                .collect(Collectors.partitioningBy(ChapterSix::isPrime));
    }

    public static class MyArrayList<T> extends ArrayList<T> {
        public String name;
        public MyArrayList() {
            super();
            name = "random" + Integer.toString(Math.abs(new SecureRandom().nextInt() % 100));
            log.info("new called : " + name);
        }
    }

    // toList Collector Implementation
    public static class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

        @Override
        public Supplier<List<T>> supplier() {
            log.info("NEW CONTAINER IS CREATED");
            return MyArrayList::new;
        }

        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return (List<T> l, T e) -> {
                log.info("ACC : " + ((MyArrayList)l).name);
                l.add(e);
            };
        }

        @Override
        public BinaryOperator<List<T>> combiner() {
            return (l1, l2) -> {
                log.info("COMBINE CALLED" + ((MyArrayList)l1).name + ", " + ((MyArrayList)l2).name);
                log.info("COMBINE : " + ((MyArrayList)l1).name + " : " + l1.toString());
                log.info("COMBINE : " + ((MyArrayList)l2).name + " : " + l2.toString());
                l1.addAll(l2);
                return l1;
            };
        }

        @Override
        public Function<List<T>, List<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            Set<Characteristics> set = new HashSet<>();
            set.add(Characteristics.UNORDERED);
            return set;
        }
    }

    // refactoring with custom collector
    public static boolean isPrime(List<Integer> primes, int cand) {
        int candRoot = (int) Math.sqrt((double) cand);
        return primes.stream()
                .takeWhile(i -> i <= candRoot)
                .noneMatch(i -> cand % i == 0);
    }

    public static class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

        @Override
        public Supplier<Map<Boolean, List<Integer>>> supplier() {
            return () -> {
                Map sup = new HashMap();
                sup.put(true, new ArrayList<>());
                sup.put(false, new ArrayList<>());
                return sup;
            };
        }

        @Override
        public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
            return (m, i) -> {
                m.get(isPrime(m.get(true), i)).add(i);
            };
        }

        @Override
        public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
            return (Map<Boolean, List<Integer>> m1, Map<Boolean, List<Integer>> m2) -> {
                m1.get(true).addAll(m2.get(true));
                m1.get(false).addAll(m2.get(false));
                return m1;
            };
        }

        @Override
        public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
        }
    }

    // primes with custom collector
    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int upto) {
        return IntStream.rangeClosed(2, upto)
                .boxed() // boxing int -> Integer
                .collect(new PrimeNumbersCollector());
    }

    public static void run() {
        List<Transaction> transactions = initTransactions();

        System.out.println(transactionsByYear(transactions));
        System.out.println(transactionsByYearWithStream(transactions));

        List<Dish> dishes = Dish.randomDishGenerator(10);

        // counting
        long dishCount = dishes.stream().collect(Collectors.counting());
        System.out.println(dishCount);

        // max, min
        Optional<Dish> maxCaloricDish = dishes.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
        maxCaloricDish.ifPresentOrElse(d -> System.out.println(d.toString()), () -> System.out.println("Empty!"));

        Optional<Dish> minCaloricDish = dishes.stream().collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)));
        minCaloricDish.ifPresentOrElse(d -> System.out.println(d.toString()), () -> System.out.println("Empty!"));

        /**
         * summarization
         */

        int totalCalories = dishes.stream().collect(Collectors.summingInt(Dish::getCalories));
        double averageCalorie = dishes.stream().collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println(String.format("Total : %d, Avg : %f", totalCalories, averageCalorie));

        IntSummaryStatistics dishStatistics = dishes.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(dishStatistics.toString());

        /**
         * joining
         */
        String dishNames = dishes.stream().map(Dish::getName).collect(Collectors.joining(","));
        System.out.println(dishNames);

        // sum with reducing
        int totalCaloriesWithReducing = dishes.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println(totalCaloriesWithReducing);

        // max withd reducing
        Optional<Dish> maxCaloricDishWithReducing = dishes.stream().collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        maxCaloricDishWithReducing.ifPresentOrElse(d -> System.out.println(d.toString()), () -> System.out.println("Empty!"));

        // collect and reduce - toList with stream.reduce (not recommended)
        // <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
        // reduce with one or two parameters receive BinaryOperator ( T,T -> T )
        Stream<Integer> integerStream = Arrays.asList(1,2,3,4,5).stream();
        List<Integer> numbers = integerStream.reduce(new ArrayList<Integer>(), (List<Integer> l, Integer e) -> {
            l.add(e);
            return l;
        }, (List<Integer> l1, List<Integer> l2) -> {
            l1.addAll(l2);
            return l1;
        });

        // counting with reducing
        long dishCountWithReducing = dishes.stream().collect(counting());
        System.out.println("Count : " + dishCountWithReducing);

        // grouping by type using groupingBy
        System.out.println(groupingByDishType(dishes).toString());

        // grouping by caloricLevel
        System.out.println(groupingByCalorie(dishes).toString());

        // grouping by filtered
        System.out.println(caloricDishesByType(dishes).toString());

        // grouping multi-level
        System.out.println(dishesByTypeCaloricLevel(dishes));

        // count by type
        System.out.println(dishCountByType(dishes));

        // partitioning - vegetarian
        System.out.println(vegetarianDishesByType(dishes));

        // Primes
        System.out.println(partitionPrimes(20));



        System.out.println(dishes.parallelStream().collect(new ToListCollector()));

        // primes with custom collector
        System.out.println(partitionPrimesWithCustomCollector(20));



    }
}
