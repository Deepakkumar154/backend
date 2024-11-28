package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tragoit.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
