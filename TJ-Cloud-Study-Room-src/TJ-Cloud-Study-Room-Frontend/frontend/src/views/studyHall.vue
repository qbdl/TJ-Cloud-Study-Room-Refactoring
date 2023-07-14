<template>

    <userHeader/>
    <FriendsModule/>
    <p class="app-name" style="font-size: 50px; font-weight: bold; color: #333; text-align: center;">欢迎来到TJ云上自习室大厅</p>

    <div class="app-content" >
        <div style="margin: 3% 10%; text-align: center">
            <div class="search-wrapper" style="margin: 3% 30%;text-align: justify;background-color: '#ffffff'">
                <input  class="search-input" v-model="search" type="text" placeholder="搜索房间ID" @keypress.native="handleKeyPress">
                <el-icon class="search-icon" :size="20" @click="load_search" style="cursor: pointer"><Search /></el-icon>


            </div>
            <div class="projects-section" >

                <div class="projects-section-header">
                    <p class="status-number" style="text-align: left">Learn and Live.</p>
                    <p style="text-align: right">2023年</p>
                    <p class="time" style="text-align: right">{{month}}{{day}}</p>
                </div>

                <div class="projects-section-line" >
                    <div class="projects-status">
                        <div class="item-status" style="text-align:left">
                            <p class="status-number" style="text-align:left">{{total}}</p>
                            <p class="status-type">当前自习室总数</p>
                        </div>
                        <div class="item-status">
                            <p class="status-number" style="text-align:left">{{userOnlineNum}}</p>
                            <p class="status-type">在线人数</p>
                        </div>

                    </div>

                </div>
                <div class="project-boxes jsGridView" style="margin-top: 2vh">
                    <div class="project-box-wrapper" style="width:26%;margin-bottom: 25px;">

                        <div class="project-box" style="background-color: #fee4cb;height: 27vh">
                            <div class="project-box-header" style="font-size: large">
                                <span>创建属于我的自习室</span>
                            </div>

                            <div class="box-progress-bar">
                                <span style="white-space: pre-wrap;"></span>
                                <span class="box-progress" style="margin-top:2vh ; width: 100%; background-color: #ff942e"></span>
                            </div>
                            <div >

                                <el-icon    :size="65"  color="#ff942e" style="margin-top: 3vh;cursor: pointer" @click="dialogFormVisible = true;this.form={}">
                                    <CirclePlusFilled />
                                </el-icon>

                            </div>
                            <el-button text  style="margin-bottom: 10%;align-self:center;font-size: large" >
                                👆戳我新建自习室
                            </el-button>

                            <el-dialog v-model="dialogFormVisible" title="新建自习室">
                                <el-form :model="form">
                                    <el-form-item label="自习室名称" :label-width="formLabelWidth">
                                        <el-input v-model="form.roomName" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="自习室简介" :label-width="formLabelWidth">
                                        <el-input v-model="form.roomAnnouncement" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="自习室人数上限" :label-width="formLabelWidth">
                                        <el-input v-model="form.maxNum" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="自习室类型" :label-width="formLabelWidth">
                                        <el-select placeholder="Please select a type" v-model="form.roomType">
                                            <el-option label="公开" value="公开" />
                                            <el-option label="不公开" value="不公开" />
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="设置自习室密码" :label-width="formLabelWidth" v-if="form.roomType === '不公开'">
                                        <el-input v-model="form.roomPassword" autocomplete="off" />
                                    </el-form-item>
                                </el-form>
                                <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirm">Confirm</el-button>
      </span>
                                </template>
                            </el-dialog>
                        </div>
                    </div>


                    <div v-for="(project, index) in displayProjectBoxes" :key="index">
                        <div class="project-box-wrapper" style="width:20vw;height:30vh;margin-bottom: 25px;">
                            <div class="project-box" :style="{ backgroundColor: colors[index%5].bcolor}">
                                <div class="project-box-header" style="font-size: large">
                                    <span>{{ project.roomName }}</span>
                                </div>
                                <div class="box-progress-bar" style="margin-top:2vh ; width: 100%;">
                                    <span class="box-progress" :style="{ backgroundColor: colors[index%5].fcolor }"></span>
                                </div>
                                <el-text class="mx-1">自习室宣言：{{project.roomAnnouncement}}</el-text>
                                <br>
                                <el-text class="mx-1" >自习室ID：{{project.rid}}</el-text>
                                <br>
                                <el-text  class="mx-1">建立时间：{{project.startTime}}</el-text>
                                <br>

                                <el-text class="mx-1" >当前人数：{{project.preNum}}</el-text>
                                <div class="flex" >

                                            <el-button type="primary" :icon="Plus" :style="{borderColor:colors[index%5].fcolor, backgroundColor: colors[index%5].fcolor,marginTop: '2vh'} "
                                                       @click="dialogFormVisible1 = true;this.form = {};ii=index">加入自习室
                                            </el-button>
                                    <el-dialog v-model="dialogFormVisible1" title="人数已达上限" v-if="projects[ii].maxNum <= projects[ii].preNum">

                                        <template #footer>
                                                <span class="dialog-footer">
                                                <el-button @click="dialogFormVisible1 = false">知道了</el-button>

                                                </span>
                                        </template>

                                    </el-dialog>

                                                <el-dialog v-model="dialogFormVisible1" title="check密码" v-if="projects[ii].roomPassword !=='-1'  && projects[ii].maxNum > projects[ii].preNum">
                                                    <el-form :model="form">
                                                        <el-form-item label="请输入密码" :label-width="formLabelWidth">
                                                            <el-input v-model="checkPassword" autocomplete="off" />
                                                        </el-form-item>

                                        </el-form>
                                        <template #footer>
                                                <span class="dialog-footer">
                                                <el-button @click="dialogFormVisible1 = false">Cancel</el-button>
                                                <el-button type="primary" @click="checkpassword(ii, checkPassword)">Confirm</el-button>

                                                </span>
                                        </template>

                                    </el-dialog>


                                            <el-dialog v-model="dialogFormVisible1" title="确认加入自习室" v-if="projects[ii].roomPassword ==='-1' && projects[ii].maxNum >projects[ii].preNum">
                                                <el-form :model="form">
