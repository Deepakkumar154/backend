package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tragoit.model.Itinerary;

@Component
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}
