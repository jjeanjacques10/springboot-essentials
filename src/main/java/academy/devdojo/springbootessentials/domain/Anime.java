package academy.devdojo.springbootessentials.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotEmpty(message = "The name of this anime cannot be empty")
    @Schema(description = "Anime's name", example = "Naruto", required = true)
    private String name;
    @URL
    @NotNull
    @Schema(description = "Anime's url", example = "http://localhost:8080/naruto", required = true)
    private String url;

}
