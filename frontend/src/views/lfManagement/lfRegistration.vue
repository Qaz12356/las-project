<template>
    <div class="centered-container">
        <div class="form-card" title="上传失物">
            <div class="card-header">
                <h3 class="card-title">失物招领 - 信息提交</h3>
                <p class="card-subtitle">请填写以下信息帮助失主找回物品</p>
            </div>
            
            <el-form :model="lostForm" :rules="rules" ref="lostRef" class="custom-form">
                <div class="form-columns">
                    <div class="form-column">
                        <el-form-item label="失物名称" :label-width="80" prop="lostName">
                            <el-input v-model="lostForm.lostName" placeholder="请输入失物名称" class="form-input" />
                        </el-form-item>
                        
                        <el-form-item label="拾到地址" :label-width="80" prop="lostAddr">
                            <el-input v-model="lostForm.lostAddr" placeholder="请输入拾到地址" class="form-input" />
                        </el-form-item>
                        
                        <el-form-item label="拾到时间" :label-width="80" prop="lostDate">
                            <el-date-picker 
                                v-model="lostForm.lostDate" 
                                type="date" 
                                placeholder="选择日期" 
                                size="default"
                                format="YYYY-MM-DD" 
                                value-format="YYYY-MM-DD" 
                                class="form-input"
                            />
                        </el-form-item>
                    </div>
                    
                    <div class="form-column">
                        <el-form-item label="失物类型" :label-width="80" prop="lostTypeId">
                            <el-select 
                                v-model="lostForm.lostTypeId" 
                                placeholder="选择类型" 
                                size="default"
                                class="form-input"
                            >
                                <el-option 
                                    v-for="item in lostTypeOpts" 
                                    :key="item.lostTypeId" 
                                    :label="item.lostTypeName"
                                    :value="item.lostTypeId" 
                                />
                            </el-select>
                        </el-form-item>
                        
                        <el-form-item label="失物描述" :label-width="80" prop="lostDespn">
                            <el-input 
                                v-model="lostForm.lostDespn" 
                                type="textarea" 
                                :rows="4" 
                                placeholder="请描述失物特征、颜色、品牌等信息"
                                class="form-input"
                            />
                        </el-form-item>
                    </div>
                </div>
                
                <el-form-item label="失物图片" :label-width="80">
                    <el-upload 
                        v-model:file-list="fileList" 
                        ref="upload" 
                        :action="actionUrl" 
                        list-type="picture-card"
                        method="post" 
                        :data="lostForm" 
                        :multiple="false" 
                        accept=".jpg,.png" 
                        :on-exceed="handleExceed"
                        :auto-upload="false" 
                        :on-success="handleSuccess" 
                        :before-upload="handleBefore"
                        :on-error="handleError" 
                        :limit="1" 
                        :with-credentials="true"
                        class="custom-upload"
                    >
                        <template #trigger>
                            <div class="upload-container">
                                <el-icon class="upload-icon"><Plus /></el-icon>
                                <span class="upload-text">点击上传图片</span>
                            </div>
                        </template>
                        <template #tip>
                            <div class="el-upload__tip">
                                图片(jpg/png)大小不能超过1M。
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
            </el-form>
            
            <div class="form-actions">
                <el-button @click="dlgLostVisible = false" class="cancel-button">取消</el-button>
                <el-button type="primary" @click="saveLost" class="submit-button">
                    <i class="el-icon-check"></i> 确定提交
                </el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import {
    reactive,
    ref,
    onMounted
} from 'vue';
import {
    ElMessageBox,
    ElMessage
} from 'element-plus';
import { Plus } from '@element-plus/icons-vue'
// 这里假设 regCfg 已经正确导入
import {
    regCfg
} from "@/utils/config.js";
// 这里假设 findLost、updLost、delLost、findAllLostType 已经正确导入
import {
    findLost,
    updLost,
    delLost
} from '@/apis/lost.js';
import {
    findAllLostType
} from "@/apis/lostType.js";

