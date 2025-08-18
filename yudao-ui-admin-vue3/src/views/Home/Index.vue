<template>
  <div class="home-index">
    <!-- 第一行：图片新闻 + 通知公告 -->
    <div class="block block-1" style="height: 380px;">
      <!-- 图片新闻模块（左侧，占2/3宽度） -->
      <el-card class="card news-card">
        <template #header>
          <div class="card-header">
            <span>图片新闻</span>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/system/news')">更多</el-link>
          </div>
        </template>
        <div class="news-content">
          <!-- 左侧图片轮播 -->
          <div class="news-carousel">
            <el-carousel height="250px" indicator-position="outside">
              <el-carousel-item v-for="item in imageNewsList" :key="item.id">
                <div class="carousel-item" @click="handleNewsClick(item)">
                  <img :src="item.imageUrl" :alt="item.title" class="carousel-image" />
                  <div class="carousel-title">{{ item.title }}</div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>
          <!-- 右侧新闻列表 -->
          <div class="news-list">
            <ul class="news-item-list">
              <li v-for="item in textNewsList" :key="item.id" class="news-item" @click="handleNewsClick(item)">
                <span class="news-title">{{ item.title }}</span>
                <span class="news-date">{{ item.createTime }}</span>
              </li>
            </ul>
          </div>
        </div>
      </el-card>
      
      <!-- 通知公告模块（右侧，占1/3宽度） -->
      <el-card class="card notice-card">
        <template #header>
          <div class="card-header">
            <span>通知公告</span>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/system/notice')">更多</el-link>
          </div>
        </template>
        <div class="card-body pt-0">
          <ul class="notice-list">
            <li v-for="item in noticeList" :key="item.id" class="notice-item" @click="handleNoticeClick(item)">
              <span class="notice-title">{{ item.title }}</span>
              <span class="notice-date">{{ item.createTime }}</span>
            </li>
          </ul>
        </div>
      </el-card>
    </div>

    <!-- 第二行：待办任务、规章制度、流程待办/已办/我的 -->
    <div class="block block-1" style="height: 360px;">
      <div class="block block-4">
        <!-- 待办任务 -->
        <el-card class="card todo-card">
          <template #header>
            <div class="card-header">
              <span>待办任务</span>
              <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/todo')">更多</el-link>
            </div>
          </template>
          <div class="card-body pt-0">
            <div v-if="todoTasks && todoTasks.length > 0">
              <el-table :data="todoTasks" stripe @row-click="handleTodoTaskClick" class="cursor-pointer">
                <el-table-column label="流程名称" min-width="200" show-overflow-tooltip>
                  <template #default="{ row }">
                    {{ row.processInstance?.name || row.processInstanceId }}
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="任务名称" show-overflow-tooltip />
                <!-- <el-table-column prop="createTime" label="创建时间" :formatter="(row) => dateFormatter(row.createTime)" /> -->
              </el-table>
            </div>
            <el-empty v-else description="暂无待办任务" />
          </div>
        </el-card>

        <!-- 规章制度 -->
        <el-card class="card regulation-card">
          <template #header>
            <div class="card-header">
              <span>规章制度</span>
              <el-link type="primary" :underline="false" @click="handleShortcutClick('/system/regulation')">更多</el-link>
            </div>
          </template>
          <div class="card-body pt-0">
            <ul class="regulation-list">
              <li v-for="item in regulationList" :key="item.id" class="regulation-item" @click="handleRegulationClick(item)">
                <span class="regulation-title">{{ item.title }}</span>
                <span class="regulation-date">{{ item.createTime }}</span>
              </li>
            </ul>
          </div>
        </el-card>
      </div>
      <!-- 流程待办/已办/我的 -->
      <el-card class="card process-card">
        <template #header>
          <div class="card-header">
            <span>当前的待办</span>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/my')">更多</el-link>
          </div>
        </template>
        <div class="card-body">
          <div class="process-stats">
            <div class="stat-item" @click="handleShortcutClick('/bpm/task/todo')">
              <div class="stat-number">{{ todoCount > 99 ? '99+' : todoCount }}</div>
              <div class="stat-label">待办</div>
            </div>
            <div class="stat-item" @click="handleShortcutClick('/bpm/task/done')">
              <div class="stat-number">{{ doneCount > 99 ? '99+' : doneCount }}</div>
              <div class="stat-label">已办</div>
            </div>
            <div class="stat-item" @click="handleShortcutClick('/bpm/task/my')">
              <div class="stat-number">{{ myCount > 99 ? '99+' : myCount }}</div>
              <div class="stat-label">我的流程</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
  
  <!-- 通知详情弹窗 -->
  <Dialog v-model="noticeDetailVisible" title="通知详情" width="50%">
    <el-descriptions :column="1" border v-loading="noticeDetailLoading">
      <el-descriptions-item label="公告标题">{{ noticeDetail.title }}</el-descriptions-item>
      <el-descriptions-item label="公告类型">
        <dict-tag :type="DICT_TYPE.SYSTEM_NOTICE_TYPE" :value="noticeDetail.type" />
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="noticeDetail.status" />
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ dateFormatter(noticeDetail.createTime) }}</el-descriptions-item>
      <el-descriptions-item label="公告内容">
        <div v-html="noticeDetail.content"></div>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>

  <!-- 新闻详情弹窗 -->
  <Dialog v-model="newsDetailVisible" title="新闻详情" width="50%">
    <el-descriptions :column="1" border v-loading="newsDetailLoading">
      <el-descriptions-item label="新闻标题">{{ newsDetail.title }}</el-descriptions-item>
      <el-descriptions-item label="新闻类型">
        <el-tag :type="newsDetail.type === 1 ? 'primary' : 'success'">
          {{ newsDetail.type === 1 ? '图片新闻' : '文字新闻' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="newsDetail.status" />
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ dateFormatter(newsDetail.createTime) }}</el-descriptions-item>
      <el-descriptions-item label="新闻内容">
        <div v-html="newsDetail.content"></div>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/modules/user'
