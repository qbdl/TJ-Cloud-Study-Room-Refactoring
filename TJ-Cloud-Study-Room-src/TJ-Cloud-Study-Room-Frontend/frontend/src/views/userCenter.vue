<template>

    <userHeader ref="Header"/>

    <FriendsModule />

    <el-row>
        <el-col :span="3"/>
        <el-col :span="21">
            <div class="userCenterBackground">
                <!--头像-->
                <div style= "text-align: center; margin-top: 50px; margin-bottom: 50px">
                    <el-upload
                        class="avatar-uploader"
                        ref="avatarUpload"
                        :accept="acceptedFileTypes"
                        action="/api/files/upload"
                        :limit="1"
                        :file-list="fileList"
                        :show-file-list="false"
                        :on-exceed="handleExceed"
                        :on-success="UpdateAvatar"
                        :before-upload="CheckAvatarSize"
                    >
                        <el-avatar v-if="userProfile.picture" :size="200" :src="userProfile.picture" class="avatar" />
                        <el-avatar v-else :size="200" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
                    </el-upload>
                </div>

                <!--个人信息标题与修改-->
                <div style="margin: auto; margin-bottom: 20px; width: 700px">
                    <el-row class="userCenterDescriptionRow">
                        <el-col :span="8">
                            <div class="userCenterHeaderContainer">
                                <p class="userCenterHeader" :style="{color: textColor}">
                                    个人信息
                                </p>

                                <el-button text @click="EditInfo" style="margin-left: 10px">
                                    <el-icon>
                                        <edit />
                                    </el-icon>
                                </el-button>
                            </div>

                            <el-dialog v-model="editInfoVisible" title="个人信息修改" style="border-radius: 20px; width: 500px">
                                <el-form
                                        ref="personalInfoForm"
                                        :rules="personalInfoFormRules"
                                        :label-position="'left'"
                                        :label-width="'100px'"
                                        :model="editUserProfile">
                                    <el-form-item prop="userName" label="用户名">
                                        <el-input v-model="editUserProfile.userName" style="width: 300px" disabled/>
                                    </el-form-item>
                                    <el-form-item prop="sex" label="性别">
                                        <el-radio-group v-model="editUserProfile.sex" class="ml-4">
                                            <el-radio label="男" size="large">男</el-radio>
                                            <el-radio label="女" size="large">女</el-radio>
                                            <el-radio label="未知" size="large">未知</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item prop="major" label="专业">
                                        <el-input v-model="editUserProfile.major" style="width: 300px"/>
                                    </el-form-item>
                                    <el-form-item prop="educationLevel" label="在读学历">
                                        <el-select placeholder="请选择" v-model="editUserProfile.educationLevel" style="width: 150px">
                                            <el-option label="本科" value="本科" />
                                            <el-option label="硕士" value="硕士" />
                                            <el-option label="博士" value="博士" />
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item prop="grade" label="年级">
                                        <el-select placeholder="请选择" v-model="editUserProfile.grade" style="width: 150px">
                                            <el-option label="一年级" value="一年级" />
                                            <el-option label="二年级" value="二年级" />
                                            <el-option label="三年级" value="三年级" />
                                            <el-option label="四年级" value="四年级" />
                                            <el-option label="五年级" value="五年级" />
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item prop="phoneNumber" label="电话号码">
                                        <el-input v-model="editUserProfile.phoneNumber" style="width: 300px"/>
                                    </el-form-item>
                                    <el-form-item prop="mailAddress" label="电子邮箱">
                                        <el-input v-model="userProfile.mailAddress" style="width: 300px"/>
                                    </el-form-item>


                                </el-form>
                                <template #footer>
                                    <span class="dialog-footer">
                                        <el-button text @click="UpdateInfo">
                                            <el-icon size="30">
                                                <check />
                                            </el-icon>
                                        </el-button>
                                    </span>
                                </template>
                            </el-dialog>
                        </el-col>

                        <el-col :span="13"/>

                        <el-col :span="1">
                            <el-button text bg type="primary" @click="EditPwd">
                                修改密码
                            </el-button>

                            <el-dialog v-model="editPwdVisible" title="密码修改" style="border-radius: 20px; width: 500px">
                                <el-form
                                    ref="pwdForm"
                                    :rules="pwdRules"
                                    :label-position="'left'"
                                    :label-width="'100px'"
                                    :model="editPwd">
                                    <el-form-item prop="curPwd" label="当前密码">
                                        <el-input
                                            v-model="editPwd.curPwd"
                                            type="password"
                                            show-password
                                            style="width: 300px"/>
                                    </el-form-item>

                                    <el-form-item prop="newPwd" label="新密码">
                                        <el-input
                                            v-model="editPwd.newPwd"
                                            type="password"
                                            show-password
                                            style="width: 300px"/>
                                    </el-form-item>

                                    <el-form-item prop="confirmPwd" label="新密码">
                                        <el-input
                                            v-model="editPwd.confirmPwd"
                                            type="password"
                                            show-password
                                            style="width: 300px"/>
                                    </el-form-item>
                                </el-form>
                                <template #footer>
                                    <span class="dialog-footer">
                                        <el-button text @click="UpdatePwd">
                                            <el-icon size="30">
                                                <check />
                                            </el-icon>
                                        </el-button>
                                    </span>
                                </template>
                            </el-dialog>
                        </el-col>
                    </el-row>
                </div>

                <!--个人信息显示-->
                <div  class="userCenterDescription" :style="{'--background-color': backgroundColor}">
                    <el-row class="userCenterDescriptionRow">
                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <user />
                                </el-icon>
                                用户名
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            {{ userProfile.userName }}
                        </el-col>

                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <loading />
                                </el-icon>
                                性别
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            <div>
                                {{ userProfile.sex }}
                            </div>
                        </el-col>
                    </el-row>

                    <el-row class="userCenterDescriptionRow">
                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <trophy />
                                </el-icon>
                                年级
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            {{(userProfile.educationLevel==="未知" || userProfile.grade === "未知") ?
                                "未知" : userProfile.educationLevel + userProfile.grade }}
                        </el-col>

                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <collection />
                                </el-icon>
                                专业
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            {{userProfile.major }}
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <iphone />
                                </el-icon>
                                电话号码
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            {{ userProfile.phoneNumber }}
                        </el-col>

                        <el-col :span="4">
                            <div class="cell-item" :style="{color: textColor}">
                                <el-icon :style="iconStyle">
                                    <postcard />
                                </el-icon>
                                电子邮箱
                            </div>
                        </el-col>
                        <el-col :span="8" :style="{color: textColor}">
                            {{ userProfile.mailAddress }}
                        </el-col>
                    </el-row>
                </div>

                <!--个人课表标题-->
                <div style="margin: auto; margin-top: 20px; margin-bottom: 20px; width: 700px">
                    <el-row>
                        <el-col :span="8">

                            <div class="userCenterHeaderContainer">
                                <p class="userCenterHeader" :style="{color: textColor}">个人课表</p>

                                <el-popover
                                    placement="right"
                                    :width="200"
                                    trigger="hover"
                                    content="点击单元格以修改课程表"
                                >
                                    <template #reference>
                                        <el-button text style="margin-left: 10px">
                                            <el-icon><edit /></el-icon>
                                        </el-button>
                                    </template>
                                </el-popover>
                            </div>

                        </el-col>
                    </el-row>
                </div>

                <!--个人课表显示-->
                <div style="margin: auto; width: 700px">
                    <timetable :class-num-per-day="12">
                    </timetable>
                </div>

            </div>
        </el-col>
    </el-row>


    <aside style>
        <userAside/>
    </aside>