<!--                                                    <el-text class="mx-1">自习室：{{projects[ii].roomName}}</el-text>-->
                                                </el-form>
                                                <template #footer>
                                                        <span class="dialog-footer">

                                                        <el-button @click="dialogFormVisible1 = false">Cancel</el-button>
<!--                                                                <router-link to="name:'/rooms',params:{index:1}}" style="text-decoration: none" >-->
                                                        <el-button type="primary" @click="checkpassword(ii,'-1')">Confirm</el-button>
<!--                                                                    </router-link>-->
                                                        </span>
                                        </template>

                                    </el-dialog>





                                </div>
                            </div>
                        </div>

                    </div>

                </div>

                <div style="display: flex; justify-content: center;">
                    <el-pagination
                            v-model:current-page="currentPage"
                            :page-size="5"
                            :small="small"
                            :disabled="disabled"
                            :background="background"
                            layout="total, prev, pager, next, jumper"
                            :total="total"
                            @current-change="handleCurrentChange(currentPage)"
                    />
                </div>

            </div>
        </div>
    </div>


    <aside style="z-index:0">
        <user-aside/>
    </aside>
</template>


<script >
import userAside from '../components/userAside.vue';
import userHeader from '../components/userHeader.vue';
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import {CirclePlus, CirclePlusFilled, Plus, Search} from '@element-plus/icons-vue'
import {reactive, ref, watch} from 'vue'
import request from "@/utils/request";
import {left} from "core-js/internals/array-reduce";
import FriendsModule from "@/views/FriendsModule/FriendsModule.vue";
import {leaveTime} from "@/views/Room.vue";
// import moment from 'moment';



