package main.core.algorithmie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import main.core.*;
import main.vue.Carte;


/**
 * Classe implémentant deux algorithmes pour résoudre le problème du plus cours chemin : Dijkstra et A*
 */
public class Algos {

	public static int attente = 50 ; 

	/**
	 * Méthode implémentant l'algorithme A* pour résoudre notre problème de plus court chemin et celui d'Ayutthaya
	 * @param graphe le graphe dans lequel on cherche le plus court chemin
	 * @param heuristique le type d'heuristique à utiliser
	 * @see TypeHeuristique
	 * @param carte Fenêtre d'affichage du problème
	 * @param ayutthaya Si on se situe dans le projet Ayutthaya, alors ce paramètre doit prendre pour valeur true
	 * @return Le chemin le plus court reliant l'entrée et la sortie, s'il existe. Renvoie uniquement le nombre de sommets visités sinon.
	 */
	public static LinkedList<Integer> aStar(Graphe graphe, TypeHeuristique heuristique, Carte carte, boolean ayutthaya) {

		if(ayutthaya) {
			graphe.resorberFeu();									// résorbe le feu dans le cas où un algo aurait déjà été exécuté
			graphe.resetTempsDepuisSource();						// et reset le temps depuis la source pour que l'affichage et le graphe soient cohérents
		}
		
		ArrayList<Sommet> liste = graphe.getListeSommets() ;		// Pour améliorer la complexité du programme, on pourrait utiliser ici une 
																	// file de priorité avec comme critère de comparaison le temps depuis la source + l'heuristique
																	// à la place de cet ArrayList

		int tempsMaxFeu = 0 ; 										// On initialise le temps de propagation du feu à 0...
		liste.get(graphe.getNumSource()).setTempsDepuisSource(0);	// ... tout comme le temps depuis la source du sommet source
		int nbEssais = 0;											// et le nombre d'essais qu'on a entrepris (c'est le nombre de sommets visités)

		HashSet<Sommet> to_visit = new HashSet<Sommet>() ;			// On met tous les sommets à visiter dans un HashSet...
		for(Sommet s : liste) {
			to_visit.add(s) ;
			s.calculHeuristique(heuristique, graphe);				// ... en calculant leur heuristique au passage
		}

		HashSet<Sommet> visites = new HashSet<Sommet>() ;			// On crée un second HashSet qui servira exclusivement à trouver la distance parcourue par le feu
		Sommet min_v = liste.get(graphe.getNumSource()) ; 			// La source est le sommet qu'on va visiter dans la première itération, donc min_v est initialisé à la source.

		while(to_visit.contains(liste.get(graphe.getNumArrivee()))) { // tant que la sortie n'a pas été visitée

			to_visit.remove(min_v) ; // on enlève le sommet min_v des sommets à visiter 
			visites.add(min_v) ; // et on l'ajoute aux sommets visités
			nbEssais += 1 ; // et on actualise le nombre de sommets visités

			if(ayutthaya) { // Si on est dans le projet Ayutthaya, alors on doit actualiser le feu
				int prevMaxFeu = tempsMaxFeu ; // Etat du feu avant l'entrée dans le bloc
				tempsMaxFeu = (int) min_v.getTempsDepuisSource() ; // on regarde la valeur de min_v pour savoir quelle doit être l'état de propagation du feu
				if(prevMaxFeu <= tempsMaxFeu) for(int i = prevMaxFeu ; i < tempsMaxFeu ; i++) graphe.propagationFeuSommets() ; 
				else { // puis on fait en sorte que cette distance soit égale à la distance de propagation du feu
					graphe.resorberFeu(); // cette partie pourrait être améliorée en complexité 
					for(int i = 0 ; i < tempsMaxFeu ; i++) graphe.propagationFeuSommets() ;
				}
			}
			
			for(Arete a : min_v.getListeAdjacence()) { // pour chaque voisin de min_v
				Sommet voisin = liste.get(a.getDestination()) ;
				double poids = a.getPoids() ; 
				if(voisin.getTempsDepuisSource() > min_v.getTempsDepuisSource() + poids) { // si la distance depuis la source est moins importante en passant par min_v
					voisin.setTempsDepuisSource(min_v.getTempsDepuisSource() + poids) ; // alors on l'actualise
					voisin.setPrecedent(min_v) ; // et min_v devient le précédent du voisin actualisé
				}				
			}
			
			try { 
				if(carte != null) { // si on a décidé d'afficher l'exécution de l'algorithme
					carte.update(graphe, min_v.getCle()); // alors on actualise la fenêtre
					Thread.sleep(attente); // et on temporise
				}
			} catch(InterruptedException e) {
				System.out.println("stop");
			}

			double min = Double.MAX_VALUE ;
			for(Sommet s : to_visit) { // et on extrait le nouveau min_v
				if(s.getTempsDepuisSource() + s.getHeuristique() < min) { // le sommet a la meilleure heuristique
					min_v = s ;
					min = s.getTempsDepuisSource() + s.getHeuristique() ;
				}
			}
			
			// Si le feu a atteint tous les sommets qu'il reste à visiter, ou qu'il a atteint le sommet arrivée, alors le prisonnier n'a aucune chance de s'en sortir
			if(ayutthaya && (min_v.getTempsDepuisSource() > 100000000 || liste.get(graphe.getNumArrivee()).getTempsSommet() == graphe.getTypesDeSol().get("F"))) {
				liste.get(graphe.getNumArrivee()).setTempsDepuisSource(Double.POSITIVE_INFINITY);
				graphe.resorberFeu();
				//System.out.println(graphe) ;
				return new LinkedList<Integer>(Arrays.asList(nbEssais)); // on retourne le nombre d'essais avec un chemin vide
			}
		}
		return getCheminParcouru(nbEssais, graphe, carte) ; // Sinon, on retourne le nombre d'essais avec le plus court chemin trouvé
	}

