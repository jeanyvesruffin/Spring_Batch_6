package ruffinjy.spring_batch_demo.configurations;

import javax.sql.DataSource;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.batch.core.job.Job;
import ruffinjy.spring_batch_demo.batch_processor.PersonItemProcessor;
import ruffinjy.spring_batch_demo.domain.Person;
import ruffinjy.spring_batch_demo.notifications.JobCompletionNotificationListener;

/**
 * Configuration Spring Batch : lecteur, processeur, writer, job et step.
 *
 * <p>Définit les beans nécessaires pour lire le fichier CSV, transformer les
 * {@link Person} et écrire les résultats dans la base de données.</p>
 */
@Configuration
public class BatchConfiguration {

    /**
     * Lit le fichier {@code sample-data.csv} et mappe chaque ligne vers une instance de {@link Person}.
     *
     * @return un lecteur de fichier plat pour {@link Person}
     */
    @Bean
    FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .targetType(Person.class)
                .build();
    }

    /**
     * Fournit le processeur métier qui transforme les données lues.
     *
     * @return instance de {@link PersonItemProcessor}
     */
    @Bean
    PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /**
     * Crée un {@link JdbcBatchItemWriter} pour insérer des personnes dans la table {@code people}.
     *
     * @param dataSource source de données utilisée par le writer
     * @return writer configuré pour insérer des {@link Person}
     */
    @Bean
    JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    /**
     * Définit le {@link Job} principal qui exécute le step fourni.
     *
     * @param jobRepository le repository de Job
     * @param step1 step à démarrer pour ce job
     * @param listener listener de fin de job
     * @return le {@link Job} configuré
     */
    @Bean
    Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder(jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    /**
     * Définit un {@link Step} qui lit, transforme et écrit les entités {@link Person}.
     *
     * @param jobRepository repository de job requis par le builder
     * @param transactionManager gestionnaire de transaction pour le step
     * @param reader lecteur de personnes
     * @param processor processeur des personnes
     * @param writer writer pour persister les personnes
     * @return le {@link Step} configuré
     */
    @Bean
    Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder(jobRepository)
                .<Person, Person>chunk(3)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
