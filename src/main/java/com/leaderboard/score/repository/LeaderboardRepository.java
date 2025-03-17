package com.leaderboard.score.repository;

import com.leaderboard.score.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    Page<LeaderboardEntry> findByPlayerNameContainingIgnoreCase(String playerName, Pageable pageable);
}
