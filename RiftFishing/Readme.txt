Pré-requis)
Il vous faut une JVM pour faire tourner mon programme vu qu'il est écrit en java.
Téléchargez le dernier JRE disponible ici, selon que votre machine est en 64 bits ou pas.
http://www.oracle.com/technetwork/java/javase/downloads/jre-7u4-download-1591157.html

0) Bien commencer
Normalement il est possible de démarrer le robot en ne paramétrant que le champ "Bouton de pêche" de l'onglet "Démarrage" et tout devrait marcher.
Assurez-vous d'avoir la barre d'action avec la canne à pêche active, pressez sur "Démarrer" et mettez votre souris à l'endroit où vous souhaitez pêcher.

Ne mettez pas votre souris trop haut sur l'écran, car il faut que le bouton "Tout prendre" soit sous la souris quand la fenêtre de butin apparait.

A ce propos, il faut désactiver le "Pillage automatique" si vous voulez que mon robot fonctionne.

Détails des différents écrans :

1) Endroits
Faites une liste des différents endroits où vous pêchez, si ils ont besoin d'être paramétré différemment.
Les valeurs R, G, B correspondent à la différence (en %) de couleur entre de l'eau calme et les remous qui indiquent qu'un poisson a mordu.
Si le robot ne détecte pas les poissons qui mordent, baissez les valeurs.
Si au contraire le robot presse pour un rien, augmentez la.

2) Démarrage
Il faut que votre canne à pêche soit affichée dans votre barre d'action (par exemple sur la touche 1, par défaut).
Cliquez sur la zone de texte "Bouton de pêche" et appuyez sur la touche correspondante.

Le "Temps au lancement" correspond au temps que va attendre le robot avant d'effectuer la 1ère action, pour vous permettre de placer votre souris où vous voulez pêcher.
A chaque poisson pêché il est possible que la souris se déplace un petit peu autour du point de pêche initial, il faut pour cela paramétrer les valeurs X et Y.
A la base j'avais implémenté cette option pour éviter de me faire gauler par Trion si jamais ils vérifient un peu les logs mais je me suis rendu compte que ça pouvait être utile pour pêcher une fois en eau profonde et la fois d'après en eau peu profonde.

3) Arrêt
Par défaut (sans rien cocher) le robot ne s'arrête jamais, mais il est possible de paramétrer la fin après
- un certain temps passé à pêché
- avoir pêché un certain nombre de poisson
- un certain nombre d'echec
- ou tout ça à la fois (par ex : Soit 100 poissons pêché, soit 20 échec)

Une fois que le robot s'arrête il peut :
- S'éteindre (quitte le programme)
- Eteindre Rift (lance la commande /quit)
- Eteindre Windows après 1 minute
- Sauver les logs dans le dossier /logs
- Et bien sur tout ça à la fois

4) Appâts
Voici l'écran le plus complexe, il permet d'ajouter des appâts à votre canne automatiquement.
Commencez par ouvrir vos sacs dans le jeu puis cliquez sur "+ appât".
Une nouvelle fenêtre apparait, cliquez sur Apprendre.
Vous avez 6 secondes pour placer votre souris sur l'appât dans votre sac. A la fin du décompte les champs X et Y seront remplis avec les coordonnées de la souris.
Remplissez ensuite le nom que vous voulez donner à votre appât (si possible le même que dans le jeu pour plus de clarté ^^) ainsi que le nombre d'utilisation max et/ou le temp maximum.
En pratique c'est soit un nombre d'utilisation, soit un temps prédéfini.
Par exemple le "Leurre magnétique temporaire" (pour les artefacts) dure 10 minutes, alors que l'"Appât pour poisson rare" est utilisable 5 fois.
Cliquez sur "Ajouter", cela crée une ligne dans la 1ère fenêtre, onglet "Appât".
Faites "Fermer" et c'est bon.

Renseignez l'emplacement de votre canne à pêche dans le sac de la même manière en pressant sur "+ canne" et vous pourrez utiliser les appâts.

Le robot appliquera un nombre fini d'appât, il se paramètre dans la zone à côté de "Appliquer".

5) Stats
Assez parlant

6) Logs
Le programme est très bavard, tout les logs se trouveront ici.
Vous pouvez :
- les copier dans le presse-papier (faites ctrl-v dans un bloc-note pour les coller)
- les sauver dans le dossier /logs
- effacer tout le texte