	/**
	 * Méthode implémentant l'algorithme de Dijsktra pour résoudre notre problème de plus court chemin
	 * @param graphe le graphe dans lequel on cherche le plus court chemin
	 * @param carte Fenêtre d'affichage du problème
	 */
	public static LinkedList<Integer> dijkstra(Graphe graphe, Carte carte) {

		ArrayList<Sommet> liste = graphe.getListeSommets() ;		// Pour améliorer la complexité du programme, on pourrait utiliser ici une 
																	// file de priorité avec comme critère de comparaison le temps depuis la source
																	// à la place de cet ArrayList
		
		liste.get(graphe.getNumSource()).setTempsDepuisSource(0);
		int nbEssais = 0;
		HashSet<Integer> aVisiter = new HashSet<Integer>();
		for(int i = 0 ; i < graphe.getNumSommets() ; i++) aVisiter.add(i) ;

		while (aVisiter.contains(graphe.getNumArrivee())) { // tant qu'on a pas visité le sommet final (mécaniquement, si on a visité le sommet final, alors on a le chemin minimum et la distance finale)
			int min_v = -1 ;
			double min = Double.MAX_VALUE ;
			for(Integer i : aVisiter) {
				if(liste.get(i).getTempsDepuisSource() < min) { // on visite le chemin le plus proche de la source
					min = liste.get(i).getTempsDepuisSource() ;
					min_v = i ;
				}
			}

			aVisiter.remove(min_v);
			nbEssais += 1;

			Sommet sMin = liste.get(min_v) ;
			for (int i = 0; i < sMin.getListeAdjacence().size(); i++) {
				Sommet voisinSommet = liste.get(sMin.getListeAdjacence().get(i).getDestination()); //sommet voisin
				double voisinPoids = sMin.getListeAdjacence().get(i).getPoids() ; //poids de l'arête séparant min_v et son voisin
				if(voisinSommet.getTempsDepuisSource() > sMin.getTempsDepuisSource() + voisinPoids) { // si la distance est moins importante
					voisinSommet.setTempsDepuisSource(sMin.getTempsDepuisSource() + voisinPoids) ; // alors on l'actualise
					voisinSommet.setPrecedent(sMin) ;
				}
			}

			try { 
				if(carte != null) { // si on a décidé d'afficher l'exécution de l'algorithme
					carte.update(graphe, min_v); // alors on actualise la fenêtre
					Thread.sleep(attente); // et on temporise
				}
			} catch(InterruptedException e) {
				System.out.println("stop");
			}
		}
		return getCheminParcouru(nbEssais, graphe, carte) ;
	}

	/**
	 * Méthode permettant de retourner une reconstruction du plus court chemin trouvé et le nombre d'essais qu'il a necéssité pour l'obtenir
	 * @param nbEssais nombre de sommets visités avant de trouver le plus court chemin
	 * @param graphe le graphe dans lequel on a cherché le plus court chemin
	 * @param carte Fenêtre d'affichage du problème, on montre le plus court chemin s'il existe
	 * @return une liste à un seul élément si le plus court chemin n'existe pas
	 */
	private static LinkedList<Integer> getCheminParcouru(int nbEssais, Graphe graphe, Carte carte) {
		LinkedList<Integer> chemin=new LinkedList<Integer>();
		chemin.addFirst(graphe.getNumArrivee());
		Sommet prev = graphe.getListeSommets().get(graphe.getNumArrivee()).getPrecedent() ;
		
		while(prev.getCle() != graphe.getNumSource()) {
			chemin.addFirst(prev.getCle());
			prev = prev.getPrecedent() ;
		}
		
		chemin.addFirst(graphe.getNumSource()) ;
		if(carte != null && graphe.getListeSommets().get(chemin.getLast()).getTempsDepuisSource() != Double.POSITIVE_INFINITY) carte.addPath(graphe, chemin); // si le chemin existe, alors on l'affiche
		chemin.addFirst(nbEssais); 
		return chemin;
	}
}



