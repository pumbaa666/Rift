Pr�-requis)
Il vous faut une JVM pour faire tourner mon programme vu qu'il est �crit en java.
T�l�chargez le dernier JRE disponible ici, selon que votre machine est en 64 bits ou pas.
http://www.oracle.com/technetwork/java/javase/downloads/jre-7u4-download-1591157.html

0) Bien commencer
Normalement il est possible de d�marrer le robot en ne param�trant que le champ "Bouton de p�che" de l'onglet "D�marrage" et tout devrait marcher.
Assurez-vous d'avoir la barre d'action avec la canne � p�che active, pressez sur "D�marrer" et mettez votre souris � l'endroit o� vous souhaitez p�cher.

Ne mettez pas votre souris trop haut sur l'�cran, car il faut que le bouton "Tout prendre" soit sous la souris quand la fen�tre de butin apparait.

A ce propos, il faut d�sactiver le "Pillage automatique" si vous voulez que mon robot fonctionne.

D�tails des diff�rents �crans :

1) Endroits
Faites une liste des diff�rents endroits o� vous p�chez, si ils ont besoin d'�tre param�tr� diff�remment.
Les valeurs R, G, B correspondent � la diff�rence (en %) de couleur entre de l'eau calme et les remous qui indiquent qu'un poisson a mordu.
Si le robot ne d�tecte pas les poissons qui mordent, baissez les valeurs.
Si au contraire le robot presse pour un rien, augmentez la.

2) D�marrage
Il faut que votre canne � p�che soit affich�e dans votre barre d'action (par exemple sur la touche 1, par d�faut).
Cliquez sur la zone de texte "Bouton de p�che" et appuyez sur la touche correspondante.

Le "Temps au lancement" correspond au temps que va attendre le robot avant d'effectuer la 1�re action, pour vous permettre de placer votre souris o� vous voulez p�cher.
A chaque poisson p�ch� il est possible que la souris se d�place un petit peu autour du point de p�che initial, il faut pour cela param�trer les valeurs X et Y.
A la base j'avais impl�ment� cette option pour �viter de me faire gauler par Trion si jamais ils v�rifient un peu les logs mais je me suis rendu compte que �a pouvait �tre utile pour p�cher une fois en eau profonde et la fois d'apr�s en eau peu profonde.

3) Arr�t
Par d�faut (sans rien cocher) le robot ne s'arr�te jamais, mais il est possible de param�trer la fin apr�s
- un certain temps pass� � p�ch�
- avoir p�ch� un certain nombre de poisson
- un certain nombre d'echec
- ou tout �a � la fois (par ex : Soit 100 poissons p�ch�, soit 20 �chec)

Une fois que le robot s'arr�te il peut :
- S'�teindre (quitte le programme)
- Eteindre Rift (lance la commande /quit)
- Eteindre Windows apr�s 1 minute
- Sauver les logs dans le dossier /logs
- Et bien sur tout �a � la fois

4) App�ts
Voici l'�cran le plus complexe, il permet d'ajouter des app�ts � votre canne automatiquement.
Commencez par ouvrir vos sacs dans le jeu puis cliquez sur "+ app�t".
Une nouvelle fen�tre apparait, cliquez sur Apprendre.
Vous avez 6 secondes pour placer votre souris sur l'app�t dans votre sac. A la fin du d�compte les champs X et Y seront remplis avec les coordonn�es de la souris.
Remplissez ensuite le nom que vous voulez donner � votre app�t (si possible le m�me que dans le jeu pour plus de clart� ^^) ainsi que le nombre d'utilisation max et/ou le temp maximum.
En pratique c'est soit un nombre d'utilisation, soit un temps pr�d�fini.
Par exemple le "Leurre magn�tique temporaire" (pour les artefacts) dure 10 minutes, alors que l'"App�t pour poisson rare" est utilisable 5 fois.
Cliquez sur "Ajouter", cela cr�e une ligne dans la 1�re fen�tre, onglet "App�t".
Faites "Fermer" et c'est bon.

Renseignez l'emplacement de votre canne � p�che dans le sac de la m�me mani�re en pressant sur "+ canne" et vous pourrez utiliser les app�ts.

Le robot appliquera un nombre fini d'app�t, il se param�tre dans la zone � c�t� de "Appliquer".

5) Stats
Assez parlant

6) Logs
Le programme est tr�s bavard, tout les logs se trouveront ici.
Vous pouvez :
- les copier dans le presse-papier (faites ctrl-v dans un bloc-note pour les coller)
- les sauver dans le dossier /logs
- effacer tout le texte