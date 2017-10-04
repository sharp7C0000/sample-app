import * as Api from "api";

import Request                   from "classes/request";
import {status as requestStatus} from "classes/request";

const LOGIN_REQUEST = "LOGIN_REQUEST";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGIN_FAIL    = "LOGIN_FAIL";

const state = function () {
  return {
    request: new Request()
  }
}

const actions = {
  
  login ({commit, state, dispatch}, {oauthToken, oauthSecret}) {
    
    return new Promise((resolve, reject) => {

      commit(LOGIN_REQUEST);

      dispatch("callApi", {
        name: "login",
        options: {
          data: {
            oauthToken, oauthSecret
          }
        }
      })
      .then((result) => {
        dispatch("setAuthToken", result);
        commit(LOGIN_SUCCESS);
        resolve();
      })
      .catch((error) => {
        commit(LOGIN_FAIL, error);
        reject();
      })
    
    })
  }
}

const mutations = {
  [LOGIN_REQUEST] (state) {
    state.request.status = requestStatus.LOADING;
  },

  [LOGIN_SUCCESS] (state) {
    state.request.status = requestStatus.SUCCESS;
  },

  [LOGIN_FAIL] (state, error) {
    state.request.status = requestStatus.FAIL;
    state.request.error  = error;
  }
}

export default {
  state, actions, mutations
}