package main.menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import main.core.Graphe;
import main.core.algorithmie.GestionAlgos;
import main.core.algorithmie.TypeAlgo;
import main.core.algorithmie.TypeHeuristique;
import main.core.algorithmie.complexite.Rapport;
import main.io.EcritureResultats;
import main.io.EntreeClavier;
import main.io.Lecture;
import main.menu.utils.AffichageConsole;
import main.menu.utils.FonctionnalitesCommunes;
import main.vue.Carte;

/**
 * Classe statique contenant permettant d'accéder aux différentes fonctionnalités liées au projet de recherche du plus court chemin
 */
public class MenuPlusCourtChemin {
	
	/**
	 * Menu principal de la classe, permet de choisir parmi les différentes actions à effectuer puis de fermer proprement le programme
	 * @param sc Scanner permettant de récuperer les différents choix de l'utilisateur
	 * @param rapports Liste qui contiendra les rapports des différents algorithmes utilisés
	 * @param cartes Contient les fenêtres qui seront ouvertes pour la visualisation de l'exécution des algorithmes
	 */
	protected static void menuPCC(Scanner sc, ArrayList<Rapport> rapports, LinkedList<Carte> cartes) {
		String dossier = "" ;
		ArrayList<String> fichiersG = new ArrayList<String>() ;
		
		AffichageConsole.menuChoixFichiersPCC() ;
		int choix = EntreeClavier.getEntierDansIntervalleInclu(0, 3, sc) ; // choix de la fonctionnalité
		
		boolean afficheCarte = true ; // choix de la visualisation ou non
		if(choix != 0) {
			AffichageConsole.choixAffichageCarte();
			afficheCarte = (EntreeClavier.getEntierDansIntervalleInclu(0, 1, sc) == 1)?true:false ;
		}
		
		if(afficheCarte) main.core.algorithmie.Algos.attente = EntreeClavier.getAttente(afficheCarte, sc) ; // choix du temps de temporisation si visualisation
		TypeHeuristique heuristique = null ;
		
		switch(choix) {
		case 1:
			System.out.print("Veuillez entrer le chemin absolu vers un fichier G : ") ;
			String fichier = EntreeClavier.getCheminValideAvecExtension(sc, "g") ;
			TypeAlgo algo = EntreeClavier.getChoixAlgorithme(sc) ;
			if(algo == TypeAlgo.ASTAR) heuristique = EntreeClavier.getChoixHeuristique(sc, false) ;
			executerAlgosSurFichier(algo, heuristique, fichier, afficheCarte, cartes, rapports) ;
			if(rapports.size() != 1) { // Dans le cas où plus d'un algo a été exécuté
				System.out.println("Vous avez exécuté plusieurs algorithmes, nous ne pouvons plus enregistrer le meilleur chemin parcouru.") ;
			} else { // sinon on propose à l'utilisateur d'enregistrer les résultats
				System.out.println("Veuillez entrer le chemin absolu vers le dossier où vous souhaitez enregistrer le meilleur chemin parcouru : ") ;
				dossier = EntreeClavier.getDossierValide(sc) ;
				EcritureResultats.ecriturePCCVersFichier(rapports.get(0), dossier) ;
			}
			break ;
		case 2:
			System.out.print("Veuillez entrer le chemin absolu vers un dossier contenant des fichiers G : ") ;
			dossier = EntreeClavier.getDossierValide(sc) ;
			fichiersG = Lecture.getFichiersExtension(dossier, "g");
			choisirAlgoEtExecuterSurDossier(sc, dossier, fichiersG, afficheCarte, cartes, rapports) ;
			break ;
		case 3:
			fichiersG = FonctionnalitesCommunes.menuGenerationFichiersAleatoires(sc, "g") ;
			choisirAlgoEtExecuterSurDossier(sc, dossier, fichiersG, afficheCarte, cartes, rapports) ;
			break ;
		case 0:
			FonctionnalitesCommunes.finProgramme(sc);
			Carte.fermerFenetre(cartes) ; // puis on ferme les différentes fenêtres 
			break ;
		default:
			System.err.println("Erreur lors du choix de la fonctionnalité.") ;
			break ;
		}
		
		FonctionnalitesCommunes.enregistrementRapportsCSV(rapports, sc) ; // Après avoir exécuté la fonctionnalité demandée, on propose d'enregistrer en CSV
		Carte.fermerFenetre(cartes) ; // puis on ferme les différentes fenêtres 
		FonctionnalitesCommunes.finProgramme(sc) ; // et le scanner
	}
	
