import matplotlib.pyplot as plt
import pandas
import matplotlib.pyplot as plt
import sys
import avg

plt.close('all')

if len(sys.argv) == 2 :
	data = pandas.read_csv(sys.argv[1]) #lecture des données
else :
	data = pandas.read_csv('results_g2Alea_10_100.csv')

data = data.sort_values(by ='nbSommetsGraphe') #tri des données
data = data.drop(['nomAlgo', 'nomFichier', 'numInstance', 'score', 'tempsChemin', 'tempsCalcul'], axis=1) #on enlève les colonnes superflues

manhattan = data.loc[data['heuristique'] == 'Manhattan'].drop(['heuristique'], axis=1) #extraction par catégories
euclidienne = data.loc[data['heuristique'] == 'euclidienne'].drop(['heuristique'], axis=1) 
ayutthaya = data.loc[data['heuristique'] == 'Ayutthaya'].drop(['heuristique'], axis=1) 

x = manhattan['nbSommetsGraphe'].tolist() # les valeurs en x

yManhattan = manhattan['nbSommetsParcourus'].tolist() # les valeurs en y
yEuclidienne = euclidienne['nbSommetsParcourus'].tolist()
yAyutthaya = ayutthaya['nbSommetsParcourus'].tolist()

plt.xlabel("Nombre de sommets du graphe")
plt.ylabel("Nombre de sommets parcourus")
plt.title("Complexité de l'algorithme A* en fonction des différentes heuristiques utilisées")

plt.yscale("log") # échelle logarithmique
plt.plot(x, yManhattan, label='Manhattan')
plt.plot(x, yEuclidienne, label='Euclidienne')
plt.plot(x, yAyutthaya, label='Ayutthaya')

print("Nombre de sommets visités moyen - Manhattan : 	",avg.Average(yManhattan))
print("Nombre de sommets visités moyen - euclidienne :	",avg.Average(yEuclidienne))
print("Nombre de sommets visités moyen - Ayutthaya : 	",avg.Average(yAyutthaya))

plt.legend()
plt.show()