package org.polytechtours.performance.tp.fourmispeintre;

/*
 * CColonie.java
 *
 * Created on 11 avril 2007, 16:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.util.Vector;
import java.util.concurrent.*;

public class CColonie implements Runnable {

  private Boolean mContinue = Boolean.TRUE;
  private Vector<CFourmi> mColonie;
  private PaintingAnts mApplis;

  // Creation d'un pool de threads
  private ExecutorService executor;
  private int NbFourmis;
  private CyclicBarrier barrier;


  /** Creates a new instance of CColonie */
  public CColonie(Vector<CFourmi> pColonie, PaintingAnts pApplis) {
    mColonie = pColonie;
    mApplis = pApplis;
    NbFourmis = pColonie.size();
    executor = Executors.newFixedThreadPool(NbFourmis);


    barrier = new CyclicBarrier(NbFourmis);
  }

  public void pleaseStop() {
    mContinue = false;
  }

  @Override
  public void run() {

    if (!mApplis.getPause()) {

      for (int i=0; i < NbFourmis; i++) {

        executor.execute(new Runnable() {
          @Override
          public void run() {
            while (mContinue == true) {
              mColonie.get((int)Thread.currentThread().getId()%NbFourmis).deplacer();
              try {
                barrier.await();
              } catch (InterruptedException e) {
                e.printStackTrace();
              } catch (BrokenBarrierException e) {
                e.printStackTrace();
              }
            }
          }
        });
      }

      // Dans le cas où l'application se met en pause, il faut arreter les taches en cours
      //    et liberer les ressources utilisees
    }
    // Si on sort du tant que, il faut que les ressources utilisées par le pool soit libérées
    executor.shutdown();
  }

}