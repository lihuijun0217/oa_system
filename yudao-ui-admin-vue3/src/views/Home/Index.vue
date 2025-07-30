<template>
  <div class="home-index">
    <!-- 第一模块：欢迎语、文件统计、当前登录人员及部门 -->
    <div class="block block-1" style="height: 400px;">
      <!-- 左边横幅 -->
      <el-card class="card banner-card">
        <div class="banner-content">
          <div class="banner-text">不忘初心，牢记使命</div>
        </div>
      </el-card>
      
      <!-- 中间文件统计 -->
      <el-card class="card stats-card">
        <div class="stats-grid">
          <div class="stat-card stat-orange" @click="handleShortcutClick('/bpm/task/todo')">
            <div class="stat-number">{{ todoCount > 99 ? '99+' : todoCount }}</div>
            <div class="stat-label">待办流程</div>
          </div>
          <div class="stat-card stat-purple" @click="handleShortcutClick('/bpm/task/done')">
            <div class="stat-number">{{ doneCount > 99 ? '99+' : doneCount }}</div>
            <div class="stat-label">已办流程</div>
          </div>
          <div class="stat-card stat-green" @click="handleShortcutClick('/bpm/task/my')">
            <div class="stat-number">{{ myCount > 99 ? '99+' : myCount }}</div>
            <div class="stat-label">我的流程</div>
          </div>
        </div>
      </el-card>
      
      <!-- 右边用户信息 -->
      <el-card class="card user-card">
        <div class="user-content">
          <div class="user-avatar">
            <img v-if="user.avatar" :src="user.avatar" :alt="user.nickname || '用户'" class="avatar-img" />
            <span v-else>{{ user.nickname ? user.nickname.charAt(0) : '好' }}</span>
          </div>
          <div class="user-name">{{ user.nickname || '人员' }}</div>
          <div class="user-title">{{ deptName || '办公室文书' }}</div>
          <div class="user-motto">细节决定成败</div>
        </div>
      </el-card>
    </div>
    <!-- 第二模块：通知公告、收件箱、系统导航 -->
    <div class="block block-2" style="height: 400px;">
      <!-- 通知公告 -->
      <el-card class="card notice-card">
        <template #header>
          <div class="card-header">
            <span>通知公告</span>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/system/notice')">更多</el-link>
          </div>
        </template>
        <div class="card-body">
          <ul class="notice-list">
            <li v-for="item in noticeList" :key="item.id" class="notice-item" @click="handleNoticeClick(item)">
              <span class="notice-title">{{ item.title }}</span>
              <span class="notice-date">{{ item.createTime }}</span>
            </li>
          </ul>
        </div>
      </el-card>

      <!-- 收件箱 -->
      <el-card class="card inbox-card">
        <template #header>
          <div class="card-header">
            <span>收件箱 ({{ unreadInboxCount }})</span>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/user/notify-message')">更多</el-link>
          </div>
        </template>
        <div class="card-body">
          <ul class="inbox-list">
            <li v-for="item in inboxList" :key="item.id" class="inbox-item" @click="handleShortcutClick('/user/notify-message')">
              <span class="inbox-sender" :title="item.templateContent">{{ item.templateContent }}</span>
              <span class="inbox-date">{{ item.date }}</span>
            </li>
          </ul>
        </div>
      </el-card>

      <!-- 系统导航 -->
      <el-card class="card system-nav-card">
        <template #header>
          <div class="card-header">
            <span>系统导航</span>
            <!-- <el-link type="primary" :underline="false">更多</el-link> -->
          </div>
        </template>
        <div class="card-body system-nav-grid">
          <div v-for="(nav, index) in systemNavs" :key="index" class="nav-item">
            <div class="nav-icon" :style="{ backgroundColor: nav.bgColor, color: nav.textColor }">
              <i v-if="nav.faIcon" :class="['fas', nav.faIcon]"></i>
            </div>
            <div class="nav-label">{{ nav.name }}</div>
          </div>
        </div>
      </el-card>
    </div>
    <!-- 第三模块：业务流程入口 -->
    <div class="block block-3">
      <el-card class="card workflow-card">
        <template #header>
          <div class="card-header">
            <span>业务流程入口</span>
            <!-- <div class="header-actions">
              <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/my')">
                查看更多
              </el-link>
            </div> -->
          </div>
        </template>
        <div class="card-body workflow-grid">
          <div v-for="item in workflowList" :key="item.id" class="workflow-item" @click="handleWorkflowClick(item)">
            <div class="workflow-icon">
              <img v-if="item.icon" :src="item.icon" :alt="item.name" class="workflow-img" />
              <div v-else class="workflow-default-icon">{{ item.name.charAt(0) }}</div>
            </div>
            <div class="workflow-name">{{ item.name }}</div>
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
      <el-descriptions-item label="创建时间">{{ formatDate(noticeDetail.createTime) }}</el-descriptions-item>
      <el-descriptions-item label="公告内容">
        <div v-html="noticeDetail.content"></div>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/store/modules/user'
