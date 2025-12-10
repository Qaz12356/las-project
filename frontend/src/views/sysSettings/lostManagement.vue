<template>
    <el-row style="height: 60px;display: flex;align-items: center;padding: 4px;">
        <el-col :span="18">
            <el-form :model="queryForm" :inline="true">
                <el-form-item label="失物名称" :label-width="80">
                    <el-input v-model="queryForm.lostName" placeholder="失物名称" style="width: 200px;" />
                </el-form-item>
                <el-form-item label="失物类别" :label-width="80">
                    <el-select v-model="queryForm.lostTypeId" placeholder="Select" size="default" style="width: 200px;">
                        <el-option v-for="item in lostTypeOpts" :key="item.lostTypeId" :label="item.lostTypeName"
                                   :value="item.lostTypeId" />
                    </el-select>
                </el-form-item>
            </el-form>
        </el-col>
        <el-col :span="6">
            <el-button type="primary" @click="queryLost(1)">查询</el-button>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="24">
            <el-table v-loading="tblLoading" highlight-current-row :data="tableData.rows" style="width: 100%"
                      :stripe="true" current-row-key="lostType" border @row-click="selectRow">
                <el-table-column type="index" width="50" />
                <el-table-column prop="lostId" label="失物Id" width="80" />
                <el-table-column prop="lostName" label="失物名称" width="180" />
                <el-table-column prop="lostType.lostTypeName" label="归属类型" width="150" />
                <el-table-column prop="lostDate" label="拾到时间" width="150" />
                <el-table-column prop="lostAddr" label="拾到地址" width="150" />
                <el-table-column prop="lostDespn" label="失物描述" width="180" />
                <el-table-column label="操作" width="180">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click.prevent="priviewPic(scope.row.picPath)">
                            预览图片
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="claimStatus" label="状态" width="120" />
            </el-table>
        </el-col>
    </el-row>
    <el-row>
        <el-col>
            <el-pagination background layout="prev, pager, next" :total="tableData.total" @prev-click="queryLost"
                           @next-click="queryLost" @current-change="queryLost" />
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="24">
            <div style="display: flex;justify-content:end;padding: 5px 4px;">
                <el-button type="success" @click="openDlg('add')">上传失物</el-button>
                <el-button type="success" @click="openDlg('claim')">认领</el-button>
				<!-- <el-button type="success" @click="openDlg('upd')">修改</el-button> -->
                <el-button type="danger" @click="delDlg">删除</el-button>
            </div>
        </el-col>
    </el-row>

    <el-dialog v-model="dlgLostVisible" title="上传失物" width="400" :modal="true">
        <el-form :model="lostForm" :rules="rules" ref="lostRef">
            <el-form-item label="失物名称" :label-width="80" prop="lostName">
                <el-input v-model="lostForm.lostName" />
            </el-form-item>
            <el-form-item label="拾到地址" :label-width="80" prop="lostAddr">
                <el-input v-model="lostForm.lostAddr" />
            </el-form-item>
            <el-form-item label="拾到时间" :label-width="80" prop="lostDate">
                <el-date-picker v-model="lostForm.lostDate" type="date" placeholder="选择日期" size="default"
                                format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item label="失物类型" :label-width="80" prop="lostTypeId">
                <el-select v-model="lostForm.lostTypeId" placeholder="选择类型" size="default">
                    <el-option v-for="item in lostTypeOpts" :key="item.lostTypeId" :label="item.lostTypeName"
                               :value="item.lostTypeId" />
                </el-select>
            </el-form-item>
            <el-form-item label="失物描述" :label-width="80" prop="lostDespn">
                <el-input v-model="lostForm.lostDespn" />
            </el-form-item>
            <el-form-item label="失物图片" :label-width="80">
                <el-upload v-model:file-list="fileList" ref="upload" :action="actionUrl" list-type="picture"
                           method="post" :data="lostForm" :multiple="false" accept=".jpg,.png" :on-exceed="handleExceed"
                           :auto-upload="false" :on-success="handleSuccess" :before-upload="handleBefore"
                           :on-error="handleError" :limit="1" :with-credentials="true">
                    <el-button type="primary">选择图片</el-button>
                    <template #tip>
                        <div class="el-upload__tip">
                            图片(jpg/png)大小不能超过1M。
                        </div>
                    </template>
                </el-upload>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dlgLostVisible = false">取消</el-button>
                <el-button type="primary" @click="saveLost">确定</el-button>
            </div>
        </template>
    </el-dialog>
    <el-dialog v-model="dlgClaimVisible" title="认领失物" width="400" :modal="true">
        <el-form :model="claimForm" ref="claimFormRef" :rules="rules">
            <el-form-item label="认领人" :label-width="80" prop="claimName">
                <el-input v-model="claimForm.claimName" />
            </el-form-item>
            <el-form-item label="失物名称" :label-width="80">
                <el-input v-model="claimForm.lostName" readonly />
            </el-form-item>
            <el-form-item label="联系方式" :label-width="80" prop="contactWay">
                <el-input v-model="claimForm.contactWay" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dlgClaimVisible = false">取消</el-button>
                <el-button type="primary" @click="saveClaim">确定</el-button>
            </div>
        </template>
    </el-dialog>
    <el-dialog v-model="dlgPicVisible" title="图片预览" width="430" :modal="true">
        <div style="width: 100%;">
            <el-image style="width: 400px; height: 400px" :src="picurl" fit="fill" />
        </div>
    </el-dialog>
