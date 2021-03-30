import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChapterFour {
    @Getter
    @ToString
    @AllArgsConstructor
    public static class Dish {
        private String name;
        private int calories;
        private boolean vegetarian;
        private Type type;

        public enum Type {
            MEAT, FISH, OTHER
        }

        private static final List<Type> TYPES = Collections.unmodifiableList(Arrays.asList(Type.values()));
        private static final int size = TYPES.size();
        private static final Random random = new SecureRandom();
        public static Type randomType() {
            return TYPES.get(random.nextInt(size));
        }
    }

    public static List<Dish> randomDishGenerator(int count) {
        List<Dish> dishes = new ArrayList<>();
        Random random = new SecureRandom();

        for (int i = 0; i < count; i++) {
            dishes.add(new Dish("dish" + random.nextInt(100),
                    random.nextInt(1000),
                    random.nextBoolean(),
                    Dish.randomType()));
        }

        return dishes;
    }

    public static List<String> lowCaloriesDishNames(List<Dish> dishes) {
        List<Dish> lowCaloriesDishes = new ArrayList<>();
        for(Dish dish : dishes) {
            if(dish.getCalories() < 400) {
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
        for(Dish dish : lowCaloriesDishes) {
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

    public static void run() {
        List<Dish> dishes = randomDishGenerator(10);

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
        dishStream.forEach(System.out::println);
        // dishStream.forEach(System.out::println); // illegalStateError

        System.out.println(threeHighCaloricDishNamesWithLog(dishes));






    }
}
