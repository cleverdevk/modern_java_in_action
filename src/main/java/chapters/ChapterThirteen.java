package chapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import objects.Ellipse;
import objects.Rectangle;
import objects.ResizableV1;
import objects.Square;

public class ChapterThirteen {
	public static void paint(List<ResizableV1> resizableV1s) {
		resizableV1s.forEach(r -> System.out.println(String.format("w : %d, h : %d", r.getWidth(), r.getHeight())));
	}


	public static void run() {
		List<ResizableV1> resizables = Arrays.asList(new Ellipse(), new Rectangle(), new Square());
		paint(resizables);
	}
}
