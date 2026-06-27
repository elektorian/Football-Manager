import { api } from './api.js'
import { state } from './state.js'

// ─── Helpers ───────────────────────────────────────────

export function entityLink(type, id, label) {
  return `<a href="#${type}/${id}" class="entity-link">${label}</a>`
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('ru-RU')
}

function matchScore(m) {
  const played = m.homeTeamScore !== '-'
  return played ? `${m.homeTeamScore} - ${m.awayTeamScore}` : '-'
}

function formatSalary(val) {
  const num = Number(val)
  if (isNaN(num)) return '—'
  return `€${Math.round(num).toLocaleString('ru-RU')}/нед`
}

function calculateAge(birthDate, fromDate) {
  if (!fromDate) return '—'
  const birth = new Date(birthDate)
  const now = new Date(fromDate)
  let age = now.getFullYear() - birth.getFullYear()
  const m = now.getMonth() - birth.getMonth()
  if (m < 0 || (m === 0 && now.getDate() < birth.getDate())) age--
  return age
}

// ─── Profile ────────────────────────────────────────────

export async function renderProfile() {
  try {
    const c = await api.getProfile()
    document.getElementById('profileFirstName').textContent = c.firstName
    document.getElementById('profileLastName').textContent = c.lastName
    document.getElementById('profileBirthDate').textContent = c.birthDate
  } catch {
    // silent
  }
}

// ─── Inbox / Notifications ───────────────────────────────

export async function renderInbox() {
  const listEl = document.getElementById('inboxList')
  listEl.innerHTML = '<p class="placeholder-text">⏳ Загрузка...</p>'

  try {
    const list = await api.getNotifications()
    state.notificationsData = list
    renderInboxList()

    if (list.length > 0) {
      const sorted = [...list].sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
      const target = sorted.find(n => !n.checked) || sorted[0]
      selectNotification(target.id, false)
    }
  } catch {
    listEl.innerHTML = '<p class="inbox-empty">Входящие пусты</p>'
  }
}

function renderInboxList() {
  const list = document.getElementById('inboxList')
  const items = state.notificationsData
  let html = ''
  for (let i = items.length - 1; i >= 0; i--) {
    const n = items[i]
    html += `<div class="inbox-item${n.checked ? ' read' : ''}" data-id="${n.id}">
      <div class="inbox-item-title">${n.title}</div>
      <div class="inbox-item-date">${formatDate(n.date)}</div>
    </div>`
  }
  list.innerHTML = html || '<p class="inbox-empty">Нет уведомлений</p>'
}

function selectNotification(id, markRead) {
  document.querySelectorAll('.inbox-item').forEach(el => {
    el.classList.toggle('active', el.dataset.id === id)
  })

  if (markRead) {
    api.markNotificationRead(id).then(renderNotificationContent).catch(() => {})
    const idx = state.notificationsData.findIndex(n => n.id === id)
    if (idx !== -1) state.notificationsData[idx].checked = true
    renderInboxList()
  } else {
    const n = state.notificationsData.find(n => n.id === id)
    if (n) renderNotificationContent(n)
  }
}

function renderNotificationContent(n) {
  const el = document.getElementById('inboxContent')
  if (n.type === 'MATCH_PREVIEW') {
    el.innerHTML = `<div class="notification-body notification-body--match">${renderMatchPreview(n.payload)}</div>`
  } else {
    el.innerHTML = `<div class="notification-body">${JSON.stringify(n.payload)}</div>`
  }
}

function renderMatchPreview(payload) {
  const items = payload.matches.map(m => `
    <div class="round-match">
      <div class="round-match-teams">
        <span class="round-match-team">(${m.homePosition}) ${entityLink('team', m.homeTeamId, m.homeTeam)}</span>
        <span class="round-match-score unplayed">-</span>
        <span class="round-match-team away">${entityLink('team', m.awayTeamId, m.awayTeam)} (${m.awayPosition})</span>
      </div>
    </div>
  `)
  return `<div class="round-matches">${items.join('')}</div>`
}

