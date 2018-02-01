package microservices.book.gamification.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import microservices.book.gamification.domain.ScoreCard;

/**
 * Handles CRUD operations with ScoreCards
 */
public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {


    List<ScoreCard> findByUser(final String user);


    /**
     * Retrieves all the ScoreCards for a given user, identified by his user id.
     * @param user the id of the user
     * @return a list containing all the ScoreCards for the given user, sorted by most recent.
     */
    List<ScoreCard> findByUserOrderByScoreTimestampDesc(final String user);
}
