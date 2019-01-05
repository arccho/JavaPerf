package org.polytechtours.performance.tp.fourmispeintre;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/*
    Classe permettant d'executer le deplacement
 */
public class ExecutionHandler implements Callable {
    private CyclicBarrier barriere;
    CFourmi fourmi;

    ExecutionHandler(CyclicBarrier barrier, CFourmi fourmi) {
        barriere = barrier;
        this.fourmi = fourmi;
    }


    @Override
    public Object call() throws Exception {
        fourmi.deplacer();

        barriere.await();

        return null;
    }
}