export function onInboxItemClick(id) {
  selectNotification(id, true)
}

// ─── Calendar ───────────────────────────────────────────

export function updateDate(timestamp) {
  state.currentMoment = timestamp
  const d = new Date(timestamp)
  document.getElementById('currentDate').textContent = d.toLocaleDateString('ru-RU')
  document.getElementById('currentMonth').textContent = d.toLocaleString('ru-RU', { month: 'long', year: 'numeric' })
  renderCalendarGrid(timestamp)
}

export function parseMoment(str) {
  const d = new Date(str)
  return {
    date: d.toLocaleDateString('ru-RU'),
    month: d.toLocaleString('ru-RU', { month: 'long', year: 'numeric' }),
  }
}

function renderCalendarGrid(timestamp) {
  const y = +timestamp.substring(0, 4)
  const m = +timestamp.substring(5, 7)
  const currentDay = +timestamp.substring(8, 10)

  const firstJS = new Date(y, m - 1, 1).getDay()
  const offset = (firstJS + 6) % 7
  const daysInMonth = new Date(y, m, 0).getDate()

  const grid = document.getElementById('calendarDays')
  grid.innerHTML = ''

  const dayNames = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс']
  dayNames.forEach(n => {
    const s = document.createElement('span')
    s.className = 'day-name'
    s.textContent = n
    grid.appendChild(s)
  })

  for (let i = 0; i < offset; i++) {
    const s = document.createElement('span')
    s.className = 'day empty'
    grid.appendChild(s)
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const s = document.createElement('span')
    s.className = `day${d === currentDay ? ' today' : ''}`
    s.textContent = d
    grid.appendChild(s)
  }
}

// ─── Tournaments (sections + detail) ─────────────────────

export async function renderTournamentSections() {
  const container = document.getElementById('tournamentSections')
  container.innerHTML = '<p class="placeholder-text">⏳ Загрузка...</p>'

  try {
    const data = await api.getLeagueData()
    if (!data?.table?.length) {
      container.innerHTML = '<p class="placeholder-text">Нет данных</p>'
      return
    }
    renderSection(container, data)
  } catch (err) {
    container.innerHTML = `<p class="placeholder-text">Нет данных (${err.message})</p>`
  }
}

function renderSection(container, data) {
  const rows = data.table.map(r => `
    <tr>
      <td>${r.position}</td>
      <td class="td-club">${entityLink('team', r.teamId, r.name)}</td>
      <td>${r.victories}</td>
      <td>${r.draws}</td>
      <td>${r.losses}</td>
      <td class="td-pts">${r.points}</td>
    </tr>
  `).join('')

  container.innerHTML = `
    <div class="tournament-section">
      <div class="tournament-section-header">
        <span class="tournament-section-title">${data.leagueName}</span>
        <a href="#tournament" class="tournament-section-link">Перейти к турниру →</a>
      </div>
      <div class="tournament-section-body">
        <table class="compact-table">
          <thead><tr><th>#</th><th>Клуб</th><th>В</th><th>Н</th><th>П</th><th>О</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
    </div>
  `
}

export async function renderTournamentPage() {
  const container = document.getElementById('tournamentTableFull')
  container.innerHTML = '<p class="placeholder-text">⏳ Загрузка...</p>'

  try {
    const data = await api.getLeagueData()
    if (!data?.table?.length) {
      container.innerHTML = '<p class="placeholder-text">Нет данных</p>'
      return
    }
    updatePageTitle(data.leagueName)
    state.roundsData = data.rounds || null
    state.currentRoundIndex = 0
    renderFullTable(container, data.table)
    switchTournamentTab('table')
  } catch {
    container.innerHTML = '<p class="placeholder-text">Нет данных</p>'
  }
}

