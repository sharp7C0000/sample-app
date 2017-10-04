const COOKIE_PREFIX = "gm_";

function trimedKey (key) {
  return `${COOKIE_PREFIX}${key}`;
}

var d = new Date();
d.setTime(d.getTime() + (30*24*60*60*1000));
const month = d.toUTCString();

export function set(key, value, expires = month) {
  var expires = "expires="+ expires;
  document.cookie = trimedKey(key) + "=" + value + ";" + expires + ";path=/";
}

export function get(key) {
  var name = trimedKey(key) + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
          c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length);
      }
  }
  return "";
}

export function remove(key) {
  set(key, '', 0);
}