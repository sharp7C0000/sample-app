import * as Cookie from "service/cookie";

const STORE_AUTH_TOKEN   = "STORE_AUTH_TOKEN";
const SET_AUTH_TOKEN     = "SET_AUTH_TOKEN";
const DISCARD_AUTH_TOKEN = "DISCARD_AUTH_TOKEN";

const state = function () {
  return {
    authToken: null
  }
}

const actions = {
  storeAuthToken ({state}) {
    Cookie.set("authToken", state.authToken);
  },

  setAuthToken ({commit, state, dispatch}, authToken) {
    commit(SET_AUTH_TOKEN, authToken);
  },

  discardAuthToken ({commit, state, dispatch}) {
    Cookie.remove("authToken");
    commit(DISCARD_AUTH_TOKEN);
  }
}

const getters = {
  isAuthed (state) {
    return state.authToken;
  }
}

const mutations = {
  [SET_AUTH_TOKEN] (state, authToken) {
    state.authToken = authToken;
  },

  [DISCARD_AUTH_TOKEN] (state) {
    state.authToken = null;
  }
}

export default {
  state, getters, actions, mutations
}