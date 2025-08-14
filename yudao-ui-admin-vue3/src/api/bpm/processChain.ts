import request from '@/config/axios'

export interface ProcessChainVO {
  id: number
  name: string
  sourceProcessKey: string
  targetProcessKey: string
  fieldMapping: FieldMappingVO[]
  enabled: boolean
  sort: number
  remark: string
  createTime: Date
  sourceProcessName: string
  targetProcessName: string
}

export interface FieldMappingVO {
  sourceField: string
  targetField: string
  transformRule: string
  defaultValue: string
  expression: string
  sourceFieldLabel: string
  targetFieldLabel: string
}

export interface ProcessChainPageReqVO extends PageParam {
  name?: string
  sourceProcessKey?: string
  targetProcessKey?: string
  enabled?: boolean
  createTime?: Date[]
}

export interface ProcessChainExportReqVO {
  name?: string
  sourceProcessKey?: string
  targetProcessKey?: string
  enabled?: boolean
  createTime?: Date[]
}

// 查询串联流程关系列表
export const getProcessChainPageApi = (params: ProcessChainPageReqVO) => {
  return request.get({ url: '/bpm/process-chain/page', params })
}

// 查询串联流程关系详情
export const getProcessChainApi = (id: number) => {
  return request.get({ url: '/bpm/process-chain/get', params: { id } })
}

// 新增串联流程关系
export const createProcessChainApi = (data: ProcessChainVO) => {
  return request.post({ url: '/bpm/process-chain/create', data })
}

// 修改串联流程关系
export const updateProcessChainApi = (data: ProcessChainVO) => {
  return request.put({ url: '/bpm/process-chain/update', data })
}

// 删除串联流程关系
export const deleteProcessChainApi = (id: number) => {
  return request.delete({ url: '/bpm/process-chain/delete', params: { id } })
}

// 根据源流程标识查询可发起的后续流程
export const getProcessChainsBySourceKeyApi = (sourceProcessKey: string) => {
  return request.get({ url: '/bpm/process-chain/get-by-source', params: { sourceProcessKey } })
}

// 根据目标流程标识查询前置流程
export const getProcessChainsByTargetKeyApi = (targetProcessKey: string) => {
  return request.get({ url: '/bpm/process-chain/get-by-target', params: { targetProcessKey } })
}

// 生成目标流程变量
export const generateTargetProcessVariablesApi = (params: {
  sourceProcessKey: string
  targetProcessKey: string
  sourceVariables: any
}) => {
  return request.post({ url: '/bpm/process-chain/generate-variables', data: params })
} 