<template>
	<el-row style="height: 60px;display: flex;align-items: center;padding: 4px;">
		<el-col :span="18">
			失物类型名称：<el-input v-model="lostTypeName" style="width: 240px" placeholder="输入失物类型名称" />
		</el-col>
		<el-col :span="6">
			<el-button type="primary" @click="queryLostType()">查询</el-button>
		</el-col>
	</el-row>
	<el-row>
		<el-col :span="24">
			<el-table v-loading="tblLoading" highlight-current-row :data="tableData.rows" style="width: 100%"
				:stripe="true" current-row-key="lostTypeId" border @row-click="selectRow">
				<el-table-column type="index" width="50" />
				<el-table-column prop="lostTypeId" label="失物类型编号" width="120" />
				<el-table-column prop="lostTypeName" label="失物类型名称" width="200" />
			</el-table>
		</el-col>
	</el-row>
	<el-row>
		<el-col :span="24">
			<div style="display: flex;justify-content:end;padding: 5px 4px;">
				<el-button type="success" @click="openDlg('add')">添加</el-button>
				<el-button type="danger" @click="delDlg">删除</el-button>
				<el-button type="info" @click="openDlg('upd')">修改</el-button>
			</div>
		</el-col>
	</el-row>

	<el-dialog v-model="dlgLostTypeVisible" :title="title" width="400" :modal="true">
		<el-form :model="lostTypeForm" ref="ruleFormRef" :rules="rules">
			<el-form-item label="失物类型名称" :label-width="120" prop="lostTypeName">
				<el-input v-model="lostTypeForm.lostTypeName" />
			</el-form-item>
		</el-form>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dlgLostTypeVisible = false">取消</el-button>
				<el-button type="primary" @click="saveLostType">确定</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script setup>
	import {
		onMounted,
		reactive,
		 ref
	} from 'vue';

	import {
		findLostType,
		addLostType,
		updLostType,
		delLostType
	} from '@/apis/lostType.js';
	//查询开始
	let tblLoading = ref(false);
	let lostTypeName = ref("");

	let tableData = reactive({
		rows: []
	})

	function queryLostType() {
		tblLoading.value = true;
		tableData.rows.length = 0;
		findLostType(lostTypeName.value).then(res => {
			Object.assign(tableData.rows, res);
			tblLoading.value = false
		});
	}

	let currentRow = null;

	function selectRow(row) {
		currentRow = row;
	}


	onMounted(() => {
		queryLostType();
	})
	//查询结束


	//对话框
	let title = ref("增加");
	let dlgLostTypeVisible = ref(false);
	//表单
	let lostTypeForm = reactive({
		lostTypeName: "",
		lostTypeId: null
	})
	let ruleFormRef = ref()
	//校验规则

	const rules = reactive({
		lostTypeName: [{
				required: true,
				message: '请输入失物类型名称',
				trigger: 'blur'
			},
			{
				min: 1,
				max: 15,
				message: '失物类型名称长度为1-15',
				trigger: 'blur'
			},
		]
	})

	function saveLostType() {
		ruleFormRef.value.validate((valid, fields) => {
			if (valid) {
				if (lostTypeForm.lostTypeId == null) {
					addLostType(lostTypeForm).then(res => {
						ElMessage('保存成功');
						queryLostType();
						dlgLostTypeVisible.value = false;
					})
				} else {
					updLostType(lostTypeForm).then(res => {
						ElMessage('保存成功');
						queryLostType();
						dlgLostTypeVisible.value = false;
					})
				}


			} else {
				console.log('error submit!', fields)
			}
		})
	}

	function openDlg(flag) {
		if (flag == "add") {
			title.value = "新增失物类型";
			lostTypeForm.lostTypeName = ""
			lostTypeForm.lostTypeId = null
			dlgLostTypeVisible.value = true;
		} else if (flag == "upd") {
			title.value = "修改失物类型";
			if (currentRow == null) {
				ElMessageBox.alert('请选中要修改的失物类型', '提示', {
					confirmButtonText: 'OK',
				})
			} else {
				dlgLostTypeVisible.value = true;
				Object.assign(lostTypeForm, currentRow);
			}
		}


	}
	//对话框结束

	function delDlg() {
		if (currentRow == null) {
			ElMessageBox.alert('请选中要删除的失物类型', '提示', {
				confirmButtonText: 'OK',
			})
		} else {
			ElMessageBox.confirm(
					'确认要删除失物类型：' + currentRow.lostTypeName + "?",
					'警告', {
						confirmButtonText: '确认',
						cancelButtonText: '取消',
						type: 'warning',
					}
				)
				.then(() => {
					delLostType(currentRow.lostTypeId).then(res => {
						ElMessage('删除成功');
						queryLostType();
					})
				})
				.catch(() => {

				})
		}
	}
</script>

<style scoped>
</style>