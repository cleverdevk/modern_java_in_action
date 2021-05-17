package objects.shapes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Monster implements Rotatable, Movable, Resizable{
    private int x;
    private int y;
    private int width;
    private int height;
    private int angle;

    public Monster() {
        this.x = 5;
        this.y = 5;
        this.width = 5;
        this.height = 5;
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
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setAbsoluteSize(int width, int height) {
        this.width = width;
        this.height = height;
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
