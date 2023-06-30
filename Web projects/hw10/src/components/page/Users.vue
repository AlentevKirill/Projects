<template>
  <div class="users datatable">
    <div class="caption">Users</div>
    <table>
      <thead>
      <tr>
        <th>Id</th>
        <th>Login</th>
        <th>Name</th>
      </tr>
      </thead>
      <tbody>
        <UserInTable v-for="user in users" :user="user" :key="user.id"/>
      </tbody>
    </table>
  </div>
</template>

<script>
import UserInTable from "@/components/page/UserInTable";

export default {
  name: "Users",
  components: {
    UserInTable
  },
  props: ["users"]
}
</script>

<style scoped>

</style>