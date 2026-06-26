function render() {
    renderHeader();
    renderSidebar();
    renderPage();
}

function renderPage() {
    const el = document.getElementById('content');

    if (AppState.loading && !AppState.date) {
        el.innerHTML = '<div class="loading"></div>';
        return;
    }

    switch (AppState.activeTab) {
        case 'home':
            el.innerHTML = renderHome();
            break;
        default:
            el.innerHTML = renderPlaceholder(AppState.activeTab);
            break;
    }
}

window.addEventListener('DOMContentLoaded', () => {
    AppState.load();
});
