package chapters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class ChapterOneAndTwo {
    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Apple {
        enum Color {
            RED, GREEN
        }

        private final int weight;
        private final Color color;

        // before java 8 - filter
        public static List<Apple> filterGreenApples(List<Apple> apples) {
            List<Apple> greenApples = new ArrayList<>();

            for(Apple apple : apples) {
                if(Color.GREEN.equals(apple.getColor())) {  // differ from below function on only this section.
                    greenApples.add(apple);
                }
            }

            return greenApples;
        }

        // before java 8 - filter
        public static List<Apple> filterHeavyApples(List<Apple> apples) {
            List<Apple> heavyApples = new ArrayList<>();
            for (Apple apple : apples) {
                if (apple.getWeight() > 5) {    // differ from above function on only this section.
                    heavyApples.add(apple);
                }
            }
            return heavyApples;
        }

        // after java 8 - filter
        public static boolean isGreen(Apple apple) {
            return Color.GREEN.equals(apple.getColor());
        }

        // after java 8 - filter
        public static boolean isHeavy(Apple apple) {
            return apple.getWeight() > 5;
        }

        // after java 8 - filter
        static List<Apple> filterApples(List<Apple> apples, Predicate<Apple> p) {
            List<Apple> filteredApples = new ArrayList<>();
            for(Apple apple : apples) {
                if(p.test(apple)) {
                    filteredApples.add(apple);
                }
            }
            return filteredApples;
        }

        static <T> List<T> filter(List<T> c, Predicate p) {
            List<T> result = new ArrayList<>();

            for(T t : c) {
                if(p.test(t)) {
                    result.add(t);
                }
            }
            return result;
        }

        // strategy pattern
        public interface ApplePredicate {
            boolean test (Apple apple);
        }

        public static class AppleHeavyPredicate implements ApplePredicate {
            public boolean test(Apple apple) {
                return apple.getWeight() > 5;
            }
        }

        public static class AppleGreenPredicate implements ApplePredicate {
            public boolean test(Apple apple) {
                return Color.GREEN.equals(apple.getColor());
            }
        }

        public static List<Apple> filterApplesWithStrategyPattern(List<Apple> apples, ApplePredicate applePredicate) {
            List<Apple> filteredApples = new ArrayList<>();
            for (Apple apple : apples) {
                if(applePredicate.test(apple)) {
                    filteredApples.add(apple);
                }
            }
            return filteredApples;
        }

        // quiz 2-1
        public static void prettyPrintApple(List<Apple> apples, PrintFormat printFormat) {
            for(Apple apple : apples) {
                String output = printFormat.getString(apple);
                System.out.println(output);
            }
        }

        public interface PrintFormat {
            public String getString(Apple apple);
        }

        public static class SimplePrintFormat implements PrintFormat {
            public String getString(Apple apple) {
                return String.format("Apple(weight=%d, color=%s)", apple.getWeight(), apple.getColor().toString());
            }
        }

        public static class StarPrintFormat implements PrintFormat {
            public String getString(Apple apple) {
                return String.format("*Apple(*weight=%d, *color=%s)*", apple.getWeight(), apple.getColor().toString());
            }
        }
    }

    public static List<Apple> appleGenerator(int count) {
        List<Apple> apples = new ArrayList<>();
        Random random = new SecureRandom();

        for (int i = 0; i < count/2; i++) {
            apples.add(new Apple(random.nextInt(10), Apple.Color.RED));
            apples.add(new Apple(random.nextInt(10), Apple.Color.GREEN));
        }

        if (count % 2 != 0) {
            apples.add(new Apple(random.nextInt(10), Apple.Color.RED));
        }

        return apples;
    }

    public static void run() {
        List<Apple> apples = appleGenerator(10);
        List<Apple> apples2 = appleGenerator(10);

        // before java 8 - sort
        Collections.sort(apples, new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return Integer.compare(o1.getWeight(), o2.getWeight());
            }
        });

        // after java 8 - sort
        apples2.sort(Comparator.comparing(Apple::getWeight));

        System.out.println(apples.toString());
        System.out.println(apples2.toString());

        // before java 8 - filter : has code duplication!
        List<Apple> greenApples = Apple.filterGreenApples(apples);
        List<Apple> heavyApples = Apple.filterHeavyApples(apples);

        System.out.println(greenApples.toString());
        System.out.println(heavyApples.toString());

        // after java 8 - filter(predicate)
        List<Apple> greenApplesByPredicate = Apple.filterApples(apples, Apple::isGreen);
        List<Apple> heavyApplesByPredicate = Apple.filterApples(apples, Apple::isHeavy);

        System.out.println(greenApplesByPredicate.toString());
        System.out.println(heavyApplesByPredicate.toString());

        // after java 8 - filter(lambda)
        List<Apple> greenApplesByPredicateLambda = Apple.filterApples(apples, (Apple a) -> Apple.Color.GREEN.equals(a.getColor()));
        List<Apple> heavyApplesByPredicateLambda = Apple.filterApples(apples, (Apple a) -> a.getWeight() > 5);
        List<Apple> greenHeavyApples = Apple.filterApples(apples, (Apple a) -> a.getWeight() > 5 && Apple.Color.GREEN.equals(a.getColor()));

        System.out.println(greenApplesByPredicateLambda.toString());
        System.out.println(heavyApplesByPredicateLambda.toString());
        System.out.println(greenHeavyApples.toString());


        List<Apple> soManyApples = appleGenerator(20);

        // not use stream
        Map<Apple.Color, List<Apple>> applesByColor = new HashMap<>();
        for (Apple apple : soManyApples) {
            if (apple.getWeight() > 5) {
                Apple.Color color = apple.getColor();
                List<Apple> appleForColor = applesByColor.get(color);
                if (appleForColor == null) {
                    appleForColor = new ArrayList<>();
                    applesByColor.put(color, appleForColor);
                }
                appleForColor.add(apple);
            }
        }

        System.out.println(applesByColor);

        // use stream
        Map<Apple.Color, List<Apple>> applesByColorUseStream =
                soManyApples.stream()
                        .filter((Apple a) -> a.getWeight() > 5)
                        .collect(java.util.stream.Collectors.groupingBy(Apple::getColor));

        System.out.println(applesByColorUseStream);

        // strategy pattern
        List<Apple> heavyApplesByStrategyPattern = Apple.filterApplesWithStrategyPattern(apples, new Apple.AppleHeavyPredicate());
        List<Apple> greenApplesByStrategyPattern = Apple.filterApplesWithStrategyPattern(apples, new Apple.AppleGreenPredicate());

        System.out.println(heavyApplesByStrategyPattern.toString());
        System.out.println(greenApplesByStrategyPattern.toString());

        // quiz 2-1
        Apple.prettyPrintApple(apples, new Apple.SimplePrintFormat());
        Apple.prettyPrintApple(apples, new Apple.StarPrintFormat());

        // use anonymous class
        List<Apple> heavyApplesByAnonymousClass = Apple.filterApplesWithStrategyPattern(apples, new Apple.ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getWeight() > 5;
            }
        });

        System.out.println(heavyApplesByAnonymousClass.toString());
    }
}
