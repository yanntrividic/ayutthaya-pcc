package main.menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import main.core.algorithmie.complexite.Rapport;
import main.io.EntreeClavier;
import main.menu.utils.AffichageConsole;
import main.menu.utils.FonctionnalitesCommunes;
import main.vue.Carte;

/**
 * Méthode statique initialisant quelques variables communes aux deux projets et permettant à l'utilisateur de chosir son projet
 */
public class MenuChoixProjet {
	public static void choixProjet(Scanner sc) {
		ArrayList<Rapport> rapports = new ArrayList<Rapport>() ;
		LinkedList<Carte> cartes = new LinkedList<Carte>() ;
		
		AffichageConsole.choixProjet() ;
		int choix = EntreeClavier.getEntierDansIntervalleInclu(0, 3, sc) ;
		
		switch(choix) {
		case 1 :
			MenuPlusCourtChemin.menuPCC(sc, rapports, cartes);
			break ;
		case 2 :
			MenuLabyrinthe.menuLabyrinthe(sc, rapports, cartes);
			break ;
		case 0 :
			FonctionnalitesCommunes.finProgramme(sc);
		}
	}
}
