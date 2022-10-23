INSERT INTO roles VALUES (1, 'ROLE_USER'),
                         (2, 'ROLE_ADMIN');

INSERT INTO users VALUES (1, 18, 'ivanov@ya.ru', 'Коля', 'Иванов', '$2a$12$ACf5y/xKfQT4N26uhbYCVOw9MakE8YJvJLMTLSXGYZPaEPsZuJAG6'),
                         (2, 25, 'petrov@ya.ru', 'Вася', 'Петров', '$2a$12$m3qtpvhstctaNoKnzqgR9.dq4.gaIgp44J4ge5Rt5ycp2dOqSd7US');
-- Пароль у Коли: 456 (user), Пароль у Васи: 123 (admin)

INSERT INTO users_roles VALUES (1, 1),
                               (2, 1),
                               (2, 2);