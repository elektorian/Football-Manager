const Router = {
    init() {
        this.handleHashChange();
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
        this.loadPage(page, panel);
    },

    async loadPage(page, panel) {
        try {
            const content = await this.fetchContent(page, panel);
            this.renderContent(page, panel, content);
        } catch (error) {
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
        
        title.textContent = page.charAt(0).toUpperCase() + page.slice(1);
        contentDiv.innerHTML = content;
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

Router.init();
