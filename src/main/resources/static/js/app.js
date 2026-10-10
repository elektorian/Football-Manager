const App = {
    init() {
        this.loadStoredState();
        AdvanceWidget.init();
        this.bindGlobalEvents();
        this.renderInitialContent();
    },

    renderInitialContent() {
        console.log('[App] renderInitialContent started');
        Router.loadPage('incoming', 'incoming').then(() => {
            console.log('[App] loadPage completed, calling loadMessages');
            if (App.loadMessages) {
                App.loadMessages();
            }
        }).catch(err => console.error('[App] renderInitialContent error:', err));
    },

    loadStoredState() {
        AppState.loadState();
    },

    bindGlobalEvents() {
        window.addEventListener('beforeunload', () => AppState.saveState());
    },



    getPageContent(page, panel) {
        const pageData = {
            incoming: {
                title: 'Incoming',
                content: this.getIncomingContent()
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

    getIncomingContent() {
        return `
            <div class="incoming-grid">
                <div class="incoming-message-list" id="incoming-message-list">
                    <div class="message-list-placeholder">Loading...</div>
                </div>
                <div class="incoming-match-overview" id="incoming-match-overview">
                    <div class="match-overview-placeholder">Select a message</div>
                </div>
            </div>
        `;
    },

    async loadMessages() {
        try {
            const response = await fetch('/messages');
            if (!response.ok) throw new Error('Failed to load messages');
            const messages = await response.json();
            this.renderMessageList(messages);
        } catch (error) {
            console.error('Error loading messages:', error);
            const listEl = document.getElementById('incoming-message-list');
            if (listEl) {
                listEl.innerHTML = '<div class="message-list-error">Failed to load messages</div>';
            }
        }
    },

    renderMessageList(messages) {
        const listEl = document.getElementById('incoming-message-list');
        if (!listEl) return;

        if (messages.length === 0) {
            listEl.innerHTML = '<div class="message-list-empty">No messages</div>';
            return;
        }

        const sorted = [...messages].sort((a, b) => new Date(b.timestamp || 0) - new Date(a.timestamp || 0));

        const itemsHtml = sorted.map(msg => {
            const isRead = msg.read === true ? 'read' : '';
            const title = msg.title || msg.subject || 'No title';
            const ts = msg.timestamp || '';
            const formattedTs = ts ? ts.substring(0, 16).replace('T', ' ') : '';
            return `
                <div class="message-item ${isRead}" data-id="${msg.id}">
                    <div class="message-title">${title}</div>
                    <div class="message-time">${formattedTs}</div>
                </div>
            `;
        }).join('');

        listEl.innerHTML = itemsHtml;

        listEl.querySelectorAll('.message-item').forEach(item => {
            item.addEventListener('click', () => {
                listEl.querySelectorAll('.message-item').forEach(i => i.classList.remove('active'));
                item.classList.add('active');
                const msgId = item.dataset.id;
                this.loadMatchOverview(msgId);
            });
        });
    },

    async loadMatchOverview(messageId) {
        const overviewEl = document.getElementById('incoming-match-overview');
        if (!overviewEl) return;

        try {
            const response = await fetch(`/messages/match-overview/${messageId}`);
            if (!response.ok) throw new Error('Failed to load match overview');
            const data = await response.json();
            this.renderMatchOverview(data.matches);
        } catch (error) {
            console.error('Error loading match overview:', error);
            overviewEl.innerHTML = '<div class="match-overview-error">Failed to load match results</div>';
        }
    },

    formatDate(dateStr) {
        if (!dateStr) return '';
        const parts = dateStr.split('-');
        return `${parts[2]}.${parts[1]}.${parts[0]}`;
    },

    renderMatchOverview(matches) {
        const overviewEl = document.getElementById('incoming-match-overview');
        if (!overviewEl) return;

        if (!matches || matches.length === 0) {
            overviewEl.innerHTML = '<div class="match-overview-empty">No matches</div>';
            return;
        }

        const rowsHtml = matches.map(match => {
            const homeScore = match.homeTeamScore !== null && match.homeTeamScore !== undefined
                ? match.homeTeamScore.toString()
                : '';
            const awayScore = match.awayTeamScore !== null && match.awayTeamScore !== undefined
                ? match.awayTeamScore.toString()
                : '';
            const date = match.date ? this.formatDate(match.date) : '';

            return `
                <div class="match-row">
                    <span class="match-date">${date}</span>
                    <span class="match-teams">${match.homeTeamName || ''}</span>
                    <span class="match-score">
                        <span class="score-home">${homeScore}</span> : <span class="score-away">${awayScore}</span>
                    </span>
                    <span class="match-teams">${match.awayTeamName || ''}</span>
                </div>
            `;
        }).join('');

        overviewEl.innerHTML = `<div class="match-rows">${rowsHtml}</div>`;
    },

    showErrorPage(message) {
        return '<div class="error-message">' + message + '</div>';
    }
};

Router.init();
PanelManager.init();
App.init();
