import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static void run() {
        List<Transaction> transactions = initTransactions();

        System.out.println(transactionsByYear(transactions));
        System.out.println(transactionsByYearWithStream(transactions));

        List<Dish> dishes = Dish.randomDishGenerator(20);

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


    }
}