function renderFullTable(container, rows) {
  const body = rows.map(r => {
    const gd = r.goalsScored - r.goalsConceded
    return `
      <tr>
        <td>${r.position}</td>
        <td class="td-club">${entityLink('team', r.teamId, r.name)}</td>
        <td>${r.victories + r.draws + r.losses}</td>
        <td>${r.victories}</td>
        <td>${r.draws}</td>
        <td>${r.losses}</td>
        <td>${r.goalsScored}</td>
        <td>${r.goalsConceded}</td>
        <td>${gd > 0 ? '+' : ''}${gd}</td>
        <td class="td-pts">${r.points}</td>
      </tr>
    `
  }).join('')

  container.innerHTML = `
    <table class="league-table">
      <thead><tr><th>#</th><th>Клуб</th><th>И</th><th>В</th><th>Н</th><th>П</th><th>ЗМ</th><th>ПМ</th><th>РМ</th><th>О</th></tr></thead>
      <tbody>${body}</tbody>
    </table>
  `
}

export function switchTournamentTab(tab) {
  document.querySelectorAll('#page-tournament .tab').forEach(t => {
    t.classList.toggle('active', t.dataset.tab === tab)
  })
  document.getElementById('tournamentTableFull').style.display = tab === 'table' ? 'block' : 'none'
  document.getElementById('tournamentRounds').style.display = tab === 'rounds' ? 'block' : 'none'
  if (tab === 'rounds') renderCurrentRound()
}

// ─── Rounds ──────────────────────────────────────────────

export function renderCurrentRound() {
  if (!state.roundsData?.length) {
    document.getElementById('roundMatchesContainer').innerHTML = '<p class="placeholder-text">Нет данных о турах</p>'
    return
  }

  const round = state.roundsData[state.currentRoundIndex]
  const total = state.roundsData.length

  document.getElementById('roundNumberInput').value = round.number
  document.getElementById('roundTotal').textContent = `из ${total}`
  document.getElementById('prevRoundBtn').disabled = state.currentRoundIndex === 0
  document.getElementById('nextRoundBtn').disabled = state.currentRoundIndex >= total - 1

  const matches = round.matches.map(m => {
    const played = m.homeTeamScore !== '-'
    return `
      <div class="round-match">
        <span class="round-match-date">${formatDate(m.date)}</span>
        <div class="round-match-teams">
          <span class="round-match-team">${entityLink('team', m.homeTeamId, m.homeTeamName)}</span>
          <span class="round-match-score${played ? '' : ' unplayed'}">${played ? `${m.homeTeamScore} - ${m.awayTeamScore}` : '-'}</span>
          <span class="round-match-team away">${entityLink('team', m.awayTeamId, m.awayTeamName)}</span>
        </div>
      </div>
    `
  }).join('')

  document.getElementById('roundMatchesContainer').innerHTML = `<div class="round-matches">${matches}</div>`
}

export function prevRound() {
  if (state.currentRoundIndex > 0) {
    state.currentRoundIndex--
    renderCurrentRound()
  }
}

export function nextRound() {
  if (state.roundsData && state.currentRoundIndex < state.roundsData.length - 1) {
    state.currentRoundIndex++
    renderCurrentRound()
  }
}

export function jumpToRound(num) {
  if (!state.roundsData) return
  const idx = parseInt(num, 10) - 1
  if (idx >= 0 && idx < state.roundsData.length) {
    state.currentRoundIndex = idx
    renderCurrentRound()
  }
}

// ─── Schedule ─────────────────────────────────────────────

function renderScheduleRows(matches, tbody) {
  if (!matches?.length) {
    tbody.innerHTML = '<tr><td colspan="2" class="empty-row">Нет матчей</td></tr>'
    return
  }
  tbody.innerHTML = matches.map(m => `
    <tr>
      <td class="schedule-match">
        <span class="round-match-team">${entityLink('team', m.homeTeamId, m.homeTeamName)}</span>
        <span class="round-match-score${m.homeTeamScore !== '-' ? '' : ' unplayed'}">${matchScore(m)}</span>
        <span class="round-match-team away">${entityLink('team', m.awayTeamId, m.awayTeamName)}</span>
      </td>
      <td class="schedule-date">${formatDate(m.date)}</td>
    </tr>
  `).join('')
}

