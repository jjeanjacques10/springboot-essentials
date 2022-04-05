package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnimeRepository {

    public List<Anime> listAll() {
        return List.of(
                new Anime(1, "Bakuman"),
                new Anime(2, "Naruto"),
                new Anime(3, "Boruto"));
    }
}
