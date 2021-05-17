package chapters;

import java.util.Arrays;
import java.util.List;

import objects.shapes.*;

public class ChapterThirteen {
	public static void paint(List<Resizable> resizables) {
		resizables.forEach(r -> System.out.println(String.format("w : %d, h : %d", r.getWidth(), r.getHeight())));
	}

	public interface A {
		default void hello() {
			System.out.println("Hello from A");
		}
	}

	public static class A_Sub implements A {

	}

	public static class AClass {
		public void hello() {
			System.out.println("Hello from AClass");
		}
	}

	public interface  B extends A {
		default void hello() {
			System.out.println("Hello from B");
		}
	}

	public static class C implements A, B {

	}

	public static class D extends AClass implements A {

	}

	public static class E extends A_Sub implements B {

	}

	public interface X {
		default void hello() {
			System.out.println("Hello from X");
		}
	}

	public interface Y {
		default void hello() {
			System.out.println("Hello from Y");
		}
	}

	public class Z implements X, Y {

		@Override
		public void hello() {
			X.super.hello();
		}
	}

	public interface Root {
		default void hello() {
			System.out.println("Hello From Root");
		}
	}

	public interface Left extends Root { }
	public interface Right extends Root { }
	public static class Leaf implements Left, Right {
	}

	public static void run() {
		List<Resizable> resizables = Arrays.asList(new Ellipse(), new Rectangle(), new Square());
		paint(resizables);

		Monster m = new Monster(); 	// Monster is the class implementing Resizable, Rotatable, Movable interfaces
		System.out.println(m);
		m.setAbsoluteSize(10, 10);
		m.rotateBy(180);
		m.moveVertically(10);
		System.out.println(m);

		Sun s = new Sun(); 			// Sun is the class implementing Rotatable, Movable
		System.out.println(s);
		s.rotateBy(180);
		s.moveVertically(90);
		System.out.println(s);

		C c = new C();
		c.hello();

		D d = new D();
		d.hello();

		E e = new E();
		e.hello();

		Leaf leaf = new Leaf();
		leaf.hello();

	}
}
