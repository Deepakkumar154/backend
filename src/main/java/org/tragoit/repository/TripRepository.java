package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tragoit.model.Trip;

import java.util.List;

@Component
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByAgentId(Long agentId);
}
