# Spring_Batch_6

## Documentataion Spring Batch 6.0.2

### Architecture de Spring Batch 6

Documentation issue de [spring-batch-architecture.html](https://docs.spring.io/spring-batch/reference/spring-batch-architecture.html)

![Spring Batch Architecture](Documentation_ressources/image.png)

Cette architecture en couches met en évidence trois composants principaux de haut niveau :
l’application, le noyau et l’infrastructure.

* L’application contient tous les traitements par lots et le code personnalisé écrit par les développeurs à l’aide de Spring Batch.
* Le noyau Batch contient les classes d’exécution essentielles au lancement et au contrôle d’un traitement par lots. Il inclut les implémentations de `read` JobOperator, Job`write` et `Stepretry`.
* L’application et le noyau reposent sur une infrastructure commune. Cette infrastructure comprend des lecteurs et des écrivains communs, ainsi que des services (comme `retry` ) RetryTemplate, utilisés à la fois par les développeurs de l’application (lecteurs et écrivains, tels que `read` et `write` ) et par le framework lui-même (`retry`, qui est sa propre bibliothèque).ItemReaderItemWriter.

## Travaux Dirigés

Vous allez créer un service qui importe des données à partir d'une feuille de calcul CSV, les transforme à l'aide de code personnalisé et stocke les résultats finaux dans une base de données.

### Initialisation du projet

* Initialiser votre projet à l'aide de Spring Initializr, et y ajouter les dependences **Spring Batch** et **HyperSQL Database**. Nous utiliserons maven et java 25 pour le projet.
* En général, votre client ou un analyste métier fournit une feuille de calcul. Dans cet exemple simple, vous trouverez des données fictives dans [input](src/main/resources/sample-data.csv).
* Ensuite, vous devez écrire un script SQL pour créer une table destinée à stocker les données. Vous trouverez un tel script ici [schema](src/main/resources/schema-all.sql). **Remarques** : Spring Boot exécute schema-@@platform@@.sql automatiquement au démarrage. **-all** C'est le comportement par défaut pour toutes les plateformes.

### Création d'un enregistrement (Record) d'une personne

Voir fichier [Record Person](src/main/java/ruffinjy/spring_batch_demo/domain/Person.java).
Vous pouvez instancier le record d'une Person avec le prénom et le nom via le constructeur.

### Création d'un processus intermédiaire

En traitement par lots, il est courant d'ingérer des données, de les transformer, puis de les acheminer vers une autre destination. Ici, vous devez écrire un transformateur simple qui convertit les noms en majuscules.

Voir fichier [Transforme datas](src/main/java/ruffinjy/spring_batch_demo/batch_processor/PersonItemProcessor.java).
