import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChapterOne {
    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Apple {
        private final int weight;
    }

    public static List<Apple> appleGenerator(int count) {
        List<Apple> apples = new ArrayList<>();
        Random random = new SecureRandom();

        for (int i = 0; i < count; i++) {
            apples.add(new Apple(random.nextInt(10)));
        }

        return apples;
    }
}
