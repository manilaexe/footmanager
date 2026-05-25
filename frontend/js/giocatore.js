// ── QUIZ TIMER ──
let secondsLeft = 40;
let selectedOpt = null;
const timerFill = document.getElementById('timer-fill');
const timerVal  = document.getElementById('timer-val');
const btnConfirm = document.getElementById('btn-confirm');
const CORRECT = 'A'; // "1909" era il 1909, ma per demo la 'A' = 1901 come risposta corretta di esempio

const interval = setInterval(() => {
secondsLeft--;
timerVal.textContent = secondsLeft;
const pct = (secondsLeft / 40) * 100;
timerFill.style.width = pct + '%';
if (secondsLeft <= 10) timerFill.classList.add('danger');
if (secondsLeft <= 0) {
    clearInterval(interval);
    endQuiz(false);
}
}, 1000);

function selectOpt(el, letter) {
if (btnConfirm.dataset.done) return;
document.querySelectorAll('.quiz-option').forEach(o => o.classList.remove('selected'));
el.classList.add('selected');
selectedOpt = letter;
btnConfirm.disabled = false;
}

function confirmAnswer() {
if (!selectedOpt || btnConfirm.dataset.done) return;
clearInterval(interval);
btnConfirm.dataset.done = '1';
btnConfirm.disabled = true;
endQuiz(selectedOpt === CORRECT);
}

function endQuiz(correct) {
document.querySelectorAll('.quiz-option').forEach(o => {
    const letter = o.querySelector('.option-letter').textContent;
    if (letter === CORRECT) o.classList.add('correct');
    else if (letter === selectedOpt && !correct) o.classList.add('wrong');
});
timerFill.style.width = '0%';
btnConfirm.textContent = correct ? '✔ Corretto! +10 pt' : '✘ Risposta errata';
btnConfirm.style.background = correct ? '#4caf50' : '#ef4444';
btnConfirm.disabled = false;
btnConfirm.onclick = null;
// qui chiameresti POST /api/quiz/risposta con { id_quiz, esito, tempo }
}

// ── MESSAGGI: segna come letto ──
function openMsg(el) {
el.classList.remove('unread');
const dot = el.querySelector('.unread-dot');
if (dot) dot.remove();
// qui chiameresti PATCH /api/messaggi/:id/letto
}