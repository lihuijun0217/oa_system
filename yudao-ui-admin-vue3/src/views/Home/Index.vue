<template>
  <div class="home-index">
    <!-- 第一模块：欢迎语、文件统计、当前登录人员及部门 -->

    
    <div class="block block-1" style="height: 400px;">
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
      
      <el-card shadow="never">
        <template #header>
          <div class="flex justify-between items-center">
            <div class="card-header">
             
              <span >待办任务</span>
            </div>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/todo')">
              查看更多
            </el-link>
          </div>
        </template>
          <div v-if="todoTasks && todoTasks.length > 0">
            <el-table :data="todoTasks" stripe @row-click="handleTodoTaskClick" class="cursor-pointer">
              <el-table-column label="流程名称" min-width="260" show-overflow-tooltip>
                <template #default="{ row }">
                  {{ row.processInstance?.name || row.processInstanceId }}
                </template>
              </el-table-column>
              <el-table-column prop="name" label="任务名称"  show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间"  :formatter="(row) => formatDate(row.createTime)" />
            </el-table>
          </div>
          <el-empty v-else description="暂无待办任务" />
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
      

    </div>
    <!-- 第二模块：通知公告、收件箱、系统导航 -->
    <div class="block block-2" style="height: 400px;">


      <!-- 收件箱 -->
      <el-card class="card inbox-card">
        <template #header>
          <div class="card-header">
            <span>消息中心</span>
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

      <el-card shadow="never">
        <template #header>
          <div class="flex justify-between items-center ">
            <div  class="card-header">
              
              <span >我的流程</span>
            </div>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/my')">
              查看更多
            </el-link>
          </div>
        </template>
          <div v-if="myProcesses && myProcesses.length > 0">
            <el-table :data="myProcesses" stripe @row-click="handleMyProcessClick" class="cursor-pointer">
              <el-table-column prop="name" label="流程名称" min-width="260" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" >
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'warning' : 'success'">
                    {{ row.status === 1 ? '处理中' : '已完成' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="发起时间" :formatter="(row) => formatDate(row.startTime)" />
            </el-table>
          </div>
          <el-empty v-else description="暂无流程" />
      </el-card>
      <!-- 系统导航 -->
      <el-card class="card  system-nav-card ">
        <template #header>
          <div class="card-header">
            <span>系统导航</span>
            <!-- <el-link type="primary" :underline="false">更多</el-link> -->
          </div>
        </template>
        <div class="card-body system-nav-grid-item">
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
          <div
v-for="item in workflowNames" :key="item.name" 
               :class="['workflow-item', { 'valid': item.flowid }]" 
               @click="handleWorkflowClick(item)">
            <div class="workflow-icon">
              <i v-if="item.icon" :class="['fas', item.icon]"></i>
              <div v-else class="workflow-default-icon">{{ item.name.charAt(0) }}</div>
            </div>
            <div class="workflow-name">
              <div class="workflow-name-text">{{ item.name }}</div>
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
// 我的流程
const myProcesses = ref<ProcessInstanceVO[]>([])

// 待办任务
const todoTasks = ref<TaskVO[]>([])
// 第二模块数据
const activeNoticeTab = ref('group')
const noticeList = ref<any[]>([])
const inboxList = ref<any[]>([])
const unreadInboxCount = ref(26)

const systemNavs = ref([
  { name: '系统1', faIcon: 'fa-file-alt', bgColor: '#409EFF', textColor: '#fff' },
  { name: '系统2', faIcon: 'fa-exchange-alt', bgColor: '#67C23A', textColor: '#fff' },
  { name: '系统3', faIcon: 'fa-building', bgColor: '#F56C6C', textColor: '#fff' },
  { name: '系统4', faIcon: 'fa-desktop', bgColor: '#909399', textColor: '#fff' }
])

// 业务流程入口数据
const workflowNames = [
            {name: "用工申请", icon: "fa-user-plus"},
            {name: "员工部门（岗位）调整审批", icon: "fa-exchange-alt"},
            {name: "借用人员审批", icon: "fa-user-friends"},
            {name: "员工转正考核审批", icon: "fa-user-check"},
            {name: "劳动合同期满考核", icon: "fa-file-contract"},
            {name: "劳动合同（聘用协议）期满审批", icon: "fa-file-signature"},
            {name: "录用人员审批表", icon: "fa-user-tie"},
            {name: "员工个人信息表", icon: "fa-id-card"},
            {name: "解除劳动合同通知书审批单", icon: "fa-user-minus"},
            {name: "员工绩效申诉表", icon: "fa-chart-line"},
            {name: "专项考核单", icon: "fa-clipboard-check"},
            {name: "离职审批", icon: "fa-sign-out-alt"},
            {name: "请假申请", icon: "fa-calendar-minus"},
            {name: "出差申请", icon: "fa-plane"},
            {name: "离宁申请", icon: "fa-map-marked-alt"},
            {name: "医疗期复工申请", icon: "fa-procedures"},
            {name: "岗位设置变更审批", icon: "fa-user-cog"},
            {name: "岗位编制调整审批", icon: "fa-users-cog"},
            {name: "岗位任职资格等级评定申请", icon: "fa-user-graduate"},
            {name: "岗位任职资格标准修订审批", icon: "fa-user-edit"},
            {name: "岗位说明书修订审批", icon: "fa-file-edit"},
            {name: "培训需求审批", icon: "fa-chalkboard-teacher"},
            {name: "临时培训需求审批", icon: "fa-chalkboard"},
            {name: "培训计划审批", icon: "fa-calendar-check"},
            {name: "专项奖励审批", icon: "fa-award"},
            {name: "合同查询/复印审批", icon: "fa-search"},
            {name: "合同（协议）审批", icon: "fa-file-signature", flowid: "htgl_hetong"},
            {name: "资料查（借）阅、复印审批", icon: "fa-file-download"},
            {name: "档案销毁申请", icon: "fa-trash-alt"},
            {name: "法人（法人代表）身份证外借登记表", icon: "fa-id-badge"},
            {name: "评标小组成员审批", icon: "fa-gavel"},
            {name: "处理废旧、闲置物资价格审定价方式变更审批", icon: "fa-recycle"},
            {name: "立项审批", icon: "fa-project-diagram"},
            {name: "询比价审批", icon: "fa-search-dollar"},
            {name: "独家谈判审批", icon: "fa-handshake"},
            {name: "招标对外发布审批", icon: "fa-bullhorn", flowid: "zb_wjsp"},
            {name: "低值易耗品采购申请", icon: "fa-shopping-cart"},
            {name: "物资采购申请", icon: "fa-boxes"},
            {name: "设备采购申请", icon: "fa-laptop"},
            {name: "固定资产采购申请", icon: "fa-building"},
            {name: "网络（固话）业务办理申请", icon: "fa-network-wired"},
            {name: "管理标准制（修）订审批", icon: "fa-clipboard-list"},
            {name: "请示汇报", icon: "fa-comment-alt"},
            {name: "电子公告发布审批", icon: "fa-bullhorn"},
            {name: "新闻报道发布审批", icon: "fa-newspaper"},
            {name: "刻章印章申请", icon: "fa-stamp"},
            {name: "印章使用申请", icon: "fa-stamp"},
            {name: "印章外借申请", icon: "fa-stamp"},
            {name: "介绍信用印审批", icon: "fa-envelope-open-text"},
            {name: "资产维修申报", icon: "fa-tools"},
            {name: "资产调拨申请", icon: "fa-exchange-alt"},
            {name: "资产处置申请", icon: "fa-trash-restore"},
            {name: "物资搬运申请", icon: "fa-truck-loading"},
            {name: "资产入库", icon: "fa-warehouse"},
            {name: "资产出库", icon: "fa-dolly"},
            {name: "资产报废审批", icon: "fa-trash"},
            {name: "财务资料查（借）阅审批", icon: "fa-file-invoice-dollar"},
            {name: "资金（经费）审核审批", icon: "fa-money-bill-wave"},
            {name: "借款审批单", icon: "fa-hand-holding-usd"},
            {name: "差旅费报销单", icon: "fa-receipt"},
            {name: "工程款支付申请", icon: "fa-file-invoice"},
            {name: "工程立项审批", icon: "fa-hammer"},
            {name: "合格供应商审查表", icon: "fa-clipboard-check"},
            {name: "合格供应商退库（暂停）审核", icon: "fa-clipboard-list"},
            {name: "供应商审验表", icon: "fa-user-tag"}
        ];
const workflowList = ref<any[]>([])

// 通知详情相关
const noticeDetailVisible = ref(false)
const noticeDetailLoading = ref(false)
const noticeDetail = ref<any>({})

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
    myProcesses.value = myRes.list || []
    console.log('我的流程数据:', myProcesses.value)
  } catch (error) {
    console.error('获取流程和任务数据失败:', error)
  }finally {
    console.log('当前数据状态:', {
      myProcesses: myProcesses.value,
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

const handleCreateProcess = () => {
  // 跳转到流程创建页面
  window.open('/bpm/process-instance/create', '_blank')
}

const handleInboxClick = (item: any) => {
  router.push(`/user/notify-message/${item.id}`)
}

const handleTodoTaskClick = (row: TaskVO) => {
  // 跳转到流程详情页面
  router.push(`/bpm/process-instance/detail?id=${row.processInstanceId}&taskId=${row.id}`)
}

const handleMyProcessClick = (row: ProcessInstanceVO) => {
  // 跳转到流程详情页面
  router.push(`/bpm/process-instance/detail?id=${row.id}`)
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
  padding-right: 0;
  padding-bottom: 100px; /* 为底部栏留出足够空间 */
  box-sizing: border-box;
}
.block-1 {
  display: grid;
  grid-template-columns: 30% 50% 1fr;
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

.cursor-pointer {
  cursor: pointer;
}

.cursor-pointer :deep(tbody tr:hover) {
  background-color: #f5f7fa;
}
</style>
