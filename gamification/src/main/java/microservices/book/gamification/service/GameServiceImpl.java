package microservices.book.gamification.service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.*;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author moises.macero
 */
@Service
@Slf4j
class GameServiceImpl implements GameService {

    public static final int LUCKY_NUMBER = 42;

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;
    private MultiplicationResultAttemptClient attemptClient;

    GameServiceImpl(ScoreCardRepository scoreCardRepository,
                    BadgeCardRepository badgeCardRepository,
                    MultiplicationResultAttemptClient attemptClient) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
        this.attemptClient = attemptClient;
    }

    @Override
    public GameStats newAttemptForUser(final String user,
                                       final Long attemptId,
                                       final boolean correct) {
        // For the first version we'll give points only if it's correct
        if(correct) {
            ScoreCard scoreCard = new ScoreCard(user, attemptId);
            scoreCardRepository.save(scoreCard);
            log.info("User with id {} scored {} points for attempt id {}",
                    user, scoreCard.getScore(), attemptId);
            List<BadgeCard> badgeCards = processForBadges(user, attemptId);
            return new GameStats(user, scoreCard.getScore(),
                    badgeCards.stream().map(BadgeCard::getBadge)
                            .collect(Collectors.toList()));
        }
        return GameStats.emptyStats(user);
    }

    /**
     * Checks the total score and the different score cards obtained
     * to give new badges in case their conditions are met.
     */
    private List<BadgeCard> processForBadges(final String user,
                                             final Long attemptId) {
        List<BadgeCard> badgeCards = new ArrayList<>();

       int totalScore = getTotalScoreUser(user);

        log.info("New score for user {} is {}", user, totalScore);

        List<ScoreCard> scoreCardList = scoreCardRepository
                .findByUserOrderByScoreTimestampDesc(user);
        List<BadgeCard> badgeCardList = badgeCardRepository
                .findByUserOrderByBadgeTimestampDesc(user);

        // Badges depending on score
        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.BRONZE_MULTIPLICATOR, totalScore, 100, user)
                .ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.SILVER_MULTIPLICATOR, totalScore, 500, user)
                .ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.GOLD_MULTIPLICATOR, totalScore, 999, user)
                .ifPresent(badgeCards::add);

        // First won badge
        if(scoreCardList.size() == 1 &&
                !containsBadge(badgeCardList, Badge.FIRST_WON)) {
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, user);
            badgeCards.add(firstWonBadge);
        }

        // Lucky number badge
        MultiplicationResultAttempt attempt = attemptClient
                .retrieveMultiplicationResultAttemptbyId(attemptId);
        if(!containsBadge(badgeCardList, Badge.LUCKY_NUMBER) &&
                (LUCKY_NUMBER == attempt.getMultiplicationFactorA() ||
                LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {
            BadgeCard luckyNumberBadge = giveBadgeToUser(
                    Badge.LUCKY_NUMBER, user);
            badgeCards.add(luckyNumberBadge);
        }

        return badgeCards;
    }

    @Override
    public GameStats retrieveStatsForUser(final String user) {
        int score =getTotalScoreUser(user);
        List<BadgeCard> badgeCards = badgeCardRepository
                .findByUserOrderByBadgeTimestampDesc(user);
        return new GameStats(user, score, badgeCards.stream()
                .map(BadgeCard::getBadge).collect(Collectors.toList()));
    }

    /**
     * Convenience method to check the current score against
     * the different thresholds to gain badges.
     * It also assigns badge to user if the conditions are met.
     */
    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(
            final List<BadgeCard> badgeCards, final Badge badge,
            final int score, final int scoreThreshold, final String user) {

        if(score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
            return Optional.of(giveBadgeToUser(badge, user));
        }
        return Optional.empty();
    }

    /**
     * Checks if the passed list of badges includes the one being checked
     */
    private boolean containsBadge(final List<BadgeCard> badgeCards,
                                  final Badge badge) {
        return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
    }

    /**
     * Assigns a new badge to the given user
     */
    private BadgeCard giveBadgeToUser(final Badge badge, final String user) {
        BadgeCard badgeCard = new BadgeCard(user, badge);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge: {}", user, badge);
        return badgeCard;
    }

    private int getTotalScoreUser(String user) {
        return (int)scoreCardRepository.findByUser(user)
                .stream()
                .map(ScoreCard::getScore)
                .collect(Collectors.summarizingInt(i->i))
                .getSum();

    }

}
