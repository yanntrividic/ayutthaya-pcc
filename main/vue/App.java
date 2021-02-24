package main.vue;

import javax.swing.JFrame;

import main.core.Graphe;

/**
 * Classe statique implémentant la méthode drawBoard
 * @author Sylvain Lobry
 */
public class App {
	
	/**
	 * Méthode pour initialiser l'affichage
	 * @param carte Plateau contenant les différentes informations relatives au problème à résoudre
	 * @param nlines nombre de lignes du plateau
	 * @param ncols nombre de colonnes du plateau
	 * @param pixelSize nombre de pixels par unité
	 */
	protected static void drawBoard(Carte carte, Graphe graphe, String nomFenetre) {
	    JFrame window = new JFrame(nomFenetre);	// nom de la fenêtre
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // le programme se termine quand on ferme la fenêtre
	    window.setBounds(100, 100, graphe.getNbColonnes()*carte.pixelSize, (int) (graphe.getNbLignes()*carte.pixelSize*1.10)); // taille de la fenêtre
	    window.getContentPane().add(carte);	 // initialise la fenêtre avec la carte
	    window.setVisible(true) ; // pour rendre la fenêtre visible
	}
}
