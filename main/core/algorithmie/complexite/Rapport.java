package main.core.algorithmie.complexite;

import java.util.LinkedList;

import main.core.Graphe;
import main.core.algorithmie.TypeAlgo;
import main.core.algorithmie.TypeHeuristique;

/**
 * Classe utilitaire permettant d'extraire des informations sur l'exécution d'un algorithme particulier
 */
public class Rapport {
	
	private Graphe graphe ;
	private String nomAlgo ;
	private String heuristique ;
	private String nomFichier ;
	private int numInstance ;
	private double tempsCalcul ;
	private int nbSommetsParcourus ;
	LinkedList<Integer> chemin ;
	
	/**
	 * Constructeur de rapports
	 * @param algo Nom de l'algorithme utilisé
	 * @param heuristique Nom de l'heuristique utilisée
	 * @param nomFichier Nom du fichier duquel a été extrait le problème
	 * @param numInstance Numéro de l'instance du problème lu
	 * @param graphe Graphe associé au problème
	 * @param nbSommetsParcourus Le nombre de sommets visités avant d'avoir trouvé le meilleur chemin, si meilleur chemin il y a
	 * @param chemin Le meilleur chemin trouvé, vide si aucun chemin n'a été trouvé
	 * @param tempsCalcul Le temps de calcul qu'a necéssité l'exécution de ce l'algorithme avec les paramètres énoncés précedemment
	 */
	public Rapport(TypeAlgo algo, TypeHeuristique heuristique, String nomFichier, int numInstance, Graphe graphe, int nbSommetsParcourus, LinkedList<Integer> chemin, double tempsCalcul) {
		this.nomAlgo = algo.toString() ;
		try { this.heuristique = heuristique.toString() ; } catch(NullPointerException e) { this.heuristique = "aucune" ;} // permet d'être robuste aux différents cas de figure
		try { this.nomFichier = nomFichier ; } catch(NullPointerException e) { this.nomFichier = "System.in" ;}
		this.tempsCalcul = tempsCalcul ;
		this.nbSommetsParcourus = nbSommetsParcourus ;
		this.chemin = chemin ;
		this.graphe = graphe ; 
	}
	
	public String getNomFichier() {
		return nomFichier ;
	}
	
	public String getNomAlgo() {
		return nomAlgo;
	}

	public String getHeuristique() {
		return heuristique;
	}

	public int getNbSommetsParcourus() {
		return nbSommetsParcourus;
	}

	public LinkedList<Integer> getChemin() {
		return chemin ;
	}
	
	/**
	 * Méthode calculant le temps nécessaire pour rejoindre le sommet source et le sommet arrivée
	 * @return double correspondant au temps du plus court chemin trouvé par l'algorithme, Double.POSITIVE_INFINITY si aucun chemin n'a été trouvé
	 */
	public double getTempsChemin() {
		return graphe.getListeSommets().get(graphe.getNumArrivee()).getTempsDepuisSource() ;
	}
	
	/**
	 * Le score est le ratio du nombre de sommets empruntés par le chemin final sur le nombre de sommets visités.
	 * L'objectif d'un algorithme de recherche du plus court chemin serait donc de faire tendre ce score vers 1.
	 * @return Le score associé au rapport 
	 */
	public double getScore() {
		if(chemin.isEmpty()) return -1 ;
		return (double) chemin.size() / nbSommetsParcourus ;
	}
	
	public String formatCSV() {
		return "\""+nomAlgo+"\","+heuristique+","+nomFichier+","+numInstance+","+String.format("%.3f", getScore())
		+","+String.format("%.3f", getTempsChemin())+","+String.format("%.3f", tempsCalcul)+","+graphe.getNumSommets()+","+nbSommetsParcourus;
	}
	
	public static String enteteCSV() {
		return "\"nomAlgo\",\"heuristique\",\"nomFichier\",\"numInstance\",\"score\",\"tempsChemin\",\"tempsCalcul\",\"nbSommetsGraphe\",\"nbSommetsParcourus\"" ;
	}
	
	public String toString() {
		return "[algo="+nomAlgo+", heuristique="+heuristique+", nomFichier="+nomFichier+", score="+String.format("%.3f", getScore())
		+", tempsChemin="+String.format("%.1f", getTempsChemin())+", tempsCalcul="+String.format("%.3f", tempsCalcul)
		+"ms, nbSommetsGraphe="+graphe.getNumSommets()+"]" ;
	}
}
