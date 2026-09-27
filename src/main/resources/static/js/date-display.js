/**
 * Обновление отображения даты
 */
const DateDisplay = {
    /**
     * Обновляет элемент date-display контейнер
     * @param {Element} container - DOM контейнер с date и time элементами
     * @param {Object|Date|string} time - Java LocalDateTime (object), JS Date или ISO string "2019-07-01T08:00:00"
     */
    update(container, time) {
        const dateEl = container.querySelector('.date');
        const timeEl = container.querySelector('.time');
        
        if (dateEl && timeEl && time) {
            if (typeof time === 'object') {
                // Java object: {year, monthValue, dayOfMonth, hour, minute}
                const { dayOfMonth, monthValue, year, hour, minute } = time;
                dateEl.textContent = `${dayOfMonth}.${monthValue}.${year}`;
                timeEl.textContent = `${hour}:${minute}`;
            } else {
                // ISO string: "2019-07-01T08:00:00"
                const isoString = String(time);
                const dateParts = isoString.split('T')[0].split('-'); // ["2019", "07", "01"]
                const timeParts = isoString.split('T')[1]?.split(':') || ["00", "00", "00"]; // ["08", "00", "00"]
                
                dateEl.textContent = `${dateParts[2]}.${dateParts[1]}.${dateParts[0]}`;
                timeEl.textContent = `${timeParts[0]}:${timeParts[1]}`;
            }
        }
    },

    formatDate(dateObj) {
        // Для случая когда приходит JS Date
        if (!(dateObj instanceof Date)) return '';
        const pad = (n) => String(n).padStart(2, '0');
        return `${pad(dateObj.getDate())}.${pad(dateObj.getMonth() + 1)}.${dateObj.getFullYear()} ${pad(dateObj.getHours())}:${pad(dateObj.getMinutes())}`;
    }
};

window.DateDisplay = DateDisplay;
