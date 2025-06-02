package org.example.lisaanna.web;

import jakarta.validation.Valid;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AppUserController {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    /*
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute AppUserDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return "register"; // visa formuläret igen
        }

        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setConsentGiven(dto.isConsentGiven());
        user.setRole("USER");

        AppUserRepository.save(user);

        return "redirect:/login";
    }
    */
}
