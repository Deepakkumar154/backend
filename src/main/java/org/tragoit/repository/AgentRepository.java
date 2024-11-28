package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.tragoit.model.Agent;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findAll();
}
