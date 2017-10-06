import * as Cookie from "service/cookie";

const SET_SERVER_TOKEN = "SET_SERVER_TOKEN";

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
  }
}

export default {
  state, getters, actions, mutations
}