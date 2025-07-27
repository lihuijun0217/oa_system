<template>
  <div>
    <el-card shadow="never">
      <el-skeleton :loading="loading" animated>
        <el-row :gutter="16" justify="space-between">
          <el-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
            <div class="flex items-center">
              <el-avatar :src="avatar" :size="70" class="mr-16px">
                <img src="@/assets/imgs/avatar.gif" alt="" />
              </el-avatar>
              <div>
                <div class="text-20px">
                  {{ t('workplace.welcome') }} {{ username }} {{ t('workplace.happyDay') }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-skeleton>
    </el-card>
  </div>

  <el-row class="mt-8px" :gutter="8" justify="space-between">
    <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24" class="mb-8px">
      <!-- 系统提醒模块 -->
      <el-card shadow="never" class="mb-8px">
        <template #header>
          <div class="h-3 flex justify-between items-center">
            <div class="flex items-center">
              <el-icon class="mr-2 text-primary" :size="20"><Bell /></el-icon>
              <span class="text-lg font-medium">系统提醒</span>
            </div>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div class="grid grid-cols-4 gap-4">
            <div class="cursor-pointer hover:bg-gray-50 rounded-lg p-4 transition-all" 
                 @click="handleShortcutClick('/bpm/task/todo')">
              <div class="flex items-center">
                <el-icon class="mr-3 text-danger" :size="24"><Document /></el-icon>
                <span class="text-gray-600 mr-2">待办任务</span>
                <el-badge :value="todoTasks.length || 0" :max="99" class="notify-badge">
                  <template #content>
                    <span class="text-lg">{{ todoTasks.length || 0 }}</span>
                  </template>
                </el-badge>
              </div>
            </div>
            <div class="cursor-pointer hover:bg-gray-50 rounded-lg p-4 transition-all" 
                 @click="handleShortcutClick('/bpm/task/done')">
              <div class="flex items-center">
                <el-icon class="mr-3 text-success" :size="24"><Tickets /></el-icon>
                <span class="text-gray-600 mr-2">已办任务</span>
                <el-badge :value="doneTasks.length || 0" :max="99" type="success" class="notify-badge">
                  <template #content>
                    <span class="text-lg">{{ doneTasks.length || 0 }}</span>
                  </template>
                </el-badge>
              </div>
            </div>
            <div class="cursor-pointer hover:bg-gray-50 rounded-lg p-4 transition-all" 
                 @click="handleShortcutClick('/bpm/task/my')">
              <div class="flex items-center">
                <el-icon class="mr-3 text-danger" :size="24"><Timer /></el-icon>
                <span class="text-gray-600 mr-2">我的流程</span>
                <el-badge :value="myProcesses.length || 0" :max="99" class="notify-badge">
                  <template #content>
                    <span class="text-lg">{{ myProcesses.length || 0 }}</span>
                  </template>
                </el-badge>
              </div>
            </div>
          </div>
        </el-skeleton>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="h-3 flex justify-between items-center">
            <div class="flex items-center">
              <el-icon class="mr-2 text-primary" :size="20"><Menu /></el-icon>
              <span class="text-lg font-medium">快捷入口</span>
            </div>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <el-row :gutter="16">
            <el-col v-for="item in shortcut" :key="item.name" :span="6" class="mb-16px">
              <div class="flex items-center justify-center cursor-pointer" @click="handleShortcutClick(item.url)">
                <el-card shadow="hover" class="w-90% text-center py-16px">
                  <Icon :icon="item.icon" :size="30" class="mb-8px" :style="{ color: item.color }" />
                  <div>{{ item.name }}</div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-skeleton>
      </el-card>
      
      <el-card shadow="never" class="mt-8px">
        <template #header>
          <div class="h-3 flex justify-between items-center">
            <div class="flex items-center">
              <el-icon class="mr-2 text-warning" :size="20"><Bell /></el-icon>
              <span class="text-lg font-medium">通知公告</span>
            </div>
            <el-link type="primary" :underline="false" @click="handleViewMore">
              查看更多
            </el-link>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-if="notice && notice.length > 0">
            <div v-for="item in notice" :key="item.id" class="mb-16px">
              <div class="flex items-center">
                <el-tag :type="item.type === 1 ? 'warning' : 'success'" class="mr-8px">
                  {{ item.type === 1 ? '通知' : '公告' }}
                </el-tag>
                <el-link :underline="false" @click="showNoticeDetail(item.id)">{{ item.title }}</el-link>
              </div>
              <div class="mt-8px text-gray-400 text-sm">
                {{ formatDate(item.createTime) }}
              </div>
              <el-divider v-if="notice.indexOf(item) !== notice.length - 1" />
            </div>
          </div>
          <el-empty v-else description="暂无通知公告" />
        </el-skeleton>
      </el-card>

      <el-card shadow="never" class="mt-8px">
        <template #header>
          <div class="h-3 flex justify-between items-center">
            <div class="flex items-center">
              <el-icon class="mr-2 text-success" :size="20"><Tickets /></el-icon>
              <span class="text-lg font-medium">我的流程（审批中）</span>
            </div>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/my')">
              查看更多
            </el-link>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-if="myProcesses && myProcesses.length > 0">
            <el-table :data="myProcesses" stripe>
              <el-table-column prop="name" label="流程名称" show-overflow-tooltip />
              <el-table-column prop="status" label="状态">
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'warning' : 'success'">
                    {{ row.status === 1 ? '处理中' : '已完成' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="发起时间" width="180" :formatter="(row) => formatDate(row.startTime)" />
            </el-table>
          </div>
          <el-empty v-else description="暂无流程" />
        </el-skeleton>
      </el-card>

      <el-card shadow="never" class="mt-8px">
        <template #header>
          <div class="h-3 flex justify-between items-center">
            <div class="flex items-center">
              <el-icon class="mr-2 text-danger" :size="20"><Document /></el-icon>
              <span class="text-lg font-medium">待办任务</span>
            </div>
            <el-link type="primary" :underline="false" @click="handleShortcutClick('/bpm/task/todo')">
              查看更多
            </el-link>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-if="todoTasks && todoTasks.length > 0">
            <el-table :data="todoTasks" stripe>
              <el-table-column prop="processInstance.name" label="流程" show-overflow-tooltip />
              <el-table-column prop="name" label="任务名称" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="180" :formatter="(row) => formatDate(row.createTime)" />
            </el-table>
          </div>
          <el-empty v-else description="暂无待办任务" />
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>

  <el-dialog v-model="noticeDetailVisible" :title="selectedNotice?.title" width="600px">
    <div v-loading="noticeDetailLoading">
      <div v-if="selectedNotice">
        <div class="mb-16px">
          <el-tag :type="selectedNotice.type === 1 ? 'warning' : 'success'" class="mr-8px">
            {{ selectedNotice.type === 1 ? '通知' : '公告' }}
          </el-tag>
          <span class="text-gray-400 text-sm">{{ formatDate(selectedNotice.createTime) }}</span>
        </div>
        <div v-html="selectedNotice.content"></div>
      </div>
    </div>
  </el-dialog>

  <!-- 通知公告列表弹窗 -->
  <el-dialog v-model="noticeListVisible" title="通知公告" width="800">
    <el-table v-loading="noticeListLoading" :data="noticeList" stripe>
      <el-table-column prop="title" label="标题" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'warning' : 'success'">
            {{ row.type === 1 ? '通知' : '公告' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'info'">
            {{ row.status === 0 ? '正常' : '关闭' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" :formatter="(row) => formatDate(row.createTime)" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showNoticeDetail(row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-10px">
      <el-pagination
        v-model:current-page="queryParams.pageNo"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        size="small"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="getNoticeList"
        @current-change="getNoticeList"
      />
    </div>
  </el-dialog>
</template>
<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue'
import { set } from 'lodash-es'
import { EChartsOption } from 'echarts'
import { formatDate } from '@/utils/formatTime'
import { useI18n } from '@/hooks/web/useI18n'
import { useUserStore } from '@/store/modules/user'
import { useRouter } from 'vue-router'
import * as NoticeApi from '@/api/system/notice'
import * as ProcessApi from '@/api/bpm/processInstance'
import * as TaskApi from '@/api/bpm/task'
import type { NoticeVO } from '@/api/system/notice'
import type { ProcessInstanceVO } from '@/api/bpm/processInstance'
import type { TaskVO } from '@/api/bpm/task'
import { pieOptions, barOptions } from './echarts-data'
import { Menu, Bell, Tickets, Document, Timer, Files } from '@element-plus/icons-vue'

defineOptions({ name: 'Index' })

const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const avatar = userStore.getUser.avatar
const username = userStore.getUser.nickname
const pieOptionsData = reactive<EChartsOption>(pieOptions) as EChartsOption

// 获取统计数
let totalSate = reactive<WorkplaceTotal>({
  project: 0,
  access: 0,
  todo: 0
})

const getCount = async () => {
  const data = {
    project: 40,
    access: 2340,
    todo: 10
  }
  totalSate = Object.assign(totalSate, data)
}

// 获取项目数
let projects = reactive<Project[]>([])
const getProject = async () => {
  const data = [
    {
      name: 'ruoyi-vue-pro',
      icon: 'simple-icons:springboot',
      message: 'github.com/YunaiV/ruoyi-vue-pro',
      personal: 'Spring Boot 单体架构',
      time: new Date('2025-01-02'),
      color: '#6DB33F'
    },
    {
      name: 'yudao-ui-admin-vue3',
      icon: 'ep:element-plus',
      message: 'github.com/yudaocode/yudao-ui-admin-vue3',
      personal: 'Vue3 + element-plus 管理后台',
      time: new Date('2025-02-03'),
      color: '#409EFF'
    },
    {
      name: 'yudao-ui-mall-uniapp',
      icon: 'icon-park-outline:mall-bag',
      message: 'github.com/yudaocode/yudao-ui-mall-uniapp',
      personal: 'Vue3 + uniapp 商城手机端',
      time: new Date('2025-03-04'),
      color: '#ff4d4f'
    },
    {
      name: 'yudao-cloud',
      icon: 'material-symbols:cloud-outline',
      message: 'github.com/YunaiV/yudao-cloud',
      personal: 'Spring Cloud 微服务架构',
      time: new Date('2025-04-05'),
      color: '#1890ff'
    },
    {
      name: 'yudao-ui-admin-vben',
      icon: 'devicon:antdesign',
      message: 'github.com/yudaocode/yudao-ui-admin-vben',
      personal: 'Vue3 + vben5(antd) 管理后台',
      time: new Date('2025-05-06'),
      color: '#e18525'
    },
    {
      name: 'yudao-ui-admin-uniapp',
      icon: 'ant-design:mobile',
      message: 'github.com/yudaocode/yudao-ui-admin-uniapp',
      personal: 'Vue3 + uniapp 管理手机端',
      time: new Date('2025-06-01'),
      color: '#2979ff'
    }
  ]
  projects = Object.assign(projects, data)
}

// 通知公告相关
const notice = ref<NoticeVO[]>([])
const noticeDetailVisible = ref(false)
const selectedNotice = ref<NoticeVO>()
const noticeDetailLoading = ref(false)

// 我的流程
const myProcesses = ref<ProcessInstanceVO[]>([])

// 待办任务
const todoTasks = ref<TaskVO[]>([])

// 已办任务
const doneTasks = ref<TaskVO[]>([])

// 通知公告列表相关
const noticeListVisible = ref(false)
const noticeListLoading = ref(false)
const noticeList = ref<NoticeVO[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  status: 0 // 已发布的通知
})

// 获取通知公告
const getNotices = async () => {
  console.log('开始获取通知公告')
  try {
    const res = await NoticeApi.getNoticePage({
      pageNo: 1,
      pageSize: 5,
      status: 0 // 已发布的通知
    })
    console.log('通知公告API响应:', res)
    if (res.list) {
      notice.value = res.list
      console.log('通知公告数据已更新:', notice.value)
    } else {
      console.warn('通知公告API返回异常:', res)
      notice.value = []
    }
  } catch (error) {
    console.error('获取通知公告失败:', error)
    notice.value = []
  }
}

// 获取快捷入口
const shortcut = ref([
  {
    name: '发起流程',
    icon: 'ep:plus',
    url: '/bpm/task/create',
    color: '#1fdaca'
  },
  {
    name: '我的流程',
    icon: 'ep:tickets',
    url: '/bpm/task/my',
    color: '#7c3aed'
  },
  {
    name: '待办任务',
    icon: 'ep:document',
    url: '/bpm/task/todo',
    color: '#ff6b6b'
  },
  {
    name: '已办流程',
    icon: 'ep:document-checked',
    url: '/bpm/task/done',
    color: '#3fb27f'
  }
])

// 用户来源
const getUserAccessSource = async () => {
  const data = [
    { value: 335, name: 'analysis.directAccess' },
    { value: 310, name: 'analysis.mailMarketing' },
    { value: 234, name: 'analysis.allianceAdvertising' },
    { value: 135, name: 'analysis.videoAdvertising' },
    { value: 1548, name: 'analysis.searchEngines' }
  ]
  set(
    pieOptionsData,
    'legend.data',
    data.map((v) => t(v.name))
  )
  pieOptionsData!.series![0].data = data.map((v) => {
    return {
      name: t(v.name),
      value: v.value
    }
  })
}
const barOptionsData = reactive<EChartsOption>(barOptions) as EChartsOption

// 周活跃量
const getWeeklyUserActivity = async () => {
  const data = [
    { value: 13253, name: 'analysis.monday' },
    { value: 34235, name: 'analysis.tuesday' },
    { value: 26321, name: 'analysis.wednesday' },
    { value: 12340, name: 'analysis.thursday' },
    { value: 24643, name: 'analysis.friday' },
    { value: 1322, name: 'analysis.saturday' },
    { value: 1324, name: 'analysis.sunday' }
  ]
  set(
    barOptionsData,
    'xAxis.data',
    data.map((v) => t(v.name))
  )
  set(barOptionsData, 'series', [
    {
      name: t('analysis.activeQuantity'),
      data: data.map((v) => v.value),
      type: 'bar'
    }
  ])
}

// 获取我的流程
const getMyProcesses = async () => {
  console.log('开始获取我的流程')
  try {
    const res = await ProcessApi.getProcessInstanceMyPage({
      pageNo: 1,
      pageSize: 5,
      status: 1 // 审批中的流程
    })
    console.log('我的流程API响应:', res)
    if (res.list) {
      myProcesses.value = res.list
      console.log('我的流程数据已更新:', myProcesses.value)
    } else {
      console.warn('我的流程API返回异常:', res)
      myProcesses.value = []
    }
  } catch (error) {
    console.error('获取我的流程失败:', error)
    myProcesses.value = []
  }
}

// 获取待办任务
const getTodoTasks = async () => {
  console.log('开始获取待办任务')
  try {
    const res = await TaskApi.getTaskTodoPage({
      pageNo: 1,
      pageSize: 5
    })
    console.log('待办任务API响应:', res)
    if (res.list) {
      todoTasks.value = res.list
      console.log('待办任务数据已更新:', todoTasks.value)
    } else {
      console.warn('待办任务API返回异常:', res)
      todoTasks.value = []
    }
  } catch (error) {
    console.error('获取待办任务失败:', error)
    todoTasks.value = []
  }
}

// 获取已办任务
const getDoneTasks = async () => {
  console.log('开始获取已办任务')
  try {
    const res = await TaskApi.getTaskDonePage({
      pageNo: 1,
      pageSize: 10
    })
    console.log('已办任务API响应:', res)
    if (res.list) {
      doneTasks.value = res.list
      console.log('已办任务数据已更新:', doneTasks.value)
    } else {
      console.warn('已办任务API返回异常:', res)
      doneTasks.value = []
    }
  } catch (error) {
    console.error('获取已办任务失败:', error)
    doneTasks.value = []
  }
}

// 显示通知详情
const showNoticeDetail = async (id: number) => {
  noticeDetailVisible.value = true
  noticeDetailLoading.value = true
  try {
    const res = await NoticeApi.getNotice(id)
    console.log('通知详情API响应:', res)
    if (res) {
      selectedNotice.value = {
        ...res,
        content: res.content || ''
      }
    }
  } catch (error) {
    console.error('获取通知详情失败:', error)
  } finally {
    noticeDetailLoading.value = false
  }
}

// 获取所有数据
const getAllApi = async () => {
  console.log('开始初始化数据')
  loading.value = true
  try {
    const results = await Promise.allSettled([
      getNotices(),
      getMyProcesses(),
      getTodoTasks(),
      getDoneTasks()
    ])
    console.log('API调用结果:', results)
    results.forEach((result, index) => {
      if (result.status === 'rejected') {
        console.error(`第${index + 1}个API调用失败:`, result.reason)
      }
    })
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
    console.log('当前数据状态:', {
      notice: notice.value,
      myProcesses: myProcesses.value,
      todoTasks: todoTasks.value,
      doneTasks: doneTasks.value,
      loading: loading.value
    })
  }
}

const handleProjectClick = (message: string) => {
  window.open(`https://${message}`, '_blank')
}

const handleShortcutClick = (url: string) => {
  router.push(url)
}

// 获取通知公告列表
const getNoticeList = async () => {
  noticeListLoading.value = true
  try {
    const res = await NoticeApi.getNoticePage(queryParams)
    if (res.list) {
      noticeList.value = res.list
      total.value = res.total
    }
  } catch (error) {
    console.error('获取通知列表失败:', error)
  } finally {
    noticeListLoading.value = false
  }
}

// 修改查看更多的点击事件
const handleViewMore = () => {
  noticeListVisible.value = true
  getNoticeList()
}

// 初始化
onMounted(() => {
  console.log('组件挂载，开始获取数据')
  getAllApi()
})
</script>

<style scoped>
.text-primary {
  color: var(--el-color-primary);
}
.text-warning {
  color: var(--el-color-warning);
}
.text-success {
  color: var(--el-color-success);
}
.text-danger {
  color: var(--el-color-danger);
}

.notify-badge :deep(.el-badge__content) {
  height: 24px;
  padding: 0 8px;
  line-height: 24px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 14px;
}
</style>
