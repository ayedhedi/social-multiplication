package microservices.book.multiplication.domain;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class MultiplicationAttempt {

    @Id
    private long _id;

    private String user;
    private final Multiplication multiplication;
    private final int resultAttempt;
    private final boolean correct;

    public MultiplicationAttempt() {
        _id = (int)(Math.random()*1000000);
        user = null;
        multiplication = null;
        resultAttempt = -1;
        correct = false;
    }

    public MultiplicationAttempt(String user, Multiplication multiplication, int resultAttempt, boolean correct) {
        _id = (int)(Math.random()*1000000);
        this.user = user;
        this.multiplication = multiplication;
        this.resultAttempt = resultAttempt;
        this.correct = correct;
    }
}
