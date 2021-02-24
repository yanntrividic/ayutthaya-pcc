package main.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import main.core.Graphe;

/**
 * Classe statique permettant la lecture de fichiers, particulièrement des fichiers G et G2
 */
public class Lecture {
		
	/**
	 * Permet d'obtenir la liste de fichiers ayant une certaine extension dans un dossier
	 * @param dossier Le dossier où chercher les fichiers (ce dossier doit exister sinon la méthode lèvera une exception)
	 * @param extension Le type de fichier que l'on veut extraire
	 * @return Une liste de fichiers triée par ordre alphabétique
	 */
	public static ArrayList<String> getFichiersExtension(String dossier, String extension) {
		File f = new File(dossier) ;
		String [] liste = f.list() ; // Liste les fichiers présents dans ce dossier
		ArrayList<String> fichiers = new ArrayList<String>() ; // Pour chaque fichier, on regarde si ce fichier a l'extension que l'on recherche
		for(int i = 0 ; i < liste.length ; i++)	if(!new File(dossier+"/"+liste[i]).isDirectory() && liste[i].endsWith("."+extension)) fichiers.add(liste[i]) ; 
		System.out.println("Nous avons trouvé "+fichiers.size()+" fichiers "+extension.toUpperCase()+" dans le dossier "+dossier+"\n") ;
		Collections.sort(fichiers) ;
 		return fichiers ;
	}
	
    protected static boolean dossierExiste(String chemin) {
    	File f = new File(chemin) ;
    	if(f.exists() && f.isDirectory()) return true ;
    	return false ;
    }
	
    /**
     * Méthode demandant à l'utilisateur de rentrer un chemin vers un fichier valide. La méthode continue de demander tant que le fichier n'est pas valide
     * @param sc Scanner 
     * @return Un chemin valide vers un fichier
     */
	public static String getFichier(Scanner sc) {
		String fichier = "";
		File f ;
		do {
			System.err.print("La lecture de votre dossier de graphes a échoué. Veuillez indiquer un chemin valide pour votre fichier : ") ;
			fichier = sc.nextLine() ;
			f = new File(fichier) ;
		} while(!f.exists()) ;
		return fichier ;
	}
	
