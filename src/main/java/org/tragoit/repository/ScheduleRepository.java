package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.tragoit.model.Schedule;

@Repository
@EnableJpaRepositories
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
