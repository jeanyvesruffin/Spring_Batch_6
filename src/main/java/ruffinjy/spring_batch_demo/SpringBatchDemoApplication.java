package ruffinjy.spring_batch_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Spring Batch Demo.
 *
 * <p>Démarre le contexte Spring Boot et termine la JVM avec le code de sortie
 * retourné par {@link SpringApplication#exit(Object...)}.</p>
 */
@SpringBootApplication
public class SpringBatchDemoApplication {

	/**
	 * Démarre l'application Spring Boot.
	 *
	 * @param args arguments de la ligne de commande
	 */
	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchDemoApplication.class, args)));
	}

}
