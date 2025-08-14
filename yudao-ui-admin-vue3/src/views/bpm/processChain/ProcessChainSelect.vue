<template>
  <ContentWrap>
    <div class="process-chain-select">
      <el-card class="source-process-info" shadow="never">
        <template #header>
          <div class="card-header">
            <Icon icon="ep:info-filled" class="mr-5px" />
            <span>源流程信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="流程实例ID">{{ sourceProcessInstanceId }}</el-descriptions-item>
          <el-descriptions-item label="源流程标识">{{ sourceProcessKey }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="available-processes" shadow="never">
        <template #header>
          <div class="card-header">
            <Icon icon="ep:list" class="mr-5px" />
            <span>可发起的后续流程</span>
          </div>
        </template>
        
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        
        <div v-else-if="processChains.length === 0" class="empty-container">
          <el-empty description="暂无可发起的后续流程" />
        </div>
        
        <div v-else class="process-list">
          <el-card
            v-for="chain in processChains"
            :key="chain.id"
            class="process-item"
            shadow="hover"
            @click="handleSelectProcess(chain)"
          >
            <div class="process-info">
              <div class="process-header">
                <h3>{{ chain.name }}</h3>
                <el-tag type="success" size="small">可发起</el-tag>
              </div>
              <div class="process-details">
                <p><strong>源流程：</strong>{{ chain.sourceProcessName }}</p>
                <p><strong>目标流程：</strong>{{ chain.targetProcessName }}</p>
                <p><strong>字段映射：</strong></p>
                <div class="field-mappings">
                  <el-tag
                    v-for="(mapping, index) in chain.fieldMapping"
                    :key="index"
                    size="small"
                    class="mr-5px mb-5px"
                    :type="getMappingType(mapping.transformRule)"
                  >
                    {{ mapping.sourceField }} → {{ mapping.targetField }}
                    <span v-if="mapping.transformRule === 'wildcard'" class="wildcard-badge">*</span>
                  </el-tag>
                </div>
              </div>
            </div>
            <div class="process-actions">
              <el-button type="primary" @click.stop="handleStartProcess(chain)">
                发起流程
              </el-button>
            </div>
          </el-card>
        </div>
      </el-card>
    </div>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as ProcessChainApi from '@/api/bpm/processChain'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'

defineOptions({ name: 'BpmProcessChainSelect' })

const route = useRoute()
const router = useRouter()
const message = useMessage()

const loading = ref(true)
const processChains = ref<ProcessChainApi.ProcessChainVO[]>([])

// 从路由参数获取源流程信息
const sourceProcessInstanceId = route.query.sourceProcessInstanceId as string
const sourceProcessKey = route.query.sourceProcessKey as string

/** 获取可发起的后续流程 */
const getAvailableProcesses = async () => {
  if (!sourceProcessKey) {
    message.error('源流程标识不能为空')
    return
  }
  
  loading.value = true
  try {
    const chains = await ProcessChainApi.getProcessChainsBySourceKeyApi(sourceProcessKey)
    processChains.value = chains.filter(chain => chain.enabled)
  } catch (error) {
    console.error('获取后续流程失败:', error)
    message.error('获取后续流程失败')
  } finally {
    loading.value = false
  }
}

/** 选择流程 */
const handleSelectProcess = (chain: ProcessChainApi.ProcessChainVO) => {
  console.log('选择流程:', chain)
}

/** 发起流程 */
const handleStartProcess = async (chain: ProcessChainApi.ProcessChainVO) => {
  try {
    console.log('准备发起后续流程:', {
      chain,
      sourceProcessInstanceId,
      sourceProcessKey,
      targetProcessDefinitionKey: chain.targetProcessKey
    })
    
    // 跳转到流程发起页面，传递源流程实例ID和目标流程定义key
    // 这样可以在流程发起页面中复用前置流程的表单数据
    await router.push({
      name: 'BpmProcessInstanceCreate',
      query: { 
        sourceProcessInstanceId: sourceProcessInstanceId,
        processDefinitionKey: chain.targetProcessKey
      }
    })
  } catch (error) {
    console.error('跳转失败:', error)
    message.error('跳转失败: ' + (error.message || error))
  }
}

/** 获取映射类型对应的标签类型 */
const getMappingType = (transformRule: string) => {
  switch (transformRule) {
    case 'direct': return 'primary'
    case 'default': return 'warning'
    case 'expression': return 'info'
    case 'wildcard': return 'success'
    default: return 'default'
  }
}

onMounted(() => {
  getAvailableProcesses()
})
</script>

<style scoped>
.process-chain-select {
  max-width: 1200px;
  margin: 0 auto;
}

.source-process-info {
  margin-bottom: 20px;
}

.available-processes {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: bold;
}

.loading-container {
  padding: 20px;
}

.empty-container {
  padding: 40px;
  text-align: center;
}

.process-list {
  display: grid;
  gap: 16px;
}

.process-item {
  cursor: pointer;
  transition: all 0.3s ease;
}

.process-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.process-info {
  flex: 1;
}

.process-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.process-header h3 {
  margin: 0;
  color: #303133;
}

.process-details p {
  margin: 8px 0;
  color: #606266;
}

.field-mappings {
  margin-top: 8px;
}

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

.process-actions {
  margin-top: 16px;
  text-align: right;
}
</style> 