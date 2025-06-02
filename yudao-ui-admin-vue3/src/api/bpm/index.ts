import request from '@/config/axios'

export interface NoticeVO {
  id: number
  title: string
  type: number
  content: string
  status: number
  createTime: number
  remark?: string
}

export interface ProcessInstanceVO {
  id: string
  name: string
  processDefinitionId: string
  category: string
  status: number
  result: number
  createTime: string
  endTime: string
}

export interface TaskVO {
  id: string
  name: string
  processInstanceId: string
  status: number
  createTime: string
}

// 获取通知公告列表
export const getNoticeAPI = async (params: any) => {
  return await request.get({ 
    url: '/system/notice/page',
    params
  })
}

// 获取通知公告详情
export const getNoticeDetail = async (id: number) => {
  return await request.get({
    url: '/system/notice/get?id=' + id
  })
}

// 获取我的流程列表
export const getMyProcessAPI = async (params: any) => {
  return await request.get({
    url: '/bpm/process-instance/my-page',
    params
  })
}

// 获取待办任务列表
export const getTodoTaskAPI = async (params: any) => {
  return await request.get({
    url: '/bpm/task/todo-page',
    params
  })
} 