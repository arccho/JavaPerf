package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v3;
// version : 4.0

import java.awt.Color;
import java.util.Random;

public class CFourmi {
  // Tableau des incrémentations à effectuer sur la position des fourmis
  // en fonction de la direction du deplacement
  static private int[][] mIncDirection = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
  // le generateur aléatoire (Random est thread safe donc on la partage)
  private static Random GenerateurAleatoire = new Random();
  // couleur déposé par la fourmi
  //private Color mCouleurDeposee;
  private int mRDeposee;
  private int mGDeposee;
  private int mBDeposee;

  private int mLuminanceCouleurSuivie;
  // objet graphique sur lequel les fourmis peuvent peindre
  private CPainting mPainting;
  // Coordonées de la fourmi
  private int x, y;
  // Proba d'aller a gauche, en face, a droite, de suivre la couleur
  private float mProba0;
  private float mProba1;
  private float mProba2;
  private float mProba3;
  // Numéro de la direction dans laquelle la fourmi regarde
  private int mDirection;
  // Taille de la trace de phéromones déposée par la fourmi
  private int mTaille;
  // Pas d'incrémentation des directions suivant le nombre de directions
  // allouées à la fourmies
  private int mDecalDir;
  // l'applet
  private PaintingAnts mApplis;
  // seuil de luminance pour la détection de la couleur recherchée
  private int mSeuilLuminance;
  // nombre de déplacements de la fourmi
  private long mNbDeplacements;

  /*************************************************************************************************
   */
  public CFourmi(int pRDeposee, int pGDeposee, int pBDeposee, int pRSuivie, int pGSuivie , int pBSuivie, float pProbaTD, float pProbaG, float pProbaD,
                 float pProbaSuivre, CPainting pPainting, char pTypeDeplacement, float pInit_x, float pInit_y, int pInitDirection,
                 int pTaille, int pSeuilLuminance, PaintingAnts pApplis) {

    //mCouleurDeposee = pCouleurDeposee;
    mRDeposee = pRDeposee;
    mGDeposee = pGDeposee;
    mBDeposee = pBDeposee;
    //mLuminanceCouleurSuivie = 0.2426f * pCouleurDeposee.getRed() + 0.7152f * pCouleurDeposee.getGreen() + 0.0722f * pCouleurDeposee.getBlue();
    mLuminanceCouleurSuivie = 2426 * pRDeposee + 7152 * pGDeposee + 722 * pBDeposee;
    mPainting = pPainting;
    mApplis = pApplis;

    // direction de départ
    mDirection = pInitDirection;

    // taille du trait
    mTaille = pTaille;

    // initialisation des probas
    mProba0 = pProbaG; // proba d'aller à gauche
    mProba1 = pProbaTD; // proba d'aller tout droit
    mProba2 = pProbaD; // proba d'aller à droite
    mProba3 = pProbaSuivre; // proba de suivre la couleur

    // nombre de directions pouvant être prises : 2 types de déplacement
    // possibles
    if (pTypeDeplacement == 'd') {
      mDecalDir = 2;
    } else {
      mDecalDir = 1;
    }

    mSeuilLuminance = pSeuilLuminance;
    mNbDeplacements = 0;
  }

