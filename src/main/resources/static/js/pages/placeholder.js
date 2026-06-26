const PLACEHOLDER_LABELS = {
    squad: 'Squad',
    tactics: 'Tactics',
    training: 'Training',
    staff: 'Staff',
    fixtures: 'Fixtures',
    competitions: 'Competitions',
    scouting: 'Scouting',
    transfers: 'Transfers',
    club: 'Club'
};

function renderPlaceholder(tab) {
    const label = PLACEHOLDER_LABELS[tab] || tab;
    return `
        <div class="placeholder">
            <div class="placeholder-icon">🔧</div>
            <div class="placeholder-title">${label}</div>
            <div class="placeholder-text">Coming soon</div>
        </div>
    `;
}
