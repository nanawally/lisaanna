package org.example.lisaanna.web;

import jakarta.validation.Valid;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.exception.UserNotFoundException;
import org.example.lisaanna.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Den är klassen ansvarar för att kommunicera med applikationens frontend, genom att agera som en
 * middle man mellan servicelagret, som innehåller logik, och frontend.
 * Den här klassen tar in HTTP requests från frontend och mappar dem till metoder (GET, POST, DELETE).
 * Därefter kallar den på servicelagret för att utföra operationerna, och returnerar till sist HTTP-
 * responses med statuskoder osv tillbaka till frontend.
 */
@RestController
@RequestMapping("/user")
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * @return en HTTP status returneras tillsammans med en lista över alla användare
     */
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAppUserList());
    }

    /**
     * @param username tas in för att skickas vidare och sökas efter i listan över AppUsers.
     * @return en HTTP status returneras tillsammans med resten av datan som hittades med
     * användarnamnet.
     */
    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        if (appUserService.getAppUserByUsername(username) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(appUserService.getAppUserByUsername(username));
        }
    }

    /**
     * @param appUserDTO tas in för att säkerställa att fält som username, password osv redan har
     * blivit validerade innan de skickas till servicelagret för att sparas.
     * @return en HTTP status returneras tillsammans med användardata i DTO-format
     */
    @PostMapping
    public ResponseEntity<AppUserDTO> addUser(@Valid @RequestBody AppUserDTO appUserDTO) {
        if (appUserService.getAppUserByUsername(appUserDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().build();
        }
        appUserService.saveUser(appUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserDTO);
    }


    /**
     * @param id tas in för att skickas vidare till servicelagret, där en användare med IDt tas bort.
     * @return en HTTP status returneras
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
