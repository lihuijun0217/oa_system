# 页面自动刷新功能使用说明

## 功能概述

页面自动刷新功能用于解决系统标签页数据不及时刷新的问题。当用户点击标签页切换或关闭标签页时，系统会自动刷新当前页面的数据，确保数据的实时性。

## 功能特性

1. **标签页切换自动刷新**：当用户点击其他标签页时，自动刷新目标页面的数据
2. **标签页关闭自动刷新**：当关闭标签页时，自动刷新当前激活页面的数据
3. **批量关闭自动刷新**：当关闭左侧、右侧或其他标签页时，自动刷新当前页面数据
4. **路由变化自动刷新**：当路由发生变化时，自动刷新页面数据

## 使用方法

### 1. 在页面组件中引入hook

```typescript
import { usePageRefresh } from '@/hooks/web/usePageRefresh'
```

### 2. 在页面组件中使用

```typescript
// 在页面组件的setup函数中添加
const getList = async () => {
  // 获取页面数据的逻辑
  const data = await Api.getList()
  list.value = data.list
}

// 使用页面自动刷新功能
usePageRefresh(() => {
  console.log('页面自动刷新')
  getList() // 刷新页面数据
})
```

### 3. 完整示例

```vue
<template>
  <div>
    <!-- 页面内容 -->
  </div>
</template>

<script lang="ts" setup>
import { usePageRefresh } from '@/hooks/web/usePageRefresh'
import * as Api from '@/api/example'

const loading = ref(true)
const list = ref([])

/** 获取列表数据 */
const getList = async () => {
  loading.value = true
  try {
    const data = await Api.getList()
    list.value = data.list
  } finally {
    loading.value = false
  }
}

/** 初始化 */
onMounted(() => {
  getList()
})

// 使用页面自动刷新功能
usePageRefresh(() => {
  console.log('页面自动刷新')
  getList()
})
</script>
```

## 触发条件

页面自动刷新功能会在以下情况下触发：

1. **点击标签页切换**：用户点击其他标签页时
2. **关闭当前标签页**：关闭当前标签页并激活其他标签页时
3. **关闭其他标签页**：关闭左侧、右侧或其他标签页时
4. **路由变化**：页面路由发生变化时

## 注意事项

1. **性能考虑**：自动刷新会触发API请求，请确保刷新逻辑不会造成性能问题
2. **错误处理**：在刷新回调中建议添加错误处理逻辑
3. **加载状态**：建议在刷新时显示加载状态，提升用户体验
4. **条件刷新**：可以根据需要添加条件判断，避免不必要的刷新

## 示例页面

以下页面已经集成了自动刷新功能：

- `src/views/bpm/processInstance/index.vue` - 流程实例列表页面
- `src/views/bpm/task/todo/index.vue` - 待办任务页面

## 技术实现

自动刷新功能通过以下技术实现：

1. **自定义事件**：使用`CustomEvent`在标签页操作时触发刷新事件
2. **事件监听**：页面组件监听`refresh-page-data`事件
3. **路由匹配**：只有当事件的路由与当前路由匹配时才执行刷新
4. **生命周期管理**：在组件挂载时添加监听器，卸载时移除监听器 