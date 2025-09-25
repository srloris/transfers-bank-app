<template>
  <div class="container mt-5">
    <h3 class="mb-4">Transfer System</h3>
    <TransferForm @refresh="refreshList" />
    <TransferList ref="list" />
  </div>
</template>

<script>
import TransferForm from "./components/TransferForm.vue";
import TransferList from "./components/TransferList.vue";

export default {
  components: { TransferForm, TransferList },
  methods: {
    refreshList() {
      this.$refs.list.load();
    },
  },
};
</script>
