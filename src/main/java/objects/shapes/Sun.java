package objects.shapes;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Sun implements Movable, Rotatable{
    private int x;
    private int y;
    private int angle;

    public Sun() {
        this.x = 5;
        this.y = 5;
        this.angle = 90;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setRotationAngle(int angleInDegrees) {
        this.angle = angleInDegrees;
    }

    @Override
    public int getRotationAngle() {
        return angle;
    }
}
