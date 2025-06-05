package org.example.lisaanna.web;

import jakarta.validation.Valid;
import org.example.lisaanna.entity.AppUser;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// mappar nu till /login -> ska mappa till loginForm eller loginResult???????
@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping()
    public String showForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "loginForm";
    }
    @PostMapping()
    public String handleForm(@Valid @ModelAttribute AppUser appUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "loginForm";
        }
        return "loginResult";
    }
}
