# Plus court chemin et le labyrinthe d'Ayutthaya

Version : 1.0 
Auteur : Trividic Yann (à partir du code de Sylvain Lobry)

**Contexte :**
Cette application a été développée dans le but d'étudier le fonctionnement de l'algorithme A* et de l'algorithme de Dijkstra dans le cadre du cours Algorithmie Avancée de l'Université de Paris. Celle-ci répond à deux problématiques : la recherche du plus court chemin reliant deux sommets dans un graphe pondéré, et la résolution du problème du labyrinthe d'Ayutthaya (plus d'informations ![ici](https://www.urionlinejudge.com.br/judge/en/problems/view/1883)).

**Configuration minimale :**
- Pour l'utilisation du programme en lui-même
	JAVA SE.12
- Pour l'étude de la complexité algorithmique
	Python 3.6
	matplotlib 3.3.3
	pandas 1.1.5

**Installation du programme :**
Ouvrez un terminal et rendez vous dans le répertoire contenant l'archive JAR (téléchargeable dans la partie `tags` de cette page). Exécutez la commande `java -jar NOMDUJAR.jar`. Suivez les instructions affichées dans le terminal.

**Exécution du programme :**
Plusieurs fonctionnalités sont mises à la disposition de l'utilisateur.
- Générer des problèmes aléatoires
- Résoudre des problèmes à partir de fichiers `G` ou `G2` (voir plus bas la partie *Formats de fichiers*)
- Enregistrer les résultats sous diverses formes dans des fichiers `TXT` ou `CSV`
- Analyser l'efficacité des différents algorithmes (voir plus bas la partie *Complexité*)
Ces fonctionnalités sont toutes accessibles via les différents menus affiché dans votre terminal.

**Formats de fichiers :**
Pour répondre aux problématiques posées, deux formats de fichiers ont dû être mis en place. 
Pour le projet PCC, il s'agit du format de fichier `G`. Vous pourrez trouver des exemples de fichiers G dans le dossier `resources/graphes`, dont la syntaxe est la suivante (les parties à remplacer sont indiquées par <>) :

	==Metadata==
	=Size=
	nlines=<int : Le nombre de lignes>
	ncol=<int: Le nombre de colonnes>
	=Types=
	G=<int: Le temps nécessaire pour parcourir verticalement ou horizontalement une case de ce type>
	<string : La couleur de la case du type précédent (sont utilisables sans modification du code : « green »,
	« gray », « blue » et « yellow »)>
	W=<int: Le temps nécessaire pour parcourir une case de ce type>
	<string : La couleur de la case du type précédent (sont utilisables sans modification du code : « green »,
	« gray », « blue » et « yellow »)>
	...
	==Graph==<string : Une succession de ncols lettres, selon les types définis précédemment.>
	<string : Une succession de ncols lettres, selon les types définis précédemment.>
	... (nlines fois)
	==Path==
	Start=<int,int: les coordonnées (ligne, colonne) du point de départ>
	Finish=<int,int: les coordonnées (ligne, colonne) du point d’arrivée>

Pour le projet Ayutthaya, il s'agit du format `G2`. Des exemples sont aussi trouvables dans le dossier `resources/graphes`, et la syntaxe du format `G2` est la suivante :

	La première ligne du fichier contient le nombre d'instance N. Il peut y avoir beaucoup d'instances.
	S'en suivent N instances représentées ainsi : 
		<int: Le nombre de lignes> <int: Le nombre de colonnes>
		<string : Une succession de ncols caractères '#', '.', 'F', 'D' ou 'S', qui représentent respectivement un mur, une case vide, la source du feu, le sommet de départ et le sommet de sortie. Il n'y a qu'une seule source de feu, un seul sommet de départ et une seule sortie>
		... (nlines fois)

**Complexité :**
Des outils ont été mis a votre disposition pour analyser les différents algorithmes ainsi que les heuristiques utilisées. Ce sont des scripts Python contenus dans le dossier `resources/results`. Pour utiliser ces scripts, il faut les extraire de l'archive JAR puis chacun peut être exécuté en ligne de commande en entrant la commande `python3 NOMDUSCRIPT.py` (excepté le script `avg.py`). Sans argument, le script ouvrira les échantillons `CSV` déjà présents dans le dossier `resources/results`. Vous pouvez générer vos propres fichiers `CSV` à partir du programme Java et visualiser les résultats obtenus en rentrant la commande `python3 NOMDUSCRIPT.py CHEMINDUFICHIERGENERE.csv`.

Les scripts comportant `Ayutthaya` dans leur nom concernent les fichiers `CSV` extraits du projet Ayutthaya, et réciproquement avec les scripts comportant `PCC`.
