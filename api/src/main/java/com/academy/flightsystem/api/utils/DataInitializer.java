package com.academy.flightsystem.api.utils;

import com.academy.flightsystem.api.model.Role;
import com.academy.flightsystem.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    // FlightRepository
    // UserRepository
    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findByName("ROLE_USER").isEmpty()){
            roleRepository.save(new Role("ROLE_USER"));
        }

        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()){
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }
}
