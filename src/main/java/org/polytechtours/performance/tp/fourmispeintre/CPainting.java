package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v2;

import java.awt.Canvas;

// version : 2.0

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * <p>
 * Titre : Painting Ants
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : Copyright (c) 2003
 * </p>
 * <p>
 * Société : Equipe Réseaux/TIC - Laboratoire d'Informatique de l'Université de
 * Tours
 * </p>
 *
 * @author Nicolas Monmarché
 * @version 1.0
 */

public class CPainting extends Canvas implements MouseListener {
  private static final long serialVersionUID = 1L;
  // matrice servant pour le produit de convolution
  /*
   * 1 2 1 2 4 2 1 2 1
   */
  static private int[][] mMatriceConv9 = {
          {1, 2, 1},
          {2, 4, 2},
          {1, 2, 1}};
  /*
   * 1 1 2 1 1 1 2 3 2 1 2 3 4 3 2 1 2 3 2 1 1 1 2 1 1
   */
  static private int[][] mMatriceConv25 = {
          {1, 1, 2, 1, 1},
          {1, 2, 3, 2, 1},
          {2, 3, 4, 3, 2},
          {1, 2, 3, 2, 1},
          {1, 1, 2, 1, 1}};
  /*
   * 1 1 2 2 2 1 1 1 2 3 4 3 2 1 2 3 4 5 4 3 2 2 4 5 8 5 4 2 2 3 4 5 4 3 2 1 2
   * 3 4 3 2 1 1 1 2 2 2 1 1
   */
  static private int[][] mMatriceConv49 = {
          {1, 1, 2, 2, 2, 1, 1},
          {1, 2, 3, 4, 3, 2, 1},
          {2, 3, 4, 5, 4, 3, 2},
          {2, 4, 5, 8, 5, 4, 2},
          {2, 3, 4, 5, 4, 3, 2},
          {1, 2, 3, 4, 3, 2, 1},
          {1, 1, 2, 2, 2, 1, 1}};

  // couleur du fond
  private int[] mFond = {255, 255, 255};

  // dimensions;
  private int mDimensionWidth;
  private int mDimensionHeight;

  private PaintingAnts mApplis;

  private boolean mSuspendu = false;

  //permet de manipuler le tableau de couleurs pour l'affichage
  private BufferedImage bi;
  private WritableRaster raster;

  /******************************************************************************
   * Titre : public CPainting() Description : Constructeur de la classe
   ******************************************************************************/
  public CPainting(int With, int Height, PaintingAnts pApplis) {
    addMouseListener(this);
    mApplis = pApplis;

    mDimensionWidth = With;
    mDimensionHeight = Height;
    setBounds(new Rectangle(0, 0, mDimensionWidth, mDimensionHeight));

    bi = new BufferedImage(mDimensionWidth, mDimensionHeight, BufferedImage.TYPE_INT_RGB);
    raster = bi.getRaster();
  }

  /******************************************************************************
   * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
   * d'une case
   ******************************************************************************/
  public int getCouleur(int x, int y) {
      return bi.getRGB(x, y);
  }

  /******************************************************************************
   * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getHauteur() {
    return mDimensionHeight;
  }

  /******************************************************************************
   * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getLargeur() {
    return mDimensionWidth;
  }

  /******************************************************************************
   * Titre : void init() Description : Initialise le fond a la couleur blanche
   * et initialise le tableau des couleurs avec la couleur blanche
   ******************************************************************************/
  public void init() {
    int i, j;

    // initialisation de la matrice des couleurs
    for (i = 0; i < mDimensionWidth; i++) {
      for (j = 0; j < mDimensionHeight; j++) {
        raster.setPixel(i, j, mFond);
      }
    }
    mSuspendu = false;
  }

