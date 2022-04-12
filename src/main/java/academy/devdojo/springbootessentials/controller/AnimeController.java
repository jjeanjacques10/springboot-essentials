package academy.devdojo.springbootessentials.controller;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("animes")
public class AnimeController {

    private final AnimeService animeRepository;

    @GetMapping
    @Operation(summary = "List all animes paginated sorted",
            description = "To use pagination and sort add the params ?page='number'&sort='field' to the url")
    public ResponseEntity<Page<Anime>> listAll(Pageable pageable) {
        return ResponseEntity.ok(animeRepository.listAll(pageable));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Anime> findById(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User logged in {}", userDetails);
        return ResponseEntity.ok(animeRepository.findById(id));
    }

    @GetMapping("/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(animeRepository.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid Anime anime) {
        return ResponseEntity.ok(animeRepository.save(anime));
    }

    @DeleteMapping("/admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        animeRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Anime anime) {
        animeRepository.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
