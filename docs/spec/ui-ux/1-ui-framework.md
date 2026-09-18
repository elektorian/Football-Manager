# UI/UX Framework Specification

> Версия: 1.0 | Обновлено: 2025-09-13

---

## 1. Обзор

Football Manager — Spring Boot приложение с серверным рендерингом (Thymeleaf).
Интерфейс использует **компонентный подход**: общие фрагменты (fragments) + отдельные
шаблоны страниц (pages). Все страницы имеют `Share Nothing` паттерн — `body` каждого шаблон самозавершен.

---

## 2. Структура файлов

```
src/main/resources/
├── static/css/
│   └── style.css                             # Единственный CSS-файл
└── templates/
    ├── fragments/
    │   ├── topbar.html       th:fragment="topbar"
    │   └── nav.html          th:fragment="switch-panel"
    └── pages/
        ├── index.html         "/"
        ├── incoming.html      "/incoming"
        ├── squad.html         "/squad"
        ├── tactic.html        "/tactic"
        ├── tournaments.html   "/tournaments"
        ├── schedule.html      "/schedule"
        └── club.html          "/club"

docs/
├── grids/
│   └── common_grid.jpg         Исходный дизайн-макет (JPG, не редактируется)
└── spec/
    └── ui-ux/
        └── ui-framework.md     Этот файл
```

---

## 3. Шаблоны страниц

Все 7 страниц имеют идентичную базовую структуру:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Football Manager</title>          <!-- Единый заголовок БЕЗ суффиксов -->
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="app-container">
    <div th:replace="~{fragments/topbar :: topbar}"></div>
    <div th:replace="~{fragments/nav :: switch-panel}"></div>
    <main class="main-panel"></main>
</div>
</body>
</html>
```

### Правила для `<main class="main-panel">`

**SHOULD:**
- Оставлять тег пустым — контент добавляется динамически позже
- Не добавлять в него Red-метки или `placeholder` текст

**SHOULDN'T:**
- добавлять в него Red-метки
- оставлять в нём текст типа "Main page", "switch panel", "search inbox" — это аннотации на дизайн-макете `common_grid.jpg`, а не часть UI

---

## 4. Фрагменты (Fragments)

### 4.1 topbar.html — `th:fragment = "topbar"`

| Зона        | Элемент                |
|---------|------------------------|
| ЛЕВАя   | `<input type="text" placeholder="Search...">` |
| ПРАВАя  | `<a class="btn" href="@{/}">menu</a>` + `<a class="btn">advance</a>` |

- placeholder "Search..." (как на дизайн-макете)
- `menu` → `th:href="@{.}"` (home page)
- `advance` → обычный `<a>` для будущей функциональности
- Опечатка исправлена: было `menuu`, стало `menu`

### 4.2 nav.html — `th:fragment = "switch-panel"`

Боковая панель навигации с активным индикатором:

| № | Ссылка    | Путь           |
|---|-----------|----------------|
| 1 | `coming`  | `/incoming`    |
| 2 | `squad`   | `/squad`       |
| 3 | `tactic`  | `/tactic`      |
| 4 | `tournaments` | `/tournaments` |
| 5 | `schedule` | `/schedule`   |
| 6 | `club`    | `/club`        |

Active state: `th:classappend="${activePage == 'squad'} ? 'active'"`

 fragment: `th:fragment="switch-panel"` (панель навигации)
 nav- класс: `th:fragment="switch-panel"` (панель навигации)

 | Layout | Пример           |
|--------|---------------------------|
| `topbar` | Верхняя панель проекта       |
| `switch-panel` | Боковая панель навигации   |

---

## 5. Controller

`MainControler.kt` — это один аутинтервал для всех тритаров:

```kotlin
@Controller
class MainController {
    @GetMapping("/")          fun index(model: Model): String = "pages/index"
    @GetMapping("/incoming")  fun incaming(model: Model): String = "pages/incoming"
    @GetMapping("/squad")     fun squad(model: Model): String = "pages/squad"
    @GetMapping("/tactic")    fun tactic(model: Model): String = "pages/tactic"
    @GetMapping("/tournaments") fun tournaments(model: Model): String = "pages/tournaments"
    @GetMapping("/schedule")  fun schedule(model: Model): String = "pages/schedule"
    @GetMapping("/club")      fun club(model: Model): String = "pages/clubs"
}
```

Каждый метод:
1. ставит `activePage` атрибут в Model
2. возвращает логическое имя шаблона `"pages/<name>"`

---

## 6. CSS / Component API

#### 6.1 Layout

| Класс             | Описание                           |
|-------------------|------------------------------------|
| `.app-container`  | flex column, min-height:100vh      |
| `.main-panel`     | flex:1, padding:20px, overflow-y:auto|

#### 6.2 Topbar

| Класс             | Описание                         |
|-------------------|----------------------------------|
| `.header-bar`     | header, bg:#2c3e50, h:50px      |
| `.header-search`  | flex:1                           |
| `.search-input`   | width:100%, border:none          |
| `.header-actions` | flex, gap:10px                   |
| `.btn`            | padding:6px 12px, bg:#fff        |
| `.btn:hover`      | bg:#e9e9e9                       |

#### 6.3 Sidebar (Switch Panel)

| Класс             | Описание                         |
|-------------------|----------------------------------|
| `.switch-panel`   | width:200px, white, border-right |
| `.nav-list`       | list-style:none                  |
| `.nav-link`       | display:block, padding:10px20px  |
| `.nav-link.active | bg:#f8f9fa, color:#2c3e50        |
| `.nav-link:hover` | bg:#e9e9e9                       |

#### 6.4 Content Areas

| Класс             | Описание                         |
|-------------------|----------------------------------|
| `.content-grid`   | grid auto-fit minmax(300px)      |
| `.content-card`   | bg:#f8f9fa, border-radius:8px    |
| `.content-table`  | width:100%, border-collapse      |

---

## 7. Как начать разработку новой страницы

1. Создать `templates/pages/newpage.html` с базовой структурой (как в секции 3)
2. В `nav.html` добавить новую ссылку:
   ```html
   <li>
     <a th:href="@{/newpage}" class="nav-link"
        th:classappend="${activePage == 'newpage'} ? 'active'">
       newpage-text
     </a>
   </li>
   ```
3. В `MainController.kt` добавить:
   ```kotlin
   @GetMapping("/newpage")
   fun newPage(model: Model): String {
       model.addAttribute("activePage", "newpage")
       return "pages/newpage"
   }
   ```

---

## 8. Изменения (Change Log)

| Что                                | До                                    
|------------------------------------|----------------------------------------|
| `<title>` на страницах             | Разные: "FootballManager - Squad" → Единый: `<title>Football Manager</title>` |
| Метка "main page" в `nav.html` top | Убран полностью       |
| Метка "switch panel" в `nav.hml` bottom | Убран полностью                    |
| Опечатка `menuu` в `topbar.html`   | исправлено на `menu`                |

---

## 9. Design-макет

Источной макет Design: `docs/grids/common_grid.jpg`

Лайаут кнопки:
- Слева : "switch-panel"
- посейidine: "main-panel"
- красный текст - аннотации (не убирались, т.к. это исходник дизайна)
