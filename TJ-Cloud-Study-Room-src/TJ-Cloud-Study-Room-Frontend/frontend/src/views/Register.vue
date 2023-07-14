<template>
    <div class="content">
        <div class="login-wrapper">
            <div class="left-img">
                <div class="glass">
                    <div class="tips">
                        <div class="title">
                            Learn and Live
                        </div>
                        <h2>TJ Cloud StudyRoom</h2>
                        <span>65472 people have joined our studyroom.</span>
                        <span>We invite you to start our journey</span>
                    </div>
                </div>
            </div>
            <div class="right-login-form">
                <div class="form-wrapper">
                    <h1 v-if="fromLogin">注 册</h1>
                    <h1 v-if="fromForget">找回密码</h1>
                    <div class="input-items" v-if="phonePageVisible">
                        <span class="input-tips">
                            手机号码
                        </span>
                        <div class="input-wrapper">
                            <input type="text" class="inputs" placeholder="请输入手机号码" v-model="user.phoneNumber">
                            <button class="get-code-btn" :disabled="verifyTimer > 0" @click="startTimerAndCheck">
                                {{ verifyTimer > 0 ? verifyTimer + '秒后重新获取' : '获取验证码' }}
                            </button>
                            <span class="error-tips error-tips3" v-show="this.user.phoneNumber.length!==11
                            &&checkPhoneNumberLengthBegin">手机号码长度应为11位</span>
                        </div>
                    </div>
                    <div class="input-items" v-if="phonePageVisible">
                        <span class="input-tips">
                            验证码
                        </span>
                        <input type="text" class="inputs" placeholder="请输入验证码" v-model="user.validateCode">
                        <span class="error-tips error-tips4" v-show="this.user.validateCode.length!==6
                        &&checkValidationCodeLengthBegin">验证码长度应为6位</span>
                    </div>
                    <div class="input-items" v-if="setPageVisible&fromLogin">
                        <span class="input-tips">
                            昵称
                        </span>
                        <input type="text" class="inputs" placeholder="请输入昵称" v-model="user.userName">
                        <span class="error-tips error-tips5" v-show="(this.user.userName.length>10||this.user.userName.length<3)
                        &&checkSetUsernameLengthBegin">用户名长度应在3-10个字符之间</span>
                    </div>
                    <div class="input-items"  v-if="setPageVisible">
                        <span class="input-tips">
                            密码
                        </span>
                        <input type="password" class="inputs" placeholder="请设置密码" v-model="user.pwd">
                        <span class="error-tips error-tips6" :style="{ top: fromLogin ? '57%' : fromForget ? '42%' : '' }"
                              v-show="(this.user.pwd.length>20||this.user.pwd.length<8)
                              &&checkPasswordLengthBegin">密码长度应在8-20个字符之间</span>
                    </div>
                    <div class="input-items"  v-if="setPageVisible">
                        <span class="input-tips">
                            确认密码
                        </span>
                        <input type="password" class="inputs" placeholder="请确认密码" v-model="user.confirmPassword">
                        <span class="error-tips error-tips7" :style="{ top: fromLogin ? '81%' : fromForget ? '75%' : '' }"
                              v-show="this.user.confirmPassword!==this.user.pwd
                        &&checkConfirmPasswordLengthBegin">确认密码与密码不匹配</span>
                    </div>
                    <div class="button-R">
                        <button class="btn-R"  @click="backToLogin" v-if="phonePageVisible">上一步</button>
                        <button class="btn-R"  @click="backOption" v-if="setPageVisible">上一步</button>
                        <button class="btn-R"  @click="nextOption" v-if="phonePageVisible">下一步</button>
                        <button class="btn-R"  @click="registerAndCheck" v-if="setPageVisible&fromLogin">注册</button>
                        <button class="btn-R"  @click="modifyAndCheck" v-if="setPageVisible&fromForget">更改</button>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";

