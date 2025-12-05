import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Organization, OrganizationTree } from '@/types/organization'
import { getOrganizationTree, getOrganizationList, createOrganization, updateOrganization, deleteOrganization, moveOrganization } from '@/api/modules/organization'
import { useMessage } from 'naive-ui'

export const useOrganizationStore = defineStore('organization', () => {
  // 组织树数据
  const organizationTree = ref<OrganizationTree[]>([])
  // 组织列表数据
  const organizationList = ref<Organization[]>([])
  // 加载状态
  const loading = ref(false)
  // 选中的组织
  const selectedOrganization = ref<Organization | null>(null)

  // 获取组织树
  const fetchOrganizationTree = async () => {
    loading.value = true
    try {
      const response = await getOrganizationTree()
      organizationTree.value = response.data
    } catch (error) {
      const message = useMessage()
      message.error('获取组织树失败')
    } finally {
      loading.value = false
    }
  }

  // 获取组织列表
  const fetchOrganizationList = async (params?: any) => {
    loading.value = true
    try {
      const response = await getOrganizationList(params)
      organizationList.value = response.data.items
    } catch (error) {
      const message = useMessage()
      message.error('获取组织列表失败')
    } finally {
      loading.value = false
    }
  }

  // 创建组织
  const addOrganization = async (data: any) => {
    loading.value = true
    try {
      await createOrganization(data)
      const message = useMessage()
      message.success('创建组织成功')
      // 刷新组织树和列表
      await fetchOrganizationTree()
      await fetchOrganizationList()
    } catch (error) {
      const message = useMessage()
      message.error('创建组织失败')
    } finally {
      loading.value = false
    }
  }

  // 更新组织
  const editOrganization = async (id: string, data: any) => {
    loading.value = true
    try {
      await updateOrganization(id, data)
      const message = useMessage()
      message.success('更新组织成功')
      // 刷新组织树和列表
      await fetchOrganizationTree()
      await fetchOrganizationList()
    } catch (error) {
      const message = useMessage()
      message.error('更新组织失败')
    } finally {
      loading.value = false
    }
  }

  // 删除组织
  const removeOrganization = async (id: string) => {
    loading.value = true
    try {
      await deleteOrganization(id)
      const message = useMessage()
      message.success('删除组织成功')
      // 刷新组织树和列表
      await fetchOrganizationTree()
      await fetchOrganizationList()
    } catch (error) {
      const message = useMessage()
      message.error('删除组织失败')
    } finally {
      loading.value = false
    }
  }

  // 移动组织
  const moveOrg = async (id: string, targetParentId: string) => {
    loading.value = true
    try {
      await moveOrganization(id, { parentId: targetParentId })
      const message = useMessage()
      message.success('移动组织成功')
      // 刷新组织树
      await fetchOrganizationTree()
    } catch (error) {
      const message = useMessage()
      message.error('移动组织失败')
    } finally {
      loading.value = false
    }
  }

  // 设置选中的组织
  const setSelectedOrganization = (org: Organization | null) => {
    selectedOrganization.value = org
  }

  // 清空选中的组织
  const clearSelectedOrganization = () => {
    selectedOrganization.value = null
  }

  // 计算属性：是否有选中的组织
  const hasSelectedOrganization = computed(() => {
    return selectedOrganization.value !== null
  })

  // 计算属性：选中组织的子组织数量
  const selectedOrganizationChildrenCount = computed(() => {
    return selectedOrganization.value?.children?.length || 0
  })

  return {
    organizationTree,
    organizationList,
    loading,
    selectedOrganization,
    fetchOrganizationTree,
    fetchOrganizationList,
    addOrganization,
    editOrganization,
    removeOrganization,
    moveOrg,
    setSelectedOrganization,
    clearSelectedOrganization,
    hasSelectedOrganization,
    selectedOrganizationChildrenCount
  }
})
