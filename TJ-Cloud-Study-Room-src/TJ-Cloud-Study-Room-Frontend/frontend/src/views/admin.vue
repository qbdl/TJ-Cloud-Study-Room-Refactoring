<template>
    <div>
        <Header />
        <div style="display: flex">
            <Aside />
            <div>
                <el-card style="width: 40%; margin: 20px">
                    <el-form :model="form" label-width="60px" >
                        <el-form-item label="用户名">
                            <el-input v-model="form.userName" style="width: 50vw" disabled/>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input v-model="form.pwd" style="width: 50vw"/>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="form.phoneNumber" style="width: 50vw"/>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="form.mailAddress" style="width: 50vw"/>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="update">保存</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive } from 'vue'
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import request from "@/utils/request";
export default {
    name: "admin",
    components: {Aside, Header},
    data(){
        return{
            form: {},
            search: "admin",
            tableData: []
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            request.get("/admin/user", {
                params: {
                    pageNum: 1,
                    pageSize: 10,
                    search: this.search
                }
            }).then(res => {
                this.tableData = res.data.records
                this.form = this.tableData[0]
            })
        },
        update() {
            request.put("/admin/user", this.form).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "更新成功"
                    })
                } else {
                    this.$message({
                        type: "error",
                        message: res.msg
                    })
                }
            })
        }
    }
}
</script>

<style scoped>

</style>