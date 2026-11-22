// On-screen phone keypad (002 spec US4, contracts/key-mapping.md), also the mouse "control pad" on desktop
// (specs/005-mouse-control US3). Sends MIDP key codes through window.mobiarmyKeypad(type, code), which the
// game installs when it starts.
//
// Shown by default on touch devices. ?keypad=1 / ?keypad=0 force it; otherwise the player's choice from the
// portal toggle (localStorage "mobiarmy.keypad" = on/off) wins. window.mobiarmyKeypadShow(on) and
// window.mobiarmyKeypadShown() let the portal play page switch it live, without reloading the game.
(function () {
  var STORE = 'mobiarmy.keypad';
  var params = new URLSearchParams(location.search);

  function stored() {
    try { return localStorage.getItem(STORE); } catch (e) { return null; }
  }

  function remember(on) {
    try { localStorage.setItem(STORE, on ? 'on' : 'off'); } catch (e) { /* storage blocked: not remembered */ }
  }

  var initial;
  if (params.get('keypad') === '1') initial = true;
  else if (params.get('keypad') === '0') initial = false;
  else if (stored() === 'on') initial = true;
  else if (stored() === 'off') initial = false;
  else initial = !!(window.matchMedia && window.matchMedia('(pointer: coarse)').matches);

  // [label, MIDP code, css class]
  var rows = [
    [['', -6, 'soft'], ['▲', -1, 'nav'], ['', -7, 'soft']],
    [['◀', -3, 'nav'], ['●', -5, 'fire'], ['▶', -4, 'nav']],
    [[' ', 0, 'gap'], ['▼', -2, 'nav'], [' ', 0, 'gap']],
    [['1', 49], ['2', 50], ['3', 51]],
    [['4', 52], ['5', 53], ['6', 54]],
    [['7', 55], ['8', 56], ['9', 57]],
    [['*', 42], ['0', 48], ['#', 35]]
  ];

  var pad = document.getElementById('keypad');
  var stops = [];
  rows.forEach(function (row) {
    row.forEach(function (k) {
      var b = document.createElement('button');
      b.type = 'button';
      b.textContent = k[0];
      b.className = 'key ' + (k[2] || 'digit');
      if (k[2] === 'gap') { b.disabled = true; b.setAttribute('aria-hidden', 'true'); }
      else { stops.push(bind(b, k[1])); b.setAttribute('aria-label', label(k)); }
      pad.appendChild(b);
    });
  });

  function label(k) {
    if (k[1] === -6) return 'Left soft key';
    if (k[1] === -7) return 'Right soft key';
    if (k[1] === -5) return 'Fire';
    return k[0];
  }

  function send(type, code) {
    if (window.mobiarmyKeypad) window.mobiarmyKeypad(type, code);
  }

  function bind(button, code) {
    var delay = null, repeat = null;
    function stop() {
      if (delay === null && repeat === null) return;
      clearTimeout(delay); clearInterval(repeat); delay = repeat = null;
      button.classList.remove('active');
      send('up', code);
    }
    button.addEventListener('pointerdown', function (e) {
      if (e.button !== undefined && e.button !== 0) return;
      e.preventDefault();
      button.setPointerCapture(e.pointerId);
      button.classList.add('active');
      send('down', code);
      // Like a held phone key: repeat after a short delay (the fire key builds power while held).
      delay = setTimeout(function () {
        repeat = setInterval(function () { send('repeat', code); }, 120);
      }, 400);
    });
    button.addEventListener('pointerup', stop);
    button.addEventListener('pointercancel', stop);
    button.addEventListener('lostpointercapture', stop);
    button.addEventListener('contextmenu', function (e) { e.preventDefault(); });
    return stop;
  }

  // Nothing may stay held when the window loses focus or the tab is hidden (spec 005 FR-008).
  function releaseAll() {
    stops.forEach(function (stop) { stop(); });
  }
  window.addEventListener('blur', releaseAll);
  document.addEventListener('visibilitychange', function () { if (document.hidden) releaseAll(); });

  function show(on) {
    if (!on) {
      releaseAll();
      if (window.mobiarmyReleaseAll) window.mobiarmyReleaseAll();
    }
    pad.hidden = !on;
    document.body.classList.toggle('with-keypad', on);
  }

  window.mobiarmyKeypadShow = function (on) {
    on = !!on;
    show(on);
    remember(on);
    var screen = document.getElementById('screen');
    if (screen) screen.focus();
  };
  window.mobiarmyKeypadShown = function () { return !pad.hidden; };

  show(initial);
})();
