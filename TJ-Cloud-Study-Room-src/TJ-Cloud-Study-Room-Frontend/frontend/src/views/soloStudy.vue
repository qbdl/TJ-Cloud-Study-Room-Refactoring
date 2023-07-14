<template xmlns:el-card="http://www.w3.org/1999/html">
    <Header />
    <FriendsModule />
    <div style="display: flex" >
        <Aside />
        <div class="container">
            <el-card class="box-card" :style="{ backgroundImage: 'url(' + backgroundImageUrl + ')' }" shadow="always">
                <el-row class="my-row1" justify="space-evenly">
                    <el-col :span="6">
                        <div class="flex">
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Time"
                                placement="bottom"
                            ><el-button class="my-button" color="black" @click="showCard4" :icon="Timer"> Personal Time</el-button>
                            </el-tooltip>
                        </div><div class="grid-content ep-bg-purple" /></el-col>
                    <el-col :span="18" class="right-button">
                        <div class="flex">
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Background"
                                placement="bottom"
                            >
                                <el-button class="my-button" type="primary" color="black" size="large" @click="showCard1" :icon="Picture" />
                            </el-tooltip>
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Sound"
                                placement="bottom"
                            >
                                <el-button class="my-button" type="primary" color="black" size="large" @click="showCard2" :icon="Headset" />
                            </el-tooltip>

                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Motivational Quote"
                                placement="bottom"
                            >
                                <el-button class="my-button" type="primary" color="black" @click="showCard3" :icon="EditPen"/>
                            </el-tooltip>

                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Full Screen"
                                placement="bottom"
                            >
                                <el-button class="my-button" type="primary" color="black" size="large"  @click="simulateF11" :icon="FullScreen" />
                            </el-tooltip>

                        </div>
                        <div class="grid-content ep-bg-purple" /></el-col>
                </el-row>

                <el-row class="my-row2" justify="space-evenly">
                    <el-col  :span="12">
                        <div class="pop-card">
                            <el-card class="temp-card1" v-if="cardVisible4" :style="{ backgroundColor: '#000' }">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Personal Time</span>
                                    <div style="display: flex">
                                        <el-button :icon="Close" type="primary" color="black" size="small" class="close-button" @click="closeCard4" circle />
                                    </div>
                                </div>
                                <el-row justify="space-evenly" v-if="rowVisible1" align="middle" style="margin-top: 5px">
                                    <el-col :span="12">
                                        <div style="color: #fff; font-size: 24px; text-align: left" >{{ formatTime }}
                                        </div><div/>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px;" @click="togglePause">
                                            <VideoPause />
                                        </el-icon>
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px; margin-left: 30px;" @click="showRow2">
                                            <RefreshLeft />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                </el-row>

                                <el-row justify="space-evenly" v-if="rowVisible2" align="middle" style="margin-top: 5px">
                                    <el-col :span="6">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px;" @click="decrementDuration">
                                            <Minus />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                    <el-col :span="12">
                                        <div style="color: #fff; font-size: 24px; text-align: left; margin-left: 20px;" >{{ initTime }}
                                        </div>
                                        <div/>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px; margin-left: 40px;" @click="incrementDuration">
                                            <Plus />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                </el-row>
                                <el-row align="middle" v-if="rowVisible2" style="margin-top: 5px" justify="center">
                                    <el-button type="info" plain  @click="closeRow2">Start timer</el-button>
                                </el-row>
                            </el-card>
                        </div></el-col>

                    <el-col  :span="12" class="right-pop-card">
                        <div class="pop-card" justify="end">
                            <el-card class="temp-card2" v-if="cardVisible1" :style="{ backgroundColor: '#000',zIndex: 9999}">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Background</span>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" class="nightbutton" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard1" circle />
                                    </div>
                                </div>
                                <div style="color: #fff">
                                    <el-row :gutter="18" class="pic-row">
                                        <el-col v-for="(url, index) in urllist" :key="index" :span="8" class="image-container" @click="changeBackground(index)">
                                            <el-image class="image-clickable" style="width: 65px; height: 65px; border-radius: 10px;"
                                                      :src="url" :preview-src-list="srcList">
                                            </el-image>
                                            <div></div>
                                        </el-col>
                                    </el-row>
                                </div>
                            </el-card>
                            <el-card class="temp-card2" v-if="cardVisible2" :style="{zIndex: 9999}">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Sound</span>
                                    <el-icon :size="15" class="myicon" color="white" style="cursor: pointer; margin-left:20px; padding-top: 8px;" @click="playMusic">
                                        <VideoPause />
                                    </el-icon>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" class="nightbutton" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard2" circle />
                                    </div>
                                </div>
                                <el-row>
                                    <span style="color: #fff;margin-top: 8px;display: flex; font-size: 12px;">
                                        bird sound
                                    </span>
                                </el-row>
                                <el-row>
                                    <el-col :span="4">
                                        <span style="color: #fff; margin-top: 8px;display: flex; align-items: center;">
                                            <el-icon  class="myicon"><Headset /></el-icon>
                                        </span>
                                    </el-col>
                                    <el-col :span="20">
                                        <el-slider
                                            :min="0"
                                            :max="100"
                                            :step="1"
                                            :show-tooltip="false"
                                            v-model="volume.audio1"
                                            :track-style="{ height: '6px', width: '70%', left: '15%' }"
                                            @change="changeVolume"
                                        >
                                            <template #start>0</template>
                                            <template #end>100</template>
                                        </el-slider>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <span style="color: #fff;margin-top: 8px;display: flex; font-size: 12px;">
                                        rain sound
                                    </span>
                                </el-row>
                                <el-row>
                                    <el-col :span="4">
                                        <span style="color: #fff; margin-top: 8px;display: flex; align-items: center;">
                                            <el-icon  class="myicon"><Headset /></el-icon>
                                        </span>
                                    </el-col>
                                    <el-col :span="20">
                                        <el-slider
                                            :min="0"
                                            :max="100"
                                            :step="1"
                                            :show-tooltip="false"
                                            v-model="volume.audio2"
                                            :track-style="{ height: '6px', width: '70%', left: '15%' }"
                                            @change="changeVolume"
                                        >
                                            <template #start>0</template>
                                            <template #end>100</template>
                                        </el-slider>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <span style="color: #fff;margin-top: 8px;display: flex; font-size: 12px;">
                                        daylight
                                    </span>
                                </el-row>
                                <el-row>
                                    <el-col :span="4">
                                        <span style="color: #fff; margin-top: 8px;display: flex; align-items: center;">
                                            <el-icon  class="myicon"><Headset /></el-icon>
                                        </span>
                                    </el-col>
                                    <el-col :span="20">
                                        <el-slider
                                            :min="0"
                                            :max="100"
                                            :step="1"
                                            :show-tooltip="false"
                                            v-model="volume.audio3"
                                            :track-style="{ height: '6px', width: '70%', left: '15%' }"
                                            @change="changeVolume"
                                        >
                                            <template #start>0</template>
                                            <template #end>100</template>
                                        </el-slider>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <span style="color: #fff;margin-top: 8px;display: flex; font-size: 12px;">
                                        piano
                                    </span>
                                </el-row>
                                <el-row>
                                    <el-col :span="4">
                                        <span style="color: #fff; margin-top: 8px;display: flex; align-items: center;">
                                            <el-icon  class="myicon"><Headset /></el-icon>
                                        </span>
                                    </el-col>
                                    <el-col :span="20">
                                        <el-slider
                                            :min="0"
                                            :max="100"
                                            :step="1"
                                            :show-tooltip="false"
                                            v-model="volume.audio4"
                                            :track-style="{ height: '6px', width: '70%', left: '15%' }"
                                            @change="changeVolume"
                                        >
                                            <template #start>0</template>
                                            <template #end>100</template>
                                        </el-slider>
                                    </el-col>
                                </el-row>
                            </el-card>
                            <el-card class="temp-card2" v-if="cardVisible3" :style="{ backgroundColor: '#000' }">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Quoto</span>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard3" circle />
                                    </div>
                                </div>
                                <div style="color: #fff">
                                    <el-row justify="center">
                                        <el-button color="#000" @click="shuffleQuoto">
                                            Shuffle<el-icon  class="myicon"><Refresh /></el-icon>
                                        </el-button>
                                    </el-row>
                                    <el-row justify="center">
                                        <el-button color="#000" v-if="quotoVisible" @click="hideQuoto">
                                            Hide<el-icon  class="myicon"><Hide /></el-icon>
                                        </el-button>
                                        <el-button color="#000" v-if="quotounVisible" @click="showQuoto">
                                            View<el-icon  class="myicon"><View /></el-icon>
                                        </el-button>
                                    </el-row>
                                </div>
                            </el-card>
                        </div></el-col>
                </el-row>
                <el-row class="my-row3" justify="end">
                    <el-col :span="6"><div class="welcome">
                        <p style="font-size: 25px; color: white" v-if="quotoVisible">{{quotoSentence}}</p>
                    </div><div class="grid-content ep-bg-purple" /></el-col>
                </el-row>
            </el-card>
        </div>
    </div>
