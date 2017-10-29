import * as Cookie from "service/cookie";

const SET_SERVER_TOKEN     = "SET_SERVER_TOKEN";
const DISCARD_SERVER_TOKEN = "DISCARD_SERVER_TOKEN";

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

  discardServerToken ({commit, state, dispatch}) {
    commit(DISCARD_SERVER_TOKEN);
  },

  discardAuthToken ({commit, state, dispatch}) {
    Cookie.remove("authToken");
  }
}

const getters = {
  isAuthed (state) {
    // try {
    //   return Cookie.get("authToken")
    // } catch (e) {
    //   console.log('$$$$$$$$$$', state.serverToken);
    //   return state.serverToken;
    // }
    //console.log("%%%%", state.serverToken)
    return state.serverToken;
  }
}

const mutations = {
  [SET_SERVER_TOKEN] (state, authToken) {
    state.serverToken = authToken;
  },

  [DISCARD_SERVER_TOKEN] (state) {
    state.serverToken = null;
  }
}

export default {
  state, getters, actions, mutations
}