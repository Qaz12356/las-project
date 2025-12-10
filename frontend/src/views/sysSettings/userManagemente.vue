<template>
    <el-row style="height: 60px;display: flex;align-items: center;padding: 4px;">
        <el-col :span="18">
            用户名称：<el-input v-model="userName" style="width: 240px" placeholder="输入用户名称" />
        </el-col>
        <el-col :span="6">
            <el-button type="primary" @click="queryUser()">查询</el-button>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="24">
            <el-table v-loading="tblLoading" highlight-current-row :data="tableData.rows" style="width: 100%"
                :stripe="true" current-row-key="userId" border @row-click="selectRow">
                <el-table-column type="index" width="50" />
                <el-table-column prop="userId" label="用户编号" width="60" />
                <el-table-column prop="userName" label="用户名称" width="180" />
                <el-table-column prop="userCode" label="用户账号" width="180" />
                <el-table-column prop="userPass" label="用户密码" width="180" />
                <el-table-column prop="userPhone" label="用户电话" width="180" />
                <el-table-column prop="userAddr" label="地址" />
                <el-table-column prop="role" label="角色" />
            </el-table>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="24">
            <div style="display: flex;justify-content:end;padding: 5px 4px;">
                <!-- <el-button type="success" @click="openDlg('addd')">导入用户</el-button> -->
                <el-button type="success" @click="openDlg('add')">添加</el-button>
                <el-button type="danger" @click="delDlg">删除</el-button>
                <el-button type="info" @click="openDlg('upd')">修改</el-button>
            </div>
        </el-col>
    </el-row>

    <el-dialog v-model="dlgUserVisible" :title="title" width="400" :modal="true">
        <el-form :model="userForm" ref="ruleFormRef" :rules="rules">
            <el-form-item label="用户名称" :label-width="80" prop="userName">
                <el-input v-model="userForm.userName" />
            </el-form-item>
			<el-form-item label="用户账号" :label-width="80" prop="userCode">
			    <el-input v-model="userForm.userCode" />
			</el-form-item>
			<el-form-item label="用户密码" :label-width="80" prop="userPass">
			    <el-input v-model="userForm.userPass" />
			</el-form-item>
            <el-form-item label="用户电话" :label-width="80" prop="userPhone">
                <el-input v-model="userForm.userPhone" />
            </el-form-item>
           
            <el-form-item label="地址" :label-width="80">
                <el-input v-model="userForm.userAddr" autocomplete="on" />
            </el-form-item>
            <div class="my-2 flex items-center text-sm">
                <el-radio-group v-model="userForm.role" class="ml-4">
                    <el-radio value="admin">管理员</el-radio>
                    <el-radio value="user">用户</el-radio>
                </el-radio-group>
            </div>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dlgUserVisible = false">取消</el-button>
                <el-button type="primary" @click="saveUser">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import {
    ref,
    onMounted,
    reactive
} from 'vue';
import {
    findUser,
    addUser,
    updUser,
    delUser
} from '@/apis/user.js';
//查询开始
let tblLoading = ref(false);
let userName = ref("");

let tableData = reactive({
    rows: []
})

function queryUser() {
    tblLoading.value = true;
    tableData.rows.length = 0;
    findUser(userName.value).then(res => {
        Object.assign(tableData.rows, res);
        tblLoading.value = false
    });
}

let currentRow = null;

function selectRow(row) {
    currentRow = row;
}


onMounted(() => {
    queryUser();
})
//查询结束


//对话框
let title = ref("增加");
let dlgUserVisible = ref(false);
//表单
let userForm = reactive({
    userName: "",
    userCode: "",
    userPass: "",
    userPhone: "",
    userAddr: "",
    role: "", // 初始化为1，对应第一个选项
    userId: null
})
let ruleFormRef = ref()
//校验规则

const rules = reactive({
    userName: [{
            required: true,
            message: '请输入用户名称',
            trigger: 'blur'
        },
        {
            min: 2,
            max: 8,
            message: '用户名称长度为2-8',
            trigger: 'blur'
        },
    ],
    userCode: [{
        required: true,
        message: '请输入用户账号',
        trigger: 'blur'
    }, {
		min: 10,
		max: 10,
		message: '用户账号长度为10',
		trigger: 'blur'
	}],
    userPass: [{
        required: true,
		
        message: '请输入用户密码',
        trigger: 'blur'
    }, 
	{
		min: 6,
		max: 10,
		message: '用户密码长度为6-10',
		trigger: 'blur'
	},
	],
    userPhone: [{
        required: true,
        message: '请输入用户电话',
        trigger: 'blur'
    }, ]
})

function saveUser() {
    ruleFormRef.value.validate((valid, fields) => {
        if (valid) {
            if (userForm.userId == null) {
                addUser(userForm).then(res => {
                    ElMessage('保存成功');
                    queryUser();
                    dlgUserVisible.value = false;
                })
            } else {
                updUser(userForm).then(res => {
                    ElMessage('保存成功');
                    queryUser();
                    dlgUserVisible.value = false;
                })
            }


        } else {
            console.log('error submit!', fields)
        }
    })
}

function openDlg(flag) {
    if (flag == "add") {
        title.value = "新增用户";
        userForm.userAddr = ""
        userForm.userName = ""
        userForm.userCode = ""
        userForm.userPass = ""
        userForm.userPhone = ""
        userForm.userId = null
        userForm.role = "user"; // 新增时默认选中第一个选项
        dlgUserVisible.value = true;
    } else if (flag == "upd") {
        title.value = "修改用户";
        if (currentRow == null) {
            ElMessageBox.alert('请选中要修改的用户', '提示', {
                confirmButtonText: 'OK',
            })
        } else {
            dlgUserVisible.value = true;
            Object.assign(userForm, currentRow);
            // 确保role有值，如果后端返回的不是字符串，需要转换
            if (userForm.role === undefined || userForm.role === null) {
                userForm.role = "admin";
            } else {
                userForm.role = String(userForm.role); // 转换为字符串
            }
        }
    }


}
//对话框结束

function delDlg() {
    if (currentRow == null) {
        ElMessageBox.alert('请选中要删除的用户', '提示', {
            confirmButtonText: 'OK',
        })
    } else {
        ElMessageBox.confirm(
                '确认要删除用户：' + currentRow.userName + "?",
                '警告', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
            .then(() => {
                delUser(currentRow.userId).then(res => {
                    ElMessage('删除成功');
                    queryUser();
                })
            })
            .catch(() => {

            })
    }
}
</script>

<style scoped>
</style>    