import request from '@/config/axios'

export interface NewsVO {
  id: number
  title: string
  content: string
  type: number
  imageUrl?: string
  status: number
  publishTime?: string
  createTime: string
}

export interface NewsPageReqVO extends PageParam {
  title?: string
  type?: number
  status?: number
  createTime?: string[]
}

export interface NewsSaveReqVO {
  id?: number
  title: string
  content: string
  type: number
  imageUrl?: string
  status: number
  publishTime?: string
}

// 查询新闻列表
export const getNewsPage = async (params: NewsPageReqVO) => {
  return await request.get({ url: '/system/news/page', params })
}

// 查询新闻详情
export const getNews = async (id: number) => {
  return await request.get({ url: `/system/news/get?id=${id}` })
}

// 新增新闻
export const createNews = async (data: NewsSaveReqVO) => {
  return await request.post({ url: '/system/news/create', data })
}

// 修改新闻
export const updateNews = async (data: NewsSaveReqVO) => {
  return await request.put({ url: '/system/news/update', data })
}

// 删除新闻
export const deleteNews = async (id: number) => {
  return await request.delete({ url: `/system/news/delete?id=${id}` })
}

// 发布新闻
export const publishNews = async (id: number) => {
  return await request.post({ url: `/system/news/publish?id=${id}` })
}

// 下架新闻
export const unpublishNews = async (id: number) => {
  return await request.post({ url: `/system/news/unpublish?id=${id}` })
} 