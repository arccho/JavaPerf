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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CColonie implements Runnable {

  private Boolean mContinue = Boolean.TRUE;
  private Vector<CFourmi> mColonie;
  private PaintingAnts mApplis;

  // gestion des fourmis
  private int id;
  // Creation d'un pool de threads
  private ExecutorService executor = new ThreadPoolExecutor(4, 8, 2,
          TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

  //private ThreadPoolExecutor pool = new ThreadPoolExecutor();

  /** Creates a new instance of CColonie */
  public CColonie(Vector<CFourmi> pColonie, PaintingAnts pApplis) {
    mColonie = pColonie;
    mApplis = pApplis;

    id = 0;
  }

  public void pleaseStop() {
    mContinue = false;
  }

/*
    Lecture et ecriture de l'id de la fourmi dont
    la trajectoire doit etre calculee. Chaque thread y accede de façon
    sequentielle
 */
  public synchronized int lireIdFourmi() {
      int val;

      if (id == 20) id = 0;
      val = id;
      id++;

      return val;
  }

  @Override
  public void run() {

    while (mContinue == true) {
      if (!mApplis.getPause()) {
        for (int i = 0; i < mColonie.size(); i++) {
          mColonie.get(i).deplacer();
        }
      } else {
        /*
         * try { Thread.sleep(100); } catch (InterruptedException e) { break; }
         */

      }
    }
  }

}