export default {
    name: 'studyHall',
    inject:['isNightMode','isView'],
    mounted() {
        leaveTime(this.isView);
        watch(
            () => this.isNightMode.value,
            (newValue, oldValue) => {
                this.nightModeChanged();
            }
        );
        if(this.isNightMode.value)
        {
            this.nightModeChanged();
        };
        const now = new Date();
        this.month =  (now.getMonth() + 1).toString().padStart(2, "0") + "月" ;
        this.day =   now.getDate().toString().padStart(2, "0")+ "日";

        setInterval(() => {
            const now = new Date();
            this.month = (now.getMonth() + 1) + "月"
            this.day =  now.getDate()+ "日";

        }, 1000);
    },

    components: {
        FriendsModule,
        CirclePlusFilled,
        CirclePlus,
        userAside,
        userHeader,
        ElForm,
        ElFormItem,
        ElInput,
        ElButton,
        Search,
        Plus,
    },
    computed: {
        Search() {
            return Search
        },
        Plus() {
            return Plus
        },
        displayProjectBoxes() {
            const start = (this.currentPage - 1) * this.maxPerPage; // 起始索引
            const end = start + this.maxPerPage; // 结束索引
            //console.log(start)
            //console.log(end)
            //console.log(this.projects)
            //console.log(this.projects.slice(start, end))
            return this.projects; // 根据索引范围获取需要显示的项目
        }
    },
    data() {
        return {
            userOnlineNum: 0,
            ifnewARoom:false,
            username: '',
            ii: 0,
            password: '',
            search: '',
            month: "",
            day: "",
            checkPassword:'',
            dialogTableVisible : ref(false),
            dialogFormVisible : ref(false),
            dialogTableVisible1 : ref(false),
            dialogFormVisible1 : ref(false),
            formLabelWidth : '140px',
            maxPerPage: 5, // 每页最多显示的 project-box 数量
            currentPage: 1, // 当前显示的页码
            pageSize: 5,
            total: 0,
            form : {},
            colors:[{bcolor:'#e8e6fc',fcolor:'#4f3fef'},{bcolor:'#daf5fc' ,fcolor:'#096c85'},{bcolor:'#fdd2e1',fcolor:'#de3670'
            },{bcolor:'#c7f6db',fcolor:'#34c371'},{bcolor:'#d4ddfe',fcolor:'#4067f8'}],
            projects: [
            ]

        }
    },
    created() {
        // let userJson = sessionStorage.getItem( "user")
        // if(!userJson) {
        //     router.push("/login")
        // }
        // this.ii = 0

        this.currentPage = 1; // 初始化当前页码
        this.load();
        setTimeout(() => {
            this.fetchTotal();
        }, 5000); // 5秒后第一次执行
    },
    methods: {

        handlePageChange(currentPage) {
            this.currentPage = currentPage; // 更新当前页码的值
            this.load()
        },

        fun(ridd){
            this.$router.push({path:'/rooms',query:{index:ridd}});
        },
        load() {
            let userJson = sessionStorage.getItem("user")
            const temporaryUser = JSON.parse(userJson)

            request.get("/api/studyHall", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: ''
                }
            }).then(res => {
                //console.log(res.data.records)
                this.projects = res.data.records

                this.projects.forEach(project => {


                    const datetime = new Date(project.startTime);

                    const year = datetime.getFullYear();
                    const month = String(datetime.getMonth() + 1).padStart(2, '0');
                    const day = String(datetime.getDate()).padStart(2, '0');
                    const hours = String(datetime.getHours()).padStart(2, '0');
                    const minutes = String(datetime.getMinutes()).padStart(2, '0');
                    const seconds = String(datetime.getSeconds()).padStart(2, '0');

                    const formattedDatetime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

                    // 使用moment.js格式化日期
                    project.startTime = formattedDatetime;
                });

                this.total = res.data.total
            })
            this.getOnlineNum()
        },
        async fetchTotal() {
            await request.get("/api/studyHall", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: this.search
                }
            }).then(res => {

                this.projects = res.data.records;
                this.projects.forEach(project => {

                    const datetime = new Date(project.startTime);

                    const year = datetime.getFullYear();
                    const month = String(datetime.getMonth() + 1).padStart(2, '0');
                    const day = String(datetime.getDate()).padStart(2, '0');
                    const hours = String(datetime.getHours()).padStart(2, '0');
                    const minutes = String(datetime.getMinutes()).padStart(2, '0');
                    const seconds = String(datetime.getSeconds()).padStart(2, '0');

                    const formattedDatetime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

                    // 使用moment.js格式化日期
                    project.startTime = formattedDatetime;
                });

                this.total = res.data.total;

                // 请求完成后设置下一次定时任务
                setTimeout(() => {
                    this.fetchTotal();
                }, 5000);

            }).catch(error => {
                console.error('Failed to fetch total:', error);

                // 请求失败后设置下一次定时任务
                setTimeout(() => {
                    this.fetchTotal();
                }, 5000);

            });
        },
        handleCurrentChange(pageNum) {
            this.currentPage = pageNum
            this.load()
        },
        handleSizeChange(pageSize) {
            this.pageSize = pageSize
            this.load()
        },
        getOnlineNum(){
            request.get("/api/studyHall/getOnlineUserNum").then(res => {
                this.userOnlineNum = res.data
            })
        },
        load_search() {
            request.get("/api/studyHall", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: this.search
                }
            }).then(res => {
                // console.log(res)
                this.projects = res.data.records
                this.projects.forEach(project => {


                    const datetime = new Date(project.startTime);

                    const year = datetime.getFullYear();
                    const month = String(datetime.getMonth() + 1).padStart(2, '0');
                    const day = String(datetime.getDate()).padStart(2, '0');
                    const hours = String(datetime.getHours()).padStart(2, '0');
                    const minutes = String(datetime.getMinutes()).padStart(2, '0');
                    const seconds = String(datetime.getSeconds()).padStart(2, '0');

                    const formattedDatetime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;


                    // 使用moment.js格式化日期
                    project.startTime = formattedDatetime;
                });
                this.total = res.data.total
            })
        },
        confirm() {
            let userJson = sessionStorage.getItem("user")
            const temporaryUser = JSON.parse(userJson)
            this.form.roomOwner = temporaryUser.uid

            request.post("/api/studyHall", this.form).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "新增成功"
                    })

                    this.load()
                    this.dialogFormVisible = false
                } else {
                    this.$message({
                        type: "error",
                        message: res.msg
                    })
                }
            })
        },
        nightModeChanged()
        {
            if (this.isNightMode.value) {
                // 切换到黑夜
                document.querySelector('.search-input').style.backgroundColor = '#1f2938';

                document.querySelector('.app-name').style.color='#ffffff';
                document.querySelector('.projects-section').style.backgroundColor='#1f2938';
                document.querySelector('.projects-status').style.color='#ffffff';
                document.querySelector('.projects-section-header').style.color='#ffffff';

                const icons = document.querySelectorAll('.el-icon');
                icons.forEach(icon => {
                    icon.style.color = '#f1f4fc';
                });

                document.querySelector('.search-wrapper').style.backgroundColor='#1f2938';
                document.querySelector('.search-icon').style.color='#ffffff';


            } else {
                // 切换到白天
                document.querySelector('.search-input').style.backgroundColor = '#ffffff';

                document.querySelector('.app-name').style.color = '#333';
                document.querySelector('.projects-section').style.backgroundColor = '#ffffff';
                document.querySelector('.projects-status').style.color = '#000000';
                document.querySelector('.projects-section-header').style.color = '#000000';


                const icons = document.querySelectorAll('.el-icon');
                icons.forEach(icon => {
                    icon.style.color = '#111828';
                });

                document.querySelector('.search-wrapper').style.backgroundColor = '#ffffff';
            }
        },
        checkpassword(index, passw){
            let userJson = sessionStorage.getItem(  "user" )
            const temporaryUser = JSON.parse(userJson)

            request.post("/api/studyHall/checkpsw/" + this.projects[index].rid + "/" + passw + "/" + temporaryUser.uid).then(res => {
                if(res.code === "0") {
                    this.$message(
                        {type: "success", message: "加入成功"}
                    )

                    let userJson = sessionStorage.getItem(  "user" )
                    let tu = JSON.parse(userJson)
                    tu.roomId = this.projects[index].rid
                    sessionStorage.setItem("user", JSON.stringify(tu))
                    console.log("666")
                    console.log(res.data)
                    sessionStorage.setItem("room", JSON.stringify(res.data))

                    this.load()
                    this.dialogFormVisible1=false
                    this.$router.push({path:'/room'})
                } else {
                    this.load()
                    this.$message(
                        {type: "error", message: res.msg}
                    )
                }
            })
        }
    }
}

