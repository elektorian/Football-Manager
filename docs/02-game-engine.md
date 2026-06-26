# Game Engine

## Назначение

Game Engine — ядро, которое управляет временем в игре и оркестрирует все подсистемы. Именно он продвигает состояние мира вперёд, когда пользователь нажимает кнопку Continue или Play.

## Компоненты


### DayProcessor

Файл: `com.footballmanager.engine.DayProcessor`

- `interface DayProcessor`

Методы и свойства:
  - process

### EventCalendar

Файл: `com.footballmanager.engine.EventCalendar`

- `class EventCalendar`

Методы и свойства:
  - schedule
  - eventsFor
  - hasEventsOn
  - clear

### GameClock

Файл: `com.footballmanager.engine.GameClock`

- `data GameClock`

Методы и свойства:
  - advance

### GameEngine

Файл: `com.footballmanager.engine.GameEngine`

- `class GameEngine`

Методы и свойства:
  - advance
  - advanceUntil

### GameEvent

Файл: `com.footballmanager.engine.GameEvent`

- `sealed GameEvent`

### GameWorld

Файл: `com.footballmanager.engine.GameWorld`

- `class GameWorld`

### ScheduledEvent

Файл: `com.footballmanager.engine.ScheduledEvent`

- `sealed ScheduledEvent`

## Как работает один день

Когда наступает новый день, GameClock сдвигается на один день вперёд. Затем EventCalendar отдаёт все события, запланированные на эту дату. После этого каждый DayProcessor по очереди (в порядке приоритета) получает текущую дату, список запланированных событий и GameWorld. Каждый процессор может мутировать GameWorld и возвращать список GameEvent. Все собранные события возвращаются вызывающему коду.

## Порядок процессоров

Сначала обрабатываются матчи — это событие дня с самым высоким приоритетом. Затем восстанавливается кондиция игроков, обновляются травмы, дрейфует мораль, обновляются атрибуты. После этого обрабатываются трансферы и контракты. В конце дня начисляются зарплаты, проверяются границы сезона и принимаются решения AI-клубами.

## Сохранение и загрузка

GameWorld целиком сериализуется и сохраняется в файл. Загрузка — это десериализация сохранённого снапшота.
