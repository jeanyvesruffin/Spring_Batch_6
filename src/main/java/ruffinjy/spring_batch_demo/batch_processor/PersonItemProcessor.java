package ruffinjy.spring_batch_demo.batch_processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;
import ruffinjy.spring_batch_demo.domain.Person;

/**
 * ItemProcessor qui convertit les champs {@code firstName} et {@code lastName} en majuscules.
 *
 * <p>Retourne un nouveau {@link Person} contenant les valeurs transformées.</p>
 */
@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    /**
     * Transforme la personne en mettant le prénom et le nom en majuscules.
     *
     * @param person la personne d'entrée
     * @return la personne transformée (prénom et nom en majuscules)
     */
    @Override
    public Person process(final Person person) {
        final String firstName = person.firstName().toUpperCase();
        final String lastName = person.lastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting ({}) into ({})", person, transformedPerson);

        return transformedPerson;
    }

}
