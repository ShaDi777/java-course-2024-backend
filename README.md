# Link Tracker

[![Bot Build](https://github.com/ShaDi777/java-course-2024-backend/actions/workflows/bot.yml/badge.svg)](https://github.com/ShaDi777/java-course-2024-backend/actions/workflows/bot.yml)
[![Scrapper Build](https://github.com/ShaDi777/java-course-2024-backend/actions/workflows/scrapper.yml/badge.svg)](https://github.com/ShaDi777/java-course-2024-backend/actions/workflows/scrapper.yml)

Приложение для отслеживания обновлений контента по ссылкам. При появлении новых событий (новые вопросы на StackOverflow, изменения в GitHub репозиториях) система отправляет уведомление в Telegram.

**Автор**: Шаланов Дмитрий Владиславович

---

## 📋 Оглавление

- [Технологии](#-технологии)
- [Архитектура](#-архитектура)
- [Компоненты системы](#-компоненты-системы)
- [Быстрый старт](#-быстрый-старт)
- [API](#-api)
- [Мониторинг](#-мониторинг)
- [CI/CD](#-cicd)

---

## 🛠 Технологии

| Категория | Технологии |
|-----------|------------|
| **Язык** | Java 21 |
| **Фреймворк** | Spring Boot 3.2.2, Spring Cloud 2023.0.0 |
| **БД** | PostgreSQL 16 |
| **ORM** | Spring Data JPA, jOOQ 3.19.6 |
| **Миграции** | Liquibase 4.26.0 |
| **Message Broker** | Apache Kafka (Confluent 7.3.2) |
| **Telegram Bot** | java-telegram-bot-api 7.0.1 |
| **Rate Limiting** | Bucket4j 8.10.1 |
| **Метрики** | Micrometer, Prometheus, Grafana |
| **Тестирование** | JUnit 5, Testcontainers, WireMock, RestAssured |
| **Сборка** | Maven, Docker, GitHub Actions |
| **Code Quality** | Checkstyle, JaCoCo |

---

## 🏗 Архитектура

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│   Telegram  │◄────────┤     Bot     │◄───────►│    Kafka    │
│    Client   │  HTTP   │  (8091)     │  Kafka  │  (9092)     │
└─────────────┘         └─────────────┘         └──────┬──────┘
                                                       │
                                                       │ Kafka
                                                       ▼
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│  StackOverflow│       │   Scrapper  │         │ PostgreSQL  │
│   GitHub API  │◄──────┤  (8081)     │◄───────►│   (5432)    │
└─────────────┘  REST   │             │   JDBC  │             │
                         └─────────────┘         └─────────────┘
                                │
                                │ /metrics
                                ▼
                         ┌─────────────┐
                         │  Prometheus │
                         │   (9090)    │
                         └──────┬──────┘
                                │
                                ▼
                         ┌─────────────┐
                         │   Grafana   │
                         │   (3000)    │
                         └─────────────┘
```

---

## 📦 Компоненты системы

### Scrapper (порт 8081)

Сервис для мониторинга внешних источников (StackOverflow, GitHub).

**Возможности:**
- Периодическая проверка ссылок на изменения
- Поддержка StackOverflow (новые ответы на вопросы)
- Поддержка GitHub (новые события в репозиториях)
- Отправка событий в Kafka
- REST API для управления ссылками
- Rate limiting через Bucket4j

**Модули:**
- `scrapper` — основной сервис
- `scrapper-jooq` — jOOQ генерация для типобезопасного доступа к БД

### Bot (порт 8091)

Telegram-бот для взаимодействия с пользователем.

**Возможности:**
- Регистрация пользователя по команде `/start`
- Добавление/удаление ссылок для отслеживания
- Получение уведомлений о новых событиях
- Команда `/list` для просмотра отслеживаемых ссылок
- Потребление событий из Kafka

---

## 🚀 Быстрый старт

### Требования

- Java 21+
- Maven 3.8.6+
- Docker & Docker Compose

### 1. Запуск инфраструктуры

```bash
docker compose up -d
```

Запустит:
- PostgreSQL (порт 5432)
- Liquibase (автоматическая миграция БД)
- ZooKeeper (порт 2181)
- Kafka (порт 9092)
- Prometheus (порт 9090)
- Grafana (порт 3000, логин/пароль: `admin/admin`)

### 2. Сборка проекта

```bash
./mvnw clean install
```

### 3. Запуск приложений

**Scrapper:**
```bash
java -jar scrapper/target/scrapper.jar
```

**Bot:**
```bash
java -jar bot/target/bot.jar
```

### 4. Конфигурация

Создайте файл `application.properties` или передайте переменные окружения:

```properties
# Telegram Bot
telegram.bot.token=<YOUR_BOT_TOKEN>

# База данных
spring.datasource.url=jdbc:postgresql://localhost:5432/scrapper
spring.datasource.username=postgres
spring.datasource.password=postgres

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
```

---

## 📡 API

### Scrapper API

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/links` | Добавить ссылку для отслеживания |
| `DELETE` | `/api/links/{linkId}` | Удалить ссылку |
| `GET` | `/api/links` | Получить список ссылок |
| `POST` | `/api/callbacks` | Регистрировать callback URL |

### Bot API

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/telegram/{chatId}/links` | Добавить ссылку (от имени бота) |
| `DELETE` | `/api/telegram/{chatId}/links/{linkId}` | Удалить ссылку |
| `GET` | `/api/telegram/{chatId}/links` | Получить список ссылок чата |

### Swagger UI

- **Scrapper**: http://localhost:8081/swagger-ui.html
- **Bot**: http://localhost:8091/swagger-ui.html

---

## 📊 Мониторинг

### Prometheus

Метрики доступны по адресу: http://localhost:9090

- Scrapper: http://localhost:8081/metrics
- Bot: http://localhost:8091/metrics

Конфигурация scraping находится в `metrics/prometheus.yml`.

### Grafana

Дашборды доступны по адресу: http://localhost:3000

**Логин/пароль**: `admin/admin`

---

## 🔧 CI/CD

Проект использует GitHub Actions для автоматизации:

### Workflows

| Workflow | Триггер | Описание |
|----------|---------|----------|
| `scrapper.yml` | PR в scrapper/** | Build, Checkstyle, Integration Tests, Docker Push, JaCoCo |
| `bot.yml` | PR в bot/** | Build, Checkstyle, Integration Tests, Docker Push, JaCoCo |

### Docker Registry

Образы публикуются в GitHub Container Registry:
- `ghcr.io/shadi777/java-course-2024-backend/link-tracker-scrapper`
- `ghcr.io/shadi777/java-course-2024-backend/link-tracker-bot`

### Code Coverage

JaCoCo отчётность с минимальным порогом 30% (общее покрытие и покрытие изменённых файлов).

---

## 📁 Структура проекта

```
.
├── bot/                    # Telegram бот
│   ├── src/
│   ├── pom.xml
│   └── bot.Dockerfile
├── scrapper/               # Скраппер сервис
│   ├── src/
│   ├── pom.xml
│   └── scrapper.Dockerfile
├── scrapper-jooq/          # jOOQ генерация
│   ├── src/
│   └── pom.xml
├── migrations/             # Liquibase миграции
│   ├── master.xml
│   ├── V0.1.0-init_tables.sql
│   ├── V0.1.1-alter_links_table.sql
│   └── V1.0.0-add_stackoverflow_table.sql
├── metrics/                # Prometheus конфигурация
│   └── prometheus.yml
├── compose.yml             # Docker Compose
├── pom.xml                 # Maven parent POM
└── checkstyle.xml          # Checkstyle конфигурация
```

---

## 🧪 Тестирование

### Запуск тестов

```bash
# Unit тесты
./mvnw test

# Integration тесты
./mvnw verify

# Checkstyle
./mvnw checkstyle:check

# JaCoCo отчёт
./mvnw jacoco:report
```

### Testcontainers

Проект использует Testcontainers для интеграционных тестов:
- PostgreSQL контейнер для тестов БД
- Kafka контейнер для тестов асинхронной коммуникации
- WireMock для мокирования внешних API