let queryForm = reactive({
    lostName: "",
    lostTypeId: 0,
    pageNo: 1,
    pageSize: 10
})

let lostTypeOpts = reactive([{
    lostTypeId: 0,
    lostTypeName: "--请选择--"
}]);

let tblLoading = ref(false);
let tableData = reactive({
    rows: [],
    total: 0
})

// 初始化页面
function initPage() {
    lostTypeOpts.length = 0;
    findAllLostType().then(res => {
        lostTypeOpts.push({
            lostTypeId: 0,
            lostTypeName: "--请选择--"
        })
        for (let i = 0; i < res.length; i++) {
            let tmp = {
                lostTypeId: res[i].lostTypeId,
                lostTypeName: res[i].lostTypeName
            }
            lostTypeOpts.push(tmp)
        }
    })
}

// 改用 async/await
// async function initPage(){
// 	try{
// 		const types = await findAllLostType();
// 		lostTypeOpts.value = [
// 			{lostTYpeId : 0,lostTypeName:"--请选择--"},
// 			...types.map(type =>({
// 				lostYypeId:type.lostTypeId,
// 				lostTypeName:type.lostTypeName,
// 			}))
			
// 		];
// 	}catch(error){
// 		console.error("初始化失败：",error);
// 		ElMessage.error("加载数据失败");
// 	}
// }


// 上传
let dlgLostVisible = ref(false)
const lostForm = reactive({
    lostAddr: "",
    lostName: "",
    lostTypeId: 0,
    lostDespn:"",
    lostDate: ""
})

let rules = {
    lostName: [{
            required: true,
            message: '请输入失物名称',
            trigger: 'blur'
        },
        {
            min: 2,
            max: 50,
            message: '失物名称长度为2 - 50',
            trigger: 'blur'
        }
    ],
    lostDate: [{
        required: true,
        message: '请输入拾到时间',
        trigger: 'blur'
    }],
    lostTypeId: [{
        required: true,
        message: '请选择类型',
        trigger: 'blur',
        validator: (rule, value, callback) => {
            if (value == 0) {
                callback(new Error('请选择类型'));
            } else {
                callback();
            }
        }
    }]
}

let lostRef = ref();
let actionUrl = ref(regCfg.baseURL + "/lost/uploadFile");

const fileList = reactive([])
const upload = ref()

function handleBefore(file) {
    console.log("handleBefore.........", file.size);
    // 不能大于 1MB
    if (file.size > 1 * 1024 * 1024) {
        ElMessage.error("不能上传超过 1M 的文件");
        return false; // 阻止文件上传
    }
    return true; // 允许文件上传
}

function handleExceed(files, uploadFiles) {
    ElMessage.warning("只能上传一张图片");
    upload.value.clearFiles();
    upload.value.handleStart(files[0]);
}

function handleSuccess(response, uploadFile, uploadFiles) {
    console.log("handleSuccess:", response);
    ElMessage.success("图片上传成功");
}

function handleError(error, uploadFile, uploadFiles) {
    console.log("handleError:", error);
    ElMessage.error("图片上传失败，请重试");
}

function saveLost() {
    console.log("saveLost..........")
    lostRef.value.validate((valid, fields) => {
        if (valid) {
            ElMessageBox.confirm('是否要提交？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                upload.value.submit();
                dlgLostVisible.value = false;
                ElMessage.success('提交成功');
                // 清除表单内容
                lostForm.lostAddr = "";
                lostForm.lostName = "";
                lostForm.lostTypeId = 0;
                lostForm.lostDate = "";
                lostForm.lostDespn="";
                // 清除文件列表，并且调用 el-upload 实例的 clearFiles 方法
                fileList.length = 0;
                upload.value.clearFiles(); 
            }).catch(() => {
                // 用户取消提交
            });
        }
    });
}

onMounted(() => {
    initPage();
})
</script>

