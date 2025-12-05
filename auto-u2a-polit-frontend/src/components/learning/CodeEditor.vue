<template>
  <div class="code-editor-container">
    <div class="editor-header">
      <n-space align="center">
        <n-text strong>{{ title }}</n-text>
        <n-tag :type="language === 'java' ? 'error' : 'success'" size="small">
          {{ language.toUpperCase() }}
        </n-tag>
      </n-space>
      <n-space>
        <n-button
          v-if="!readonly"
          text
          @click="formatCode"
          size="small"
        >
          <template #icon>
            <n-icon><CodeOutline /></n-icon>
          </template>
          格式化
        </n-button>
        <n-button
          text
          @click="copyCode"
          size="small"
        >
          <template #icon>
            <n-icon><CopyOutline /></n-icon>
          </template>
          复制
        </n-button>
      </n-space>
    </div>
    
    <div ref="editorRef" class="monaco-editor"></div>
    
    <!-- 输出面板 -->
    <n-collapse-transition :show="showOutput">
      <n-card v-if="showOutput" title="运行结果" size="small" class="output-panel">
        <pre class="output-content">{{ output }}</pre>
      </n-card>
    </n-collapse-transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { useMessage } from 'naive-ui'
import * as monaco from 'monaco-editor'
import { CodeOutline, CopyOutline } from '@vicons/ionicons5'

// 定义props
interface Props {
  code?: string
  language?: string
  readonly?: boolean
  height?: string
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  code: '',
  language: 'javascript',
  readonly: false,
  height: '400px',
  title: '代码编辑器'
})

const emit = defineEmits<{
  'update:code': [value: string]
  'code-change': [value: string]
}>()

const message = useMessage()
const editorRef = ref<HTMLElement>()
const editor = ref<monaco.editor.IStandaloneCodeEditor>()
const output = ref('')
const showOutput = ref(false)

// Monaco编辑器配置
const editorOptions: monaco.editor.IStandaloneEditorConstructionOptions = {
  value: props.code,
  language: props.language,
  readOnly: props.readonly,
  theme: 'vs-dark',
  fontSize: 14,
  lineNumbers: 'on',
  roundedSelection: true,
  scrollBeyondLastLine: false,
  automaticLayout: true,
  minimap: { enabled: false },
  wordWrap: 'on',
  tabSize: 2,
  insertSpaces: true,
  autoIndent: 'full',
  formatOnPaste: true,
  formatOnType: true
}

// 初始化编辑器
const initEditor = () => {
  if (!editorRef.value) return
  
  editor.value = monaco.editor.create(editorRef.value, editorOptions)
  
  // 监听内容变化
  editor.value.onDidChangeModelContent(() => {
    const code = editor.value?.getValue() || ''
    emit('update:code', code)
    emit('code-change', code)
  })
  
  // 设置编辑器高度
  if (props.height) {
    editorRef.value.style.height = props.height
  }
}

// 格式化代码
const formatCode = () => {
  if (editor.value) {
    editor.value.getAction('editor.action.formatDocument')?.run()
    message.success('代码格式化完成!')
  }
}

// 复制代码
const copyCode = async () => {
  try {
    const code = editor.value?.getValue() || props.code
    await navigator.clipboard.writeText(code)
    message.success('代码已复制到剪贴板!')
  } catch (err) {
    message.error('复制失败，请手动复制')
  }
}

// 运行代码
const runCode = async () => {
  showOutput.value = true
  output.value = '正在运行代码...\n'
  
  try {
    // 模拟代码执行
    const code = editor.value?.getValue() || props.code
    
    // 根据语言类型模拟不同输出
    if (props.language === 'java') {
      output.value += 'Java代码执行结果:\n'
      output.value += '线程启动成功，任务执行完成!\n'
    } else if (props.language === 'go') {
      output.value += 'Go代码执行结果:\n'
      output.value += 'Goroutine启动成功，任务执行完成!\n'
    } else {
      output.value += '代码执行成功!\n'
    }
    
    output.value += `执行时间: ${Math.random().toFixed(3)}秒\n`
    
  } catch (error) {
    output.value += `执行错误: ${error}\n`
  }
}

// 监听props变化
watch(() => props.code, (newCode) => {
  if (editor.value && newCode !== editor.value.getValue()) {
    editor.value.setValue(newCode)
  }
})

watch(() => props.language, (newLanguage) => {
  if (editor.value) {
    const model = editor.value.getModel()
    if (model) {
      monaco.editor.setModelLanguage(model, newLanguage)
    }
  }
})

// 生命周期
onMounted(() => {
  nextTick(() => {
    initEditor()
  })
})

onUnmounted(() => {
  editor.value?.dispose()
})

// 暴露方法给父组件
defineExpose({
  formatCode,
  copyCode,
  runCode,
  getCode: () => editor.value?.getValue() || ''
})
</script>

<style scoped>
.code-editor-container {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  overflow: hidden;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #d9d9d9;
}

.monaco-editor {
  width: 100%;
  min-height: 200px;
}

.output-panel {
  margin-top: 0;
  border-top: 1px solid #d9d9d9;
}

.output-content {
  margin: 0;
  padding: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
  white-space: pre-wrap;
  color: #333;
}

/* 暗色主题支持 */
:deep(.vs-dark) .editor-header {
  background-color: #1e1e1e;
  border-bottom-color: #333;
}

:deep(.vs-dark) .code-editor-container {
  border-color: #333;
}

:deep(.vs-dark) .output-panel {
  border-top-color: #333;
  background-color: #1e1e1e;
}

:deep(.vs-dark) .output-content {
  color: #ccc;
}
</style>