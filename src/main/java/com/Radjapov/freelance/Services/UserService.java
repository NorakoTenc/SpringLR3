package com.Radjapov.freelance.Services;

import com.Radjapov.freelance.Models.Freelancer;
import com.Radjapov.freelance.Models.User;
import com.Radjapov.freelance.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreelancerService freelancerService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        if (user.getRole() == User.Role.FREELANCER) {
            User savedUser = userRepository.save(user);
            Freelancer freelancer = new Freelancer();
            freelancer.setUser(savedUser);
            freelancer.setName(savedUser.getUsername());

            freelancerService.saveFreelancer(freelancer, savedUser);

            return savedUser;
        } else {
            return userRepository.save(user);
        }
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(User.Role.CUSTOMER);
    }

}
