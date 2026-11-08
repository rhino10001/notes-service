Спроектируйте и реализуйте REST API сервиса «Управление заметками» (Notes Service) на Spring Boot.

Функциональность:
- CRUD для заметок (title, content, createdAt, tags).
- GET /notes?tag=work — фильтрация по тегу.
- Заметки хранятся в in-memory структуре (Map или H2).

Требования:
1. Структура Maven-проекта (pom.xml с зависимостями spring-boot-starter-web, spring-boot-starter-test).
2. REST-контроллер с корректными HTTP-методами и кодами ответов (201 Created, 404 Not Found, 400 Bad Request).
3. Сервисный слой с бизнес-логикой, соответствующий SOLID (SRP, DIP через интерфейс репозитория).
4. Unit-тесты для сервисного слоя (JUnit 5 + Mockito, не менее 3 тестов).
5. Dockerfile (multi-stage: build с Maven → run на eclipse-temurin:17-jre-alpine).
   Время: до 60 минут.