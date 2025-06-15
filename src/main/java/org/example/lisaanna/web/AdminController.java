package org.example.lisaanna.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FATE TO BE DECIDED
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * @return
     */
    @GetMapping
    public String getAdminPage(){
        return "admin";
    }

}

