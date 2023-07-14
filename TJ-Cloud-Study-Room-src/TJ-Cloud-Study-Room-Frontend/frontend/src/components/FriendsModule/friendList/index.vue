<template>
    <div>
        <!-- 好友列表上的搜索栏 -->
        <div class="friend-nav">
            <el-icon class="search-icon"><Search /></el-icon>
            <el-input v-model="friendSearchQuery" placeholder="搜索好友" clearable class="search-input"></el-input>
            <!-- 添加好友的按钮 -->
            <el-button class="add-friend-btn" type="primary" circle @click="showAddFriendWindow">
                <el-icon><ZoomIn /></el-icon>
            </el-button>
            <!-- 删除好友的按钮 -->
            <el-button class="delete-friend-btn" type="danger" circle @click="showDeleteFriendWindow">
                <el-icon><Delete /></el-icon>
            </el-button>
        </div>
        <!-- 好友列表的主体部分 -->
        <div v-if="friendList" class="container" id="my-container">
            <!-- 一个好友卡片 -->
            <div
                    v-for="friend in filteredFriendList"
                    :key="friend.id"
                    class="friend-card"
                    @click="friend.online ? selectFriend(friend) : showOfflineErrorMessage()"
            >
                <div class="friend-line">
                    <el-avatar class="friend-avatar" :src=friend.avatar :size="35"></el-avatar>
                    <div class="friend-name">{{ friend.name }}</div>
                    <div style="flex: 1;"></div>
                    <div class="friend-status">
                        <el-icon v-if="friend.online" class="online-icon"><ChatDotRound /></el-icon>
                        <el-icon v-else class="offline-icon"><ChatRound /></el-icon>
                    </div>
                </div>
            </div>
        </div>
        <!--    没有好友时的友情提示信息-->
        <div v-else class="no-friendlist">
            <img class="background-image" src="@/assets/group.png" alt="Image">
            <div class="prompt-message">快去添加好友开始聊天吧~</div>
        </div>
        <!-- 添加好友的弹出窗 -->
        <el-card v-if="addFriendWindowVisible" class="add-friend-window" shadow="never">
            <el-button type="text" @click="closeAddFriendWindow" class="close-button">
                <el-icon><Close /></el-icon>
            </el-button>
            <div class="add-friend-content">
                <el-input v-model="addFriendSearchQuery" placeholder="请输入好友用户名" class="add-friend-input"></el-input>
                <div v-if="addFriendSearchList && addFriendSearchList.length === 0" class="no-results">没有搜索到结果</div>
                <div v-else-if="addFriendSearchList && addFriendSearchList.length > 0">
                    <!-- 根据 addFriendSearchList 的值进行渲染 -->
                    <el-list-item v-for="friend in addFriendSearchList" :key="friend.id" class="addSearch-item">
                        <el-avatar :src=friend.avatar></el-avatar>
                        <div class="addSearch-name">{{ friend.name }}</div>
                        <el-button type="primary" @click="sendFriendRequest(friend)" class="add-friend-request" :disabled="friend.isFriend || friend.requested">
                            {{ friend.isFriend ? '已是好友' : (friend.requested ? '已发送' : '发送申请') }}
                        </el-button>
                    </el-list-item>
                </div>
            </div>
        </el-card>

        <!-- 删除好友的弹出窗 -->
        <el-card v-if="deleteFriendWindowVisible" class="delete-friend-window" shadow="never">
            <el-button type="text" @click="closeDeleteFriendWindow" class="close-button">
                <el-icon><Close /></el-icon>
            </el-button>

            <template v-if="friendList?.length > 0">
                <el-checkbox-group v-model="selectedDeleteFriendIds" class="delete-friend-list" @change="toggleSelectFriend">
                    <el-checkbox v-for="friend in friendList" :key="friend.id" :label="friend.id" class="friend-checkbox">
                        {{ friend.name }}
                    </el-checkbox>
                </el-checkbox-group>

                <div class="delete-confirm-button">
                    <el-button type="danger" @click="multiDeleteFriend" class="delete-friend-confirm">确认删除</el-button>
                </div>
            </template>

            <template v-else>
                <div class="no-friends-text">没有好友可删除</div>
            </template>
        </el-card>


        <!-- 不在线错误消息提示框 -->
        <div v-if="offlineErrorMessage" class="offline-error-message">
            <el-icon class="error-icon" size="large"><WarningFilled /></el-icon>
            {{ offlineErrorMessage }}
        </div>
        <!--发送好友申请失败错误消息提示框 -->
        <div v-if="failedApplicationErrorMessage" class="failed-application-error-message">
            <el-icon class="error-icon" size="large"><WarningFilled /></el-icon>
            {{ failedApplicationErrorMessage }}
        </div>
        <!--删除好友失败错误消息提示框 -->
        <div v-if="failedDeletionErrorMessage" class="failed-deletion-error-message">
            <el-icon class="error-icon" size="large"><WarningFilled /></el-icon>
            {{ failedDeletionErrorMessage }}
        </div>
        <!-- 发送好友申请成功正确消息提示框 -->
        <div v-if="successfulApplicationCorrectMessage" class="successful-application-correct-message">
            <el-icon class="correct-icon" size="large"><SuccessFilled /></el-icon>
            {{ successfulApplicationCorrectMessage }}
        </div>
        <!-- 删除好友成功正确消息提示框 -->
        <div v-if="successfulDeletionCorrectMessage" class="successful-deletion-correct-message">
            <el-icon class="correct-icon" size="large"><SuccessFilled /></el-icon>
            {{ successfulDeletionCorrectMessage }}
        </div>
    </div>
