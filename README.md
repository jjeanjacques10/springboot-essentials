# Spring Boot Essentials

Spring Boot course from DevDojo Academy

## Topics

- @Component, @Autowired and @SpringBootApplication
- Hot Swap
- Spring Initializr
- CRUD (GET, READ, UPDATE and DELETE)
- JPA (MySQL)
- Exception Handling
- Paging and Sorting
- Spring Security
- [OpenAPI](https://springdoc.org/)
- Spring Actuator

## Notes

- Avoid return the object prefers use a ResponseEntity

``` java
@GetMapping
public List<Anime> listAll() {} 😕
public ResponseEntity<List<Anime>> listAll() {} ✅
```

- Use config to get stack trace when pass trace param
  ```/animes/6?trace=true``` (Not Found)
- JpaRepository has @NoRepositoryBean
- Use a Handler to get errors and set a template to response (
  Example: [RestExceptionHandler](./src/main/java/academy/devdojo/springbootessentials/handler/RestExceptionHandler.java))

``` json
{
    "title": "Resource not Found",
    "status": 404,
    "details": "Anime Not Found",
    "timestamp": "2022-04-05T20:17:57.3394187",
    "developerMethod": "academy.devdojo.springbootessentials.exception.ResourceNotFoundException"
}
```

- It's possible ignore errors when use transaction ``@Transactional(dontRollbackOn = Exception.class)``
- Configure logger to log hibernate SQL only when level is DEBUG

``` yml
logging:
  level:
    org:
      hibernate:
        SQL: DEBUG
```

- RestTemplate is used to simulate requests to your API
- Use a mock database to create tests with JPA, such as H2

``` xml
<!-- Mock database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope> <!-- Set scope -->
</dependency>
```

- Change display name tests format using ```@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)```

``` java
void should_Save_Persist_Anime_When_Successful() -> "should Save Persist Anime When Successful"
```

- Two ways to get exceptions in tests

``` java
Assertions.assertThatThrownBy(() -> animeRepository.save(anime))
        .isInstanceOf(ConstraintViolationException.class);

Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> animeRepository.save(anime))
        .withMessageContaining("The name of this anime cannot be empty");
```

- Use @ExtendWith(SpringExtension.class) to integrates the Spring TestContext Framework into JUnit 5's Jupiter
  programming model
- When my method doesn't return a result I can use this Mock

``` java
Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
```

- Get port using
  @LocalServerPort ([Example](./src/test/java/academy/devdojo/springbootessentials/integration/AnimeControllerIT.java))

``` java
@Value("${local.server.port}")
public @interface LocalServerPort {
}
```

- Mock database as a Bean (@MockBean)
- Set Maven profile for Integration Tests

``` xml
<profiles>
    <profile>
        <id>integration-tests</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <includes>
                            <include>**/*IT.*</include>
                        </includes>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

- Get XSRF in Postman using JavaScript

``` javascript
var xsrfCookie = postman.getResponseCookie("XSRF-TOKEN");
postman.setEnvironmentVariable("x-xsrf-token", xsrfCookie.value);
```

<img src="https://i.ytimg.com/vi/9JrzPX1pVjs/maxresdefault.jpg" width="600px"/>

- Protect specific routes using ``@PreAuthorize("hasRole('ADMIN')")``
- Hidden parameter when you're using Swagger

``` java
@Parameter(hidden = true)
```

- Use http://localhost:8080/swagger-ui/index.html to access documentation or http://localhost:8080/v3/api-docs
- Download yaml docs version use http://localhost:8080/v3/api-docs.yaml

## Get Started

Start database container docker:

``` bash
docker-compose up -d
```

Run spring application:

```
mvn clean install package

java -jar .\target\springboot-essentials-0.0.1-SNAPSHOT.jar
```

It's possible change database config editing [docker-compose.yml](docker-compose.yml)

## Test

Run all tests (unit and integration tests)

``` bash
mvn test 
```

Run only integration tests

``` bash
mvn test -Pintegration-tests 
```

Collection Postman folder [file](./files/Springboot%20Essentials.postman_collection.json)

## Playlist

- https://www.youtube.com/playlist?list=PL0Un1HNdB4jHTXBeJ8u3Kaz0NMxuMkmOY

---
Developed by [Jean Jacques](https://github.com/jjeanjacques10)