</template>

<script>
import Header from "../components/userHeader.vue";
import Aside from "../components/userAside.vue";
import { Delete, Edit, Search, Share, Upload, Close, FullScreen, Picture, Headset, EditPen, Timer, VideoPause, RefreshLeft, Plus, Minus, Refresh, Hide, View} from '@element-plus/icons-vue'
import {watch} from "vue";
import {leaveTime} from "@/views/Room.vue";
import FriendsModule from "@/views/FriendsModule/FriendsModule.vue";

export default {
    name: "soloStudy",
    computed: {
        Close() {
            return Close
        },
        Search() {
            return Search
        },
        FullScreen() {
            return FullScreen
        },
        Picture() {
            return Picture
        },
        Headset() {
            return Headset
        },
        EditPen() {
            return EditPen
        },
        Timer() {
            return Timer
        },
        VideoPause() {
            return VideoPause
        },
        RefreshLeft() {
            return RefreshLeft
        },
        Plus() {
            return Plus
        },
        Minus() {
            return Minus
        },
        Refresh() {
            return Refresh
        },
        Hide() {
            return Hide
        },
        View() {
            return View
        },
        formatTime() {
            const hours = Math.floor(this.timeRemaining / 3600);
            const minutes = Math.floor((this.timeRemaining % 3600) / 60);
            const seconds = Math.floor(this.timeRemaining % 60);
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        },
        initTime() {
            const setHours = Math.floor(this.duration / 3600);
            const setMinutes = Math.floor((this.duration % 3600) / 60);
            const setSeconds = Math.floor(this.duration % 60);
            return `${setHours.toString().padStart(2, '0')}:${setMinutes.toString()
                .padStart(2, '0')}:${setSeconds.toString().padStart(2, '0')}`;
        },
        quotoSentence() {
            const sentences = [
                '“No matter what people tell you, words and ideas can change the world.”',
                '“Don\'t try to become a person of importance. Try to become a person of value.”',
                '“Live as if you were to die tomorrow. Learn as if you were to live forever.”',
                '“You can be everything. You can be the infinite amount of things that people are.”',
                '“Wake up determined, go to bed satisfied.”',
                '“If you don’t like the road you’re walking, start paving another one!”',
                '“Without great solitude, no serious work is possible.”',
                '“Many of life’s failures are people who did not realize how close they were to success when they gave up.”',
                '“If people only knew how hard I’ve worked to gain my mastery, it wouldn’t seem so wonderful at all.”',
                '“There is only one way to learn... It\'s through action. Everything you need to know you have learned through your journey.”',
            ];
            return sentences[this.randomInt];
        }
    },
    components: {
        FriendsModule,
        RefreshLeft,
        VideoPause,
        Aside,
        Header,
        Delete,
        Edit,
        Search,
        Share,
        Upload,
        Close,
        FullScreen,
        Picture,
        Minus,
        Plus,
        Refresh,
        Hide,
        View,
        Headset,
        Timer
    },
    data() {
        return {
            cardVisible1: false,
            cardVisible2: false,
            cardVisible3: false,
            cardVisible4: false,
            isFullScreen: false,
            timeRemaining: 600, // 倒计时时间（单位：秒）
            intervalId: null, // 用于保存计时器的 ID
            isPaused: false, // 是否暂停计时器
            duration: 0,
            rowVisible1: true,
            rowVisible2: false,
            quotoVisible:true,
            quotounVisible:false,
            randomInt:0,
            volume: {
                audio1: 0,
                audio2: 0,
                audio3: 0,
                audio4: 0
            },
            audioList: [],
            backgroundImageUrl: require('../assets/bk7.gif'),
            urllist: [ require('../assets/bk1.gif'),
                require('../assets/bk2.gif'),
                require('../assets/bk3.jpg'),
                require('../assets/bk4.gif'),
                require('../assets/bk5.gif'),
                require('../assets/bk6.gif'),
                require('../assets/bk7.gif'),
                require('../assets/bk8.gif'),
                require('../assets/bk9.gif'),
                ],
            selectedImageIndex: 6,
        }
    },
    inject: ['isNightMode','isView'],
    methods: {
        showCard1() {
            this.cardVisible1 = true
            this.cardVisible2 = false
            this.cardVisible3 = false
            this.cardVisible4 = false
        },
        showCard2() {
            this.cardVisible2 = true
            this.cardVisible1 = false
            this.cardVisible3 = false
            this.cardVisible4 = false
        },
        showCard3() {
            this.cardVisible3 = true
            this.cardVisible4 = false
            this.cardVisible2 = false
            this.cardVisible1 = false
        },
        showCard4() {
            this.cardVisible4 = true
            this.cardVisible3 = false
            this.cardVisible2 = false
            this.cardVisible1 = false
        },
        closeCard1() {
            this.cardVisible1 = false
        },
        closeCard2() {
            this.cardVisible2 = false
        },
        closeCard3() {
            this.cardVisible3 = false
        },
        closeCard4() {
            this.cardVisible4 = false
        },
        showRow2() {
            this.rowVisible2 = true
            this.rowVisible1 = false
            this.duration = 0
        },
        closeRow2() {
            this.rowVisible2 = false
            this.rowVisible1 = true
            this.timeRemaining = this.duration
        },
        showQuoto() {
            this.quotoVisible = true
            this.quotounVisible = false
        },
        hideQuoto() {
            this.quotoVisible = false
            this.quotounVisible = true
        },
        countdown() {
            this.intervalId = setInterval(() => {
                if (!this.isPaused) { // 如果没有暂停计时器，则每秒更新剩余时间
                    if (this.timeRemaining > 0) {
                        this.timeRemaining--;
                    } else {
                        clearInterval(this.intervalId);
                    }
                }
            }, 1000);
        },
        togglePause() {
            this.isPaused = !this.isPaused; // 切换 isPaused 属性的值
        },
        incrementDuration() {
            this.duration += 5 * 60;
        },
        decrementDuration() {
            if (this.duration >= 5 * 60) {
                this.duration -= 5 * 60;
            }
        },
        shuffleQuoto() {
            this.randomInt = Math.floor(Math.random() * 10);
        },
        nightModeChanged(){
            if(this.isNightMode.value) {
                const icons = document.querySelectorAll('.nightIcon');
                icons.forEach(icon => {
                    icon.style.color = '#f1f4fc';
                });
            }
            else{
                const icons = document.querySelectorAll('.nightIcon');
                icons.forEach(icon => {
                    icon.style.color = '#111828';
                });
            }
        },
        playMusic() {
            // 创建四个音频标签并设置循环播放和音量
            let audio1 = new Audio(require('@/assets/bird.mp3'))
            let audio2 = new Audio(require('@/assets/rain.mp3'))
            let audio3 = new Audio(require('@/assets/Daylight.mp3'))
            let audio4 = new Audio(require('@/assets/Piano.mp3'))
            audio1.loop = true
            audio2.loop = true
            audio3.loop = true
            audio4.loop = true
            audio1.volume = this.volume.audio1 / 100
            audio2.volume = this.volume.audio2 / 100
            audio3.volume = this.volume.audio3 / 100
            audio4.volume = this.volume.audio4 / 100
            audio1.play()
            audio2.play()
            audio3.play()
            audio4.play()
            this.audioList = [audio1, audio2, audio3, audio4]
        },
        changeVolume() {
            this.audioList.forEach((audio, index) => {
                audio.volume = this.volume['audio' + (index + 1)] / 100
            })
        },
        changeBackground(index) {
            // 在这里可以编写逻辑来改变背景图片URL
            this.backgroundImageUrl = this.urllist[index];
        },
        simulateF11() {
            const element = document.documentElement; // 获取整个文档的根元素

            if (document.fullscreenElement) {
                // 如果当前已处于全屏状态，则退出全屏
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.webkitExitFullscreen) {
                    document.webkitExitFullscreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
            } else {
                // 否则，进入全屏
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.webkitRequestFullscreen) {
                    element.webkitRequestFullscreen();
                } else if (element.msRequestFullscreen) {
                    element.msRequestFullscreen();
                }
            }
        },
    },
    mounted() {
        this.countdown();
        leaveTime(this.isView);
        watch(
            () => this.isNightMode.value,
            (newValue, oldValue) => {
                this.nightModeChanged();
            }
        )
    },
}

