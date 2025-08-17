import request from '@/config/axios'

export interface RegulationVO {
  id: number
  title: string
  fileName: string
  fileUrl: string
  fileSize?: number
  status: number
  publishTime?: string
  createTime: string
}

export interface RegulationPageReqVO extends PageParam {
  title?: string
  status?: number
  createTime?: string[]
}

export interface RegulationSaveReqVO {
  id?: number
  title: string
  fileName: string
  fileUrl: string
  fileSize?: number
  status: number
  publishTime?: string
}

// 查询规章制度列表
export const getRegulationPage = async (params: RegulationPageReqVO) => {
  return await request.get({ url: '/system/regulation/page', params })
}

// 查询规章制度详情
export const getRegulation = async (id: number) => {
  return await request.get({ url: `/system/regulation/get?id=${id}` })
}

// 新增规章制度
export const createRegulation = async (data: RegulationSaveReqVO) => {
  return await request.post({ url: '/system/regulation/create', data })
}

// 修改规章制度
export const updateRegulation = async (data: RegulationSaveReqVO) => {
  return await request.put({ url: '/system/regulation/update', data })
}

// 删除规章制度
export const deleteRegulation = async (id: number) => {
  return await request.delete({ url: `/system/regulation/delete?id=${id}` })
}

// 发布规章制度
export const publishRegulation = async (id: number) => {
  return await request.post({ url: `/system/regulation/publish?id=${id}` })
}

// 下架规章制度
export const unpublishRegulation = async (id: number) => {
  return await request.post({ url: `/system/regulation/unpublish?id=${id}` })
}

// 下载规章制度文件
export const downloadRegulation = async (id: number) => {
  return await request.download({ url: `/system/regulation/download?id=${id}` })
} 