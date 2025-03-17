package com.leaderboard.score.controller;

import com.leaderboard.score.entity.LeaderboardEntry;
import com.leaderboard.score.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    // GET /api/leaderboard - Get leaderboard with pagination, sorting, and filtering
    @GetMapping("")
    public ResponseEntity<Page<LeaderboardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "desc") String order) {

        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by("score").ascending() : Sort.by("score").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LeaderboardEntry> result;
        if (filter != null && !filter.isEmpty()) {
            result = leaderboardService.findByPlayerName(filter, pageable);
        } else {
            result = leaderboardService.findAll(pageable);
        }
        return ResponseEntity.ok(result);
    }

    // GET /api/leaderboard/{playerId}/neighbors - Get nearby players in the leaderboard
    @GetMapping("/{playerId}/neighbors")
    public ResponseEntity<?> getNeighbors(@PathVariable Long playerId, @RequestParam(defaultValue = "2") int range) {
        List<LeaderboardEntry> fullList = leaderboardService.getAllEntries();
        int index = java.util.stream.IntStream.range(0, fullList.size())
                .filter(i -> fullList.get(i).getPlayerId().equals(playerId))
                .findFirst().orElse(-1);
        if (index == -1) return ResponseEntity.notFound().build();

        int start = Math.max(index - range, 0);
        int end = Math.min(index + range + 1, fullList.size());
        List<LeaderboardEntry> neighbors = fullList.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("currentRank", index + 1);
        response.put("neighbors", neighbors);
        return ResponseEntity.ok(response);
    }

    // POST /api/leaderboard - Add or update leaderboard entry
    @PostMapping("")
    public ResponseEntity<LeaderboardEntry> createOrUpdateEntry(@RequestBody LeaderboardEntry entry) {
        LeaderboardEntry savedEntry = leaderboardService.updateEntry(entry);
        return ResponseEntity.ok(savedEntry);
    }

    // PUT /api/leaderboard/{playerId} - Update leaderboard entry for a player
    @PutMapping("/{playerId}")
    public ResponseEntity<LeaderboardEntry> updateLeaderboardEntry(@PathVariable Long playerId, @RequestBody LeaderboardEntry updatedEntry) {
        LeaderboardEntry savedEntry = leaderboardService.updateEntry(updatedEntry);
        return ResponseEntity.ok(savedEntry);
    }

    // DELETE /api/leaderboard/{playerId} - Remove a player from the leaderboard
    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deleteLeaderboardEntry(@PathVariable Long playerId) {
        leaderboardService.deleteEntry(playerId);
        return ResponseEntity.noContent().build();
    }
}
