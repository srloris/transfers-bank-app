<template>
  <div class="card p-3 mb-4">
    <h5>Schedule Transfer</h5>
    <form @submit.prevent="create">

      <div class="mb-3">
        <label class="form-label">Source Account</label>
        <input v-model="form.sourceAccount" type="text" class="form-control" required />
        <div v-if="fieldErrors.sourceAccount" class="text-danger mt-1">
          {{ fieldErrors.sourceAccount }}
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Destination Account</label>
        <input v-model="form.destinationAccount" type="text" class="form-control" required />
        <div v-if="fieldErrors.destinationAccount" class="text-danger mt-1">
          {{ fieldErrors.destinationAccount }}
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Amount</label>
        <input v-model="form.amount" type="number" step="0.01" class="form-control" required />
        <div v-if="fieldErrors.amount" class="text-danger mt-1">
          {{ fieldErrors.amount }}
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">Transfer Date</label>
        <input v-model="form.transferDate" type="date" class="form-control" required />
        <div v-if="fieldErrors.transferDate" class="text-danger mt-1">
          {{ fieldErrors.transferDate }}
        </div>
      </div>

      <button class="btn btn-primary" type="submit">Schedule</button>
    </form>

    <div v-if="generalMessage" class="alert mt-3" :class="alertClass">
      {{ generalMessage }}
    </div>
  </div>
</template>

<script>
import api from "../api/transferApi.js";

export default {
  data() {
    return {
      form: {
        sourceAccount: "",
        destinationAccount: "",
        amount: null,
        transferDate: "",
      },
      fieldErrors: {},
      generalMessage: "",
      success: false,
    };
  },
  computed: {
    alertClass() {
      return this.success ? "alert-success" : "alert-danger";
    },
  },
  methods: {
    async create() {
      this.fieldErrors = {};
      this.generalMessage = "";

      try {
        await api.create(this.form);
        this.generalMessage = "Transfer successfully scheduled!";
        this.success = true;
        this.$emit("refresh");
      } catch (err) {
        this.success = false;

        const responseData = err.response?.data;
        if (responseData?.errors) {

          responseData.errors.forEach(e => {
            this.fieldErrors[e.fieldName] = e.message;
          });
        } else {
          this.generalMessage = responseData?.error || "Error while scheduling transfer.";
        }
      }
    },
  },
};
</script>
