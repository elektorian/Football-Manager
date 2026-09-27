/**
 * Виджет Advance - управляет кнопкой и fetch-запросами
 */
const AdvanceWidget = {
    btn: null,
    dateDisplayContainer: null,

    init() {
        this.btn = document.getElementById('advance-btn');
        this.dateDisplayContainer = document.getElementById('date-display-container');

        if (!this.btn || !this.dateDisplayContainer) {
            console.warn('AdvanceWidget: Could not find required elements');
            return;
        }

        this.bindClick();
        this.loadCurrentTime();
    },

    bindClick() {
        this.btn.addEventListener('click', () => {
            this.fetchAndAdvance();
        });
    },

    async fetchAndAdvance() {
        this.btn.disabled = true;
        this.btn.textContent = '...';

        try {
            const response = await fetch('/advance', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const data = await response.json();
            const time = data.time;

            DateDisplay.update(this.dateDisplayContainer, time);

        } catch (error) {
            console.error('AdvanceWidget fetch error:', error);
            this.showError();
        } finally {
            this.btn.disabled = false;
            this.btn.textContent = 'advance';
        }
    },

    async loadCurrentTime() {
        try {
            const response = await fetch('/time');
            const data = await response.json();
            DateDisplay.update(this.dateDisplayContainer, data.time);
        } catch (error) {
            console.error('AdvanceWidget loadCurrentTime error:', error);
        }
    },

    showError() {
        const errorHtml = `
            <div class="error-message">
                Не удалось обновить время. Попробуйте позже.
            </div>
        `;
        this.dateDisplayContainer.innerHTML = errorHtml;
    }
};

window.AdvanceWidget = AdvanceWidget;
