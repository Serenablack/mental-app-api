package com.mentalapp.repository;

import com.mentalapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.authProvider = :authProvider")
    Optional<User> findByEmailAndAuthProvider(@Param("email") String email,
            @Param("authProvider") User.AuthProvider authProvider);

    boolean existsByEmail(String email);

    boolean existsByGoogleId(String googleId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= CURRENT_DATE")
    long countTodaySignups();
}
