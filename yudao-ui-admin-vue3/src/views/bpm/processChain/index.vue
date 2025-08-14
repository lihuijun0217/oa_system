<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="流程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入串联流程名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="源流程" prop="sourceProcessKey">
        <el-input
          v-model="queryParams.sourceProcessKey"
          placeholder="请输入源流程标识"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="目标流程" prop="targetProcessKey">
        <el-input
          v-model="queryParams.targetProcessKey"
          placeholder="请输入目标流程标识"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="enabled">
        <el-select
          v-model="queryParams.enabled"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option label="启用" :value="true" />
          <el-option label="禁用" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['bpm:process-chain:create']">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="串联流程名称" align="center" prop="name" min-width="200" />
      <el-table-column label="源流程" align="center" prop="sourceProcessName" min-width="150" />
      <el-table-column label="目标流程" align="center" prop="targetProcessName" min-width="150" />
      <el-table-column label="字段映射" align="center" prop="fieldMapping" min-width="200">
        <template #default="scope">
          <div v-if="scope.row.fieldMapping && scope.row.fieldMapping.length > 0">
            <el-tag
              v-for="(mapping, index) in scope.row.fieldMapping"
              :key="index"
              size="small"
              class="mr-5px mb-5px"
              :type="getMappingType(mapping.transformRule)"
            >
              {{ mapping.sourceField }} → {{ mapping.targetField }}
              <span v-if="mapping.transformRule === 'wildcard'" class="wildcard-badge">*</span>
            </el-tag>
          </div>
          <span v-else class="text-gray-400">无字段映射</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="enabled" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'danger'">
            {{ scope.row.enabled ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sort" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDate(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="200">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['bpm:process-chain:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['bpm:process-chain:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗 -->
  <ProcessChainForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import * as ProcessChainApi from '@/api/bpm/processChain'
import ProcessChainForm from './ProcessChainForm.vue'

defineOptions({ name: 'BpmProcessChain' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: '',
  sourceProcessKey: '',
  targetProcessKey: '',
  enabled: undefined
})
const queryFormRef = ref() // 搜索的表单
const formRef = ref() // 表单 Ref

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessChainApi.getProcessChainPageApi(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 打开表单 */
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ProcessChainApi.deleteProcessChainApi(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 获取映射类型对应的标签类型 */
const getMappingType = (transformRule: string) => {
  switch (transformRule) {
    case 'direct': return 'primary'
    case 'default': return 'warning'
    case 'expression': return 'info'
    case 'wildcard': return 'success'
    default: return 'info'
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.wildcard-badge {
  background-color: #10b981;
  color: white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  display: inline-block;
  text-align: center;
  line-height: 16px;
  font-size: 10px;
  margin-left: 4px;
}
</style> 