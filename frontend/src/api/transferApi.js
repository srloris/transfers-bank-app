import axios from "axios";

const api = axios.create({
  baseURL: "/api/transfer"
});

export default {
  list(page, size) {
    return api.get(`/?size=${size}&page=${page}`);
  },
  create(transfer) {
    return api.post("/", transfer);
  },
};
