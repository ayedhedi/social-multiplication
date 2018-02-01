package microservices.book.gamification.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This class links a Badge to a User. Contains also a timestamp with the moment in which the user got it.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class BadgeCard {

    @Id
    private long badgeId;

    private String user;
    private long badgeTimestamp;
    private Badge badge;


    public BadgeCard(final String user, final Badge badge) {
        this.user = user;
        this.badge = badge;
        badgeTimestamp = (new Date()).getTime();
    }

}
