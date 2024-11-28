package org.tragoit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.tragoit.model.Role;

@Repository
@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
