package com.leaderboard.score.service;

import com.leaderboard.score.entity.LeaderboardEntry;
import com.leaderboard.score.entity.ScoreHistory;
import com.leaderboard.score.repository.LeaderboardRepository;
import com.leaderboard.score.repository.ScoreHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
private ScoreHistoryRepository scoreHistoryRepository;

    // Fetch all leaderboard entries with pagination
    public Page<LeaderboardEntry> findAll(Pageable pageable) {
        return leaderboardRepository.findAll(pageable);
    }

    // Fetch leaderboard entries by player name (for filtering)
    public Page<LeaderboardEntry> findByPlayerName(String playerName, Pageable pageable) {
        return leaderboardRepository.findByPlayerNameContainingIgnoreCase(playerName, pageable);
    }

    // Fetch all entries (used for neighbor ranking)
    public List<LeaderboardEntry> getAllEntries() {
        return leaderboardRepository.findAll();
    }

    // Create or update a leaderboard entry and add score to history
    
public LeaderboardEntry updateEntry(LeaderboardEntry entry) {
    LeaderboardEntry savedEntry = leaderboardRepository.save(entry);

    // Store in Score History
    ScoreHistory history = new ScoreHistory();
    history.setPlayerId(entry.getPlayerId());
    history.setScore(entry.getScore());
    history.setTimestamp(java.time.LocalDateTime.now()); // Set timestamp

    scoreHistoryRepository.save(history); // Save history entry

    return savedEntry;
}


    // Delete a leaderboard entry by player ID
    public void deleteEntry(Long playerId) {
        leaderboardRepository.deleteById(playerId);
    }
}
