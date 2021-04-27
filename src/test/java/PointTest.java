import static org.junit.jupiter.api.Assertions.*;

import objects.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PointTest {

    @Test
    public void testMoveRightBy() throws Exception {
        Point p1 = new Point(5, 5);
        Point p2 = p1.moveRightBy(10);
        assertEquals(15, p2.getX());
        assertEquals(5, p2.getY());
    }

    @Test
    public void testComparingTwoPoints() throws Exception {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(5, 10);

        int result = Point.compareByXAndThenY.compare(p1, p2);
        assertTrue(result < 0);
    }

    @Test
    public void testMoveAllPointsRightBy() throws Exception {
        List<Point> points = Arrays.asList(new Point(0, 1), new Point(1, 2), new Point(2, 3));
        List<Point> movedPoints = Point.moveAllPointsRightBy(points, 10);
        List<Point> expected = Arrays.asList(new Point(10, 1), new Point(11, 2), new Point(12, 3));

        assertEquals(movedPoints, expected);
    }
}
