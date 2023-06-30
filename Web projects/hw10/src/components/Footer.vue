<template>
    <footer>
      <p>Registered: {{ usersCount }} users</p>
      <p>Posts: {{ postsCount }}</p>
      <a href="/">Codeforces</a> &copy; 2022 by Mike Mirzayanov
    </footer>
</template>

<script>
export default {
  name: "Footer",
  props: ["usersCount", "postsCount"/*, "users"*/]
}
</script>

<style scoped>

</style>