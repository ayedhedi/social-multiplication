package microservices.book.multiplication.domain;


import org.springframework.data.annotation.Id;

import java.util.Random;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents a Multiplication (a * b).
 */
@Getter
@ToString
@EqualsAndHashCode
public final class Multiplication {

    @Id
    private Long id;

    // Both factors
    private final int factorA;
    private final int factorB;

    public Multiplication(int a, int b) {
        id = Math.abs((new Random()).nextLong());
        this.factorA = a;
        this.factorB = b;
    }

    // Empty constructor for JSON/JPA
    public Multiplication() {
        this(0, 0);
    }


}
