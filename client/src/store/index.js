import Vue from 'vue';
import Vuex from 'vuex';

import * as Api from "api";

import LoginModule from "./modules/login";

import * as Localstorage from "service/localStorage";

Vue.use(Vuex);

export function createStore () {
  return new Vuex.Store({
    
    state: {
      
    },

    actions: {

      callApi ({dispatch}, {name, options}) {
        const apiInfo = Api[name];
        if(apiInfo.auth) {
          return Api.authedFetch(Object.assign({}, {
            url   : apiInfo.url,
            method: apiInfo.method
          }, options))
        } else {
          return Api.fetch(Object.assign({}, {
            url   : apiInfo.url,
            method: apiInfo.method
          }, options))
        }
      },

      init ({dispatch, commit}) {
        dispatch("callApi", {
          name: "init"
        })
        .then((result) => {
          console.log("!!!!!", result);
        })
        .catch((error) => {
          console.log("%%%%%%", error);
        })
      }
    
    },

    modules: {
      login: LoginModule
    } 

  })
}