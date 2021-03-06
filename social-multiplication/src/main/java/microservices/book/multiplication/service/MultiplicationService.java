package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Creates a Multiplication object with two randomly-generated factors
     * between 11 and 99.
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return a {@link MultiplicationAttempt}, which contains information about the attempt and
     * indicates if it's correct or not.
     */
    MultiplicationAttempt checkAttempt(final MultiplicationAttempt resultAttempt);

    /**
     * Gets the statistics for a given user.
     *
     * @param userAlias the user's alias
     * @return a list of {@link MultiplicationAttempt} objects, being the past attempts of the user.
     */
    List<MultiplicationAttempt> getStatsForUser(final String userAlias);

    /**
     * Gets an attempt by its id
     *
     * @param resultId the identifier of the attempt
     * @return the {@link MultiplicationAttempt} object matching the id, otherwise null.
     */
    MultiplicationAttempt getResultById(final Long resultId);

}
