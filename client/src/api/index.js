import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export function fetch ({
  url, method, data, headers
}) {
  return new Promise((resolve, reject) => {
    axios.request({
      url, method, data, headers
    })
    .then((resp) => {
      resolve(resp.data)
    })
    .catch((error) => {
      if(error.response.data) {
        reject({code: error.response.status, message: error.response.data.message})
      } else {
        reject({code: error.response.status, message: "Server not answer"});
      }
    })
  });
}

export function authedFetch ({
  url, method, data, headers, authToken
}) {
  return fetch({
    url, method, data, headers: Object.assign({}, {
      Authorization: `Bearer ${authToken}`
    }, headers)
  })
}

export const init = {
  url   : `${BASE_URL}/app/current`,
  method: "get",
  auth  : true
}

export const login = {
  url   : `${BASE_URL}/auth/authorize`,
  method: "post"
}