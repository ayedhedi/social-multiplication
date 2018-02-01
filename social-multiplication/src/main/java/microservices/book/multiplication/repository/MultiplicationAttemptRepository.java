package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.MultiplicationAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This interface allow us to store and retrieve attempts
 */
public interface MultiplicationAttemptRepository extends CrudRepository<MultiplicationAttempt, Long> {

    /**
     * @return the latest 5 attempts for a given user, identified by their alias.
     */
    List<MultiplicationAttempt> findTop5ByUser(String user);
}