import { getTaskTodoPage, getTaskDonePage } from '@/api/bpm/task'
import * as ProcessApi from '@/api/bpm/processInstance'
import { getDept } from '@/api/system/dept'
import { getBpmModelList } from '@/api/bpm/model'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Dialog } from '@/components/Dialog'
import { DictTag } from '@/components/DictTag'
import { useRouter } from 'vue-router'
import * as NoticeApi from '@/api/system/notice'
import * as NewsApi from '@/api/system/news'
import * as RegulationApi from '@/api/system/regulation'
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { downloadRegulationFile } from '@/utils/download'
import type { ProcessInstanceVO } from '@/api/bpm/processInstance'
import type { TaskVO } from '@/api/bpm/task/types'

const userStore = useUserStore()
const router = useRouter()
const user = userStore.getUser
const welcomeText = '欢迎回来，祝您工作愉快！'

const todoCount = ref(0)
const doneCount = ref(0)
const myCount = ref(0)
const deptName = ref('')
// 待办任务
const todoTasks = ref<TaskVO[]>([])
// 第二模块数据
const noticeList = ref<any[]>([])

const workflowList = ref<any[]>([])

// 通知详情相关
const noticeDetailVisible = ref(false)
const noticeDetailLoading = ref(false)
const noticeDetail = ref<any>({})

// 新闻详情相关
const newsDetailVisible = ref(false)
const newsDetailLoading = ref(false)
const newsDetail = ref<any>({})

// 新闻数据
const imageNewsList = ref<any[]>([])
const textNewsList = ref<any[]>([])

// 规章制度数据
const regulationList = ref<any[]>([])

const fetchStats = async () => {
  try {
    // 待办
    const todoRes = await getTaskTodoPage({ pageNo: 1, pageSize: 5 })
    todoCount.value = todoRes.total || 0
    todoTasks.value = todoRes.list || []
    console.log('待办任务数据:', todoTasks.value)
    
    // 已办
    const doneRes = await getTaskDonePage({ pageNo: 1, pageSize: 1 })
    doneCount.value = doneRes.total || 0
    
    // 我的
    const myRes = await ProcessApi.getProcessInstanceMyPage({ pageNo: 1, pageSize: 5 })
    myCount.value = myRes.total || 0
  } catch (error) {
    console.error('获取流程和任务数据失败:', error)
  }finally {
    console.log('当前数据状态:', {
      todoTasks: todoTasks.value,
    })
  }
}

const fetchDeptName = async () => {
  if (user.deptId) {
    try {
      console.log('开始获取部门信息，用户deptId:', user.deptId)
      const res = await getDept(user.deptId)
      deptName.value = res.name || ''
      // console.log('获取部门信息成功:', res.name)
    } catch (error) {
      // console.error('获取部门信息失败:', error)
      // 如果获取部门信息失败，不影响页面其他功能
      deptName.value = ''
      // 可以在这里添加更详细的错误处理，比如显示默认值
    }
  } else {
    // console.log('用户没有deptId，跳过获取部门信息')
  }
}

