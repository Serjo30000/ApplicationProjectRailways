package ru.moiseenko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.moiseenko.entity.User;
import ru.moiseenko.service.NotificationService;
import ru.moiseenko.service.UserService;
import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm()) ||
                !Pattern.matches("^(\\+7|7|8)?[\\s\\-]?\\(?[1234567890][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
                        userForm.getPhone())){
            return "redirect:/registration?error=true";
        }
        if (!userService.saveUser(userForm)){
            return "redirect:/registration?error=true";
        }

        return "redirect:/";
    }
    @GetMapping("/registrationCompany")
    public String registrationCompany(Model model) {
        model.addAttribute("userForm", new User());

        return "registrationCompany";
    }

    @PostMapping("/registrationCompany")
    public String addCompany(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registrationCompany";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm()) ||
                !Pattern.matches("^(\\+7|7|8)?[\\s\\-]?\\(?[1234567890][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
                        userForm.getPhone())){
            return "redirect:/registrationCompany?error=true";
        }
        if (!userService.saveUser(userForm)){
            return "redirect:/registrationCompany?error=true";
        }

        return "redirect:/adminPanel/adminUser";
    }
    @GetMapping("/account")
    public String viewAccount(Model model) {
        model.addAttribute("userServ", userService);
        return "account";
    }
    @GetMapping("/accountNotification")
    public String viewAccountNotification(Model model) {
        model.addAttribute("userServ", userService);
        model.addAttribute("notificationServ", notificationService);
        return "accountNotification";
    }
    @PostMapping("/accountNotification")
    public String  deleteAccountNotification(@RequestParam(required = true ) int notificationId,
                                      @RequestParam(required = true, defaultValue = "" ) String action,
                                      Model model) {
        if (action.equals("delete")){
            notificationService.deleteNotification(notificationId);
        }
        return "redirect:/accountNotification";
    }
}