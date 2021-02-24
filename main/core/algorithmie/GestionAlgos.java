package main.core.algorithmie;

import java.util.LinkedList;

import main.core.Graphe;
import main.core.algorithmie.complexite.Rapport;
import main.vue.Carte;

/**
 * Classe statique pour gérer l'exécution d'un algorithme
 */
public class GestionAlgos {
	
	/**
	 * Méthode générant un rapport sur l'exécution de l'algorithme demandé
	 * @param algo Algorithme à exécuter
	 * @see TypeAlgo
	 * @param heuristique Heuristique à utiliser dans le cas où l'algorithme serait AStar
	 * @param nomFichier Fichier à partir duquel a été lu le graphe
	 * @param numInstance Numéro de l'instance du graphe
	 * @param graphe Graphe sur lequel on exécute les algorithmes
	 * @param carte Fenêtre d'affichage 
	 * @param ayutthaya Si on se situe dans le projet Ayutthaya ou non
	 * @return Un rapport contenant toutes les informations importantes par rapport à l'exécution de l'algorithme
	 */
	public static Rapport getRapportAlgo(TypeAlgo algo, TypeHeuristique heuristique, String nomFichier, int numInstance, Graphe graphe, Carte carte, boolean ayutthaya) {
		
		LinkedList<Integer> meilleurCheminParcouru = new LinkedList<Integer>() ;
		long tempsDebut = System.nanoTime();
		
		switch(algo) {
		case DIJSKTRA :
			meilleurCheminParcouru = Algos.dijkstra(graphe, carte) ;
			break ;
		case ASTAR :
			meilleurCheminParcouru = Algos.aStar(graphe, heuristique, carte, ayutthaya) ;
			break ;
		default :
			System.err.println("Erreur lors du choix de l'algorithme.") ;
			break ;
		}

		int nbEssais = meilleurCheminParcouru.poll() ;
		long tempsFin = System.nanoTime();	
		double tempsEcoule = tempsFin - tempsDebut; // calcul du temps d'exécution de l'algorithme
		
		Rapport r = new Rapport(algo, heuristique, nomFichier, numInstance, graphe, nbEssais, meilleurCheminParcouru, tempsEcoule/1000000) ; // génération du rapport
		afficherResultats(r, ayutthaya, numInstance) ; // affichage des résultats dans la console
		return r ;
	}
	
	/**
	 * Méthode affichant dans la console les rapports d'exécution des différents algorithmes
	 * @param rapport Rapport d'exécution de l'algorithme
	 * @param ayutthaya si on se situe dans le projet Ayutthaya ou non
	 * @param numInstance le numéro d'instance associé 
	 */
	public static void afficherResultats(Rapport rapport, boolean ayutthaya, int numInstance) {
		System.out.println("Calcul terminé sur l"+(ayutthaya?("'instance #"+numInstance+" du"):"e")+" fichier "+rapport.getNomFichier()) ;
		System.out.println("\tAlgorithme utilisé : "+rapport.getNomAlgo()+((rapport.getHeuristique()!="aucune")?" avec la distance "+rapport.getHeuristique():""));
		System.out.println("\tNombre total de sommets explorés : " + rapport.getNbSommetsParcourus());
		System.out.println("\tTemps total du meilleur chemin trouvé : " + String.format("%.1f", rapport.getTempsChemin())+"\n");
	}
}
