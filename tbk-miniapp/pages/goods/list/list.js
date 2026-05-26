// pages/goods/list/list.js - 收益分析页面
const request = require('../../../utils/request.js');
const { PLATFORM_TYPES } = require('../../../config/platform.js');
const app = getApp();

Page({
  data: {
    // 时间筛选
    timeRange: 'today', // today, yesterday, last3days, last7days, last30days, custom
    timeRangeText: '今天',
    showTimeMenu: false,
    customStartDate: '',
    customEndDate: '',
    
    // 收益数据
    incomeData: {
      validOrderAmount: 0,    // 有效成交金额
      validOrderCount: 0,      // 有效订单
      validIncome: 0,          // 有效收益
      estimatedIncome: 0,      // 预估今日收益
      totalSalesAmount: 0,     // 总销售额
      totalOrderCount: 0,      // 总订单数
      estimatedTotalIncome: 0, // 预估收益
      refundSalesAmount: 0,    // 退款销售额
      refundOrderCount: 0,     // 退款订单
      refundRate: 0,           // 退款率(%)
      violationSalesAmount: 0, // 违规成交金额
      violationOrderCount: 0,  // 违规订单
      violationIncome: 0       // 违规收益
    },
    
    // 订单明细
    orderList: [],
    
    // 联盟类型筛选（与首页保持一致的数据结构）
    allianceFilter: 'all', // all, 或使用 platformType: 'tbk', 'jd', 'dy' 等
    allianceOptions: [], // 动态加载的联盟类型选项
    availablePlatforms: [], // 用户可用的平台列表（从首页逻辑同步）
    showAllianceMenu: false,
    
    // 状态筛选
    statusFilter: 'all', // all(全部状态), paid(已付款), confirmed(确认收货), invalid(已失效), rights(维权订单), violation(违规订单)
    showStatusMenu: false,
    
    // 数据说明弹窗
    showDataExplainModal: false,
    
    // 收益统计展开/收起
    incomeExpanded: false,
    
    // 日期选择器
    showDatePicker: false,
    calendarYear: 2026,
    calendarMonth: 3,
    calendarDays: [],
    selectedStartDate: null,
    selectedEndDate: null,
    selectingRange: false, // 是否正在选择范围
    
    loading: false,
    hasMore: true,
    page: 1,
    pageSize: 20,
    
    // 登录状态追踪
    lastToken: null,
    platformsLoaded: false
  },

  onLoad(options) {
    // 保存初始登录状态
    this.setData({
      lastToken: app.globalData.token
    });
    
    // 先加载用户可用的联盟平台列表
    this.loadAvailablePlatforms();
  },

  onShow() {
    // 检测登录状态变化
    const currentToken = app.globalData.token;
    const isLogout = this.data.lastToken && !currentToken;
    const tokenChanged = this.data.lastToken !== currentToken;
    
    // 更新token状态
    this.setData({
      lastToken: currentToken
    });
    
    // 如果登录状态变化，重新加载平台列表
    if (isLogout || tokenChanged) {
      console.log('收益页面检测到登录状态变化，重新加载平台数据');
      this.setData({
        platformsLoaded: false,
        orderList: [],
        page: 1,
        hasMore: true
      });
      this.loadAvailablePlatforms();
    } else {
      // 正常刷新数据
      this.loadIncomeData();
    }
  },

  /**
   * 加载用户可用的联盟平台列表
   * 与首页逻辑保持一致：
   * 1. 未登录用户 -> 从字典表获取系统默认联盟类型
   * 2. 已登录普通用户（非代理）-> 从字典表获取系统默认联盟类型
   * 3. 已登录代理用户 -> 查询用户绑定的联盟平台
   */
  loadAvailablePlatforms() {
    const isLoggedIn = !!app.globalData.token;
    const isAgent = app.globalData.isAgent;
    
    console.log('========== 收益页面加载联盟平台列表 ==========');
    console.log('登录状态:', isLoggedIn ? '已登录' : '未登录');
    console.log('代理状态:', isAgent);
    console.log('====================================');
    
    if (!isLoggedIn || !isAgent) {
      // 未登录或普通用户：从字典表获取系统默认联盟类型
      this._loadSystemDefaultPlatforms(isLoggedIn);
    } else {
      // 代理用户：查询用户绑定的联盟平台
      this._loadAgentBoundPlatforms();
    }
  },

  /**
   * 加载系统默认平台列表（从字典表）
   */
  _loadSystemDefaultPlatforms(isLoggedIn) {
    console.log('→ 从字典表获取系统默认联盟类型');
    
    request.publicGet('/system/dict/data/type/union_platform_type')
      .then(res => {
        if (res.code === 200 && res.data && res.data.length > 0) {
          const platforms = res.data.map(dict => ({
            platformType: dict.dictValue, // 'tbk', 'jd', 'dy' 等
            name: dict.dictLabel,
            dictCode: dict.dictCode
          }));
          
          console.log('  成功获取', platforms.length, '个系统默认平台');
          this.setupAllianceOptions(platforms);
        } else {
          console.warn('  字典表中没有联盟平台类型数据');
          this.setDefaultAllianceOptions();
        }
      })
      .catch(err => {
        console.error('  从字典表获取联盟平台类型失败:', err);
        this.setDefaultAllianceOptions();
      })
      .finally(() => {
        this.loadIncomeData();
        this.loadOrderList();
      });
  },

  /**
   * 加载代理用户绑定的平台列表
   */
  _loadAgentBoundPlatforms() {
    // 优先使用登录时缓存的平台数据
    if (app.globalData.boundPlatforms && app.globalData.boundPlatforms.length > 0) {
      console.log('→ 使用登录时缓存的绑定平台数据');
      const platforms = app.globalData.boundPlatforms.map(platform => ({
        platformType: platform.platformType, // 'tbk', 'jd', 'dy' 等
        name: platform.platformName,
        id: platform.id
      }));
      console.log('  从缓存获取', platforms.length, '个绑定平台');
      this.setupAllianceOptions(platforms);
      this.loadIncomeData();
      this.loadOrderList();
      return;
    }
    
    // 缓存为空时调用接口
    console.log('→ 查询代理用户绑定的联盟平台');
    request.get('/system/unionPlatform/userBound')
      .then(res => {
        if (res.code === 200 && res.data && res.data.length > 0) {
          const platforms = res.data.map(platform => ({
            platformType: platform.platformType,
            name: platform.platformName,
            id: platform.id
          }));
          console.log('  从接口获取', platforms.length, '个绑定平台');
          
          // 更新全局缓存
          app.globalData.boundPlatforms = res.data;
          wx.setStorageSync('boundPlatforms', res.data);
          
          this.setupAllianceOptions(platforms);
        } else {
          console.log('  代理用户暂无绑定的联盟平台');
          this.setDefaultAllianceOptions();
        }
      })
      .catch(err => {
        console.error('  获取用户绑定平台失败:', err);
        this.setDefaultAllianceOptions();
      })
      .finally(() => {
        this.loadIncomeData();
        this.loadOrderList();
      });
  },

  /**
   * 设置联盟类型选项
   */
  setupAllianceOptions(platforms) {
    // 构建联盟类型选项（添加"全部"选项）
    const allianceOptions = [
      { platformType: 'all', name: '全部' },
      ...platforms
    ];
    
    this.setData({
      availablePlatforms: platforms,
      allianceOptions: allianceOptions,
      platformsLoaded: true
    });
    
    console.log('联盟类型选项设置完成:', allianceOptions);
  },

  /**
   * 设置默认联盟类型选项
   */
  setDefaultAllianceOptions() {
    const defaultOptions = [
      { platformType: 'all', name: '全部' },
      { platformType: 'tbk', name: '淘宝客' },
      { platformType: 'jd', name: '京东' },
      { platformType: 'dy', name: '抖音' }
    ];
    
    this.setData({
      allianceOptions: defaultOptions,
      availablePlatforms: defaultOptions.slice(1), // 去掉"全部"选项
      platformsLoaded: true
    });
  },

  /**
   * 加载收益数据
   */
  loadIncomeData() {
    const params = {
      timeRange: this.data.timeRange
    };
    
    if (this.data.timeRange === 'custom') {
      params.startDate = this.data.customStartDate;
      params.endDate = this.data.customEndDate;
    }
    
    // 如果用户已登录，传入用户ID
    if (app.globalData.userInfo && app.globalData.userInfo.userId) {
      params.userId = app.globalData.userInfo.userId;
    }
    
    request.publicGet('/miniapp/income/summary', params)
      .then(res => {
        if (res.code === 200 && res.data) {
          this.setData({
            incomeData: {
              validOrderAmount: res.data.validOrderAmount || 0,
              validOrderCount: res.data.validOrderCount || 0,
              validIncome: res.data.validIncome || 0,
              estimatedIncome: res.data.estimatedIncome || 0,
              totalSalesAmount: res.data.totalSalesAmount || 0,
              totalOrderCount: res.data.totalOrderCount || 0,
              estimatedTotalIncome: res.data.estimatedTotalIncome || 0,
              refundSalesAmount: res.data.refundSalesAmount || 0,
              refundOrderCount: res.data.refundOrderCount || 0,
              refundRate: res.data.refundRate || 0,
              violationSalesAmount: res.data.violationSalesAmount || 0,
              violationOrderCount: res.data.violationOrderCount || 0,
              violationIncome: res.data.violationIncome || 0
            }
          });
        }
      })
      .catch(err => {
        console.error('加载收益数据失败:', err);
      });
  },

  /**
   * 加载订单列表
   */
  loadOrderList(isRefresh = false) {
    if (this.data.loading && !isRefresh) return;
    
    const page = isRefresh ? 1 : this.data.page;
    this.setData({ loading: true });
    
    const params = {
      page: page,
      pageSize: this.data.pageSize,
      timeRange: this.data.timeRange
    };
    
    if (this.data.timeRange === 'custom') {
      params.startDate = this.data.customStartDate;
      params.endDate = this.data.customEndDate;
    }
    
    // 联盟类型参数（与首页保持一致，使用 platformType）
    if (this.data.allianceFilter !== 'all') {
      params.platformType = this.data.allianceFilter;
    }
    
    if (this.data.statusFilter !== 'all') {
      params.status = this.data.statusFilter;
    }
    
    // 如果用户已登录，传入用户ID
    if (app.globalData.userInfo && app.globalData.userInfo.userId) {
      params.userId = app.globalData.userInfo.userId;
    }
    
    request.publicGet('/miniapp/income/orders', params)
      .then(res => {
        if (res.code === 200 && res.data) {
          const newOrders = res.data.list || [];
          const orders = isRefresh ? newOrders : [...this.data.orderList, ...newOrders];
          
          this.setData({
            orderList: orders,
            page: page,
            hasMore: newOrders.length >= this.data.pageSize,
            loading: false
          });
        } else {
          this.setData({ loading: false });
        }
      })
      .catch(err => {
        console.error('加载订单列表失败:', err);
        this.setData({ loading: false });
      });
  },

  /**
   * 切换时间范围
   */
  toggleTimeMenu() {
    this.setData({
      showTimeMenu: !this.data.showTimeMenu
    });
  },

  /**
   * 选择时间范围
   */
  selectTimeRange(e) {
    const range = e.currentTarget.dataset.range;
    const rangeTextMap = {
      'today': '今天',
      'yesterday': '昨天',
      'last3days': '近3天',
      'last7days': '近7天',
      'last30days': '近30天',
      'custom': '自定义'
    };
    
    this.setData({
      timeRange: range,
      timeRangeText: rangeTextMap[range],
      showTimeMenu: false,
      page: 1,
      orderList: []
    });
    
    if (range === 'custom') {
      // 显示日期选择器
      this.showDatePicker();
    } else {
      this.loadIncomeData();
      this.loadOrderList(true);
    }
  },

  /**
   * 显示日期选择器
   */
  showDatePicker() {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    
    this.setData({
      showDatePicker: true,
      calendarYear: year,
      calendarMonth: month,
      selectedStartDate: null,
      selectedEndDate: null,
      selectingRange: false
    });
    
    this.generateCalendar(year, month);
  },

  /**
   * 关闭日期选择器
   */
  closeDatePicker() {
    this.setData({
      showDatePicker: false,
      selectedStartDate: null,
      selectedEndDate: null,
      selectingRange: false
    });
  },

  /**
   * 生成日历
   */
  generateCalendar(year, month) {
    const firstDay = new Date(year, month - 1, 1);
    const lastDay = new Date(year, month, 0);
    const daysInMonth = lastDay.getDate();
    const startWeekDay = firstDay.getDay(); // 0-6, 0是周日
    
    const days = [];
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const maxDate = new Date(today);
    maxDate.setMonth(maxDate.getMonth() + 2); // 最多两个月后
    
    // 添加上个月的日期填充
    const prevMonthDays = new Date(year, month - 1, 0).getDate();
    for (let i = startWeekDay - 1; i >= 0; i--) {
      days.push({
        day: prevMonthDays - i,
        disabled: true,
        isOtherMonth: true
      });
    }
    
    // 添加当月日期
    for (let i = 1; i <= daysInMonth; i++) {
      const currentDate = new Date(year, month - 1, i);
      const isDisabled = currentDate > maxDate || currentDate < today;
      
      days.push({
        day: i,
        disabled: isDisabled,
        isOtherMonth: false,
        dateString: `${year}-${String(month).padStart(2, '0')}-${String(i).padStart(2, '0')}`
      });
    }
    
    // 添加下个月的日期填充
    const remainingDays = 42 - days.length; // 6行 * 7列
    for (let i = 1; i <= remainingDays; i++) {
      days.push({
        day: i,
        disabled: true,
        isOtherMonth: true
      });
    }
    
    this.setData({
      calendarDays: days
    });
  },

  /**
   * 切换月份
   */
  changeMonth(e) {
    const direction = e.currentTarget.dataset.direction;
    let { calendarYear, calendarMonth } = this.data;
    
    if (direction === 'prev') {
      calendarMonth--;
      if (calendarMonth < 1) {
        calendarMonth = 12;
        calendarYear--;
      }
    } else {
      calendarMonth++;
      if (calendarMonth > 12) {
        calendarMonth = 1;
        calendarYear++;
      }
    }
    
    this.setData({
      calendarYear,
      calendarMonth
    });
    
    this.generateCalendar(calendarYear, calendarMonth);
  },

  /**
   * 选择日期
   */
  selectDate(e) {
    const { index } = e.currentTarget.dataset;
    const day = this.data.calendarDays[index];
    
    if (day.disabled || day.isOtherMonth) return;
    
    const selectedDate = day.dateString;
    const { selectedStartDate, selectedEndDate } = this.data;
    
    // 如果已经选择了起止日期,重新开始选择
    if (selectedStartDate && selectedEndDate) {
      this.setData({
        selectedStartDate: selectedDate,
        selectedEndDate: null,
        selectingRange: true
      });
      return;
    }
    
    // 如果只选择了开始日期
    if (selectedStartDate && !selectedEndDate) {
      const start = new Date(selectedStartDate);
      const end = new Date(selectedDate);
      
      // 确保结束日期在开始日期之后
      if (end < start) {
        this.setData({
          selectedStartDate: selectedDate,
          selectedEndDate: null
        });
        return;
      }
      
      // 检查日期范围是否超过两个月
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      
      if (diffDays > 60) {
        wx.showToast({
          title: '日期范围不能超过两个月',
          icon: 'none'
        });
        return;
      }
      
      this.setData({
        selectedEndDate: selectedDate,
        selectingRange: false
      });
    } else {
      // 首次选择开始日期
      this.setData({
        selectedStartDate: selectedDate,
        selectedEndDate: null,
        selectingRange: true
      });
    }
  },

  /**
   * 确认选择日期
   */
  confirmDateSelection() {
    const { selectedStartDate, selectedEndDate } = this.data;
    
    if (!selectedStartDate || !selectedEndDate) {
      wx.showToast({
        title: '请选择起止日期',
        icon: 'none'
      });
      return;
    }
    
    this.setData({
      timeRange: 'custom',
      customStartDate: selectedStartDate,
      customEndDate: selectedEndDate,
      showDatePicker: false,
      page: 1,
      orderList: []
    });
    
    this.loadIncomeData();
    this.loadOrderList(true);
  },

  /**
   * 切换联盟类型筛选
   */
  toggleAllianceMenu() {
    this.setData({
      showAllianceMenu: !this.data.showAllianceMenu,
      showStatusMenu: false
    });
  },

  /**
   * 选择联盟类型
   */
  selectAlliance(e) {
    const filter = e.currentTarget.dataset.filter; // platformType: 'all', 'tbk', 'jd', 'dy' 等
    console.log('选择联盟类型:', filter);
    
    this.setData({
      allianceFilter: filter,
      showAllianceMenu: false,
      page: 1
    });
    this.loadOrderList(true);
  },

  /**
   * 切换状态筛选
   */
  toggleStatusMenu() {
    this.setData({
      showStatusMenu: !this.data.showStatusMenu,
      showAllianceMenu: false
    });
  },

  /**
   * 选择状态
   */
  selectStatus(e) {
    const filter = e.currentTarget.dataset.filter;
    this.setData({
      statusFilter: filter,
      showStatusMenu: false,
      page: 1
    });
    this.loadOrderList(true);
  },

  /**
   * 显示数据说明弹窗
   */
  showDataExplain() {
    this.setData({
      showDataExplainModal: true
    });
  },

  /**
   * 关闭数据说明弹窗
   */
  closeDataExplain() {
    this.setData({
      showDataExplainModal: false
    });
  },

  /**
   * 切换收益统计展开/收起
   */
  toggleIncomeExpand() {
    this.setData({
      incomeExpanded: !this.data.incomeExpanded
    });
  },

  /**
   * 下拉刷新
   */
  onPullDownRefresh() {
    this.loadIncomeData();
    this.loadOrderList(true);
    setTimeout(() => {
      wx.stopPullDownRefresh();
    }, 1000);
  },

  /**
   * 上拉加载更多
   */
  onReachBottom() {
    if (!this.data.hasMore || this.data.loading) return;
    
    this.setData({ 
      page: this.data.page + 1 
    });
    this.loadOrderList();
  },

  /**
   * 分享
   */
  onShareAppMessage() {
    const shareData = {
      title: '收益分析',
      path: '/pages/goods/list/list'
    };
    
    // 如果是代理，添加邀请码
    if (app.globalData.isAgent && app.globalData.invitationCode) {
      shareData.path += `?invitationCode=${app.globalData.invitationCode}`;
    }
    
    return shareData;
  }
});
