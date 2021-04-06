import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.security.SecureRandom;
import java.util.*;

@Getter
@ToString
@AllArgsConstructor
public class Dish {
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
}

