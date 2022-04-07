package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Anime Repository Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    void should_Save_Persist_Anime_When_Successful() {
        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo("Demon Slayer");
    }

    @Test
    void should_Update_Anime_When_Successful() {
        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);

        savedAnime.setName("Demon Slayer: Movie (I have to watch it)");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
    }

    @Test
    void should_Delete_Anime_When_Successful() {
        Anime anime = createAnime();

        Anime savedAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    void should_FindByName_And_Return_Anime() {
        Anime anime = createAnime();
        Anime savedAnime = this.animeRepository.save(anime);

        String name = savedAnime.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes).contains(savedAnime);
    }

    @Test
    void should_Not_Find_Anime_By_Name() {
        String name = "No exists";
        List<Anime> animes = this.animeRepository.findByName(name);
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    void should_Throw_ConstraintViolationException_When_Name_Is_Empty() {
        Anime anime = new Anime();
        /*Assertions.assertThatThrownBy(() -> animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);*/

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> animeRepository.save(anime))
                .withMessageContaining("The name of this anime cannot be empty");
    }

    private Anime createAnime() {
        return Anime.builder()
                .name("Demon Slayer")
                .url("http://localhost:8080/demon-slayer")
                .build();
    }

}