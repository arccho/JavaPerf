package Benchmarks;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

public class Benchmark1 {
    /*@Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 3)
    public void BenchValueOf(Blackhole blackhole) {
        String s = "12345";
        float f = Float.valueOf(s).floatValue();
        blackhole.consume(f);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 3)
    public void BenchParseOf(Blackhole blackhole) {
        String s = "12345";
        float f = Float.parseFloat(s);
        blackhole.consume(f);
    }*/

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 3)
    public void BenchMathRand(Blackhole blackhole) {
        float f = (float) (Math.random() * (100 - 10)) + 10;
        blackhole.consume(f);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 3)
    public void BenchRand(Blackhole blackhole) {
        float f = 10 +  new Random().nextFloat() * (100 - 10);
        blackhole.consume(f);
    }
}
