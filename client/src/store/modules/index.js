const LOGIN_REQUEST = "LOGIN_REQUEST";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGIN_FAIL    = "LOGIN_FAIL";

import axios from "axios";

const state = function () {
  return {
    loginRequest: null
  }
}

const actions = {
  login ({commit, state}, {oauthToken, oauthSecret}) {
    
    return new Promise((resolve, reject) => {

      commit(LOGIN_REQUEST);
      
      axios.post('http://0.0.0.0:8080/api/auth/authorize', {
      // oauthToken : route.query.oauth_token,
      // oauthSecret: route.query.oauth_secret
      })
      .then((response) => {
        // router.push({
        //   name: "main"
        // })

        commit(LOGIN_SUCCESS);
        resolve();
      })
      .catch((error) => {
        console.log("!!!!", error)
        // router.push({
        //   name: "index"
        // });

        commit(LOGIN_FAIL);
        resolve();
      });
    })
  }
}

const mutations = {
  [LOGIN_REQUEST] (state) {
    state.loginRequest = "loading";
  },

  [LOGIN_SUCCESS] (state) {
    state.loginRequest = "success";
  },

  [LOGIN_FAIL] (state, error) {
    state.loginRequest = "error";
  }
}

const getters = {

}



export default {
  state, actions, mutations, getters
}