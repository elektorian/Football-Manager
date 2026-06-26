const API = {
    async getState() {
        const res = await fetch('/api/engine/state');
        if (!res.ok) throw new Error(`GET /api/engine/state: ${res.status}`);
        return res.json();
    },

    async advance(days = 1) {
        const res = await fetch(`/api/engine/advance?days=${days}`, { method: 'POST' });
        if (!res.ok) throw new Error(`POST /api/engine/advance: ${res.status}`);
        return res.json();
    }
};