</template>

<script>
import {ref, computed, onMounted, watch, inject} from 'vue';
import {ElAvatar, ElCard, ElButton, ElDialog, ElIcon, ElInput} from 'element-plus';
import {
    ChatDotRound,
    ChatRound,
    Close, Delete,
    Plus,
    Search,
    SuccessFilled,
    WarningFilled,
    ZoomIn,
} from '@element-plus/icons-vue';
import request from "@/utils/request";
export default {
    name: 'FriendList',
    components: {
        Close,
        ZoomIn,
        Delete,
        SuccessFilled,
        WarningFilled,
        ChatRound,
        ChatDotRound,
        Plus,
        ElAvatar,
        ElCard,
        ElButton,
        ElDialog,
        ElIcon,
        ElInput,
        Search,
    },
    emits: ['friend-selected'],
    setup(_, { emit }) {
        const friendList = ref();
        const isFriendShow=inject('isFriendShow');
        // 标记修改
        const selectedFriend = ref(null);//选中聊天的好友

        const friendSearchQuery = ref('');// 用于搜索好友列表的查询
        const addFriendSearchQuery = ref('');// 用于添加好友弹出窗口的查询
        const addFriendWindowVisible = ref(false);
        const deleteFriendWindowVisible = ref(false);
        const selectedDeleteFriendIds = ref([]);
        const offlineErrorMessage = ref('');
        const failedApplicationErrorMessage = ref('');
        const failedDeletionErrorMessage= ref('');
        const successfulApplicationCorrectMessage=ref('');
        const successfulDeletionCorrectMessage= ref('');
        const addFriendSearchList =ref([]);
        const loadFriendList = () => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request.get('/api/friendsModule/friendList', {
                params: {
                    uid: temporaryUser.uid//标记替换
                }
            }).then(res => {
                if(res.data.length!=0){
                    friendList.value = res.data;
                    filteredFriendList.value=res.data;
                }
                else{
                    friendList.value = null;
                    filteredFriendList.value=null;
                }
            }).catch(error => {
                console.error(error);
            });
        };
        // 当获取到自己的好友变更时，提示并重新渲染好友列表（当用户同意你的好友申请，或者你被删除什么的）
        const longPollingLoadFriendList = () => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request.get('/api/friendsModule/friendList', {
                params: {
                    uid: temporaryUser.uid//标记替换
                }
            }).then(res => {
                if(res.data.length!=0){
                    friendList.value = res.data;
                    filteredFriendList.value=res.data;
                }
                else{
                    friendList.value = null;
                    filteredFriendList.value=null;
                }
                longPollingLoadFriendList();
            }).catch(error => {
                console.error(error);
                longPollingLoadFriendList();
            });
        };
        onMounted(() => {
            loadFriendList();
            longPollingLoadFriendList();
            // console.log(friendList[0].avatar);
        });
        // 展示用户不在线的错误消息
        const showOfflineErrorMessage = () => {
            offlineErrorMessage.value = '该用户不在线！';
            setTimeout(() => {
                offlineErrorMessage.value = '';
            }, 1500);
        };

        const selectFriend = (friend) => {
            selectedFriend.value = friend;
            emit('friend-selected', friend);
        };

        const filteredFriendList = computed(() => {
            if (!friendList.value || !Array.isArray(friendList.value) || friendList.value.length === 0) {
                return []; // 返回空数组或其他默认值
            }

            const sortedFriendList = friendList.value.slice().sort((a, b) => {
                if (a.online && !b.online) return -1;
                if (!a.online && b.online) return 1;
                return 0;
            });

            return sortedFriendList.filter((friend) => {
                const name = friend.name.toLowerCase();
                const search = friendSearchQuery.value.toLowerCase();
                return name.includes(search);
            });
        });

        const showAddFriendWindow = () => {
            addFriendWindowVisible.value = true;
        };
        const loadAddFriendASearchList = () => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request
                .get('/api/friendsModule/addFriendSearchList', {
                    params: {
                        uid:temporaryUser.uid,
                        fname: addFriendSearchQuery.value
                    }
                })
                .then(res => {
                    if(res.data.length!=0){
                        addFriendSearchList.value = res.data;
                    }
                    else{
                        addFriendSearchList.value = null;
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        };
        watch(addFriendSearchQuery, (newValue, oldValue) => {
            // 监听搜索关键字变化，重新加载搜索好友列表
            loadAddFriendASearchList();
        });
        const resetAddFriendWindow = () => {
            addFriendWindowVisible.value = false;
            addFriendSearchQuery.value = '';
        };

        const sendFriendRequest = (friend) => {
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request.post("/api/friendsModule/sendFriendRequest",{},{
                params: {
                    uid: temporaryUser.uid,
                    fid: friend.id, //标记修改
                }
            }).then(res => {
                friend.requested=true;
                // 发送好友申请成功，显示成功消息
                successfulApplicationCorrectMessage.value = '发送申请成功';
                // 清空错误消息
                setTimeout(() => {
                    successfulApplicationCorrectMessage.value = '';
                }, 1500);
            }).catch(error => {
                console.error(error);
                // 发送好友申请失败，显示失败消息
                failedApplicationErrorMessage.value = '发送申请失败，请重新申请';
                // 清空错误消息
                setTimeout(() => {
                    failedApplicationErrorMessage.value = '';
                }, 1500);
            });
        };

        const closeAddFriendWindow = () => {
            addFriendWindowVisible.value = false;
            addFriendSearchQuery.value = '';
            loadFriendList();
        };

        const showDeleteFriendWindow = () => {
            deleteFriendWindowVisible.value = true;
        };

        const resetDeleteFriendWindow = () => {
            deleteFriendWindowVisible.value = false;
            selectedDeleteFriendIds.value = [];
        };

        // 实现删除多选的
        const toggleSelectFriend = (friendId) => {
            if (!selectedDeleteFriendIds.value.includes(friendId)) {
                // Friend is not selected, add it
                selectedDeleteFriendIds.value.push(friendId);
            }
        };
        const deleteFriend = (friendId) =>{
            let userJson = sessionStorage.getItem("user");
            const temporaryUser = JSON.parse(userJson);
            request.delete('/api/friendsModule/deleteFriends/' + temporaryUser.uid + "/" + friendId)
                .then(res => {

                    // 显示成功消息
                    successfulDeletionCorrectMessage.value = '删除好友成功';

                    // 清除成功消息
                    setTimeout(() => {
                        successfulDeletionCorrectMessage.value = '';
                    }, 1500);
                })
                .catch((error) => {
                    // 删除好友失败
                    console.error(error);

                    // 显示错误消息
                    failedDeletionErrorMessage.value = '删除好友失败，请重试';

                    // 清除错误消息
                    setTimeout(() => {
                        failedDeletionErrorMessage.value = '';
                    }, 1500);
                });
        };
        const multiDeleteFriend = () => {
            for (var i = 0; i < selectedDeleteFriendIds.value.length; i=i+2) {
                console.log(i);
                deleteFriend(selectedDeleteFriendIds.value[i]);
            }
            // 删除好友成功，更新好友列表
            friendList.value = friendList.value.filter((friend) => !selectedDeleteFriendIds.value.includes(friend.id));
            // 重置删除好友窗口
            resetDeleteFriendWindow();
        };


        const closeDeleteFriendWindow = () => {
            deleteFriendWindowVisible.value = false;
            selectedDeleteFriendIds.value = [];
            loadFriendList();
        };

        return {
            friendList,
            selectedFriend,
            friendSearchQuery,
            addFriendSearchQuery,
            addFriendWindowVisible,
            addFriendSearchList,
            deleteFriendWindowVisible,
            selectedDeleteFriendIds,
            filteredFriendList,
            offlineErrorMessage,
            failedApplicationErrorMessage,
            failedDeletionErrorMessage,
            successfulApplicationCorrectMessage,
            successfulDeletionCorrectMessage,
            loadFriendList,
            longPollingLoadFriendList,
            showOfflineErrorMessage,
            selectFriend,
            showAddFriendWindow,
            loadAddFriendASearchList,
            resetAddFriendWindow,
            closeAddFriendWindow,
            sendFriendRequest,
            showDeleteFriendWindow,
            resetDeleteFriendWindow,
            toggleSelectFriend,
            deleteFriend,
            multiDeleteFriend,
            closeDeleteFriendWindow,
        };
    },
};
</script>

