<template>
    <div class="recommend-page">
        <div class="navbar">
            <h3 class="navbar-font">好友推荐</h3>
        </div>
        <div  class="friendRecommend-list">
            <div v-if="friendRecommends.length > 0" v-for="recommend in friendRecommends" :key="recommend.id" class="friendRecommend-item">
                <div class="user-info">
                    <el-avatar :src="recommend.avatar" size="medium" shape="square"></el-avatar>
                    <span class="username">{{ recommend.name }}</span>
                </div>
                <el-button type="primary" @click="sendFriendRequest(recommend)" class="add-friend-request" :disabled="recommend.requested">
                    {{ recommend.requested ? '已发送' : '发送申请'}}
                </el-button>
            </div>
        </div>
    </div>
  <!--发送好友申请失败错误消息提示框 -->
    <div v-if="failedApplicationErrorMessage" class="failed-application-error-message">
        <el-icon class="error-icon" size="large"><WarningFilled /></el-icon>
        {{ failedApplicationErrorMessage }}
    </div>
  <!-- 发送好友申请成功正确消息提示框 -->
    <div v-if="successfulApplicationCorrectMessage" class="successful-application-correct-message">
        <el-icon class="correct-icon" size="large"><SuccessFilled /></el-icon>
        {{ successfulApplicationCorrectMessage }}
    </div>
</template>
<script>
import { ref, onMounted, defineComponent } from 'vue';
import request from "@/utils/request";
import {SuccessFilled, WarningFilled} from "@element-plus/icons-vue";

export default defineComponent({
    name: 'RecommendPage',
    components: {WarningFilled, SuccessFilled},
    setup() {
        const friendRecommends = ref([]);
        const failedApplicationErrorMessage = ref('');
        const successfulApplicationCorrectMessage = ref('');
        const loadFriendRecommends = () => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request
                .get("/api/recommand/friend", {
                    params: {
                        uid: temporaryUser.uid
                    }
                })
                .then(res => {
                    if(res.data.length!=0){
                        friendRecommends.value = res.data;
                    }
                    else{
                        friendRecommends.value = [];
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        };

        const sendFriendRequest = (recommend) => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request
                .post('/api/friendsModule/sendFriendRequest',{},{
                    params: {
                        uid: temporaryUser.uid,
                        fid: recommend.id
                    }
                })
                .then(res => {
                    recommend.requested=true;
                    // 发送好友申请成功，显示成功消息
                    successfulApplicationCorrectMessage.value = '发送申请成功';
                    // 清空错误消息
                    setTimeout(() => {
                        successfulApplicationCorrectMessage.value = '';
                    }, 1500);
                })
                .catch(error => {
                    console.error(error);
                    // 发送好友申请失败，显示失败消息
                    failedApplicationErrorMessage.value = '发送申请失败，请重新申请';
                    // 清空错误消息
                    setTimeout(() => {
                        failedApplicationErrorMessage.value = '';
                    }, 1500);
                });
        };
        onMounted(() => {
            loadFriendRecommends();
        });

        return {
            friendRecommends,
            failedApplicationErrorMessage,
            successfulApplicationCorrectMessage,
            sendFriendRequest,
        };
    },
});
</script>

<style scoped>
.recommend-page {
    width:290px;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.navbar {
    width: 290px;
    height: 60px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    background-color: rgba(255, 255, 255, 0.4);
    /*background-color: rgba(21, 159, 255, 0.1);*/
}
.navbar-font{
    font-size:15px;
    margin-right: 10px;
    font-weight: bold;
}
.friendRecommend-list {
    width: 290px;
    padding: 5px;
    height: 622px;
    overflow-y: auto;
}
.friendRecommend-list::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.friendRecommend-list::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.friendRecommend-list::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}
.friendRecommend-list::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}
.friendRecommend-item {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
    border-bottom: 1px solid #ddd;
}
.user-info {
    display: flex;
    align-items: center;
}
.username {
    margin-left: 10px;
    font-size:10px;
}

.add-friend-request {
    margin-left:10px;
    width: 60px;
    font-size: 10px;
    white-space: nowrap; /* 不换行 */
    overflow: hidden; /* 溢出部分隐藏 */
    text-overflow: ellipsis; /* 显示省略号 */
}
.failed-application-error-message{
    position: absolute;
    top: 60px;
    left: 60px;
    z-index: 9999;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15); /* 添加阴影效果 */
    border: 1px solid #ccc; /* 添加边线 */
    padding:10px;
    border-radius: 5px;
    background-color: rgba(243, 95, 95, 0.5);
    color: red; /* 设置文字颜色为红色 */
}
.successful-application-correct-message{
    position: absolute;
    top: 60px;
    left: 85px;
    z-index: 9999;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15); /* 添加阴影效果 */
    border: 1px solid #ccc; /* 添加边线 */
    padding:10px;
    border-radius: 5px;
    background-color: rgba(167, 227, 170, 0.8);
    color: rgba(34, 225, 34, 0.8); /* 设置文字颜色为绿色 */
}
</style>