const fetchNotices = async () => {
  try {
    console.log('开始获取通知公告...')
    const res = await NoticeApi.getNoticePage({ pageNo: 1, pageSize: 5 })
    console.log('通知公告API响应:', res)
    noticeList.value = res.list.map((item: any) => ({
      id: item.id,
      title: item.title,
      createTime: item.createTime ? new Date(item.createTime).toLocaleDateString() : ''
    }))
    console.log('处理后的通知公告数据:', noticeList.value)
  } catch (error) {
    console.error('获取通知公告失败:', error)
    // 添加一些模拟数据用于测试
    noticeList.value = [
      { id: 1, title: '系统维护通知', createTime: '2024-01-15' },
      { id: 2, title: '新功能上线公告', createTime: '2024-01-14' }
    ]
  }
}



// 获取新闻数据
const fetchNews = async () => {
  try {
    // 从后端API获取新闻数据
    const res = await NewsApi.getNewsPage({ pageNo: 1, pageSize: 10, status: 1 })
    
    // 分离图片新闻和文字新闻
    const imageNews = res.list.filter((item: any) => item.type === 1 && item.status === 1)
    const textNews = res.list.filter((item: any) => item.type === 2 && item.status === 1)
    
    // 设置图片新闻数据（最多3条）
    imageNewsList.value = imageNews.slice(0, 3).map((item: any) => ({
      id: item.id,
      title: item.title,
      imageUrl: item.imageUrl || 'https://via.placeholder.com/400x200/409EFF/FFFFFF?text=默认图片',
      content: item.content,
      type: item.type,
      status: item.status,
      createTime: item.createTime ? new Date(item.createTime).toLocaleDateString() : ''
    }))

    // 设置文字新闻数据（最多5条）
    textNewsList.value = textNews.slice(0, 5).map((item: any) => ({
      id: item.id,
      title: item.title,
      content: item.content,
      type: item.type,
      status: item.status,
      createTime: item.createTime ? new Date(item.createTime).toLocaleDateString() : ''
    }))
  } catch (error) {
    console.error('获取新闻数据失败:', error)
    // 如果API调用失败，使用模拟数据
    imageNewsList.value = [
      {
        id: 1,
        title: '公司年度总结大会圆满召开',
        imageUrl: 'https://via.placeholder.com/400x200/409EFF/FFFFFF?text=年度总结大会',
        content: '公司年度总结大会圆满召开，回顾过去一年的成就，展望未来发展...',
        type: 1,
        status: 1,
        createTime: '2024-01-15'
      }
    ]
    textNewsList.value = [
      {
        id: 4,
        title: '关于加强安全生产管理的通知',
        content: '为进一步加强安全生产管理，确保员工生命财产安全...',
        type: 2,
        status: 1,
        createTime: '2024-01-12'
      }
    ]
  }
}

// 获取规章制度数据
const fetchRegulations = async () => {
  try {
    // 从后端API获取规章制度数据
    const res = await RegulationApi.getRegulationPage({ pageNo: 1, pageSize: 5, status: 1 })
    
    // 设置规章制度数据
    regulationList.value = res.list.map((item: any) => ({
      id: item.id,
      title: item.title,
      fileName: item.fileName,
      fileUrl: item.fileUrl,
      fileSize: formatFileSize(item.fileSize),
      createTime: item.createTime ? new Date(item.createTime).toLocaleDateString() : ''
    }))
  } catch (error) {
    console.error('获取规章制度数据失败:', error)
    // 如果API调用失败，使用模拟数据
    regulationList.value = [
      {
        id: 1,
        title: '员工手册',
        fileName: '员工手册.pdf',
        fileUrl: 'https://example.com/files/员工手册.pdf',
        fileSize: '2.5MB',
        createTime: '2024-01-15'
      }
    ]
  }
}

