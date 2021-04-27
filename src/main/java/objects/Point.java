package objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Point {
    private final int x;
    private final int y;

    public final static Comparator<Point> compareByXAndThenY =
            Comparator.comparing(Point::getX).thenComparing(Point::getY);

    public Point moveRightBy(int dx) {
        return new Point(x + dx, y);
    }

    public static List<Point> moveAllPointsRightBy(List<Point> points, int dx) {
        return points.stream().map(p -> new Point(p.getX() + dx, p.getY())).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}
