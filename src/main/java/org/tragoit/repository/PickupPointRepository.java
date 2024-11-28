package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tragoit.model.Activity;
import org.tragoit.model.PickupPoint;

@Component
public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
}
