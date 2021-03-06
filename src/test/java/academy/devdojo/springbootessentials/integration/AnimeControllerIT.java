package academy.devdojo.springbootessentials.integration;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.repository.AnimeRepository;
import academy.devdojo.springbootessentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static academy.devdojo.springbootessentials.util.AnimeCreator.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AnimeControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;

    @MockBean
    private AnimeRepository animeRepositoryMock;

    @Lazy
    @TestConfiguration
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("jean", "barros");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("jacques", "barros");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(any(PageRequest.class)))
                .thenReturn(animePage);
        BDDMockito.when(animeRepositoryMock.findById(anyInt())).thenReturn(Optional.of(createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findByName(anyString())).thenReturn(List.of(createValidAnime()));
        BDDMockito.when(animeRepositoryMock.save(any())).thenReturn(createValidAnime());
        BDDMockito.doNothing().when(animeRepositoryMock).delete(any(Anime.class));
        BDDMockito.when(animeRepositoryMock.save(createValidAnime())).thenReturn(createValidUpdateAnime());
    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    void should_List_All_Animes() {
        String expectedName = createValidAnime().getName();

        Page<Anime> animePage = testRestTemplateUser.exchange("/animes", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    void should_FindById_Anime() {
        Integer expectedId = createValidAnime().getId();

        Anime anime = testRestTemplateAdmin.getForObject("/animes/admin/1", Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of animes when successful")
    void should_FindByAnime_Animes() {
        String expectedName = createValidAnime().getName();

        List<Anime> animeList = testRestTemplateUser.exchange("/animes/find?name='DBS'",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Save anime and returns an anime object when successful")
    void should_Save_Create_Anime() {
        Integer expectedId = createValidAnime().getId();
        Anime animeToBeSaved = createAnimeToBeSaved();

        Anime animeCreated = testRestTemplateUser.exchange("/animes", HttpMethod.POST,
                createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();

        Assertions.assertThat(animeCreated).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete an anime when successful")
    void should_Delete_Anime() {
        ResponseEntity<Void> responseEntity = testRestTemplateAdmin.exchange("/animes/admin/1", HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("return 403 when try to delete an anime")
    void should_Return403_When_Delete_Anime() {
        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/animes/admin/1", HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Update anime and returns an anime object when successful")
    void should_Update_Anime() {
        Anime validUpdateAnime = createValidUpdateAnime();

        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/animes", HttpMethod.PUT, createJsonHttpEntity(validUpdateAnime), Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    private HttpEntity<Anime> createJsonHttpEntity(Anime anime) {
        return new HttpEntity<>(anime, createJsonHeader());
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}