var tabs = ['inbox', 'squad', 'tactics', 'tournaments', 'schedule', 'club']

function navigate() {
  var tab = location.hash.replace(/^#/, '') || 'inbox'
  document.querySelectorAll('.page-content').forEach(function(el) {
    el.style.display = 'none'
  })
  var page = document.getElementById('page-' + tab)
  if (page) page.style.display = 'block'
  document.querySelectorAll('.nav-item').forEach(function(el) {
    el.classList.toggle('active', el.getAttribute('data-tab') === tab)
  })
}

function toggleCalendar() {
  document.getElementById('calendarBody').classList.toggle('show')
}

function toggleMenu() {
  document.getElementById('menuBody').classList.toggle('show')
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
    .then(function(r) { return r.text() })
    .then(function(str) { updateDate(str) })
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
