package org.example.lisaanna.web;

import org.example.lisaanna.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public WelcomeController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
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
