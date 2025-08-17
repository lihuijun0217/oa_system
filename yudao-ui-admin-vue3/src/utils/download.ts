import request from '@/config/axios'

/**
 * 通用文件下载工具函数
 * @param fileUrl 文件URL
 * @param fileName 文件名
 * @param target 目标窗口（默认为_blank）
 */
export const downloadFile = async (fileUrl: string, fileName?: string, target: string = '_blank') => {
  if (!fileUrl) {
    console.error('文件URL不能为空')
    return
  }

  try {
    // 使用后端API下载文件，避免MinIO重定向问题
    const response = await request.download({ url: fileUrl })
    
    // 创建blob URL
    const blob = new Blob([response])
    const blobUrl = window.URL.createObjectURL(blob)
    
    // 创建下载链接
    const link = document.createElement('a')
    link.href = blobUrl
    link.download = fileName || 'download'
    link.target = target
    link.style.display = 'none'
    
    // 添加到DOM并触发点击
    document.body.appendChild(link)
    link.click()
    
    // 清理DOM和blob URL
    setTimeout(() => {
      document.body.removeChild(link)
      window.URL.revokeObjectURL(blobUrl)
    }, 100)
  } catch (error) {
    console.error('文件下载失败:', error)
    throw error
  }
}

/**
 * 从URL中提取文件名和扩展名
 * @param url 文件URL
 * @returns 文件名（包含扩展名）
 */
const extractFileNameFromUrl = (url: string): string => {
  try {
    // 处理MinIO URL的特殊情况
    let processedUrl = url
    
    // 如果是Office Online预览链接，先提取原始URL
    if (url.includes('view.officeapps.live.com')) {
      const urlParams = new URL(url)
      const srcParam = urlParams.searchParams.get('src')
      if (srcParam) {
        processedUrl = decodeURIComponent(srcParam)
      }
    }
    
    // 从URL中提取文件名
    const urlObj = new URL(processedUrl)
    const pathname = urlObj.pathname
    
    // 分割路径并获取最后一个非空部分
    const pathParts = pathname.split('/').filter(part => part.length > 0)
    const fileName = pathParts[pathParts.length - 1] || ''
    
    // 如果文件名包含查询参数，去掉查询参数
    const cleanFileName = fileName.split('?')[0]
    
    // 如果文件名为空或没有扩展名，返回默认名称
    if (!cleanFileName || !cleanFileName.includes('.')) {
      return 'regulation.txt'
    }
    
    // 解码URL编码的文件名
    const decodedFileName = decodeURIComponent(cleanFileName)
    
    // 确保文件名不为空
    if (!decodedFileName || decodedFileName.trim() === '') {
      return 'regulation.txt'
    }
    
    return decodedFileName
  } catch (error) {
    console.error('从URL提取文件名失败:', error)
    return 'regulation.txt'
  }
}

/**
 * 下载规章制度文件
 * @param id 规章制度ID
 * @param fileName 文件名（可选，如果不传则从后端获取）
 * @param fileUrl 文件URL（可选，用于提取文件名）
 */
export const downloadRegulationFile = async (id: number, fileName?: string, fileUrl?: string) => {
  if (!id) {
    console.error('规章制度ID不能为空')
    return
  }

  try {
    // 使用规章制度下载API
    const response = await request.download({ url: `/system/regulation/download?id=${id}` })
    
    // 创建blob URL
    const blob = new Blob([response])
    const blobUrl = window.URL.createObjectURL(blob)
    
    // 确定下载文件名
    let downloadFileName = fileName
    if (!downloadFileName && fileUrl) {
      downloadFileName = extractFileNameFromUrl(fileUrl)
    }
    if (!downloadFileName) {
      downloadFileName = 'regulation.txt'
    }
    
    // 创建下载链接
    const link = document.createElement('a')
    link.href = blobUrl
    link.download = downloadFileName
    link.style.display = 'none'
    
    // 添加到DOM并触发点击
    document.body.appendChild(link)
    link.click()
    
    // 清理DOM和blob URL
    setTimeout(() => {
      document.body.removeChild(link)
      window.URL.revokeObjectURL(blobUrl)
    }, 100)
  } catch (error) {
    console.error('规章制度文件下载失败:', error)
    throw error
  }
}

/**
 * 检查URL是否是Office Online预览链接
 * @param url 要检查的URL
 * @returns 是否是Office Online预览链接
 */
export const isOfficeOnlineUrl = (url: string): boolean => {
  return url.includes('view.officeapps.live.com')
}

/**
 * 从Office Online预览链接中提取原始文件URL
 * @param url Office Online预览链接
 * @returns 原始文件URL
 */
export const extractOriginalUrl = (url: string): string => {
  if (!isOfficeOnlineUrl(url)) {
    return url
  }

  try {
    const urlParams = new URL(url)
    const srcParam = urlParams.searchParams.get('src')
    if (srcParam) {
      return decodeURIComponent(srcParam)
    }
  } catch (error) {
    console.error('解析Office Online链接失败:', error)
  }

  return url
}

/**
 * 下载图片
 * @param url 图片URL
 * @param fileName 文件名（可选）
 */
export const downloadImage = async (url: string, fileName?: string) => {
  if (!url) {
    console.error('图片URL不能为空')
    return
  }

  try {
    // 使用fetch获取图片数据
    const response = await fetch(url)
    const blob = await response.blob()
    
    // 创建blob URL
    const blobUrl = window.URL.createObjectURL(blob)
    
    // 创建下载链接
    const link = document.createElement('a')
    link.href = blobUrl
    link.download = fileName || `image_${Date.now()}.png`
    link.style.display = 'none'
    
    // 添加到DOM并触发点击
    document.body.appendChild(link)
    link.click()
    
    // 清理DOM和blob URL
    setTimeout(() => {
      document.body.removeChild(link)
      window.URL.revokeObjectURL(blobUrl)
    }, 100)
  } catch (error) {
    console.error('图片下载失败:', error)
    throw error
  }
}

// 保持向后兼容的默认导出
const download = {
  image: downloadImage,
  file: downloadFile,
  regulation: downloadRegulationFile
}

export default download
