package com.Radjapov.freelance.Controllers;

import com.Radjapov.freelance.Models.Order;
import com.Radjapov.freelance.Models.Freelancer;
import com.Radjapov.freelance.Models.User;
import com.Radjapov.freelance.Services.OrderService;
import com.Radjapov.freelance.Services.UserService;
import com.Radjapov.freelance.Services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;  // Додано для отримання замовників

    @Autowired
    private FreelancerService freelancerService;  // Додано для отримання фрілансерів

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());

        // Отримуємо користувачів з роллю замовника
        List<User> customers = userService.getUsersByRole("CUSTOMER");
        // Отримуємо всіх фрілансерів
        List<Freelancer> freelancers = freelancerService.getAllFreelancers();

        // Перевірка на порожній список
        if (customers.isEmpty()) {
            model.addAttribute("error", "Немає доступних замовників");
        }
        if (freelancers.isEmpty()) {
            model.addAttribute("error", "Немає доступних фрілансерів");
        }

        model.addAttribute("users", customers);
        model.addAttribute("freelancers", freelancers);

        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Перевірте введені дані");
            return "order/create";
        }
        orderService.saveOrder(order);
        return "redirect:/orders";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "order/edit";
        }
        return "redirect:/orders";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id, @Valid @ModelAttribute("order") Order order, BindingResult result) {
        if (result.hasErrors()) {
            return "order/edit";
        }
        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
