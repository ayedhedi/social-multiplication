package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationAttempt;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationAttemptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationAttemptRepository attemptRepository;
    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationAttemptRepository attemptRepository,
                                     final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    public MultiplicationAttempt checkAttempt(final MultiplicationAttempt attempt) {
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!");

        // Check if the attempt is correct
        boolean isCorrect = attempt.getResultAttempt() ==
                        attempt.getMultiplication().getFactorA() *
                        attempt.getMultiplication().getFactorB();

        MultiplicationAttempt checkedAttempt = new MultiplicationAttempt(
                attempt.getUser(),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                isCorrect
        );

        // Stores the attempt
        MultiplicationAttempt storedAttempt = attemptRepository.save(checkedAttempt);

        // Communicates the result via Event
        eventDispatcher.send(
                new MultiplicationSolvedEvent(storedAttempt.get_id(),
                        checkedAttempt.getUser(),
                        checkedAttempt.isCorrect()));

        return storedAttempt;
    }

    @Override
    public List<MultiplicationAttempt> getStatsForUser(final String userAlias) {
        return attemptRepository.findTop5ByUser(userAlias);
    }

    @Override
    public MultiplicationAttempt getResultById(final Long resultId) {
        return attemptRepository.findOne(resultId);
    }


}
