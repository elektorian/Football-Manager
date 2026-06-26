# Архитектура Football Manager

## Стек

Язык: Kotlin 2.1.21
Фреймворк: Spring Boot 3.3.4
Версия Java: 21
Сборка: Gradle (Kotlin DSL)

## Архитектурные решения

Всё состояние игры хранится в оперативной памяти. Никакой базы данных во время игровой сессии нет. Сохранение и загрузка — это сериализация и десериализация всего состояния в файл.

Ядро игры не зависит от Spring, JPA, HTTP и любых других внешних библиотек. Доменная логика изолирована от фреймворка.

При продвижении по дням движок генерирует события. Фронтенд читает эти события и показывает пользователю, что произошло.

## Правило зависимостей

Доменный слой не должен зависеть ни от чего внешнего. В нём нет Spring, JPA или HTTP.

Слой приложения зависит только от домена через интерфейсы.

Слой инфраструктуры реализует интерфейсы, определённые в домене или приложении.

Слой представления зависит только от приложения.

## Структура проекта

Ниже перечислены пакеты, обнаруженные в исходниках. Если пакет пуст (не содержит .kt файлов) или отсутствует — он скрыт.


## /Users/edegtyannikov/IdeaProjects/Football-Manager/src/main/kotlin/com/footballmanager

Пакет: `com.footballmanager`

### Файлы

- `FootballManagerApplication.kt`
- `DayProcessor.kt`
- `GameWorld.kt`
- `GameEvent.kt`
- `EventCalendar.kt`
- `ScheduledEvent.kt`
- `GameClock.kt`
- `GameEngine.kt`

### Типы

- `class FootballManagerApplication`
- `interface DayProcessor`
- `class GameWorld`
- `sealed GameEvent`
- `class EventCalendar`
- `sealed ScheduledEvent`
- `data GameClock`
- `class GameEngine`


## engine

Пакет: `com.footballmanager/engine`

### Файлы

- `DayProcessor.kt`
- `GameWorld.kt`
- `GameEvent.kt`
- `EventCalendar.kt`
- `ScheduledEvent.kt`
- `GameClock.kt`
- `GameEngine.kt`

### Типы

- `interface DayProcessor`
- `class GameWorld`
- `sealed GameEvent`
- `class EventCalendar`
- `sealed ScheduledEvent`
- `data GameClock`
- `class GameEngine`

