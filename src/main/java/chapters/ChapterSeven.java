package chapters;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class ChapterSeven {
    public static class ForkJoinSumCalculator extends RecursiveTask<Long> {
        private final long[] numbers;
        private final int start; // initial position of the subarray processed by this subtask
        private final int end;  // final position
        private static final long THRESHOLD = 10_000; // subtask threshold

        public ForkJoinSumCalculator(long[] numbers) {
            this(numbers, 0, numbers.length);
        }

        public ForkJoinSumCalculator(long[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start;
            log.info("LENGTH : " + length);
            if (length <= THRESHOLD) {
                return computeSequentially();
            }
            ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
            leftTask.fork(); // asynchronously executes the newly created subtask using another thread of fjp
            ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end);
            Long rightResult = rightTask.compute();
            Long leftResult = leftTask.join(); // reads the results or waiting
            return rightResult + leftResult;
        }

        private long computeSequentially() {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
            }
            return sum;
        }

        public static long forkJoinSum(long n) {
            long[] numbers = LongStream.rangeClosed(1, n).toArray();
            ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
            return new ForkJoinPool().invoke(task);
        }
    }

    // iteratively
    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }

    // functional
    @AllArgsConstructor
    public static class WordCounter {
        private final int counter;
        private final boolean lastSpace;

        public WordCounter accumulator(Character c) {
            if (Character.isWhitespace(c)) {
                return lastSpace ? this : new WordCounter(counter, true);
            } else {
                return lastSpace ? new WordCounter(counter+1, false) : this;
            }
        }

        public WordCounter combine(WordCounter wordCounter) {
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }
        public int getCounter() {
            return counter;
        }
    }

    public static class WordCounterSpliterator implements Spliterator<Character> {
        private final String string;
        private int currentChar = 0;

        public WordCounterSpliterator(String string) {
            this.string = string;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {
            action.accept(string.charAt(currentChar++));
            return currentChar < string.length();
        }

        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = string.length() - currentChar;
            if (currentSize < 10) { // 더 이상 분할하지 않겠다.
                return null;
            }
            for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
                if (Character.isWhitespace(string.charAt(splitPos))) {
                    Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
                    currentChar = splitPos;
                    return spliterator;
                }
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return string.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }

    public static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulator, WordCounter::combine);
        return wordCounter.getCounter();
    }

    public static void run() {
        System.out.println(countWordsIteratively("asjdfpiojwa  fajwoeijfa oiwjef f awfj waf awofj             faowijefoiawjefoijaw"));
        String ex = "awjefowaje jf awejf oiawjf aiwjefoajw eofjawo iejfaowije foaiwjefoi ajweofij awoeijf oawej foawej foiawje ofijawoe ifjawoiejf oaiwje";
        Stream<Character> stream = IntStream.range(0, ex.length()).mapToObj(ex::charAt);
        System.out.println(countWords(stream));
        stream = IntStream.range(0, ex.length()).mapToObj(ex::charAt);
        System.out.println("Wrong!! : " + countWords(stream.parallel()));
        Spliterator<Character> spliterator = new WordCounterSpliterator(ex);
        Stream<Character> streamWithSpliterator = StreamSupport.stream(spliterator, true);
        System.out.println("Correct : " + countWords(streamWithSpliterator));

    }
}