<style scoped>
.centered-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 40px 20px;
    background-color: #f5f7fa;
    background-image: 
        radial-gradient(circle at 25% 25%, rgba(58, 143, 254, 0.03) 0%, transparent 50%),
        radial-gradient(circle at 75% 75%, rgba(58, 143, 254, 0.03) 0%, transparent 50%);
}

.form-card {
    background-color: white;
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
    width: 100%;
    max-width: 900px;
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.form-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 40px rgba(0, 0, 0, 0.12);
}

.card-header {
    padding: 32px 48px;
    background: linear-gradient(135deg, #3a8ffe 0%, #66b1ff 100%);
    color: white;
    position: relative;
    overflow: hidden;
}

.card-header::before {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    width: 100%;
    height: 100%;
    background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.05' fill-rule='evenodd'/%3E%3C/svg%3E");
    opacity: 0.2;
    pointer-events: none;
}

.card-title {
    margin: 0;
    font-size: 28px;
    font-weight: 500;
    letter-spacing: -0.5px;
}

.card-subtitle {
    margin-top: 8px;
    font-size: 16px;
    opacity: 0.8;
}

.custom-form {
    padding: 48px;
}

.form-columns {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 32px;
    margin-bottom: 32px;
}

.form-column {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.form-input .el-input__inner,
.form-input .el-date-picker__editor,
.form-input .el-select .el-input__inner {
    border-radius: 10px;
    padding: 12px 16px;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
    font-size: 15px;
    height: auto;
}

.form-input .el-input__inner:focus,
.form-input .el-date-picker__editor:focus,
.form-input .el-select .el-input__inner:focus {
    border-color: #3a8ffe;
    box-shadow: 0 0 0 3px rgba(58, 143, 254, 0.15);
}

.el-form-item__label {
    font-size: 15px;
    color: #303133;
}

.custom-upload .el-upload--picture-card {
    width: 150px;
    height: 150px;
    border-radius: 12px;
    border: 2px dashed #dcdfe6;
    background-color: #fafafa;
    transition: all 0.3s ease;
}

.custom-upload .el-upload--picture-card:hover {
    border-color: #3a8ffe;
    background-color: #f5f9ff;
    transform: translateY(-3px);
}

.upload-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.upload-icon {
    font-size: 28px;
    color: #3a8ffe;
    margin-bottom: 8px;
    transition: transform 0.3s ease;
}

.custom-upload .el-upload--picture-card:hover .upload-icon {
    transform: scale(1.1);
}

.upload-text {
    color: #606266;
    font-size: 14px;
}

.form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 20px;
    padding: 32px 48px;
    background-color: #fafafa;
    border-top: 1px solid #f0f0f0;
}

.cancel-button {
    border-radius: 10px;
    padding: 12px 32px;
    font-size: 15px;
    color: #606266;
    border-color: #dcdfe6;
    transition: all 0.3s ease;
}

.cancel-button:hover {
    color: #303133;
    border-color: #c0c4cc;
    background-color: #f5f7fa;
    transform: translateY(-2px);
}

.submit-button {
    border-radius: 10px;
    padding: 12px 32px;
    font-size: 15px;
    background-color: #3a8ffe;
    border-color: #3a8ffe;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(58, 143, 254, 0.15);
}

.submit-button:hover {
    background-color: #2a7fe0;
    border-color: #2a7fe0;
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(58, 143, 254, 0.25);
}

.submit-button:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(58, 143, 254, 0.2);
}

/* 动画效果 */
.fade-in {
    animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
}

/* 响应式调整 */
@media (max-width: 768px) {
    .form-card {
        max-width: 100%;
    }
    
    .card-header,
    .custom-form,
    .form-actions {
        padding: 24px 32px;
    }
    
    .form-columns {
        grid-template-columns: 1fr;
        gap: 24px;
    }
    
    .card-title {
        font-size: 24px;
    }
    
    .card-subtitle {
        font-size: 14px;
    }
    
    .custom-upload .el-upload--picture-card {
        width: 120px;
        height: 120px;
    }
}
</style>    