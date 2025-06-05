package org.example.lisaanna.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String getAdminPage(){
        return "admin";
    }


    /*@PostMapping("/register")
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
    }*/

}

