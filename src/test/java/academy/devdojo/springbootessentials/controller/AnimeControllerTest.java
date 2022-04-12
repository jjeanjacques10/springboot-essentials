package academy.devdojo.springbootessentials.controller;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.service.AnimeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static academy.devdojo.springbootessentials.util.AnimeCreator.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(any())).thenReturn(animePage);
        BDDMockito.when(animeServiceMock.findById(anyInt())).thenReturn(createValidAnime());
        BDDMockito.when(animeServiceMock.findByName(anyString())).thenReturn(List.of(createValidAnime()));
        BDDMockito.when(animeServiceMock.save(any())).thenReturn(createValidAnime());
        BDDMockito.doNothing().when(animeServiceMock).delete(anyInt());
        BDDMockito.when(animeServiceMock.save(createValidAnime())).thenReturn(createValidUpdateAnime());
    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    void should_List_All_Animes() {
        String expectedName = createValidAnime().getName();

        Page<Anime> animePage = animeController.listAll(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    void should_FindById_Anime() {
        Integer expectedId = createValidAnime().getId();

        Anime anime = animeController.findById(1, null).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a pageable list of animes when successful")
    void should_FindByAnime_Animes() {
        String expectedName = createValidAnime().getName();

        List<Anime> animeList = animeController.findByName("DBS").getBody();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Save anime and returns an anime object when successful")
    void should_Save_Create_Anime() {
        Integer expectedId = createValidAnime().getId();
        Anime animeToBeSaved = createAnimeToBeSaved();

        Anime animeCreated = animeController.save(animeToBeSaved).getBody();

        Assertions.assertThat(animeCreated).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete an anime when successful")
    void should_Delete_Anime() {
        ResponseEntity<Void> responseEntity = animeController.delete(1);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("Update anime and returns an anime object when successful")
    void should_Update_Anime() {
        Anime validUpdateAnime = createValidUpdateAnime();

        ResponseEntity<Void> responseEntity = animeController.update(validUpdateAnime);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }
}