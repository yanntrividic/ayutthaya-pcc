package main.vue;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import main.core.* ;
import main.core.algorithmie.TypeAlgo;
import main.core.algorithmie.TypeHeuristique;

/**
 * Classe pour gérer l'affichage
 */
public class Carte extends JComponent {
	
	public static int nbCartesMaxSimultanee = 3 ;
	
	private static final long serialVersionUID = 1L;
	
	private Graphe graph;
	protected int pixelSize;
	private int ncols;
	private int nlines;
	private HashMap<Double, String> colors;
	private int start;
	private int end;
	private int feu;
	private double max_distance;
	private int current;
	private LinkedList<Integer> path;

	/**
	 * Constructeur de la carte
	 * @param graph Le graphe associé à la carte, il y a un nLines*nCols sommets dans ce graphe
	 * @param pixelSize la taille d'affichage des pixels
	 * @param ncols nombre de colonnes de la carte 
	 * @param nlines nombre de lignes de la carte
	 * @param colors HashMap relative aux couleurs des pixels selon la valeur des sommets
	 * @param start sommet de départ
	 * @param end sommet de fin
	 */
	public Carte(Graphe graph) {
		super();
		this.graph = graph;
		this.pixelSize = 700/graph.getNbLignes();
		this.ncols = graph.getNbColonnes();
		this.nlines = graph.getNbLignes();
		this.colors = graph.getCouleursDeSol();
		this.start = graph.getNumSource();
		this.end = graph.getNumArrivee();
		this.feu = graph.getNumFeu() ;
		this.max_distance = ncols * nlines;
		this.current = -1;
		this.path = null;
	}

	//Mise à jour de l'affichage
	public void paint(Graphics g) {
		ArrayList<Sommet> liste = this.graph.getListeSommets() ;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//Ugly getCle()ar of the frame
		g2.setColor(Color.cyan);
		g2.fill(new Rectangle2D.Double(0,0,this.ncols*this.pixelSize, this.nlines*this.pixelSize));


		int num_case = 0;
		for (Sommet v : liste){ // Pour chaque sommet du graphe/unité de la fenêtre
			double type = v.getTempsSommet();		// va permettre de récupérer la couleur associée à cette valeur dans la HashMap
			int i = num_case / this.ncols; 	// on obtient la ligne
			int j = num_case % this.ncols;	// on obtient la colonne

			// Traduction des type en couleurs de pixels
			if (colors.get(type).equals("green")) g2.setPaint(Color.green); 
			if (colors.get(type).equals("gray")) g2.setPaint(Color.gray);
			if (colors.get(type).equals("blue")) g2.setPaint(Color.blue);
			if (colors.get(type).equals("yellow"))	g2.setPaint(Color.yellow);
			if (colors.get(type).equals("black")) g2.setPaint(Color.black);
			if (colors.get(type).equals("red")) g2.setPaint(Color.red);
			if (colors.get(type).equals("white")) g2.setPaint(Color.white);						
			
			g2.fill(new Rectangle2D.Double(j*this.pixelSize, i*this.pixelSize, this.pixelSize, this.pixelSize));

			if (num_case == this.current) { // le pixel courant est affiché en rouge
				g2.setPaint(Color.yellow);
				g2.draw(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 12, 12));
			}

			if (num_case == this.start)	{ // le pixel de départ est affiché en blanc
				g2.setPaint(Color.white);
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
			}

			if (num_case == this.end) { // le pixel de fin est affiché en noir
				g2.setPaint(Color.black);
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
			}

			if (num_case == this.feu) { // le pixel d'origine du feu sera en cyan
				g2.setPaint(Color.cyan);
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 8, 8));
			}

			num_case += 1; // On implémente num_case. Quand num_case sera plus grand que la taille de la fenêtre, on quitte
		}

		num_case = 0;
		for (Sommet v : liste) {
			int i = num_case / this.ncols;
			int j = num_case % this.ncols;
			if (v.getTempsDepuisSource() < Double.POSITIVE_INFINITY) {
				float g_value = (float) (1 - v.getTempsDepuisSource() / this.max_distance);
				if (g_value < 0)
					g_value = 0;
				g2.setPaint(new Color(g_value, g_value, g_value));
				g2.fill(new Ellipse2D.Double(j*this.pixelSize+this.pixelSize/2, i*this.pixelSize+this.pixelSize/2, 4, 4));
				Sommet prec = v.getPrecedent();
				if (prec != null) {
					int i2 = prec.getCle() / this.ncols;
					int j2 = prec.getCle() % this.ncols;
					g2.setPaint(Color.black);
					g2.draw(new Line2D.Double(j * this.pixelSize + this.pixelSize/2, i * this.pixelSize + this.pixelSize/2, j2 * this.pixelSize + this.pixelSize/2, i2 * this.pixelSize + this.pixelSize/2));
				}
			}
			num_case += 1;
		}

		int prev = -1;
		if (this.path != null) {
			g2.setStroke(new BasicStroke(3.0f));
			for (int cur : this.path) {
				if (prev != -1)	{
					g2.setPaint(Color.white);
					int i = prev / this.ncols;
					int j = prev % this.ncols;
					int i2 = cur / this.ncols;
					int j2 = cur % this.ncols;
					g2.draw(new Line2D.Double(j * this.pixelSize + this.pixelSize/2, i * this.pixelSize + this.pixelSize/2, j2 * this.pixelSize + this.pixelSize/2, i2 * this.pixelSize + this.pixelSize/2));
				}
				prev = cur;
			}
		}
	}

	// Mise à jour du graphe (à appeler avant de mettre à jour l'affichage)
	public void update(Graphe graph, int i) {
		this.graph = graph;
		this.current = i ;
		repaint();
	}

	// Indiquer le chemin (pour affichage)
	public void addPath(Graphe graph, LinkedList<Integer> path) {
		this.graph = graph;
		this.path = path;
		this.current = -1;
		repaint();
	}
	
	// Méthode pour ajouter des informations à la fenêtre initiale
	public static Carte initCarte(Carte carte, Graphe graphe, String fichier, int numInstance, TypeAlgo algo, TypeHeuristique heuristique, LinkedList<Carte> cartes) {
		carte = new Carte(graphe);	// Initialisation du plateau
		String nomFenetre = fichier+", instance #"+numInstance+" ("+algo.toString() ;
		nomFenetre += (heuristique != null)?", "+heuristique.toString()+")":")" ;
		App.drawBoard(carte, graphe, nomFenetre);	
		carte.repaint();	
		cartes.addLast(carte) ;
		return carte ;
	}
	
	public static void fermerFenetre(Carte c) {
		Window win = SwingUtilities.getWindowAncestor(c) ;
		win.dispose(); 
	}
	
	public static void fermerFenetre(LinkedList<Carte> cartes) {
		while(!cartes.isEmpty()) {
			fermerFenetre(cartes.poll()) ;
		}
	}
}