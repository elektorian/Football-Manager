const PanelManager = {
    init() {
        this.bindLeftPanelClicks();
    },

    bindLeftPanelClicks() {
        document.querySelectorAll('.panel-item').forEach(item => {
            item.addEventListener('click', (e) => this.handlePanelClick(e));
        });
    },

    handlePanelClick(e) {
        const page = e.target.dataset.page || e.target.textContent.toLowerCase();
        const panel = e.target.dataset.panel || page;
        
        // Убираем дубли если page == panel
        if (page === panel) {
            window.location.hash = page;
        } else {
            window.location.hash = `${page}/${panel}`;
        }
        setState(page, panel);
    }
};

PanelManager.init();
