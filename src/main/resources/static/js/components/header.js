function renderHeader() {
    const el = document.getElementById('header');
    el.innerHTML = `
        <div class="header-left">
            <span class="header-logo">FM</span>
            <span class="header-club">${AppState.clubName}</span>
            <span class="header-date">${AppState.date || '—'}</span>
        </div>
        <div class="header-right">
            <button class="btn btn-primary" id="btnContinue" ${AppState.loading ? 'disabled' : ''}>
                ${AppState.loading ? 'Loading...' : 'Continue'}
            </button>
        </div>
    `;

    document.getElementById('btnContinue')?.addEventListener('click', () => AppState.advance());
}
