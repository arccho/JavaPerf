package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

// version : 2.0

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
  static private int[][] mMatriceConv9 = new int[3][3];
  static private int[][] mMatriceConv25 = new int[5][5];
  static private int[][] mMatriceConv49 = new int[7][7];
  // Objet de type Graphics permettant de manipuler l'affichage du Canvas
  private Graphics mGraphics;
  // Objet ne servant que pour les bloc synchronized pour la manipulation du
  // tableau des couleurs
  private Object mMutexCouleurs = new Object();
  // tableau des couleurs, il permert de conserver en memoire l'état de chaque
  // pixel du canvas, ce qui est necessaire au deplacemet des fourmi
  // il sert aussi pour la fonction paint du Canvas
  //private Color[][] mCouleurs;
  private int[][][] mCouleurs;
  // couleur du fond
  //private Color mCouleurFond = new Color(255, 255, 255);
  private int mRFond = 255;
  private int mGFond = 255;
  private int mBFond = 255;
  // dimensions
  private Dimension mDimension = new Dimension();

  private PaintingAnts mApplis;

  private boolean mSuspendu = false;

  /******************************************************************************
   * Titre : public CPainting() Description : Constructeur de la classe
   ******************************************************************************/
  public CPainting(Dimension pDimension, PaintingAnts pApplis) {
    int i, j;
    addMouseListener(this);

    mApplis = pApplis;

    mDimension = pDimension;
    setBounds(new Rectangle(0, 0, mDimension.width, mDimension.height));

    this.setBackground(new Color(mRFond, mGFond, mBFond));

    // initialisation de la matrice des couleurs
    //mCouleurs = new Color[mDimension.width][mDimension.height];
    mCouleurs = new int[mDimension.width][mDimension.height][3];
    synchronized (mMutexCouleurs) {
      for (i = 0; i != mDimension.width; i++) {
        for (j = 0; j != mDimension.height; j++) {
          //mCouleurs[i][j] = new Color(mRFond, mGFond, mBFond);
          mCouleurs[i][j][0] = mRFond;
          mCouleurs[i][j][1] = mGFond;
          mCouleurs[i][j][2] = mBFond;
        }
      }
    }

  }

  /******************************************************************************
   * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
   * d'une case
   ******************************************************************************/
  public int[]/*Color*/ getCouleur(int x, int y) {
    synchronized (mMutexCouleurs) {
      return mCouleurs[x][y];
    }
  }

  /******************************************************************************
   * Titre : Color getDimension Description : Cette fonction renvoie la
   * dimension de la peinture
   ******************************************************************************/
  public Dimension getDimension() {
    return mDimension;
  }

  /******************************************************************************
   * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getHauteur() {
    return mDimension.height;
  }

  /******************************************************************************
   * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getLargeur() {
    return mDimension.width;
  }

  /******************************************************************************
   * Titre : void init() Description : Initialise le fond a la couleur blanche
   * et initialise le tableau des couleurs avec la couleur blanche
   ******************************************************************************/
  public void init() {
    int i, j;
    mGraphics = getGraphics();
    synchronized (mMutexCouleurs) {
      mGraphics.clearRect(0, 0, mDimension.width, mDimension.height);

      // initialisation de la matrice des couleurs

      for (i = 0; i != mDimension.width; i++) {
        for (j = 0; j != mDimension.height; j++) {
          //mCouleurs[i][j] = new Color(mRFond, mGFond, mBFond);
          mCouleurs[i][j][0] = mRFond;
          mCouleurs[i][j][1] = mGFond;
          mCouleurs[i][j][2] = mBFond;
        }
      }
    }

    // initialisation de la matrice de convolution : lissage moyen sur 9
    // cases
    /*
     * 1 2 1 2 4 2 1 2 1
     */
    CPainting.mMatriceConv9[0][0] = 1;
    CPainting.mMatriceConv9[0][1] = 2;
    CPainting.mMatriceConv9[0][2] = 1;
    CPainting.mMatriceConv9[1][0] = 2;
    CPainting.mMatriceConv9[1][1] = 4;
    CPainting.mMatriceConv9[1][2] = 2;
    CPainting.mMatriceConv9[2][0] = 1;
    CPainting.mMatriceConv9[2][1] = 2;
    CPainting.mMatriceConv9[2][2] = 1;

    // initialisation de la matrice de convolution : lissage moyen sur 25
    // cases
    /*
     * 1 1 2 1 1 1 2 3 2 1 2 3 4 3 2 1 2 3 2 1 1 1 2 1 1
     */
    CPainting.mMatriceConv25[0][0] = 1;
    CPainting.mMatriceConv25[0][1] = 1;
    CPainting.mMatriceConv25[0][2] = 2;
    CPainting.mMatriceConv25[0][3] = 1;
    CPainting.mMatriceConv25[0][4] = 1;
    CPainting.mMatriceConv25[1][0] = 1;
    CPainting.mMatriceConv25[1][1] = 2;
    CPainting.mMatriceConv25[1][2] = 3;
    CPainting.mMatriceConv25[1][3] = 2;
    CPainting.mMatriceConv25[1][4] = 1;
    CPainting.mMatriceConv25[2][0] = 2;
    CPainting.mMatriceConv25[2][1] = 3;
    CPainting.mMatriceConv25[2][2] = 4;
    CPainting.mMatriceConv25[2][3] = 3;
    CPainting.mMatriceConv25[2][4] = 2;
    CPainting.mMatriceConv25[3][0] = 1;
    CPainting.mMatriceConv25[3][1] = 2;
    CPainting.mMatriceConv25[3][2] = 3;
    CPainting.mMatriceConv25[3][3] = 2;
    CPainting.mMatriceConv25[3][4] = 1;
    CPainting.mMatriceConv25[4][0] = 1;
    CPainting.mMatriceConv25[4][1] = 1;
    CPainting.mMatriceConv25[4][2] = 2;
    CPainting.mMatriceConv25[4][3] = 1;
    CPainting.mMatriceConv25[4][4] = 1;

    // initialisation de la matrice de convolution : lissage moyen sur 49
    // cases
    /*
     * 1 1 2 2 2 1 1 1 2 3 4 3 2 1 2 3 4 5 4 3 2 2 4 5 8 5 4 2 2 3 4 5 4 3 2 1 2
     * 3 4 3 2 1 1 1 2 2 2 1 1
     */
    CPainting.mMatriceConv49[0][0] = 1;
    CPainting.mMatriceConv49[0][1] = 1;
    CPainting.mMatriceConv49[0][2] = 2;
    CPainting.mMatriceConv49[0][3] = 2;
    CPainting.mMatriceConv49[0][4] = 2;
    CPainting.mMatriceConv49[0][5] = 1;
    CPainting.mMatriceConv49[0][6] = 1;

    CPainting.mMatriceConv49[1][0] = 1;
    CPainting.mMatriceConv49[1][1] = 2;
    CPainting.mMatriceConv49[1][2] = 3;
    CPainting.mMatriceConv49[1][3] = 4;
    CPainting.mMatriceConv49[1][4] = 3;
    CPainting.mMatriceConv49[1][5] = 2;
    CPainting.mMatriceConv49[1][6] = 1;

    CPainting.mMatriceConv49[2][0] = 2;
    CPainting.mMatriceConv49[2][1] = 3;
    CPainting.mMatriceConv49[2][2] = 4;
    CPainting.mMatriceConv49[2][3] = 5;
    CPainting.mMatriceConv49[2][4] = 4;
    CPainting.mMatriceConv49[2][5] = 3;
    CPainting.mMatriceConv49[2][6] = 2;

    CPainting.mMatriceConv49[3][0] = 2;
    CPainting.mMatriceConv49[3][1] = 4;
    CPainting.mMatriceConv49[3][2] = 5;
    CPainting.mMatriceConv49[3][3] = 8;
    CPainting.mMatriceConv49[3][4] = 5;
    CPainting.mMatriceConv49[3][5] = 4;
    CPainting.mMatriceConv49[3][6] = 2;

    CPainting.mMatriceConv49[4][0] = 2;
    CPainting.mMatriceConv49[4][1] = 3;
    CPainting.mMatriceConv49[4][2] = 4;
    CPainting.mMatriceConv49[4][3] = 5;
    CPainting.mMatriceConv49[4][4] = 4;
    CPainting.mMatriceConv49[4][5] = 3;
    CPainting.mMatriceConv49[4][6] = 2;

    CPainting.mMatriceConv49[5][0] = 1;
    CPainting.mMatriceConv49[5][1] = 2;
    CPainting.mMatriceConv49[5][2] = 3;
    CPainting.mMatriceConv49[5][3] = 4;
    CPainting.mMatriceConv49[5][4] = 3;
    CPainting.mMatriceConv49[5][5] = 2;
    CPainting.mMatriceConv49[5][6] = 1;

    CPainting.mMatriceConv49[6][0] = 1;
    CPainting.mMatriceConv49[6][1] = 1;
    CPainting.mMatriceConv49[6][2] = 2;
    CPainting.mMatriceConv49[6][3] = 2;
    CPainting.mMatriceConv49[6][4] = 2;
    CPainting.mMatriceConv49[6][5] = 1;
    CPainting.mMatriceConv49[6][6] = 1;

    mSuspendu = false;
  }

  /****************************************************************************/
  public void mouseClicked(MouseEvent pMouseEvent) {
    pMouseEvent.consume();
    if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
      // double clic sur le bouton gauche = effacer et recommencer
      if (pMouseEvent.getClickCount() == 2) {
        init();
      }
      // simple clic = suspendre les calculs et l'affichage
      mApplis.pause();
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
  @Override
  public void paint(Graphics pGraphics) {
    int i, j;

    synchronized (mMutexCouleurs) {
      for (i = 0; i < mDimension.width; i++) {
        for (j = 0; j < mDimension.height; j++) {
          pGraphics.setColor(new Color(mCouleurs[i][j][0], mCouleurs[i][j][1], mCouleurs[i][j][2]));
          pGraphics.fillRect(i, j, 1, 1);
        }
      }
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
    Color lColor;
    //Color c = new Color(cR, cG, cB);

    synchronized (mMutexCouleurs) {
      if (!mSuspendu) {
        // on colorie la case sur laquelle se trouve la fourmi
        mGraphics.setColor(new Color(cR, cG, cB));
        mGraphics.fillRect(x, y, 1, 1);
      }

      //mCouleurs[x][y] = c;
      mCouleurs[x][y][0] = cR;
      mCouleurs[x][y][1] = cG;
      mCouleurs[x][y][2] = cB;



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
                  m = (x + i + k - 2 + mDimension.width) % mDimension.width;
                  n = (y + j + l - 2 + mDimension.height) % mDimension.height;
                  R += CPainting.mMatriceConv9[k][l] * mCouleurs[m][n][0];
                  G += CPainting.mMatriceConv9[k][l] * mCouleurs[m][n][1];
                  B += CPainting.mMatriceConv9[k][l] * mCouleurs[m][n][2];
                }
              }
              lColor = new Color(R / 16, G / 16,B / 16);

              mGraphics.setColor(lColor);

              m = (x + i - 1 + mDimension.width) % mDimension.width;
              n = (y + j - 1 + mDimension.height) % mDimension.height;
              //mCouleurs[m][n] = lColor;
              mCouleurs[m][n][0] = R / 16;
              mCouleurs[m][n][1] = G / 16;
              mCouleurs[m][n][2] = B / 16;
              if (!mSuspendu) {
                mGraphics.fillRect(m, n, 1, 1);
              }
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
                  m = (x + i + k - 4 + mDimension.width) % mDimension.width;
                  n = (y + j + l - 4 + mDimension.height) % mDimension.height;
                  R += CPainting.mMatriceConv25[k][l] * mCouleurs[m][n][0];
                  G += CPainting.mMatriceConv25[k][l] * mCouleurs[m][n][1];
                  B += CPainting.mMatriceConv25[k][l] * mCouleurs[m][n][2];
                }
              }
              lColor = new Color(R / 44,G / 44,B / 44);
              mGraphics.setColor(lColor);
              m = (x + i - 2 + mDimension.width) % mDimension.width;
              n = (y + j - 2 + mDimension.height) % mDimension.height;

              //mCouleurs[m][n] = lColor;
              mCouleurs[m][n][0] =  R / 44;
              mCouleurs[m][n][1] =  G / 44;
              mCouleurs[m][n][2] =  B / 44;
              if (!mSuspendu) {
                mGraphics.fillRect(m, n, 1, 1);
              }

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
                  m = (x + i + k - 6 + mDimension.width) % mDimension.width;
                  n = (y + j + l - 6 + mDimension.height) % mDimension.height;
                  R += CPainting.mMatriceConv49[k][l] * mCouleurs[m][n][0];
                  G += CPainting.mMatriceConv49[k][l] * mCouleurs[m][n][1];
                  B += CPainting.mMatriceConv49[k][l] * mCouleurs[m][n][2];
                }
              }
              lColor = new Color(R / 128, G / 128,B / 128);
              mGraphics.setColor(lColor);;
              m = (x + i - 3 + mDimension.width) % mDimension.width;
              n = (y + j - 3 + mDimension.height) % mDimension.height;

              //mCouleurs[m][n] = lColor;
              mCouleurs[m][n][0] =  R / 128;
              mCouleurs[m][n][1] =  G / 128;
              mCouleurs[m][n][2] =  B / 128;
              if (!mSuspendu) {
                mGraphics.fillRect(m, n, 1, 1);
              }

            }
          }
          break;
      }// end switch
    }
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