</script>

<style scoped>
.container {
    position: absolute;
    right: 10px;
    width: calc(100% - 75px);
}

.box-card {
    padding: 0px;
    margin-right: 5px;
    width: 100%;
    height: calc(100vh - 80px);
    display: flex;
    background-image: url(../assets/bk3.jpg);
    background-size: 100%;
    border-radius: 15px
}

.pop-card {
    font-family: 'Arial','Comic Sans MS',sans-serif;
    line-height: 1.5;
    margin-top: 5vh;
    display: flex;
}

.right-button {
    text-align: right;
}

.left-button {
    text-align: left;
}

.right-pop-card {
    display: flex;
    justify-content: flex-end;
}

.welcome {
    font-family: 'Arial','Comic Sans MS',sans-serif;
    line-height: 1.5;
    margin-top: 5vh;
    text-align: right;
}

.my-row1 {
    width: 88vw;
    margin-top: 0;
    margin-bottom: 0;
    padding-top: 0;
}

.my-row2 {
    width: 88vw;
    height: 80px;
    display: flex;
    align-items: flex-end;

}
.my-row3 {
    width: 88vw;
    margin-top: 10vh;
}
.my-button {
    height: 60px;
    line-height: 60px;
    border-radius: 10px;
    font-size: 16px;
    padding: 0 20px;
}

