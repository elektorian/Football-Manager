async function request(method, path) {
  const res = await fetch(path, { method })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json()
}

export const api = {
  getProfile: () => request('GET', '/profile/coach'),
  getNotifications: () => request('GET', '/notifications'),
  markNotificationRead: (id) => request('POST', `/notifications/${id}`),
  getLeagueData: () => request('GET', '/profile/league'),
  getSchedule: () => request('GET', '/profile/schedule'),
  getTeam: (id) => request('GET', `/teams/${id}`),
  getMyTeam: () => request('GET', '/profile/team'),
  getTeamSchedule: (id) => request('GET', `/teams/${id}/schedule`),
  getPlayers: (teamId) => request('GET', `/players?team=${teamId}`),
  getPlayer: (id) => request('GET', `/players/${id}`),
  getTournament: (id) => request('GET', `/tournaments/${id}`),
  advanceDay: () => request('POST', '/calendar/advance'),
}
