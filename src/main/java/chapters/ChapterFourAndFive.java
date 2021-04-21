package chapters;

import objects.Dish;
import objects.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChapterFourAndFive {

    public static List<String> lowCaloriesDishNames(List<Dish> dishes) {
        List<Dish> lowCaloriesDishes = new ArrayList<>();
        for (Dish dish : dishes) {
            if (dish.getCalories() < 400) {
                lowCaloriesDishes.add(dish);
            }
        }
        Collections.sort(lowCaloriesDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        List<String> lowCaloriesDishNames = new ArrayList<>();
        for (Dish dish : lowCaloriesDishes) {
            lowCaloriesDishNames.add(dish.getName());
        }

        return lowCaloriesDishNames;
    }

    public static List<String> lowCaloriesDishNamesWithStream(List<Dish> dishes) {
        return dishes.parallelStream()
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    public static List<String> threeHighCaloricDishNames(List<Dish> dishes) {
        return dishes.stream()                      // get stream() from list
                .filter(d -> d.getCalories() > 300) // pipelining
                .map(Dish::getName)                 // pipelining
                .limit(3)                           // get 3 output
                .collect(Collectors.toList());      // save outputs as a list
    }

    public static List<String> threeHighCaloricDishNamesWithLog(List<Dish> dishes) {
        return dishes.stream()
                .filter(d -> {
                    System.out.println("filter : " + d.getName() + " calories : " + d.getCalories());
                    return d.getCalories() > 300;
                })
                .map(d -> {
                    System.out.println("map : " + d.getName());
                    return d.getName();
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    public static class Trader {
        private final String name;
        private final String city;

        public Trader(String n, String c) {
            this.name = n;
            this.city = c;
        }

        public String getName() {
            return this.name;
        }

        public String getCity() {
            return this.city;
        }

        public String toString() {
            return "Trader:" + this.name + " in " + this.city;
        }
    }

    public static void practicalExample() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        // 1. 2011년에 일어난 모든 트랜잭션 오름차순
        List<Integer> values = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .map(Transaction::getValue).sorted().collect(Collectors.toList());

        System.out.println(values);

        // 2. 거래자가 근무하는 모든 도시를 중복 없이
        List<String> cities = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());

        System.out.println(cities);

        // 3. 케임브리지에서 근무하는 거래자 이름순 정렬
        List<Trader> traders = transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getTrader)
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        System.out.println(traders);

        // 4. 모든 거래자의 이름 알파벳 순으로 정렬
        List<String> traderNames = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        System.out.println(traderNames);

        // 5. 밀라노에 거래자가 있는가?
        transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Milan"))
                .findAny()
                .ifPresentOrElse(System.out::println, () -> System.out.println("NO"));

        // 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하라.
        List<Transaction> cambridgeTransactions = transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .collect(Collectors.toList());

        System.out.println(cambridgeTransactions);

        // 7. 전체 트랜잭션 중 최대값
        transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max)
                .ifPresentOrElse(v -> System.out.println("MAX VALUE : " + Integer.toString(v)), () -> System.out.println("STREAM IS EMPTY!!"));

        // 8. 최솟값
        transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min)
                .ifPresentOrElse(v -> System.out.println("MIN VALUE : " + Integer.toString(v)), () -> System.out.println("STREAM IS EMPTY!!"));
    }

    public static void pythagoras() {
        List<int[]> numbers = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}))
                .collect(Collectors.toList());

        numbers.forEach(n ->
                System.out.println(String.format("(%s,%s,%s)", n[0], n[1], n[2])));
    }


    public static void run() {
        List<Dish> dishes = Dish.randomDishGenerator(100);

        // parallelStream benchmark
        long beforeTime = System.currentTimeMillis();
        lowCaloriesDishNames(dishes);
        long afterTime = System.currentTimeMillis();
        lowCaloriesDishNamesWithStream(dishes);
        long afterAfterTime = System.currentTimeMillis();

        System.out.println((afterTime - beforeTime) / 1000.0);
        System.out.println((afterAfterTime - afterTime) / 1000.0);

        System.out.println(threeHighCaloricDishNames(dishes));


        Stream<Dish> dishStream = dishes.stream();
        // dishStream.forEach(System.out::println);
        // dishStream.forEach(System.out::println); // illegalStateError

        // System.out.println(threeHighCaloricDishNamesWithLog(dishes));

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1);
        numbers.stream()
                .filter(n -> n % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        List<Dish> identicalDishes = new ArrayList<>();
        Dish sampleDish = new Dish("bbb", 22, false, Dish.Type.MEAT);
        identicalDishes.add(new Dish("aaa", 15, false, Dish.Type.FISH));
        identicalDishes.add(new Dish("aaa", 15, false, Dish.Type.FISH));
        identicalDishes.add(sampleDish);
        identicalDishes.add(sampleDish);

        identicalDishes.stream().distinct().forEach(System.out::println);

        List<Dish> sortedDishes = dishes
                .stream()
                .sorted(Comparator.comparing(d -> d.getCalories()))
                .collect(Collectors.toList());

        List<Dish> lowCaloricDishes = sortedDishes
                .stream()
                .takeWhile(dish -> dish.getCalories() < 1)
                .collect(Collectors.toList());

        System.out.println(lowCaloricDishes);

        List<String> words = Arrays.asList("Hello", "World");
        List<String> letters = words
                .stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(letters);

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<int[]> pairs = numbers1.stream()
                .map(n -> numbers2.stream().map(n2 -> new int[]{n, n2}).collect(Collectors.toList()))
                .flatMap(List::stream)
                .filter(p -> p[0] + p[1] == 5)
                .collect(Collectors.toList());

        pairs.forEach(i -> System.out.println(String.format("%d,%d", i[0], i[1])));

        Optional<Dish> dish = dishes.stream()
                .filter(Dish::isVegetarian)
                .findAny();

        System.out.println(dish.get().toString());


        // reduce
        int caloriesSum = dishes.stream().map(d -> d.getCalories()).reduce(0, Integer::sum);
        System.out.println(caloriesSum);

        Optional<Integer> maxCalorie = dishes.stream().map(Dish::getCalories).reduce(Integer::max);
        maxCalorie.ifPresent(System.out::println);

        practicalExample();

        pythagoras();

        // without Stream.ofNullable
        String homeValue = System.getProperty("home");
        Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(homeValue);

        // with Stream.of
        Stream<String> homeValueStreamByStreamOf = Stream.ofNullable(homeValue);

        Stream<String> values = Stream.of("config", "home", "user", "user.home")
                .flatMap(key -> Stream.ofNullable(System.getProperty(key)));

        values.forEach(System.out::println);

        // fibonacci
        Stream.iterate(new int[]{0, 1}, x -> new int[]{x[1], x[0] + x[1]})
                .limit(10)
                .forEach(x -> System.out.println(x[0]));


    }
}
