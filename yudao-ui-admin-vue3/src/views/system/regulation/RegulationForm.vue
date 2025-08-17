<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入规章制度标题" />
      </el-form-item>
      <el-form-item label="文件" prop="fileUrl">
        <UploadFile
          v-model="formData.fileUrl"
          :file-type="['pdf', 'doc', 'docx', 'xls', 'xlsx']"
          :file-size="10"
          :limit="1"
          directory="regulation"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="0">草稿</el-radio>
          <el-radio :label="1">发布</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as RegulationApi from '@/api/system/regulation'
import { UploadFile } from '@/components/UploadFile'

defineOptions({ name: 'RegulationForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: '',
  fileName: '',
  fileUrl: '',
  fileSize: undefined,
  status: 0
})
const formRules = reactive({
  title: [{ required: true, message: '规章制度标题不能为空', trigger: 'blur' }],
  fileUrl: [{ required: true, message: '请选择文件', trigger: 'change' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref



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
      const data = await RegulationApi.getRegulation(id)
      formData.value = data

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
    const data = formData.value as unknown as RegulationApi.RegulationSaveReqVO
    if (formType.value === 'create') {
      await RegulationApi.createRegulation(data)
      message.success(t('common.createSuccess'))
    } else {
      await RegulationApi.updateRegulation(data)
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
    title: '',
    fileName: '',
    fileUrl: '',
    fileSize: undefined,
    status: 0
  }
  formRef.value?.resetFields()
}


</script> 