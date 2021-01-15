package contact;

import contact.model.Contact;
import contact.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ContactRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Contact("Bilbo Baggins", "bilbo@baggins.com")));
            log.info("Preloading " + repository.save(new Contact("Frodo Baggins", "frodo@baggis.com")));
        };
    }
}
