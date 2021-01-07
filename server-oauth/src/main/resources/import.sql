INSERT INTO usuarios (usuario, password, enabled) VALUES ('admin', '$2a$10$CJ5p.7NfVg32n1CQ0aqpZuawZ/7eiTPgP.OUieLRPlNGuJpoWJkFu', 1);
INSERT INTO usuarios (usuario, password, enabled) VALUES ('arturo', '$2a$10$5IQiDJaSWMF/eaZKAW00hu5YhJFe4QYBZuGDvdzdPAtJriXqd1eSO', 1);
INSERT INTO usuarios (usuario, password, enabled) VALUES ('carlos', '$2a$10$JxWjJ5Cy8i30c4caI2KtNOLiyP8VKT7qHI1r63nStEPkhZJOETJIi', 1);
INSERT INTO usuarios (usuario, password, enabled) VALUES ('javi', '$2a$10$tS/VK7pGTGGz.6TPzthDN.s8Y/doz.4cxacPpSZYDuqaKgebeQaAm', 1);


INSERT INTO roles (id_usuario, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id_usuario, authority) VALUES (2, 'ROLE_USER');
INSERT INTO roles (id_usuario, authority) VALUES (3, 'ROLE_USER');
INSERT INTO roles (id_usuario, authority) VALUES (4, 'ROLE_USER');