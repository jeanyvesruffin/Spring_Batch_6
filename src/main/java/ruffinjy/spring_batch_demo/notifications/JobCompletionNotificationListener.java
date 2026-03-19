package ruffinjy.spring_batch_demo.notifications;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ruffinjy.spring_batch_demo.domain.Person;

/**
 * Listener exécuté après l'exécution d'un Job Spring Batch.
 *
 * <p>Lorsque le job est {@link BatchStatus#COMPLETED}, interroge la table {@code people}
 * et journalise les entrées trouvées.</p>
 */
@Slf4j
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Crée une instance du listener.
     *
     * @param jdbcTemplate template JDBC utilisé pour interroger la base
     */
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Appelé après l'exécution du job.
     *
     * <p>Si le statut est {@link BatchStatus#COMPLETED}, effectue une requête et
     * journalise les personnes trouvées en base.</p>
     *
     * @param jobExecution informations sur l'exécution du job
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT first_name, last_name FROM people", new DataClassRowMapper<>(Person.class))
                    .forEach(person -> log.info("Found < {} > in the database.", person));

        }
    }
}
