package com.leaderboard.score.controller;

import com.leaderboard.score.entity.LeaderboardEntry;
import com.leaderboard.score.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LeaderboardWebSocketController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Listen for messages at /app/leaderboard/update, update the entry and then manually broadcast it.
    @MessageMapping("/leaderboard/update")
    public void broadcastUpdate(LeaderboardEntry entry) {
        LeaderboardEntry updatedEntry = leaderboardService.updateEntry(entry);
        // Send updated entry to all subscribers of /topic/leaderboard
        messagingTemplate.convertAndSend("/topic/leaderboard", updatedEntry);
    }
}
