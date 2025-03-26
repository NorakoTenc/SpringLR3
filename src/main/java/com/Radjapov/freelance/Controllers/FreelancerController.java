package com.Radjapov.freelance.Controllers;

import com.Radjapov.freelance.Models.Freelancer;
import com.Radjapov.freelance.Models.User;
import com.Radjapov.freelance.Services.FreelancerService;
import com.Radjapov.freelance.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/freelancers")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @GetMapping
    public String listFreelancers(Model model) {
        List<Freelancer> freelancers = freelancerService.getAllFreelancers();
        model.addAttribute("freelancers", freelancers);
        return "freelancer/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("freelancer", new Freelancer());
        model.addAttribute("user", new User());  // Додаємо користувача до моделі
        return "freelancer/create";
    }

    @PostMapping("/create")
    public String createFreelancer(@Valid @ModelAttribute("freelancer") Freelancer freelancer,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "freelancer/create";
        }


        String password = freelancer.getUser().getPassword();


        User user = new User();
        user.setUsername(freelancer.getUser().getUsername());
        user.setPassword(password);
        user.setRole(User.Role.FREELANCER);


        freelancer.setUser(user);


        freelancerService.saveFreelancer(freelancer, user);

        return "redirect:/freelancers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Freelancer> freelancer = freelancerService.getFreelancerById(id);
        if (freelancer.isPresent()) {
            model.addAttribute("freelancer", freelancer.get());
            return "freelancer/edit";
        }
        return "redirect:/freelancers";
    }

    @PostMapping("/edit/{id}")
    public String updateFreelancer(@PathVariable Long id, @Valid @ModelAttribute("freelancer") Freelancer freelancer, BindingResult result) {
        if (result.hasErrors()) {
            return "freelancer/edit";
        }


        Optional<Freelancer> existingFreelancer = freelancerService.getFreelancerById(id);

        if (existingFreelancer.isPresent()) {
            Freelancer currentFreelancer = existingFreelancer.get();


            User user = currentFreelancer.getUser();


            freelancer.setUser(user);


            freelancerService.saveFreelancer(freelancer, user);
        }

        return "redirect:/freelancers";
    }


    @GetMapping("/delete/{id}")
    public String deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
        return "redirect:/freelancers";
    }
}