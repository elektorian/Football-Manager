function renderHome() {
    const m = AppState.nextMatch;
    const t = AppState.leagueTable;
    const form = AppState.form;
    const fixtures = AppState.fixtures;

    const formHtml = form.map(f => `<span class="form-badge form-${f.toLowerCase()}">${f}</span>`).join('');

    const fixturesHtml = fixtures.map(f => `
        <div class="fixture-row">
            <span class="fixture-date">${f.date}</span>
            <span class="fixture-opponent">${f.opponent} (${f.venue})</span>
            <span class="fixture-time">${f.time}</span>
        </div>
    `).join('');

    return `
        <div class="home-grid">
            <div class="card match-card">
                <div class="card-title">Next Match</div>
                <div class="match-opponent">${m.opponent} (${m.venue})</div>
                <div class="match-datetime">${m.date}, ${m.time}</div>
                <div class="match-competition">${m.competition}</div>
            </div>
            <div class="card table-card">
                <div class="card-title">League Table</div>
                <div class="table-competition">${t.competition}</div>
                <div class="table-position">
                    <span class="pos-badge">${t.position}${ordinal(t.position)}</span>
                    <span class="pos-total">/ ${t.total}</span>
                </div>
                <div class="table-stats">Pts: ${t.pts} &middot; GD: ${t.gd}</div>
            </div>
            <div class="card form-card">
                <div class="card-title">Recent Form</div>
                <div class="form-row">${formHtml}</div>
            </div>
        </div>

        <div class="card fixtures-card">
            <div class="card-title">Upcoming Fixtures</div>
            <div class="fixtures-list">${fixturesHtml}</div>
        </div>

        <div class="card inbox-card">
            <div class="card-title">Inbox</div>
            <div class="inbox-empty">No new messages</div>
        </div>
    `;
}

function ordinal(n) {
    const s = ['th', 'st', 'nd', 'rd'];
    const v = n % 100;
    return s[(v - 20) % 10] || s[v] || s[0];
}
