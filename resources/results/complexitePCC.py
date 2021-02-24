import matplotlib.pyplot as plt
import pandas
import matplotlib.pyplot as plt
import sys
import avg

plt.close('all')

if len(sys.argv) == 2 :
	data = pandas.read_csv(sys.argv[1]) #lecture des données du fichier passé en ligne de command
else :
	data = pandas.read_csv('results_gAlea_10_150.csv')

data = data.sort_values(by ='nbSommetsGraphe') #tri des données
data = data.drop(['nomFichier', 'numInstance', 'score', 'tempsChemin', 'nbSommetsParcourus'], axis=1) #on enlève les colonnes superflues

dijsktra = data.loc[data['nomAlgo'] == 'Dijsktra'].drop(['heuristique', 'nomAlgo'], axis=1)

data.drop(['nomAlgo'], axis=1)

manhattan = data.loc[data['heuristique'] == 'Manhattan'].drop(['heuristique'], axis=1) #extraction par catégories
euclidienne = data.loc[data['heuristique'] == 'euclidienne'].drop(['heuristique'], axis=1) 

x = manhattan['nbSommetsGraphe'].tolist() # les valeurs en x

yManhattan = manhattan['tempsCalcul'].tolist() # les valeurs en y
yEuclidienne = euclidienne['tempsCalcul'].tolist()
yDijsktra = dijsktra['tempsCalcul'].tolist()

plt.xlabel("Nombre de sommets du graphe")
plt.ylabel("Temps de calcul de l'algorithme (en ms - échelle logarithmique)")
plt.title("Complexité des différents algorithmes utilisés pour trouver le PCC")

plt.yscale("log") # échelle logarithmique
plt.plot(x, yManhattan, label='A* - Manhattan')
plt.plot(x, yEuclidienne, label='A* - Euclidienne')
plt.plot(x, yDijsktra, label='Dijkstra')

print("Complexité moyenne en ms - Manhattan : 		",avg.Average(yManhattan))
print("Complexité moyenne en ms - euclidienne :	",avg.Average(yEuclidienne))
print("Complexité moyenne en ms - Dijkstra : 		",avg.Average(yDijsktra))

plt.legend()
plt.show()