.temp-card1 {
    width: 300px;
    background-color: #000000; /* 设置黑色背景色 */
    border:none;
    border-radius: 15px;
    padding: 0 !important;
}

.temp-card2 {
    width: 265px;
    background-color: #000000; /* 设置黑色背景色 */
    border:none;
    border-radius: 15px;
    padding: 0 !important;
    opacity: 85%;
}

.temp-card1 .card-header {
    display: flex;
    justify-content: flex-start; /* 右对齐 */
    font-size: 15px;
}

.temp-card2 .card-header {
    display: flex;
    justify-content: flex-start; /* 右对齐 */
    font-size: 15px;
}

.card-close {
    color: #ffffff; /* 设置白色 */
    font-size: 20px;
}

.close-button {
    margin-left: 140px;
    margin-right: 0px;
    margin-top: 0px;
}

.pic-row {
    margin-top: 10px;
}

.image-clickable {
    border: 3px solid transparent;
    transition: border-color 0.3s ease;
}

.image-clickable:hover {
    border-color: white;
}

.image-container {
    position: relative;
    display: inline-block;
}

.overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 5px;
    opacity: 0;
    transition: opacity 0.3s ease;
    display: flex;
    justify-content: center;
    align-items: center;
}

.overlay::after {
    content: "\2714"; /* 对号的 Unicode 字符 */
    font-size: 24px;
    color: white;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.image-container:hover .overlay {
    opacity: 1;
}

.image-container:hover .overlay::after {
    opacity: 1;
}

.pause-button {
    height: 20px;
    padding-left: 5px;
    padding-right: 5px;
}

.el-slider {
    --el-slider-button-wrapper-size: 32px;
    --el-slider-main-bg-color: #242628;
    --el-slider-height: 2px;
    --el-slider-button-size: 12px;
}

</style>