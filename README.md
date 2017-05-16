# DaylightedAndroidExo

Hello!

Voici une (petite) doc de l'application Android.

Le code executé se trouve dans le dossier "DaylightedExercice/app/src/main/java/com/aubin/victor/daylightedexercice".

Il y a 4 classes Java: 

RequestManager.java : Gère les requêtes HTTP et leurs réponses. Cette classe fourni une file de requetes HTTP en les executant de façon asynchrone.
MainActivity.java : Classe principale de l'appli. Cette classe utilise toutes les autres classes pour les différentes fonctionnalités.
MyListAdapter.java : Cette classe formate les données du JSON téléchargé dans une liste basée sur le tempalte rowlayout.xml qui sera affichée. 
DatabaseManager.java : Cette classe gère la mini base de donnée intégré à l'application (sauvegarde et récupération des données).

Lorsque l'application est lancée, la méthode onCreate() de la classe MainActivity est appelée. Cette méthode va tenter de récupérer le cookie puis dans le cas échant télécharger le JSON de l'api et envoyer ces données à MyListAdapter.
