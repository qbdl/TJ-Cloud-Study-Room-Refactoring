<template>
    <div>
        <router-view style="flex: 1" />
    </div>
</template>

<style></style>

<script>
import Aside from './components/Aside.vue';
import Header from './components/Header.vue';
import {ref} from "vue";
import router from "@/router";
import request from "@/utils/request";
export default {
    name: "Layout",
    components: {
        Header,
        Aside
    },
    mounted() {
        setInterval(this.checkLoginTime, 5000); // 每秒钟调用一次
    },
    provide() {
        return {
            isNightMode: ref(false),
            isInRoom: ref(false),
            isView: ref(false),
            isFriendShow: ref(false),
        };
    },
    methods: {
        checkLoginTime(){
            let userJson = sessionStorage.getItem("user");
            if (userJson) {
                let user = JSON.parse(userJson);
                // 将loginTime添加到请求头或请求参数中
                request.post("/api/checkLoginTime", user).then(res => {
                    if(res.code !== "0") {//被其他人登陆，给出提示，返回login页面，清空session
                        this.$message(
                            {type: "error", message: res.msg}
                        )
                        router.push("/login");
                        sessionStorage.clear();
                    }
                })
            } else if (this.$route.path !== "/register") {
                router.push("/login");
            }
        }
    }
}
</script>