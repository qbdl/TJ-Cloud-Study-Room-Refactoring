<template>
    <div>
        <el-menu :default-active="this.$route.path"
                 class="el-menu-vertical-demo"
                 style="min-height: calc(100vh - 50px)"
                 router>
            <el-menu-item index="/admin/user">
                <el-icon><user-filled /></el-icon>
                <template #title>用户</template>
            </el-menu-item>
            <el-menu-item index="/admin/room">
                <el-icon><home-filled /></el-icon>
                <template #title>房间</template>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script>
import {
    UserFilled,
    HomeFilled
} from '@element-plus/icons-vue'
export default {
    name: "Aside",
    components: {
        UserFilled,
        HomeFilled
    },
}
</script>

<style scoped>
.el-menu-vertical-demo{
    width: 200px;
    min-height: 400px;
}
</style>