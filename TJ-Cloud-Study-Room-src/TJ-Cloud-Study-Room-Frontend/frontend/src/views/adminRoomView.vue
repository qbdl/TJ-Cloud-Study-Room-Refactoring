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
                    <!--            <el-table-column label="房间号" prop="rid" sortable />-->
                    <el-table-column fixed label="房间号" prop="rid" width="100" sortable />
                    <el-table-column label="房间名称" prop="roomName" />
                    <el-table-column label="当前人数" prop="prenum" />
                    <el-table-column label="人数上限" prop="maxnum" />
                    <el-table-column label="房内用户" width="200" type="expand">
                        <template #default="props">
                            <div style="margin-left: 20vw">
                                <el-card style="width: 60%; margin: 20px">
                                    <el-table :data="userInRoom" stripe border size="small">
                                        <el-table-column label="ID" prop="id" />
                                        <el-table-column label="用户名" prop="name" />
                                        <el-table-column label="在线时长" prop="time" />
                                        <el-table-column label="操作" fixed="right" width="150">
                                            <template #default="scope">
                                                <el-popconfirm title="确认移除该用户？">
                                                    <template #reference>
                                                        <el-button
                                                            size="small"
                                                            type="danger"
                                                            plain
                                                        >移除</el-button
                                                        >
                                                    </template>
                                                </el-popconfirm>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </el-card>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" width="150">
                        <template #default="scope">
                            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>

                            <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.row.rid)">
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
                        <el-form-item label="房间名称">
                            <el-input v-model="form.roomName" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="人数上限">
                            <el-input v-model="form.maxnum" style="width:80%"></el-input>
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
                        <el-form-item label="房间名称">
                            <el-input v-model="form.roomName" style="width:80%"></el-input>
                        </el-form-item>
                        <el-form-item label="人数上限">
                            <el-input v-model="form.maxnum" style="width:80%"></el-input>
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
import request from "@/utils/request";

import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import router from "@/router";
export default {
    name: "RoomView",
    components: {
        Aside,
        Header
    },
    data(){
        return {
            form : {},
            dialogVisible : false,
            dialogVisibleUpdate :false,
            search: '',
            searchIcon,
            currentPage: 1,
            pageSize: 10,
            total: 100,
            value: true,
            tableData: [],
            userInRoom:[]
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
            request.get("/admin/room", {
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
        save(){
            request.post("/admin/room", this.form).then(res => {
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

        handleEdit(row){
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
        onAddItem(){
            this.dialogVisible = true
            this.form = {}
        },
        handleDelete(rid){
            request.delete("/admin/room/" + rid).then(res => {
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
        update() {
            request.put("/admin/room", this.form).then(res => {
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
        },
        handleBulkDelete() {
            for (var i = 0; i < this.$refs.tableSeletion.getSelectionRows().length; i++) {
                this.handleDelete(this.$refs.tableSeletion.getSelectionRows()[i].rid)
            }
        },
    }
}
</script>

<style scoped>

</style>