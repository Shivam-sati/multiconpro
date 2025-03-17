package com.leaderboard.score.service;

import com.leaderboard.score.entity.ScoreHistory;
import com.leaderboard.score.repository.ScoreHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ScoreHistoryService {

    @Autowired
    private ScoreHistoryRepository scoreHistoryRepository;

    // Fetch player's score history
    public List<ScoreHistory> getPlayerHistory(Long playerId) {
        List<ScoreHistory> history = scoreHistoryRepository.findByPlayerId(playerId);
        if (history.isEmpty()) {
            System.out.println("No history found for player ID: " + playerId);
        }
        return history;
    }

    // Fetch player's analytics
    public Map<String, Object> getPlayerAnalytics(Long playerId) {
        List<ScoreHistory> history = scoreHistoryRepository.findByPlayerId(playerId);

        if (history.isEmpty()) {
            return Map.of("message", "No history available for player ID " + playerId);
        }

        // Calculate analytics
        int totalGames = history.size();
        int highestScore = history.stream().mapToInt(ScoreHistory::getScore).max().orElse(0);
        int lowestScore = history.stream().mapToInt(ScoreHistory::getScore).min().orElse(0);
        OptionalDouble averageScore = history.stream().mapToInt(ScoreHistory::getScore).average();
        int lastScore = history.get(history.size() - 1).getScore();

        // Prepare response
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("playerId", playerId);
        analytics.put("totalGames", totalGames);
        analytics.put("highestScore", highestScore);
        analytics.put("lowestScore", lowestScore);
        analytics.put("averageScore", averageScore.isPresent() ? averageScore.getAsDouble() : 0);
        analytics.put("lastScore", lastScore);

        return analytics;
    }

    // Add new score entry for a player and store in history
    public ScoreHistory addScore(Long playerId, ScoreHistory scoreHistory) {
        scoreHistory.setPlayerId(playerId);
        scoreHistory.setTimestamp(LocalDateTime.now()); // Ensure timestamp is set
        return scoreHistoryRepository.save(scoreHistory);
    }

    // Update an existing score entry AND save it to history
    public ScoreHistory updateScore(Long playerId, Long historyId, ScoreHistory updatedScore) {
        return scoreHistoryRepository.findById(historyId).map(existingRecord -> {
            existingRecord.setScore(updatedScore.getScore());
            existingRecord.setTimestamp(LocalDateTime.now()); // Ensure timestamp updates
            return scoreHistoryRepository.save(existingRecord);
        }).orElse(null);
    }

    // Delete a score entry
    public void deleteScore(Long playerId, Long historyId) {
        scoreHistoryRepository.deleteById(historyId);
    }
}
