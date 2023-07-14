<template>
    <div>
        <el-menu :default-active="this.$route.path"
                 class="el-menu-vertical-demo"
                 router>
            <el-menu-item index="/studyHall" class="menu-item">
                <el-icon class="nightIcon" :size=100><house /></el-icon>
            </el-menu-item>
<!--            <el-menu-item index="/room" class="menu-item">-->
            <el-menu-item @click="checkInRoom" class="menu-item">
                <el-icon class="nightIcon" :size=100><user /></el-icon>
            </el-menu-item>
            <el-menu-item index="/soloStudy" class="menu-item">
                <el-icon class="nightIcon" :size=100><AlarmClock /></el-icon>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script>
import {AlarmClock, House, Search, User} from "@element-plus/icons-vue";
import router from "@/router";

export default {
    name: "userAside",
    computed: {
        Search() {
            return Search
        },
    },
    components: {AlarmClock, User, House},
    methods: {
        checkInRoom() {
            let roomInfo = JSON.parse(sessionStorage.getItem("room"));
            if (!roomInfo || !roomInfo.rid) {
                this.$router.push("/studyHall");
                this.$message(
                    {type: "error", message: "您还未进入房间！"}
                )
            }
            else
                this.$router.push("/room");
        }
    }
}
</script>

<style scoped>

.el-menu-vertical-demo{
    width: 75px;
    height: calc(100vh - 80px);
    padding-top: 40px;
    padding-left: 5px;
    background-color: #f1f4fc;
    border-color: #f1f4fc;
    position: fixed;
    top: 0;
    left: 0;
}

.menu-item{
    height: 80px;
}
</style>