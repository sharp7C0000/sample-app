export const status = {
  INITIAL: "initial",
  SUCCESS: "success",
  LOADING: "loading",
  FAIL   : "fail"
}

export default class Request {
  constructor() {
    this.status = status.INITIAL;
    this.error  = null;
  }
}