  /****************************************************************************/
  public void mouseClicked(MouseEvent pMouseEvent) {
    pMouseEvent.consume();
    if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
      mApplis.pause();
      // double clic sur le bouton gauche = effacer et recommencer
      if (pMouseEvent.getClickCount() == 2) {
        init();
        mApplis.start();
      }
      // simple clic = suspendre les calculs et l'affichage
    } else {
      // bouton du milieu (roulette) = suspendre l'affichage mais
      // continuer les calculs
      if (pMouseEvent.getButton() == MouseEvent.BUTTON2) {
        suspendre();
      } else {
        // clic bouton droit = effacer et recommencer
        // case pMouseEvent.BUTTON3:
        init();
      }
    }
  }

  /****************************************************************************/
  public void mouseEntered(MouseEvent pMouseEvent) {
  }

  /****************************************************************************/
  public void mouseExited(MouseEvent pMouseEvent) {
  }

  /****************************************************************************/
  public void mousePressed(MouseEvent pMouseEvent) {

  }

  /****************************************************************************/
  public void mouseReleased(MouseEvent pMouseEvent) {
  }

  /******************************************************************************
   * Titre : void paint(Graphics g) Description : Surcharge de la fonction qui
   * est appelé lorsque le composant doit être redessiné
   ******************************************************************************/
  //@Override
  public void paint(Graphics pGraphics) {
    pGraphics.drawImage(bi, 0, 0, this);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /******************************************************************************
   * Titre : void colorer_case(int x, int y, Color c) Description : Cette
   * fonction va colorer le pixel correspondant et mettre a jour le tabmleau des
   * couleurs
   ******************************************************************************/
  public void setCouleur(int x, int y, int cR, int cG, int cB, int pTaille) {
    int i, j, k, l, m, n;
    int R, G, B;
    int[] RGB = {255, 255, 255};
      if (!mSuspendu) {
        // on colorie la case sur laquelle se trouve la fourmi
        int[] cFourmi = {cR, cG, cB};
        raster.setPixel(x, y, cFourmi);
      }


      // on fait diffuser la couleur :
      switch (pTaille) {
        case 0:
          // on ne fait rien = pas de diffusion
          break;
        case 1:
          // produit de convolution discrete sur 9 cases
          for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
              R = G = B = 0;

              for (k = 0; k < 3; k++) {
                for (l = 0; l < 3; l++) {
                  m = (x + i + k - 2 + mDimensionWidth) % mDimensionWidth;
                  n = (y + j + l - 2 + mDimensionHeight) % mDimensionHeight;

                  RGB = bi.getRaster().getPixel(m,n, RGB);

                  R += CPainting.mMatriceConv9[k][l] * RGB[0];
                  G += CPainting.mMatriceConv9[k][l] * RGB[1];
                  B += CPainting.mMatriceConv9[k][l] * RGB[2];
                }
              }

              m = (x + i - 1 + mDimensionWidth) % mDimensionWidth;
              n = (y + j - 1 + mDimensionHeight) % mDimensionHeight;
              RGB[0] = R/16;
              RGB[1] = G/16;
              RGB[2] = B/16;
              raster.setPixel(m, n, RGB);
            }
          }
          break;
        case 2:
          // produit de convolution discrete sur 25 cases
          for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
              R = G = B = 0;

              for (k = 0; k < 5; k++) {
                for (l = 0; l < 5; l++) {
                  m = (x + i + k - 4 + mDimensionWidth) % mDimensionWidth;
                  n = (y + j + l - 4 + mDimensionHeight) % mDimensionHeight;
                  RGB = bi.getRaster().getPixel(m,n, RGB);
                  R += CPainting.mMatriceConv25[k][l] * RGB[0];
                  G += CPainting.mMatriceConv25[k][l] * RGB[1];
                  B += CPainting.mMatriceConv25[k][l] * RGB[2];
                }
              }

              m = (x + i - 2 + mDimensionWidth) % mDimensionWidth;
              n = (y + j - 2 + mDimensionHeight) % mDimensionHeight;
              RGB[0] = R/44;
              RGB[1] = G/44;
              RGB[2] = B/44;
              raster.setPixel(m, n, RGB);
            }
          }
          break;
        case 3:
          // produit de convolution discrete sur 49 cases
          for (i = 0; i < 7; i++) {
            for (j = 0; j < 7; j++) {
              R = G = B = 0;

              for (k = 0; k < 7; k++) {
                for (l = 0; l < 7; l++) {
                  m = (x + i + k - 6 + mDimensionWidth) % mDimensionWidth;
                  n = (y + j + l - 6 + mDimensionHeight) % mDimensionHeight;
                  RGB = bi.getRaster().getPixel(m,n, RGB);
                  R += CPainting.mMatriceConv49[k][l] * RGB[0];
                  G += CPainting.mMatriceConv49[k][l] * RGB[1];
                  B += CPainting.mMatriceConv49[k][l] * RGB[2];
                }
              }

              m = (x + i - 3 + mDimensionWidth) % mDimensionWidth;
              n = (y + j - 3 + mDimensionHeight) % mDimensionHeight;
              RGB[0] = R/128;
              RGB[1] = G/128;
              RGB[2] = B/128;
              raster.setPixel(m, n, RGB);
            }
          }
          break;
      }// end switch
    repaint();
  }

  /******************************************************************************
   * Titre : setSupendu Description : Cette fonction change l'état de suspension
   ******************************************************************************/

  public void suspendre() {
    mSuspendu = !mSuspendu;
    if (!mSuspendu) {
      repaint();
    }
  }
}