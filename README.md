# Spring Boot Essentials

Spring Boot course from DevDojo Academy

## Topics

- @Component, @Autowired and @SpringBootApplication
- Hot Swap
- Spring Initializr
- CRUD (GET, READ, UPDATE and DELETE)
- JPA (MySQL)
- Exception Handling

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

## Playlist

- https://www.youtube.com/playlist?list=PL0Un1HNdB4jHTXBeJ8u3Kaz0NMxuMkmOY

---
Developed by [Jean Jacques](https://github.com/jjeanjacques10)