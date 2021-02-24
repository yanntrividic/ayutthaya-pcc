package main.core;

import java.util.HashMap;
import java.util.LinkedList;

import main.core.algorithmie.TypeHeuristique;

/**
 * Classe contenant les informations sur la structure de données Sommet. Cette classe fonctionne exclusivement en dialogue avec les deux autres classes de ce package
 * @see Arete
 * @see Sommet
 */
public class Sommet {
	private double tempsSommet; // Poids du sommet courant
	private double tempsDepuisSource; // Temps qu'il a fallu parcourir pour rejoindre ce sommet à partir du sommet source
	private double heuristique; // Valeur permettant de décider s'il faut choisir de visiter ce sommet ou non
	private Sommet precedent; // Pointe vers le sommet qui a permis d'atteindre le sommet courant. Permet de reconstituer le chemin le plus court.
	private LinkedList<Arete> listeAdjacence; // toutes les arêtes (et donc tous les voisins) du sommet
	private final int cle; // identifiant du sommet

	/**
	 * Constructeur du sommet 
	 * @param cle identifiant unique du sommet
	 */
	public Sommet(int cle) {
		this.tempsSommet = Double.POSITIVE_INFINITY; // tant qu'on a pas associé le sommet à un type, on ne sait pas quel est son poids
		this.tempsDepuisSource = Double.POSITIVE_INFINITY; // tant qu'on a pas visité le sommet, on ne sait pas quelle distance le sépare du début du graphe
		this.precedent = null; // tant qu'on a pas visité le sommet, on ne sait pas quel sommet a permis de l'atteindre
		this.listeAdjacence = new LinkedList<Arete>();  // chaque sommet voit ses voisins représentés dans une liste d'adjacence
		this.cle = cle;									
	}

	/**
	 * Méthode ajoutant un voisin au sommet courant en ajoutant une arête dans sa liste d'adjacene.
	 * Dans ce projet, chaque sommet est voisin des sommets directement accolés à lui, mais pas plus.
	 * Un sommet a donc au plus huit voisins si on compte les diagonales et au moins quatre sinon.
	 * @param une arête dont la source est le voisin courant et dont la destination est le voisin.
	 */
	public void addVoisin(Arete e) {
		this.listeAdjacence.addFirst(e); 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder() ;
		sb.append(cle) ;
		sb.append(" = [prev="+((precedent == null)?"null":precedent.getCle())+", indivTime="+tempsSommet+", timeFromSource="+tempsDepuisSource+", heuristic="+heuristique+", ") ;
		if(!listeAdjacence.isEmpty()) {
			sb.append("adjacencylist=") ;

			for(Arete e : listeAdjacence) sb.append(e.toString()+", ") ;
			return sb.substring(0, sb.length()-3).toString()+"]" ;
		}
		sb.append("adjacencylist=empty");
		return sb.toString() ;
	}

	/**
	 * Méthode permettant de calculer l'heuristique d'un sommet en fonction de l'heuristique choisie
	 * @param th Un type d'heuristique particulier
	 * @param graphe Le graphe dans lequel est contenu le sommet
	 */
	public void calculHeuristique(TypeHeuristique th, Graphe graphe) {

		int nCols = graphe.getNbColonnes() ;
		int end = graphe.getNumArrivee() ;
		int nbSommets = graphe.getNumSommets() ;

		switch(th) {
		case MANHATTAN :
			this.heuristique = Math.abs(cle%nCols-end%nCols) + Math.abs(cle/(nbSommets/nCols)-end/(nbSommets/nCols)) ; // calcul de la distance Manhattan
			break ;
		case EUCLIDIENNE:
			this.heuristique = Math.sqrt(Math.pow(cle%nCols-end%nCols, 2) + Math.pow(cle/(nbSommets/nCols)-end/(nbSommets/nCols), 2)) ; // calcul de la distance euclidienne
			break ;
		case FEU:
			int feu = graphe.getNumFeu() ;
			double distanceSommetSortie = Math.abs(cle%nCols-end%nCols) + Math.abs(cle/(nbSommets/nCols)-end/(nbSommets/nCols)) ; // calcul de la distance Manhattan
			double distanceSommetOrigineFeu =  Math.abs(cle%nCols-feu%nCols) + Math.abs(cle/(nbSommets/nCols)-feu/(nbSommets/nCols)) ; // prenant aussi en compte la distance séparant le sommet courant et l'origine du feu
			this.heuristique = distanceSommetSortie - distanceSommetOrigineFeu / distanceSommetSortie ; // en ajoutant ce ratio à la distance Manhattan, on obtient des résultats significativement meilleurs
			break ;
		default:
			throw new IllegalArgumentException("L'heuristique passée en argument n'est pas supportée par ce programme.") ;
		}
	}

	/**
	 * Méthode calculant si le sommet est proche des différents bords du graphe ou non.
	 * @param graphe le graphe auquel appartient le sommet 
	 * @return quatre valeurs booléennes indiquant si le sommet est proche du bord indiqué dans le set
	 */
	public HashMap<String, Boolean> ouAjouterAretes(Graphe graphe) {
		HashMap<String, Boolean> valeurs = new HashMap<String, Boolean>() ;

		int x = this.getCle()%graphe.getNbColonnes() ;
		int y = this.getCle()/graphe.getNbColonnes() ;

		valeurs.put("gauche", x != 0) ;
		valeurs.put("droite", x != graphe.getNbColonnes()-1) ;			
		valeurs.put("haut", y != 0) ;
		valeurs.put("bas", y != graphe.getNbLignes()-1) ;

		return valeurs ;
	}
	
	

	public double getTempsDepuisSource() {
		return tempsDepuisSource;
	}

	public void setTempsDepuisSource(double tempsDepuisSource) {
		this.tempsDepuisSource = tempsDepuisSource;
	}

	public Sommet getPrecedent() {
		return precedent;
	}

	public void setPrecedent(Sommet precedent) {
		this.precedent = precedent;
	}

	public double getTempsSommet() {
		return tempsSommet;
	}
	
	public void setTempsSommet(double tempsSommet) {
		this.tempsSommet = tempsSommet ;
	}

	public void senflamme(Graphe g) {
		this.tempsSommet = g.getTypesDeSol().get("F") ;
	}
	
	public void feuSeResorbe(Graphe g) {
		if(this.tempsSommet == g.getTypesDeSol().get("F")) this.tempsSommet = g.getTypesDeSol().get(".") ;
	}

	public double getHeuristique() {
		return heuristique;
	}

	public LinkedList<Arete> getListeAdjacence() {
		return listeAdjacence;
	}
	
	public void setListeAdjacence(LinkedList<Arete> listeAdjacence) {
		this.listeAdjacence = listeAdjacence;
	}

	public int getCle() {
		return cle;
	}
}
