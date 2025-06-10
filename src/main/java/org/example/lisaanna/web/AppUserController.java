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
 *
 */
@RestController
@RequestMapping("/user")
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
/*
    @GetMapping
    public String getUserPage() {
        return "user";
    }

 */

    /**
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAppUserList());
    }

    /**
     * @param username
     * @return
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
     * @param appUserDTO
     * @return
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
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