</template>
<script setup>
    import {
        reactive,
        ref,
        onMounted
    } from 'vue';
    import {
        regCfg
    } from "@/utils/config.js";
    import {
        findLost,
        delLost,
		updLost,
        addStatus,
        updStatus
    } from '@/apis/lost.js';
    import {
        findAllLostType
    } from "@/apis/lostType.js";
    import {
        addClaiminfo,
        updClaiminfo,
		getByLostId,
		updClaimin
    } from '@/apis/claiminfo.js';
    import {
        ElMessageBox,
        ElMessage
    } from 'element-plus';

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

    initPage();

    //初始化页面
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

    function queryLost(pageNo) {
        if (undefined != pageNo) {
            queryForm.pageNo = pageNo
        } else {
            queryForm.pageNo = 1
        }

        tableData.rows.length = 0;
        tblLoading.value = true;
        findLost(queryForm).then(res => {
            tblLoading.value = false;
            Object.assign(tableData.rows, res.rows);
            tableData.total = res.total;
        })
    }
    //上传
    let dlgLostVisible = ref(false)
    const lostForm = reactive({
        lostAddr: "",
        lostName: "",
        lostType: 0,
        lostDate: "",
        lostDespn: "",
        //lostId: null
    })
    let dlgClaimVisible = ref(false)
    let claimForm = reactive({
        claimName: "",
        contactWay: "",
        claimTime: "",
        lostId: 0,
        lostName: ""
    })
    let claimFormRef = ref()
    let rules = {
        lostName: [{
            required: true,
            message: '请输入失物名称',
            trigger: 'blur'
        },
            {
                min: 2,
                max: 50,
                message: '失物名称长度为2-50',
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
        }],
        contactWay: [{
            required: true,
            message: '请输入联系电话',
            trigger: 'blur'
        },
            {
                min: 11,
                max: 11,
                message: '电话长度为11',
                trigger: 'blur'
            }
        ],
        claimName: [{
            required: true,
            message: '请输入认领人',
            trigger: 'blur'
        },
            {
                min: 1,
                max: 10,
                message: '编码长度为2-10',
                trigger: 'blur'
            }
        ]
    }
    let lostRef = ref();
    let actionUrl = ref(regCfg.baseURL + "/lost/uploadFile");

    const fileList = reactive([])
    const upload = ref()

    function handleBefore(file) {
        if (file.size > 1 * 1024 * 1024) {
            ElMessageBox.alert("不能上传超过 1M 的文件", {
                type: 'error'
            });
            return false;
        }
        return true;
    }

    function handleExceed(files, uploadFiles) {
        upload.value.clearFiles();
        upload.value.handleStart(files[0]);
    }

    function saveLost() {
        lostRef.value.validate((valid, fields) => {
            if (valid) {
                new Promise((resolve, reject) => {
                    upload.value.resolve = resolve;
                    upload.value.reject = reject;
                    upload.value.submit();
                }).then((res) => {
                    ElMessage.success('上传成功');
                    lostForm.lostAddr = "";
                    lostForm.lostName = "";
                    lostForm.lostTypeId = 0;
                    lostForm.lostDate = "";
                    lostForm.lostDespn = "";
                    fileList.length = 0;
                    if (upload.value) {
                        upload.value.clearFiles();
                    }
                    dlgLostVisible.value = false;
                    queryLost();
                }).catch(() => {});
            }
        });
    }

    function handleSuccess(response, uploadFile, uploadFiles) {
        if (upload.value.resolve) {
            upload.value.resolve(response);
        }
    }

    function handleError(error, uploadFile, uploadFiles) {
        if (upload.value.reject) {
            upload.value.reject(error);
        }
    }

    //删除
    let currentRow = null;

    function selectRow(row) {
        currentRow = row;
    }

   function delDlg() {
     if (currentRow == null) {
       // 未选中行时的提示
       ElMessageBox.alert('请选中要删除的失物', '提示', { confirmButtonText: 'OK' });
     } else {
       // 判断状态是否为“已经被认领了”
       if (currentRow.claimStatus === '已经被认领了') {
         // 显示禁止删除的提示弹窗
         ElMessageBox.alert(
           '已经被认领的失物不能删除', 
           '提示', 
           { confirmButtonText: '确定' } // 点击确定后自动关闭弹窗
         );
         return; // 终止后续删除流程
       }
   
       // 原有删除确认逻辑（状态正常时执行）
       ElMessageBox.confirm(
         `确认要删除失物：${currentRow.lostName}？`,
         '警告', 
         {
           confirmButtonText: '确认',
           cancelButtonText: '取消',
           type: 'warning'
         }
       ).then(() => {
         // 执行删除操作
         delLost(currentRow.lostId).then(res => {
           ElMessage.success('删除成功');
           queryLost(queryForm.pageNo);
         });
       }).catch(() => {
         // 用户取消删除
       });
     }
   }

    function openDlg(flag) {
        if (flag == "add") {
            dlgLostVisible.value = true;
        } else if (flag == "claim") {
            if (currentRow == null) {
                ElMessageBox.alert('请选中要认领的失物', '提示', {
                    confirmButtonText: 'OK'
                })
            } else if (currentRow.claimStatus === '已经被认领了') {
                ElMessageBox.confirm(
                    '已经被认领了',
                    '提示',
                    {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'question'
                    }
                ).then(() => {
     //                dlgClaimVisible.value = true;
					// getByLostId()
     //                claimForm.lostId = currentRow.lostId;
     //                claimForm.lostName = currentRow.lostName;
     //                claimForm.claimName = currentRow.claimName;
     //                claimForm.contactWay = currentRow.contactWay;
                }).catch(() => {
                    // 用户取消，不做任何操作
                });
            } else {
                dlgClaimVisible.value = true;
                claimForm.lostId = currentRow.lostId;
                claimForm.lostName = currentRow.lostName;
                claimForm.claimName = "";
                claimForm.contactWay = "";
                claimForm.claimTime = "";
            }
        }else if (flag == "upd") {
        title.value = "修改失物";
        if (currentRow == null) {
            ElMessageBox.alert('请选中要修改的用户', '提示', {
                confirmButtonText: 'OK',
            })
        } else {
            dlgLostVisible.value = true;
            Object.assign(userForm, currentRow);
			}
    }
	}

    function saveClaim() {
		 console.log('claimForm.claimId:', claimForm.claimId);
        if (claimFormRef.value) {
            claimFormRef.value.validate((valid, fields) => {
                if (valid) {
                    // 自动生成当天日期
                    const today = new Date();
                    const year = today.getFullYear();
                    const month = String(today.getMonth() + 1).padStart(2, '0');
                    const day = String(today.getDate()).padStart(2, '0');
                    claimForm.claimTime = `${year}-${month}-${day}`;

                    if (currentRow.claimStatus === '已经被认领了') {
                        updClaimin(claimForm).then(res => {
                            ElMessage.success('认领信息更新成功');
                            currentRow.claimName = claimForm.claimName;
                            currentRow.contactWay = claimForm.contactWay;
                            dlgClaimVisible.value = false;
                        }).catch(error => {
                            ElMessage.error('认领信息更新失败：' + error.message);
                        });
                    } else {
                        addClaiminfo(claimForm).then(res => {
                            if (currentRow) {
                                // 更新当前行的状态
                                currentRow.claimStatus = "已经被认领了";
                                // 复制当前行的所有字段到一个新对象
                                const updatedLost = { ...currentRow };
                                // 更新认领状态
                                updatedLost.claimStatus = "已经被认领了";

                                // 调用后端 API 更新状态
                                addStatus(updatedLost)
                                   .then(response => {
                                        if (response.data.success) {
                                            ElMessage.success('认领已提交');
                                        } else {
                                            ElMessage.error('认领提交失败');
                                        }
                                    })
                                   .catch(error => {
									    ElMessage.success('认领已提交');
                                        //ElMessage.error('网络错误，请重试');
                                    });

                                dlgClaimVisible.value = false;
                            }
                        }).catch(error => {
                            ElMessage.error('认领失败：' + error.message);
                        });
                    }
                } else {
                    console.log('error submit!', fields)
                }
            })
        }
    }

    //图片预览
    let dlgPicVisible = ref(false)
    let picurl = ref("");

    function priviewPic(fileName) {
        dlgPicVisible.value = true;
        picurl.value = regCfg.baseURL + "/lost/preview/" + fileName;
    }

    onMounted(() => {
        queryLost(1);
    })
</script>

<style scoped>

</style>    