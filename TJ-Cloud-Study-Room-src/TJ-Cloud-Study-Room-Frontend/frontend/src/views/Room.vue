<template>
    <div class="room">
        <userHeader />
        <FriendsModule></FriendsModule>
        <div style="position: relative">
            <userAside />
            <!--            视频区-->
            <el-card class="mainWindow" shadow="never">
                <div class="mainContent">
                    <div style="display: flex; justify-content: space-between;">
                        <div style="display: flex; align-items: center;">
                            <el-tooltip content="退出房间">
                                <el-button :icon="Back" circle type="primary" style="margin-right: 10px" class="noc" @click="exit"/>
                            </el-tooltip>
                            <h2 class="nightElement">{{ room.roomName }}</h2>
                        </div>
                        <div style="display: flex; align-items: center;">
                            <el-icon class="nightElement" style="margin-right: 10px" :size="25"><Clock /></el-icon>
                            <h2 class="nightElement">{{ date }} {{ time }}</h2>
                        </div>
                    </div>
                    <div style="display: flex; align-items: center">
                        <el-card class="details" shadow="never">
                            <el-text class="nightElement" style="font-size: 20px; font-weight: bold">{{ room.preNum }}</el-text>
                            <div style="margin-top: 10px">
                                <el-text class="nightElement">在线人数</el-text>
                            </div>
                        </el-card>
                        <el-card class="details" shadow="never">
                            <el-text class="nightElement" style="font-size: 20px; font-weight: bold">{{ room.maxNum }}</el-text>
                            <div style="margin-top: 10px">
                                <el-text class="nightElement">最大人数</el-text>
                            </div>
                        </el-card>
                        <el-card class="details" shadow="never">
                            <el-text class="nightElement" style="font-size: 20px; font-weight: bold">{{ timeDiff }}</el-text>
                            <div style="margin-top: 10px">
                                <el-text class="nightElement">存在时长</el-text>
                            </div>
                        </el-card>
                        <div style="flex: 1" />
                        <el-tooltip content="修改房间信息">
                            <el-icon class="nightElement" @click="editRoomInfo" style="margin-right: 20px" :size="25"><Edit  /></el-icon>
                        </el-tooltip>
                        <el-dialog v-model="dialogVisible" title="修改房间信息" width="30%" draggable @close="roomInfoCancel">
                            <el-form :model="form" label-width="80px" :rules="rules" status-icon ref="roomForm">
                                <el-form-item label="房间名" prop="roomName">
                                    <el-input v-model="form.roomName" />
                                </el-form-item>
                                <el-form-item label="最大人数" prop="maxNum">
                                    <el-input v-model="form.maxNum" />
                                </el-form-item>
                                <el-form-item label="房间密码" prop="roomPassword">
                                    <el-input v-model="form.roomPassword"/>
                                </el-form-item>
                                <el-form-item label="房间公告">
                                    <el-input v-model="form.roomAnnouncement" type="textarea" show-word-limit maxlength="100"/>
                                </el-form-item>
                                <div style="display: flex; justify-content: right">
                                    <el-button type="primary" @click="roomInfoSubmit">确认</el-button>
                                    <el-button @click="roomInfoCancel">取消</el-button>
                                    <el-button type="danger" @click="deleteRoom" :disabled="room.fixed===1">解散房间</el-button>
                                </div>
                            </el-form>
                        </el-dialog>
                        <el-tooltip :content="tooltipContent" :disabled="!showPopper">
                            <el-icon class="nightElement" :size="25" @mouseenter="showPopper = true" @click="localView">
                                <template v-if=this.isView.value>
                                    <View />
                                </template>
                                <template v-else>
                                    <Hide />
                                </template>
                            </el-icon>
                        </el-tooltip>
                    </div>
                    <div class="video-container" id="video-container" />
                </div>
            </el-card>
            <!--            公告区-->
            <el-card class="announcement" shadow="never">
                <div class="mainContent">
                    <div style="display: flex; align-items: center; margin-bottom: 20px">
                        <h2 class="nightElement">公告</h2>
                        <el-icon class="nightElement" style="margin-left: 10px" :size="30"><Promotion /></el-icon>
                    </div>
                    <el-text class="nightElement" style="white-space: pre-wrap; margin-top: 20px">{{room.roomAnnouncement}}</el-text>
                </div>
            </el-card>
            <!--            聊天区-->
            <el-card class="chatWindow" shadow=never>
                <div class="mainContent">
                    <div style="display: flex; align-items: center;">
                        <h2 class="nightElement">聊天区</h2>
                        <el-icon class="nightElement" style="margin-left: 10px" :size="30"><Comment /></el-icon>
                    </div>
                    <div class="chat-area">
                        <el-card class="message-list" shadow="never" ref="messageList">
                            <div v-for="(message, index) in messages" :key="index"
                                 class="message-item"
                                 :class="{ 'sent-message': message.from === userInfo.userName, 'received-message': message.from !== userInfo.userName }">
                                <el-avatar :src=message.avatar :size="35" style="margin-right: 10px" />
                                <div class="message-card">
                                    {{ message.text }}
                                </div>
                            </div>
                        </el-card>
                        <div class="input-area">
                            <el-icon class="nightElement" style="margin-right: 10px; cursor: pointer" :size="30" @click="showEmojis = !showEmojis">
                                <Grid />
                            </el-icon>
                            <div v-if="showEmojis" class="emoji-panel">
                                <span v-for="emoji in emojis" :key="emoji" class="emoji" @click="addEmoji(emoji)">{{ emoji }}</span>
                            </div>
                            <el-input
                                v-model="newMessage"
                                placeholder="请输入消息"
                                size="large"
                                @keyup.enter="sendMessage"
                            ></el-input>
                            <el-button @click="sendMessage" size="large" class="el-button--primary">发送</el-button>
                        </div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
