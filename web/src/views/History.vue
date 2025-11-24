<template>
  <div class="history-container">
    <div class="history-header">
      <h2>对话历史</h2>
      <div class="header-actions">
        <el-select v-model="filterSubject" placeholder="筛选学科" clearable style="width: 140px" @change="handleFilter">
          <el-option label="全部学科" value="" />
          <el-option
            v-for="subject in SUBJECTS"
            :key="subject.code"
            :label="subject.name"
            :value="subject.code"
          />
        </el-select>
      </div>
    </div>

    <div v-loading="conversationStore.loading" class="history-content">
      <div v-if="conversationStore.conversationList.length === 0" class="empty-state">
        <el-empty description="暂无对话历史">
          <el-button type="primary" @click="goToChat">开始对话</el-button>
        </el-empty>
      </div>

      <div v-else class="conversation-list">
        <div
          v-for="conversation in conversationStore.conversationList"
          :key="conversation.id"
          class="conversation-item"
          @click="openConversation(conversation.id)"
        >
          <div class="conversation-header">
            <div class="title-section">
              <h3 class="conversation-title">{{ conversation.title || '未命名对话' }}</h3>
              <el-tag v-if="conversation.isFavorite" type="warning" size="small">
                <el-icon><Star /></el-icon>
                收藏
              </el-tag>
            </div>
            <div class="actions">
              <el-tooltip content="收藏">
                <el-button
                  :icon="conversation.isFavorite ? Star : StarFilled"
                  circle
                  size="small"
                  @click.stop="toggleFavorite(conversation)"
                />
              </el-tooltip>
              <el-tooltip content="删除">
                <el-button
                  :icon="Delete"
                  circle
                  size="small"
                  type="danger"
                  @click.stop="confirmDelete(conversation.id)"
                />
              </el-tooltip>
            </div>
          </div>

          <div class="conversation-meta">
            <el-tag size="small">{{ getSubjectName(conversation.subject) }}</el-tag>
            <el-tag size="small" type="info">{{ getGradeName(conversation.grade) }}</el-tag>
            <span class="time">{{ formatRelativeTime(conversation.createTime) }}</span>
          </div>

          <div v-if="conversation.lastMessage" class="last-message">
            {{ conversation.lastMessage }}
          </div>
        </div>
      </div>

      <div v-if="conversationStore.conversationList.length > 0" class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, StarFilled, Delete } from '@element-plus/icons-vue'
import { useConversationStore } from '@/stores/conversation'
import { SUBJECTS, getSubjectName, getGradeName } from '@/utils/constants'
import { formatRelativeTime } from '@/utils/format'
import { toggleFavorite as toggleFavoriteApi, deleteConversation } from '@/api/conversation.mock'
import type { Conversation } from '@/types'

const router = useRouter()
const conversationStore = useConversationStore()

const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterSubject = ref('')

const loadConversations = async () => {
  try {
    const result = await conversationStore.fetchConversationList({
      page: currentPage.value,
      size: pageSize.value,
      subject: filterSubject.value || undefined,
    })
    total.value = result.total
  } catch (error) {
    console.error('加载对话列表失败:', error)
    ElMessage.error('加载对话列表失败')
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  loadConversations()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadConversations()
}

const handleFilter = () => {
  currentPage.value = 1
  loadConversations()
}

const openConversation = (id: number) => {
  router.push(`/chat/${id}`)
}

const toggleFavorite = async (conversation: Conversation) => {
  try {
    await toggleFavoriteApi(conversation.id, !conversation.isFavorite)
    conversation.isFavorite = conversation.isFavorite ? 0 : 1
    ElMessage.success(conversation.isFavorite ? '已收藏' : '已取消收藏')
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

const confirmDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个对话吗？删除后无法恢复', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteConversation(id)
    ElMessage.success('已删除')
    loadConversations()
  } catch (error) {
    // 用户取消或删除失败
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const goToChat = () => {
  router.push('/chat')
}

onMounted(() => {
  loadConversations()
})
</script>

<style scoped lang="scss">
.history-container {
  max-width: 1200px;
  margin: 0 auto;

  .history-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
    }
  }

  .history-content {
    min-height: 400px;

    .empty-state {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 400px;
    }

    .conversation-list {
      display: grid;
      gap: 16px;

      .conversation-item {
        padding: 20px;
        background: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          border-color: #409eff;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          transform: translateY(-2px);
        }

        .conversation-header {
          display: flex;
          align-items: flex-start;
          justify-content: space-between;
          margin-bottom: 12px;

          .title-section {
            flex: 1;
            display: flex;
            align-items: center;
            gap: 12px;
            min-width: 0;

            .conversation-title {
              font-size: 16px;
              font-weight: 500;
              color: #303133;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }

          .actions {
            display: flex;
            gap: 8px;
            opacity: 0;
            transition: opacity 0.3s;
          }
        }

        &:hover .actions {
          opacity: 1;
        }

        .conversation-meta {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 12px;
          font-size: 12px;

          .time {
            color: #909399;
            margin-left: auto;
          }
        }

        .last-message {
          font-size: 14px;
          color: #606266;
          line-height: 1.6;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
      }
    }

    .pagination {
      display: flex;
      justify-content: center;
      margin-top: 32px;
    }
  }
}

// 深色模式
.dark {
  .history-container {
    .history-header {
      h2 {
        color: #e5e5e5;
      }
    }

    .history-content {
      .conversation-list {
        .conversation-item {
          background: #1a1a1a;
          border-color: #2a2a2a;

          &:hover {
            border-color: #409eff;
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
          }

          .conversation-header {
            .title-section {
              .conversation-title {
                color: #e5e5e5;
              }
            }
          }

          .last-message {
            color: #909399;
          }
        }
      }
    }
  }
}
</style>