	/**
	 * Méthode retournant un graphe lu à partir d'un fichier G ouvert avec un Scanner
	 * @param lecteurGraphe Un Scanner ouvert sur le fichier G contenant le graphe à extraire
	 * @return Le graphe contenu dans le fichier G
	 * @see Graphe
	 */
	public static Graphe lireGrapheG(Scanner lecteurGraphe) {
		Graphe graphe = new Graphe(); // Initialise le graphe avant de le lire
		String data = "" ; // contiendra les données parsées au fur et à mesure
		
		try {
			for (int i=0; i < 3; i++) data = lecteurGraphe.nextLine(); // les deux premières lignes contiennent "==Metadata==" et "=Size="
			
			graphe.setNbLignes(Integer.parseInt(data.split("=")[1])) ; // Lecture du nombre de lignes...
			data = lecteurGraphe.nextLine();
			graphe.setNbColonnes(Integer.parseInt(data.split("=")[1])) ; // ... et du nombre de colonnes
	
			data = lecteurGraphe.nextLine(); // on saute cette ligne car il s'agit de "=Types="
			data = lecteurGraphe.nextLine();
	
			// Lire les différents types de cases
			while (!data.equals("==Graph==")) {
				String name = data.split("=")[0]; // correspond au type de sol (G, W, B ou S dans graph.txt)
				int time = Integer.parseInt(data.split("=")[1]); // correspond à la durée associée (respectivement 1, 1000, 20 et 30)
				data = lecteurGraphe.nextLine();
				String color = data; // respectivement (vert, gris, bleu et jaune)
				graphe.ajouterTypeDeSol(name, time); // on stocke les données parsées précédemment dans deux HashMaps
				graphe.ajouterCouleurDeSol(time, color);
				data = lecteurGraphe.nextLine(); // puis on itère
			}
	
			// A ce stade là, on est sur la ligne "==Graph=="
			// On ajoute les sommets dans le graphe (avec le bon type)					
			for (int line=0; line < graphe.getNbLignes(); line++) { // pour chaque ligne...
				data = lecteurGraphe.nextLine() ;
				for (int col=0; col < graphe.getNbColonnes(); col++) { // ... et pour chaque colonne
					graphe.addSommet(graphe.getTypesDeSol().get(String.valueOf(data.charAt(col))));	// on ajoute un sommet au graph et on initialise son indivTime avec la valeur 
				} // associée à au String contenu dans groundTypes			
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("Le fichier n'a pas pu être lu correctement car il n'est pas au format.") ;
			return null ;
		}

		graphe.parcoursSommetsGrapheRectangulaire(true); // ajout des arêtes avec diagonales
		
		data = lecteurGraphe.nextLine(); 
		data = lecteurGraphe.nextLine(); // ces deux points sont encodés avec des coordonnées x et y 
		graphe.setNumSource(Integer.parseInt(data.split("=")[1].split(",")[0]) * graphe.getNbColonnes() + Integer.parseInt(data.split("=")[1].split(",")[1]));
		data = lecteurGraphe.nextLine(); // ils sont ensuite convertis en numéro de sommet par rapport au nombre de lignes et de colonnes puis ajouté au graphe
		graphe.setNumArrivee(Integer.parseInt(data.split("=")[1].split(",")[0]) * graphe.getNbColonnes() + Integer.parseInt(data.split("=")[1].split(",")[1]));
		
		lecteurGraphe.close(); // On a lu toutes les valeurs qu'il y avait à lire dans le fichier, on peut le fermer
		
		return graphe ;
	}
	
	/**
	 * Méthode retournant un graphe lu à partir d'un fichier G2 ouvert avec un Scanner
	 * @param lecteurGraphe Un Scanner ouvert sur le fichier G2 contenant le ou les graphes à extraire
	 * @return Les instances de graphes contenues dans le fichier G2
	 * @see Graphe
	 */
	public static ArrayList<Graphe> lireGrapheG2(Scanner lecteurGraphe) {
		String data = "";
		int nbInstances = Integer.parseInt(lecteurGraphe.nextLine()) ; // La première ligne contient le nombre d'instances

		ArrayList<Graphe> instances = new ArrayList<Graphe>(nbInstances) ; // pour chaque instance, on réserve une entrée de la ArrayList
		boolean sourceFeuAjoutee = false, departAjoute = false, arriveeAjoutee = false ;	// ces booléens servent à vérifier que 
																					// les trois sommets "particuliers" ont bien été ajoutés au graphe
		try {
			for(int i = 0 ; i < nbInstances ; i++) {
				
				Graphe graphe = new Graphe(); // Initialise simplement une ArrayList de Vertex
		
				data = lecteurGraphe.nextLine() ;
				graphe.setNbLignes(Integer.parseInt(data.split(" ")[0])) ; // Lecture du nombre de lignes...
				graphe.setNbColonnes(Integer.parseInt(data.split(" ")[1])) ; // ... et du nombre de colonnes
				
				graphe.ajouterTypeDeSol(".", 1);
				graphe.ajouterTypeDeSol("#", Integer.MAX_VALUE);
				graphe.ajouterTypeDeSol("F", Integer.MAX_VALUE-1); // ici, on met des valeurs différentes pour que les types de sols puissent bien être lus à l'affichage
				graphe.ajouterTypeDeSol("D", 1.01); // ces valeurs seront lissées lors du calcul du poids des arêtes
				graphe.ajouterTypeDeSol("S", 1.02);
				
				graphe.ajouterCouleurDeSol(1, "gray"); // correspondance des valeurs dans les couleurs de sols
				graphe.ajouterCouleurDeSol(Integer.MAX_VALUE, "black");
				graphe.ajouterCouleurDeSol(Integer.MAX_VALUE-1, "red");
				graphe.ajouterCouleurDeSol(1.01, "blue");
				graphe.ajouterCouleurDeSol(1.02, "yellow");
						
				
				for (int line=0; line < graphe.getNbLignes(); line++) { // pour chaque ligne...
					data = lecteurGraphe.nextLine();
					for (int col=0; col < graphe.getNbColonnes(); col++) { // ... et pour chaque colonne 
						char c = data.charAt(col) ;				
						graphe.addSommet(graphe.getTypesDeSol().get(String.valueOf(c))); 	// on ajoute un sommet au graph et on initialise le poids
						if(c == 'S') {														// du sommet avec la valeur contenue dans le type de sol
							graphe.setNumArrivee(graphe.getNumSommets()-1);
							arriveeAjoutee = true ;
						}
						if(c == 'D') {
							graphe.setNumSource(graphe.getNumSommets()-1); // on ajoute le sommet au graphe comme les autres
							departAjoute = true ; // et on change la valeur du booléen associé
						}
						if(c == 'F') {
							graphe.setNumFeu(graphe.getNumSommets()-1);
							sourceFeuAjoutee = true ;
						}
					}		
				}
							
				// On vérifie que les trois sommets spéciaux ont bien été ajoutés
				if(!(arriveeAjoutee && departAjoute && sourceFeuAjoutee)) throw new Exception("Une des cases clés n'a pas pu être correctement ajoutée") ; 				
				
				graphe.parcoursSommetsGrapheRectangulaire(false); // ajout des arêtes sans diagonales
				instances.add(graphe) ; 
			}
		} catch(Exception e) {
			System.err.println("Le fichier n'a pas pu être lu correctement car il n'est pas au format.") ;
			e.printStackTrace();
			return null ;
		}
		return instances ; // On retourne la liste de graphes lue
	}
	
	/**
	 * Méthode retournant une liste de graphes si le fichiers G2 existe
	 * @param fichierG2 fichier G2 dont on va essayer d'extraire des instances de graphes
	 * @return liste d'instances de graphes ou null si le fichier n'existe pas
	 */
	public static ArrayList<Graphe> lireInstancesDepuisFichier(String fichierG2) {
		Scanner lecteurGraphe = Lecture.getScannerGraphe(fichierG2) ;
		if(lecteurGraphe == null) return null ;
		return Lecture.lireGrapheG2(lecteurGraphe) ;
	}
	
	/**
	 * Méthode retournant un Scanner ouvert sur un fichier si le fichier existe, et retourne null sinon
	 * @param chemin chemin quelconque
	 * @return Scanner ouvert sur un fichier si le fichier existe, et retourne null sinon
	 */
	public static Scanner getScannerGraphe(String chemin) {
		File myObj = new File(chemin);		
		Scanner sc = null ;
		try {
			sc = new Scanner(myObj);
		} catch (FileNotFoundException e) {
			System.err.println("Le fichier "+chemin+" n'a pas pu être lu correctement.") ;
			return null ;
		}
		return sc ;
	}
}
