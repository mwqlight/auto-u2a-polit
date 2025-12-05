<template>
  <div class="concurrent-programming-page">
    <!-- 页面标题 -->
    <n-page-header
      title="并发编程模块"
      subtitle="Java与Go并发编程对比学习"
      @back="$router.back"
    >
      <template #extra>
        <n-space>
          <n-button type="primary" @click="runAllExamples">
            <template #icon>
              <n-icon><PlayCircleOutline /></n-icon>
            </template>
            运行所有示例
          </n-button>
          <n-button @click="resetProgress">
            <template #icon>
              <n-icon><RefreshOutline /></n-icon>
            </template>
            重置进度
          </n-button>
        </n-space>
      </template>
    </n-page-header>

    <!-- 学习进度 -->
    <n-card class="mb-6" title="学习进度">
      <n-space vertical>
        <n-progress
          type="line"
          :percentage="progressPercentage"
          :indicator-placement="'inside'"
          processing
        />
        <n-space>
          <n-tag
            v-for="topic in topics"
            :key="topic.key"
            :type="topic.completed ? 'success' : 'default'"
            @click="scrollToTopic(topic.key)"
            class="cursor-pointer"
          >
            {{ topic.name }}
          </n-tag>
        </n-space>
      </n-space>
    </n-card>

    <!-- 学习内容 -->
    <div class="learning-content">
      <!-- 线程 vs Goroutines -->
      <section id="threads-vs-goroutines" class="topic-section">
        <n-card title="线程 vs Goroutines" class="mb-6">
          <template #header-extra>
            <n-switch
              v-model:value="topics[0].completed"
              @update:value="updateProgress"
            />
          </template>
          
          <n-tabs type="line" animated>
            <n-tab-pane name="concept" tab="概念对比">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java线程模型" size="small">
                    <n-list>
                      <n-list-item>
                        <n-text strong>重量级线程</n-text>
                        <n-text depth="3">每个线程对应一个操作系统线程</n-text>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>上下文切换开销大</n-text>
                        <n-text depth="3">需要内核态切换</n-text>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>内存占用高</n-text>
                        <n-text depth="3">默认栈大小1MB</ntext>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>线程池管理</n-text>
                        <n-text depth="3">Executor框架提供线程池</ntext>
                      </n-list-item>
                    </n-list>
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go Goroutines模型" size="small">
                    <n-list>
                      <n-list-item>
                        <n-text strong>轻量级协程</n-text>
                        <n-text depth="3">用户态线程，由Go运行时管理</n-text>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>上下文切换开销小</n-text>
                        <n-text depth="3">在用户态完成切换</n-text>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>内存占用低</n-text>
                        <n-text depth="3">初始栈大小2KB</ntext>
                      </n-list-item>
                      <n-list-item>
                        <n-text strong>自动调度</ntext>
                        <n-text depth="3">GMP调度模型自动管理</ntext>
                      </n-list-item>
                    </n-list>
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
            
            <n-tab-pane name="code" tab="代码示例">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java线程示例" size="small">
                    <code-editor
                      :code="javaThreadCode"
                      language="java"
                      :readonly="true"
                      height="300px"
                    />
                    <template #footer>
                      <n-button @click="runJavaThreadExample" type="primary" size="small">
                        运行示例
                      </n-button>
                    </template>
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go Goroutines示例" size="small">
                    <code-editor
                      :code="goGoroutineCode"
                      language="go"
                      :readonly="true"
                      height="300px"
                    />
                    <template #footer>
                      <n-button @click="runGoGoroutineExample" type="primary" size="small">
                        运行示例
                      </n-button>
                    </template>
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
          </n-tabs>
        </n-card>
      </section>

      <!-- 同步机制 -->
      <section id="sync-mechanisms" class="topic-section">
        <n-card title="同步机制对比" class="mb-6">
          <template #header-extra>
            <n-switch
              v-model:value="topics[1].completed"
              @update:value="updateProgress"
            />
          </template>
          
          <n-tabs type="line" animated>
            <n-tab-pane name="mutex" tab="互斥锁">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java synchronized/ReentrantLock" size="small">
                    <code-editor
                      :code="javaMutexCode"
                      language="java"
                      :readonly="true"
                      height="250px"
                    />
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go sync.Mutex" size="small">
                    <code-editor
                      :code="goMutexCode"
                      language="go"
                      :readonly="true"
                      height="250px"
                    />
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
            
            <n-tab-pane name="channel" tab="通道通信">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java BlockingQueue/Executor" size="small">
                    <code-editor
                      :code="javaChannelCode"
                      language="java"
                      :readonly="true"
                      height="250px"
                    />
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go Channels" size="small">
                    <code-editor
                      :code="goChannelCode"
                      language="go"
                      :readonly="true"
                      height="250px"
                    />
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
          </n-tabs>
        </n-card>
      </section>

      <!-- 并发模式 -->
      <section id="concurrent-patterns" class="topic-section">
        <n-card title="并发模式对比" class="mb-6">
          <template #header-extra>
            <n-switch
              v-model:value="topics[2].completed"
              @update:value="updateProgress"
            />
          </template>
          
          <n-tabs type="line" animated>
            <n-tab-pane name="worker-pool" tab="工作池模式">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java ExecutorService" size="small">
                    <code-editor
                      :code="javaWorkerPoolCode"
                      language="java"
                      :readonly="true"
                      height="300px"
                    />
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go Goroutines + Channels" size="small">
                    <code-editor
                      :code="goWorkerPoolCode"
                      language="go"
                      :readonly="true"
                      height="300px"
                    />
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
            
            <n-tab-pane name="pipeline" tab="管道模式">
              <n-grid :cols="2" :x-gap="24">
                <n-gi>
                  <n-card title="Java Stream API" size="small">
                    <code-editor
                      :code="javaPipelineCode"
                      language="java"
                      :readonly="true"
                      height="300px"
                    />
                  </n-card>
                </n-gi>
                <n-gi>
                  <n-card title="Go Channels Pipeline" size="small">
                    <code-editor
                      :code="goPipelineCode"
                      language="go"
                      :readonly="true"
                      height="300px"
                    />
                  </n-card>
                </n-gi>
              </n-grid>
            </n-tab-pane>
          </n-tabs>
        </n-card>
      </section>

      <!-- 性能对比 -->
      <section id="performance-comparison" class="topic-section">
        <n-card title="性能对比分析" class="mb-6">
          <template #header-extra>
            <n-switch
              v-model:value="topics[3].completed"
              @update:value="updateProgress"
            />
          </template>
          
          <n-grid :cols="2" :x-gap="24">
            <n-gi>
              <n-card title="内存占用对比" size="small">
                <n-statistic label="Java线程" :value="1024" suffix="KB">
                  <template #prefix>
                    <n-icon color="#ff6b6b">
                      <TrendingUpOutline />
                    </n-icon>
                  </template>
                </n-statistic>
                <n-statistic label="Go Goroutine" :value="2" suffix="KB">
                  <template #prefix>
                    <n-icon color="#51cf66">
                      <TrendingDownOutline />
                    </n-icon>
                  </template>
                </n-statistic>
              </n-card>
            </n-gi>
            <n-gi>
              <n-card title="创建速度对比" size="small">
                <n-statistic label="Java线程创建" :value="1000" suffix="μs">
                  <template #prefix>
                    <n-icon color="#ff6b6b">
                      <TrendingUpOutline />
                    </n-icon>
                  </template>
                </n-statistic>
                <n-statistic label="Go Goroutine创建" :value="0.5" suffix="μs">
                  <template #prefix>
                    <n-icon color="#51cf66">
                      <TrendingDownOutline />
                    </n-icon>
                  </template>
                </n-statistic>
              </n-card>
            </n-gi>
          </n-grid>
        </n-card>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useMessage } from 'naive-ui'
