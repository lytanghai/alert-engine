package com.finance.alert_engine.repository;

import com.finance.alert_engine.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Configuration, Integer> {
    Optional<Configuration> findByName(String name);
}
