<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="串联流程名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入串联流程名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="源流程标识" prop="sourceProcessKey">
            <el-select
              v-model="formData.sourceProcessKey"
              placeholder="请选择源流程"
              clearable
              filterable
              class="w-full"
            >
              <el-option
                v-for="item in processDefinitionList"
                :key="item.key"
                :label="`${item.name} (${item.key})`"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="目标流程标识" prop="targetProcessKey">
            <el-select
              v-model="formData.targetProcessKey"
              placeholder="请选择目标流程"
              clearable
              filterable
              class="w-full"
            >
              <el-option
                v-for="item in processDefinitionList"
                :key="item.key"
                :label="`${item.name} (${item.key})`"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="enabled">
            <el-radio-group v-model="formData.enabled">
              <el-radio :label="true">启用</el-radio>
              <el-radio :label="false">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" :max="999" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="formData.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 字段映射配置 -->
      <el-form-item label="字段映射配置">
        <div class="field-mapping-container">
          <div class="field-mapping-header">
            <span>源字段 → 目标字段</span>
            <div class="header-buttons">
              <el-button type="success" size="small" @click="addWildcardMapping">
                <Icon icon="ep:magic-stick" class="mr-5px" />通配符映射
              </el-button>
              <el-button type="primary" size="small" @click="addFieldMapping">
                <Icon icon="ep:plus" class="mr-5px" />添加字段映射
              </el-button>
            </div>
          </div>
          
          <!-- 表达式说明 -->
          <div class="expression-help">
            <el-alert
              title="映射规则说明"
              type="info"
              :closable="false"
              show-icon
            >
              <template #default>
                <div class="text-sm">
                  <p><strong>直接复制：</strong>将源字段值直接复制到目标字段</p>
                  <p><strong>使用默认值：</strong>当源字段为空时，使用指定的默认值</p>
                  <p><strong>表达式计算：</strong>支持SpEL表达式，如：</p>
                  <ul class="ml-4 mt-2">
                    <li><code>#{sourceField}</code> - 引用源字段值</li>
                    <li><code>#{sourceField + '_suffix'}</code> - 字符串拼接</li>
                    <li><code>#{sourceField ?: 'default'}</code> - 空值判断</li>
                    <li><code>#{T(java.time.LocalDateTime).now()}</code> - 当前时间</li>
                    <li><code>#{sourceField * 1.1}</code> - 数值计算</li>
                  </ul>
                  <p class="mt-2"><strong>通配符映射：</strong>支持 * 通配符，如：</p>
                  <ul class="ml-4 mt-2">
                    <li><code>lb_*</code> → <code>lb_*</code> - 批量映射所有以lb_开头的字段</li>
                    <li><code>*_name</code> → <code>*_name</code> - 批量映射所有以_name结尾的字段</li>
                    <li><code>user_*_info</code> → <code>user_*_info</code> - 批量映射中间包含*的字段</li>
                  </ul>
                </div>
              </template>
            </el-alert>
          </div>
          
          <div
            v-for="(mapping, index) in formData.fieldMapping"
            :key="index"
            class="field-mapping-item"
          >
            <el-row :gutter="10">
              <el-col :span="6">
                <el-input
                  v-model="mapping.sourceField"
                  placeholder="源字段名 (支持*通配符)"
                  size="small"
                />
              </el-col>
              <el-col :span="6">
                <el-input
                  v-model="mapping.targetField"
                  placeholder="目标字段名 (支持*通配符)"
                  size="small"
                />
              </el-col>
              <el-col :span="4">
                <el-select v-model="mapping.transformRule" placeholder="转换规则" size="small">
                  <el-option label="直接复制" value="direct" />
                  <el-option label="使用默认值" value="default" />
                  <el-option label="表达式计算" value="expression" />
                  <el-option label="通配符映射" value="wildcard" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-input
                  v-model="mapping.defaultValue"
                  placeholder="默认值"
                  size="small"
                  v-if="mapping.transformRule === 'default'"
                />
                <el-input
                  v-model="mapping.expression"
                  placeholder="表达式"
                  size="small"
                  v-if="mapping.transformRule === 'expression'"
                />
                <el-tag v-if="mapping.transformRule === 'wildcard'" type="success" size="small">
                  通配符映射
                </el-tag>
              </el-col>
              <el-col :span="4">
                <el-button
                  type="danger"
                  size="small"
                  @click="removeFieldMapping(index)"
                >
                  删除
                </el-button>
              </el-col>
            </el-row>
            <!-- 通配符映射预览 -->
            <div v-if="mapping.transformRule === 'wildcard' && mapping.sourceField && mapping.targetField" class="wildcard-preview">
              <el-text type="info" size="small">
                预览：{{ getWildcardPreview(mapping.sourceField, mapping.targetField) }}
              </el-text>
            </div>
          </div>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as ProcessChainApi from '@/api/bpm/processChain'
