import { router } from './router.js'
import * as views from './views.js'

document.addEventListener('DOMContentLoaded', () => {
  router.init()
  views.updateDate('2020-07-01T08:00')

  // Back button
  document.getElementById('backBtn').addEventListener('click', () => router.goBack())

  // Dropdown toggles
  document.querySelector('.js-menu > button').addEventListener('click', e => {
    e.stopPropagation()
    document.getElementById('menuBody').classList.toggle('show')
  })
  document.querySelector('.js-calendar').addEventListener('click', e => {
    e.stopPropagation()
    document.getElementById('calendarBody').classList.toggle('show')
  })

  // Advance day
  document.querySelector('.topbar-btn.primary').addEventListener('click', () => views.advanceDay())

  // Inbox: item click delegation
  document.getElementById('inboxList').addEventListener('click', e => {
    const item = e.target.closest('.inbox-item')
    if (item) views.onInboxItemClick(item.dataset.id)
  })

  // Tournament tab clicks
  document.querySelectorAll('#page-tournament .tab').forEach(el => {
    el.addEventListener('click', () => views.switchTournamentTab(el.dataset.tab))
  })

  // Round navigation
  document.getElementById('prevRoundBtn').addEventListener('click', () => views.prevRound())
  document.getElementById('nextRoundBtn').addEventListener('click', () => views.nextRound())
  document.getElementById('roundNumberInput').addEventListener('change', e => views.jumpToRound(e.target.value))

  // Club tab clicks
  document.querySelectorAll('#page-club .tab').forEach(el => {
    el.addEventListener('click', () => views.switchClubTab(el.dataset.tab))
  })

  // Close popups on outside click
  document.addEventListener('click', e => {
    if (!e.target.closest('.js-calendar, .calendar-popup')) {
      document.getElementById('calendarBody').classList.remove('show')
    }
    if (!e.target.closest('.js-menu, .dropdown-menu')) {
      document.getElementById('menuBody').classList.remove('show')
    }
  })
})
