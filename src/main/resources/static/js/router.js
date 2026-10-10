const Router = {
    init() {
        window.addEventListener('hashchange', () => this.handleHashChange());
    },

    parseHash() {
        const hash = window.location.hash.slice(1);
        if (!hash) {
            return { page: 'incoming', panel: 'incoming' };
        }
        const parts = hash.split('/');
        if (parts.length === 1) {
            return { page: parts[0], panel: parts[0] };
        }
        return { page: parts[0], panel: parts[1] };
    },

    updateHash(page, panel) {
        if (page === panel) {
            window.location.hash = page;
        } else {
            window.location.hash = `${page}/${panel}`;
        }
    },

    handleHashChange() {
        const { page, panel } = this.parseHash();
        setState(page, panel);
        this.loadPage(page, panel).then(() => {
            if (page === 'incoming' && panel === 'incoming' && App.loadMessages) {
                App.loadMessages();
            }
        });
    },

    async loadPage(page, panel) {
        try {
            console.log(`[Router] loadPage called: page=${page}, panel=${panel}`);
            const content = await this.fetchContent(page, panel);
            console.log(`[Router] content received, length=${content.length}`);
            this.renderContent(page, panel, content);
            console.log('[Router] renderContent done');
        } catch (error) {
            console.error('[Router] loadPage error:', error);
            this.showError(error.message);
        }
    },

    async fetchContent(page, panel) {
        // Get content from page data
        const pageData = App.getPageContent(page, panel);
        return pageData.content;
    },

    renderContent(page, panel, content) {
        const title = document.getElementById('panel-title');
        const contentDiv = document.getElementById('panel-content');
        
        console.log('[Router] DOM elements - title:', !!title, 'contentDiv:', !!contentDiv);
        title.textContent = page.charAt(0).toUpperCase() + page.slice(1);
        contentDiv.innerHTML = content;
        console.log('[Router] Content rendered. panel-content now has', contentDiv.children.length, 'children');
    },

    showError(message) {
        const contentDiv = document.getElementById('panel-content');
        contentDiv.innerHTML = `<div class="error-message">${this.escapeHtml(message)}</div>`;
    },

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
};

window.Router = Router;