import * as DefinitionApi from '@/api/bpm/definition'

defineOptions({ name: 'ProcessChainForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  sourceProcessKey: '',
  targetProcessKey: '',
  fieldMapping: [],
  enabled: true,
  sort: 0,
  remark: ''
})
const formRules = reactive({
  name: [{ required: true, message: '串联流程名称不能为空', trigger: 'blur' }],
  sourceProcessKey: [{ required: true, message: '源流程标识不能为空', trigger: 'change' }],
  targetProcessKey: [{ required: true, message: '目标流程标识不能为空', trigger: 'change' }],
  enabled: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

const processDefinitionList = ref<any[]>([])

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProcessChainApi.getProcessChainApi(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ProcessChainApi.ProcessChainVO
    if (formType.value === 'create') {
      await ProcessChainApi.createProcessChainApi(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProcessChainApi.updateProcessChainApi(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    sourceProcessKey: '',
    targetProcessKey: '',
    fieldMapping: [],
    enabled: true,
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}

/** 添加字段映射 */
const addFieldMapping = () => {
  formData.value.fieldMapping.push({
    sourceField: '',
    targetField: '',
    transformRule: 'direct',
    defaultValue: '',
    expression: ''
  })
}

/** 添加通配符映射 */
const addWildcardMapping = () => {
  formData.value.fieldMapping.push({
    sourceField: '',
    targetField: '',
    transformRule: 'wildcard',
    defaultValue: '',
    expression: ''
  })
}

/** 删除字段映射 */
const removeFieldMapping = (index: number) => {
  formData.value.fieldMapping.splice(index, 1)
}

/** 获取流程定义列表 */
const getProcessDefinitionList = async () => {
  try {
    processDefinitionList.value = await DefinitionApi.getSimpleProcessDefinitionList()
  } catch (error) {
    console.error('获取流程定义列表失败:', error)
  }
}

/** 获取通配符预览 */
const getWildcardPreview = (source: string, target: string) => {
  if (!source || !target) return '';
  if (source.includes('*') || target.includes('*')) {
    return `源: ${source} -> 目标: ${target}`;
  }
  return `源: ${source} -> 目标: ${target}`;
};

onMounted(() => {
  getProcessDefinitionList()
})
</script>

<style scoped>
.field-mapping-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 16px;
}

.field-mapping-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-weight: bold;
}

.header-buttons {
  display: flex;
  gap: 8px;
}

.expression-help {
  margin-bottom: 16px;
}

.field-mapping-item {
  margin-bottom: 16px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.field-mapping-item:last-child {
  margin-bottom: 0;
}

.wildcard-preview {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 4px;
  border-left: 4px solid #0ea5e9;
}

.text-sm {
  font-size: 12px;
}

.ml-4 {
  margin-left: 16px;
}

.mt-2 {
  margin-top: 8px;
}

code {
  background-color: #f0f0f0;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

/* 通配符映射的特殊样式 */
.field-mapping-item:has(.wildcard-preview) {
  border-left-color: #10b981;
  background-color: #f0fdf4;
}

/* 通配符按钮样式 */
.el-button--success.is-plain {
  border-color: #10b981;
  color: #10b981;
}

.el-button--success.is-plain:hover {
  background-color: #10b981;
  border-color: #10b981;
  color: white;
}
</style> 