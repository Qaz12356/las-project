<template>
  <el-row style="height: 60px;display: flex;align-items: center;padding: 4px;">
    <el-col :span="18">
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="认领人名称" :label-width="100">
          <el-input v-model="queryForm.claimName" placeholder="认领人名称" style="width: 200px;" />
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="6">
      <el-button type="primary" @click="queryClaim(1)">查询</el-button>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-table v-loading="tblLoading" highlight-current-row :data="tableData.rows" style="width: 100%"
        :stripe="true" current-row-key="lost" border @row-click="selectRow">
        <el-table-column type="index" width="50" />
        <el-table-column prop="claimName" label="认领人" width="180" />
        <el-table-column prop="lost.lostName" label="失物名称" width="150" />
        <el-table-column prop="claimTime" label="认领时间" width="150" />
        <el-table-column prop="contactWay" label="联系方式" width="300" />
      </el-table>
    </el-col>
  </el-row>
  <el-row>
    <el-col>
      <el-pagination background layout="prev, pager, next" :total="tableData.total" @prev-click="queryClaim"
        @next-click="queryClaim" @current-change="queryClaim" />
    </el-col>
  </el-row>
  <!-- <el-row>
    <el-col :span="24">
      <div style="display: flex;justify-content:end;padding: 5px 4px;">
        <el-button type="success" @click="openDlg('add')">添加</el-button>
        <el-button type="danger" @click="delDlg">删除</el-button>
        <el-button type="info" @click="openDlg('upd')">修改</el-button>
      </div>
    </el-col>
  </el-row> -->
  <el-dialog v-model="dlgClaimVisible" :title="title" width="400" :modal="true">
    <el-form :model="claimForm" ref="claimFormRef" :rules="rules"> <!-- 添加 ref 属性 -->
      <el-form-item label="认领人" :label-width="80" prop="claimName">
        <el-input v-model="claimForm.claimName" />
      </el-form-item>
      <el-form-item label="失物名称" :label-width="80" prop="lostId">
        <el-select v-model="claimForm.lostId" placeholder="选择失物" size="default">
          <el-option v-for="item in filteredLostOpts" :key="item.lostId" :label="`${item.lostId} - ${item.lostName}`"
            :value="item.lostId" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系方式" :label-width="80" prop="contactWay">
        <el-input v-model="claimForm.contactWay" />
      </el-form-item>
      <el-form-item label="拾到时间" :label-width="80" prop="claimTime">
        <el-date-picker v-model="claimForm.claimTime" type="date" placeholder="选择日期" size="default"
          format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dlgClaimVisible = false">取消</el-button>
        <el-button type="primary" @click="saveClaim">确定</el-button>
      </div>
    </template>
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
  findClaiminfo,
  addClaiminfo,
  updClaiminfo,
  updClaimin,
  updateClaimInfo,
  delClaim
} from '@/apis/claiminfo.js';
import {
  findAllLost
} from "@/apis/lost.js"
import { ElMessage, ElMessageBox } from 'element-plus';

let queryForm = reactive({
  claimName: "",
  lostId: 0,
  pageNo: 1,
  pageSize: 10
})

// 定义 tblLoading
let tblLoading = ref(false);
// 定义并初始化 tableData 对象
let tableData = reactive({
  rows: [],     // 存储表格数据
  total: 0      // 存储总记录数
});
function queryClaim(pageNo) {
  if (undefined != pageNo) {
    queryForm.pageNo = pageNo
  } else {
    queryForm.pageNo = 1
  }

  tableData.rows.length = 0;
  tblLoading.value = true;
  findClaiminfo(queryForm).then(res => {
    tblLoading.value = false;
    // console.log(res)
    Object.assign(tableData.rows, res.rows);
    tableData.total = res.total;
  })
}

let dlgClaimVisible = ref(false)
let claimForm = reactive({
  claimName: "",
  contactWay: "",
  claimTime: "",
  lostId: 0
})
let claimFormRef = ref()
const rules = {
  contactWay: [{
      required: true,
      message: '请输入联系电话',
      trigger: 'blur',
      // validator: (rule, value, callback) => {

      // }
    },
    {
      min: 11,
      max: 11,
      message: '电话长度为11',
      trigger: 'blur'
    },
  ],
  claimName: [{
      required: true,
      message: '请输入认领人',
      trigger: 'blur'
    },
    {
      min: 2,
      max: 10,
      message: '编码长度为2-10',
      trigger: 'blur'
    },
  ],
  claimTime: [{
    required: true,
    message: '请选择认领日期',
    trigger: 'blur'
  }],
};
let title = ref("增加")
//对话框结束


let currentRow = null;

function selectRow(row) {
  currentRow = row;
}

function saveClaim() {
  if (claimFormRef.value) {
    claimFormRef.value.validate((valid, fields) => {
      if (valid) {
        if (claimForm.claimId == null) {
          addClaiminfo(claimForm).then(res => {
            ElMessage('保存成功');
            selectedLostIds.value.push(claimForm.lostId);
            localStorage.setItem('selectedLostIds', JSON.stringify(selectedLostIds.value));
            updateFilteredOpts();
            queryClaim();
            dlgClaimVisible.value = false;
            // 通知父组件失物已被认领，传递失物ID
            const event = new CustomEvent('lostClaimed', { detail: claimForm.lostId });
            document.dispatchEvent(event); 
          })
        } else {
          updClaimin(claimForm).then(res => {
            ElMessage('保存成功');
            queryClaim();
            dlgClaimVisible.value = false;
          })
        }
      } else {
        console.log('error submit!', fields)
      }
    })
  }
}

function openDlg(flag) {
  if (flag == "add") {
    title.value = "新增认领记录";
    claimForm.claimName = "",
    claimForm.claimTime = "",
    claimForm.contactWay = "",
    claimForm.lostId = 0,
    claimForm.lostTypeId = null
    dlgClaimVisible.value = true;
  } else if (flag == "upd") {
    title.value = "修改认领记录";
    if (currentRow == null) {
      ElMessageBox.alert('请选中要修改的认领记录', '提示', {
        confirmButtonText: 'OK',
      })
    } else {
      dlgClaimVisible.value = true;
      Object.assign(claimForm, currentRow);
    }
  }
}

function delDlg() {
  if (currentRow == null) {
    ElMessageBox.alert('请选中要删除的认领记录', '提示', {
      confirmButtonText: 'OK',
    })
  } else {
    ElMessageBox.confirm(
      '确认要删除认领人：' + currentRow.claimName + "的记录?",
      '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    .then(() => {
        delClaim(currentRow.claimId).then(res => {
          ElMessage('删除成功');
          // 从已选失物ID列表中移除对应的ID
          const index = selectedLostIds.value.indexOf(currentRow.lost.lostId);
          if (index!== -1) {
            selectedLostIds.value.splice(index, 1);
            // 更新本地存储
            localStorage.setItem('selectedLostIds', JSON.stringify(selectedLostIds.value));
            // 更新过滤后的选项
            updateFilteredOpts();
          }
          queryClaim();
        })
      })
    .catch(() => {

    })
  }
}

onMounted(() => {
  queryClaim(1);
})
</script>

<style scoped>

</style>