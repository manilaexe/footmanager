const PLAYERS = [
  { id:1,  init:'LR', nome:'Lorenzo Rossi',    num:9,  pos:'ATT', piede:'Destro',    naz:'🇮🇹', alt:181, status:'available',  pres:18, gol:12, ass:5,  tiri:34, pass_pct:78, drib_pct:62 },
  { id:2,  init:'MB', nome:'Marco Bianchi',    num:8,  pos:'CEN', piede:'Sinistro',  naz:'🇮🇹', alt:178, status:'available',  pres:20, gol:4,  ass:8,  tiri:18, pass_pct:85, drib_pct:70 },
  { id:3,  init:'AF', nome:'Andrea Ferrari',   num:11, pos:'ATT', piede:'Destro',    naz:'🇮🇹', alt:176, status:'injured',    pres:15, gol:9,  ass:3,  tiri:28, pass_pct:72, drib_pct:68 },
  { id:4,  init:'GE', nome:'Giorgio Esposito', num:1,  pos:'POR', piede:'Destro',    naz:'🇮🇹', alt:190, status:'available',  pres:22, gol:0,  ass:0,  tiri:0,  pass_pct:88, drib_pct:0  },
  { id:5,  init:'PR', nome:'Paolo Romano',     num:5,  pos:'DIF', piede:'Destro',    naz:'🇮🇹', alt:183, status:'suspended',  pres:19, gol:2,  ass:1,  tiri:8,  pass_pct:80, drib_pct:45 },
  { id:6,  init:'FL', nome:'Filippo Luca',     num:7,  pos:'ATT', piede:'Sinistro',  naz:'🇮🇹', alt:174, status:'available',  pres:17, gol:1,  ass:4,  tiri:20, pass_pct:74, drib_pct:75 },
  { id:7,  init:'DM', nome:'Davide Mancini',   num:6,  pos:'DIF', piede:'Destro',    naz:'🇮🇹', alt:185, status:'available',  pres:21, gol:1,  ass:2,  tiri:6,  pass_pct:82, drib_pct:40 },
  { id:8,  init:'SM', nome:'Simone Martini',   num:4,  pos:'CEN', piede:'Destro',    naz:'🇮🇹', alt:180, status:'available',  pres:18, gol:3,  ass:6,  tiri:14, pass_pct:87, drib_pct:60 },
  { id:9,  init:'RC', nome:'Roberto Conti',    num:3,  pos:'DIF', piede:'Sinistro',  naz:'🇮🇹', alt:182, status:'available',  pres:20, gol:0,  ass:1,  tiri:4,  pass_pct:83, drib_pct:35 },
  { id:10, init:'VN', nome:'Valerio Neri',     num:2,  pos:'DIF', piede:'Destro',    naz:'🇮🇹', alt:179, status:'available',  pres:16, gol:0,  ass:3,  tiri:5,  pass_pct:79, drib_pct:42 },
  { id:11, init:'EM', nome:'Emre Mazzi',       num:10, pos:'CEN', piede:'Destro',    naz:'🇹🇷', alt:177, status:'available',  pres:19, gol:5,  ass:9,  tiri:22, pass_pct:89, drib_pct:72 },
  { id:12, init:'KD', nome:'Kevin Dupont',     num:23, pos:'ATT', piede:'Destro',    naz:'🇫🇷', alt:183, status:'available',  pres:14, gol:6,  ass:2,  tiri:24, pass_pct:70, drib_pct:65 },
];

const posColor = { ATT:'pos-att', CEN:'pos-cen', DIF:'pos-dif', POR:'pos-por' };
const statusLabel = { available:'Disponibile', injured:'Infortunato', suspended:'Squalificato' };
const statusPill  = { available:'pill-green', injured:'pill-red', suspended:'pill-amber' };
let currentFilter = 'tutti';
let currentStatus = 'tutti';
let currentView   = 'grid';

function getFiltered() {
  const q = document.getElementById('search-input').value.toLowerCase();
  return PLAYERS.filter(p => {
    const matchPos    = currentFilter === 'tutti' || p.pos === currentFilter;
    const matchStatus = currentStatus === 'tutti' || p.status === currentStatus;
    const matchSearch = !q || p.nome.toLowerCase().includes(q) || p.pos.toLowerCase().includes(q) || String(p.num).includes(q);
    return matchPos && matchStatus && matchSearch;
  });
}

function renderGrid() {
  const grid = document.getElementById('view-grid');
  const players = getFiltered();
  grid.innerHTML = players.map(p => `
    <div class="player-card" onclick="openDetail(${p.id})">
      <div class="player-card-top">
        <span class="number">#${p.num}</span>
        <div class="status-dot ${p.status}"></div>
        <div class="player-pic">${p.init}</div>
        <div class="name">${p.nome.split(' ')[0]}<br/>${p.nome.split(' ')[1]}</div>
        <span class="pos-badge ${posColor[p.pos]}">${p.pos}</span>
      </div>
      <div class="player-card-body">
        <div class="mini-stats">
          <div class="mini-stat"><div class="v">${p.pres}</div><div class="l">Pres</div></div>
          <div class="mini-stat"><div class="v">${p.gol}</div><div class="l">Gol</div></div>
          <div class="mini-stat"><div class="v">${p.ass}</div><div class="l">Ass</div></div>
        </div>
        <div class="player-meta">
          <span class="meta-tag">${p.naz}</span>
          <span class="meta-tag">${p.piede}</span>
          <span class="meta-tag">${p.alt} cm</span>
        </div>
        <div class="card-actions">
          <div class="btn-card primary">📊 Stats</div>
          <div class="btn-card">✏️ Modifica</div>
        </div>
      </div>
    </div>
  `).join('');
}

