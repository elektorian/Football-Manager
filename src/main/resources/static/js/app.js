var tabs = ['profile', 'inbox', 'squad', 'tactics', 'tournaments', 'schedule', 'club', 'tournament']
var navHistory = []
var ignoreNextHashChange = false
var roundsData = null
var currentRoundIndex = 0
var notificationsData = []

function navigate() {
  var tab = location.hash.replace(/^#/, '') || 'profile'

  if (!ignoreNextHashChange) {
    if (navHistory.length === 0 || navHistory[navHistory.length - 1] !== tab) {
      navHistory.push(tab)
    }
  }
  ignoreNextHashChange = false
  updateBackButton()

  document.querySelectorAll('.page-content').forEach(function(el) {
    el.style.display = 'none'
  })
  var page = document.getElementById('page-' + tab)
  if (page) page.style.display = 'block'

  if (tab === 'tournament') {
    document.querySelector('[data-tab="tournaments"]').classList.add('active')
  } else {
    document.querySelectorAll('.nav-item').forEach(function(el) {
      el.classList.toggle('active', el.getAttribute('data-tab') === tab)
    })
  }

  if (tab === 'profile') fetchProfile()
  if (tab === 'tournaments') {
    console.log('navigate: tournaments tab, calling fetchTournamentSections')
    fetchTournamentSections()
  }
  if (tab === 'tournament') {
    updatePageTitle('')
    fetchTournamentPage()
  }
  if (tab === 'inbox') fetchNotifications()
}

// ─── Входящие / уведомления ─────────────────────────
function fetchNotifications() {
  var listEl = document.getElementById('inboxList')
  if (!listEl) return
  listEl.innerHTML = '<p style="color:var(--text-secondary);padding:12px;">⏳ Загрузка...</p>'

  fetch('/notifications')
    .then(function(r) { return r.json() })
    .then(function(list) {
      notificationsData = list
      renderInboxList()
      if (list.length > 0) {
        var target = list.filter(function(n) { return !n.checked })
          .sort(function(a, b) { return new Date(b.timestamp) - new Date(a.timestamp) })[0]
        if (!target) target = list.sort(function(a, b) { return new Date(b.timestamp) - new Date(a.timestamp) })[0]
        selectNotification(target.id, false)
      }
    })
    .catch(function() {})
}

function renderInboxList() {
  var list = document.getElementById('inboxList')
  var html = ''
  for (var i = notificationsData.length - 1; i >= 0; i--) {
    var n = notificationsData[i]
    var d = new Date(n.date).toLocaleDateString('ru-RU')
    html += '<div class="inbox-item' + (n.checked ? ' read' : '') + '" data-id="' + n.id + '" onclick="onInboxItemClick(\'' + n.id + '\')">'
      + '<div class="inbox-item-title">' + n.title + '</div>'
      + '<div class="inbox-item-date">' + d + '</div>'
      + '</div>'
  }
  list.innerHTML = html || '<p class="inbox-empty">Нет уведомлений</p>'
}

function selectNotification(id, fromClick) {
  document.querySelectorAll('.inbox-item').forEach(function(el) {
    el.classList.toggle('active', el.getAttribute('data-id') === id)
  })

  if (fromClick) {
    fetch('/notifications/' + id, { method: 'POST' })
      .then(function(r) { return r.json() })
      .then(function(n) {
        renderNotificationContent(n)
      })
      .catch(function() {})
    var idx = notificationsData.findIndex(function(n) { return n.id === id })
    if (idx !== -1) notificationsData[idx].checked = true
    renderInboxList()
  } else {
    var n = notificationsData.find(function(n) { return n.id === id })
    if (n) renderNotificationContent(n)
  }
}

function renderNotificationContent(n) {
  var el = document.getElementById('inboxContent')
  if (n.type === 'MATCH_PREVIEW') {
    el.innerHTML = '<div style="white-space:normal;">' + renderMatchPreview(n.payload) + '</div>'
  } else {
    el.innerHTML = '<div style="white-space:pre-wrap;">' + JSON.stringify(n.payload) + '</div>'
  }
}

function renderMatchPreview(payload) {
  var html = '<div class="round-matches">'
  payload.matches.forEach(function(m) {
    html += '<div class="round-match">'
      + '<div class="round-match-teams">'
      + '<span class="round-match-team">(' + m.homePosition + ') ' + m.homeTeam + '</span>'
      + '<span class="round-match-score unplayed">-</span>'
      + '<span class="round-match-team away">' + m.awayTeam + ' (' + m.awayPosition + ')</span>'
      + '</div>'
      + '</div>'
  })
  html += '</div>'
  return html
}

function onInboxItemClick(id) {
  selectNotification(id, true)
}

function parseLocalDateTime(arr) {
  if (!arr) return ''
  if (typeof arr === 'string') return new Date(arr).toLocaleDateString('ru-RU')
  if (arr.length < 3) return ''
  var d = new Date(arr[0], arr[1] - 1, arr[2], arr[3] || 0, arr[4] || 0)
  return d.toLocaleDateString('ru-RU')
}

function goBack() {
  if (navHistory.length < 2) return
  navHistory.pop()
  var prev = navHistory[navHistory.length - 1]
  ignoreNextHashChange = true
  location.hash = '#' + prev
}

function updateBackButton() {
  var btn = document.getElementById('backBtn')
  if (btn) btn.disabled = navHistory.length < 2
}

function toggleCalendar() {
  document.getElementById('calendarBody').classList.toggle('show')
}

function toggleMenu() {
  document.getElementById('menuBody').classList.toggle('show')
}

function updatePageTitle(text) {
  document.getElementById('pageTitle').textContent = text || ''
}

function fetchProfile() {
  fetch('/profile/coach')
    .then(function(r) { return r.json() })
    .then(function(c) {
      document.getElementById('profileFirstName').textContent = c.firstName
      document.getElementById('profileLastName').textContent = c.lastName
      document.getElementById('profileBirthDate').textContent = c.birthDate
    })
    .catch(function() {})
}

// ─── Страница "Турниры" (#tournaments) ─────────────────────────
// Рендерит секции для каждого турнира с компактной таблицей
function fetchTournamentSections() {
  var container = document.getElementById('tournamentSections')
  if (!container) {
    console.error('tournamentSections element not found')
    return
  }
  console.log('fetchTournamentSections: fetching /profile/league')
  container.innerHTML = '<p>⏳ Загрузка...</p>'

  fetch('/profile/league')
    .then(function(r) {
      console.log('fetchTournamentSections: response status', r.status)
      if (!r.ok) throw new Error('HTTP ' + r.status)
      return r.json()
    })
    .then(function(data) {
      console.log('fetchTournamentSections: data received', data)
      if (!data || !data.table || data.table.length === 0) {
        container.innerHTML = '<p>Нет данных</p>'
        return
      }
      renderTournamentSection(container, data.leagueName, data.table)
    })
    .catch(function(err) {
      console.error('fetchTournamentSections: error', err)
      container.innerHTML = '<p>Нет данных (' + err.message + ')</p>'
    })
}

function renderTournamentSection(container, leagueName, rows) {
  var html = '<div class="tournament-section">'
    + '<div class="tournament-section-header">'
    + '<span class="tournament-section-title">' + leagueName + '</span>'
    + '<a href="#tournament" class="tournament-section-link">Перейти к турниру →</a>'
    + '</div>'
    + '<div class="tournament-section-body">'
    + '<table class="compact-table"><thead><tr>'
    + '<th>#</th><th>Клуб</th><th>В</th><th>Н</th><th>П</th><th>О</th>'
    + '</tr></thead><tbody>'

  rows.forEach(function(r) {
    html += '<tr>'
      + '<td>' + r.position + '</td>'
      + '<td class="td-club">' + r.name + '</td>'
      + '<td>' + r.victories + '</td>'
      + '<td>' + r.draws + '</td>'
      + '<td>' + r.losses + '</td>'
      + '<td class="td-pts">' + r.points + '</td>'
      + '</tr>'
  })

  html += '</tbody></table></div></div>'
  container.innerHTML = html
}

// ─── Страница турнира (#tournament) ────────────────────────────
// Показывает полную таблицу с вкладками
function fetchTournamentPage() {
  var container = document.getElementById('tournamentTableFull')
  if (!container) {
    console.error('tournamentTableFull element not found')
    return
  }
  container.innerHTML = '<p>⏳ Загрузка...</p>'

  fetch('/profile/league')
    .then(function(r) {
      if (!r.ok) throw new Error('HTTP ' + r.status)
      return r.json()
    })
    .then(function(data) {
      if (!data || !data.table || data.table.length === 0) {
        container.innerHTML = '<p>Нет данных</p>'
        return
      }
      updatePageTitle(data.leagueName)
      roundsData = data.rounds || null
      currentRoundIndex = 0
      renderFullTable(container, data.table)
    })
    .catch(function(err) {
      console.error('fetchTournamentPage: error', err)
      container.innerHTML = '<p>Нет данных</p>'
    })
}

function renderFullTable(container, rows) {
  var html = '<table class="league-table"><thead><tr>'
    + '<th>#</th><th>Клуб</th><th>И</th><th>В</th><th>Н</th><th>П</th>'
    + '<th>ЗМ</th><th>ПМ</th><th>РМ</th><th>О</th>'
    + '</tr></thead><tbody>'

  rows.forEach(function(r) {
    var gd = r.goalsScored - r.goalsConceded
    html += '<tr>'
      + '<td>' + r.position + '</td>'
      + '<td class="td-club">' + r.name + '</td>'
      + '<td>' + (r.victories + r.draws + r.losses) + '</td>'
      + '<td>' + r.victories + '</td>'
      + '<td>' + r.draws + '</td>'
      + '<td>' + r.losses + '</td>'
      + '<td>' + r.goalsScored + '</td>'
      + '<td>' + r.goalsConceded + '</td>'
      + '<td>' + (gd > 0 ? '+' : '') + gd + '</td>'
      + '<td class="td-pts">' + r.points + '</td>'
      + '</tr>'
  })

  html += '</tbody></table>'
  container.innerHTML = html
}

function switchTournamentTab(tab) {
  document.querySelectorAll('#page-tournament .tab').forEach(function(t) {
    t.classList.toggle('active', t.getAttribute('data-tab') === tab)
  })
  document.getElementById('tournamentTableFull').style.display = tab === 'table' ? 'block' : 'none'
  document.getElementById('tournamentRounds').style.display = tab === 'rounds' ? 'block' : 'none'
  if (tab === 'rounds') renderCurrentRound()
}

// ─── Туры ───────────────────────────────────────────
function renderCurrentRound() {
  if (!roundsData || roundsData.length === 0) {
    document.getElementById('roundMatchesContainer').innerHTML = '<p>Нет данных о турах</p>'
    return
  }
  var round = roundsData[currentRoundIndex]
  var total = roundsData.length

  document.getElementById('roundNumberInput').value = round.number
  document.getElementById('roundTotal').textContent = 'из ' + total
  document.getElementById('prevRoundBtn').disabled = currentRoundIndex === 0
  document.getElementById('nextRoundBtn').disabled = currentRoundIndex >= total - 1

  var html = '<div class="round-matches">'
  round.matches.forEach(function(m) {
    var dateStr = new Date(m.date).toLocaleDateString('ru-RU')
    var played = m.homeTeamScore !== '-'
    html += '<div class="round-match">'
      + '<span class="round-match-date">' + dateStr + '</span>'
      + '<div class="round-match-teams">'
      + '<span class="round-match-team">' + m.homeTeamName + '</span>'
      + '<span class="round-match-score' + (played ? '' : ' unplayed') + '">'
      + (played ? m.homeTeamScore + ' - ' + m.awayTeamScore : '-')
      + '</span>'
      + '<span class="round-match-team away">' + m.awayTeamName + '</span>'
      + '</div>'
      + '</div>'
  })
  html += '</div>'
  document.getElementById('roundMatchesContainer').innerHTML = html
}

function prevRound() {
  if (currentRoundIndex > 0) {
    currentRoundIndex--
    renderCurrentRound()
  }
}

function nextRound() {
  if (roundsData && currentRoundIndex < roundsData.length - 1) {
    currentRoundIndex++
    renderCurrentRound()
  }
}

function jumpToRound(num) {
  if (!roundsData) return
  var idx = parseInt(num, 10) - 1
  if (idx >= 0 && idx < roundsData.length) {
    currentRoundIndex = idx
    renderCurrentRound()
  }
}

function renderCalendarGrid(timestamp) {
  var y = +timestamp.substring(0, 4)
  var m = +timestamp.substring(5, 7)
  var currentDay = +timestamp.substring(8, 10)

  var firstJS = new Date(y, m - 1, 1).getDay()
  var offset = (firstJS + 6) % 7
  var daysInMonth = new Date(y, m, 0).getDate()

  var grid = document.getElementById('calendarDays')
  grid.innerHTML = ''

  var names = ['Пн','Вт','Ср','Чт','Пт','Сб','Вс']
  names.forEach(function(n) {
    var s = document.createElement('span')
    s.className = 'day-name'
    s.textContent = n
    grid.appendChild(s)
  })

  for (var i = 0; i < offset; i++) {
    var s = document.createElement('span')
    s.className = 'day empty'
    grid.appendChild(s)
  }

  for (var d = 1; d <= daysInMonth; d++) {
    var s = document.createElement('span')
    s.className = 'day' + (d === currentDay ? ' today' : '')
    s.textContent = d
    grid.appendChild(s)
  }
}

function parseMoment(str) {
  var d = new Date(str)
  return {
    date: d.toLocaleDateString('ru-RU'),
    month: d.toLocaleString('ru-RU', { month: 'long', year: 'numeric' }),
  }
}

function updateDate(timestamp) {
  var d = new Date(timestamp)
  document.getElementById('currentDate').textContent = d.toLocaleDateString('ru-RU')
  document.getElementById('currentMonth').textContent = d.toLocaleString('ru-RU', { month: 'long', year: 'numeric' })
  renderCalendarGrid(timestamp)
}

function advanceDay() {
  fetch('/calendar/advance', { method: 'POST' })
    .then(function(r) { return r.json() })
    .then(function(data) {
      updateDate(data.currentMoment)
      if (data.anyUnreadNotifications) {
        if (location.hash === '#inbox') fetchNotifications()
        else location.hash = '#inbox'
      }
    })
    .catch(function() { alert('Ошибка при продвижении дня') })
}

document.addEventListener('DOMContentLoaded', function() {
  navigate()
  updateDate('2020-07-01T08:00')
})

window.addEventListener('hashchange', navigate)

document.addEventListener('click', function(e) {
  if (!e.target.closest('.js-calendar')) {
    var cb = document.getElementById('calendarBody')
    if (cb) cb.classList.remove('show')
  }
  if (!e.target.closest('.js-menu')) {
    var mb = document.getElementById('menuBody')
    if (mb) mb.classList.remove('show')
  }
})
