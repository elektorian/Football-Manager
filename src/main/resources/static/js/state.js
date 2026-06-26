const AppState = {
    date: null,
    clubName: 'Manchester United',
    activeTab: 'home',
    loading: false,

    nextMatch: {
        opponent: 'Arsenal',
        venue: 'A',
        date: '28 Jul 2024',
        time: '15:00',
        competition: 'Premier League'
    },

    leagueTable: {
        position: 3,
        total: 20,
        competition: 'Premier League',
        pts: 0,
        gd: 0
    },

    form: ['W', 'D', 'W', 'L', 'D'],

    fixtures: [
        { date: '28 Jul', opponent: 'Arsenal', venue: 'A', time: '15:00' },
        { date: '4 Aug', opponent: 'Chelsea', venue: 'H', time: '15:00' },
        { date: '11 Aug', opponent: 'Liverpool', venue: 'A', time: '15:00' },
        { date: '18 Aug', opponent: 'Man City', venue: 'H', time: '15:00' },
        { date: '25 Aug', opponent: 'Tottenham', venue: 'A', time: '15:00' }
    ],

    setTab(tab) {
        this.activeTab = tab;
        render();
    },

    async load() {
        this.loading = true;
        render();
        try {
            const state = await API.getState();
            this.date = state.date;
        } catch (err) {
            console.error('Failed to load state:', err);
            this.date = 'error';
        } finally {
            this.loading = false;
            render();
        }
    },

    async advance() {
        if (this.loading) return;
        this.loading = true;
        render();
        try {
            const result = await API.advance(1);
            this.date = result.date;
        } catch (err) {
            console.error('Failed to advance:', err);
        } finally {
            this.loading = false;
            render();
        }
    }
};
