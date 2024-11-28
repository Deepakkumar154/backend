package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tragoit.model.Stay;

public interface StayRepository extends JpaRepository<Stay, Long> {
}
