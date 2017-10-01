import {fetch as fetchApi} from "api";

import Request                   from "classes/request";
import {status as requestStatus} from "classes/request";

import * as Localstorage from "service/localstorage";

const LOGIN_REQUEST = "LOGIN_REQUEST";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGIN_FAIL    = "LOGIN_FAIL";

const DELETE_TEMP_AUTH_TOKEN = "UPDATE_AUTH_TOKEN";

const state = function () {
  return {
    authToken: null,
    request  : new Request()
  }
}

const actions = {
  
  login ({commit, state}, {oauthToken, oauthSecret}) {
    
    return new Promise((resolve, reject) => {

      commit(LOGIN_REQUEST);

      fetchApi({
        url   : 'http://0.0.0.0:8080/api/auth/authorize',
        method: "post",
        data  : {
          oauthToken, oauthSecret
        }
      })
      .then((result) => {
        commit(LOGIN_SUCCESS, result);
        resolve();
      })
      .catch((error) => {
        commit(LOGIN_FAIL, error);
        reject();
      })
    
    })
  },

  updateAuthToken ({commit, state}) {
    // save token
    Localstorage.save("authToken", state.authToken);
    commit(DELETE_TEMP_AUTH_TOKEN); 
  }
}

const mutations = {
  [LOGIN_REQUEST] (state) {
    state.request.status = requestStatus.LOADING;
  },

  [LOGIN_SUCCESS] (state, authToken) {
    state.authToken      = authToken;
    state.request.status = requestStatus.SUCCESS;
  },

  [LOGIN_FAIL] (state, error) {
    state.request.status = requestStatus.FAIL;
    state.request.error  = error;
  },

  [DELETE_TEMP_AUTH_TOKEN] (state) {
    state.authToken = null;
  }
}

export default {
  state, actions, mutations
}