import { getTaskTodoPage, getTaskDonePage, getTaskManagerPage } from '@/api/bpm/task'
import * as ProcessApi from '@/api/bpm/processInstance'
import { getDept } from '@/api/system/dept'
import { getNoticePage } from '@/api/system/notice'
import { getNotifyMessagePage } from '@/api/system/notify/message'
import { getBpmModelList } from '@/api/bpm/model'
import { ElMessage } from 'element-plus'
import { Icon } from '@/components/Icon'
import { useRouter } from 'vue-router'
import * as NoticeApi from '@/api/system/notice'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'

const userStore = useUserStore()
const router = useRouter()
const user = userStore.getUser
const welcomeText = '欢迎回来，祝您工作愉快！'

const todoCount = ref(0)
const doneCount = ref(0)
const myCount = ref(0)
const deptName = ref('')

// 第二模块数据
const activeNoticeTab = ref('group')
const noticeList = ref<any[]>([])
const inboxList = ref<any[]>([])
const unreadInboxCount = ref(26)

const systemNavs = ref([
  { name: '系统1', faIcon: 'fa-file-alt', bgColor: '#409EFF', textColor: '#fff' },
  { name: '系统2', faIcon: 'fa-exchange-alt', bgColor: '#67C23A', textColor: '#fff' },
  { name: '系统3', faIcon: 'fa-building', bgColor: '#F56C6C', textColor: '#fff' },
  { name: '系统4', faIcon: 'fa-desktop', bgColor: '#909399', textColor: '#fff' },
  { name: '系统5', faIcon: 'fa-flask', bgColor: '#409EFF', textColor: '#fff' },
  { name: '系统6', faIcon: 'fa-book', bgColor: '#F56C6C', textColor: '#fff' }
])

const workflowList = ref<any[]>([])

// 通知详情相关
const noticeDetailVisible = ref(false)
const noticeDetailLoading = ref(false)
const noticeDetail = ref<any>({})

const fetchStats = async () => {
  // 待办
  const todoRes = await getTaskTodoPage({ pageNo: 1, pageSize: 1 })
  todoCount.value = todoRes.total || 0
  // 已办
  const doneRes = await getTaskDonePage({ pageNo: 1, pageSize: 1 })
  doneCount.value = doneRes.total || 0
  // 我的
  const myRes = await ProcessApi.getProcessInstanceMyPage({ pageNo: 1, pageSize: 1 })
  myCount.value = myRes.total || 0
}

const fetchDeptName = async () => {
  if (user.deptId) {
    const res = await getDept(user.deptId)
    deptName.value = res.name || ''
  }
}

const fetchNotices = async () => {
  try {
    console.log('开始获取通知公告...')
    const res = await getNoticePage({ pageNo: 1, pageSize: 5 })
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

const fetchInbox = async () => {
  try {
    console.log('开始获取收件箱...')
    const res = await getNotifyMessagePage({ pageNo: 1, pageSize: 5 })
    console.log('收件箱API响应:', res)
    inboxList.value = res.list.map((item: any) => ({
      id: item.id,
      templateContent: item.templateContent,
      date: item.createTime ? new Date(item.createTime).toLocaleDateString() : ''
    }))
    console.log('处理后的收件箱数据:', inboxList.value)
  } catch (error) {
    console.error('获取收件箱失败:', error)
    // 添加一些模拟数据用于测试
    inboxList.value = [
      { id: 1, sender: '系统管理员', date: '2024-01-15' },
      { id: 2, sender: '人事部', date: '2024-01-14' }
    ]
  }
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
  // 跳转到新建流程页面，并传递流程定义信息
  await router.push({
    name: 'BpmProcessInstanceCreate',
    query: { 
      processDefinitionKey: item.key,
      processDefinitionName: item.name
    }
  })
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

const handleCreateProcess = () => {
  // 跳转到流程创建页面
  window.open('/bpm/process-instance/create', '_blank')
}

const handleInboxClick = (item: any) => {
  router.push(`/user/notify-message/${item.id}`)
}

onMounted(() => {
  fetchStats()
  fetchDeptName()
  fetchNotices()
  fetchInbox()
  fetchWorkflowModels()
})
</script>

<style scoped>
.home-index {
  padding: 24px;
}
.block-1 {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 24px;
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
  grid-template-columns: 1fr 1fr 1fr;
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
  padding: 16px;
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
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  padding: 10px;
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

.workflow-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}

.workflow-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.workflow-item:hover {
  transform: translateY(-3px);
}

.workflow-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.workflow-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 4px;
}

.workflow-default-icon {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28px;
  color: #fff;
  background-color: #409EFF;
  border-radius: 4px;
}

.workflow-name {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
</style>
