<template>
    <div>
        <el-table
                :data="timetable"
                :span-method="objectSpanMethod"
                :cell-style="cellDisplay"
                :header-cell-style="{color: 'black', background: '#f6f7fa'}"
                @cell-click="SelectOperation"
                border
                style="width: 100%; margin-top: 20px; color: black"
        >
            <el-table-column prop="period" label="节次" header-align="center" width="60"/>
            <el-table-column prop="monday" label="星期一" header-align="center" :formatter="infoDisplay"/>
            <el-table-column prop="tuesday" label="星期二" header-align="center"  :formatter="infoDisplay"/>
            <el-table-column prop="wednesday" label="星期三" header-align="center"  :formatter="infoDisplay"/>
            <el-table-column prop="thursday" label="星期四" header-align="center"  :formatter="infoDisplay"/>
            <el-table-column prop="friday" label="星期五" header-align="center"  :formatter="infoDisplay"/>
            <el-table-column prop="saturday" label="星期六" header-align="center"  :formatter="infoDisplay"/>
            <el-table-column prop="sunday" label="星期日" header-align="center"  :formatter="infoDisplay"/>
        </el-table>

        <!--  修改课程对话框-->
        <el-dialog
                v-model="editMode"
                style="border-radius: 20px"
                title="修改课程"
                draggable
                :modal="false">
            <el-form ref="editForm" :model="editClass" :rules="classRules">
                <el-form-item label="课程名称" prop="title">
                    <el-input v-model="editClass.title" style="width: 300px"/>
                </el-form-item>

                <el-form-item label="上课地点" prop="location">
                    <el-input v-model="editClass.location" style="width: 300px"/>
                </el-form-item>

                <el-row>
                    <el-col :span="8">
                        <!--                      星期几-->
                        <el-form-item label="上课时间" prop="weekday">
                            <el-select placeholder="请选择" v-model="editClass.weekday" style="width: 100px">
                                <el-option
                                        v-for="item in weekdaysOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <!--                      开始时间-->
                        <el-form-item label="从" prop="start">
                            <el-select placeholder="请选择" v-model="editClass.start" style="width: 100px">
                                <el-option
                                        v-for="item in periodOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                        :disabled="DisabledStartTime(item.value)"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <!--                      结束时间-->
                        <el-form-item label="到" prop="end">
                            <el-select placeholder="请选择" v-model="editClass.end" style="width: 100px">
                                <el-option
                                        v-for="item in periodOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                        :disabled="DisabledEndTime(item.value)"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="颜色">
                    <el-color-picker
                            v-model="editClass.color"
                            color-format='hex'
                            :predefine="predefineColors"/>
                </el-form-item>
            </el-form>

            <template #footer>
              <span class="dialog-footer">
                  <el-button type="success" :icon="Check" circle @click="SaveEditChange"/>
                  <el-button type="danger" :icon="Delete" circle @click="DeleteClass"/>
                  <el-button :icon="Close" circle @click="QuitEditChange"/>
              </span>
            </template>
        </el-dialog>

        <!--  添加课程对话框-->
        <el-dialog
                v-model="addMode"
                style="border-radius: 20px"
                title="添加课程"
                draggable
                :modal="false">
            <el-form ref="addForm" :model="editClass" :rules="classRules">
                <el-form-item label="课程名称" prop="title">
                    <el-input v-model="editClass.title" style="width: 300px"/>
                </el-form-item>

                <el-form-item label="上课地点" prop="location">
                    <el-input v-model="editClass.location" style="width: 300px"/>
                </el-form-item>

                <el-row>
                    <el-col :span="8">
                        <!--                      星期几-->
                        <el-form-item label="上课时间" prop="weekday">
                            <el-select placeholder="请选择" v-model="editClass.weekday" style="width: 100px">
                                <el-option
                                        v-for="item in weekdaysOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <!--                      开始时间-->
                        <el-form-item label="从" prop="start">
                            <el-select placeholder="请选择" v-model="editClass.start" style="width: 100px">
                                <el-option
                                        v-for="item in periodOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                        :disabled="DisabledStartTime(item.value)"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <!--                      结束时间-->
                        <el-form-item label="到" prop="end">
                            <el-select placeholder="请选择" v-model="editClass.end" style="width: 100px">
                                <el-option
                                        v-for="item in periodOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                        :disabled="DisabledEndTime(item.value)"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="颜色">
                    <el-color-picker
                            v-model="editClass.color"
                            color-format='hex'
                            :predefine="predefineColors"/>
                </el-form-item>
            </el-form>

            <template #footer>
              <span class="dialog-footer">
                  <el-button type="success" :icon="Check" circle @click="SaveAddChange"/>
                  <el-button :icon="Close" circle @click="QuitAddChange"/>
              </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
