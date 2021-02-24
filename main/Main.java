package main;

import java.util.Scanner;
import main.menu.MenuChoixProjet ;
import main.menu.utils.AffichageConsole;

/**
 * Classe d'entrée dans le programme, initialise un Scanner qui sera utilisé tout au long de l'exécution du programme
 * Affiche un court texte d'introduction
 * @author Sylvain Lobry
 * @author Yann Trividic
 */
public class Main {
	public static void main(String[] args) {
		AffichageConsole.afficherIntro() ;
		Scanner sc = new Scanner(System.in) ;
		MenuChoixProjet.choixProjet(sc) ;
	}
}
