package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tragoit.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
