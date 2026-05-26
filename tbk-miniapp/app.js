// app.js
const request = require('./utils/request.js');
const auth = require('./utils/auth.js');

App({
  onLaunch(options) {
    console.log('小程序启动', options);
    
    // 从缓存恢复数据
    this.restoreFromStorage();
    
    // 处理启动参数（如邀请码）
    if (options.query && options.query.invitationCode) {
      this.globalData.referralCode = options.query.invitationCode;
      wx.setStorageSync('referralCode', options.query.invitationCode);
    }
    
    // 延迟检查登录状态，避免阻塞启动
    setTimeout(() => {
      this.checkLogin();
    }, 500);
  },

  onShow(options) {
    console.log('小程序显示', options);
  },

  onHide() {
    console.log('小程序隐藏');
  },

  onError(msg) {
    console.log('小程序错误:', msg);
  },

  /**
   * 从缓存恢复数据
   */
  restoreFromStorage() {
    try {
      const token = auth.getToken();
      const userInfo = wx.getStorageSync('userInfo');
      const isAgent = wx.getStorageSync('isAgent');
      const agentInfo = wx.getStorageSync('agentInfo');
      const invitationCode = wx.getStorageSync('invitationCode');
      const referralCode = wx.getStorageSync('referralCode');
      const boundPlatforms = wx.getStorageSync('boundPlatforms'); // 新增：恢复绑定平台列表
      
      console.log('========== 从缓存恢复数据 ==========');
      console.log('原始 isAgent 值:', isAgent, '(类型:', typeof isAgent, ')');
      console.log('原始 boundPlatforms:', boundPlatforms);
      
      if (token) {
        this.globalData.token = token;
      }
      if (userInfo) {
        this.globalData.userInfo = userInfo;
      }
      if (agentInfo) {
        this.globalData.agentInfo = agentInfo;
      }
      // 【关键修复】isAgent需要明确赋值，即使是false也要赋值
      // 因为缓存中的false值也是有意义的状态（表示已登录但非代理）
      // 如果不赋值，可能会保留上次的状态导致判断错误
      // 【类型转换】确保从 storage 读取的值转换为正确的布尔类型
      if (isAgent !== null && isAgent !== undefined && isAgent !== '') {
        // 强制转换为布尔值，处理字符串 "true"/"false" 的情况
        this.globalData.isAgent = isAgent === true || isAgent === 'true' || isAgent === 1 || isAgent === '1';
        console.log('转换后 isAgent:', this.globalData.isAgent);
      } else {
        // 缓存中没有值，使用默认值false
        this.globalData.isAgent = false;
        console.log('isAgent 使用默认值: false');
      }
      if (invitationCode) {
        this.globalData.invitationCode = invitationCode;
      }
      if (referralCode) {
        this.globalData.referralCode = referralCode;
      }
      // 【类型转换】确保 boundPlatforms 是数组类型
      if (boundPlatforms) {
        // 如果是字符串，尝试解析为数组；否则直接使用
        try {
          this.globalData.boundPlatforms = typeof boundPlatforms === 'string' 
            ? JSON.parse(boundPlatforms) 
            : (Array.isArray(boundPlatforms) ? boundPlatforms : []);
          console.log('转换后 boundPlatforms 数量:', this.globalData.boundPlatforms.length);
        } catch (e) {
          console.error('解析 boundPlatforms 失败:', e);
          this.globalData.boundPlatforms = [];
        }
      } else {
        this.globalData.boundPlatforms = [];
        console.log('boundPlatforms 使用默认值: []');
      }
      
      console.log('缓存恢复完成 - isAgent:', this.globalData.isAgent, ', boundPlatforms数量:', this.globalData.boundPlatforms.length);
      console.log('====================================');
    } catch (e) {
      console.error('恢复缓存数据失败:', e);
    }
  },

  /**
   * 检查登录状态（异步方法，不阻塞启动流程）
   */
  checkLogin() {
    const token = this.globalData.token;
    if (!token) {
      console.log('未登录，跳过检查');
      return;
    }
    
    console.log('检查登录状态...');
    // 验证 token 是否有效
    request.get('/miniapp/auth/userInfo')
      .then(res => {
        if (res.code === 200 && res.data) {
          console.log('登录状态有效，用户信息:', res.data);
          
          // 兼容不同的返回数据结构
          // 如果 res.data 有 user 属性，使用 user；否则直接使用 data
          const userData = res.data.user || res.data;
          
          this.globalData.userInfo = userData;
          this.globalData.isAgent = res.data.isAgent || userData.isAgent || false;
          this.globalData.invitationCode = res.data.invitationCode || userData.invitationCode || '';
          this.globalData.boundPlatforms = res.data.boundPlatforms || []; // 新增：同步绑定平台列表
          
          // 更新缓存
          wx.setStorageSync('userInfo', this.globalData.userInfo);
          wx.setStorageSync('isAgent', this.globalData.isAgent);
          wx.setStorageSync('invitationCode', this.globalData.invitationCode);
          wx.setStorageSync('boundPlatforms', this.globalData.boundPlatforms); // 新增：更新绑定平台缓存
          
          console.log('用户信息已更新, boundPlatforms:', this.globalData.boundPlatforms.length);
        } else {
          console.warn('用户信息返回数据异常:', res);
        }
      })
      .catch(err => {
        console.error('获取用户信息失败:', err);
        // token 无效，清除登录状态
        if (err && (err.code === 401 || err.statusCode === 401)) {
          console.log('登录已过期，清除登录状态');
          this.logout();
        }
      });
  },

  /**
   * 登录
   */
  login(userInfo) {
    console.log('登录成功，保存用户信息:', userInfo);
    
    // 兼容不同的返回数据结构
    // 优先从userInfo获取token，如果没有则从storage读取（auth.js已保存）
    const token = userInfo.token || auth.getToken() || this.globalData.token;
    const isAgent = userInfo.isAgent || false;
    const invitationCode = userInfo.invitationCode || '';
    const boundPlatforms = userInfo.boundPlatforms || []; // 新增：保存绑定的联盟平台列表
    
    this.globalData.userInfo = userInfo;
    this.globalData.token = token;
    this.globalData.isAgent = isAgent;
    this.globalData.invitationCode = invitationCode;
    this.globalData.boundPlatforms = boundPlatforms; // 新增：缓存到全局数据
    
    // 保存到缓存
    if (token) {
      auth.setToken(token);
    }
    wx.setStorageSync('userInfo', userInfo);
    wx.setStorageSync('isAgent', isAgent);
    wx.setStorageSync('invitationCode', invitationCode);
    wx.setStorageSync('boundPlatforms', boundPlatforms); // 新增：保存到本地缓存
    
    console.log('用户信息已保存到缓存, token:', token ? '已获取' : '未获取', ', boundPlatforms:', boundPlatforms.length);
  },

  /**
   * 退出登录
   */
  logout() {
    this.globalData.userInfo = null;
    this.globalData.token = null;
    this.globalData.isAgent = false;
    this.globalData.agentInfo = null;
    this.globalData.invitationCode = '';
    this.globalData.referralCode = '';
    this.globalData.boundPlatforms = []; // 新增：清除绑定平台列表
    
    // 清除缓存
    auth.removeToken();
    wx.removeStorageSync('userInfo');
    wx.removeStorageSync('isAgent');
    wx.removeStorageSync('agentInfo');
    wx.removeStorageSync('invitationCode');
    wx.removeStorageSync('referralCode');
    wx.removeStorageSync('boundPlatforms'); // 新增：清除绑定平台缓存
  },

  /**
   * 更新代理状态
   */
  updateAgentStatus(isAgent, invitationCode, agentInfo) {
    this.globalData.isAgent = isAgent;
    this.globalData.invitationCode = invitationCode;
    if (agentInfo) {
      this.globalData.agentInfo = agentInfo;
    }
    
    // 更新缓存
    wx.setStorageSync('isAgent', isAgent);
    wx.setStorageSync('invitationCode', invitationCode);
    if (agentInfo) {
      wx.setStorageSync('agentInfo', agentInfo);
    }
    
    // 更新用户信息
    if (this.globalData.userInfo) {
      this.globalData.userInfo.isAgent = isAgent;
      this.globalData.userInfo.invitationCode = invitationCode;
      wx.setStorageSync('userInfo', this.globalData.userInfo);
    }
  },

  globalData: {
    userInfo: null,
    token: null,
    isAgent: false,
    agentInfo: null,
    invitationCode: '',
    referralCode: '',
    boundPlatforms: [], // 新增：绑定的联盟平台列表
    apiUrl: 'http://localhost:8080'
  }
});