  /*************************************************************************************************
   * Titre : void deplacer() Description : Fonction de deplacement de la fourmi
   *
   */
  public void deplacer() {
    float tirage, prob1, prob2, prob3, total;
    //int[] dir = new int[3];
    int dir0 = 0;
    int dir1 = 0;
    int dir2 = 0;
    int i, j;
    //Color lCouleur;
    int lR;
    int lG;
    int lB;

    mNbDeplacements++;

    //dir[0] = 0;
    //dir[1] = 0;
    //dir[2] = 0;

    // le tableau dir contient 0 si la direction concernée ne contient pas la
    // couleur
    // à suivre, et 1 sinon (dir[0]=gauche, dir[1]=tt_droit, dir[2]=droite)
    i = modulo(x + CFourmi.mIncDirection[modulo(mDirection - mDecalDir, 8)][0], mPainting.getLargeur());
    j = modulo(y + CFourmi.mIncDirection[modulo(mDirection - mDecalDir, 8)][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      //lCouleur = new Color(mApplis.mBaseImage.getRGB(i, j));
      int rgb = mApplis.mBaseImage.getRGB(i, j);
      lR = (rgb >> 16) & 0x000000FF;
      lG = (rgb >>8 ) & 0x000000FF;
      lB = (rgb) & 0x000000FF;
    } else {
      //lCouleur = new Color(mPainting.getCouleur(i, j));
      int couleur = mPainting.getCouleur(i, j);
      lR = (couleur >> 16) & 0xFF;
      lG = (couleur >> 8) & 0xFF;
      lB = couleur & 0xFF;
    }
    if (testCouleur(lR, lG, lB)) {
      //dir[0] = 1;
      dir0 = 1;
    }

    i = modulo(x + CFourmi.mIncDirection[mDirection][0], mPainting.getLargeur());
    j = modulo(y + CFourmi.mIncDirection[mDirection][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      //lCouleur = new Color(mApplis.mBaseImage.getRGB(i, j));
      int rgb = mApplis.mBaseImage.getRGB(i, j);
      lR = (rgb >> 16) & 0x000000FF;
      lG = (rgb >>8 ) & 0x000000FF;
      lB = (rgb) & 0x000000FF;

    } else {
      //lCouleur = new Color(mPainting.getCouleur(i, j).getRGB());
      int couleur = mPainting.getCouleur(i, j);
      lR = (couleur >> 16) & 0xFF;
      lG = (couleur >> 8) & 0xFF;
      lB = couleur & 0xFF;
    //System.out.println(lR + " " + lG + " " + lB);
    }
    if (testCouleur(lR, lG, lB)) {
      //dir[1] = 1;
      dir1 = 1;
    }
    i = modulo(x + CFourmi.mIncDirection[modulo(mDirection + mDecalDir, 8)][0], mPainting.getLargeur());
    j = modulo(y + CFourmi.mIncDirection[modulo(mDirection + mDecalDir, 8)][1], mPainting.getHauteur());
    if (mApplis.mBaseImage != null) {
      //lCouleur = new Color(mApplis.mBaseImage.getRGB(i, j));
      int rgb = mApplis.mBaseImage.getRGB(i, j);
      lR = (rgb >> 16) & 0x000000FF;
      lG = (rgb >>8 ) & 0x000000FF;
      lB = (rgb) & 0x000000FF;
    } else {
      //lCouleur = new Color(mPainting.getCouleur(i, j).getRGB());
      int couleur = mPainting.getCouleur(i, j);
      lR = (couleur >> 16) & 0xFF;
      lG = (couleur >> 8) & 0xFF;
      lB = couleur & 0xFF;

    }
    if (testCouleur(lR, lG, lB)) {
      //dir[2] = 1;
      dir2 = 1;
    }

    // tirage d'un nombre aléatoire permettant de savoir si la fourmi va suivre
    // ou non la couleur
    tirage = GenerateurAleatoire.nextFloat();// Math.random();

    // la fourmi suit la couleur
    if (((tirage <= mProba3) && ((dir0 + dir1 + dir2) > 0)) || ((dir0 + dir1 + dir2) == 3)) {
      prob1 = (dir0) * mProba0;
      prob2 = (dir1) * mProba1;
      prob3 = (dir2) * mProba2;
    }
    // la fourmi ne suit pas la couleur
    else {
      prob1 = (1 - dir0) * mProba0;
      prob2 = (1 - dir1) * mProba1;
      prob3 = (1 - dir2) * mProba2;
    }
    total = prob1 + prob2 + prob3;
    prob1 = prob1 / total;
    prob2 = prob2 / total + prob1;
    prob3 = prob3 / total + prob2;

    // incrémentation de la direction de la fourmi selon la direction choisie
    tirage = GenerateurAleatoire.nextFloat();// Math.random();
    if (tirage < prob1) {
      mDirection = modulo(mDirection - mDecalDir, 8);
    } else {
      if (tirage < prob2) {
        /* rien, on va tout droit */
      } else {
        mDirection = modulo(mDirection + mDecalDir, 8);
      }
    }

    x += CFourmi.mIncDirection[mDirection][0];
    y += CFourmi.mIncDirection[mDirection][1];

    x = modulo(x, mPainting.getLargeur());
    y = modulo(y, mPainting.getHauteur());

    // coloration de la nouvelle position de la fourmi
    mPainting.setCouleur(x, y, mRDeposee, mGDeposee, mBDeposee, mTaille);

    mApplis.IncrementFpsCounter();
  }

  /*************************************************************************************************
   */
  public long getNbDeplacements() {
    return mNbDeplacements;
  }
  /****************************************************************************/

  /*************************************************************************************************
   */
  public int getX() {
    return x;
  }

  /*************************************************************************************************
   */
  public int getY() {
    return y;
  }

  /*************************************************************************************************
   * Titre : modulo Description : Fcontion de modulo permettant au fourmi de
   * reapparaitre de l autre coté du Canvas lorsque qu'elle sorte de ce dernier
   *
   * @param x
   *          valeur
   *
   * @return int
   */
  private int modulo(int x, int m) {
    return (x + m) % m;
  }

  /*************************************************************************************************
   * Titre : boolean testCouleur() Description : fonction testant l'égalité
   * d'une couleur avec la couleur suivie
   *
   */
  private boolean testCouleur(/*Color pCouleur*/int R, int G, int B) {
    boolean lReponse = false;
    int lLuminance;

    /* on calcule la luminance */
    //lLuminance = 0.2426f * pCouleur.getRed() + 0.7152f * pCouleur.getGreen() + 0.0722f * pCouleur.getBlue();
    lLuminance = 2426 * R + 7152 * G + 722 * B;

    /* test */
    if (Math.abs(mLuminanceCouleurSuivie - lLuminance) < mSeuilLuminance) {
      lReponse = true;
      // System.out.print(x);
    }

    return lReponse;
  }
}
