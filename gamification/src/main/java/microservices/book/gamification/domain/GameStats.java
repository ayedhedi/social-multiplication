package microservices.book.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This object contains the result of one or many iterations of the game.
 * It may contain any combination of {@link ScoreCard} objects and {@link BadgeCard} objects.
 *
 * It can be used as a delta (as a single game iteration) or to represent the total amount of score / badges.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class GameStats {

    private final String user;
    private final int score;
    private final List<Badge> badges;

    // Empty constructor for JSON / JPA
    public GameStats() {
        this.user = null;
        this.score = 0;
        this.badges = new ArrayList<>();
    }

    /**
     * Factory method to build an empty instance (zero points and no badges)
     * @param user the user's id
     * @return a {@link GameStats} object with zero score and no badges
     */
    public static GameStats emptyStats(final String user) {
        return new GameStats(user, 0, Collections.emptyList());
    }

    /**
     * @return an unmodifiable view of the badge cards list
     */
    public List<Badge> getBadges() {
        return Collections.unmodifiableList(badges);
    }
}
