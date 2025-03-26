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
        // Перевірка, чи існує користувач з таким ID
        if (user.getId() != null) {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {
                // Якщо користувач існує, використовуємо існуючого
                user = existingUser.get();
            } else {
                // Якщо користувач з таким ID не знайдений, створюємо нового
                user = userRepository.save(user);
            }
        } else {
            // Якщо користувач не має ID, створюємо нового
            user = userRepository.save(user);
        }

        // Прив'язуємо користувача до фрілансера
        freelancer.setUser(user);

        // Перевірка, чи не існує вже фрілансер з таким користувачем
        Optional<Freelancer> existingFreelancer = freelancerRepository.findByUser(user);
        if (existingFreelancer.isPresent()) {
            // Якщо такий фрілансер вже існує, повертаємо існуючий
            return existingFreelancer.get();
        }

        // Зберігаємо новий фрілансер
        return freelancerRepository.save(freelancer);
    }

    public void deleteFreelancer(Long id) {
        freelancerRepository.deleteById(id);
    }
}
