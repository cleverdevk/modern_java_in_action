package chapters;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@BenchmarkMode(Mode.AverageTime) // Measures the average time taken
@OutputTimeUnit(TimeUnit.MILLISECONDS) // print result using milliseconds
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"}) // with 4Gb of heap space and benchmark 2 times to increase reliability
@State(Scope.Benchmark)
public class ChapterSevenBenchmark {
    private static final long N = 10_000_000L; // "_" is just for readability


    @Benchmark
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long sequentialSumWithParallelStream() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .parallel() // can set to sequential with sequential()
                .reduce(0L, Long::sum);
    }

    @TearDown(Level.Invocation) // tries to run the gc after each iteration of benchmark
    public void tearDown() {
        System.gc();
    }
}
