//package org.tragoit.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.stereotype.Repository;
//import org.tragoit.model.RefreshToken;
//import org.tragoit.model.User;
//
//import java.util.Optional;
//
//@Repository
//@EnableJpaRepositories
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    Optional<RefreshToken> findByToken(String token);
//
//    @Modifying
//    int deleteByUser(User user);
//}
