function renderHome() {
    const nextMatch = AppState.nextMatch;
    const table = AppState.leagueTable;
    const form = AppState.recentForm;
    const fixtures = AppState.upcomingFixtures;

    let nextHtml = '';
    if (nextMatch) {
        nextHtml = `
            <div class="match-opponent">${nextMatch.opponent} (${nextMatch.venue})</div>
            <div class="match-datetime">${nextMatch.date}</div>
            <div class="match-competition">${nextMatch.competition}</div>
        `;
    } else {
        nextHtml = `<div class="inbox-empty">No upcoming matches</div>`;
    }

    const formHtml = form.length > 0
        ? form.map(f => `<span class="form-badge form-${f.toLowerCase()}">${f}</span>`).join('')
        : '<span class="events-empty">No results yet</span>';

    const tableRows = table.map(t => `
        <div class="fixture-row">
            <span class="fixture-date" style="width:auto;margin-right:12px;font-weight:700;color:${t.position <= 4 ? 'var(--accent-green)' : 'var(--text-primary)'}">${t.position}</span>
            <span class="fixture-opponent">${t.clubName}</span>
            <span style="margin-left:auto;color:var(--text-secondary);font-variant-numeric:tabular-nums">${t.played}/${t.points}</span>
            <span style="margin-left:12px;color:var(--text-secondary);font-size:13px">${t.gd >= 0 ? '+' : ''}${t.gd}</span>
        </div>
    `).join('');

    const fixturesHtml = fixtures.map(f => `
        <div class="fixture-row">
            <span class="fixture-date">${f.date}</span>
            <span class="fixture-opponent">${f.opponent} (${f.venue})</span>
            <span class="fixture-time">${f.competition}</span>
        </div>
    `).join('');

    return `
        <div class="home-grid">
            <div class="card match-card">
                <div class="card-title">Next Match</div>
                ${nextHtml}
            </div>
            <div class="card table-card">
                <div class="card-title">League Table</div>
                <div class="fixtures-list">${tableRows}</div>
            </div>
            <div class="card form-card">
                <div class="card-title">Recent Form</div>
                <div class="form-row">${formHtml}</div>
            </div>
        </div>

        <div class="card fixtures-card">
            <div class="card-title">Upcoming Fixtures</div>
            <div class="fixtures-list">${fixturesHtml || '<div class="events-empty">No upcoming fixtures</div>'}</div>
        </div>

        <div class="card inbox-card">
            <div class="card-title">Inbox</div>
            <div class="inbox-empty">No new messages</div>
        </div>
    `;
}
