<template>
    <div>
        <Header />
        <div style="display: flex">
            <Aside />
            <div>
                <el-button type="primary" plain
                           style="width: 10%; margin-left: 10px; margin-top: 10px"
                           size="small"
                           @click="onAddItem">增加
                </el-button>
                <el-button type="primary" plain
                           style="width: 10%; margin-left: 10px; margin-top: 10px"
                           size="small"
                           @click="handleBulkDelete">批量删除
                </el-button>
                <el-input v-model="search" size="small" placeholder="Type to search"
                          @input="load" clearable
                          style="width: 20vw; margin-left: 10px; margin-top: 10px"
                          :prefix-icon=searchIcon
                />

                <el-table :data="tableData" style="width: 85vw" stripe ref="tableSeletion">
                    <el-table-column type="selection" />
                    <el-table-column fixed label="ID" prop="uid" width="100" sortable />
                    <el-table-column label="用户名" prop="userName" width="100" />
                    <el-table-column label="所在房间" prop="roomId" width="200" sortable />
                    <el-table-column label="密码" prop="pwd" width="200" />
                    <el-table-column label="性别" prop="sex" width="100" />
                    <el-table-column label="专业" prop="major" width="200" />
                    <el-table-column label="年级" prop="grade" width="100" />
                    <el-table-column label="培养层次" prop="educationLevel" width="100" />
                    <el-table-column label="手机号" prop="phoneNumber" width="200" />
                    <el-table-column label="邮箱" prop="mailAddress" width="200" />
                    <el-table-column label="操作" fixed="right" width="150">
                        <template #default="scope">
                            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                            <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.row.uid)">
                                <template #reference>
                                    <el-button size="small" type="danger">删除</el-button>
                                </template>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>

                <el-pagination
                    v-model:current-page="currentPage"
                    v-model:page-size="pageSize"
                    :page-sizes="[5, 10, 20]"
                    small
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    :sizes="pageSize"
                    @current-change="handleCurrentChange(currentPage)"
                    @size-change="handleSizeChange(pageSize)"
                    style="margin-top: 10px; margin-left: 20vw"
                />
                <el-dialog title="增加" v-model="dialogVisible" width="30%">
                    <el-form :model="form" label-width="120px">
                        <el-form-item label="用户名">
                            <el-input v-model="form.userName" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input v-model="form.pwd" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="所在房间">
                            <el-input v-model="form.roomId" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="性别">
                            <el-radio v-model="form.sex" label="男">男</el-radio>
                            <el-radio v-model="form.sex" label="女">女</el-radio>
                            <el-radio v-model="form.sex" label="未知">未知</el-radio>
                        </el-form-item>
                        <el-form-item label="专业">
                            <el-input v-model="form.major" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="年级">
                            <el-input v-model="form.grade" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="培养层次">
                            <el-input v-model="form.educationLevel" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="form.phoneNumber" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="form.mailAddress" style="width:80%"></el-input>
                        </el-form-item>
                    </el-form>
                    <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisible=false">取消</el-button>
              <el-button type="primary" @click="save">确定</el-button>
            </span>
                    </template>
                </el-dialog>

                <el-dialog title="编辑" v-model="dialogVisibleUpdate" width="30%">
                    <el-form :model="form" label-width="120px">
                        <el-form-item label="用户名">
                            <el-input v-model="form.userName" style="width:80%" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input v-model="form.pwd" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="所在房间">
                            <el-input v-model="form.roomId" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="性别">
                            <el-radio v-model="form.sex" label="男">男</el-radio>
                            <el-radio v-model="form.sex" label="女">女</el-radio>
                            <el-radio v-model="form.sex" label="未知">未知</el-radio>
                        </el-form-item>
                        <el-form-item label="专业">
                            <el-input v-model="form.major" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="年级">
                            <el-input v-model="form.grade" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="培养层次">
                            <el-input v-model="form.educationLevel" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="form.phoneNumber" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="form.mailAddress" style="width:80%"></el-input>
                        </el-form-item>
                    </el-form>
                    <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisibleUpdate=false">取消</el-button>
              <el-button type="primary" @click="update">确定</el-button>
            </span>
                    </template>
                </el-dialog>
            </div>
        </div>
    </div>
</template>

<script>

import {
    Search as searchIcon
} from '@element-plus/icons-vue'
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import request from "@/utils/request";
import router from "@/router";
export default {
    name: 'UserView',
    components: {
        Aside,
        Header
    },
    data(){
        return {
            form: {},
            dialogVisible: false,
            dialogVisibleUpdate: false,
            search: '',
            searchIcon,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            value: true,
            tableData: [],
        }
    },
    created() {
        let userJson = sessionStorage.getItem("user")

        if(!userJson){
            router.push("/login")
        } else {
            const tu = JSON.parse(userJson)
            if(tu.userName !== "admin")
                router.push("/studyhall")
        }

        this.load()
    },
    methods: {
        load() {
            request.get("/admin/user", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: this.search
                }
            }).then(res => {
                // console.log(res)
                this.tableData = res.data.records
                this.total = res.data.total
            })
        },
        handleEdit(row) {
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogVisibleUpdate = true
        },
        handleCurrentChange(pageNum) {
            this.currentPage = pageNum
            this.load()
        },
        handleSizeChange(pageSize) {
            this.pageSize = pageSize
            this.load()
        },
        onAddItem() {
            this.dialogVisible = true
            this.form = {}  /*新增时先清空一下表单域*/
        },
        handleDelete(uid) {
            request.delete("/admin/user/" + uid).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "删除成功"
                    })
                } else {
                    this.$message({
                        type: "error",
                        message: res.msg
                    })
                }
                this.load()
            })
        },
        handleBulkDelete() {
            for (var i = 0; i < this.$refs.tableSeletion.getSelectionRows().length; i++) {
                this.handleDelete(this.$refs.tableSeletion.getSelectionRows()[i].uid)
            }
        },
        save() {
            request.post("/admin/user", this.form).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "新增成功"
                    })
                    this.load()
                    this.dialogVisible = false
                } else {
                    this.$message({
                        type: "error",
                        message: res.msg
                    })
                }
            })
        },
        update() {
            request.put("/admin/user", this.form).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "更新成功"
                    })
                    this.load()
                    this.dialogVisibleUpdate = false
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