import {
    UserFilled,
    Lock
} from '@element-plus/icons-vue'
import md5 from 'js-md5'
import {nextTick} from "vue";

export default {
    name: "Login",
    computed: {
        UserFilled() {
            return UserFilled
        },
        Lock() {
            return Lock
        }
    },
    data() {
        return {
            user: {
                userName: '',
                phoneNumber: '',
                pwd: '',
                validateCode: '',
                confirmPassword: ''
            },
            //设置账号密码页面显示
            setPageVisible: false,
            //手机号验证码页面显示
            phonePageVisible: true,
            //是否开始实时检测手机号码长度
            checkPhoneNumberLengthBegin: false,
            //是否开始实时检测验证码长度
            checkValidationCodeLengthBegin: false,
            //是否开始实时检测设置用户名长度
            checkSetUsernameLengthBegin: false,
            //是否开始实时检测设置密码长度
            checkPasswordLengthBegin: false,
            //是否开始实时检测设置确认密码长度
            checkConfirmPasswordLengthBegin: false,
            //路径来源检查变量
            fromLogin: false,
            fromForget: false,
            //下一步控制变量
            checkNextStep: false,
            //验证码部分变量
            verifyTimer: 0,
            verifyInterval: null,
            generatedCode: 0,
            verifiedPhoneNumber: ''
        }
    },
    methods: {
        /*******执行下一步函数*******/
        nextOption() {
            // 开始实时检测
            this.checkValidationCodeLengthBegin = true;
            this.checkPhoneNumberLengthBegin = true;

            // 实时检测未通过，退出
            if(this.user.validateCode.length!==6 || this.user.phoneNumber.length!==11)
                return false;

            // 验证码未通过
            if(this.generatedCode !== Number(this.user.validateCode)){
                this.$message.error("输入的验证码错误，请重试")
                return false
            }
            // 手机号未验证
            if(this.user.phoneNumber !== this.verifiedPhoneNumber){
                this.$message.error("手机号未验证")
                return false
            }
            // 检验：如果注册过了，不许再注册；如果没注册过，不许修改
            request.get("/api/checkPhoneNumber", {
                params: {
                    phoneNumber: this.user.phoneNumber
                }
            }).then(res => {
                if(res.code === '0' && this.fromLogin) {
                    this.$message.error("此手机号已注册")
                }
                else if(res.code !== '0' && this.fromForget){
                    this.$message.error("此手机号未注册")
                }
                else{
                    //切换到设置密码页面
                    if(this.user.phoneNumber.length===11&&this.user.validateCode.length===6){
                        this.setPageVisible = true;
                        this.phonePageVisible = false;
                        this.verifyTimer = 0;
                    }
                }
            })

        },

        /*******返回上一步函数*******/
        backOption() {
            //重置相关值
            this.checkPasswordLengthBegin = false;
            this.checkConfirmPasswordLengthBegin = false;
            this.checkSetUsernameLengthBegin = false;
            this.user.pwd = '';
            this.user.confirmPassword = '';
            this.user.userName = '';
            //切换到手机号验证码页面
            this.setPageVisible = false;
            this.phonePageVisible = true;
        },

        /*******返回登陆界面函数*******/
        backToLogin() {
            //重置相关值
            this.checkPhoneNumberLengthBegin = false;
            this.checkValidationCodeLengthBegin = false;
            this.verifyTimer = 0;
            this.user.phoneNumber = '';
            this.user.validateCode = '';
            //返回登陆页面
            this.$router.push('/login')
        },

        /*******倒计时及检验函数*******/
        startTimerAndCheck() {
            //开始实时检测
            const phoneNumber = this.user.phoneNumber;
            this.checkPhoneNumberLengthBegin = true;
            //检测成功方能开始时间计时获取验证码
            if(phoneNumber.length === 11){
                this.generateRandomCode()
                this.verifiedPhoneNumber = this.user.phoneNumber

                request.post('/api/vcode',{},{
                    params: {
                        num: phoneNumber.toString()
                    }
                }).then(res => {
                    console.log(res.message)
                    console.log(res)
                    this.generatedCode = Number(res.data);
                })

                this.startTimer();
            }
        },
        startTimer() {
            this.verifyTimer = 60;
            this.verifyInterval = setInterval(() => {
                this.verifyTimer--;
                if (this.verifyTimer === 0) {
                    clearInterval(this.verifyInterval);
                }
            }, 1000);
        },
        generateRandomCode(){
            this.generatedCode = Math.round(Math.random() * (999999 - 100000) + 100000);
            // console.log('函数的密码：'+ this.generatedCode)
        },

        /*******注册及检验函数*******/
        registerAndCheck(){
            //开始实时检测
            this.checkSetUsernameLengthBegin = true;
            this.checkPasswordLengthBegin = true;
            this.checkConfirmPasswordLengthBegin = true;
            //条件成立方可调用register函数
            if(this.user.userName.length<=10&&this.user.userName.length>=3&&this.user.pwd.length>=8
                &&this.user.pwd.length<=20){
                this.register();
            }
        },

        register() {
            // 验证两次密码输入是否一致
            if (this.user.pwd !== this.user.confirmPassword) {
                this.$message.error("两次输入的密码不一致")
            }
            else{
                // 密码加密
                let postUser = JSON.parse(JSON.stringify(this.user))
                postUser.pwd = md5(postUser.pwd)
                // 后端添加
                request.post("/api/register", postUser).then(res => {
                    if(res.code === '0') {
                        this.$message.success("注册成功")
                        this.$router.push("/login")
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            }
        },

        /*******修改密码及检验函数*******/
        modifyAndCheck(){
            //开始实时检测
            this.checkPasswordLengthBegin = true;
            this.checkConfirmPasswordLengthBegin = true;
            //条件成立方可调用modify函数
            if(this.user.pwd.length>=8&&this.user.pwd.length<=20){
                this.modify();
            }
        },
        modify(){
            // 检验两遍密码输入得一样不一样
            if (this.user.pwd !== this.user.confirmPassword) {
                this.$message.error("两次输入的密码不一致")
            }
            else{
                // 密码加密
                let postUser = JSON.parse(JSON.stringify(this.user))
                postUser.pwd = md5(postUser.pwd)
                // 后端更新
                request.put("/api/modifyPwd", {},{
                    params: {
                        userName: postUser.userName,
                        pwd: postUser.pwd,
                        phoneNumber: postUser.phoneNumber,
                    }
                }).then(res => {
                    if(res.code === '0') {
                        this.$message.success("修改密码成功")
                        this.$router.push("/login")
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            }
        }
    },

    /*******开始时候的渲染，判断是注册页面还是忘记密码页面的唯一依据*******/
    mounted() {
        if (this.$route.query.from === 'login') {
            this.fromLogin=true;
        }
        else if(this.$route.query.from === 'forget'){
            this.fromForget=true;
        }

    }
}
</script>

<style>

.content .login-wrapper .right-login-form .form-wrapper .button-R {
    display: flex;
    justify-content: space-between;
}


.content .login-wrapper .right-login-form .form-wrapper .button-R .btn-R {
    width: 40%;
    height: 30px;
    background-color: rgb(128, 218, 243);
    border: 0;
    border-radius: 5px;
    color: #fff;
    font-size: 15px;
    margin: 20px 0;
    cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips3 {
    top: 110%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips4 {
    top: 75%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips5 {
    top: 32%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips6 {
    top: 57%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips7 {
    top: 81%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .input-wrapper {
    position: relative;
}

.get-code-btn {
    position: absolute;
    top: 50%;
    right: 5px;
    transform: translateY(-50%);
    background-color: rgb(128, 218, 243);
    color: #fff;
    border: none;
    padding: 10px 10px;
    border-radius: 5px;
    cursor: pointer;
}
</style>
