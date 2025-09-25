<template>
  <div>
    <h5>Scheduled Transfers</h5>

    <table class="table table-striped">
      <thead>
        <tr>
          <th>From</th>
          <th>To</th>
          <th>Amount</th>
          <th>Fee</th>
          <th>Scheduled On</th>
          <th>Transfer Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in transfers" :key="t.id">
          <td>{{ t.sourceAccount }}</td>
          <td>{{ t.destinationAccount }}</td>
          <td>$ {{ format(t.amount) }}</td>
          <td>$ {{ format(t.fee) }}</td>
          <td>{{ new Date(t.scheduledDate).toLocaleString() }}</td>
          <td>{{ new Date(t.transferDate).toLocaleDateString() }}</td>
        </tr>
      </tbody>
    </table>

    <div class="d-flex justify-content-between align-items-center mt-2">
      <button class="btn btn-primary" @click="prevPage" :disabled="page === 0">Previous</button>
      <span>Page {{ page + 1 }} of {{ totalPages }}</span>
      <button class="btn btn-primary" @click="nextPage" :disabled="page + 1 >= totalPages">Next</button>
    </div>
  </div>
</template>

<script>
import api from "../api/transferApi.js";

export default {
  data() {
    return {
      transfers: [],
      page: 0,
      size: 5,
      totalPages: 0,
    };
  },
  methods: {
    async load() {
      const resp = await api.list(this.page, this.size);
      this.transfers = resp.data.content;
      this.totalPages = resp.data.totalPages;
    },
    format(value) {
      return Number(value).toFixed(2);
    },
    nextPage() {
      if (this.page + 1 < this.totalPages) {
        this.page++;
        this.load();
      }
    },
    prevPage() {
      if (this.page > 0) {
        this.page--;
        this.load();
      }
    },
  },
  mounted() {
    this.load();
  },
};
</script>
