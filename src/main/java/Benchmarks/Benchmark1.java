package Benchmarks;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class Benchmark1 {
    @Benchmark
    @Fork(value = 1)
    @Timeout(time=60)
    public void Bench(Blackhole blackhole) {
        int i =5;
        blackhole.consume(i);
    }
}