<style scoped>
.friend-nav {
    width: 290px;
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    /*background-color: rgba(21, 159, 255, 0.1);*/
    background-color: rgba(255, 255, 255, 0.4);
}
.search-icon{
    margin-right: 10px;
}

.search-input {
    width: 220px;
    margin-right: 10px;
}
.add-friend-btn{
    width: 25px;
    height: 25px;

}
.add-friend-window {
    position: absolute;
    top: 310px;
    left: 145px;
    transform: translate(-50%, -50%);
    z-index: 9999;
    width: 280px;
    height: 600px; /* 设置最大高度 */
    overflow-y: auto;
    background-color: rgba(225, 240, 251, 0.95);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-friend-window::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.add-friend-window::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.add-friend-window::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}

.add-friend-window::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}

.close-button {
    position:absolute;
    right: 0;
    top: 0;
    padding: 5px;
}
.add-friend-content {
    display: flex;
    align-items: center;
    flex-direction: column;
}
.add-friend-input {
    width: 90%;
    font-size: 10px;
}
/*搜索返回无结果*/
.no-results{
    margin-top: 20px;
    font-size:10px;
}
.addSearch-item {
    margin-top: 10px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 5px;
    border-bottom: 1px solid #ddd;
}
.addSearch-name{
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
.delete-friend-btn{
    width: 25px;
    height: 25px;
}
.delete-friend-window{
    position: absolute;
    top: 310px;
    left: 145px;
    transform: translate(-50%, -50%);
    z-index: 9999;
    width: 250px;
    max-height: 300px; /* 设置最大高度 */
    overflow-y: auto;
    background-color: rgba(255, 244, 244, 0.95);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.delete-friend-window::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.delete-friend-window::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.delete-friend-window::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}

.delete-friend-window::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}
.delete-friend-list {
    display: flex;
    flex-wrap: wrap;
    font-size: 10px;
}
.delete-confirm-button{
    margin-top:10px;
    display: flex;
    justify-content: center;
}
.friend-checkbox {
    width: 100%;
    font-size:10px;
}
.delete-friend-confirm{
    font-size: 10px;
}
.container {
    position: relative;
    height: 562px;
    width: 290px;
    overflow-y: auto;
    padding: 10px;
}
.container::-webkit-scrollbar {
    /*width: 5px; !* 设置滚动条的宽度 *!*/
    width: 5px;
    background: transparent;
}

