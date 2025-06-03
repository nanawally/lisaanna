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
    public String getUserPage(){
        return "user";
    }

    @GetMapping()
    public String getUsers(Model model){
        List<AppUser> appUserList = appUserService.getAppUserList();
        model.addAttribute("appUserList", appUserList);
        return "userlist";
    }

    @GetMapping("/{username}")
    public String getUserByUsername(@PathVariable String username, Model model){
        AppUser appUser = appUserService.getAppUserByUsername(username);
        model.addAttribute("appUser", appUser);
        return "user";
    }

    @PostMapping
    public ResponseEntity<AppUser> addUser(@Valid @RequestBody AppUser appUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }

/*    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        if (id > 0 && id < MovieService.movieList.size()) {
            return ResponseEntity.ok(movieService.getMovieByID(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> getMovieByTitle(@RequestParam String title) {
        List<Movie> movieList = movieService.getMovieByTitle(title);
        if (!movieList.isEmpty()) {
            return ResponseEntity.ok(movieList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            movieService.addMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        if (id > 0 && id < MovieService.movieList.size()) {
            movieService.updateMovie(id, movie);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable int id) {
        if (id >= 0 && id < MovieService.movieList.size()) {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }*/

}
