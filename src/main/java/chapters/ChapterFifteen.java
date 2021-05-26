package chapters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ChapterFifteen {
	public static ExecutorService executorService = Executors.newFixedThreadPool(2);

	public static void run() throws InterruptedException, ExecutionException {
		int x = 1337;
		Result result = new Result();

		// using thread
		Thread t1 = new Thread(() -> {result.setLeft(f(x));});
		Thread t2 = new Thread(() -> {result.setRight(g(x));});

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		System.out.println(result.getLeft() + result.getRight());

		// use Future API
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Future<Integer> y = executorService.submit(() -> f(x));
		Future<Integer> z = executorService.submit(() -> g(x));

		System.out.println(y.get() + z.get());

		Future<Integer> refactoredY = refactoredF(x);
		Future<Integer> refactoredZ = refactoredG(x);

		System.out.println(refactoredY.get() + refactoredZ.get());

		// Reactive - callback
		Result result2 = new Result();

		fCallbackType(x, (int xx) -> {
			result2.setLeft(xx);
			System.out.println(result2.getLeft() + result2.getRight());
		});
		fCallbackType(x, (int xx) -> {
			result2.setRight(xx);
			System.out.println(result2.getLeft() + result2.getRight());
		});

		// sleep waste system resource
		work1();
		Thread.sleep(3);
		work2();

		// use ScheduledExecutorService
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

		work1();
		scheduledExecutorService.schedule(ChapterFifteen::work2, 3, TimeUnit.SECONDS);

		// 			[q1]
		//		   /	\
		// r -> [p]		 [r] ->
		//		   \	/
		//			[q2]

		// case1 : no parallelism
		int t = p(x);
		System.out.println(r(q1(t), q2(t)));

		// case2 : future
		int tt = p(x);
		Future<Integer> a1 = executorService.submit(() -> q1(tt));
		Future<Integer> a2 = executorService.submit(() -> q2(tt));

		System.out.println(r(a1.get(), a2.get()));


		// CompletableFuture
		CompletableFuture<Integer> a = new CompletableFuture<>();
		executorService.submit(() -> a.complete(f(x)));
		int b = g(x);
		System.out.println("CompletableFuture : " + (a.get() + b));  // resource can be wasted for waiting a.get()

		// CompletableFuture thenCombine
		CompletableFuture<Integer> aa = new CompletableFuture<>();
		CompletableFuture<Integer> bb = new CompletableFuture<>();
		CompletableFuture<Integer> cc = aa.thenCombine(bb, (p, q) -> p + q);
		executorService.submit(() -> aa.complete(f(x)));
		executorService.submit(() -> bb.complete(g(x)));

		System.out.println("thenCombine : " + cc.get());

		executorService.shutdown();

		// Flow
		SimpleCell c1 = new SimpleCell("C1");
		SimpleCell c2 = new SimpleCell("C2");
		SimpleCell c3 = new SimpleCell("C3");

		c1.subscribe(c3);
		c1.onNext(10);
		c2.onNext(20);

		ArithmeticCell c4 = new ArithmeticCell("Arith C4");
		c1.subscribe(c4::setLeft);
		c2.subscribe(c4::setRight);


	}

	public static int f(int x) {
		return x + 1;
	}
	public static int g(int x) {
		return x * 2;
	}

	public static Future<Integer> refactoredF(int x)  {
		return executorService.submit(() -> x + 1);
	}

	public static Future<Integer> refactoredG(int x) {
		return executorService.submit(() -> x * 2);
	}

	public static void fCallbackType(int x, IntConsumer dealWithResult) {
		dealWithResult.accept(x);
	}

	public static void work1() {
		System.out.println("WORK1");
	}

	public static void work2() {
		System.out.println("WORK2");
	}

	public static int p(int x) {
		return x + 1;
	}

	public static int q1(int x) {
		return x + 10;
	}
	public static int q2(int x) {
		return x + 100;
	}
	public static int r(int x, int y) {
		return x + y;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	public static class Result {
		private int left;
		private int right;
	}

	public static class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {
		private int value = 0;
		private String name;
		private List<Subscriber> subscribers = new ArrayList<>();
		public SimpleCell(String name) {
			this.name = name;
		}

		@Override
		public void subscribe(Subscriber<? super Integer> subscriber) {
			subscribers.add(subscriber);
		}

		private void notifyAllSubscribers() {
			subscribers.forEach(subscriber -> subscriber.onNext(this.value));
		}

		@Override
		public void onNext(Integer newValue) {
			this.value = newValue;
			System.out.println(this.name + " : " + this.value);
			notifyAllSubscribers();
		}
	}

	interface Publisher<T> {
		void subscribe(Subscriber<? super T> subscriber);
	}

	interface Subscriber<T> {
		void onNext(T t);
	}

	public static class ArithmeticCell extends SimpleCell {
		private int left;
		private int right;

		public ArithmeticCell(String name) {
			super(name);
		}

		public void setLeft(int left) {
			this.left = left;
			onNext(left + this.right);
		}

		public void setRight(int right) {
			this.right = right;
			onNext(right + this.left);
		}
	}
}