.container::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.container::-webkit-scrollbar-thumb {
    background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
    border-radius: 4px; /* 设置滚动条滑块的圆角 */
}
.container::-webkit-scrollbar-thumb:hover {
    background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}
.friend-card {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    height: 50px;
    cursor: pointer;
    border: 1px solid rgba(204, 204, 204, 0.2);
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
    background-color:rgba(255, 255, 255, 1);
}
.friend-card:hover {
    background-color:rgba(172, 214, 255, 0.2);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.friend-line {
    width:100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.friend-avatar{
    margin-left: 20px;
}

.friend-name {
    margin-left: 20px;
    font-size: 15px;
    font-weight: bold;
}
.friend-status {
    margin-right: 25px;
    font-size: 20px;
}
.friend-status .online-icon {
    color: rgba(114, 246, 114, 0.8);
}

.friend-status .offline-icon {
    color: rgba(248, 65, 65, 0.8);
}
.error-icon{
    margin-right: 10px;
}
.offline-error-message {
    position: absolute;
    top: 60px;
    left: 80px;
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
    font-size:10px;
}
.failed-application-error-message{
    position: absolute;
    top: 60px;
    left: 50px;
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
    font-size:10px;
}
.failed-deletion-error-message{
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
    font-size:10px;
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
    font-size:10px;
}
.successful-deletion-correct-message{
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
    font-size:10px;
}
.no-friendlist {
    height: 562px;
    width:290px;
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.background-image {
    width: 60%;
    height: 180px;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    margin-top: -100px;
    margin-left: -10px;
}

.prompt-message {
    margin-top: 0px;
    font-size: 15px;
    color: rgba(128, 115, 115, 0.76);
}
</style>
