import { ProcessInstanceVO } from '@/api/bpm/processInstance'

export interface TaskVO {
  id: string
  name: string
  processInstanceId: string
  processInstance?: ProcessInstanceVO
  createTime: string
  status: number
  // 其他可能的字段
}