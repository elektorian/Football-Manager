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

document.addEventListener('DOMContentLoaded', function() {
  navigate()
  var calGrid = document.getElementById('calendarDays')
  for (var i = 1; i <= 30; i++) {
    var span = document.createElement('span')
    span.className = 'day' + (i === 15 ? ' today' : '')
    span.textContent = i
    calGrid.appendChild(span)
  }
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
