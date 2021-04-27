import static org.junit.jupiter.api.Assertions.*;

import chapters.ChapterOneAndTwo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ChapterOneAndTwoTest {
    @Test
    public void testFilter() {
        List<Integer> numbers = Arrays.asList(1,2,3,4);
        List<Integer> expectedEven = Arrays.asList(2,4);
        List<Integer> expectedSmallerThanThree = Arrays.asList(1,2);

         List<Integer> even = ChapterOneAndTwo.filter(numbers, n -> n % 2 == 0);
         List<Integer> smallerThanThree = ChapterOneAndTwo.filter(numbers, n -> n < 3);

         assertEquals(expectedEven, even);
         assertEquals(expectedSmallerThanThree, smallerThanThree);

    }
}
