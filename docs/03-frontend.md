# Фронтенд

## Стек

Vanilla JS SPA. Без фреймворков. Всё встроено в один JAR.

## Структура файлов

```
src/main/resources/static/
├── index.html              — корневой layout
├── css/
│   └── style.css           — тёмная тема, layout, карточки
└── js/
    ├── api.js              — fetch-обёртки для /api/engine/*
    ├── state.js            — AppState: игровое состояние, навигация
    ├── app.js              — рендер страницы по activeTab
    ├── components/
    │   ├── header.js       — топ-бар: лого, клуб, дата, Continue
    │   └── sidebar.js      — левая навигация (10 вкладок)
    └── pages/
        ├── home.js         — домашняя страница (контент)
        └── placeholder.js  — заглушки для нереализованных вкладок
```

## Layout страницы

```
┌──────────────────────────────────────────────┐
│ FM  Manchester United  2024-07-01  [Continue] │
├──────┬───────────────────────────────────────┤
│sidebar│  content                              │
│ 76px  │  (страница activeTab)                 │
└──────┴───────────────────────────────────────┘
```

## Вкладки сайдбара

Порядок (сверху вниз):

| # | Вкладка | Иконка | Статус |
|---|---------|--------|--------|
| 1 | Home | 🏠 | Реализована |
| 2 | Squad | 👥 | Placeholder |
| 3 | Tactics | 📋 | Placeholder |
| 4 | Training | 🏋️ | Placeholder |
| 5 | Staff | 👔 | Placeholder |
| 6 | Fixtures | 📅 | Placeholder |
| 7 | Competitions | 🏆 | Placeholder |
| 8 | Scouting | 🔍 | Placeholder |
| 9 | Transfers | 💰 | Placeholder |
| 10 | Club | 🏟️ | Placeholder |

## Home — домашняя страница

Три колонки сверху:

```
┌─────────────┬─────────────┬─────────────┐
│ Next Match  │ League Table│ Recent Form │
│ Arsenal (A) │ Premier Lg  │ W D W L D   │
│ 28 Jul 2024 │ 3rd / 20    │ (цветные)   │
│ 15:00       │ Pts: 0      │             │
│ Premier Lg  │ GD: 0       │             │
└─────────────┴─────────────┴─────────────┘
```

Под сеткой — список ближайших матчей. Под ним — инбокс (пока пустой).

## AppState

- `activeTab` — текущая вкладка (home по умолчанию)
- `clubName` — название клуба (Manchester United)
- `nextMatch`, `leagueTable`, `form`, `fixtures` — данные для Home
- `setTab(tab)` — переключение вкладки с ререндером

## API

| Метод | Путь | Ответ |
|-------|------|-------|
| GET | `/api/engine/state` | `{"date": "2024-07-01"}` |
| POST | `/api/engine/advance?days=N` | `{"date": "2024-07-02", "events": [...]}` |
