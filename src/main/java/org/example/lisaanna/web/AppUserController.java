package org.example.lisaanna.web;

import jakarta.validation.Valid;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public String getUserPage() {
        return "user";
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAppUserList());
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        if (appUserService.getAppUserByUsername(username) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(appUserService.getAppUserByUsername(username));
        }
    }

    ///  ////// Kolla av mot databasen om en användare med samma namn redan finns
    @PostMapping
    public ResponseEntity<AppUser> addUser(@Valid @RequestBody AppUser appUser) {
        //appUserService.saveUser(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }
    /*      @PostMapping
        public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
            if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                movieService.addMovie(movie);
                return ResponseEntity.status(HttpStatus.CREATED).body(movie);
            }
        }
        */

    @DeleteMapping("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        if (id >= 0 && id < appUserService.getAppUserList().size()) {
            //appUserService.deleteUser();
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
