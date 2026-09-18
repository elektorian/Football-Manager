# Game Calendar / Date-Time Feature

## 1. Обзор

Реализован функционал игрового календаря и слайдера времени: виджет с текущей датой в topbar и кнопка «advance» для перехода к следующему временному слоту.

## 2. Слои и файлы

### 2.1 Domain Layer

#### TimeSlot (`domain/calendar/TimeSlot.kt`)
```kotlin
enum class TimeSlot(val time: LocalTime) {
    Start(LocalTime.of(8, 0)),
    PreMatch(LocalTime.of(15, 0)),
    PostMatch(LocalTime.of(18, 0)),
    EndDay(LocalTime.of(23, 0));

    fun next(): TimeSlot {
        return entries[(ordinal + 1) % entries.size]
    }
}
```
Слоты дня: 08:00 → 15:00 → 18:00 → 23:00 → (цикл).

#### CalendarProvider (`domain/calendar/CalendarProvider.kt`)
```kotlin
interface CalendarProvider {
    fun now(): LocalDateTime
    fun advanceOneSlot(): LocalDateTime
}
```
Абстракция календаря для domain-слоя.

### 2.2 Infrastructure Layer

#### SystemCalendarProvider (`infrastructure/calendar/SystemCalendarProvider.kt`)
```kotlin
@Service
class SystemCalendarProvider : CalendarProvider {

    private var current: LocalDateTime = LocalDateTime.of(2019, 7, 8, 8, 0)

    override fun now(): LocalDateTime = current

    override fun advanceOneSlot(): LocalDateTime {
        val day = current.toLocalDate()
        val currentTime = current.toLocalTime()

        val slot = TimeSlot.entries.find { it.time == currentTime }
            ?: TimeSlot.Start

        val nextSlot = slot.next()

        if (nextSlot == TimeSlot.Start) {
            current = LocalDateTime.of(day.plusDays(1), TimeSlot.Start.time)
        } else {
            current = LocalDateTime.of(day, nextSlot.time)
        }

        return current
    }
}
```
Конкретная реализация. Стартовый момент: 08.07.2019 08:00. При переходе от EndDay → Start день инкрементируется.

### 2.3 Application Layer

#### CalendarService (`application/calendar/CalendarService.kt`)
```kotlin
@Service
class CalendarService(
    private val calendarProvider: CalendarProvider
) {
    fun now(): LocalDateTime = calendarProvider.now()
    fun advanceOneSlot(): LocalDateTime = calendarProvider.advanceOneSlot()
}
```
Фасад над CalendarProvider.

### 2.4 Controller Layer

#### CalendarController (`controller/CalendarController.kt`)
```kotlin
@Controller
class CalendarController(private val calendarService: CalendarService) {

    @PostMapping("/game/advance")
    fun advance(): String {
        calendarService.advanceOneSlot()
        return "redirect:/"
    }
}
```
POST /game/advance — ключевой endpoint для сдвига времени. После сдвига редирект на `/`.

#### MainController (`controller/MainController.kt`)
Каждая `@GetMapping` функция теперь вызывает `calendarService.now()` и добавляет `"currentDateTime"` в модель. Ранее — `@ModelAttribute`.

### 2.5 Template Layer (`templates/fragments/topbar.html`)

#### Widget даты
```html
<div class="datetime-widget">
    <span class="datetime-date" th:text="${#temporals.format(currentDateTime, 'dd.MM.yyyy')}">08.07.2019</span>
</div>
```
Документ: `dd.MM.yyyy`, в topbar, 18px.

#### Кнопка Advance
```html
<form th:action="@{/game/advance}" method="post" style="display:inline;">
    <button type="submit" class="btn">advance</button>
</form>
```
POST-форма (не `<a>`), без CSRF-токена (Spring Security в проекте отсутствует).

## 3. Изменения

| Файл | Изменено |
|------|----------|
| `domain/calendar/TimeSlot.kt` | Новый enum — slotes дня |
| `domain/calendar/CalendarProvider.kt` | Новый interface — абстракция календаря |
| `infrastructure/calendar/SystemCalendarProvider.kt` | Новая реализация календаря |
| `application/calendar/CalendarService.kt` | Новый service-фасад |
| `controller/CalendarController.kt` | Новый контроллер: POST /game/advance |
| `controller/MainController.kt` | Добавлен `calendarService.now()` + `"currentDateTime"` в модель |
| `templates/fragments/topbar.html` | Добавлен widget даты и POST-forma с кнопкой advance |

## 4. Обратная совместимость

Все существующие страницы сохранены. Изменены только:
- `MainController.kt` — добавлен `currentDateTime` в модель
- `topbar.html` — добавлен date-widger и `advance`-кнопка
