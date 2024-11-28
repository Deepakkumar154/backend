package org.tragoit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tragoit.dto.AgentDto;
import org.tragoit.model.Agent;
import org.tragoit.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
public class AgentController {
    private final UserService userService;

    @PostMapping("user/{userId}")
    public ResponseEntity<Agent> addAgentDetails(@PathVariable Long userId, @RequestBody AgentDto userDto) {
        return ResponseEntity.ok(userService.saveAgentForUser(userId, userDto));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<Agent> updateAgentDetails(@PathVariable Long userId, @RequestBody Agent agentDetails) {
        Agent updatedAgent = userService.updateAgentDetails(userId, agentDetails);
        return ResponseEntity.ok(updatedAgent);
    }

    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = userService.getAllAgents();
        return ResponseEntity.ok(agents);
    }
}