</script>


<style scoped>


* {
    box-sizing: border-box;
}



.el-icon:not(.CirclePlusFilled) {
    color: #fd932e !important;
}
:root {
    --app-container: #f3f6fd;
    --main-color: #1f1c2e;
    --secondary-color: #4A4A4A;
    --link-color: #1f1c2e;
    --link-color-hover: #c3cff4;
    --link-color-active: #fff;
    --link-color-active-bg: #1f1c2e;
    --projects-section: #fff;
    --message-box-hover: #fafcff;
    --message-box-border: #e9ebf0;
    --more-list-bg: #fff;
    --more-list-bg-hover:  #f6fbff;
    --more-list-shadow: rgba(209, 209, 209, 0.4);
    --button-bg: #1f1c24;
    --search-area-bg: #fff;
    --star: #1ff1c2e;
    --message-btn: #fff;
}

.app-container {
    background-color: #f3f6fd;
}
.projects-section {
    background-color: #fff;
}



html, body {
    width: 100%;
    height: 100vh;
    margin: 0;
}

body {
    font-family: 'DM Sans', sans-serif;
    overflow: hidden;
    display: flex;
    justify-content: center;
    background-color: var(--app-container);
}

button, a {
    cursor: pointer;
}

.status-number {
    display: flex;
    justify-content: left;
    align-items: flex-start;
}
.projects-section-line {
    margin-top: -10px;
}

