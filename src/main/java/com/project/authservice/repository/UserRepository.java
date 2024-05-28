package com.project.authservice.repository;

import com.project.authservice.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE is_active = 1",
            countQuery = "SELECT count(*) FROM user WHERE is_active = 1",
            nativeQuery = true)
    Page<User> findAllUserPaging(Pageable pageable);
}
