INSERT INTO anime.user (id, authorities, name, password, username) VALUES (1, 'USER_ROLE', 'jean', '{bcrypt}$2a$10$rPqNlIhWXim1Lq.DD23l9u9rmh94Ayc06JK0sSCxhTqxErzQAiJR.', 'jean');
INSERT INTO anime.user (id, authorities, name, password, username) VALUES (1, 'USER_ROLE,ADMIN_ROLE', 'root', '{bcrypt}$2a$10$rPqNlIhWXim1Lq.DD23l9u9rmh94Ayc06JK0sSCxhTqxErzQAiJR.', 'root');

INSERT INTO anime.anime (id, name, url) VALUES ('1', 'Naruto', 'http://localhost/naruto');
INSERT INTO anime.anime (id, name, url) VALUES ('2', 'DBZ', 'http://localhost/dbz');
INSERT INTO anime.anime (id, name, url) VALUES ('3', 'Digimon', 'http://localhost/digimon');