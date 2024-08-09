package com.academy.flightsystem.api.repository;

import com.academy.flightsystem.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
