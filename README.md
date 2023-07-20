# RandomBagReward
Projet initial :
https://github.com/la-taverne-mc/RandomReward

RandomBagReward correspond à la version RandomReward.2.0
Ce plugin permet d'offrir des récompenses à chaques vote d'un joueur via une commande (nécessite un plugin de gestion de vote pour appeler la commande ).
Les votes sont tirées aléatoirement dans une liste de récompenses qui se trouve dans le fichier [rewards.yml](https://github.com/BlackFox3000/RandomBagReward/blob/master/src/main/java/fr/lataverne/randomreward/rewards.txt)

Avantage de notre plugin :
- Gestion du gain des joueurs hors-ligne
- Prise en charge de gain de récompense par % propre à chaque item

## Commandes
-rr bag : ouvre une gui vide, qui peu servire de poubelle actuellement ... (disponible op uniquement) [interface a venir..]
-rr baglist : affiche la première page de contenu du sac, si le sac n'est pas vide
-rr bag list : même commande que baglist
-rr baglist [numero_page] : affiche la [numero_page] page de contenu du sac, si le sac n'est pas vide ou si la page existe
-rr space : affiche l'espace disponible de votre inventaire

-rr get [index_objet] : Récupère l'objet à l'emplacement [index_objet] du bag si existant
-rr getAll : Remplit l'espace vide de votre inventaire tant qu'il y a des emplacements libres avec le contenu de votre sac tant qu'il reste des items dans le sac

-rr list : Affiche les récompenses et leurs pourcentages associés 
-rr give [pseudo] : Donne une récompense aléatoire dans le sac du joueur désigné
-rr give [pseudo] [quantité] : Donne une [quantité] de récompense dans l'inventaire du joueur sélectionné

-rr help : affiche les commandes joueurs
-rr adminhelp : affiches les commandes admin

## Récompenses
RandomReward est compatible actuellement avec le plugin ItemReward.

Il n'est pas compatible avec :
- des objets générés par commande (ne lis pas de JSON)
- des équipements enchantés
- des livres enchantés

## Permissions
help est fourni par défaut pour tout les utilisateurs

- permission :commande1, commande2
- rr.bag.read : baglist, bag list, baglist [numero_page] space
- rr.bag.read.gui : bag
- rr.bag.get : get [index_objet], getAll
- rr.give : list, give
- rr.admin.help



## Installation
Chargez le plugin une première fois pour générer les différents fichiers et dossiers :
- Dossier players où seront stocker les différents bags de joueurs exemple: [players](https://github.com/BlackFox3000/RandomBagReward/tree/master/src/main/java/fr/lataverne/randomreward/players)
- [rewards.yml](https://github.com/BlackFox3000/RandomBagReward/blob/master/src/main/java/fr/lataverne/randomreward/rewards.txt)
Dans ce fichier vous devrez définir les récompenses possibles dans le format :
```yml
# plugin  item quantite chance(xx,xx)
minecraft netherite_scrap 1 2.3
itemreward ULU 1 1.6
```
Une fois les récompenses terminées, il faut recharger le plugin.

## Compatibilité
- [itemReward](https://github.com/la-taverne-mc/ItemReward)
- [itemsAdder](https://itemsadder.devs.beer/)
- [ecoItems](https://github.com/Auxilor/EcoItems)
- [1.20]


## À venir
- Bag GUI (visible en faisant /rr bag actuelement disponible uniquement au OP, sert actuelement de grosse poubelle) 
 
