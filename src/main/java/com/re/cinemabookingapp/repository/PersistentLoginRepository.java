package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {
    void deleteByUsername(String username);
}
