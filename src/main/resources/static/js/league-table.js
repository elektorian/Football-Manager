const LeagueTable = {
    sortColumn: null,
    sortDirection: null,

    init() {
        document.addEventListener('click', (e) => {
            const th = e.target.closest('.sortable-header');
            if (th) {
                this.handleSort(th.dataset.sortKey);
            }
        });
    },

    render(leagueData) {
        const teams = [...leagueData.teams];
        let html = `<h1 class="league-title">${this.escapeHtml(leagueData.name)}</h1>`;
        html += `<div class="league-table-container"><table class="league-table" data-league-name="${this.escapeHtml(leagueData.name)}">`;
        html += `<thead><tr>`;

        const columns = [
            { key: 'position', label: '#' },
            { key: 'name', label: 'Team' },
            { key: 'matches', label: 'M' },
            { key: 'wins', label: 'W' },
            { key: 'draws', label: 'D' },
            { key: 'losses', label: 'L' },
            { key: 'scored', label: 'Scored' },
            { key: 'conceded', label: 'Conceded' },
            { key: 'diff', label: 'Diff' },
            { key: 'points', label: 'Pts' }
        ];

        columns.forEach(col => {
            const sortIndicator = this.getSortIndicator(col.key);
            html += `<th data-sort-key="${col.key}" class="sortable-header">${col.label}${sortIndicator}</th>`;
        });

        html += `</tr></thead><tbody>`;

        teams.forEach(team => {
            html += `<tr>`;
            html += `<td>${team.position}</td>`;
            html += `<td class="team-name">${this.escapeHtml(team.name)}</td>`;
            html += `<td>${team.matches}</td>`;
            html += `<td>${team.wins}</td>`;
            html += `<td>${team.draws}</td>`;
            html += `<td>${team.losses}</td>`;
            html += `<td>${team.scored}</td>`;
            html += `<td>${team.conceded}</td>`;
            html += `<td>${team.diff}</td>`;
            html += `<td class="points-cell">${team.points}</td>`;
            html += `</tr>`;
        });

        html += `</tbody></table></div>`;
        return html;
    },

    getSortIndicator(key) {
        if (this.sortColumn !== key) return '';
        return this.sortDirection === 'asc' ? ' \u2191' : ' \u2193';
    },

    handleSort(columnKey) {
        if (this.sortColumn === columnKey) {
            this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            this.sortColumn = columnKey;
            this.sortDirection = 'asc';
        }
        this.refreshTable();
    },

    refreshTable() {
        const table = document.querySelector('.league-table');
        if (!table) return;

        const leagueName = table.dataset.leagueName;
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));

        rows.sort((a, b) => {
            const aVal = this.parseCellValue(a.cells, this.sortColumn);
            const bVal = this.parseCellValue(b.cells, this.sortColumn);

            let comparison = 0;
            if (typeof aVal === 'string') {
                comparison = aVal.localeCompare(bVal);
            } else {
                comparison = aVal - bVal;
            }

            return this.sortDirection === 'asc' ? comparison : -comparison;
        });

        rows.forEach(row => tbody.appendChild(row));

        const headers = table.querySelectorAll('.sortable-header');
        headers.forEach(th => {
            th.classList.remove('sort-asc', 'sort-desc');
            if (th.dataset.sortKey === this.sortColumn) {
                th.classList.add(this.sortDirection === 'asc' ? 'sort-asc' : 'sort-desc');
            }
        });
    },

    parseCellValue(cells, key) {
        const keyMap = ['position', 'name', 'matches', 'wins', 'draws', 'losses', 'scored', 'conceded', 'diff', 'points'];
        const index = keyMap.indexOf(key);
        const val = cells[index].textContent.trim();
        return key === 'name' ? val : parseInt(val, 10) || 0;
    },

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
};

window.LeagueTable = LeagueTable;
