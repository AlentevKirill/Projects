<template>
    <div class="middle">
        <Sidebar :posts="viewPosts"/>
        <main>
            <Index v-if="page === 'Index'"/>
            <Enter v-if="page === 'Enter'"/>
            <Users v-if="page === 'Users'" :users="users"/>
            <Register v-if="page === 'Register'"/>
            <WritePost v-if="page === 'WritePost'"/>
            <EditPost v-if="page === 'EditPost'"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./page/Index";
import Enter from "./page/Enter";
import WritePost from "./page/WritePost";
import EditPost from "./page/EditPost";
import Register from "@/components/page/Register";
import Users from "@/components/page/Users";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index"
        }
    },
    components: {
        Register,
        Users,
        WritePost,
        Enter,
        Index,
        Sidebar,
        EditPost
    },
    props: ["posts", "users"],
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
        }
    },
    beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
    }
}
</script>

<style scoped>

</style>
