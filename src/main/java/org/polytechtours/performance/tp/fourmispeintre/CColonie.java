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

  // gestion des fourmis
  private int id;
  private int maxNbFourmis;
  // Creation d'un pool de threads
  //private ExecutorService executor = new ThreadPoolExecutor(10, 20, 20,
          //TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
  private ExecutorService executor = Executors.newFixedThreadPool(20);
  private CyclicBarrier barrier;



  /** Creates a new instance of CColonie */
  public CColonie(Vector<CFourmi> pColonie, PaintingAnts pApplis) {
    mColonie = pColonie;
    mApplis = pApplis;

    id = 0;
    maxNbFourmis = pColonie.size();
    barrier = new CyclicBarrier(maxNbFourmis);
  }

  public void pleaseStop() {
    mContinue = false;
  }

/*
    Lecture et ecriture de l'id de la fourmi dont
    la trajectoire doit etre calculee. Chaque thread y accede de façon
    sequentielle
 */
  private synchronized int lireIdFourmi() {
      int val;

      if (id == maxNbFourmis) id = 0;
      val = id;
      id++;

      return val;
  }

  // Dans le cas où les taches executees sont annulees, on reprend
    // a partir de l'id ou on s'est arrete
  private void resetIdFourmi() {
      if (id == 0) id = maxNbFourmis - 1;
      else id -= 1;
  }

  @Override
  public void run() {

    while (mContinue == true) {
      if (!mApplis.getPause()) {
          for (CFourmi fourmi: mColonie) {
              Future futur = executor.submit(new ExecutionHandler(barrier, fourmi));
              futur = null;
          }

          /*executor.submit(new Runnable() {
              @Override
              public void run() {
                  int idFourmi = lireIdFourmi();
                  mColonie.get(idFourmi).deplacer();

                  try {
                      barrier.await();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } catch (BrokenBarrierException e) {
                      e.printStackTrace();
                  }
              }
          });*/
      } else {
          // Dans le cas où l'application se met en pause, il faut arreter les taches en cours
          //    et liberer les ressources utilisees
          executor.shutdownNow();
          //resetIdFourmi();
      }
    }
    // Si on sort du tant que, il faut que les ressources utilisées par le pool soit libérées
      executor.shutdown();
  }

}
