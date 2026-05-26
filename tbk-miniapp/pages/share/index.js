// pages/share/index.js - 分享推广页面
const app = getApp()
const request = require('../../utils/request.js')

Page({
  data: {
    isLoggedIn: false,
    isAgent: false,
    agentInfo: {}
  },

  onLoad() {
    this.checkLoginAndAgentStatus()
  },

  onShow() {
    this.checkLoginAndAgentStatus()
  },

  // 检查登录状态和代理状态
  checkLoginAndAgentStatus() {
    const globalData = app.globalData || {}
    const isLoggedIn = !!(globalData.token || wx.getStorageSync('token'))
    const isAgent = !!(globalData.isAgent || wx.getStorageSync('isAgent'))
    
    console.log('========== 分享推广页面状态检测 ==========')
    console.log('登录状态:', isLoggedIn)
    console.log('代理状态:', isAgent)
    console.log('全局 boundPlatforms:', globalData.boundPlatforms)
    console.log('====================================')

    this.setData({ isLoggedIn, isAgent })

    // 如果已登录且是代理，加载完整的代理信息和绑定平台
    if (isLoggedIn && isAgent) {
      this.loadAgentInfo()
    } else {
      // 非代理用户，清空代理信息
      this.setData({ agentInfo: {} })
    }
  },

  // 加载代理信息（使用 request.js 封装方法）
  loadAgentInfo() {
    const token = app.globalData?.token || wx.getStorageSync('token')
    if (!token) {
      console.log('token 不存在，无法加载代理信息')
      return
    }

    console.log('开始加载代理信息...')

    // 使用 request.get 封装方法，避免 baseUrl/apiUrl 问题
    request.get('/miniapp/tbk/agent/status')
      .then((res) => {
        console.log('代理状态接口返回:', res)
        if (res && res.code === 200) {
          const info = res.data || {}
          console.log('代理基本信息:', info)
          // 获取用户绑定的联盟平台
          this.loadBoundPlatforms(info)
        } else {
          console.error('获取代理状态失败:', res)
        }
      })
      .catch((err) => {
        console.error('代理状态接口调用失败:', err)
      })
  },

  // 加载用户绑定的联盟平台
  loadBoundPlatforms(agentInfo) {
    console.log('开始加载绑定平台...')
    console.log('全局缓存 boundPlatforms:', app.globalData.boundPlatforms)

    // 优先使用全局缓存的绑定平台数据
    if (app.globalData.boundPlatforms && app.globalData.boundPlatforms.length > 0) {
      console.log('使用全局缓存的绑定平台数据')
      this.processPlatformData(agentInfo, app.globalData.boundPlatforms)
      return
    }

    // 缓存为空时调用接口
    console.log('缓存为空，调用接口获取绑定平台')
    request.get('/system/unionPlatform/userBound')
      .then((res) => {
        console.log('绑定平台接口返回:', res)
        if (res && res.code === 200 && res.data && res.data.length > 0) {
          console.log('成功获取绑定平台:', res.data)

          // 更新全局缓存
          app.globalData.boundPlatforms = res.data
          wx.setStorageSync('boundPlatforms', res.data)

          this.processPlatformData(agentInfo, res.data)
        } else {
          console.log('用户没有绑定平台')
          agentInfo.platformTypeLabel = '暂未分配'
          this.setData({ agentInfo: agentInfo })
        }
      })
      .catch((err) => {
        console.error('获取绑定平台失败:', err)
        agentInfo.platformTypeLabel = '未知'
        this.setData({ agentInfo: agentInfo })
      })
  },

  // 处理平台数据（显示所有绑定平台）
  processPlatformData(agentInfo, boundPlatforms) {
    console.log('开始处理平台数据...')
    console.log('agentInfo:', agentInfo)
    console.log('boundPlatforms:', boundPlatforms)

    const platformMap = {
      'tbk': '淘宝联盟',
      'jd': '京东联盟',
      'dy': '抖音联盟',
      'pdd': '拼多多',
      'dtk': '大淘客'
    }

    // 将所有绑定平台转换为中文标签数组
    if (boundPlatforms && boundPlatforms.length > 0) {
      agentInfo.platforms = boundPlatforms.map(p => {
        return platformMap[p.platformType] || p.platformName || p.platformType || '未知'
      })
      console.log('所有平台标签:', agentInfo.platforms)
    } else {
      console.log('没有绑定平台数据')
      agentInfo.platforms = []
    }

    console.log('设置 agentInfo 到页面:', agentInfo)
    this.setData({ agentInfo: agentInfo })

    // 更新全局数据缓存
    app.globalData.agentInfo = agentInfo
    wx.setStorageSync('agentInfo', agentInfo)

    console.log('平台数据处理完成')
  },

  // 复制邀请码
  onCopyInvitationCode() {
    const code = this.data.agentInfo.invitationCode
    if (!code || code === '-') {
      wx.showToast({ title: '暂无邀请码', icon: 'none' })
      return
    }
    wx.setClipboardData({
      data: code,
      success() {
        wx.showToast({ title: '邀请码已复制', icon: 'success' })
      }
    })
  },

  // 跳转登录页
  goToLogin() {
    wx.navigateTo({ url: '/pages/auth/login/login' })
  },

  // 跳转代理申请页
  goToApply() {
    wx.navigateTo({ url: '/pages/agent/apply/apply' })
  },

  // 跳转收益页
  goToIncome() {
    wx.switchTab({ url: '/pages/goods/list/list' })
  },

  // 跳转首页
  goToHome() {
    wx.switchTab({ url: '/pages/index/index' })
  },

  // 微信分享配置
  onShareAppMessage() {
    const code = this.data.agentInfo.invitationCode || ''
    return {
      title: '好物推荐，高佣返利！',
      path: '/pages/index/index?inviteCode=' + code,
      imageUrl: ''
    }
  }
})
