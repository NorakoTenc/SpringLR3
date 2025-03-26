package com.Radjapov.freelance.Repositories;

import com.Radjapov.freelance.Models.Freelancer;
import com.Radjapov.freelance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    Optional<Freelancer> findByUser(User user);

}