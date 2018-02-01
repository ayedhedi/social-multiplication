package microservices.book.gamification.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Random;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class represents the Score linked to an attempt in the game,
 * with an associated user and the timestamp in which the score
 * is registered.
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public final class ScoreCard {

    // The default score assigned to this card, if not specified.
    public static final int DEFAULT_SCORE = 10;

    @Id
    private long cardId;

    private String user;
    private Long attemptId;
    private long scoreTimestamp;
    private int score;

    public ScoreCard(String user, long attemptId) {
        cardId = (int)(Math.random()*1000000);
        this.user = user;
        this.attemptId = attemptId;
        this.scoreTimestamp = (new Date()).getTime();
    }
}
