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
                        <span>We invite you to start our journey.</span>
                    </div>
                </div>
            </div>
            <div class="right-login-form">
                <div class="form-wrapper">
                    <h1>登 录</h1>
                    <div class="input-items">
                        <span class="input-tips">
                            账户
                        </span>
                        <input type="text" class="inputs" placeholder="请输入手机号码/用户名" v-model="user.userName">
                        <span class="error-tips error-tips1" v-show="isSubmitted && userNameError">用户名长度应在3-11个字符之间</span>
                    </div>
                    <div class="input-items">
                        <span class="input-tips">
                            密码
                        </span>
                        <input type="password" class="inputs" placeholder="请输入密码" v-model="user.pwd">
                        <span class="error-tips error-tips2" v-show="isSubmitted && passwordError">密码长度应在8-20个字符之间</span>
                        <router-link to="/register?from=forget" style="text-decoration: none">
                            <span class="forgot">忘记密码？</span>
                        </router-link>
                    </div>
                    <button class="btn"   @click="validateAndLogin()">登录</button>
                    <div class="signup-tips">
                        <span>没有账户？</span>
                        <router-link to="/register?from=login" style="text-decoration: none">
                            <span> 注册一个</span>
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";
import md5 from 'js-md5'

import {
    UserFilled,
    Lock
} from '@element-plus/icons-vue'

export default {
    name: "Login",
    computed: {
        UserFilled() {
            return UserFilled
        },
        Lock() {
            return Lock
        },
        userNameError() {
            const { userName } = this.user;
            return userName.length < 3 || userName.length > 11;
        },
        passwordError() {
            const { pwd } = this.user;
            return pwd.length < 8 || pwd.length > 20;
        },
    },
    data() {
        return {
            user: {
                userName: '',
                pwd: '',
                loginTime: '',
                phoneNumber: ''
            },
            isSubmitted: false,
            showUserNameError: false,
            showPasswordError: false,
        }
    },
    methods: {
        validateAndLogin() {
            const { userName, pwd } = this.user;
            this.showUserNameError = userName.length < 3 || userName.length > 11;
            this.showPasswordError = pwd.length < 8 || pwd.length > 20;
            this.isSubmitted = true;
            if (!this.showUserNameError && !this.showPasswordError) {
                // 执行登录逻辑
                this.login();
            }
        },
        login() {
            let postUser = JSON.parse(JSON.stringify(this.user))
            // 修改密码
            postUser.pwd = md5(postUser.pwd)
            // 获取登录时间
            postUser.loginTime = new Date().getTime()
            // 确定输入的是用户名还是电话
            if(postUser.userName.length === 11){
                postUser.phoneNumber = this.user.userName
                postUser.userName = ""
            }
            else
                postUser.phoneNumber = ""
            // 后端发送
            request.post("/api/login", postUser).then(res => {
                if(res.code === "0") {
                    this.$message(
                        {type: "success", message: "登录成功"}
                    )
                    res.data.loginTime = postUser.loginTime;
                    sessionStorage.setItem("user", JSON.stringify(res.data))
                    if(this.user.userName === "admin"){
                        this.$router.push("/admin/user")
                    } else {
                        this.$router.push("/studyHall")
                    }
                } else {
                    this.$message(
                        {type: "error", message: res.msg}
                    )
                }
            })

        }
    }
}
</script>

<style>
* {
    padding: 0;
    margin: 0;
}

.content {
    width: 100vw;
    height: 100vh;
    background-color: rgb(174, 227, 241);
    position: relative;
}

.content .login-wrapper {
    width: 70vw;
    height: 80vh;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
    background-color: #fff;
    border-radius: 40px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    display: flex;
}

.content .login-wrapper .left-img {
    border-radius: 40px;
    flex: 1;
    background: url(../assets/loginbg.jpg) no-repeat;
    background-size: cover;
    position: relative;
}

.content .login-wrapper .left-img .glass {
    width: 60%;
    padding: 20px;
    color: #fff;
    position: absolute;
    top: 20%;
    left: 35%;
    transform: translate(-50%, -20%);
    background: rgba(255, 255, 255, 0.1);
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips .title {
    width: 60%;
    font-weight: 600;
    font-size: 15px;
    text-align: center;
    padding: 10px;
    margin-bottom: 10px;
    overflow: hidden;
    background: rgba(255, 255, 255, 0.1);
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border-radius: 10px;
    border: 1px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips h1 {
    margin: 15px 0;
}

.content .login-wrapper .left-img .glass .tips span {
    margin: 5px 0;
    display: block;
    font-weight: 100;
}

.content .login-wrapper .right-login-form {
    flex: 1;
    position: relative;
}

.content .login-wrapper .right-login-form .form-wrapper {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    width:50%
}

.content .login-wrapper .right-login-form .form-wrapper h1 {
    font-size: 30px;
    text-align: center;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items {
    margin: 20px 0 30px;
    width: 100%; /* Add this line to set a fixed width */
    box-sizing: border-box;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .input-tips {
    display: inline-block;
    font-weight: 600;
    font-size: 20px;
    margin: 10px 0;
}

.content .login-wrapper .right-login-form .input-items .error-tips {
    position: absolute;
    left: 0;
    width: 100%;
    display: inline-block;
    font-weight: 20;
    font-size: 5px;
    margin: 0;
    color: red;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips1 {
    top: 38%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .error-tips2 {
    top: 67%; /* 位于输入容器下方 */
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .inputs {
    width: 100%;
    height: 50px;
    border-radius: 10px;
    border: 1px solid rgb(227, 227, 227);
    padding: 20px;
    box-sizing: border-box;
    outline: none;
    transition: 0.5s;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .inputs:focus {
    border: 1px solid rgb(106, 142, 196);
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .forgot {
    float: right;
    font-weight: 600;
    text-align: right;
    margin: 5px 0;
    font-size: 13px;
    cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .btn {
    width: 100%;
    height: 40px;
    background-color: rgb(128, 218, 243);
    border: 0;
    border-radius: 10px;
    color: #fff;
    font-size: 20px;
    margin: 20px 0;
    cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .signup-tips {
    text-align: center;
    font-weight: 600;
    font-size: 12px;
    margin: 10px 0;
}

.content .login-wrapper .right-login-form .form-wrapper .signup-tips span:last-child {
    color: rgb(128, 218, 243);
    cursor: pointer;
}

@media (max-width: 1024px) {
    .login-wrapper {
        padding: 20px;
    }
    .login-wrapper .left-img {
        display: none;
    }
    .login-wrapper .right-login-form {
        padding: 20px;
    }
}

</style>
