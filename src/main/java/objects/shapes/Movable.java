package objects.shapes;

public interface Movable {
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);

    default void moveHorizontally(int d) {
        setX(getX() + d);
    }

    default void moveVertically(int d) {
        setY(getY() + d);
    }
}
