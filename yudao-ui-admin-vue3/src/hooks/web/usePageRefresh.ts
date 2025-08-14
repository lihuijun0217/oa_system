import { onMounted, onUnmounted } from 'vue'
import type { RouteLocationNormalizedLoaded } from 'vue-router'
import { useRoute } from 'vue-router'

export interface RefreshPageDataEvent {
  route: RouteLocationNormalizedLoaded
}

export const usePageRefresh = (refreshCallback: () => void) => {
  const route = useRoute()
  
  // 处理页面刷新事件
  const handleRefreshPageData = (event: CustomEvent<RefreshPageDataEvent>) => {
    const { route: eventRoute } = event.detail
    
    // 只有当事件的路由与当前路由匹配时才执行刷新
    if (eventRoute.fullPath === route.fullPath) {
      console.log('页面数据自动刷新:', route.fullPath)
      refreshCallback()
    }
  }
  
  // 监听页面刷新事件
  const addRefreshListener = () => {
    window.addEventListener('refresh-page-data', handleRefreshPageData as EventListener)
  }
  
  // 移除页面刷新事件监听
  const removeRefreshListener = () => {
    window.removeEventListener('refresh-page-data', handleRefreshPageData as EventListener)
  }
  
  onMounted(() => {
    addRefreshListener()
  })
  
  onUnmounted(() => {
    removeRefreshListener()
  })
  
  return {
    addRefreshListener,
    removeRefreshListener
  }
} 