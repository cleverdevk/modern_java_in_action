import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<ChapterOne.Apple> apples = ChapterOne.appleGenerator(10);
        List<ChapterOne.Apple> apples2 = ChapterOne.appleGenerator(10);

        // before java 8
        Collections.sort(apples, new Comparator<ChapterOne.Apple>() {
            @Override
            public int compare(ChapterOne.Apple o1, ChapterOne.Apple o2) {
                return Integer.compare(o1.getWeight(), o2.getWeight());
            }
        });

        // after java 8
        apples2.sort(Comparator.comparing(ChapterOne.Apple::getWeight));

        System.out.println(apples.toString());
        System.out.println(apples2.toString());
    }
}
