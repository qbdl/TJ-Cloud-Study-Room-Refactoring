<template>
    <div class="notification-page">
        <div class="navbar">
            <img class="new-friend-icon" src="@/assets/phone.png" alt="New Friend">
        </div>
        <div  class="friendRequests-list">
            <h3>好友申请</h3>
            <div v-if="friendRequests" v-for="request in friendRequests.slice().reverse()" :key="request.id" class="notification-item" :class="getRequestClass(request.requestStatus)">
                <div class="user-info">
                    <el-avatar :src="request.avatar" size="medium" shape="square"></el-avatar>
                    <span class="username">{{ request.name }}</span>
                </div>
                <div v-if="request.requestStatus === 0">
                    <el-button
                            type="primary"
                            @click="approveRequest(request.id)"
                            class="agree-button"
                    >
                        同意
                    </el-button>
                    <el-button
                            type="danger"
                            @click="rejectRequest(request.id)"
                            class="reject-button"
                    >
                        拒绝
                    </el-button>
                </div>
                <div v-else-if="request.requestStatus === 1">
                    <span>已同意</span>
                </div>
                <div v-else-if="request.requestStatus=== -1">
                    <span>已拒绝</span>
                </div>
                <div class="request-time-wrapper">
                    <span class="request-time">{{ request.requestTime }}</span>
                </div>
            </div>
            <!--    没有好友申请时的友情提示信息-->
            <div v-else class="no-friendRequests">
                <div class="friendRequests-message">暂无好友申请</div>
            </div>
        </div>


        <div class="my-requests">
            <h3>我的申请</h3>
            <div v-if="myRequests"  v-for="request in myRequests.slice().reverse()" :key="request.id" class="notification-item" :class="getRequestClass(request.requestStatus)">
                <div class="user-info">
                    <el-avatar :src="request.avatar" size="medium" shape="square"></el-avatar>
                    <span class="username">{{ request.name }}</span>
                </div>
                <div v-if="request.requestStatus === 0">
                    <span>申请中</span>
                </div>
                <div v-else-if="request.requestStatus === 1">
                    <span>已同意</span>
                </div>
                <div v-else-if="request.requestStatus === -1">
                    <span>被拒绝</span>
                </div>
                <div class="request-time-wrapper">
                    <span class="request-time">{{ request.requestTime }}</span>
                </div>
            </div>
            <!--    没有我的申请时的友情提示信息-->
            <div v-else class="no-myRequests">
                <div class="myRequests-message">暂无我的申请</div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, defineComponent } from 'vue';
import request from "@/utils/request";

export default defineComponent({
    name: 'NotificationPage',
    setup() {
        let userJson = sessionStorage.getItem("user");
        const temporaryUser = JSON.parse(userJson);

        const friendRequests = ref([]);
        const myRequests = ref([]);

        const loadFriendRequests = () => {
            request
                .get("/api/friendsModule/getFriendRequests", {
                    params: {
                        uid: temporaryUser.uid
                    }
                })
                .then(res => {
                    if(res.data.length!=0){
                        friendRequests.value = res.data;
                    }
                    else{
                        friendRequests.value = null;
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        };

        const loadMyRequests = () => {
            request
                .get("/api/friendsModule/getMyRequests", {
                    params: {
                        uid: temporaryUser.uid
                    }
                })
                .then(res => {
                    if(res.data.length!=0){
                        myRequests.value = res.data;
                    }
                    else{
                        myRequests.value = null;
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        };

        const approveRequest = (requestId) => {
            const requestItem = friendRequests.value.find((r) => r.id === requestId);
            if (requestItem) {
                request
                    .put('/api/friendsModule/approveFriendRequest',{},{
                        params: {
                            uid: temporaryUser.uid,
                            fid:requestId//标记修改
                        }
                    })
                    .then(res => {
                        // 请求成功后的处理逻辑
                        console.log('Request approved:', res.data);
                        // 更新 requestItem 对象的状态
                        requestItem.requestStatus = 1;
                        loadFriendRequests();

                    })
                    .catch(error => {
                        // 请求失败时的错误处理逻辑
                        console.error('Failed to approve request:', error);
                    });

            }

        };
        const rejectRequest = (requestId) => {
            const requestItem = friendRequests.value.find((r) => r.id === requestId);
            if (requestItem) {
                request
                    .put('/api/friendsModule/rejectFriendRequest',{},{
                        params: {
                            uid: temporaryUser.uid,
                            fid:requestId//标记修改
                        }
                    })
                    .then(res => {
                        // 请求成功后的处理逻辑
                        console.log('Request approved:', res.data);
                        // 更新 requestItem 对象的状态
                        requestItem.requestStatus = -1;
                        loadFriendRequests();
                    })
                    .catch(error => {
                        // 请求失败时的错误处理逻辑
                        console.error('Failed to approve request:', error);
                    });
            }
        };

        onMounted(() => {
            loadFriendRequests();
            loadMyRequests();
        });

        const getRequestClass = (requestStatus) => {
            return {
                'approved': requestStatus === 1,
                'rejected': requestStatus === -1,
                'pending': requestStatus === 0,
            };
        };

        return {
            friendRequests,
            myRequests,
            loadFriendRequests,
            loadMyRequests,
            getRequestClass,
            approveRequest,
            rejectRequest,
        };
    },
});
</script>

<style scoped>
.notification-page {
    width:290px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.navbar {
    width: 100%;
    height: 60px;
    display: flex;
    justify-content: flex-end;
    background-color: rgba(255, 255, 255, 0.4);
    /*background-color: rgba(21, 159, 255, 0.1);*/
}

.new-friend-icon {
    width: 20%;
    height: auto;
    transform: scale(0.5);
}

.friendRequests-list {
    width: 100%;
    padding: 5px;
    height: 250px; /* 设置最大高度 */
    overflow-y: auto;
    font-size: 10px;
}
.friendRequests-list::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.friendRequests-list::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.friendRequests-list::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}
.friendRequests-list::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}
.my-requests {
    margin-top: 20px;
    width: 100%;
    padding: 5px;
    height: 250px; /* 设置最大高度 */
    overflow-y: auto;
    font-size: 10px;
}
.my-requests::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.my-requests::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.my-requests::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}
.my-requests::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}
.notification-item {
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
}

.agree-button {
    font-size: 10px;
    width: 30px;
    height:25px;
    margin-right: 40px;
}

.reject-button {
    position: absolute;
    right: 5px;
    font-size: 10px;
    width: 30px;
    height:25px;
}

.request-time-wrapper {
    position: absolute;
    bottom: 0;
    right: 5px;
    display: flex;
    align-items: flex-end;
    font-size: 5px;
}

.request-time {
    font-size: 5px;
    margin-left: 10px;
    color: gray;
}
.approved {
    color: rgba(1, 36, 114, 0.99);
}

.rejected {
    color: #480101;
}

.pending {

}
.friendRequests-message {
    left: 50%; /* 修改此行 */
    top:50%;
    margin-bottom: auto; /* 修改此行 */
    font-size: 10px;
    color: rgba(128, 115, 115, 0.76);
}
.myRequests-message {
    left: 50%; /* 修改此行 */
    top:50%;
    font-size: 10px;
    color: rgba(128, 115, 115, 0.76);
}
</style>