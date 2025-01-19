# SAE_3.03_reseau_puissance_4

## Manuel d'utilisation

### Executer la commande de compilation :
- bash compil.sh

### Executer les commande pour lancer le serveur et le client dans 2 terminaux différents :
- bash serveur.sh
- bash client.sh

## Remarques
### Pour la partie BD:
Il faut d'abord se connecter à mysql dans un terminal puis dans une database faire la commande :
- mysql -h <nom_du_serveur_mysql> -u <nom_utilisateur> -p
- source Puissance4.sql;

Cela va créer les tables pour les enregistrements.

Ensuite, il faut changer dans le code, la connexion à la base de donnée. Dans le fichier ExecServ.java ligne 8. Vous devez entrer le nom du serveur mysql, le nom de la database, le nom de l'utilisateur mysql, et le mot de passe.

## Pour lancer l'interface graphique
Il faut configurer les liens des librairies javafx s'ils ne sont pas déjà configurés.

Ensuite vous pouvez :
- soit exécuter le fichier start_puissance4.sh
- soit exécuter le fichier AppliJeu.java dans le répertoir Puissance4_graphic

#### Note :
L'interface graphique n'est pas lié aux client ni aux serveur ni a la bd.

## Commandes pour le client

``` bash
connect <pseudo_joueur>   # Pour vous connecter
bonjour                   # Pour connaître votre pseudo
players                   # Pour connaître les joueurs qui peuvent être défiés
ask <nom_joueur>          # Pour affronter un autre joueur
historique                # Pour connaître l'historique de vos parties
score                     # Pour connaître votre score
