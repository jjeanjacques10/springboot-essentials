package academy.devdojo.springbootessentials.client;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class SpringClient {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("barros"));
        //testGetWithParams();
        //callLists();

        Anime ghost = Anime.builder().name("Ghost in the Shell")
                .url("https://localhost:8080/ghost-in-the-shell").build();

        Anime animeCreated = postRestTemplate(ghost);
        putRestTemplate(animeCreated);
        deleteRestTemplate(animeCreated);
    }

    private static void deleteRestTemplate(Anime anime) {
        ResponseEntity<Void> animeResponse = new RestTemplate()
                .exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE,
                        null, Void.class, anime.getId());
        log.info("Anime Deleted Entity status: {}", animeResponse.getStatusCode());
    }

    private static void putRestTemplate(Anime anime) {
        anime.setName("Ghost in the Sheel: Movie");
        ResponseEntity<Void> animeResponse = new RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.PUT,
                        new HttpEntity<>(anime, createJsonHeader()), Void.class);
        log.info("Anime Updated Entity status: {}", animeResponse.getStatusCode());
    }

    private static Anime postRestTemplate(Anime ghost) {
        //Anime ghostResponse = new RestTemplate().postForObject("http://localhost:8080/animes", ghost, Anime.class);
        //log.info("Anime Created id: {}", ghostResponse.getId());

        ResponseEntity<Anime> animeResponse = new RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>(ghost, createJsonHeader()), Anime.class);
        log.info("Anime Created Entity id: {}", animeResponse.getBody().getId());
        return animeResponse.getBody();
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private static void testGetWithParams() {
        ResponseEntity<Anime> animeResponseEntity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2); //jackson

        log.info("Response Entity {}", animeResponseEntity);
        log.info("Response Data {}", animeResponseEntity.getBody());

        Anime anime = new RestTemplate()
                .getForObject("http://localhost:8080/animes/{id}", Anime.class, 2); //jackson

        log.info("Anime {}", anime);
    }

    private static void callLists() {
        Anime[] animeArray = new RestTemplate()
                .getForObject("http://localhost:8080/animes", Anime[].class); //jackson

        log.info("Anime Array {}", animeArray);

        ResponseEntity<List<Anime>> exchangeAnimeList = new RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                });

        log.info("Anime List Exchange {}", exchangeAnimeList);

        ResponseEntity<PageableResponse<Anime>> pageAnime = new RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        log.info("Anime Pageable {}", pageAnime);
    }
}
