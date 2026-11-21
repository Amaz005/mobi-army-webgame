// On-screen phone keypad for touch devices (spec US4, contracts/key-mapping.md).
// Shown when the device's main pointer is coarse (touch), or with ?keypad=1. Sends MIDP key codes
// through window.mobiarmyKeypad(type, code), which the game installs when it starts.
(function () {
  var params = new URLSearchParams(location.search);
  var wanted = params.get('keypad') === '1' ||
    (params.get('keypad') !== '0' && window.matchMedia && window.matchMedia('(pointer: coarse)').matches);
  if (!wanted) return;

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
  rows.forEach(function (row) {
    row.forEach(function (k) {
      var b = document.createElement('button');
      b.type = 'button';
      b.textContent = k[0];
      b.className = 'key ' + (k[2] || 'digit');
      if (k[2] === 'gap') { b.disabled = true; b.setAttribute('aria-hidden', 'true'); }
      else { bind(b, k[1]); b.setAttribute('aria-label', label(k)); }
      pad.appendChild(b);
    });
  });
  pad.hidden = false;
  document.body.classList.add('with-keypad');

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
      e.preventDefault();
      button.setPointerCapture(e.pointerId);
      button.classList.add('active');
      send('down', code);
      // Like a held phone key: repeat after a short delay.
      delay = setTimeout(function () {
        repeat = setInterval(function () { send('repeat', code); }, 120);
      }, 400);
    });
    button.addEventListener('pointerup', stop);
    button.addEventListener('pointercancel', stop);
    button.addEventListener('contextmenu', function (e) { e.preventDefault(); });
  }
})();
