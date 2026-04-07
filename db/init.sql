
-- Типы-перечисления (enum) в PostgreSQL
CREATE TYPE task_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');
CREATE TYPE task_status   AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');

-- Основная таблица задач
CREATE TABLE IF NOT EXISTS tasks (
    id          BIGSERIAL    PRIMARY KEY,           -- автоинкремент (SERIAL = INTEGER + SEQUENCE)
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    priority    task_priority NOT NULL DEFAULT 'MEDIUM',
    due_date    DATE,
    status      task_status   NOT NULL DEFAULT 'TODO'
);

-- Начальные данные для демонстрации
INSERT INTO tasks (title, description, priority, due_date, status) VALUES
    ('Написать Docker-file',     'Сборка Spring Boot',   'HIGH',   '2025-06-01', 'IN_PROGRESS'),
    ('Настроить docker-compose', 'Сеть, volume, переменные окружения', 'HIGH',   '2025-06-02', 'TODO'),
    ('Выгрузить на GitHub',       'Настроить .gitignore, написать README',   'HIGH',   '2025-06-10', 'TODO'),
    ('Сдать лабораторную',        'Отправить на почту с 8:00-11:15',      'MEDIUM', '2025-06-05', 'TODO');
