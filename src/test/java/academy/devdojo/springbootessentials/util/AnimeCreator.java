package academy.devdojo.springbootessentials.util;

import academy.devdojo.springbootessentials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Demon Slayer")
                .url("http://localhost:8080/demon-slayer")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("Demon Slayer")
                .url("http://localhost:8080/demon-slayer")
                .build();
    }

    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .id(1)
                .name("Demon Slayer")
                .url("http://localhost:8080/demon-slayer")
                .build();
    }
}
