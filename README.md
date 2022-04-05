# Spring Boot Essentials

Spring Boot course from DevDojo Academy

## Topics

- @Component, @Autowired and @SpringBootApplication
- Hot Swap
- Spring Initializr
- CRUD (GET, READ, UPDATE and DELETE)

## Notes

- Avoid return the object prefers use a ResponseEntity
``` java
@GetMapping
public List<Anime> listAll() {} 😕
public ResponseEntity<List<Anime>> listAll() {} ✅
```
- Use config to get stack trace when pass trace param
```/animes/6?trace=true``` (Not Found)

## Playlist

- https://www.youtube.com/playlist?list=PL0Un1HNdB4jHTXBeJ8u3Kaz0NMxuMkmOY

---
Developed by [Jean Jacques](https://github.com/jjeanjacques10)