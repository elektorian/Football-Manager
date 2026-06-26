# Доменная модель

## Пакет

```
com.footballmanager.domain/
├── Person.kt
├── PersonRole.kt
├── Position.kt
├── Contract.kt
├── Club.kt
├── Competition.kt
├── Fixture.kt
├── Standing.kt
└── Formation.kt
```

## Сущности

### Person

Центральная сущность. Один класс для игроков и персонала. Роль определяет специализацию.

Поля: id, name, age, nationality, role, positions (список), ability (0-200), potential (0-200), contract (0..1), injured, injuryWeeks.

### PersonRole

Enum: Player, Manager, AssistantManager, Coach, Scout, Physio

### Position

Enum (13 значений): GK, CB, LB, RB, WB, DM, CM, LM, RM, AM, LW, RW, ST

### Contract

Value object: clubId, wageWeekly, signedDate, expiryDate + isExpired(date).

### Club

Агрегат: id, name, shortName, balance, wageBudget, players, staff, competitionIds.

### Competition

Sealed interface. Две реализации: League (таблица) и Cup (сетка раундов).

### Fixture

Запись в календаре: id, competitionId, date, homeClubId, awayClubId, played, homeGoals, awayGoals.

### Standing

Строка таблицы: clubId, played, wins, draws, losses, goalsFor, goalsAgainst, form.

### Formation

Схема: name + список позиций (11 слотов). Дефолт — 4-4-2.

## Связи

Club → Person[] (players + staff)
Club ↔ Competition[] (участие через competitionIds)
Competition → Fixture[] (календарь)
Competition → Standing[] (только League)
Person → Contract (0..1)
Fixture → MatchResult (computed)