.status-type {
    display: flex;
    justify-content: center;
    align-items: center;
}

.app {
    &-container {
        width: 100%;
        display: flex;
        flex-direction: column;
        height: 100%;
        background-color: var(--app-container);
        transition: .2s;
        max-width: 1800px;

        button, input, optgroup, select, textarea {
            font-family: 'DM Sans', sans-serif;
        }
    }

    &-content {
        display: flex;
        height: 100%;
        overflow: hidden;
        padding: 16px 24px 24px 0;
    }

    &-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        padding: 16px 24px;
        position: relative;

        &-left, &-right {
            display: flex;
            align-items: center;
        }

        &-left { flex-grow: 1; }

        &-right button {
            margin-left: 10px;
        }
    }

    &-icon {
        width: 26px;
        height: 2px;
        border-radius: 4px;
        background-color: var(--main-color);
        position: relative;

        &:before, &:after {
            content: '';
            position: absolute;
            width: 12px;
            height: 2px;
            border-radius: 4px;
            background-color: var(--main-color);
            left: 50%;
            transform: translatex(-50%);
        }

        &:before { top: -6px; }
        &:after { bottom: -6px; }
    }

    &-name {
        color: var(--main-color);
        margin: 0;
        font-size: 40px;
        line-height: 24px;
        font-weight: 700;
        margin: 0 32px;
    }
}



