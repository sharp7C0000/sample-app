import axios from "axios";

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
      if(error.data) {
        reject(error.data.message)
      } else {
        reject("server error");
      }
    })
  });
}