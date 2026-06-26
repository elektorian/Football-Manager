const TABS = [
    { id: 'home',        icon: '🏠',  label: 'Home' },
    { id: 'squad',       icon: '👥',  label: 'Squad' },
    { id: 'tactics',     icon: '📋',  label: 'Tactics' },
    { id: 'training',    icon: '🏋️', label: 'Training' },
    { id: 'staff',       icon: '👔',  label: 'Staff' },
    { id: 'fixtures',    icon: '📅',  label: 'Fixtures' },
    { id: 'competitions',icon: '🏆',  label: 'Competitions' },
    { id: 'scouting',    icon: '🔍',  label: 'Scouting' },
    { id: 'transfers',   icon: '💰',  label: 'Transfers' },
    { id: 'club',        icon: '🏟️',  label: 'Club' }
];

function renderSidebar() {
    const el = document.getElementById('sidebar');
    el.innerHTML = TABS.map(tab => `
        <div class="sidebar-item ${AppState.activeTab === tab.id ? 'active' : ''}" data-tab="${tab.id}">
            <span class="sidebar-icon">${tab.icon}</span>
            <span class="sidebar-label">${tab.label}</span>
        </div>
    `).join('');

    el.querySelectorAll('.sidebar-item').forEach(item => {
        item.addEventListener('click', () => AppState.setTab(item.dataset.tab));
    });
}
