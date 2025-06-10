/*
package org.example.lisaanna.web;

import jakarta.validation.Valid;
import org.example.lisaanna.entity.AppUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
// mappar nu till /login -> ska mappa till loginForm eller loginResult???????
/*
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * @param model
     * @return
     */
/*
    @GetMapping()
    public String showForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "registeruser";
    }

    /**
     * @param appUser
     * @param bindingResult
     * @return
     */
/*
    @PostMapping()
    public String handleForm(@Valid @ModelAttribute AppUser appUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registeruser";
        }
        return "loginResult";
    }
}
*/
