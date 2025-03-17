package com.leaderboard.score.controller;

import com.leaderboard.score.entity.ScoreHistory;
import com.leaderboard.score.service.ScoreHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private ScoreHistoryService scoreHistoryService;

    // GET /api/player/{playerId}/history - Get player's score history
    @GetMapping("/{playerId}/history")
    public ResponseEntity<List<ScoreHistory>> getHistory(@PathVariable Long playerId) {
        return ResponseEntity.ok(scoreHistoryService.getPlayerHistory(playerId));
    }

    // GET /api/player/{playerId}/analytics - Get player's analytics
    @GetMapping("/{playerId}/analytics")
public ResponseEntity<Object> getAnalytics(@PathVariable Long playerId) {
    Object analytics = scoreHistoryService.getPlayerAnalytics(playerId);
    return ResponseEntity.ok(analytics);
}

    // POST /api/player/{playerId}/history - Add score history
    @PostMapping("/{playerId}/history")
    public ResponseEntity<ScoreHistory> addScoreHistory(@PathVariable Long playerId, @RequestBody ScoreHistory scoreHistory) {
        ScoreHistory savedHistory = scoreHistoryService.addScore(playerId, scoreHistory);
        return ResponseEntity.ok(savedHistory);
    }

    // PUT /api/player/{playerId}/history/{historyId} - Update a specific score history
    @PutMapping("/{playerId}/history/{historyId}")
    public ResponseEntity<ScoreHistory> updateScoreHistory(@PathVariable Long playerId, 
                                                           @PathVariable Long historyId, 
                                                           @RequestBody ScoreHistory updatedScore) {
        ScoreHistory savedHistory = scoreHistoryService.updateScore(playerId, historyId, updatedScore);
        return ResponseEntity.ok(savedHistory);
    }

    // DELETE /api/player/{playerId}/history/{historyId} - Delete a specific score history
    @DeleteMapping("/{playerId}/history/{historyId}")
    public ResponseEntity<Void> deleteScoreHistory(@PathVariable Long playerId, @PathVariable Long historyId) {
        scoreHistoryService.deleteScore(playerId, historyId);
        return ResponseEntity.noContent().build();
    }
}
