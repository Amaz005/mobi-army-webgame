// Game portal (specs/004-game-portal): home page, shared game page, account dialog, live MobiArmy info.
// Plain JS, no libraries, nothing loaded from other sites. All catalog text goes through textContent.
(function () {
  'use strict';

  var RESERVED = { api: true, game: true, games: true };
  var LIVE_REFRESH_MS = 30000;
  // Per-browser convenience only (research D7): the welcome dialog was closed once.
  var WELCOME_KEY = 'portal.welcomeSeen';

  // --- helpers ---

  function $(sel, root) { return (root || document).querySelector(sel); }

  function el(tag, cls, text) {
    var n = document.createElement(tag);
    if (cls) n.className = cls;
    if (text != null) n.textContent = text;
    return n;
  }

  function link(text, href, cls) {
    var a = el('a', cls, text);
    a.href = href;
    return a;
  }

  function setStatus(text) {
    var s = $('#status');
    if (s) { s.textContent = text || ''; s.hidden = !text; }
  }

  function isSameSitePath(p) {
    return typeof p === 'string' && p.charAt(0) === '/' && p.charAt(1) !== '/' && p.indexOf('\\') < 0;
  }

  function isText(v) { return typeof v === 'string' && v.trim() !== ''; }

  function store(key, value) {
    try {
      if (value === undefined) return localStorage.getItem(key);
      localStorage.setItem(key, value);
    } catch (e) { /* private mode or blocked storage: the portal works without it */ }
    return null;
  }

  // contracts/game-catalog.md: skip broken entries, keep the rest (FR-005).
  function validEntry(e) {
    if (!e || typeof e !== 'object') return 'not an object';
    if (typeof e.id !== 'string' || !/^[a-z0-9-]{1,32}$/.test(e.id) || RESERVED[e.id]) return 'bad id';
    if (!isText(e.name)) return 'missing name';
    if (!isText(e.description)) return 'missing description';
    if (!isSameSitePath(e.playUrl)) return 'bad playUrl';
    if (e.image != null && !isSameSitePath(e.image)) return 'bad image';
    if (e.status != null && ['visible', 'maintenance', 'hidden'].indexOf(e.status) < 0) return 'bad status';
    return null;
  }

  function loadCatalog() {
    return fetch('/games.json', { cache: 'no-cache' })
      .then(function (r) { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })
      .then(function (list) {
        if (!Array.isArray(list)) throw new Error('games.json is not a list');
        var seen = {};
        var games = [];
        list.forEach(function (e, i) {
          var why = validEntry(e);
          if (!why && seen[e.id]) why = 'duplicate id';
          if (why) { console.warn('games.json entry ' + i + ' skipped: ' + why); return; }
          seen[e.id] = true;
          games.push(e);
        });
        games.sort(function (a, b) {
          var oa = typeof a.order === 'number' ? a.order : Infinity;
          var ob = typeof b.order === 'number' ? b.order : Infinity;
          return oa !== ob ? (oa < ob ? -1 : 1) : a.name.localeCompare(b.name, 'vi');
        });
        return games;
      });
  }

  function statusOf(g) { return g.status || 'visible'; }
  function shown(g) { return statusOf(g) !== 'hidden'; }

  function picture(g, size) {
    var img = el('img', 'pic');
    img.alt = '';
    img.width = size;
    img.height = size;
    img.loading = 'lazy';
    if (g.image) img.src = g.image; else img.classList.add('placeholder');
    img.onerror = function () { img.removeAttribute('src'); img.classList.add('placeholder'); };
    return img;
  }

  function card(g) {
    var li = el('li', 'card');
    var a = link(null, '/' + g.id + '/');
    a.appendChild(picture(g, 72));
    var body = el('div', 'card-body');
    body.appendChild(el('h3', null, g.name));
    body.appendChild(el('p', null, g.description.length > 160 ? g.description.slice(0, 159) + '…' : g.description));
    var chips = el('p', 'chips');
    if (isText(g.players)) chips.appendChild(el('span', 'chip', g.players));
    if (g.live) {
      var live = el('span', 'chip live');
      live.setAttribute('data-live', g.live);
      chips.appendChild(live);
    }
    if (statusOf(g) === 'maintenance') chips.appendChild(el('span', 'chip badge', 'Đang bảo trì'));
    if (chips.childNodes.length) body.appendChild(chips);
    a.appendChild(body);
    li.appendChild(a);
    return li;
  }

  /** Sidebar link to the ranking of the first game that has one. */
  function wireRankingLink(games) {
    var g = games.filter(function (x) { return shown(x) && x.live === 'mobiarmy'; })[0];
    Array.prototype.forEach.call(document.querySelectorAll('[data-ranking-link]'), function (a) {
      if (!g) return;
      a.href = '/' + g.id + '/#bang-xep-hang';
      a.hidden = false;
    });
  }

  // --- API (contracts/portal-api.md) ---

  function api(method, path, body) {
    var opts = { method: method, credentials: 'same-origin', headers: {} };
    if (body !== undefined) {
      opts.headers['Content-Type'] = 'application/json';
      opts.body = JSON.stringify(body);
    }
    return fetch(path, opts).then(function (r) {
      return r.text().then(function (t) {
        var data = null;
        try { data = t ? JSON.parse(t) : null; } catch (e) { data = null; }
        return { status: r.status, data: data };
      });
    }, function () { return { status: 0, data: null }; });
  }

  function errorText(res) {
    if (res.data && res.data.message) return res.data.message;
    if (res.status === 0 || res.status === 502 || res.status === 504) return 'Máy chủ tạm dừng, vui lòng thử lại sau.';
    return 'Có lỗi xảy ra, vui lòng thử lại.';
  }

  // --- live MobiArmy info (US4) ---

  function updateLive() {
    var targets = document.querySelectorAll('[data-live="mobiarmy"]');
    if (!targets.length || document.visibilityState === 'hidden') return;
    api('GET', '/api/mobiarmy/status').then(function (res) {
      var ok = res.status === 200 && res.data && typeof res.data.online === 'number';
      var text = ok ? res.data.online + ' người đang chơi' : 'Máy chủ tạm dừng';
      Array.prototype.forEach.call(targets, function (t) {
        t.textContent = t.getAttribute('data-prefix') && ok ? t.getAttribute('data-prefix') + text : text;
        t.classList.toggle('offline', !ok);
      });
    });
  }

  function startLive() {
    updateLive();
    setInterval(updateLive, LIVE_REFRESH_MS);
    document.addEventListener('visibilitychange', updateLive);
  }

  function showRanking(section) {
    section.hidden = false;
    var msg = $('.msg', section);
    var tbody = $('tbody', section);
    api('GET', '/api/mobiarmy/ranking').then(function (res) {
      tbody.textContent = '';
      if (res.status !== 200 || !res.data || !Array.isArray(res.data.entries)) {
        msg.textContent = 'Máy chủ tạm dừng, chưa xem được bảng xếp hạng.';
      } else {
        msg.textContent = res.data.entries.length ? '' : 'Chưa có người chơi nào.';
        res.data.entries.forEach(function (e) {
          var tr = el('tr', e.rank <= 3 ? 'top' + e.rank : null);
          [e.rank, e.name, e.level, e.exp].forEach(function (v) { tr.appendChild(el('td', null, String(v))); });
          tbody.appendChild(tr);
        });
      }
      if (location.hash === '#bang-xep-hang') section.scrollIntoView();
    });
  }

  // --- account (US2): sidebar box + dialog ---

  var me = null;

  function avatar(name) {
    return el('span', 'avatar', name ? name.charAt(0).toUpperCase() : '?');
  }

  function renderAccount() {
    var box = $('#account');
    if (!box) return;
    box.textContent = '';
    var who = el('div', 'who');
    var av = avatar(me);
    if (me) {
      var al = link(null, '#tai-khoan', 'avatar-link');
      al.title = 'Đổi mật khẩu';
      al.appendChild(av);
      av = al;
    }
    who.appendChild(av);
    who.appendChild(el('span', 'name', me || 'Khách'));
    box.appendChild(who);
    var actions = el('div', 'actions');
    if (me) {
      actions.appendChild(link('Đổi mật khẩu', '#tai-khoan', 'btn-small ghost wide-only'));
      var out = el('button', 'btn-small ghost', 'Đăng xuất');
      out.type = 'button';
      out.onclick = function () {
        api('POST', '/api/logout', {}).then(function () { me = null; renderAccount(); });
      };
      actions.appendChild(out);
    } else {
      actions.appendChild(link('Đăng nhập', '#dang-nhap', 'btn-small'));
      actions.appendChild(link('Đăng ký', '#dang-ky', 'btn-small ghost wide-only'));
    }
    box.appendChild(actions);
  }

  function loadMe() {
    return api('GET', '/api/me').then(function (res) {
      me = res.status === 200 && res.data ? res.data.username : null;
      renderAccount();
    });
  }

  var NAME_HINT = '3–20 ký tự, chỉ gồm chữ thường a–z và số.';
  var PASS_HINT = '6–50 ký tự, chỉ gồm chữ và số.';
  var SAME_LOGIN = 'Tên đăng nhập và mật khẩu này cũng dùng để đăng nhập trong game.';

  // One entry per dialog view; '#…' is the address that opens it.
  var VIEWS = {
    '#chao-mung': { welcome: true },
    '#dang-nhap': {
      title: 'Đăng nhập', path: '/api/login', submit: 'Đăng nhập',
      fields: [
        { name: 'username', label: 'Tên đăng nhập', auto: 'username', max: 20 },
        { name: 'password', label: 'Mật khẩu', type: 'password', auto: 'current-password', max: 50 }
      ],
      foot: [['Chưa có tài khoản? ', 'Đăng ký', '#dang-ky'], ['Quên mật khẩu? Liên hệ quản trị viên.']]
    },
    '#dang-ky': {
      title: 'Tạo tài khoản', path: '/api/register', submit: 'Tạo tài khoản',
      fields: [
        { name: 'username', label: 'Tên đăng nhập', auto: 'username', min: 3, max: 20, hint: NAME_HINT },
        { name: 'password', label: 'Mật khẩu', type: 'password', auto: 'new-password', min: 6, max: 50, hint: PASS_HINT }
      ],
      foot: [['Đã có tài khoản? ', 'Đăng nhập', '#dang-nhap']]
    },
    '#tai-khoan': {
      title: 'Đổi mật khẩu', path: '/api/password', submit: 'Đổi mật khẩu', needsLogin: true,
      fields: [
        { name: 'currentPassword', label: 'Mật khẩu hiện tại', type: 'password', auto: 'current-password', max: 50 },
        { name: 'newPassword', label: 'Mật khẩu mới', type: 'password', auto: 'new-password', min: 6, max: 50, hint: PASS_HINT }
      ],
      foot: [['Mật khẩu mới áp dụng cho cả trò chơi.']]
    }
  };

  var BENEFITS = [
    ['ico-key', 'Một tài khoản cho mọi trò chơi'],
    ['ico-star', 'Giữ cấp độ và kinh nghiệm sau mỗi trận'],
    ['ico-rank', 'Có tên trên bảng xếp hạng'],
    ['ico-phone', 'Chơi trên máy tính lẫn điện thoại']
  ];

  var dialog = null;

  function closeDialog() {
    if (!dialog) return;
    dialog.remove();
    dialog = null;
    document.body.classList.remove('modal-open');
    if (VIEWS[location.hash]) history.replaceState(null, '', location.pathname + location.search);
  }

  function welcomeView(box) {
    store(WELCOME_KEY, '1');
    box.appendChild(el('p', 'kicker', 'Chào bạn mới'));
    box.appendChild(el('h2', null, 'Tạo tài khoản để chơi trọn vẹn'));
    var ul = el('ul', 'benefits');
    BENEFITS.forEach(function (b) {
      var li = el('li');
      var icon = el('span', 'ibox');
      icon.appendChild(el('span', 'ico ' + b[0]));
      li.appendChild(icon);
      li.appendChild(el('span', null, b[1]));
      ul.appendChild(li);
    });
    box.appendChild(ul);
    box.appendChild(link('Tạo tài khoản', '#dang-ky', 'btn block'));
    box.appendChild(link('Đăng nhập', '#dang-nhap', 'btn block secondary'));
    var later = el('button', 'linklike later', 'Xem trước, để sau');
    later.type = 'button';
    later.onclick = closeDialog;
    box.appendChild(later);
  }

  function formView(box, view) {
    box.appendChild(el('h2', null, view.title));
    var form = el('form');
    form.noValidate = false;
    view.fields.forEach(function (f) {
      var label = el('label', null, f.label);
      var input = el('input');
      input.name = f.name;
      input.type = f.type || 'text';
      input.autocomplete = f.auto;
      input.required = true;
      if (f.min) input.minLength = f.min;
      input.maxLength = f.max;
      label.appendChild(input);
      form.appendChild(label);
      if (f.hint) form.appendChild(el('p', 'hint', f.hint));
    });
    var msg = el('p', 'msg');
    msg.setAttribute('role', 'alert');
    form.appendChild(msg);
    var button = el('button', 'btn block', view.submit);
    button.type = 'submit';
    form.appendChild(button);
    form.addEventListener('submit', function (ev) {
      ev.preventDefault();
      var body = {};
      Array.prototype.forEach.call(form.elements, function (f) { if (f.name) body[f.name] = f.value; });
      msg.textContent = '';
      button.disabled = true;
      api('POST', view.path, body).then(function (res) {
        button.disabled = false;
        if (res.status < 200 || res.status >= 300) { msg.textContent = errorText(res); return; }
        if (view.path === '/api/password') {
          closeDialog();
          setStatus('Đã đổi mật khẩu. Lần đăng nhập game sau, hãy dùng mật khẩu mới.');
          return;
        }
        me = res.data && res.data.username;
        renderAccount();
        closeDialog();
        setStatus('Xin chào ' + me + '! Chọn một trò chơi để bắt đầu. ' + SAME_LOGIN);
      });
    });
    box.appendChild(form);
    view.foot.forEach(function (line) {
      var p = el('p', 'hint', line[0]);
      if (line[1]) p.appendChild(link(line[1], line[2]));
      box.appendChild(p);
    });
    if (view.path !== '/api/password') box.appendChild(el('p', 'hint', SAME_LOGIN));
  }

  function openDialog(hash) {
    var view = VIEWS[hash];
    if (view.needsLogin && !me) { hash = '#dang-nhap'; view = VIEWS[hash]; }
    if (dialog) dialog.remove();
    dialog = el('div', 'modal');
    dialog.addEventListener('click', function (ev) { if (ev.target === dialog) closeDialog(); });
    var box = el('div', 'dialog');
    box.setAttribute('role', 'dialog');
    box.setAttribute('aria-modal', 'true');
    var close = el('button', 'close', '×');
    close.type = 'button';
    close.setAttribute('aria-label', 'Đóng');
    close.onclick = closeDialog;
    box.appendChild(close);
    if (view.welcome) welcomeView(box); else formView(box, view);
    dialog.appendChild(box);
    document.body.appendChild(dialog);
    document.body.classList.add('modal-open');
    var first = $('input', box) || $('.btn', box);
    if (first) first.focus();
  }

  function route() {
    if (VIEWS[location.hash]) openDialog(location.hash); else closeDialog();
  }

  function initDialog() {
    window.addEventListener('hashchange', route);
    document.addEventListener('keydown', function (ev) { if (ev.key === 'Escape') closeDialog(); });
  }

  // --- pages ---

  function homePage(games) {
    var list = $('#games');
    var visible = games.filter(shown);
    if (!visible.length) { setStatus('Chưa có trò chơi.'); return; }
    visible.forEach(function (g) { list.appendChild(card(g)); });

    // Featured banner: the first playable game.
    var f = visible.filter(function (g) { return statusOf(g) === 'visible'; })[0];
    if (f) {
      var banner = $('#featured');
      banner.href = '/' + f.id + '/';
      var img = $('img', banner);
      if (f.image) img.src = f.image; else img.remove();
      $('strong', banner).textContent = 'Vào trận ' + f.name;
      $('small', banner).textContent = isText(f.tagline) ? f.tagline : f.description;
      banner.hidden = false;
    }
    var live = visible.filter(function (g) { return g.live; })[0];
    if (live) {
      var online = $('#online');
      var span = $('.live', online);
      span.setAttribute('data-live', live.live);
      span.setAttribute('data-prefix', live.name + ': ');
      online.hidden = false;
    }
  }

  function gamePage(games) {
    var id = location.pathname.replace(/^\/|\/$/g, '');
    var g = games.filter(function (x) { return x.id === id && shown(x); })[0];
    if (!g) {
      document.title = 'Không tìm thấy trò chơi – Cổng game';
      $('#g-missing').hidden = false;
    } else {
      document.title = 'Chơi ' + g.name + ' online miễn phí – Cổng game';
      $('#g-name').textContent = g.name;
      $('#g-tagline').textContent = isText(g.tagline) ? g.tagline : g.description;
      var players = $('#g-players');
      if (isText(g.players)) players.textContent = g.players; else players.remove();
      var img = $('#g-img');
      var pic = picture(g, 96);
      pic.id = 'g-img';
      img.parentNode.replaceChild(pic, img);
      var live = $('#game .live');
      if (g.live) live.setAttribute('data-live', g.live); else live.remove();
      var play = $('#g-play');
      if (statusOf(g) === 'maintenance') {
        play.parentNode.hidden = true;
        $('#g-maint').hidden = false;
      } else {
        play.href = '/' + g.id + '/choi/';
      }
      var how = $('#cach-choi');
      (Array.isArray(g.howToPlay) ? g.howToPlay : []).forEach(function (s) {
        if (!s || !isText(s.title) || !isText(s.text)) return;
        how.appendChild(el('h2', 'section-title', s.title));
        s.text.split(/\n\s*\n/).forEach(function (para) { how.appendChild(el('p', null, para)); });
      });
      if (g.live === 'mobiarmy') showRanking($('#bang-xep-hang'));
      $('#game').hidden = false;
    }
    var others = games.filter(function (x) { return shown(x) && (!g || x.id !== g.id); });
    var section = $('#g-others');
    var ul = $('ul', section);
    others.forEach(function (x) { ul.appendChild(card(x)); });
    $('.empty', section).hidden = others.length > 0;
    section.hidden = false;
  }

  // Play page /<id>/choi/: the game itself (playUrl) in a same-site frame, inside the portal layout.
  function playPage(games) {
    var id = location.pathname.split('/')[1];
    var g = games.filter(function (x) { return x.id === id && statusOf(x) === 'visible'; })[0];
    if (!g) {
      setStatus('Không tìm thấy trò chơi, hoặc trò chơi đang bảo trì.');
      $('#leave').textContent = 'Về trang chủ';
      return;
    }
    document.title = g.name + ' – Cổng game';
    $('#p-name').textContent = g.name;
    $('#leave').href = '/' + g.id + '/';
    var live = $('.stage-top .live');
    if (g.live) live.setAttribute('data-live', g.live); else live.remove();

    var frame = $('#frame');
    // Keeps keyboard input in the game after clicks on the portal around it.
    function focusGame() {
      try {
        frame.contentWindow.focus();
        var screen = frame.contentDocument && frame.contentDocument.getElementById('screen');
        if (screen) screen.focus();
      } catch (e) { /* not same-site: the player clicks the game instead */ }
    }
    frame.addEventListener('load', focusGame);
    $('.stage').addEventListener('click', function (ev) {
      if (!ev.target.closest('a, button, .keys-panel')) focusGame();
    });
    frame.src = g.playUrl + location.search;
    frame.hidden = false;

    $('#btn-keys').onclick = function () {
      var panel = $('#keys');
      panel.hidden = !panel.hidden;
      this.setAttribute('aria-pressed', String(!panel.hidden));
      focusGame();
    };
    $('#btn-full').onclick = function () {
      var stage = $('.stage');
      if (document.fullscreenElement) document.exitFullscreen().then(focusGame, focusGame);
      else if (stage.requestFullscreen) stage.requestFullscreen().then(focusGame, focusGame);
    };
  }

  function init() {
    var page = document.body.getAttribute('data-page');
    initDialog();
    loadMe().then(function () {
      if (VIEWS[location.hash]) route();
      else if (page === 'home' && !me && !store(WELCOME_KEY)) openDialog('#chao-mung');
    });
    loadCatalog().then(function (games) {
      setStatus('');
      wireRankingLink(games);
      if (page === 'game') gamePage(games);
      else if (page === 'play') playPage(games);
      else homePage(games);
      startLive();
    }, function (err) {
      console.warn('games.json could not be loaded:', err);
      setStatus('Chưa có trò chơi. (Không đọc được danh sách trò chơi, hãy thử tải lại trang.)');
    });
  }

  init();
})();
