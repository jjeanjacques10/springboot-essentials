package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Anime;
import academy.devdojo.springbootessentials.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class AnimeRepository {

    private final Utils utils;
    private static List<Anime> animes;

    static {
        animes = new ArrayList<>(List.of(new Anime(1, "Bakuman"),
                new Anime(2, "Naruto"),
                new Anime(3, "Boruto")));
    }

    public List<Anime> listAll() {
        return animes;
    }

    public Anime findById(int id) {
        return utils.findAnimeOrThrowNotFound(id, animes);
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextInt(4, 100000));
        animes.add(anime);
        return anime;
    }

    public void delete(int id) {
        animes.remove(utils.findAnimeOrThrowNotFound(id, animes));
    }

    public void update(Anime anime) {
        animes.remove(utils.findAnimeOrThrowNotFound(anime.getId(), animes));
        animes.add(anime);
    }
}