import UserAside from "@/components/userAside.vue";
import UserHeader from "@/components/userHeader.vue";
import {Back, Clock, Comment, Edit, Grid, Hide, Promotion, StarFilled, View} from "@element-plus/icons-vue";
import AgoraRTC from "agora-rtc-sdk-ng";
import {watch} from "vue";
import router from "@/router";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import emojis from "@/emoji";
import FriendsModule from "@/views/FriendsModule/FriendsModule.vue";

let rtc = {
    client: null,
    localAudioTrack: null,
    localVideoTrack: null,
    remoteVideoTracks: [], // 新增一个remoteVideoTracks数组
};
let options = {
    // Pass your App ID here.
    appId: "61a48f2ad3e54194bc0a770812ccb7dd",
    // Set the channel name.
    channel: null,
    // Pass your temp token here.
    token: null,
};
// 调试
// let user = {
//     uid: 10086,
//     userName: "起一个长一点的名字哈哈哈哈哈哈哈哈",
// };
let user = {};
let socket;

// 视频api相关
async function dealSubscribe(room) {
    // Listen for the "user-published" event, from which you can get an AgoraRTCRemoteUser object.
    rtc.client.on("user-published", async (user, mediaType) => {
        // Subscribe to the remote user when the SDK triggers the "user-published" event
        await rtc.client.subscribe(user, mediaType);

        // If the remote user publishes a video track.
        if (mediaType === "video") {
            // Get the RemoteVideoTrack object in the AgoraRTCRemoteUser object.
            // const remoteVideoTrack = user.videoTrack;
            const remoteVideoTrack = user.videoTrack;
            rtc.remoteVideoTracks.push({
                videoTrack: remoteVideoTrack,
                userId: user.uid // 存储发布者用户ID
            });

            const videoContainer = document.getElementById('video-container')
            playRemoteVideoTrack(videoContainer, remoteVideoTrack, user.uid)
            room.preNum = rtc.remoteVideoTracks.length + 1
        }

        // If the remote user publishes an audio track.
        if (mediaType === "audio") {
            // Get the RemoteAudioTrack object in the AgoraRTCRemoteUser object.
            const remoteAudioTrack = user.audioTrack;
            // Play the remote audio track. No need to pass any DOM element.
            remoteAudioTrack.play();
        }

        // Listen for the "user-unpublished" event
        rtc.client.on("user-unpublished", user => {
            // 从remoteVideoTracks数组中删除相应的视频轨道
            for (let i = 0; i < rtc.remoteVideoTracks.length; i++) {
                const remoteVideoTrack = rtc.remoteVideoTracks[i];
                if (remoteVideoTrack.userId === user.uid) {
                    rtc.remoteVideoTracks.splice(i, 1);
                    break;
                }
            }
            // Get the dynamically created DIV container.
            const remotePlayerContainer = document.getElementById(user.uid);
            // Destroy the container.
            if (remotePlayerContainer) {
                remotePlayerContainer.remove();
            }
            room.preNum = rtc.remoteVideoTracks.length + 1
        });
    });
}
function playLocalVideoTrack() {
    const videoContainer = document.getElementById('video-container');
    const localPlayerContainer = document.createElement('div');
    localPlayerContainer.id = user.uid.toString();
    localPlayerContainer.style.width = '300px';
    localPlayerContainer.style.height = '225px';
    localPlayerContainer.style.borderRadius = '20px';
    localPlayerContainer.style.overflow = 'hidden';
    localPlayerContainer.style.position = 'relative';
    videoContainer.insertBefore(localPlayerContainer, videoContainer.firstChild);

    const usernameElement = document.createElement('div');
    usernameElement.style.position = 'absolute';
    usernameElement.style.bottom = '0';
    usernameElement.style.left = '10px';
    usernameElement.style.width = 'auto';
    usernameElement.style.height = '20px';
    usernameElement.style.paddingLeft = '10px';
    usernameElement.style.paddingRight = '10px';
    usernameElement.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    usernameElement.innerHTML = user.userName;
    usernameElement.style.color = '#fff';
    usernameElement.style.fontSize = '10px';
    usernameElement.style.display = 'flex';
    usernameElement.style.alignItems = 'center';
    usernameElement.style.zIndex = '1';
    localPlayerContainer.appendChild(usernameElement);

    // Play local video track
    rtc.localVideoTrack.play(localPlayerContainer);
}
function playRemoteVideoTrack(videoContainer, remoteVideoTrack, userId) {
    // 后端接口
    request.get('/api/rooms/getUserInfo/id', {
        params: {
            uid: userId
        }
    }).then(res => {
        const userName = res.data.userName;

        // 创建一个新的<div>元素，并将其添加到页面中
        const remotePlayerContainer = document.createElement("div");
        remotePlayerContainer.id = userId;
        remotePlayerContainer.style.width = "300px";
        remotePlayerContainer.style.height = "225px";
        remotePlayerContainer.style.borderRadius = "20px";
        remotePlayerContainer.style.overflow = "hidden";
        remotePlayerContainer.style.position = 'relative';
        videoContainer.append(remotePlayerContainer);

        const usernameElement = document.createElement('div');
        usernameElement.style.position = 'absolute';
        usernameElement.style.bottom = '0';
        usernameElement.style.left = '10px';
        usernameElement.style.width = 'auto';
        usernameElement.style.height = '20px';
        usernameElement.style.paddingLeft = '10px';
        usernameElement.style.paddingRight = '10px';
        usernameElement.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
        usernameElement.innerHTML = userName;
        usernameElement.style.color = '#fff';
        usernameElement.style.fontSize = '10px';
        usernameElement.style.display = 'flex';
        usernameElement.style.alignItems = 'center';
        usernameElement.style.zIndex = '1';
        remotePlayerContainer.appendChild(usernameElement);

        // 将视频轨道播放到新的<div>元素中
        remoteVideoTrack.play(remotePlayerContainer);
    })
}
function playAllRemoteVideoTracks() {
    const videoContainer = document.getElementById('video-container');
    for (let i = 0; i < rtc.remoteVideoTracks.length; i++) {
        playRemoteVideoTrack(videoContainer,rtc.remoteVideoTracks[i].videoTrack, rtc.remoteVideoTracks[i].userId)
    }
}

