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
        // Якщо роль користувача - фрілансер, створюємо фрілансера
        if (user.getRole() == User.Role.FREELANCER) {
            // Зберігаємо користувача
            User savedUser = userRepository.save(user);

            // Створюємо об'єкт фрілансера та прив'язуємо до нього збереженого користувача
            Freelancer freelancer = new Freelancer();
            freelancer.setUser(savedUser);
            freelancer.setName(savedUser.getUsername());  // Ім'я фрілансера = username користувача

            // Викликаємо метод сервісу фрілансера для збереження фрілансера
            freelancerService.saveFreelancer(freelancer, savedUser);

            return savedUser;
        } else {
            // Якщо це не фрілансер, просто зберігаємо юзера без фрілансера
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
