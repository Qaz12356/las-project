<template>
  <el-row style="height: 60px;display: flex;align-items: center;padding: 4px;">
    <el-col :span="18">
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="失物名称" :label-width="80">
          <el-input v-model="queryForm.lostName" placeholder="文件名称" style="width: 200px;" />
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
        <el-table-column prop="lostName" label="失物名称" width="150" />
        <el-table-column prop="lostType.lostTypeName" label="归属类型" width="150" />
        <el-table-column prop="lostDate" label="拾到时间" width="120" />
        <el-table-column prop="lostAddr" label="拾到地址" width="200" />
        <el-table-column prop="lostDespn" label="失物描述" width="200" />
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
       <!-- <el-button type="success" @click="openDlg()">认领</el-button> -->
      </div>
    </el-col>
  </el-row>

  <el-dialog v-model="dlgClaimVisible" title="失物认领申请" width="400" :modal="true">
    <el-form :model="lostForm" :rules="rules" ref="lostRef">
      <el-form-item label="失物名称" :label-width="80" prop="lostName">
        <el-input v-model="lostForm.lostName" :disabled="isReadOnly" />
      </el-form-item>
      <el-form-item label="拾到地址" :label-width="80" prop="lostAddr">
        <el-input v-model="lostForm.lostAddr" :disabled="isReadOnly" />
      </el-form-item>
      <el-form-item label="拾到时间" :label-width="80" prop="lostDate">
        <el-date-picker v-model="lostForm.lostDate" type="date" placeholder="选择日期" size="default"
          format="YYYY-MM-DD" value-format="YYYY-MM-DD" :disabled="isReadOnly" />
      </el-form-item>
      <el-form-item label="失物类型" :label-width="80" prop="lostTypeId">
        <el-select v-model="lostForm.lostTypeId" placeholder="选择类型" size="default" :disabled="isReadOnly">
          <el-option v-for="item in lostTypeOpts" :key="item.lostTypeId" :label="item.lostTypeName"
            :value="item.lostTypeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="失物描述" :label-width="80" prop="lostDespn">
        <el-input v-model="lostForm.lostDespn" :disabled="isReadOnly" />
      </el-form-item>
      <el-button link type="primary" size="small" @click.prevent="priviewPic(lostForm.picPath)">
        预览图片
      </el-button>
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
  ref
} from 'vue';
import {
  regCfg
} from "@/utils/config.js";
import {
  findLost,
  updLost,
  
  addStatus,
  delLost
} from '@/apis/lost.js';
import {
  findAllLostType
} from "@/apis/lostType.js"
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
let isReadOnly = ref(false); // 用于控制表单是否只读的标志位
initPage();

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
  queryLost(1); // 初始化时直接查询数据
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
    // 为每一行添加 claimStatus 属性并设置初始值为“未认领”
    // res.rows.forEach(row => {
    //   row.claimStatus = "未认领";
    // });
    Object.assign(tableData.rows, res.rows);
    tableData.total = res.total;
  })
}
// 上传
let dlgClaimVisible = ref(false)
const lostForm = reactive({
  lostAddr: "",
  claimStatus: "",
  lostName: "",
  lostTypeId: 0,
  lostDate: "",
  lostDespn: "",
  picPath: "",
  lostId:null
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
      message: '失物名称长度为2-50',
      trigger: 'blur'
    },
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
      console.log("value:", value)
      if (value == 0) {
        callback(new Error('请选择类型'));
      } else {
        callback();
      }
    }
  }]
}

let currentRow = null;

function selectRow(row) {
  currentRow = row;
  lostForm.lostTypeId = row.lostType? row.lostType.lostTypeId : 0;
  lostForm.picPath = row.picPath;
}

function openDlg() {
  if (currentRow == null) {
    ElMessageBox.alert('请选中要认领的失物', '提示', {
      confirmButtonText: 'OK',
    })
  } else if (currentRow.claimStatus === '认领申请中') {
    ElMessageBox.alert('该失物已经被申请认领了，如有疑问请联系管理员谢谢', '提示', {
      confirmButtonText: 'OK',
    })
  } else {
    dlgClaimVisible.value = true;
    Object.assign(lostForm, currentRow);
    lostForm.lostTypeId = currentRow.lostType? currentRow.lostType.lostTypeId : 0;
    isReadOnly.value = true; // 打开对话框时设置为只读
  }
}

function saveClaim() {
  if (currentRow) {
    // 更新当前行的状态
    currentRow.claimStatus = "认领申请中"; 
    // 复制当前行的所有字段到一个新对象
    const updatedLost = { ...currentRow }; 
    // 更新认领状态
    updatedLost.claimStatus = "认领申请中"; 

    // 调用后端 API 更新状态
    addStatus(updatedLost)
    .then(response => {
      if (response.data.success) {
        ElMessage.success('认领申请已提交');
      } else {
        ElMessage.error('认领申请提交失败');
      }
    })
    .catch(error => {
      ElMessage.error('网络错误，请重试');
    });

    dlgClaimVisible.value = false;
  }
}
// 图片预览
let dlgPicVisible = ref(false)
let picurl = ref("");

function priviewPic(fileName) {
  dlgPicVisible.value = true;
  console.log(regCfg.baseURL + "/lost/preview/" + fileName);
  picurl.value = regCfg.baseURL + "/lost/preview/" + fileName;
}

onMounted(() => {
  // 可以在这里不做任何操作，因为 initPage 中已经调用了 queryLost
})
</script>

<style scoped>

</style>