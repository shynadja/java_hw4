# Tic-Tac-Toe REST API

Простое REST API для игры в крестики-нолики, реализованное на Spring Boot.

## Возможности

- Создание новых игр с настраиваемым размером поля
- Выполнение ходов игроков
- Отслеживание состояния игры (ход игры, победа, ничья)
- Просмотр истории последних игр
- Удаление завершенных игр

## Технологии

- **Java 24**
- **Spring Boot 4.0.0-SNAPSHOT**
- **Gradle 9.1**
- **Tomcat 11** (встроенный)

## Установка и запуск

### Предварительные требования
- JDK 24
- Gradle 9.1

### Запуск приложения

1. Клонируйте репозиторий:
```bash
git clone <repository-url>
cd hello-spring
Запустите приложение:

```bash
./gradlew bootRun
Или соберите и запустите JAR:

```bash
./gradlew build
java -jar build/libs/hello-spring-0.0.1-SNAPSHOT.jar
Приложение будет доступно по адресу: http://localhost:8080

API Endpoints
- Получить все игры: 
GET /api/games

- Получить последние игры:
GET /api/games/recent?limit=10
Параметры:
limit - количество игр (по умолчанию: 10)

- Создать новую игру:
POST /api/games?size=3&player1=Alice&player2=Bob
Параметры:
size - размер игрового поля (минимум 3)
player1 - имя первого игрока
player2 - имя второго игрока

- Получить игру по ID:
GET /api/games/{id}

- Сделать ход:
POST /api/games/{id}/move?row=1&col=1
Параметры:
row - номер строки (начинается с 1)
col - номер столбца (начинается с 1)

- Удалить игру:
DELETE /api/games/{id}

Пример использования
1. Создание игры
```bash
curl -X POST "http://localhost:8080/api/games?size=3&player1=XPlayer&player2=OPlayer"

2. Получение информации об игре
```bash
curl "http://localhost:8080/api/games/1"

3. Выполнение хода
```bash
curl -X POST "http://localhost:8080/api/games/1/move?row=1&col=1"

4. Просмотр последних игр
```bash
curl "http://localhost:8080/api/games/recent?limit=5"


Модель данных
Game
id - уникальный идентификатор игры

board - игровое поле

player1, player2 - игроки

currentPlayer - текущий игрок

status - статус игры (IN_PROGRESS, PLAYER1_WON, PLAYER2_WON, DRAW)

createdAt, updatedAt - временные метки

Player
name - имя игрока
mark - метка игрока ('X' или 'O')

Board
size - размер поля
grid - двумерный массив representing the game state

Структура проекта

src/main/java/com/example/hello_spring/
├── HelloSpringApplication.java     # Главный класс приложения
├── controller/
│   └── GameController.java         # REST контроллер
├── service/
│   └── GameService.java            # Бизнес-логика
├── repository/
│   └── GameRepository.java         # Хранилище данных
└── model/
    ├── Game.java                   # Модель игры
    ├── Player.java                 # Модель игрока
    ├── Board.java                  # Модель игрового поля
    └── GameStatus.java             # Перечисление статусов игры

Правила игры
Игроки по очереди ставят свои метки ('X' и 'O') на поле.
Побеждает игрок, первым собравший линию своих меток (горизонтальную, вертикальную или диагональную).
Если все клетки заполнены, но победителя нет - объявляется ничья.
Размер поля может быть любым (≥3), но стандартный - 3×3.