export async function renderSchedule() {
  const tbody = document.getElementById('scheduleBody')
  try {
    const matches = await api.getSchedule()
    renderScheduleRows(matches, tbody)
  } catch {
    tbody.innerHTML = '<tr><td colspan="2" class="empty-row">Ошибка загрузки</td></tr>'
  }
}

export async function renderClubSchedule() {
  const tbody = document.getElementById('clubScheduleBody')
  try {
    const matches = await api.getTeamSchedule(state.currentClubId)
    renderScheduleRows(matches, tbody)
  } catch {
    tbody.innerHTML = '<tr><td colspan="2" class="empty-row">Ошибка загрузки</td></tr>'
  }
}

// ─── Club ─────────────────────────────────────────────────

export async function renderClub(teamId) {
  try {
    const team = teamId ? await api.getTeam(teamId) : await api.getMyTeam()
    state.currentClubId = team.id
    document.getElementById('clubName').textContent = team.name
    document.getElementById('clubCountry').textContent = team.country
    document.getElementById('clubCity').textContent = team.city
    switchClubTab('info')
  } catch {
    // silent
  }
}

export function switchClubTab(tab) {
  document.querySelectorAll('#page-club .tab').forEach(t => {
    t.classList.toggle('active', t.dataset.tab === tab)
  })
  document.getElementById('clubTabInfo').style.display = tab === 'info' ? 'block' : 'none'
  document.getElementById('clubTabSquad').style.display = tab === 'squad' ? 'block' : 'none'
  document.getElementById('clubTabMatches').style.display = tab === 'matches' ? 'block' : 'none'
  if (tab === 'squad') renderSquad()
  if (tab === 'matches') renderClubSchedule()
}

export async function renderSquad() {
  const listEl = document.getElementById('clubSquadList')
  try {
    const players = await api.getPlayers(state.currentClubId)
    const rows = players.map(p => {
      const name = `${p.lastName} ${p.firstName}`
      const age = calculateAge(p.birthDate, state.currentMoment)
      const salary = p.salary != null ? formatSalary(p.salary) : '—'
      return `
        <tr>
          <td class="td-player">${entityLink('player', p.id, name)}</td>
          <td>${age}</td>
          <td class="td-salary">${salary}</td>
        </tr>
      `
    }).join('')

    listEl.innerHTML = `
      <table class="squad-table">
        <thead><tr><th>Игрок</th><th>Возраст</th><th>Зарплата</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>
    `
  } catch {
    listEl.innerHTML = '<p class="placeholder-text">Ошибка загрузки</p>'
  }
}

// ─── Entity pages (stubs) ─────────────────────────────────

export async function renderPlayerPage(id) {
  try {
    const p = await api.getPlayer(id)
    updatePageTitle(`${p.lastName} ${p.firstName}`)
    document.getElementById('page-player').innerHTML = ''
  } catch {
    updatePageTitle('Игрок')
  }
}

export async function renderTournamentDetailPage(id) {
  try {
    const l = await api.getTournament(id)
    updatePageTitle(l.name)
    document.getElementById('page-tournament-detail').innerHTML = ''
  } catch {
    updatePageTitle('Лига')
  }
}

// ─── Navigation helpers ───────────────────────────────────

export function updatePageTitle(text) {
  document.getElementById('pageTitle').textContent = text || ''
}

// ─── Advance day ──────────────────────────────────────────

export async function advanceDay() {
  try {
    const data = await api.advanceDay()
    updateDate(data.currentMoment)
    if (data.anyUnreadNotifications) {
      if (location.hash === '#inbox') renderInbox()
      else location.hash = '#inbox'
    }
  } catch {
    alert('Ошибка при продвижении дня')
  }
}