import {Check, Close, Delete} from "@element-plus/icons-vue";
import request from "@/utils/request";
import {watch} from "vue";
import router from "@/router";

export default{
    name: "timetable",

    computed: {
        Delete() {
            return Delete
        },
        Close() {
            return Close
        },
        Check() {
            return Check
        }
    },

    inject: ['isNightMode'],

    props: {
        // 总节次
        classNumPerDay: {
            type: [String, Number],
            default: 12
        }
    },

    methods: {
        load(){
            let userJson = sessionStorage.getItem("user")
            const temporaryUser = JSON.parse(userJson)
            request.get("/api/userCenter/timetable", {
                params: {
                    search: temporaryUser.uid
                }
            }).then(res => {
                this.classes = JSON.parse(JSON.stringify(res.data))
                this.mergeData()
            })
        },
        // 建立用于显示的课程表
        makeTimetable(){
            this.timetable = []
            for(let i = 0; i < this.classNumPerDay; ++i){
                let one = {
                    period: i + 1,
                    monday: {},
                    tuesday: {},
                    wednesday: {},
                    thursday: {},
                    friday: {},
                    saturday: {},
                    sunday: {}
                }
                this.timetable.push(one)
            }
        },
        // 建立时间占用情况标记表, true为该段时间可用，false为不可用
        makeAvailablePeriods(){
            this.availablePeriods=[]
            for(let i = 0; i < this.classNumPerDay; ++i){
                let one = {
                    monday: true,
                    tuesday: true,
                    wednesday: true,
                    thursday: true,
                    friday: true,
                    saturday: true,
                    sunday: true
                }
                this.availablePeriods.push(one)
            }
        },
        // 将classes数据填入课程表
        mergeData(){
            this.makeTimetable()
            this.makeAvailablePeriods()
            if(this.classes.length > 0) {
                for(let i = 0; i < this.classes.length; i++){
                    let weekday = this.weekdays[this.classes[i].weekday - 1]
                    this.timetable[this.classes[i].start - 1][weekday] = this.classes[i]
                    for(let t = this.classes[i].start - 1; t < this.classes[i].end; ++t)
                        this.availablePeriods[t][weekday] = false
                }
            }
        },
        // 课程表合并函数
        objectSpanMethod({rowIndex, columnIndex}){
            //节次行
            if(columnIndex === 0)
                return{
                    rowspan: 1,
                    colspan: 1
                }
            let rowSpan = 1, colSpan = 1
            let weekday = this.weekdays[columnIndex - 1]
            let currentClass = this.timetable[rowIndex][weekday]
            if(currentClass.title !== undefined){
                rowSpan = currentClass.end - currentClass.start + 1
                colSpan = 1
            }
            else if (this.availablePeriods[rowIndex][weekday] === false){
                rowSpan = 0
                colSpan = 0
            }
            else{
                rowSpan = 1
                colSpan = 1
            }
            return{
                rowspan: rowSpan,
                colspan: colSpan
            }
        },
        // 显示课程内容
        infoDisplay(row, column){
            let value = row[column.property]
            if(value.title !== undefined)
                return `${value.title}[${value.location}]`
        },
        // 单元格风格确认
        cellDisplay({row,column}){
            let value = row[column.property]
            if (value.title !== undefined)
                return {
                    background: value.color,
                    textAlign: 'center',
                };
            else return {
                background: this.backgroundColor,
                textAlign: 'center',
                color: this.textColor
            }
        },
        // 修改or新建
        SelectOperation(row, column){
            if(column.property === "period")
                return
            let value = row[column.property]
            if (value.title !== undefined) {
                this.editMode = true

                for(let i = 0; i < this.classes.length; i++){
                    if(this.classes[i] === row[column.property]) {
                        // 编辑指示变量更新
                        this.editIndex = i
                        this.editClass = JSON.parse(JSON.stringify(this.classes[i]))
                        // 空余时间更新
                        let weekday = this.weekdays[this.editClass.weekday - 1]
                        for(let t = this.editClass.start - 1; t < this.editClass.end; ++t)
                            this.availablePeriods[t][weekday] = true
                        break
                    }
                }
            }
            else {
                let userJson = sessionStorage.getItem("user")
                const temporaryUser = JSON.parse(userJson)
                this.addMode = true
                this.editClass = {
                    weekday: 1,
                    uid: temporaryUser.uid
                }
                this.editIndex = -1
            }
        },
        // 保存修改
        SaveEditChange(){
            this.$refs.editForm.validate((valid) => {
                if (valid) {
                    request.put("/api/userCenter/timetable", this.editClass).then(res => {
                        if (res.code === '0'){
                            this.$message({
                                type: "success",
                                message: "更新成功"
                            })
                            this.classes[this.editIndex] = JSON.parse(JSON.stringify(this.editClass))
                            this.mergeData()
                            this.editClass = {}
                            this.editMode = false
                            this.dialogVisible = false
                        } else {
                            this.$message({
                                type: "error",
                                message: res.msg
                            })
                        }
                    })
                } else {
                    console.log('表格校验未通过');
                    return false;
                }
            });
        },
        // 放弃修改
        QuitEditChange(){
            this.editClass = {}
            this.editMode = false
            // 空余时间恢复
            let weekday = this.weekdays[this.classes[this.editIndex].weekday - 1]
            for(let t = this.classes[this.editIndex].start - 1; t < this.classes[this.editIndex].end; ++t)
                this.availablePeriods[t][weekday] = false
        },
        // 删除选择的课程
        DeleteClass(){
            request.delete("/api/userCenter/timetable",{
                params: {
                    cid: this.editClass.cid
                }
            }).then(res => {
                if (res.code === '0'){
                    this.$message({
                        type: "success",
                        message: "删除成功"
                    })
                    this.classes.splice(this.editIndex, 1)
                    this.mergeData()
                    this.editClass = {}
                    this.editMode = false
                } else {
                    this.$message({
                        type: "error",
                        message: res.msg
                    })
                }
            })
        },
        // 开始非法时间验证
        DisabledStartTime(period){
            if(typeof this.editClass.weekday === 'undefined')
                return false
            const weekday = this.weekdays[this.editClass.weekday - 1]
            if(this.availablePeriods[period - 1][weekday] === false)
                return true
            else if(this.editIndex !== -1 && this.editClass.weekday === this.classes[this.editIndex].weekday) {
                for(let i = period; i < this.editClass.end; ++i){
                    if (this.availablePeriods[i - 1][weekday] === false)
                        return true
                }
                return period > this.editClass.end
            }
            else if(this.editIndex === -1) {
                for(let i = period; i < this.editClass.end; ++i){
                    if (this.availablePeriods[i - 1][weekday] === false)
                        return true
                }
                return period > this.editClass.end
            }
            else
                return false
        },
        // 结束非法时间验证
        DisabledEndTime(period){
            if(typeof this.editClass.weekday === 'undefined')
                return false
            const weekday = this.weekdays[this.editClass.weekday - 1]
            if(this.availablePeriods[period - 1][weekday] === false)
                return true
            else if(this.editIndex !== -1 && this.editClass.weekday === this.classes[this.editIndex].weekday) {
                for(let i = period; i > this.editClass.start; --i){
                    if (this.availablePeriods[i - 1][weekday] === false)
                        return true
                }
                return period < this.editClass.start
            }
            else if(this.editIndex === -1) {
                for(let i = period; i > this.editClass.start; --i){
                    if (this.availablePeriods[i - 1][weekday] === false)
                        return true
                }
                return period < this.editClass.start
            }
            else
                return false
        },
        // 保存新建
        SaveAddChange(){
            this.$refs.addForm.validate((valid) => {
                if (valid) {
                    request.post("/api/userCenter/timetable", this.editClass).then(res => {
                        if (res.code === '0'){
                            this.$message({
                                type: "success",
                                message: "新增成功"
                            })
                            this.load()
                            this.editClass = {}
                            this.addMode = false
                        } else {
                            this.$message({
                                type: "error",
                                message: res.msg
                            })
                        }
                    })
                } else {
                    console.log('表格校验失败');
                    return false;
                }
            })
        },
        // 放弃新建
        QuitAddChange(){
            this.editClass = {}
            this.addMode = false
        },
        // 黑夜模式转换
        nightModeChanged(){
            if(this.isNightMode.value) {
                this.backgroundColor = '#1f2938'
                this.textColor = 'white';
            }
            else{
                this.backgroundColor = 'white'
                this.textColor = 'black';
            }
        }
    },

    data() {
        return {
            // 传入数据
            classes:[
                {
                    weekday: 1,
                    title: '数学',
                    location: '地点',
                    start: 1,
                    end: 3,
                    color: '#FEE4CB'
                },
                {
                    weekday: 2,
                    title: '语文',
                    location: '地点',
                    start: 4,
                    end: 5,
                    color: '#E9E7FD'
                }
            ],
            // 课程编辑
            editClass: {},
            classRules:{
                title: [
                    { required: true, message: '请输入课程名称', trigger: 'blur' },
                ],
                location: [
                    { required: true, message: '请输入上课地点', trigger: 'blur' },
                ],
                weekday: [
                    { required: true, message: '请选择上课日期', trigger: 'change' },
                ],
                start: [
                    { required: true, message: '请选择开始时间', trigger: 'change' },
                ],
                end: [
                    { required: true, message: '请选择结束时间', trigger: 'change' },
                ]
            },
            editIndex: 0,
            editMode: false,
            addMode: false,
            // 课程显示
            timetable: [],
            availablePeriods: [],
            weekdays: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'],
            // 下拉框信息
            weekdaysOptions: [
                {
                    value: 1,
                    label: '星期一'
                },
                {
                    value: 2,
                    label: '星期二'
                },
                {
                    value: 3,
                    label: '星期三'
                },
                {
                    value: 4,
                    label: '星期四'
                },
                {
                    value: 5,
                    label: '星期五'
                },
                {
                    value: 6,
                    label: '星期六'
                },
                {
                    value: 7,
                    label: '星期日'
                },
            ],
            periodOptions:[
                {
                    value: 1,
                    label: '第一节课',
                },
                {
                    value: 2,
                    label: '第二节课',
                },
                {
                    value: 3,
                    label: '第三节课',
                },
                {
                    value: 4,
                    label: '第四节课',
                },
                {
                    value: 5,
                    label: '第五节课',
                },
                {
                    value: 6,
                    label: '第六节课',
                },
                {
                    value: 7,
                    label: '第七节课',
                },
                {
                    value: 8,
                    label: '第八节课',
                },
                {
                    value: 9,
                    label: '第九节课',
                },
                {
                    value: 10,
                    label: '第十节课',
                },
                {
                    value: 11,
                    label: '第十一节课',
                },
                {
                    value: 12,
                    label: '第十二节课',
                },
            ],
            predefineColors: [
                '#FEE4CB',
                '#E9E7FD',
                '#DBF6FD',
                '#FFD3E2',
                '#C8F7DC',
                '#D5DEFF'
            ],
            // 黑夜模式切换
            backgroundColor: 'white',
            textColor: 'black'
        }
    },

    created() {
        let userJson = sessionStorage.getItem("user")
        if(!userJson) {
            router.push("/login")
        }
        this.load()
    },

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
}
</script>

