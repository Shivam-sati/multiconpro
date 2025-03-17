package com.leaderboard.score.repository;

import com.leaderboard.score.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {
    List<ScoreHistory> findByPlayerId(Long playerId);
}
