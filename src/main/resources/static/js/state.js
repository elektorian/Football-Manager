const AppState = {
    page: null,
    panel: null,
    settings: null
};

const STORAGE_KEY = 'footballManagerState';

function saveState() {
    try {
        const state = {
            page: AppState.page,
            panel: AppState.panel,
            settings: AppState.settings
        };
        localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
    } catch (error) {
        console.error('Error saving state:', error);
    }
}

function loadState() {
    try {
        const saved = localStorage.getItem(STORAGE_KEY);
        if (saved) {
            const parsed = JSON.parse(saved);
            AppState.page = parsed.page || 'main';
            AppState.panel = parsed.panel || 'incoming';
            AppState.settings = parsed.settings || null;
            return true;
        }
    } catch (error) {
        console.error('Error loading state:', error);
    }
    return false;
}

function clearState() {
    try {
        localStorage.removeItem(STORAGE_KEY);
    } catch (error) {
        console.error('Error clearing state:', error);
    }
}

function setState(page, panel, settings = null) {
    AppState.page = page;
    AppState.panel = panel;
    AppState.settings = settings;
    saveState();
}

function getState() {
    return { ...AppState };
}

window.AppState = AppState;
window.AppState.loadState = loadState;
window.AppState.setState = setState;
window.AppState.getState = getState;
window.AppState.saveState = saveState;
window.AppState.clearState = clearState;
