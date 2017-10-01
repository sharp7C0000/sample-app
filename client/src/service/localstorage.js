const STORE_PREFIX = "gm_";

function trimedKey (key) {
  return `${STORE_PREFIX}${key}`;
}

export function save(key, value) {
  window.localStorage.setItem(trimedKey(key), value);
}

export function get(key) {
  window.localStorage.getItem(trimedKey(key));
}

export function remove(key) {
  window.localStorage.removeItem(trimedKey(key));
}