	/**
	 * Méthode lisant un fichier G pour en extraire un graphe, puis exécute un algo sur ce graphe pour extraire le plus court chemin
	 * @param fichier Fichier G dans lequel est contenu le graphe à étudier
	 * @param algo Algo choisi
	 * @param heuristique Heuristique choisie si l'algo est A*
	 * @param afficheCarte Si true, alors on affiche la visualisation
	 * @param cartes Les différentes visualisations
	 * @param rapports La liste de rapports qui contiendra les rapports d'exécution des différentes instances
	 */
	private static void lireGrapheDepuisFichierEtExecuterAlgo(String fichier, TypeAlgo algo, TypeHeuristique heuristique, boolean afficheCarte, 
															LinkedList<Carte> cartes, ArrayList<Rapport> rapports) {

		Scanner lecteurGraphe = Lecture.getScannerGraphe(fichier) ;
		if(lecteurGraphe == null) return ;
		Graphe graphe = Lecture.lireGrapheG(lecteurGraphe) ;
		
		Carte carte = null ;
		if(afficheCarte) carte = Carte.initCarte(carte, graphe, fichier, 1, algo, heuristique, cartes) ; // si visualisation, on initialise la fenêtre

		Rapport rapport = GestionAlgos.getRapportAlgo(algo, heuristique, fichier, 1, graphe, carte, false) ;
		rapports.add(rapport) ;
		
		if(afficheCarte) { // si visualisation, on temporise
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				System.out.println("stop");
			}
		}
		
		while(cartes.size() > Carte.nbCartesMaxSimultanee) Carte.fermerFenetre(cartes.poll()) ; // tant que le nombre de fenêtres est supérieur au max voulu, on défile et on ferme
	}
	
	
	/**
	 * Méthode permettant d'exécuter un ou plusieurs algos avec une ou plusieurs heuristique selon ce qu'aura choisi l'utilisateur
	 * @param algo Algo choisi, si null, on applique tous les algos et l'heuristique est null aussi
	 * @param heuristique Heuristique choisie si l'algo est A*, si null, alors on les applique toutes sauf celle réservée pour Ayutthaya
	 * @param f le fichier G à extraire
	 * @param afficheCarte Si true, alors on affiche la visualisation
	 * @param cartes Les différentes visualisations
	 * @param rapports La liste de rapports qui contiendra les rapports d'exécution des différentes instances
	 */
	private static void executerAlgosSurFichier(TypeAlgo algo, TypeHeuristique heuristique, String f, boolean afficheCarte, 
												LinkedList<Carte> cartes, ArrayList<Rapport> rapports) {
		if(algo != null) { // alors on exécute un seul algo
			if(algo == TypeAlgo.DIJSKTRA || heuristique != null) { // alors on execute une seule heuristique
				lireGrapheDepuisFichierEtExecuterAlgo(f, algo, heuristique, afficheCarte, cartes, rapports) ;
			} else if(heuristique == null) {
				for(TypeHeuristique heuristiqueEnum : TypeHeuristique.values()) { // mais potentiellement toutes les heuristiques
					if(heuristiqueEnum != TypeHeuristique.FEU) lireGrapheDepuisFichierEtExecuterAlgo(f, algo, heuristiqueEnum, afficheCarte, cartes, rapports) ;
				}
			}
		} else { // alors on execute tous les algos
			for(TypeAlgo algoEnum : TypeAlgo.values()) {
				if(algoEnum == TypeAlgo.ASTAR) { // et toutes les heuristiques
					for(TypeHeuristique heuristiqueEnum : TypeHeuristique.values()) {
						if(heuristiqueEnum != TypeHeuristique.FEU) lireGrapheDepuisFichierEtExecuterAlgo(f, algoEnum, heuristiqueEnum, afficheCarte, cartes, rapports) ;
					}
				} else lireGrapheDepuisFichierEtExecuterAlgo(f, algoEnum, null, afficheCarte, cartes, rapports) ;
			}
		}
	}
	
	/**
	 * Choix de l'algo et de l'heuristique, puis exécution sur les fichiers G contenus dans un dossier choisi
	 * @param sc Scanner permettant de récuperer les différents choix de l'utilisateur
	 * @param dossier Dossier dans lequel sont stockés les fichiers 
	 * @param fichiers Nom des fichiers
	 * @param afficheCarte Si true, alors on affiche la visualisation
	 * @param cartes Les différentes visualisations
	 * @param rapports La liste de rapports qui contiendra les rapports d'exécution des différentes instances
	 */
	private static void choisirAlgoEtExecuterSurDossier(Scanner sc, String dossier, ArrayList<String> fichiers, boolean afficheCarte, 
														LinkedList<Carte> cartes, ArrayList<Rapport> rapports) {
		TypeAlgo algo = EntreeClavier.getChoixAlgorithme(sc) ;
		TypeHeuristique heuristique = null ;
		if(algo == TypeAlgo.ASTAR) heuristique = EntreeClavier.getChoixHeuristique(sc, false) ;
		
		for(String f : fichiers) {
			executerAlgosSurFichier(algo, heuristique, (dossier.isEmpty()?"":(dossier+"/"))+f, afficheCarte, cartes, rapports) ;
		}
	}
}
