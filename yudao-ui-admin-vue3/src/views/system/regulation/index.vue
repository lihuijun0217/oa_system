<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          class="!w-240px"
          clearable
          placeholder="请输入规章制度标题"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          placeholder="请选择状态"
        >
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="已下架" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          class="!w-240px"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="openForm('create')">
          <Icon class="mr-5px" icon="ep:plus" />
          新增
        </el-button>
      </el-col>
    </el-row>
    
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="标题" align="center" prop="title" min-width="200" show-overflow-tooltip />
      <el-table-column label="文件名" align="center" prop="fileName" min-width="150" show-overflow-tooltip />
      <el-table-column label="文件大小" align="center" prop="fileSize">
        <template #default="scope">
          {{ formatFileSize(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'info' : scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.status === 0 ? '草稿' : scope.row.status === 1 ? '已发布' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="publishTime" :formatter="dateFormatter" />
      <el-table-column label="创建时间" align="center" prop="createTime" :formatter="dateFormatter" />
      <el-table-column label="操作" align="center" fixed="right" width="250">
        <template #default="scope">
          <el-button link type="primary" @click="handleDownload(scope.row)">
            下载
          </el-button>
          <el-button link type="primary" @click="openForm('edit', scope.row.id)">
            编辑
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            link
            type="success"
            @click="handlePublish(scope.row.id)"
          >
            发布
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            link
            type="warning"
            @click="handleUnpublish(scope.row.id)"
          >
            下架
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗 -->
  <RegulationForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import { downloadRegulationFile } from '@/utils/download'
import * as RegulationApi from '@/api/system/regulation'
import RegulationForm from './RegulationForm.vue'
import { Icon } from '@/components/Icon'

defineOptions({ name: 'SystemRegulation' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: '',
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询规章制度列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RegulationApi.getRegulationPage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await RegulationApi.deleteRegulation(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 发布按钮操作 */
const handlePublish = async (id: number) => {
  try {
    // 发布的二次确认
    await message.confirm('是否确认发布该规章制度？')
    // 发起发布
    await RegulationApi.publishRegulation(id)
    message.success('发布成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 下架按钮操作 */
const handleUnpublish = async (id: number) => {
  try {
    // 下架的二次确认
    await message.confirm('是否确认下架该规章制度？')
    // 发起下架
    await RegulationApi.unpublishRegulation(id)
    message.success('下架成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 下载文件 */
const handleDownload = async (row: any) => {
  try {
    await downloadRegulationFile(row.id, row.fileName, row.fileUrl)
    message.success('文件下载已开始')
  } catch (error) {
    message.error('文件下载失败，请重试')
    console.error('下载失败:', error)
  }
}

/** 格式化文件大小 */
const formatFileSize = (bytes?: number) => {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script> 