// 格式化文件大小
const formatFileSize = (bytes?: number) => {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const fetchWorkflowModels = async () => {
  try {
    const res = await getBpmModelList({})
    workflowList.value = res.map((item: any) => ({
      id: item.id,
      name: item.name,
      key: item.key, // 添加key字段
      icon: item.icon,
      url: item.url
    }))
  } catch (error) {
    console.error('获取业务流程模型失败:', error)
  }
}

const handleWorkflowClick = async (item: any) => {
  // 检查流程是否有效（是否有flowid且与workflowList中的流程匹配）
  const isValidFlow = item.flowid && workflowList.value.some(workflow => workflow.key === item.flowid)
  
  if (!isValidFlow) {
    ElMessage.warning('该流程正在开发中，敬请期待！')
    return
  }
  
  // 找到匹配的流程定义
  const matchedWorkflow = workflowList.value.find(workflow => workflow.key === item.flowid)
  
  if (matchedWorkflow) {
    // 跳转到新建流程页面，并传递流程定义信息
    await router.push({
      name: 'BpmProcessInstanceCreate',
      query: { 
        processDefinitionKey: matchedWorkflow.key,
        processDefinitionName: matchedWorkflow.name
      }
    })
  } else {
    ElMessage.error('流程配置异常，请联系管理员')
  }
}

const handleShortcutClick = (url: string) => {
  router.push(url)
}

const handleNoticeClick = async (item: any) => {
  noticeDetailVisible.value = true
  noticeDetailLoading.value = true
  try {
    noticeDetail.value = await NoticeApi.getNotice(item.id)
  } finally {
    noticeDetailLoading.value = false
  }
}

const handleTodoTaskClick = (row: TaskVO) => {
  // 跳转到流程详情页面
  router.push(`/bpm/process-instance/detail?id=${row.processInstanceId}&taskId=${row.id}`)
}

const handleNewsClick = (item: any) => {
  newsDetail.value = item
  newsDetailVisible.value = true
  newsDetailLoading.value = true
  // 模拟加载新闻详情
  setTimeout(() => {
    newsDetailLoading.value = false
  }, 1000)
}

const handleRegulationClick = async (item: any) => {
  try {
    // 显示确认对话框
    await ElMessageBox.confirm(
      `确定要下载《${item.title}》吗？`,
      '下载确认',
      {
        confirmButtonText: '确定下载',
        cancelButtonText: '取消',
        type: 'info'
      }
    )
    
    // 用户确认后，使用下载工具函数
    if (item.id) {
      await downloadRegulationFile(item.id, item.fileName, item.fileUrl)
      ElMessage.success('文件下载已开始')
    } else {
      ElMessage.error('文件信息不完整，无法下载')
    }
  } catch (error) {
    // 用户取消下载或下载失败
    if (error !== 'cancel') {
      ElMessage.error('文件下载失败，请重试')
    }
    console.log('下载操作:', error === 'cancel' ? '用户取消下载' : '下载失败')
  }
}



onMounted(() => {
  fetchStats()
  fetchDeptName()
  fetchNotices()
  fetchWorkflowModels()
  fetchNews()
  fetchRegulations()
})
</script>

<style scoped>
.home-index {
  padding-bottom: 2px; /* 为底部栏留出足够空间 */
  box-sizing: border-box;
}
.block-1 {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.block-2 {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}
.block-4 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  height: 100%;
  margin-bottom: 24px;
}

/* 左边横幅 */
.banner-card {
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
  border: none;
}

.banner-card :deep(.el-card__body) {
  padding: 0;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
}

.banner-content {
  color: #fff;
  text-align: center;
  padding: 24px;
}

.banner-text {
  font-size: 24px;
  font-weight: 600;
  line-height: 1.4;
}

/* 中间文件统计 */
.stats-card :deep(.el-card__body) {
  padding: 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: 1fr;
  gap: 16px;
  width: 100%;
  height: 100%;
  align-items: center;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  color: #fff;
  font-weight: 600;
  text-align: center;
  padding: 16px 12px;
  cursor: pointer;
  transition: transform 0.2s ease;
  height: 120px;
  min-height: 120px;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-orange {
  background-color: #ff7a45;
}

.stat-cyan {
  background-color: #13c2c2;
}

.stat-purple {
  background-color: #722ed1;
}

.stat-blue {
  background-color: #1890ff;
}

.stat-green {
  background-color: #52c41a;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  opacity: 0.95;
  font-weight: 500;
  line-height: 1.2;
}

/* 右边用户信息 */
.user-card :deep(.el-card__body) {
  padding: 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-content {
  text-align: center;
  width: 100%;
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #409EFF;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  margin: 0 auto 16px;
  overflow: hidden;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.user-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.user-motto {
  font-size: 12px;
  color: #999;
  font-style: italic;
}

/* 第二模块样式 */
.block-2 {
  display: grid;
  grid-template-columns: 30% 50% 1fr;
  gap: 24px;
  margin-bottom: 24px;
  /* border: 2px solid red; 调试用 */
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-body {
  padding: 12px;
}
.pt-0{
  padding-top: 0;
}

/* 通知公告 */
.notice-list,
.inbox-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-item,
.inbox-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px dashed #eee;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: background-color 0.2s;
}

.notice-item:hover,
.inbox-item:hover {
  background-color: #f5f5f5;
}

.notice-item:last-child,
.inbox-item:last-child {
  border-bottom: none;
}

.notice-title {
  flex-grow: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
  color: #333;
  cursor: pointer;
}

.notice-title:hover {
  color: #409EFF;
}

.notice-date,
.inbox-date {
  flex-shrink: 0;
  color: #999;
}

.inbox-sender {
  flex-grow: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
  color: #333;
  cursor: pointer;
}

.inbox-sender:hover {
  color: #409EFF;
}

/* 系统导航 */
.system-nav-grid {

  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 10px;
  align-item:  center;
}
.system-nav-card{
    display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 10px;
  align-item:  center;
  padding-top: 0;
}
.system-nav-grid-item{
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 50px;
  padding: 10px;
}
.system-nav-card :deep(.el-card__body) {
  padding: 20px;

  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.nav-item:hover {
  transform: translateY(-3px);
}

.nav-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28px;
  margin-bottom: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

/* 空状态样式 */
.empty-state {
  padding: 20px 0;
  text-align: center;
}

/* 业务流程入口样式 */
.workflow-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
  padding: 16px;
  margin-bottom: 24px;
}

.block-3 {
  height: 600px;

}

.block-3 .workflow-card{
  padding-top: 0;
}

.workflow-grid {
  display: grid;
  grid-template-columns: repeat(13, 1fr);
  gap: 12px;
  padding: 16px 0;
  max-width: 100%;
  overflow: hidden;
}

.workflow-item {
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding: 12px 8px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  min-width: 0; /* 防止内容溢出 */
}

/* 有效流程的样式 */
.workflow-item.valid {
  background-color: #f0f8ff;
  border-color: #b3d8ff;
}

.workflow-item.valid:hover {
  background-color: #e6f3ff;
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
  transform: translateY(-3px);
}

.workflow-item:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
  transform: translateY(-3px);
}

.workflow-icon {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 8px;
  margin-top: 4px;
  font-size: 20px;
  color: #4dabf7;
  background: transparent;
  box-shadow: none;
  transition: all 0.3s ease;
  flex-shrink: 0; /* 防止图标被压缩 */
}

.workflow-item:hover .workflow-icon {
  color: #1890ff;
  transform: scale(1.1);
}

.workflow-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
}

.workflow-default-icon {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  color: #4dabf7;
  background: transparent;
  border-radius: 8px;
}

.workflow-name {
  font-size: 12px;
  color: #444;
  font-weight: 500;
  max-width: 100%;
  line-height: 1.3;
  word-break: break-word;
  text-align: center;
  padding: 0 2px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.workflow-name-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  word-break: break-word;
  line-height: 1.3;
}

/* 新闻模块样式 */
.news-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}

.news-content {
  display: flex;
  gap: 20px;
  height: 280px;
}

.news-carousel {
  flex: 1;
  border-radius: 8px;
  overflow: hidden;
}

.carousel-item {
  position: relative;
  height: 100%;
  cursor: pointer;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: white;
  padding: 15px 15px 10px;
  font-size: 14px;
  font-weight: 500;
}

.news-list {
  flex: 1;
  overflow: hidden;
}

.news-item-list {
  list-style: none;
  padding: 0;
  margin: 0;
  height: 100%;
  overflow-y: auto;
}

.news-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.news-item:hover {
  background-color: #f5f5f5;
}

.news-item:last-child {
  border-bottom: none;
}

.news-title {
  flex: 1;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 10px;
}

.news-date {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

/* 规章制度样式 */
.regulation-list {
  list-style: none;
  padding: 0;
  margin: 0;
 /* height: 180px;
  overflow-y: auto;*/
}

.regulation-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.regulation-item:hover {
  background-color: #f5f5f5;
}

.regulation-item:last-child {
  border-bottom: none;
}

.regulation-title {
  flex: 1;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 10px;
}

.regulation-date {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

/* 流程统计样式 */
.process-stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 180px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.2s;
  padding: 20px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  min-width: 80px;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-item:nth-child(2) {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-item:nth-child(3) {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 12px;
  text-align: center;
}

/* 头部操作区域样式 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cursor-pointer {
  cursor: pointer;
}

/* 优化表格样式 */
.cursor-pointer :deep(.el-table) {
  font-size: 13px;
}

.cursor-pointer :deep(.el-table th) {
  padding: 8px 0;
}

.cursor-pointer :deep(.el-table td) {
  padding: 6px 0;
}

.cursor-pointer :deep(tbody tr:hover) {
  background-color: #f5f7fa;
}
</style>
