import {fetch as fetchApi} from "api";

import * as Request from "utils/request";

const LOGIN_REQUEST = "LOGIN_REQUEST";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGIN_FAIL    = "LOGIN_FAIL";

const state = function () {
  return {
    request: Request.generateState()
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
    state.request.status = "loading";
  },

  [LOGIN_SUCCESS] (state) {
    state.request.status = "success";
  },

  [LOGIN_FAIL] (state, error) {
    state.request.status = "fail";
    state.reuqest.error  = error;
  }
}

export default {
  state, actions, mutations
}