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

* Initialiser votre projet à l'aide de Spring Initializr, et y ajouter les dependences `Spring Batch` et `HyperSQL Database`. Nous utiliserons maven et java 25 pour le projet.
* En général, votre client ou un analyste métier fournit une feuille de calcul. Dans cet exemple simple, vous trouverez des données fictives dans [input](src/main/resources/sample-data.csv).
* Ensuite, vous devez écrire un script SQL pour créer une table destinée à stocker les données. Vous trouverez un tel script ici [schema](src/main/resources/schema-all.sql). `Remarques` : Spring Boot exécute schema-@@platform@@.sql automatiquement au démarrage. `-all` C'est le comportement par défaut pour toutes les plateformes.

### Création d'un enregistrement (Record) d'une personne

Voir fichier [Record Person](src/main/java/ruffinjy/spring_batch_demo/domain/Person.java).
Vous pouvez instancier le record d'une Person avec le prénom et le nom via le constructeur.

### Création d'un processus intermédiaire

En traitement par lots, il est courant d'ingérer des données, de les transformer, puis de les acheminer vers une autre destination. Ici, vous devez écrire un transformateur simple qui convertit les noms en majuscules.

Voir fichier [Transforme datas](src/main/java/ruffinjy/spring_batch_demo/batch_processor/PersonItemProcessor.java).

`PersonItemProcessor` implémente l'interface de Spring Batch `ItemProcessor`. Cela facilite l'intégration du code dans un traitement par lots. Conformément à l'interface, vous recevez un objet entrant `Person`, que vous transformez ensuite en un objet en majuscules Person.

### Assemblage du job Batch

Il vous faut maintenant mettre en place le traitement par lots. Spring Batch fournit de nombreuses classes utilitaires qui réduisent le besoin d'écrire du code personnalisé. Vous pouvez ainsi vous concentrer sur la logique métier.

Pour configurer votre tâche, vous devez d'abord créer une classe `@Configuration` Spring comme dans l'exemple suivant [Configuration](src/main/java/ruffinjy/spring_batch_demo/configurations/BatchConfiguration.java). Cet exemple utilise une base de données en mémoire, ce qui signifie qu'une fois la tâche terminée, les données sont effacées. Ajoutez ensuite les beans suivants à votre classe `BatchConfiguration` pour définir les `@Bean` `reader`, `processor`, et un `writer`.

Le premier bloc de code définit l'entrée, le processeur et la sortie.

* `reader()` crée un `ItemReader`. Il recherche un fichier appelé _sample-data.csv_ et analyse chaque élément de ligne avec suffisamment d'informations pour le transformer en un objet `Person`.
* `processor()` crée une instance de la fonction `PersonItemProcessor` que vous avez définie précédemment, destinée à convertir les données en majuscules.
* `writer(DataSource)` crée un objet `ItemWriter`. Celui-ci est destiné au JDBC et obtient automatiquement un objet `DataSource` créé par Spring Boot. Il inclut l'instruction SQL nécessaire à l'insertion d'un seul enregistrement Person, piloté par des composants d'enregistrement Java.

Les blocs suivants définie le `Job` (la tâche) et les `Step` (les étapes).

* Les tâches sont composées `Step`, chacune pouvant impliquer un `reader`, un `processor`, et un `writer`.
* Vous listez ensuite chaque `Step` (bien que cette tâche ne comporte qu'un seul step). La tâche se termine et l'API Java génère un `Step` parfaitement configurée.
* Dans la définition d'un step, vous spécifiez la quantité de données à écrire simultanément. Ici, jusqu'à `3` enregistrements sont écrits à la fois. Ensuite, vous configurez le reader, processor, et writer à l'aide des beans injectés précédemment.
* La dernière partie de la configuration par lots permet d'être notifié de la fin de l'exécution [notification](src/main/java/ruffinjy/spring_batch_demo/notifications/JobCompletionNotificationListener.java). Le `JobCompletionNotificationListener` écoute lorsqu'une tâche est lancée `BatchStatus.COMPLETED`, puis utilise `JdbcTemplate` pour inspecter les résultats.

### Execution de l'application

`@SpringBootApplication` est une annotation de commodité qui ajoute tout ce qui suit :

* `@Configuration`: Indique que la classe est une source de définitions de beans pour le contexte de l'application.
* `@EnableAutoConfiguration`: Indique à Spring Boot de commencer à ajouter des beans en fonction des paramètres du classpath, des autres beans et de divers paramètres de propriétés. Par exemple, si le fichier spring-webmvc est présent dans le classpath, cette annotation signale l'application comme une application web et active des comportements clés, tels que la configuration d'un DispatcherServlet.
* `@ComponentScan`: Indique à Spring de rechercher d'autres composants, configurations et services dans le com/example package, lui permettant ainsi de trouver les contrôleurs.

Cette méthode `main()` utilise Spring Boot SpringApplication.run() pour lancer une application. Avez-vous remarqué l'absence totale de code XML ? Aucun web.xml fichier n'est présent non plus. Cette application web est entièrement écrite en Java et vous n'avez eu à vous soucier d'aucune configuration d'infrastructure.

Veuillez noter System.exit() sur SpringApplication.exit() que la JVM doit s'arrêter une fois la tâche terminée. Consultez la section « Arrêt de l'application » de la documentation de référence de Spring Boot pour plus de détails.

À titre de démonstration, un code permet d'injecter un élément JdbcTemplate, d'interroger la base de données et d'afficher les noms des personnes insérées par le traitement par lots.

_Notez que l'application n'utilise pas l'annotation `@EnableBatchProcessing`. Auparavant, `@EnableBatchProcessing` permettait d'activer la configuration automatique de Spring Batch par Spring Boot. Désormais, un bean annoté avec `@EnableBatchProcessing` ou étendant `DefaultBatchConfiguration` peut être défini pour désactiver la configuration automatique, laissant ainsi à l'application le contrôle total de la configuration de Spring Batch._
