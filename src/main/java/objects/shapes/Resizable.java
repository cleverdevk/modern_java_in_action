package objects.shapes;

public interface Resizable {
	int getWidth();
	int getHeight();
	void setWidth(int width);
	void setHeight(int height);
	void setAbsoluteSize(int width, int height);
	// void setRelativeSize(int wFactor, int hFactor); <- error occurred because of the classess which not implement this method.
	default void setRelativeSize(int wFactor, int hFactor) {
		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
	}
}