.search-wrapper {
    border-radius: 20px;
    padding-right: 12px;
    height: 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    max-width: 480px;
    color: var(--light-font);
    box-shadow: 0 2px 6px 0 rgba(136,148,171,.2),0 24px 20px -24px rgba(71,82,107,.1);
    overflow: hidden;
    background-color: var(--search-area-bg);

    .dark & { box-shadow: none; }
}

.search-input {
    border: none;
    flex: 1;
    outline: none;
    height: 100%;
    padding: 0 20px;
    font-size: 16px;
    background-color: var(--search-area-bg);
    color: var(--main-color);

    &:placeholder {
        color: var(--main-color);
        opacity: .6;
    }
}



.projects-section {
    flex: 2;
    background-color: #ffffff;
    border-radius: 32px;
    padding: 32px 32px 0 32px;
    overflow: hidden;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;

    &-line {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-bottom: 32px;
    }

    &-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        color: var(--main-color);

        p {
            font-size: 24px;
            line-height: 32px;
            font-weight: 700;
            opacity: .9;
            margin: 0;
            color: var(--main-color);
        }

        .time {
            font-size: 20px;
        }
    }
}

.projects-status {
    display: flex;
}

.item-status {
    display: flex;
    flex-direction: column;
    margin-right: 16px;

    &:not(:last-child) .status-type:after {
        content: '';
        position: absolute;
        right: 8px;
        top: 50%;
        transform: translatey(-50%);
        width: 6px;
        height: 6px;
        border-radius: 50%;
        border: 1px solid var(--secondary-color);
    }
}
.search-box {
    position: relative;
    display: inline-block;
    margin-left: 30px;
    background-color: #ffffff;
}

.status-number {
    font-size: 24px;
    line-height: 32px;
    font-weight: 700;
    color: var(--main-color);
}

.status-type {
    position: relative;
    padding-right: 24px;
    color: var(--secondary-color);
}

.projects-section-line {
    margin-bottom: 22px;
}
.project-boxes {
    margin: 0 -8px;
    overflow-y: auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    &.jsGridView {
        display: flex;
        flex-wrap: wrap;

        .project-box-wrapper {
            width:33.3%;

        }
    }


}

.project-box {
    --main-color-card: #dbf6fd;
    border-radius: 30px;
    padding: 16px;
    background-color: var(--main-color-card);
    height: 27vh;
//width: calc(20% - 24px); /* 一行排列3个，减去左右两侧的8像素的padding */


    &-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16px;
        color: var(--main-color);

        span {
            color: #4A4A4A;
            opacity: .7;
            font-size: 14px;
            line-height: 16px;
        }
    }

    &-content-header {
        text-align: center;
        margin-bottom: 16px;

        p { margin: 0; }
    }

    &-wrapper {
        padding: 8px;
        transition: .2s;
    //width:40%;
        width: calc(33.33% - 16px); /* 每行三个，减去左右两侧的 8 像素的 padding */
        margin-bottom: 24px;

    }
}

.project-btn-more {
    padding: 0;
    height: 14px;
    width: 24px;
    height: 24px;
    position: relative;
    background-color: transparent;
    border: none;
    flex-shrink: 0;

    /*&:after, &:before {
      content: '';
      position: absolute;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: var(--main-color);
      opacity: .8;
      left: 50%;
      transform: translatex(-50%);
    }

    &:before { top: 0;}
    &:after { bottom: 0; }*/
}

.more-wrapper {
    position: relative;
}

.box-content-header {
    font-size: 16px;
    line-height: 24px;
    font-weight: 700;
    opacity: .7;
}

.box-content-subheader {
    font-size: 14px;
    line-height: 24px;
    opacity: .7;
}

