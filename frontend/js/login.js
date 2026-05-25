const form     = document.getElementById('login-form');
const btn      = document.getElementById('btn-login');
const alertBox = document.getElementById('alert-box');
const pwInput  = document.getElementById('password');
const togglePw = document.getElementById('toggle-pw');

togglePw.addEventListener('click', () => {
const isHidden = pwInput.type === 'password';
pwInput.type   = isHidden ? 'text' : 'password';
togglePw.textContent = isHidden ? '🙈' : '👁';
});

form.addEventListener('submit', async (e) => {
e.preventDefault();
const username = document.getElementById('username').value.trim();
const password = pwInput.value;

if (!username || !password) {
    showAlert('Compila tutti i campi.');
    return;
}

setLoading(true);
alertBox.classList.remove('show');

try {
    const res = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
    });

    if (!res.ok) throw new Error();

    const data = await res.json();
    // Salva JWT e redirect in base al ruolo
    localStorage.setItem('fm_token', data.token);
    redirectByRole(data.ruolo);

} catch {
    showAlert('Credenziali non valide. Riprova.');
    setLoading(false);
}
});

function setLoading(on) {
btn.disabled = on;
btn.innerHTML = on
    ? '<span class="spinner"></span>Accesso in corso...'
    : 'Accedi';
}

function showAlert(msg) {
alertBox.textContent = msg;
alertBox.classList.add('show');
}

function redirectByRole(ruolo) {
const map = {
    'STAFF':       'pages/dashboard-staff.html',
    'ALLENATORE':  'pages/dashboard-allenatore.html',
    'GIOCATORE':   'pages/dashboard-giocatore.html',
    'DIRIGENZA':   'pages/dashboard-dirigenza.html',
    'IT':          'pages/dashboard-it.html',
};
window.location.href = map[ruolo] ?? 'pages/dashboard-giocatore.html';
}

// Demo login: pre-compila i campi e simula submit
function demoLogin(user, pass) {
document.getElementById('username').value = user;
pwInput.value = pass;
// In un vero contesto manderebbe la richiesta al backend
// Qui simuliamo il redirect per demo
const roleMap = {
    'allenatore': 'ALLENATORE',
    'giocatore1': 'GIOCATORE',
    'staff1':     'STAFF',
    'dirigente1': 'DIRIGENZA',
};
const fakeToken = btoa(JSON.stringify({ user, role: roleMap[user] }));
localStorage.setItem('fm_token', fakeToken);
redirectByRole(roleMap[user]);
}