package com.Radjapov.freelance.Services;

import com.Radjapov.freelance.Models.Freelancer;
import com.Radjapov.freelance.Models.User;
import com.Radjapov.freelance.Repositories.FreelancerRepository;
import com.Radjapov.freelance.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FreelancerService {
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Freelancer> getAllFreelancers() {
        return freelancerRepository.findAll();
    }

    public Optional<Freelancer> getFreelancerById(Long id) {
        return freelancerRepository.findById(id);
    }

    public Freelancer saveFreelancer(Freelancer freelancer, User user) {
        if (user.getId() != null) {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                user = userRepository.save(user);
            }
        } else {
            user = userRepository.save(user);
        }

        freelancer.setUser(user);

        Optional<Freelancer> existingFreelancer = freelancerRepository.findByUser(user);
        if (existingFreelancer.isPresent()) {
            return existingFreelancer.get();
        }

        return freelancerRepository.save(freelancer);
    }

    public void deleteFreelancer(Long id) {
        freelancerRepository.deleteById(id);
    }
}
