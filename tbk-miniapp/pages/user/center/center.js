// pages/user/center/center.js
import { request } from '../../../utils/request';
import { getToken, removeToken } from '../../../utils/auth';

const app = getApp();

Page({
  data: {
    userInfo: null,
    isLoggedIn: false,
    agentInfo: null, // 代理信息
    isAgent: false, // 是否是代理
    stats: {
      orderCount: 0, // 订单数量
      totalCommission: 0, // 累计佣金
      availableCommission: 0, // 可提现佣金
      todayCommission: 0 // 今日佣金
    },
    menuList: [
      {
        id: 'team',
        icon: '👥',
        name: '我的团队',
        url: '/pages/team/list',
        agentOnly: true
      },
      {
        id: 'withdraw',
        icon: '💳',
        name: '提现管理',
        url: '/pages/withdraw/list',
        agentOnly: true
      },
      {
        id: 'share',
        icon: '📱',
        name: '分享推广',
        url: '/pages/share/index',
        agentOnly: true
      },
      {
        id: 'about',
        icon: 'ℹ️',
        name: '关于我们',
        url: '/pages/about/index'
      }
    ]
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.checkLoginStatus();
    if (this.data.isLoggedIn) {
      this.loadUserData();
    }
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadUserData().finally(() => {
      wx.stopPullDownRefresh();
    });
  },

  // 分享配置
  onShareAppMessage() {
    const { agentInfo } = this.data;
    if (agentInfo && agentInfo.invitationCode) {
      return {
        title: '淘宝客优惠商城 - 省钱又赚钱',
        path: `/pages/index/index?inviteCode=${agentInfo.invitationCode}`,
        imageUrl: '/images/share-bg.png'
      };
    }
    return {
      title: '淘宝客优惠商城 - 精选好货',
      path: '/pages/index/index'
    };
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = getToken();
    const userInfo = app.globalData.userInfo;
    // 优先从全局数据读取代理状态，全局数据不存在则从缓存读取
    const isAgent = app.globalData.isAgent || wx.getStorageSync('isAgent') === true || wx.getStorageSync('isAgent') === 'true' || false;
    const agentInfo = app.globalData.agentInfo || wx.getStorageSync('agentInfo') || null;
    
    this.setData({
      isLoggedIn: !!token && !!userInfo,
      userInfo: userInfo || null,
      isAgent: isAgent,
      agentInfo: agentInfo
    });
  },

  // 加载用户数据
  async loadUserData() {
    try {
      wx.showLoading({ title: '加载中...' });

      // 并行加载代理信息和统计数据
      const [agentRes, statsRes] = await Promise.all([
        this.loadAgentInfo(),
        this.loadStats()
      ]);

      wx.hideLoading();
    } catch (error) {
      wx.hideLoading();
      console.error('加载用户数据失败:', error);
    }
  },

  // 加载代理信息
  async loadAgentInfo() {
    try {
      const res = await request({
        url: '/miniapp/tbk/agent/status',
        method: 'GET'
      });

      if (res.code === 200 && res.data) {
        const agentInfo = res.data;
        const isAgent = agentInfo.status === 'approved'; // status='approved'表示已通过审核
        
        this.setData({
          agentInfo: agentInfo,
          isAgent: isAgent
        });
        
        // 同步更新到全局数据和缓存
        if (isAgent) {
          app.globalData.isAgent = true;
          app.globalData.agentInfo = agentInfo;
          app.globalData.invitationCode = agentInfo.invitationCode || '';
          wx.setStorageSync('isAgent', true);
          wx.setStorageSync('agentInfo', agentInfo);
          wx.setStorageSync('invitationCode', agentInfo.invitationCode || '');
        }
        
        return agentInfo;
      }
    } catch (error) {
      // 如果未申请代理，接口可能返回404，这是正常情况
      // 注意：接口失败时不覆盖已有的代理状态（避免覆盖从全局数据中读取的正确状态）
      console.log('未找到代理信息:', error);
      if (!this.data.isAgent) {
        this.setData({
          agentInfo: null,
          isAgent: false
        });
      }
    }
    return null;
  },

  // 加载统计数据
  async loadStats() {
    try {
      const res = await request({
        url: '/miniapp/user/stats',
        method: 'GET'
      });

      if (res.code === 200 && res.data) {
        this.setData({
          stats: {
            orderCount: res.data.orderCount || 0,
            totalCommission: res.data.totalCommission || 0,
            availableCommission: res.data.availableCommission || 0,
            todayCommission: res.data.todayCommission || 0
          }
        });
        return res.data;
      }
    } catch (error) {
      console.error('加载统计数据失败:', error);
    }
    return null;
  },

  // 点击登录按钮
  handleLogin() {
    wx.navigateTo({
      url: '/pages/auth/login/login'
    });
  },

  // 点击代理申请/代理中心
  handleAgentAction() {
    const { isAgent, agentInfo } = this.data;
    
    if (!this.data.isLoggedIn) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/auth/login/login'
        });
      }, 1500);
      return;
    }

    if (isAgent) {
      // 已是代理，跳转到代理中心
      wx.navigateTo({
        url: '/pages/agent/center'
      });
    } else if (agentInfo && agentInfo.status === 0) {
      // 审核中
      wx.showModal({
        title: '申请审核中',
        content: '您的代理申请正在审核中，请耐心等待',
        showCancel: false
      });
    } else if (agentInfo && agentInfo.status === 2) {
      // 已拒绝，可以重新申请
      wx.navigateTo({
        url: '/pages/agent/apply/apply'
      });
    } else {
      // 未申请，跳转到申请页面
      wx.navigateTo({
        url: '/pages/agent/apply/apply'
      });
    }
  },

  // 复制邀请码（WXML 使用 catchtap 已阻止冒泡，无需在 JS 中调用 stopPropagation）
  handleCopyInviteCode: function() {
    // 从多个来源尝试获取邀请码
    var agentInfo = this.data.agentInfo;
    var invitationCode = (agentInfo && agentInfo.invitationCode) || 
                         app.globalData.invitationCode || 
                         wx.getStorageSync('invitationCode') || 
                         '';
    
    console.log('复制邀请码 - agentInfo:', agentInfo);
    console.log('复制邀请码 - invitationCode:', invitationCode);
    
    if (!invitationCode || invitationCode === '-') {
      wx.showToast({
        title: '暂无邀请码',
        icon: 'none',
        duration: 2000
      });
      return;
    }
    
    wx.setClipboardData({
      data: String(invitationCode),
      success: function() {
        wx.showToast({
          title: '邀请码已复制',
          icon: 'success'
        });
      },
      fail: function(err) {
        console.error('复制邀请码失败:', err);
        wx.showToast({
          title: '复制失败',
          icon: 'none'
        });
      }
    });
  },

  // 点击菜单项
  handleMenuClick(e) {
    const { item } = e.currentTarget.dataset;
    
    // 检查是否需要代理权限
    if (item.agentOnly && !this.data.isAgent) {
      wx.showToast({
        title: '请先成为代理',
        icon: 'none'
      });
      return;
    }

    // 检查是否需要登录
    if (!this.data.isLoggedIn) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/auth/login/login'
        });
      }, 1500);
      return;
    }

    // 跳转到对应页面
    if (item.url) {
      wx.navigateTo({
        url: item.url,
        fail: () => {
          wx.showToast({
            title: '页面开发中',
            icon: 'none'
          });
        }
      });
    }
  },

  // 退出登录
  handleLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用app.logout()统一清除所有缓存和全局状态
          app.logout();
          
          // 更新页面状态
          this.setData({
            isLoggedIn: false,
            userInfo: null,
            agentInfo: null,
            isAgent: false,
            stats: {
              orderCount: 0,
              totalCommission: 0,
              availableCommission: 0,
              todayCommission: 0
            }
          });

          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
      }
    });
  },

  // 格式化金额
  formatMoney(amount) {
    if (!amount) return '0.00';
    return (amount / 100).toFixed(2);
  }
});