</template>

<script>
import {
    Check, Close,
    Collection, Edit,
    Iphone, Loading, Plus,
    Postcard,
    Trophy,
    User
} from "@element-plus/icons-vue";
import request from "@/utils/request";
import timetable from "@/components/timetable.vue";
import userAside from "@/components/userAside.vue";
import userHeader from "@/components/userHeader.vue";
import router from "@/router";
import {watch} from "vue";
import md5 from 'js-md5'
import FriendsModule from "@/views/FriendsModule/FriendsModule.vue";

export default {
    name: "userCenter",
    components: {
        FriendsModule,
        Plus,
        userHeader,
        userAside, timetable, Close, Check, Edit, Postcard, Collection, Loading, Trophy, User, Iphone},

    inject: ['isNightMode'],

    mounted() {
        watch(
            () => this.isNightMode.value,
            (newValue, oldValue) => {
                this.nightModeChanged();
            }
        );
        if(this.isNightMode.value){
            this.nightModeChanged();
        }
    },

    data(){
        return{
            userProfile:{
                userName: 'Tianyi Zhang',
                pwd: md5('123'),
                sex: '女',
                major: 'CS',
                grade: '大一',
                educationLevel: '本科',
                phoneNumber: '12345678901',
                mailAddress: '00@qq.com',
                picture: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
            },
            fileList:[],

            editPwd: {},
            editPwdVisible: false,
            pwdRules: {
                curPwd: [
                    { required: true, message: '请输入当前密码', trigger: 'blur'},
                    { validator: (rule, value, callback) => {
                            if (md5(value) === this.userProfile.pwd) {
                                callback()
                            } else {
                                callback(new Error('密码错误'))
                            }
                        }, trigger: 'blur' }
                ],
                newPwd: [
                    { required: true, message: '请输入新密码', trigger: 'blur'},
                ],
                confirmPwd: [
                    { required: true, message: '请确认密码', trigger: 'blur'},
                    { validator: (rule, value, callback) => {
                            if (value === this.editPwd.newPwd) {
                                callback()
                            } else {
                                callback(new Error('两次密码输入不一致'))
                            }
                        }, trigger: 'blur' }
                ],
            },

            editUserProfile: {},
            editInfoVisible: false,
            editTimetableVisible: false,
            personalInfoFormRules: {
                sex: [
                    { required: true, message: '请选择性别', trigger: 'change' },
                ],
                educationLevel: [
                    { required: true, message: '请选择在读学历', trigger: 'change' },
                ],
                grade: [
                    { required: true, message: '请选择年级', trigger: 'change' },
                ],
                major: [
                    { required: true, message: '请输入专业', trigger: 'blur' },
                ],
                phoneNumber: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                ],
                mailAddress: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                ],
            },
            textColor: '#000000',
            backgroundColor: '#ffffff',
            acceptedFileTypes: 'image/jpeg, image/png, image/gif',
        }
    },

    created() {
        let userJson = sessionStorage.getItem("user")
        if(!userJson) {
            router.push("/login")
        }
        this.load()
    },

    methods: {
        load() {
            let userJson = sessionStorage.getItem("user")
            const temporaryUser = JSON.parse(userJson)
            request.get("/api/userCenter/userProfile", {
                params: {
                    search: temporaryUser.userName
                }
            }).then(res => {
                this.userProfile = res.data
            })
        },
        /* 上传头像函数 */
        UpdateAvatar(avatarURL){
            // 建立编辑拷贝
            this.editUserProfile = JSON.parse(JSON.stringify(this.userProfile))
            // 修改编辑拷贝
            this.editUserProfile.picture = avatarURL.data
            // 后端对接
            request.put("/api/userCenter/userProfile", this.editUserProfile).then(res => {
                if(res.code === "0") {
                    this.$message(
                        {type: "success", message: "修改成功"}
                    )
                    // 修改当前用户信息
                    this.userProfile = JSON.parse(JSON.stringify(this.editUserProfile))
                    // session更新
                    sessionStorage.setItem("user", JSON.stringify(this.userProfile))
                    // 更新当前界面
                    this.load()
                    // 更新header
                    this.$refs.Header.load()
                    // 清空编辑拷贝与文件列表
                    this.editUserProfile = {}
                    this.fileList = []
                } else {
                    this.$message(
                        {type: "error", message: res.msg}
                    )
                }
            })
        },
        /* 文件数量超出范围 */
        handleExceed() {
            this.$message.warning(
                "一次只能上传1个文件"
            );
        },
        // 检查文件大小
        CheckAvatarSize(file){
            if (file.size > 1024 * 1024) {
                this.$message.error('头像文件大小不能超过1MB');
                return false;
            }
            return true;
        },
        EditInfo(){
            this.editUserProfile = JSON.parse(JSON.stringify(this.userProfile))
            this.editInfoVisible = true
        },
        UpdateInfo() {
            this.$refs.personalInfoForm.validate((valid) => {
                if (valid) {
                    request.put("/api/userCenter/userProfile", this.editUserProfile).then(res => {
                        if(res.code === "0") {
                            this.$message(
                                {type: "success", message: "修改成功"}
                            )
                            this.editInfoVisible = false
                            this.editUserProfile = {}
                            this.load()
                        } else {
                            this.$message(
                                {type: "error", message: res.msg}
                            )
                        }
                    })
                } else {
                    console.log('校验表单失败');
                    return false;
                }
            });

        },
        nightModeChanged(){
            if(this.isNightMode.value) {
                document.querySelector('.userCenterBackground').style.backgroundColor = '#1f2938'
                this.backgroundColor = '#1f2938'
                this.textColor = 'white';

                const icons = document.querySelectorAll('.el-icon:not(.CirclePlusFilled)');
                icons.forEach(icon => {
                    icon.style.color = '#f1f4fc';
                });
            }
            else{
                document.querySelector('.userCenterBackground').style.backgroundColor = 'white'
                this.backgroundColor = 'white'
                this.textColor = 'black';

                const icons = document.querySelectorAll('.el-icon:not(.CirclePlusFilled)');
                icons.forEach(icon => {
                    icon.style.color = '#111828';
                });
            }
        },
        EditPwd(){
            this.editUserProfile = JSON.parse(JSON.stringify(this.userProfile));
            this.editPwdVisible = true;
        },
        UpdatePwd(){
            this.$refs.pwdForm.validate((valid) => {
                if (valid) {
                    // md5 加密
                    this.editUserProfile.pwd = md5(this.editPwd.newPwd);
                    // 后端更新
                    request.put("/api/userCenter/userProfile", this.editUserProfile).then(res => {
                        if(res.code === "0") {
                            this.$message(
                                {type: "success", message: "修改成功"}
                            )
                            /*  ----------调试用，以下可删------ */
                            this.userProfile = this.editUserProfile
                            /*  ----------调试用，以上可删------ */
                            this.editPwdVisible = false
                            this.editUserProfile = {}
                            this.editPwd = {}
                            this.load()
                        } else {
                            this.$message(
                                {type: "error", message: res.msg}
                            )
                        }
                    })
                } else {
                    console.log('校验表单失败');
                    return false;
                }
            });
        }
    }
}
</script>

<style scoped>
.userCenterBackground{
    border-radius: 15px;
    padding: 20px;
    background-color: white;
    position: absolute;
    right: 0;
    width: calc(100% - 75px)
}

.userCenterHeader{
    font-size: 20px;
    font-weight: bold;
    width: 80px;
}

.userCenterHeaderContainer{
    display: flex;
}

.userCenterDescription{
    margin:auto;
    width:700px;
    --background-color: #f0f0f0;
    background-color: var(--background-color);
}

.userCenterDescriptionRow{
    margin-bottom: 20px;
}

</style>
