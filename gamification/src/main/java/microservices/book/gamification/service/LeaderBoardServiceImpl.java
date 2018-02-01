package microservices.book.gamification.service;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.ScoreCardRepository;

import org.bouncycastle.util.io.Streams;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
class LeaderBoardServiceImpl implements LeaderBoardService {

    private ScoreCardRepository scoreCardRepository;

    LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        List<LeaderBoardRow> result = new ArrayList<>();
                StreamSupport
                        .stream(scoreCardRepository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(ScoreCard::getUser))
                        .forEach((user, scores) -> {
                            LongAdder a = new LongAdder();
                            scores.stream().map(ScoreCard::getScore).forEach(a::add);
                            result.add(new LeaderBoardRow(user, a.longValue()));
                        });

        return result.stream()
                .sorted(Comparator.comparingLong(LeaderBoardRow::getTotalScore)).limit(10)
                .collect(Collectors.toList());
    }
}
