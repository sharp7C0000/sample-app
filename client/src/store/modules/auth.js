import * as Cookie from "service/cookie";

const SET_SERVER_TOKEN   = "SET_SERVER_TOKEN";
const STORE_AUTH_TOKEN   = "STORE_AUTH_TOKEN";
const DISCARD_AUTH_TOKEN = "DISCARD_AUTH_TOKEN";

const state = function () {
  return {
    serverToken: null
  }
}

const actions = {
  storeAuthToken ({state, commit}, authToken) {
    Cookie.set("authToken", authToken);
  },

  setServerToken ({commit, state, dispatch}, authToken) {
    commit(SET_SERVER_TOKEN, authToken);
  },

  discardAuthToken ({commit, state, dispatch}) {
    Cookie.remove("authToken");
    commit(DISCARD_AUTH_TOKEN);
  }
}

const getters = {
  isAuthed (state) {
    try {
      return Cookie.get("authToken")
    } catch (e) {
      return state.serverToken;
    }
  }
}

const mutations = {

  [SET_SERVER_TOKEN] (state, authToken) {
    state.serverToken = authToken;
  },

  [DISCARD_AUTH_TOKEN] (state) {
    state.authToken = null;
  }
}

export default {
  state, getters, actions, mutations
}