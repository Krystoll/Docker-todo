# TodoApp — учебный проект с CI/CD

REST API для управления задачами. Приложение на Spring Boot, база данных PostgreSQL, оба сервиса запускаются в контейнерах.  
Настроен полный CI/CD‑пайплайн: сборка → линтер → тесты + проверка покрытия → сборка Docker‑образа → публикация на Docker Hub.



## Технологии

| Компонент        | Технология                          |
|------------------|-------------------------------------|
| Язык             | Java 21                             |
| Фреймворк        | Spring Boot 3.2.5                   |
| Сборка           | Maven                               |
| База данных      | PostgreSQL 17                       |
| Тесты            | JUnit 5 + Mockito                   |
| Покрытие         | JaCoCo (порог **50%**)              |
| Линтер           | Checkstyle                          |
| Контейнеризация  | Docker + Docker Compose             |
| CI/CD            | GitHub Actions                      |
| Реестр образов   | Docker Hub                          |

## Функциональность API

CRUD + фильтрация и поиск по задачам.

### Модель задачи

- `id` (Long, автоинкремент)
- `title` (String, обязательное)
- `description` (String, опционально)
- `status` (enum: `TODO`, `IN_PROGRESS`, `DONE`)
- `priority` (enum: `LOW`, `MEDIUM`, `HIGH`)
- `dueDate` (LocalDate, опционально)

### Эндпоинты

| Метод | Эндпоинт                                      | Описание                          |
|-------|-----------------------------------------------|-----------------------------------|
| GET   | `/api/tasks`                                  | Все задачи                        |
| GET   | `/api/tasks?status=TODO`                      | Фильтр по статусу                 |
| GET   | `/api/tasks?priority=HIGH`                    | Фильтр по приоритету              |
| GET   | `/api/tasks?search=docker`                    | Поиск по названию (без учёта регистра) |
| GET   | `/api/tasks/{id}`                             | Получить задачу по ID             |
| POST  | `/api/tasks`                                  | Создать задачу                    |
| PUT   | `/api/tasks/{id}`                             | Полное обновление задачи          |
| DELETE| `/api/tasks/{id}`                             | Удалить задачу                    |

### Примеры запросов (curl)

**Создать задачу**

```
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Изучить CI/CD",
    "description": "Настроить GitHub Actions",
    "status": "TODO",
    "priority": "HIGH",
    "dueDate": "2025-05-01"
  }'
```

**Получить все задачи**
```
curl http://localhost:8080/api/tasks
``` 

**Фильтр по статусу**
```
curl "http://localhost:8080/api/tasks?status=TODO"
```

**Поиск по названию**
```
curl "http://localhost:8080/api/tasks?search=docker"
```

**Обновить задачу**
```
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "CI/CD настроен",
    "status": "DONE",
    "priority": "MEDIUM",
    "dueDate": "2025-05-01"
  }'
```

**Удалить задачу**
```
curl -X DELETE http://localhost:8080/api/tasks/1
```

## Запуск проекта локально (Docker Compose)

### Требования

- Docker Desktop (или Docker + Compose)
- Git

### Инструкция

**Клонируйте репозиторий:**
```
git clone https://github.com/ваш-аккаунт/todo-docker.git
cd todo-docker
```

**Создайте файл окружения из шаблона:**
```
cp .env.example .env
```
*(Отредактируйте пароли в .env)*

**Запустите приложение и БД:**
```
docker compose up --build
```
*API будет доступно по адресу http://localhost:8080/api/tasks.*


**Остановка**
```
docker compose down
```

**Чтобы удалить том с данными PostgreSQL:**
```
docker compose down -v
```

## CI/CD пайплайн (GitHub Actions)
Файл конфигурации: ```.github/workflows/ci.yml.``` 

### Триггеры

- pull_request в ветку main
- push в ветку main

## Jobs

| Job              | Что делает                          |
|------------------|-------------------------------------|
| build            | Сборка приложения (mvn package -DskipTests)                                           |
| lint             | Запуск Checkstyle (mvn checkstyle:check) – падает при ошибках                         |
| test             | Запуск тестов, проверка покрытия JaCoCo (порог 50%). Сохраняет HTML‑отчёт как артефакт|
| docker_build     | Сборка Docker‑образа с тегами                                                         |
| docker_push      | Публикация образа на Docker Hub - только при push в main                              |
              
**Артефакты:** после каждого запуска test вы можете скачать отчёт JaCoCo (папка jacoco-coverage-report) со страницы Actions.

## Настройка секретов GitHub

Для публикации образа в Docker Hub необходимо добавить секреты в репозиторий:

**Settings → Secrets and variables → Actions → New repository secret**

| Имя секрета            | Значение                                                  |
|------------------------|-----------------------------------------------------------|
| `DOCKERHUB_USERNAME`   | Ваш логин на Docker Hub                                   |
| `DOCKERHUB_TOKEN`      | Personal Access Token (права **Read, Write, Delete**)     |

**Токен создаётся на** [hub.docker.com](https://hub.docker.com) → **Account Settings** → **Personal Access Tokens**.

## Структура проекта

```text
todo-docker/
├── .github/workflows/ci.yml
├── app/
│   ├── checkstyle.xml
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/todo/
│       │   ├── TodoApplication.java
│       │   ├── controller/TaskController.java
│       │   ├── service/TaskService.java
│       │   ├── repository/TaskRepository.java
│       │   ├── model/Task.java
│       │   ├── dto/TaskRequest.java
│       │   └── exception/
│       └── test/
│           ├── java/com/example/todo/
│           │   ├── service/TaskServiceTest.java
│           │   └── repository/TaskRepositoryTest.java
│           └── resources/application-test.properties
├── db/
│   ├── Dockerfile
│   └── init.sql
├── docker-compose.yml
├── .env.example
├── .gitignore          
└── README.md
