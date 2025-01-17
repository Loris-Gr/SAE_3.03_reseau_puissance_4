# SAE_3.03_reseau_puissance_4

## Commandes utiles 

javac -d Puissance4_v2/bin/ Puissance4_v2/bd/*.java Puissance4_v2/*.java
java -cp "Puissance4_v2/bin/:lib/mariadb-java-client-debian.jar" Puissance4Terminal
java -cp "Puissance4_v2/bin/:lib/mariadb-java-client-debian.jar" ExecServ
java -cp "Puissance4_v2/bin/:lib/mariadb-java-client-debian.jar" ExecClient


mysql -h servinfo-maria -u vergerolle -p

