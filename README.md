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

Nous allons créer un service qui importe des données à partir d'une feuille de calcul CSV, les transforme à l'aide de code personnalisé et stocke les résultats finaux dans une base de données.