import {
  PlayCircleOutline,
  RefreshOutline,
  TrendingUpOutline,
  TrendingDownOutline
} from '@vicons/ionicons5'

const message = useMessage()

// 学习主题
const topics = ref([
  { key: 'threads-vs-goroutines', name: '线程 vs Goroutines', completed: false },
  { key: 'sync-mechanisms', name: '同步机制', completed: false },
  { key: 'concurrent-patterns', name: '并发模式', completed: false },
  { key: 'performance-comparison', name: '性能对比', completed: false }
])

// 进度计算
const progressPercentage = computed(() => {
  const completedCount = topics.value.filter(t => t.completed).length
  return Math.round((completedCount / topics.value.length) * 100)
})

// Java代码示例
const javaThreadCode = `public class ThreadExample {
    public static void main(String[] args) {
        // 创建线程
        Thread thread = new Thread(() -> {
            System.out.println("Java线程执行中...");
        });
        
        // 启动线程
        thread.start();
        
        // 使用线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(() -> {
            System.out.println("线程池任务执行");
        });
        executor.shutdown();
    }
}`

const goGoroutineCode = `package main

import (
    "fmt"
    "sync"
)

func main() {
    var wg sync.WaitGroup
    
    // 创建goroutine
    wg.Add(1)
    go func() {
        defer wg.Done()
        fmt.Println("Go Goroutine执行中...")
    }()
    
    // 等待所有goroutine完成
    wg.Wait()
}`

const javaMutexCode = `public class Counter {
    private int count = 0;
    private final Object lock = new Object();
    
    public void increment() {
        synchronized(lock) {
            count++;
        }
    }
    
    // 使用ReentrantLock
    private final ReentrantLock reentrantLock = new ReentrantLock();
    
    public void safeIncrement() {
        reentrantLock.lock();
        try {
            count++;
        } finally {
            reentrantLock.unlock();
        }
    }
}`