// 离开房间--外部调用
export function leaveTime(isView) {
    if(isView.value){
        let timerId = null;
        const enterTime = new Date();
        timerId = window.setInterval(() => {
            const now = new Date();
            const diff = now.getTime() - enterTime.getTime();
            if(diff > 5 * 60 * 1000){
                console.log("到时间了")
                isView.value = !isView.value;
                if (rtc.localAudioTrack && rtc.localVideoTrack) {
                    console.log("关闭")
                    // rtc.client.unpublish([rtc.localAudioTrack, rtc.localVideoTrack]); // 从发布列表中移除
                    rtc.client.unpublish(rtc.localVideoTrack); // 从发布列表中移除
                    rtc.localAudioTrack.close();
                    rtc.localVideoTrack.close();
                }
                clearInterval(timerId); // 清除定时器
            }
        }, 1000);
    }
}

// 输入校验
const validateName = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入房间名'))
    } else if (value.length < 1 || value.length > 20) {
        callback(new Error('长度限制为1~20个字符'))
    } else {
        callback()
    }
}
const validateMaxNum = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入最大人数'))
    } else if (!/^[0-9]+$/.test(value)) {
        callback(new Error('输入必须为数字'))
    } else if (value < 1 || value > 30) {
        callback(new Error('最大人数必须在1到30之间'))
    } else {
        callback()
    }
}
const validatePass = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入密码'))
    } else if (value.length < 8 || value.length > 20) {
        callback(new Error('长度必须在8到20之间'))
    } else {
        callback()
    }
}

