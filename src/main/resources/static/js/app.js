const App = {
    init() {
        this.loadStoredState();
        AdvanceWidget.init();
        this.bindGlobalEvents();
        this.renderInitialContent();
    },

    loadStoredState() {
        const hasState = AppState.loadState();
        if (hasState) {
            const { page, panel } = AppState.getState();
            Router.updateHash(page, panel);
        }
    },

    bindGlobalEvents() {
        window.addEventListener('beforeunload', () => AppState.saveState());
    },

    renderInitialContent() {
        const { page, panel } = AppState.getState();
        Router.loadPage(page, panel);
    },

    getPageContent(page, panel) {
        const pageData = {
            incoming: {
                title: 'Incoming',
                content: '<div class="placeholder">Incoming panel content</div>'
            },
            squad: {
                title: 'Squad',
                content: '<div class="placeholder">Squad panel content</div>'
            },
            tactic: {
                title: 'Tactic',
                content: '<div class="placeholder">Tactic panel content</div>'
            },
            tournaments: {
                title: 'Tournaments',
                content: '<div class="placeholder">Tournaments panel content</div>'
            },
            schedule: {
                title: 'Schedule',
                content: '<div class="placeholder">Schedule panel content</div>'
            },
            club: {
                title: 'Club',
                content: '<div class="placeholder">Club panel content</div>'
            }
        };

        return pageData[panel] || { title: panel, content: '<div class="placeholder">Panel not found</div>' };
    },

    showErrorPage(message) {
        return '<div class="error-message">' + message + '</div>';
    }
};

App.init();
