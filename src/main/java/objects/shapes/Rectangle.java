package objects.shapes;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import objects.shapes.Resizable;

@AllArgsConstructor
public class Rectangle implements Resizable {
	private int width;
	private int height;

	public Rectangle() {
		this.width = 5;
		this.height = 5;
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
}
