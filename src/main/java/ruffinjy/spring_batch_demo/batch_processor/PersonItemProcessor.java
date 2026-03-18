package ruffinjy.spring_batch_demo.batch_processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;
import ruffinjy.spring_batch_demo.domain.Person;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) {
        final String firstName = person.firstName().toUpperCase();
        final String lastName = person.lastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting ({}) into ({})", person, transformedPerson);

        return transformedPerson;
    }

}
