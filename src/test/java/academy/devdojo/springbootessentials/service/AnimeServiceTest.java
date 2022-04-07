package academy.devdojo.springbootessentials.service;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.exception.ResourceNotFoundException;
import academy.devdojo.springbootessentials.repository.AnimeRepository;
import academy.devdojo.springbootessentials.util.Utils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static academy.devdojo.springbootessentials.util.AnimeCreator.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @Mock
    private Utils utilsMock;

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
        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(anyInt(), any(AnimeRepository.class)))
                .thenReturn(createValidUpdateAnime());
    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    void should_List_All_Animes() {
        String expectedName = createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    void should_FindById_Anime() {
        Integer expectedId = createValidAnime().getId();

        Anime anime = animeService.findById(1);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a pageable list of animes when successful")
    void should_FindByAnime_Animes() {
        String expectedName = createValidAnime().getName();

        List<Anime> animeList = animeService.findByName("DBS");

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Save anime and returns an anime object when successful")
    void should_Save_Create_Anime() {
        Integer expectedId = createValidAnime().getId();
        Anime animeToBeSaved = createAnimeToBeSaved();

        Anime animeCreated = animeService.save(animeToBeSaved);

        Assertions.assertThat(animeCreated).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isNotNull();
        Assertions.assertThat(animeCreated.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete an anime when successful")
    void should_Delete_Anime() {
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when the anime not exists")
    void should_Not_Delete_Anime_When_Not_exists() {
        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(anyInt(), any(AnimeRepository.class)))
                .thenThrow(new ResourceNotFoundException("Anime Not Found"));
        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> animeService.delete(100));
    }

    @Test
    @DisplayName("Update anime when successful")
    void should_Update_Anime() {
        Anime validUpdateAnime = createValidUpdateAnime();

        String expectedName = validUpdateAnime.getName();

        Anime validAnime = createValidAnime();

        Anime anime = animeService.save(validAnime);
        animeService.update(validUpdateAnime);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(expectedName);
    }
}