const goMutexCode = `package main

import (
    "sync"
)

type Counter struct {
    mu    sync.Mutex
    count int
}

func (c *Counter) Increment() {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.count++
}

func main() {
    var counter Counter
    var wg sync.WaitGroup
    
    for i := 0; i < 1000; i++ {
        wg.Add(1)
        go func() {
            defer wg.Done()
            counter.Increment()
        }()
    }
    
    wg.Wait()
    fmt.Println("最终计数:", counter.count)
}`

const javaChannelCode = `public class ChannelExample {
    public static void main(String[] args) throws InterruptedException {
        // 使用BlockingQueue模拟channel
        BlockingQueue<String> channel = new ArrayBlockingQueue<>(10);
        
        // 生产者
        ExecutorService producer = Executors.newSingleThreadExecutor();
        producer.submit(() -> {
            try {
                channel.put("消息1");
                channel.put("消息2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // 消费者
        ExecutorService consumer = Executors.newSingleThreadExecutor();
        consumer.submit(() -> {
            try {
                String msg = channel.take();
                System.out.println("收到: " + msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.shutdown();
        consumer.shutdown();
    }
}`

const goChannelCode = `package main

import "fmt"

func main() {
    // 创建无缓冲channel
    ch := make(chan string)
    
    // 生产者goroutine
    go func() {
        ch <- "消息1"
        ch <- "消息2"
        close(ch)
    }()
    
    // 消费者goroutine
    go func() {
        for msg := range ch {
            fmt.Println("收到:", msg)
        }
    }()
    
    // 等待完成
    select {}
}`

const javaWorkerPoolCode = `public class WorkerPool {
    private final ExecutorService executor;
    
    public WorkerPool(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }
    
    public void submitTask(Runnable task) {
        executor.submit(task);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    public static void main(String[] args) {
        WorkerPool pool = new WorkerPool(4);
        
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.submitTask(() -> {
                System.out.println("任务" + taskId + "执行中");
            });
        }
        
        pool.shutdown();
    }
}`

const goWorkerPoolCode = `package main

import (
    "fmt"
    "sync"
)

func worker(id int, jobs <-chan int, results chan<- int, wg *sync.WaitGroup) {
    defer wg.Done()
    for job := range jobs {
        fmt.Printf("Worker %d processing job %d\\n", id, job)
        results <- job * 2
    }
}

func main() {
    jobs := make(chan int, 100)
    results := make(chan int, 100)
    
    var wg sync.WaitGroup
    
    // 启动4个worker
    for w := 1; w <= 4; w++ {
        wg.Add(1)
        go worker(w, jobs, results, &wg)
    }
    
    // 发送10个任务
    for j := 1; j <= 10; j++ {
        jobs <- j
    }
    close(jobs)
    
    wg.Wait()
    close(results)
}`

const javaPipelineCode = `public class PipelineExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // 使用Stream API构建管道
        numbers.stream()
            .filter(n -> n % 2 == 0)        // 过滤偶数
            .map(n -> n * 2)               // 乘以2
            .sorted()                      // 排序
            .forEach(System.out::println); // 输出
    }
}`

const goPipelineCode = `package main

import "fmt"

func generator(nums ...int) <-chan int {
    out := make(chan int)
    go func() {
        for _, n := range nums {
            out <- n
        }
        close(out)
    }()
    return out
}

func square(in <-chan int) <-chan int {
    out := make(chan int)
    go func() {
        for n := range in {
            out <- n * n
        }
        close(out)
    }()
    return out
}

func main() {
    // 构建管道: 生成 -> 平方 -> 输出
    for n := range square(generator(1, 2, 3, 4, 5)) {
        fmt.Println(n)
    }
}`

// 方法实现
const scrollToTopic = (key: string) => {
  const element = document.getElementById(key)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

const updateProgress = () => {
  message.success('学习进度已更新!')
}

const runAllExamples = () => {
  message.info('开始运行所有并发编程示例...')
  // 这里可以添加实际运行代码的逻辑
}

const resetProgress = () => {
  topics.value.forEach(topic => {
    topic.completed = false
  })
  message.success('学习进度已重置!')
}

const runJavaThreadExample = () => {
  message.info('运行Java线程示例...')
}

const runGoGoroutineExample = () => {
  message.info('运行Go Goroutine示例...')
}
</script>

<style scoped>
.concurrent-programming-page {
  max-width: 1200px;
  margin: 0 auto;
}

.topic-section {
  margin-bottom: 32px;
}

.mb-6 {
  margin-bottom: 24px;
}

.cursor-pointer {
  cursor: pointer;
}

.learning-content {
  padding: 0 16px;
}
</style>