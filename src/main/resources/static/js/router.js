import { state } from './state.js'
import {
  renderProfile,
  renderInbox,
  renderTournamentSections,
  renderTournamentPage,
  renderTournamentDetailPage,
  renderSchedule,
  renderClub,
  renderPlayerPage,
} from './views.js'

const actions = {
  profile: () => renderProfile(),
  inbox: () => renderInbox(),
  tournaments: () => renderTournamentSections(),
  tournament: () => renderTournamentPage(),
  'tournament-detail': (id) => renderTournamentDetailPage(id),
  schedule: () => renderSchedule(),
  club: (id) => renderClub(id),
  player: (id) => renderPlayerPage(id),
}

export const router = {
  init() {
    this.resolve()
    window.addEventListener('hashchange', () => this.resolve())
  },

  resolve() {
    const hash = location.hash.replace(/^#/, '') || 'profile'
    const parts = hash.split('/')
    let tab = parts[0]
    const entityId = parts[1]

    if (!state.nav.ignoreNext) {
      const prev = state.nav.history.at(-1)
      if (prev !== hash) state.nav.history.push(hash)
    }
    state.nav.ignoreNext = false
    this.backBtn()

    if (tab === 'team') tab = 'club'
    const isTournamentDetail = tab === 'tournament' && entityId
    const resolvedTab = isTournamentDetail ? 'tournament-detail' : tab

    document.querySelectorAll('.page-content').forEach(el => { el.style.display = 'none' })
    const pageEl = document.getElementById(`page-${resolvedTab}`)
    if (pageEl) pageEl.style.display = 'block'

    document.querySelectorAll('.nav-item').forEach(el => {
      el.classList.toggle('active', isTournamentDetail ? el.dataset.tab === 'tournaments' : el.dataset.tab === tab)
    })

    const fn = actions[resolvedTab]
    if (fn) fn(entityId)
  },

  goBack() {
    if (state.nav.history.length < 2) return
    state.nav.history.pop()
    state.nav.ignoreNext = true
    location.hash = `#${state.nav.history.at(-1)}`
  },

  backBtn() {
    const btn = document.getElementById('backBtn')
    if (btn) btn.disabled = state.nav.history.length < 2
  },
}
