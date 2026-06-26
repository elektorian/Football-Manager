const AppState = {
    date: null,
    club: null,
    nextMatch: null,
    leagueTable: [],
    recentForm: [],
    upcomingFixtures: [],
    activeTab: 'home',
    loading: false,

    setTab(tab) {
        this.activeTab = tab;
        render();
    },

    updateFrom(data) {
        this.date = data.date;
        this.club = data.club;
        this.nextMatch = data.nextMatch;
        this.leagueTable = data.leagueTable || [];
        this.recentForm = data.recentForm || [];
        this.upcomingFixtures = data.upcomingFixtures || [];
    },

    async load() {
        this.loading = true;
        render();
        try {
            const state = await API.getState();
            this.updateFrom(state);
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
            this.updateFrom(result);
        } catch (err) {
            console.error('Failed to advance:', err);
        } finally {
            this.loading = false;
            render();
        }
    }
};
