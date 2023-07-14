<template>
    <div class="sidebar">
        <div class="user-info">
            <el-avatar class="avatar" :src=selfAvatar :size="40"></el-avatar>
        </div>
        <div class="select">
            <el-row>
                <el-button type="primary" circle @click="showUser">
                    <el-icon><User /></el-icon>
                </el-button >
            </el-row>
            <el-row>
                <el-button type="primary" circle @click="showChat">
                    <el-icon><ChatDotRound /></el-icon>
                </el-button >
            </el-row>
            <el-row>
                <el-button type="primary" circle @click="showNotifications">
                    <el-icon><Bell /></el-icon>
                </el-button >
            </el-row>
            <el-row>
                <el-button type="primary" circle @click="showRecommends">
                    <el-icon><Star /></el-icon>
                </el-button >
            </el-row>
        </div>
    </div>
</template>

<script>
import { defineComponent, ref } from 'vue';
import { ElAvatar, ElButton, ElIcon } from 'element-plus';
import {Bell, ChatDotRound, Connection, Star, User} from "@element-plus/icons-vue";

export default defineComponent({
    name: 'Sidebar',
    components: {
        Star,
        Connection,
        User,
        ChatDotRound,
        Bell,
        ElAvatar,
        ElButton,
        ElIcon
    },
    computed:{
        selfAvatar(){
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            return temporaryUser.picture;
        }

    },
    methods: {
        showUser() {
            this.$emit('change-content', 'user');
        },
        showChat() {
            this.$emit('change-content', 'chat');
        },
        showNotifications() {
            this.$emit('change-content', 'notifications');
        },
        showRecommends(){
            this.$emit('change-content', 'recommend');
        }
    },
});
</script>

<style scoped>
.sidebar {
    display: flex;
    flex-direction: column;
    text-align: center;
    width: 60px;
    height: 100%;
    /*background-color: #9ad5ff; !* 浅蓝色 *!*/
    background-color: rgb(255, 255, 255,0.7);
    padding-top: 10px;
    align-items: center;
}

.user-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    margin-bottom: 30px;
}

.avatar {
    margin-bottom: 10px; /*和下面的元素离多远*/
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
}
.select {
    display: grid;
    grid-template-rows: repeat(4, auto);
    place-items: center;
    width: 100%;
    margin-top: 80px;
}

.select > * + * {
    margin-top: 40px; /* 设置按钮之间的间距 */
}
</style>

