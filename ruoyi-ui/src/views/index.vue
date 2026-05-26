<template>
  <div class="app-container home">
    <!-- 数据过滤工具栏 -->
    <el-row :gutter="20" class="filter-section">
      <el-col :span="24">
        <el-card class="filter-card">
          <div class="filter-content">
            <div class="filter-label">
              <i class="el-icon-s-operation"></i>
              <span>数据筛选：</span>
            </div>
            <div class="filter-controls">
              <el-select
                v-model="selectedPlatform"
                placeholder="请选择联盟平台"
                clearable
                @change="handlePlatformChange"
                style="width: 200px"
              >
                <el-option
                  v-for="platform in availablePlatforms"
                  :key="platform.id"
                  :label="platform.platformName"
                  :value="platform.id"
                >
                  <span style="float: left">
                    <i :class="getPlatformIcon(platform.platformType)" style="margin-right: 8px"></i>
                    {{ platform.platformName }}
                  </span>
                  <span style="float: right; color: #8492a6; font-size: 13px">
                    {{ getPlatformTypeLabel(platform.platformType) }}
                  </span>
                </el-option>
              </el-select>
              <el-button
                type="primary"
                icon="el-icon-refresh"
                size="small"
                @click="handleRefresh"
                style="margin-left: 10px"
              >
                刷新数据
              </el-button>
              <div class="filter-info" v-if="selectedPlatform">
                <el-tag type="success" size="small">
                  当前筛选：{{ getCurrentPlatformName() }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 欢迎区域 -->
    <el-row :gutter="20" class="welcome-section">
      <el-col :span="24">
        <el-card class="welcome-card">
          <div class="welcome-content">
            <div class="welcome-left">
              <h1>CPS 联盟业务管理系统</h1>
              <p class="welcome-subtitle">多平台联盟推广 · 智能数据分析 · 高效佣金管理</p>
              <div class="quick-stats">
                <div class="stat-item">
                  <div class="stat-value">{{ statistics.todayOrders }}</div>
                  <div class="stat-label">今日订单</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">¥{{ statistics.todayCommission }}</div>
                  <div class="stat-label">今日佣金</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ statistics.activeAgents }}</div>
                  <div class="stat-label">活跃代理</div>
                </div>
              </div>
            </div>
        <div class="welcome-right">
          <img :src="require('@/assets/images/dashboard-preview.svg')" alt="Dashboard" />
        </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 核心数据卡片 -->
    <el-row :gutter="20" class="panel-group">
      <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleCardClick('orders')">
          <div class="card-panel-icon-wrapper icon-orders">
            <i class="el-icon-shopping-cart-2 card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">订单总量</div>
            <div class="card-panel-num">{{ statistics.totalOrders }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleCardClick('commission')">
          <div class="card-panel-icon-wrapper icon-commission">
            <i class="el-icon-money card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">累计佣金</div>
            <div class="card-panel-num">¥{{ statistics.totalCommission }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleCardClick('agents')">
          <div class="card-panel-icon-wrapper icon-agents">
            <i class="el-icon-user card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">代理人数</div>
            <div class="card-panel-num">{{ statistics.totalAgents }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleCardClick('conversion')">
          <div class="card-panel-icon-wrapper icon-conversion">
            <i class="el-icon-s-data card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">转化率</div>
            <div class="card-panel-num">{{ statistics.conversionRate }}%</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 - 订单趋势图 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span class="chart-title">订单与佣金趋势</span>
            <el-radio-group v-model="trendPeriod" size="small" style="float: right" @change="handlePeriodChange">
              <el-radio-button label="today">当天</el-radio-button>
              <el-radio-button label="week">近7天</el-radio-button>
              <el-radio-button label="month">近30天</el-radio-button>
              <el-radio-button label="year">近一年</el-radio-button>
            </el-radio-group>
          </div>
          <div id="orderTrendChart" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 联盟平台占比 和 系统公告 -->
    <el-row :gutter="20" class="chart-section">
      <!-- 联盟平台占比 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span class="chart-title">联盟平台订单占比</span>
          </div>
          <div id="platformPieChart" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>

      <!-- 系统公告 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>系统公告</span>
          </div>
          <div class="notice-list">
            <div v-for="(notice, index) in noticeList" :key="index" class="notice-item" @click="handleNoticeClick(notice)">
              <div class="notice-title">
                <el-tag v-if="notice.isNew" type="danger" size="mini" effect="dark">新</el-tag>
                {{ notice.title }}
              </div>
              <div class="notice-time">{{ notice.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { listUnionPlatform, getUserBoundPlatforms } from '@/api/system/unionPlatform'
import { listNotice } from '@/api/system/notice'

export default {
  name: "Index",
  data() {
    return {
      // 选中的联盟平台
      selectedPlatform: null,
      // 可用的联盟平台列表
      availablePlatforms: [],
      // 用户绑定的联盟平台列表（用于权限控制）
      userBoundPlatforms: [],
      // 用户是否为管理员
      isAdmin: false,
      // 统计数据
      statistics: {
        todayOrders: 1258,
        todayCommission: '3,456.78',
        activeAgents: 89,
        totalOrders: 45236,
        totalCommission: '892,345.67',
        totalAgents: 356,
        conversionRate: 23.5
      },
      // 趋势周期
      trendPeriod: 'today',
      // 系统公告
      noticeList: [],
      // 图表实例
      orderTrendChart: null,
      platformPieChart: null
    }
  },
  created() {
    // 判断用户角色
    this.checkUserRole()
    // 先加载用户绑定的联盟平台，再加载可用平台列表
    this.loadUserBoundPlatforms()
    // 加载系统公告
    this.loadNoticeList()
  },
  mounted() {
    this.$nextTick(() => {
      this.initOrderTrendChart()
      this.initPlatformPieChart()
    })
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize)
    // 加载初始数据
    this.loadDashboardData()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.orderTrendChart) {
      this.orderTrendChart.dispose()
    }
    if (this.platformPieChart) {
      this.platformPieChart.dispose()
    }
  },
  methods: {
    // 检查用户角色
    checkUserRole() {
      const roles = this.$store.getters.roles || []
      this.isAdmin = roles.includes('admin') || roles.includes('超级管理员')
    },
    // 加载用户绑定的联盟平台列表
    loadUserBoundPlatforms() {
      getUserBoundPlatforms().then(response => {
        this.userBoundPlatforms = response.data || []
        // 加载完用户绑定信息后，再加载可用平台列表
        this.loadAvailablePlatforms()
      }).catch(() => {
        // 如果获取失败，清空绑定列表并继续加载可用平台
        this.userBoundPlatforms = []
        this.loadAvailablePlatforms()
      })
    },
    // 加载可用的联盟平台列表
    loadAvailablePlatforms() {
      // 如果是管理员，显示所有启用的平台；如果是普通代理且有绑定，只显示绑定的平台
      if (this.isAdmin) {
        // 管理员可以看到所有被使用的联盟平台（isQuote='1'表示被使用）
        listUnionPlatform({ isQuote: '1' }).then(response => {
          this.availablePlatforms = response.rows || []
          // 不默认选中任何平台，显示所有平台的汇总数据
          this.selectedPlatform = null
          if (this.availablePlatforms.length > 0) {
            this.loadDashboardData()
          }
        }).catch(() => {
          this.availablePlatforms = []
        })
      } else {
        // 普通代理用户只能看到自己绑定的联盟平台
        if (this.userBoundPlatforms && this.userBoundPlatforms.length > 0) {
          this.availablePlatforms = this.userBoundPlatforms
          // 不默认选中任何平台，显示所有绑定平台的汇总数据
          this.selectedPlatform = null
          this.loadDashboardData()
        } else {
          // 没有绑定任何联盟平台，显示提示
          this.availablePlatforms = []
          this.$message.warning('您还未绑定任何联盟平台，无法查看数据。请联系管理员分配联盟权限。')
        }
      }
    },
    // 加载仪表板数据
    loadDashboardData() {
      // 这里应该调用后端API获取数据，目前使用模拟数据
      // 实际项目中应该类似：
      // getDashboardStatistics({ platformId: this.selectedPlatform }).then(...)
      const loading = this.$loading({
        lock: true,
        text: '加载数据中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      setTimeout(() => {
        // 模拟数据加载
        this.updateStatisticsData()
        this.updateOrderTrendData(this.trendPeriod)
        this.updatePlatformPieData()
        loading.close()
      }, 500)
    },
    // 更新统计数据
    updateStatisticsData() {
      // 根据选中的平台过滤数据
      if (this.selectedPlatform) {
        // 模拟根据平台过滤后的数据
        this.statistics = {
          todayOrders: Math.floor(Math.random() * 500) + 100,
          todayCommission: (Math.random() * 2000 + 500).toFixed(2),
          activeAgents: Math.floor(Math.random() * 50) + 20,
          totalOrders: Math.floor(Math.random() * 20000) + 5000,
          totalCommission: (Math.random() * 500000 + 100000).toFixed(2),
          totalAgents: Math.floor(Math.random() * 200) + 50,
          conversionRate: (Math.random() * 15 + 10).toFixed(1)
        }
      } else {
        // 显示全部平台的汇总数据
        this.statistics = {
          todayOrders: 1258,
          todayCommission: '3,456.78',
          activeAgents: 89,
          totalOrders: 45236,
          totalCommission: '892,345.67',
          totalAgents: 356,
          conversionRate: 23.5
        }
      }
    },
    // 更新平台饼图数据
    updatePlatformPieData() {
      if (!this.platformPieChart) return

      let pieData = []
      if (this.selectedPlatform) {
        // 如果选中了平台，只显示该平台的数据
        const platform = this.availablePlatforms.find(p => p.id === this.selectedPlatform)
        if (platform) {
          pieData = [
            { value: 100, name: platform.platformName, itemStyle: { color: this.getPlatformColor(platform.platformType) } }
          ]
        }
      } else {
        // 根据用户权限显示平台占比（管理员显示所有平台，普通用户仅显示绑定的平台）
        pieData = this.availablePlatforms.map((platform, index) => {
          // 为每个平台生成模拟数据（实际应从后端获取真实订单数据）
          const mockValue = Math.floor(Math.random() * 1000) + 200
          return {
            value: mockValue,
            name: platform.platformName,
            itemStyle: { color: this.getPlatformColor(platform.platformType) }
          }
        })
      }

      this.platformPieChart.setOption({
        series: [{
          data: pieData
        }]
      })
    },
    // 处理平台切换
    handlePlatformChange(value) {
      this.$message.success(value ? `已切换到${this.getCurrentPlatformName()}平台` : '已显示全部平台数据')
      this.loadDashboardData()
    },
    // 处理刷新
    handleRefresh() {
      this.$message.info('正在刷新数据...')
      // 重新加载用户绑定信息和数据
      this.loadUserBoundPlatforms()
      this.loadDashboardData()
    },
    // 获取当前平台名称
    getCurrentPlatformName() {
      if (!this.selectedPlatform) return '全部平台'
      const platform = this.availablePlatforms.find(p => p.id === this.selectedPlatform)
      return platform ? platform.platformName : '未知平台'
    },
    // 获取平台图标
    getPlatformIcon(platformType) {
      const iconMap = {
        'tbk': 'el-icon-s-goods',
        'jd': 'el-icon-box',
        'pdd': 'el-icon-present',
        'dtk': 'el-icon-shopping-bag-2'
      }
      return iconMap[platformType] || 'el-icon-s-platform'
    },
    // 获取平台类型标签
    getPlatformTypeLabel(platformType) {
      const labelMap = {
        'tbk': '淘宝',
        'jd': '京东',
        'pdd': '拼多多',
        'dtk': '第三方'
      }
      return labelMap[platformType] || '其他'
    },
    // 获取平台颜色
    getPlatformColor(platformType) {
      const colorMap = {
        'tbk': '#D84315',
        'jd': '#C62828',
        'pdd': '#6A1B9A',
        'dtk': '#1565C0'
      }
      return colorMap[platformType] || '#999999'
    },
    // 初始化订单趋势图
    initOrderTrendChart() {
      const chartDom = document.getElementById('orderTrendChart')
      if (!chartDom) return
      this.orderTrendChart = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['订单量', '佣金金额']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: [
          {
            type: 'value',
            name: '订单量',
            position: 'left'
          },
          {
            type: 'value',
            name: '佣金(元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '订单量',
            type: 'line',
            smooth: true,
            data: [120, 132, 101, 134, 90, 230, 210],
            itemStyle: {
              color: '#409EFF'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
              ])
            }
          },
          {
            name: '佣金金额',
            type: 'line',
            smooth: true,
            yAxisIndex: 1,
            data: [220, 282, 191, 234, 290, 330, 310],
            itemStyle: {
              color: '#67C23A'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
              ])
            }
          }
        ]
      }
      this.orderTrendChart.setOption(option)
    },
    // 初始化平台占比饼图
    initPlatformPieChart() {
      const chartDom = document.getElementById('platformPieChart')
      if (!chartDom) return
      this.platformPieChart = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [
          {
            name: '订单量',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: '淘宝联盟', itemStyle: { color: '#D84315' } },
              { value: 735, name: '京东联盟', itemStyle: { color: '#C62828' } },
              { value: 580, name: '拼多多', itemStyle: { color: '#6A1B9A' } },
              { value: 234, name: '大淘客', itemStyle: { color: '#1565C0' } }
            ]
          }
        ]
      }
      this.platformPieChart.setOption(option)
    },
    // 处理周期变化
    handlePeriodChange(period) {
      // 这里可以根据不同周期重新加载数据
      console.log('切换周期:', period)
      // 模拟数据更新
      this.updateOrderTrendData(period)
    },
    // 更新订单趋势数据
    updateOrderTrendData(period) {
      let xAxisData = []
      let ordersData = []
      let commissionData = []

      if (period === 'today') {
        // 当天数据：按小时展示（0点到23点）
        xAxisData = Array.from({ length: 24 }, (_, i) => `${i}:00`)
        // 模拟当天每小时的订单量（实际应从后端获取）
        ordersData = Array.from({ length: 24 }, () => Math.floor(Math.random() * 50) + 10)
        // 模拟当天每小时的佣金金额（实际应从后端获取）
        commissionData = Array.from({ length: 24 }, () => Math.floor(Math.random() * 100) + 20)
      } else if (period === 'week') {
        xAxisData = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        ordersData = [120, 132, 101, 134, 90, 230, 210]
        commissionData = [220, 282, 191, 234, 290, 330, 310]
      } else if (period === 'month') {
        xAxisData = Array.from({ length: 30 }, (_, i) => `${i + 1}日`)
        ordersData = Array.from({ length: 30 }, () => Math.floor(Math.random() * 200) + 50)
        commissionData = Array.from({ length: 30 }, () => Math.floor(Math.random() * 300) + 100)
      } else {
        xAxisData = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        ordersData = Array.from({ length: 12 }, () => Math.floor(Math.random() * 5000) + 1000)
        commissionData = Array.from({ length: 12 }, () => Math.floor(Math.random() * 8000) + 2000)
      }

      this.orderTrendChart.setOption({
        xAxis: { data: xAxisData },
        series: [
          { data: ordersData },
          { data: commissionData }
        ]
      })
    },
    // 处理卡片点击
    handleCardClick(type) {
      this.$message.info(`查看${type}详情`)
    },
    // 加载系统公告列表
    loadNoticeList() {
      // 查询正常状态的公告，按创建时间倒序排列，只取最新的5条
      listNotice({
        status: '0',  // 0表示正常状态
        pageNum: 1,
        pageSize: 5
      }).then(response => {
        if (response.rows && response.rows.length > 0) {
          // 转换数据格式以适配页面显示
          const currentDate = new Date()
          // 先按创建时间倒序排序，确保最新的在前面
          const sortedRows = response.rows.sort((a, b) => {
            return new Date(b.createTime) - new Date(a.createTime)
          })

          this.noticeList = sortedRows.map(notice => {
            // 判断是否为最近3天内的公告，标记为"新"
            const createTime = new Date(notice.createTime)
            const daysDiff = Math.floor((currentDate - createTime) / (1000 * 60 * 60 * 24))

            return {
              title: notice.noticeTitle,
              time: this.parseTime(notice.createTime, '{y}-{m}-{d} {h}:{i}:{s}'),
              isNew: daysDiff <= 3,
              noticeId: notice.noticeId,
              noticeType: notice.noticeType,
              noticeContent: notice.noticeContent
            }
          })
        } else {
          this.noticeList = []
        }
      }).catch(() => {
        this.noticeList = []
      })
    },
    // 处理公告点击
    handleNoticeClick(notice) {
      this.$message.info(`查看公告: ${notice.title}`)
    },
    // 处理窗口大小变化
    handleResize() {
      if (this.orderTrendChart) {
        this.orderTrendChart.resize()
      }
      if (this.platformPieChart) {
        this.platformPieChart.resize()
      }
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  padding: 20px;
  background: #f0f2f5;

  // 数据过滤工具栏
  .filter-section {
    margin-bottom: 20px;

    .filter-card {
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
      border: 1px solid #e8e8e8;

      ::v-deep .el-card__body {
        padding: 16px 20px;
      }

      .filter-content {
        display: flex;
        align-items: center;
        gap: 15px;

        .filter-label {
          display: flex;
          align-items: center;
          font-size: 14px;
          font-weight: 500;
          color: #333;
          white-space: nowrap;

          i {
            font-size: 18px;
            margin-right: 8px;
            color: #409EFF;
          }
        }

        .filter-controls {
          flex: 1;
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          gap: 10px;

          .filter-info {
            margin-left: 10px;
          }
        }
      }
    }
  }

  // 欢迎区域
  .welcome-section {
    margin-bottom: 20px;

    .welcome-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      color: #fff;

      ::v-deep .el-card__body {
        padding: 30px;
      }

      .welcome-content {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .welcome-left {
          flex: 1;

          h1 {
            font-size: 32px;
            font-weight: 600;
            margin: 0 0 10px 0;
          }

          .welcome-subtitle {
            font-size: 16px;
            opacity: 0.9;
            margin-bottom: 30px;
          }

          .quick-stats {
            display: flex;
            gap: 40px;

            .stat-item {
              .stat-value {
                font-size: 28px;
                font-weight: bold;
                margin-bottom: 5px;
              }

              .stat-label {
                font-size: 14px;
                opacity: 0.8;
              }
            }
          }
        }

        .welcome-right {
          img {
            max-width: 300px;
            border-radius: 8px;
          }
        }
      }
    }
  }

  // 数据卡片组
  .panel-group {
    margin-bottom: 20px;

    .card-panel-col {
      margin-bottom: 20px;
    }

    .card-panel {
      height: 108px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
      border-color: rgba(0, 0, 0, 0.05);
      border-radius: 8px;
      padding: 20px;
      display: flex;
      align-items: center;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        transform: translateY(-2px);
      }

      .card-panel-icon-wrapper {
        float: left;
        margin-right: 20px;
        padding: 16px;
        transition: all 0.3s;
        border-radius: 8px;

        .card-panel-icon {
          font-size: 48px;
          color: #fff;
        }
      }

      .icon-orders {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      .icon-commission {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      .icon-agents {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      .icon-conversion {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      .card-panel-description {
        float: right;
        text-align: right;

        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }

        .card-panel-num {
          font-size: 24px;
          font-weight: bold;
          color: #333;
        }
      }
    }
  }

  // 图表区域
  .chart-section {
    margin-bottom: 20px;

    .chart-card {
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

      .chart-title {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
    }
  }

  // 公告列表
  .notice-list {
    .notice-item {
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;
      transition: all 0.3s;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        background: #f8f9fa;
        padding-left: 10px;
      }

      .notice-title {
        font-size: 14px;
        color: #333;
        margin-bottom: 5px;

        .el-tag {
          margin-right: 5px;
        }
      }

      .notice-time {
        font-size: 12px;
        color: #999;
      }
    }
  }

  // 通用卡片样式
  .update-log {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    margin-bottom: 20px;

    ::v-deep .el-card__header {
      border-bottom: 1px solid #f0f0f0;
      padding: 18px 20px;

      .clearfix {
        display: flex;
        align-items: center;
        justify-content: space-between;

        span {
          font-size: 16px;
          font-weight: 500;
          color: #333;
        }
      }
    }

    ::v-deep .el-card__body {
      padding: 20px;
    }
  }
}

// 全宽样式（用于非管理员用户）
.full-width {
  flex: 0 0 100%;
  max-width: 100%;
}

// 响应式适配
@media (max-width: 768px) {
  .home {
    .filter-section {
      .filter-content {
        flex-direction: column;
        align-items: flex-start !important;

        .filter-label {
          width: 100%;
        }

        .filter-controls {
          width: 100%;
          flex-direction: column;
          align-items: flex-start !important;

          .el-select {
            width: 100% !important;
          }

          .el-button {
            width: 100%;
            margin-left: 0 !important;
          }

          .filter-info {
            margin-left: 0 !important;
            margin-top: 5px;
          }
        }
      }
    }

    .welcome-content {
      flex-direction: column;

      .welcome-right {
        margin-top: 20px;

        img {
          max-width: 100%;
        }
      }

      .welcome-left .quick-stats {
        flex-direction: column;
        gap: 15px;
      }
    }
  }
}
</style>