export default {
    name: "Room",
    components: {
        FriendsModule,
        Edit, Clock, StarFilled, Promotion, Grid, Comment, Text, Hide, View, UserHeader, UserAside},
    data(){
        return{
            date: "",
            time: "",
            timeDiff: "",
            showPopper: false,
            showEmojis: false,
            messages: [
                // {from:1,to:4,text:'多来一点长度+++',avatar:"../assets/css/logo.png"}
            ],
            newMessage: '',
            dialogVisible: false,
            room: {},
            form: {},
            // 调试
            // room: {
            //     rid: 4,
            //     roomName:"测试",
            //     fixed: 0,
            //     maxNum: 20,
            //     preNum: rtc.remoteVideoTracks.length + 1,
            //     roomAnnouncement: "测试数据",
            //     roomPassword: "111",
            //     startTime: "2023-05-10T00:00:00.123",
            //     roomOwner: 10086
            // },
            // form: {
            //     rid: 4,
            //     roomName:"测试",
            //     fixed: 0,
            //     maxNum: 20,
            //     preNum: rtc.remoteVideoTracks.length + 1,
            //     roomAnnouncement: "测试数据",
            //     roomPassword: "111",
            //     startTime: "2023-05-10T00:00:00.123",
            //     roomOwner: 10086
            // },
            rules: {
                roomName: [
                    { validator: validateName, trigger: 'blur' }
                ],
                maxNum: [
                    { validator: validateMaxNum, trigger: 'blur' }
                ],
                roomPassword: [
                    { validator: validatePass, trigger: 'blur' }
                ],
            },
            interval_s: null,
            interval_5s: null
        }
    },
    inject:['isNightMode','isInRoom','isView'],
    created() {
        // 调试需关
        this.getInfoInit().then(res=>{
            this.initSocket();
            if(!this.isInRoom.value){
                rtc.client = AgoraRTC.createClient({mode: "rtc", codec: "vp8"});
                rtc.client.enableDualStream().then(() => {
                    console.log("enable dual stream success");
                }).catch(err => {
                    console.log(err);
                });
                options.channel = this.room.rid.toString();

                try {
                    rtc.client.join(options.appId, options.channel, options.token || null, user.uid || null);
                    this.isInRoom.value = true
                } catch (error) {
                    console.error("加入频道失败");
                    // handle error
                }
            }
            else{
                playAllRemoteVideoTracks();
                if(this.isView.value){
                    playLocalVideoTrack()
                }
            }
            dealSubscribe(this.room)
        })
    },
    mounted() {
        // this.scrollToBottom();
        if(this.isNightMode.value){
            this.nightModeChanged();
        }

        watch(
            () => this.isNightMode.value,
            (newValue, oldValue) => {
                this.nightModeChanged();
            }
        )

        const now = new Date();
        this.date = now.getFullYear() + "." + (now.getMonth() + 1).toString().padStart(2, "0") + "." + now.getDate().toString().padStart(2, "0");
        this.time = now.getHours().toString().padStart(2, "0") + ":" + now.getMinutes().toString().padStart(2, "0") + ":" + now.getSeconds().toString().padStart(2, "0");
        this.timeDiff = this.getTimeDiff(now, new Date(this.room.startTime));
        this.interval_s = setInterval(() => {
            const now = new Date();
            this.date = now.getFullYear() + "." + (now.getMonth() + 1) + "." + now.getDate();
            this.time = now.getHours().toString().padStart(2, "0") + ":" + now.getMinutes().toString().padStart(2, "0") + ":" + now.getSeconds().toString().padStart(2, "0");
            this.timeDiff = this.getTimeDiff(now, new Date(this.room.startTime));
        }, 1000);
        this.interval_5s = setInterval(() => {
            //后端接口
            request.get('/api/rooms/getRoomInfo', {
                params: {
                    rid: this.room.rid
                }
            }).then(res => {
                // 调试必关
                if(res.code === "0") {
                    if(!this.dialogVisible){
                        this.room = JSON.parse(JSON.stringify(res.data));
                        this.form = JSON.parse(JSON.stringify(res.data));
                        //session相关
                        sessionStorage.setItem("room", JSON.stringify(this.room));
                    }
                }
                else{
                    ElMessage({
                        type: 'warning',
                        message: res.msg,
                    })
                    this.exitChannel();
                    //session相关
                    // sessionStorage.removeItem("room");

                    // user.roomId = 0;
                    // sessionStorage.removeItem("room");
                    // sessionStorage.setItem("user", JSON.stringify(user));
                    // this.$router.push('/studyHall');
                    // this.isInRoom.value = false;
                    // this.isView.value = false;

                    this.exit();
                }
            })
        },5000);
    },
    beforeUnmount() {
        clearInterval(this.interval_s);
        clearInterval(this.interval_5s);
    },
    methods: {
        // 属性
        async getInfoInit(){
            //session相关
            let userInfo = JSON.parse(sessionStorage.getItem("user"));
            if(!userInfo || !userInfo.uid){
                this.$router.push("/login");
            }
            user = userInfo;

            let roomInfo = JSON.parse(sessionStorage.getItem("room"));
            if (!roomInfo || !roomInfo.rid) {
                this.$router.push("/studyHall");
            }
            this.room = JSON.parse(JSON.stringify(roomInfo));
            this.form = JSON.parse(JSON.stringify(roomInfo));
        },
        getTimeDiff(now, startTime) {
            const diff = now.getTime() - startTime.getTime();
            const hours = Math.floor(diff / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
            // const seconds = Math.floor((diff % (1000 * 60)) / 1000);
            return hours + "h " + minutes + "m";
        },
        // scrollToBottom() {
        //     this.$nextTick(() => {
        //         const messageList = this.$refs.messageList;
        //         messageList.scrollTop = messageList.scrollHeight;
        //     });
        // },
        nightModeChanged(){
            if(this.isNightMode.value) {
                const nightElements = document.querySelectorAll('.nightElement,.nightIcon');
                nightElements.forEach(nightElement => {
                    // nightElement.style.color = '#b8bbc0';
                    nightElement.style.color = '#f1f4fc';
                });
                const details = document.querySelectorAll('.details,.mainWindow,.chatWindow,.announcement');
                details.forEach(detail =>{
                    detail.style.backgroundColor = '#1f2938';
                    detail.style.borderColor = '#1f2938';
                })
                const items = document.querySelectorAll('.received-message .message-card');
                items.forEach(item=>{
                    item.style.backgroundColor = '#425269';
                    item.style.borderColor = '#425269';
                    item.style.color = '#f1f4fc';
                })
                const chatArea = document.querySelector('.message-list');
                chatArea.style.backgroundColor = '#1f2938';
                chatArea.style.borderColor = '#2a384d'
            }
            else{
                const nightElements = document.querySelectorAll('.nightElement,.nightIcon');
                nightElements.forEach(nightElement => {
                    nightElement.style.color = '#111828';
                });
                const details = document.querySelectorAll('.details,.mainWindow,.chatWindow,.announcement');
                details.forEach(detail =>{
                    detail.style.backgroundColor = '#fdfdfe';
                    detail.style.borderColor = '#fdfdfe';
                })
                const items = document.querySelectorAll('.received-message .message-card');
                items.forEach(item=>{
                    item.style.backgroundColor = '#f5f5f5';
                    item.style.borderColor = '#f5f5f5';
                    item.style.color = '#111828';
                })
                const chatArea = document.querySelector('.message-list');
                chatArea.style.backgroundColor = '#fdfdfe';
                chatArea.style.borderColor = '#e2e5ec'
            }
        },
        async localView() {
            try {
                this.isView.value = !this.isView.value
                if (this.isView.value) {
                    rtc.localAudioTrack = await AgoraRTC.createMicrophoneAudioTrack();
                    rtc.localVideoTrack = await AgoraRTC.createCameraVideoTrack();
                    // await rtc.client.publish([rtc.localAudioTrack, rtc.localVideoTrack]);
                    await rtc.client.publish(rtc.localVideoTrack);
                    playLocalVideoTrack()
                } else {
                    // 停止本地音频和视频轨道
                    if (rtc.localAudioTrack && rtc.localVideoTrack) {
                        // await rtc.client.unpublish([rtc.localAudioTrack, rtc.localVideoTrack]); // 从发布列表中移除
                        await rtc.client.unpublish(rtc.localVideoTrack); // 从发布列表中移除
                        rtc.localAudioTrack.close();
                        rtc.localVideoTrack.close();
                        const local = document.getElementById(user.uid);
                        local.remove();
                    }
                }
            } catch (error) {
                console.error(error);
                // handle error
            }
        },
        async exitChannel(){
            if (rtc.localAudioTrack && rtc.localVideoTrack) {
                // await rtc.client.unpublish([rtc.localAudioTrack, rtc.localVideoTrack]); // 从发布列表中移除
                await rtc.client.unpublish(rtc.localVideoTrack); // 从发布列表中移除
                rtc.localAudioTrack.close();
                rtc.localVideoTrack.close();
                const local = document.getElementById(user.uid);
                if (local) {
                    local.remove();
                }
            }

            // Traverse all remote users.
            rtc.client.remoteUsers.forEach(user => {
                // Destroy the dynamically created DIV containers.
                const playerContainer = document.getElementById(user.uid.toString());
                playerContainer && playerContainer.remove();
            });
            rtc.remoteVideoTracks.splice(0, rtc.remoteVideoTracks.length);

            // Leave the channel.
            await rtc.client.leave();
        },
        exit(){
            this.exitChannel();

            // 后端接口
            request.put( "/api/rooms/exit",{},{
                params: {
                    uid: user.uid,
                    rid: this.room.rid
                }
            }).then(res => {
                // session相关
                user.roomId = 0;
                sessionStorage.removeItem("room");
                sessionStorage.setItem("user", JSON.stringify(user));
                this.$router.push('/studyHall');
                this.isInRoom.value = false;
                this.isView.value = false;
            })
        },
        // 更改房间信息
        editRoomInfo() {
            if(user.uid === this.room.roomOwner){
                this.dialogVisible = true;
            }
            else{
                ElMessageBox.alert('抱歉，您没有修改房间信息的权限', '提示', {
                    confirmButtonText: 'OK',
                })
            }
        },
        roomInfoSubmit() {
            this.$refs.roomForm.validate(valid => {
                if (valid) {
                    ElMessage({
                        type: 'success',
                        message: '房间信息修改成功',
                    })
                    this.dialogVisible = false;
                    // this.form.preNum = rtc.remoteVideoTracks.length + 1;
                    this.room = JSON.parse(JSON.stringify(this.form));
                    // 后端接口
                    request.put('/api/rooms/changeRoomInfo', this.room)
                        .then(res => {
                            // 请求成功回调
                        }).catch(error => {
                        // 请求失败回调
                    })
                    user.roomId = 0;
                    // session相关
                    sessionStorage.setItem("room", JSON.stringify(this.room));
                    sessionStorage.setItem("user", JSON.stringify(user));
                } else {
                    ElMessage({
                        type: 'warning',
                        message: '房间信息不合规，请重新填写',
                    })
                }
            })
        },
        roomInfoCancel() {
            this.dialogVisible = false;
            this.form = JSON.parse(JSON.stringify(this.room));
        },
        deleteRoom(){
            ElMessageBox.confirm(
                '该操作将会解散房间。确认继续？',
                '警告',
                {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
                .then(() => {
                    ElMessage({
                        type: 'success',
                        message: '房间已解散，即将返回大厅',
                    })
                    this.exit();
                    // 后端接口
                    request.delete('/api/rooms/deleteRoom/' + this.room.rid)
                        .then(res => {
                            // 请求成功回调
                        }).catch(error => {
                        // 请求失败回调
                    })
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: '操作已取消',
                    })
                })
        },
        // 聊天
        initSocket(){
            if (typeof (WebSocket) == "undefined") {
                console.log("您的浏览器不支持WebSocket");
            } else {
                console.log("您的浏览器支持WebSocket");

                // 本地
                let socketUrl = "ws://localhost:9092/roomChat/" + user.userName;

                // 云端
                // let socketUrl = "ws://121.37.184.56:80/websocketR/roomChat/" + user.userName;

                socket = new WebSocket(socketUrl);

                //打开事件
                socket.onopen = function () {
                    console.log("websocket已打开");
                };
                //  浏览器端收消息，获得从服务端发送过来的文本消息
                const t = this;
                socket.onmessage = function (msg) {
                    console.log("收到数据====" + msg.data)
                    let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
                    if (data.users) {  // 获取在线人员信息
                        //     _this.users = data.users.filter(user => user.username !== username)  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
                    } else {
                        //     如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
                        //      // {"from": "zhang", "text": "hello"}
                        let toRoom = JSON.parse(sessionStorage.getItem("room"));
                        if (data.to === "room" + toRoom.rid.toString()) {
                            t.getUserAvatar(data.from).then(result => {
                                t.messages.push({
                                    from: data.from,
                                    text: data.text,
                                    avatar: result
                                })
                            });
                        }
                    }
                };
                //关闭事件
                socket.onclose = function () {
                    console.log("websocket已关闭");
                };
                //发生了错误事件
                socket.onerror = function () {
                    console.log("websocket发生了错误");
                }
            }
        },
        sendMessage() {
            if (this.newMessage.trim() !== '') {
                if (typeof (WebSocket) == "undefined") {
                    console.log("您的浏览器不支持WebSocket");
                } else {
                    console.log("您的浏览器支持WebSocket");
                    // 组装待发送的消息 json
                    // {"from": "zhang", "to": "admin", "text": "聊天文本"}
                    let message = {from: user.userName, to: "room" + this.room.rid.toString(), text: this.newMessage}
                    socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
                    this.newMessage = ''
                }

                // this.scrollToBottom();
            }
        },
        async getUserAvatar(userName){
            // 后端接口
            let url = null;
            await request.get('/api/rooms/getUserInfo/name', {
                params: {
                    userName: userName
                }
            }).then(res => {
                url = res.data.picture;
            })
            // console.log(url);
            return url;
        },
        addEmoji(emoji) {
            this.newMessage += emoji;
        }
    },
    computed: {
        Back() {
            return Back
        },
        tooltipContent() {
            return this.isView.value ? '关闭摄像头' : '打开摄像头'
        },
        userInfo(){
            return user
        },
        emojis() {
            return emojis
        }
    },
}
</script>

<style scoped>
body{
    overflow:hidden;
}

.mainWindow{
    position: absolute;
    top:20px;
    left:75px;
    height: 85vh;
    width: 62vw;
    border-radius: 40px;
    background-color: #fdfdfe;
    border-color: #fdfdfe;
}

.chatWindow{
    position: absolute;
    top:calc(20px + 32vh);
    left:68vw;
    height: 53vh;
    width: 28vw;
    border-radius: 40px;
    background-color: #fdfdfe;
    border-color: #fdfdfe;
}

.announcement{
    position: absolute;
    top:20px;
    left:68vw;
    height: 30vh;
    width: 28vw;
    border-radius: 40px;
    background-color: #fdfdfe;
    border-color: #fdfdfe;
}

.mainContent{
    padding: 20px;
    height: 80%;
}

.details{
    height: 100px;
    width: 150px;
    flex-direction: column;
    background-color: #fdfdfe;
    border-color: #fdfdfe;
    margin-top: 20px;
}

.video-container {
    flex-wrap: wrap;
    max-height: 550px;
    overflow-y: auto;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-gap: 10px;
}

.video-container > div {
    margin: 10px;
}

.chat-area {
    display: flex;
    flex-direction: column;
    margin-top: 5%;
}

.message-list {
    height: 33vh;
    padding: 10px;
    overflow: auto;
}

.message-item {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 12px;
    align-items: center;
}

.sent-message .message-card {
    background-color: #409EFF;
    color: #fff;
    border-color: #409EFF;
    align-self: flex-end;
}

.received-message .message-card {
    background-color: #f5f5f5;
    color: #000;
    align-self: flex-start;
}

.message-card {
    border-radius: 10px;
    max-width: 70%;
    word-break: break-all;
    display: flex;
    align-items: center;
    justify-content: left;
    padding: 10px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
    white-space: pre-wrap;
    font-size: 12px;
}

.input-area {
    display: flex;
    align-items: center;
    margin-top: 5%;
}

.emoji-panel {
    position: absolute;
    bottom: 100px;
    right: 0;
    width: 150px;
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 8px;
    display: flex;
    flex-wrap: wrap;
    z-index: 1;
    height: 150px; /* 设置最大高度 */
    overflow-y: auto;
}

.emoji {
    cursor: pointer;
    margin: 2px;
    font-size: 18px;
}
</style>