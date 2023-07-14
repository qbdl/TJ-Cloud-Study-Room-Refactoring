<template>
    <div style="height: 50px; line-height: 40px; border-bottom: 1px solid #ccc; display: flex">
        <div style="width: 200px; padding-left: 10px; padding-top: 4px; font-weight: bold; color: #3f9dfe;">
            后台管理系统
        </div>
        <img src="../assets/title.png" alt="" style="width: 12%; margin-left: 30vw"/>
        <div style="flex: 1;"></div>
        <el-dropdown style="padding-right: 30px; padding-top: 5px;">
            <el-button type="primary" round plain>
            管理员<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
                <el-dropdown-menu>
                <router-link to="/admin" style="text-decoration: none">
                    <el-dropdown-item>个人中心</el-dropdown-item>
                </router-link>
                <el-dropdown-item>消息处理</el-dropdown-item>
                <el-dropdown-item @click="log_out">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script>
import { ArrowDown } from '@element-plus/icons-vue'
import request from "@/utils/request";
export default {
    name: "Header",
    components: {
        ArrowDown
    },
    methods: {
        log_out(){
            console.log("success")

            let userJson = sessionStorage.getItem( "user" )
            const user = JSON.parse(userJson)

            request.post("/api/header/logOut/" + user.uid).then(res => {
                if(res.code === "0") {
                    this.$message({type: "success", message: "退出登陆成功"})
                    sessionStorage.removeItem("user");
                    sessionStorage.removeItem("room");
                    this.$router.push({path:'/login'})
                }
            })
        }

    }
}
</script>

<style scoped>

</style>