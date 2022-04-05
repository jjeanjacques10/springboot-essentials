package academy.devdojo.springbootessentials.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResourceNotFoundDetails {
    private String title;
    private int status;
    private String details;
    private LocalDateTime timestamp;
    private String developerMethod;
}