.box-progress {
    display: block;
    height: 4px;
    border-radius: 6px;

    &-bar {
        width: 100%;
        height: 4px;
        border-radius: 6px;
        overflow: hidden;
        background-color: #fff;
        margin: 8px 0;
    }

    &-header {
        font-size: 14px;
        font-weight: 700;
        line-height: 16px;
        margin: 0;
    }

    &-wrapper {

    }

    &-percentage {
        text-align: right;
        margin: 0;
        font-size: 14px;
        font-weight: 700;
        line-height: 16px;
    }
}

.project-box-footer {
    display: flex;
    justify-content: space-between;
    padding-top: 16px;
    position: relative;

    &:after {
        content: '';
        position: absolute;
        background-color: rgba(255,255,255,0.6);;
        width: calc(100% + 32px);
        top: 0;
        left: -16px;
        height: 1px;
    }
}



.days-left {
    background-color: rgba(255,255,255,0.6);
    font-size: 12px;
    border-radius: 20px;
    flex-shrink: 0;
    padding: 6px 16px;
    font-weight: 700;
}

.mode-switch.active .moon {
    fill: var(--main-color);
}

.el-button--text {
    margin-right: 15px;
}
.el-select {
    width: 300px;
}
.el-input {
    width: 300px;
}
.dialog-footer button:first-child {
    margin-right: 10px;
}

.example-pagination-block + .example-pagination-block {
    margin-top: 10px;
}
.example-pagination-block .example-demonstration {
    margin-bottom: 16px;
}
/*
@media screen and (max-width: 980px) {
    .project-boxes.jsGridView .project-box-wrapper {
        width: 50%;
    }

    .status-number, .status-type {
        font-size: 14px;
    }

    .status-type:after {
        width: 4px;
        height: 4px;
    }

    .item-status { margin-right: 0; }
}

@media screen and (max-width: 880px) {
    .messages-section {
        transform: translateX(100%);
        position: absolute;
        opacity: 0;
        top: 0;
        z-index: 2;
        height: 100%;
        width: 100%;

        .messages-close { display: block; }
    }

    .messages-btn { display: flex; }
}

@media screen and (max-width: 720px) {
    .app-name, .profile-btn span { display: none; }

    .add-btn, .notification-btn, .mode-switch {
        width: 20px;
        height: 20px;

        svg {
            width: 16px;
            height: 16px;
        }
    }

    .app-header-right button {
        margin-left: 4px;
    }
}

@media screen and (max-width: 520px) {
    .projects-section { overflow: auto; }
    .project-boxes { overflow-y: visible; }

    .app-sidebar, .app-icon { display: none; }

    .app-content { padding: 16px 12px 24px 12px;}

    .status-number, .status-type {
        font-size: 10px;
    }

    .view-btn {
        width: 24px;
        height: 24px;
    }

    .app-header {
        padding: 16px 10px;
    }

    .search-input {
        max-width: 120px;
    }

    .project-boxes.jsGridView .project-box-wrapper {
        width: 30%;
    }

    .projects-section {
        padding: 24px 16px 0 16px;
    }

    .profile-btn img {
        width: 24px;
        height: 24px;
    }

    .app-header {
        padding: 10px;
    }

    .projects-section-header p,
    .projects-section-header .time{
        font-size: 18px;
    }

    .status-type {
        padding-right: 4px;

        &:after {
            display: none;
        }
    }

    .search-input {
        font-size: 14px;
    }

    .messages-btn { top: 48px; }

    .box-content-header {
        font-size: 12px;
        line-height: 16px;
    }

    .box-content-subheader {
        font-size: 12px;
        line-height: 16px;
    }

    .project-boxes.jsListView .project-box-header > span {
        font-size: 10px;
    }

    .box-progress-header {
        font-size: 12px;
    }

    .box-progress-percentage {
        font-size: 10px;
    }

    .days-left {
        font-size: 8px;
        padding: 6px 6px;
        text-align: center;
    }

    .project-boxes.jsListView .project-box > * {
        margin-right: 10px;
    }

    .project-boxes.jsListView .more-wrapper {
        right: 2px;
        top: 10px;
    }
}*/
</style>

