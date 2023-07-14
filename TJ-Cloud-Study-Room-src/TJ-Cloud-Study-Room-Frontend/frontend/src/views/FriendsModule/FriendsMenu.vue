<template>
  <div class="window-wrap">
    <div class="sidebar-wrap">
      <BarLeft @change-content="changeContent" />
    </div>
    <div class="content-wrap">
      <div class="content">
        <template v-if="activeContent === 'user'">
          <!-- 显示用户内容 -->
          <FriendList @friend-selected="handleFriendSelected" />
        </template>
        <template v-else-if="activeContent === 'chat'">
          <!-- 显示聊天内容 -->
          <Chat :friend="selectedFriend" />
        </template>
        <template v-else-if="activeContent === 'notifications'">
          <!-- 显示通知内容 -->
          <Notification />
        </template>
        <template v-else-if="activeContent === 'recommend'">
            <!-- 显示推荐内容 -->
            <Recommend />
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref } from 'vue'
import BarLeft from '@/components/FriendsModule/sidebar-left'
import FriendList from "@/components/FriendsModule/friendList";
import Chat from "@/components/FriendsModule/chat";
import Notification from "@/components/FriendsModule/notification";
import Recommend from "@/components/FriendsModule/recommend";
export default defineComponent({
  components: {
    BarLeft,
    Chat,
    FriendList,
    Notification,
    Recommend
  },
  setup() {
    const activeContent = ref('user'); // 初始显示聊天内容
    const selectedFriend = ref(null); // 初始化 selectedFriend 变量

    const changeContent = (content) => {
      activeContent.value = content;
    };

    const handleFriendSelected = (friend) => {
      activeContent.value = 'chat'; // 切换到聊天内容
      selectedFriend.value = friend; // 更新 selectedFriend 变量
    };
    return {
      activeContent,
      selectedFriend, // 将 selectedFriend 变量暴露出去
      changeContent,
      handleFriendSelected,
    };
  }
})
</script>

<style scoped>
.window-wrap {
  width: 100%; /* 适应父容器的宽度 */
  height: 100%; /* 适应父容器的高度 */
  display: flex;
  position: relative; /* 相对定位 */
}

.sidebar-wrap {
  width: 60px;
}

.content-wrap {
  width: 290px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  height: 100%; /* 使内容区域撑满高度 */
}

.content {
  flex-grow: 1;
  /*白天模式*/
  background-color: rgba(243, 250, 253, 0.95);
  padding: 0;
}
</style>
