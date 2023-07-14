<template>
    <div class="userHeader">
        <img src="../assets/title.png" alt="" style="height: 60px; padding-left: 75px; user-select: none;"/>

        <div style="flex: 1;"></div>
        <el-tooltip :content="tooltipContent" :disabled="!showPopper">
            <el-icon class="nightIcon" :size="25" @mouseenter="showPopper = true" @click="toggleNightMode">
                <template v-if=this.isNightMode.value>
                    <Sunny />
                </template>
                <template v-else>
                    <Moon />
                </template>
            </el-icon>
        </el-tooltip>
        <el-text v-text="userName" class="name"></el-text>
        <el-dropdown  style="padding-right: 30px;">
            <el-avatar :size="40" :src="avatarUrl" style="outline: none;" />
            <template #dropdown>
                <el-dropdown-menu>
                    <router-link to="/userCenter" style="text-decoration: none">
                        <el-dropdown-item>个人中心</el-dropdown-item>
                    </router-link>
                    <el-dropdown-item>消息处理</el-dropdown-item>
                    <router-link to="/login" style="text-decoration: none">
                        <el-dropdown-item @click="log_out">退出登录</el-dropdown-item>
                    </router-link>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script>
import studyHall from '../views/studyHall.vue';
import {Moon, Search, Sunny} from '@element-plus/icons-vue'
import router from "@/router";
import request from "@/utils/request";
import {createRouter as $router} from "vue-router";

export default {
    name: "userHeader",
    inject: ['isNightMode'],
    mounted() {
        if(this.isNightMode.value){
            document.body.style.backgroundColor = '#111828';
            document.querySelector('.name').style.color = 'white';
            document.querySelector('.el-menu-vertical-demo').style.backgroundColor = '#111828';
            document.querySelector('.el-menu-vertical-demo').style.borderColor = '#111828';
        }
        else{
            document.body.style.backgroundColor = '#f1f4fc';
            document.querySelector('.name').style.color = 'black';
            document.querySelector('.el-menu-vertical-demo').style.backgroundColor = '#f1f4fc';
            document.querySelector('.el-menu-vertical-demo').style.borderColor = '#f1f4fc';
        }
    },
    created() {
        // let userJson = sessionStorage.getItem("user")
        // if(!userJson) {
        //     router.push("/login")
        // }
        this.load()
    },
    computed: {
        Search() {
            return Search
        },
        tooltipContent() {
            return this.isNightMode.value ? '白天模式' : '黑夜模式'
        }
    },
    components: {
        Sunny,
        Moon,
        Search
    },

    data(){
        return{
            avatarUrl: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
            userName: "TestTest",
            showPopper: false,
        }
    },

    methods: {
        toggleNightMode() {
            this.isNightMode.value = !this.isNightMode.value;
            if (this.isNightMode.value) {
                // 切换到黑夜模式
                document.body.style.backgroundColor = '#111828';
                document.querySelector('.name').style.color = 'white';
                document.querySelector('.el-menu-vertical-demo').style.backgroundColor = '#111828';
                document.querySelector('.el-menu-vertical-demo').style.borderColor = '#111828';
            } else {
                // 切换到白天模式
                document.body.style.backgroundColor = '#f1f4fc';
                document.querySelector('.name').style.color = 'black';
                document.querySelector('.el-menu-vertical-demo').style.backgroundColor = '#f1f4fc';
                document.querySelector('.el-menu-vertical-demo').style.borderColor = '#f1f4fc';
            }
        },
        load(){
            let userJson = sessionStorage.getItem("user")
            const temporaryUser = JSON.parse(userJson)
            this.userName = temporaryUser.userName
            this.avatarUrl = temporaryUser.picture
        },
        log_out(){
            console.log("success")

            let userJson = sessionStorage.getItem( "user" )
            const user = JSON.parse(userJson)

            request.put("/api/header/logOut/" + user.uid).then(res => {
                if(res.code === "0") {
                    this.$message({type: "success", message: "退出登录成功"})
                    sessionStorage.removeItem("user");
                    sessionStorage.removeItem("room");
                    this.$router.push({path:'/login'})
                }
            })
        },
    }
}
</script>

<style scoped>
.userHeader{
    height: 70px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0;
    padding: 0;
}

.name{
    padding-left: 30px;
    padding-right: 10px;
    user-select: none;
    font-weight: bold;
}
</style>