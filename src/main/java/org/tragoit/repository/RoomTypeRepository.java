package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tragoit.model.RoomType;

@Component
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
}