function renderList() {
  const tbody = document.getElementById('list-body');
  const players = getFiltered();
  tbody.innerHTML = players.map(p => `
    <tr onclick="openDetail(${p.id})">
      <td style="color:var(--muted);font-weight:600">#${p.num}</td>
      <td><div class="player-name-cell"><div class="list-avatar">${p.init}</div>${p.nome}</div></td>
      <td><span class="pill ${posColor[p.pos].replace('pos-','pill-').replace('att','red').replace('cen','blue').replace('dif','amber').replace('por','').trim()}" style="background:${p.pos==='POR'?'rgba(139,92,246,0.2)':''};color:${p.pos==='POR'?'#a78bfa':''}">${p.pos}</span></td>
      <td>${p.piede}</td>
      <td>${p.pres}</td>
      <td><strong>${p.gol}</strong></td>
      <td>${p.ass}</td>
      <td><span class="pill ${statusPill[p.status]}">${statusLabel[p.status]}</span></td>
      <td><div class="tbl-actions"><button class="btn-sm" onclick="event.stopPropagation()">✏️</button><button class="btn-sm danger" onclick="event.stopPropagation()">🗑</button></div></td>
    </tr>
  `).join('');
}

function render() { renderGrid(); renderList(); }

function setFilter(val, btn) {
  currentFilter = val;
  btn.closest('.filter-group').querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  render();
}
function setStatus(val, btn) {
  currentStatus = val;
  btn.closest('.filter-group').querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  render();
}
function filterPlayers() { render(); }

function setView(v) {
  currentView = v;
  document.getElementById('view-grid').style.display = v === 'grid' ? 'grid' : 'none';
  document.getElementById('view-list').style.display = v === 'list' ? 'block' : 'none';
  document.getElementById('btn-grid').classList.toggle('active', v === 'grid');
  document.getElementById('btn-list').classList.toggle('active', v === 'list');
}

function openDetail(id) {
  const p = PLAYERS.find(x => x.id === id);
  document.getElementById('detail-hero').innerHTML = `
    <div class="modal-hero-pic">${p.init}</div>
    <div class="modal-hero-info">
      <div class="number">N° ${p.num}</div>
      <h2>${p.nome}</h2>
      <div class="tags">
        <span class="pos-badge ${posColor[p.pos]}">${p.pos}</span>
        <span class="pill ${statusPill[p.status]}">${statusLabel[p.status]}</span>
      </div>
    </div>
  `;
  document.getElementById('detail-body').innerHTML = `
    <div class="modal-section-title">Dati anagrafici</div>
    <div class="info-grid">
      <div class="info-item"><div class="lbl">Nazionalità</div><div class="val">${p.naz}</div></div>
      <div class="info-item"><div class="lbl">Altezza</div><div class="val">${p.alt} cm</div></div>
      <div class="info-item"><div class="lbl">Piede</div><div class="val">${p.piede}</div></div>
    </div>
    <div class="modal-section-title">Statistiche stagione</div>
    <div class="stat-grid-modal">
      <div class="stat-box"><div class="v">${p.pres}</div><div class="l">Presenze</div></div>
      <div class="stat-box"><div class="v">${p.gol}</div><div class="l">Gol</div></div>
      <div class="stat-box"><div class="v">${p.ass}</div><div class="l">Assist</div></div>
      <div class="stat-box"><div class="v">${p.tiri}</div><div class="l">Tiri tot.</div></div>
    </div>
    <div class="modal-section-title" style="margin-top:1rem">Percentuali</div>
    ${p.pos !== 'POR' ? `
    <div style="margin-bottom:10px">
      <div style="display:flex;justify-content:space-between;font-size:0.8rem;margin-bottom:4px"><span style="color:var(--muted)">Passaggi riusciti</span><span>${p.pass_pct}%</span></div>
      <div style="background:var(--dark3);border-radius:4px;height:6px"><div style="height:6px;border-radius:4px;background:var(--green-l);width:${p.pass_pct}%"></div></div>
    </div>
    <div>
      <div style="display:flex;justify-content:space-between;font-size:0.8rem;margin-bottom:4px"><span style="color:var(--muted)">Dribbling riusciti</span><span>${p.drib_pct}%</span></div>
      <div style="background:var(--dark3);border-radius:4px;height:6px"><div style="height:6px;border-radius:4px;background:#60a5fa;width:${p.drib_pct}%"></div></div>
    </div>` : '<p style="color:var(--muted);font-size:0.875rem">Statistiche specifiche per portieri non disponibili in questa vista.</p>'}
  `;
  openModal('modal-detail');
}

function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

// Export CSV
document.getElementById('btn-export').addEventListener('click', () => {
  const rows = [['Nome','Pos','#','Pres','Gol','Assist','Stato']];
  PLAYERS.forEach(p => rows.push([p.nome,p.pos,p.num,p.pres,p.gol,p.ass,statusLabel[p.status]]));
  const csv = rows.map(r => r.join(',')).join('\n');
  const a = document.createElement('a');
  a.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
  a.download = 'rosa-footmanager.csv';
  a.click();
});

// Chiudi modal cliccando fuori
document.querySelectorAll('.modal-overlay').forEach(o => o.addEventListener('click', e => { if(e.target === o) o.classList.remove('open'); }));

render();