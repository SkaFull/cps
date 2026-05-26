// pages/index/index.js
const request = require('../../utils/request.js');
const platformApi = require('../../api/platform-api.js');
const { PLATFORM_TYPES, getPlatformConfig } = require('../../config/platform.js');
const app = getApp();

Page({
  data: {
    currentPlatform: PLATFORM_TYPES.TBK, // 当前选中的平台
    platforms: [ // 平台列表（将根据用户权限动态加载）
      { key: PLATFORM_TYPES.TBK, name: '淘宝客' },
      { key: PLATFORM_TYPES.JD, name: '京东联盟' }
    ],
    availablePlatforms: [], // 用户可用的平台列表（从后端获取）
    platformSelectorOptions: [], // 下拉选择器选项
    selectedPlatformIndex: 0, // 选中的平台索引
    banners: [],
    categories: [],
    hotGoods: [],
    loading: false,
    refreshing: false,
    hasMore: true,
    page: 1,
    pageSize: 20,
    isAgent: false,
    invitationCode: '',
    showTpwdModal: false,
    currentTpwd: '',
    searchKeyword: '',
    lastToken: null, // 用于检测登录状态变化
    platformsLoaded: false, // 标记平台数据是否已成功加载
    hasShownNoPlatformTip: false, // 标记是否已显示过无平台提示（避免重复弹窗）
    safetyNotice: '安全提示：本平台为官方合作渠道，返利全部可提现，请放心使用！'
  },

  onLoad(options) {
    // 获取并保存初始状态
    this.setData({
      isAgent: app.globalData.isAgent,
      invitationCode: app.globalData.invitationCode,
      lastToken: app.globalData.token // 初始化lastToken，避免onShow误判
    });
    
    // 先加载用户可用的联盟平台列表
    this.loadAvailablePlatforms();
  },

  onShow() {
    // 从全局状态读取最新值
    const currentToken = app.globalData.token;
    const currentIsAgent = app.globalData.isAgent;
    const currentInvitationCode = app.globalData.invitationCode;
    
    // 详细的状态检测日志
    console.log('========== onShow 状态检测 ==========');
    console.log('上次token:', this.data.lastToken ? '存在' : '不存在');
    console.log('当前token:', currentToken ? '存在' : '不存在');
    console.log('上次isAgent:', this.data.isAgent);
    console.log('当前isAgent:', currentIsAgent);
    console.log('平台数据已加载:', this.data.platformsLoaded);
    console.log('可用平台数量:', this.data.availablePlatforms?.length || 0);
    console.log('====================================');
    
    // 判断各种状态变化场景
    const isFirstShow = this.data.lastToken === null;
    const isLogout = !isFirstShow && this.data.lastToken && !currentToken;
    // 【关键修复】退出后重新登录：lastToken 为 null（退出时被清除）但 currentToken 存在
    const isReloginAfterLogout = isFirstShow && !!currentToken;
    const tokenChanged = !isFirstShow && this.data.lastToken !== currentToken;
    const agentStatusChanged = !isFirstShow && this.data.isAgent !== currentIsAgent;
    
    // 检测代理用户是否需要重新加载平台数据
    // 关键修复：即使 platformsLoaded 为 true，如果平台列表为空也需要重新加载
    const isAgentWithoutPlatforms = currentToken && currentIsAgent && 
                                    !isFirstShow &&
                                    (!this.data.availablePlatforms || this.data.availablePlatforms.length === 0);
    
    console.log('→ isFirstShow:', isFirstShow, ', isLogout:', isLogout, ', isReloginAfterLogout:', isReloginAfterLogout);
    console.log('→ tokenChanged:', tokenChanged, ', agentStatusChanged:', agentStatusChanged);
    
    // 更新页面状态
    this.setData({
      isAgent: currentIsAgent,
      invitationCode: currentInvitationCode,
      lastToken: currentToken
    });
    
    // 根据不同场景处理数据加载
    if (isLogout) {
      // 场景1：用户登出
      console.log('→ 检测到用户登出，重置到默认状态');
      this.resetToDefaultState();
      this.loadAvailablePlatforms();
    } else if (isReloginAfterLogout) {
      // 场景2：退出后重新登录（lastToken 为 null 但 currentToken 存在）
      console.log('→ 检测到退出后重新登录，重新加载平台数据');
      this.setData({
        hotGoods: [],
        page: 1,
        hasMore: true,
        platformsLoaded: false,
        hasShownNoPlatformTip: false
      });
      this.loadAvailablePlatforms();
    } else if (tokenChanged || agentStatusChanged) {
      // 场景3：登录状态或代理状态变化
      console.log('→ 检测到状态变化，重新加载数据');
      this.setData({
        hotGoods: [],
        page: 1,
        hasMore: true,
        platformsLoaded: false  // 重置加载标记，确保重新加载
      });
      this.loadAvailablePlatforms();
    } else if (isAgentWithoutPlatforms) {
      // 场景4：代理用户但平台列表为空（可能之前加载失败或数据丢失）
      console.log('→ 检测到代理用户平台数据为空，重新加载');
      this.setData({
        platformsLoaded: false  // 重置标记以允许重新加载
      });
      this.loadAvailablePlatforms();
    }
  },

  /**
   * 重置为默认状态（用于登出场景）
   */
  resetToDefaultState() {
    this.setData({
      availablePlatforms: [],
      platformSelectorOptions: [],
      platforms: [],
      selectedPlatformIndex: 0,
      currentPlatform: PLATFORM_TYPES.TBK,
      hotGoods: [],
      page: 1,
      hasMore: true,
      isAgent: false,
      invitationCode: '',
      platformsLoaded: false,  // 重置平台加载标记
      hasShownNoPlatformTip: false  // 重置提示弹窗标记
    });
  },

  /**
   * 加载用户可用的联盟平台列表
   * 
   * 逻辑说明：
   * 1. 未登录用户 -> 从字典表获取系统默认联盟类型
   * 2. 已登录普通用户（非代理）-> 从字典表获取系统默认联盟类型
   * 3. 已登录代理用户 -> 查询用户绑定的联盟平台
   */
  loadAvailablePlatforms() {
    // 【优化8】直接从全局状态读取最新值，确保状态准确性
    // 因为 setData 是异步的，页面 data 可能还未更新
    const isLoggedIn = !!app.globalData.token;
    const isAgent = app.globalData.isAgent;
    
    // 【优化9】详细的状态日志，便于问题追踪
    console.log('========== 加载联盟平台列表 ==========');
    console.log('登录状态:', isLoggedIn ? '已登录' : '未登录');
    console.log('代理状态:', isAgent, '(类型:', typeof isAgent, ')');
    console.log('token:', app.globalData.token ? '存在' : '不存在');
    console.log('boundPlatforms缓存:', app.globalData.boundPlatforms ? app.globalData.boundPlatforms.length : 0);
    console.log('数据源:', this._determineDataSource(isLoggedIn, isAgent));
    console.log('====================================');
    
    // 【优化10】清晰的数据源判断逻辑
    if (this._shouldUseSystemDefault(isLoggedIn, isAgent)) {
      // 场景1: 未登录用户
      // 场景2: 已登录普通用户（非代理）
      this._loadSystemDefaultPlatforms(isLoggedIn);
    } else {
      // 场景3: 已登录代理用户
      this._loadAgentBoundPlatforms();
    }
  },

  /**
   * 判断是否应使用系统默认平台（字典表）
   */
  _shouldUseSystemDefault(isLoggedIn, isAgent) {
    return !isLoggedIn || !isAgent;
  },

  /**
   * 确定数据源（用于日志输出）
   */
  _determineDataSource(isLoggedIn, isAgent) {
    if (!isLoggedIn) {
      return '字典表（未登录用户）';
    } else if (!isAgent) {
      return '字典表（已登录普通用户）';
    } else {
      return '用户绑定平台（代理用户）';
    }
  },

  /**
   * 加载系统默认平台列表（从字典表）
   */
  _loadSystemDefaultPlatforms(isLoggedIn) {
    console.log('→ 从字典表获取系统默认联盟类型');
    console.log('  用户类型:', isLoggedIn ? '已登录普通用户' : '未登录用户');
    
    request.publicGet('/system/dict/data/type/union_platform_type')
      .then(res => {
        if (res.code === 200 && res.data && res.data.length > 0) {
          // 将字典数据转换为平台选择器需要的格式
          const platforms = res.data.map(dict => {
            return {
              key: this._mapDictValueToPlatformKey(dict.dictValue),
              name: dict.dictLabel,
              platformType: dict.dictValue,
              dictCode: dict.dictCode
            };
          });
          
          console.log('  成功获取', platforms.length, '个系统默认平台');
          this.setupPlatformSelector(platforms);
        } else {
          // 字典数据为空，使用默认平台
          console.warn('  字典表中没有联盟平台类型数据，使用默认配置');
          this.setDefaultPlatforms();
        }
      })
      .catch(err => {
        console.error('  从字典表获取联盟平台类型失败:', err);
        this.setDefaultPlatforms();
      })
      .finally(() => {
        this.loadIndexData();
      });
  },

  /**
   * 加载代理用户绑定的平台列表
   * 
   * 优化策略：优先使用登录时缓存的 boundPlatforms 数据，避免重复请求
   */
  _loadAgentBoundPlatforms() {
    // 优先使用登录时缓存的平台数据
    if (app.globalData.boundPlatforms && app.globalData.boundPlatforms.length > 0) {
      console.log('→ 使用登录时缓存的绑定平台数据');
      const platforms = app.globalData.boundPlatforms.map(platform => {
        return {
          key: this._mapDictValueToPlatformKey(platform.platformType),
          name: platform.platformName,
          platformType: platform.platformType,
          id: platform.id
        };
      });
      console.log('  从缓存获取', platforms.length, '个绑定平台');
      this.setupPlatformSelector(platforms);
      this.loadIndexData();
      return;
    }
    
    // 缓存为空时才调用接口
    console.log('→ 缓存为空，查询代理用户绑定的联盟平台');
    request.get('/system/unionPlatform/userBound')
      .then(res => {
        if (res.code === 200 && res.data && res.data.length > 0) {
          // 将用户绑定的平台数据转换为平台选择器需要的格式
          const platforms = res.data.map(platform => {
            return {
              key: this._mapDictValueToPlatformKey(platform.platformType),
              name: platform.platformName,
              platformType: platform.platformType,
              id: platform.id
            };
          });
          console.log('  从接口获取', platforms.length, '个绑定平台');
          
          // 更新全局缓存（补充缓存数据）
          app.globalData.boundPlatforms = res.data;
          wx.setStorageSync('boundPlatforms', res.data);
          
          this.setupPlatformSelector(platforms);
        } else {
          // 代理用户但没有绑定平台
          console.log('  代理用户暂无绑定的联盟平台');
          
          // 只在首次显示提示时弹窗，避免重复弹窗
          if (!this.data.hasShownNoPlatformTip) {
            wx.showModal({
              title: '温馨提示',
              content: '您的代理账号暂未分配联盟平台，请联系管理员进行分配。分配后即可查看商品和推广赚佣金。',
              showCancel: false,
              confirmText: '我知道了'
            });
            
            // 标记已显示过提示
            this.setData({
              hasShownNoPlatformTip: true
            });
          }
          
          // 关键修复：设置空平台列表，但不设置 platformsLoaded 为 true
          // 这样当用户切换页面再返回时，onShow 会检测到平台列表为空并重新加载
          this.setData({
            availablePlatforms: [],
            platformSelectorOptions: ['暂未分配联盟平台'],
            platforms: [],
            selectedPlatformIndex: 0
            // 注意：这里不设置 platformsLoaded: true，允许后续重新加载
          });
        }
      })
      .catch(err => {
        console.error('  获取用户绑定平台失败:', err);
        // 出错时降级为默认平台
        this.setDefaultPlatforms();
      })
      .finally(() => {
        this.loadIndexData();
      });
  },

  /**
   * 映射字典值到平台 key
   */
  _mapDictValueToPlatformKey(dictValue) {
    const mapping = {
      'jd': PLATFORM_TYPES.JD,
      'pdd': PLATFORM_TYPES.PDD,
      'dtk': PLATFORM_TYPES.DTK,
      'tbk': PLATFORM_TYPES.TBK,
      'dy': PLATFORM_TYPES.DY
    };
    return mapping[dictValue] || PLATFORM_TYPES.TBK; // 默认淘宝客
  },

  /**
   * 设置默认平台列表（当API调用失败时使用）
   */
  setDefaultPlatforms() {
    const defaultPlatforms = [
      { key: PLATFORM_TYPES.TBK, name: '淘宝客', platformType: 'tbk' },
      { key: PLATFORM_TYPES.JD, name: '京东联盟', platformType: 'jd' }
    ];
    this.setData({
      platforms: defaultPlatforms,
      availablePlatforms: defaultPlatforms,
      platformSelectorOptions: defaultPlatforms.map(p => p.name),
      selectedPlatformIndex: 0,
      currentPlatform: defaultPlatforms[0].key
    });
  },

  /**
   * 设置平台选择器
   */
  setupPlatformSelector(platformList) {
    // 将后端返回的平台数据转换为前端需要的格式
    const platforms = platformList.map(platform => {
      // 映射平台类型到前端常量
      let key = PLATFORM_TYPES.TBK; // 默认淘宝客
      if (platform.platformType === 'jd') {
        key = PLATFORM_TYPES.JD;
      } else if (platform.platformType === 'pdd') {
        key = PLATFORM_TYPES.PDD;
      } else if (platform.platformType === 'dtk') {
        key = PLATFORM_TYPES.DTK;
      } else if (platform.platformType === 'dy') {
        key = PLATFORM_TYPES.DY;
      }
      
      return {
        key: key,
        name: platform.name,  // 使用 platform.name（已在调用处映射好）
        platformType: platform.platformType,
        id: platform.id
      };
    });
    
    // 构建下拉选择器选项
    const selectorOptions = platforms.map(p => p.name);
    
    console.log('设置平台选择器 - 平台列表:', platforms);
    console.log('设置平台选择器 - 选择器选项:', selectorOptions);
    
    this.setData({
      platforms: platforms,
      availablePlatforms: platforms,
      platformSelectorOptions: selectorOptions,
      selectedPlatformIndex: 0,
      currentPlatform: platforms.length > 0 ? platforms[0].key : PLATFORM_TYPES.TBK,
      platformsLoaded: true  // 标记平台数据已成功加载
    });
    
    console.log('平台选择器设置完成 - 当前平台:', this.data.currentPlatform);
    console.log('平台选择器设置完成 - 可用平台数:', platforms.length);
  },

  /**
   * 加载首页数据
   */
  loadIndexData() {
    // 不在这里统一设置loading，让每个子方法自己管理
    Promise.all([
      this.loadBanners(),
      this.loadCategories(),
      this.loadHotGoods(true) // 传入true表示刷新
    ]);
  },

  /**
   * 加载轮播图
   */
  loadBanners() {
    return request.publicGet('/miniapp/tbk/banners')
      .then(res => {
        if (res.code === 200) {
          this.setData({
            banners: res.data || []
          });
        }
      })
      .catch(err => {
        console.error('加载轮播图失败:', err);
      });
  },

  /**
   * 加载分类
   */
  loadCategories() {
    // 在小程序端直接定义分类图标本地路径
    const categoryIconMap = {
      16: '/images/category/nvzhuang.png',      // 女装
      30: '/images/category/nanzhuang.png',     // 男装
      50010788: '/images/category/meizhuang.png', // 美妆
      50013864: '/images/category/shuma.png',   // 数码
      21: '/images/category/jiaju.png',         // 家居
      50016422: '/images/category/meishi.png',  // 美食
      50014812: '/images/category/muying.png',  // 母婴
      50013199: '/images/category/yundong.png'  // 运动
    };
    
    return request.publicGet('/miniapp/tbk/categories')
      .then(res => {
        if (res.code === 200) {
          // 后端只返回ID和名称，在前端添加本地图标路径
          const categories = (res.data || []).map(cat => ({
            ...cat,
            iconUrl: categoryIconMap[cat.id] || '/images/category/default.png'
          }));
          
          this.setData({
            categories: categories
          });
        }
      })
      .catch(err => {
        console.error('加载分类失败:', err);
      });
  },

  /**
   * 加载热门商品（使用新的多平台架构）
   */
  loadHotGoods(isRefresh = false) {
    if (this.data.loading && !isRefresh) return;
    
    const page = isRefresh ? 1 : this.data.page;
    
    this.setData({ 
      loading: true,
      refreshing: isRefresh 
    });
    
    // 构建请求参数
    const params = {
      page: page,
      pageSize: this.data.pageSize
    };
    
    // 如果有搜索关键词，添加到请求参数
    if (this.data.searchKeyword) {
      params.keyword = this.data.searchKeyword;
    }
    
    // 如果有推荐人邀请码，添加到请求参数
    if (app.globalData.referralCode) {
      params.invitationCode = app.globalData.referralCode;
    }
    
    // 如果用户已登录且没有推荐人邀请码，传递用户ID用于查询代理信息
    if (!params.invitationCode && app.globalData.userInfo && app.globalData.userInfo.userId) {
      params.userId = app.globalData.userInfo.userId;
    }
    
    // 使用新的平台API获取商品列表
    return platformApi.getGoodsList(this.data.currentPlatform, params)
      .then(newGoods => {
        const platformConfig = getPlatformConfig(this.data.currentPlatform);
        const platformName = platformConfig ? platformConfig.name : '未知平台';
        console.log(`${platformName}商品列表:`, newGoods);
        
        const goods = isRefresh ? newGoods : [...this.data.hotGoods, ...newGoods];
        
        this.setData({
          hotGoods: goods,
          page: page,
          hasMore: newGoods.length >= this.data.pageSize,
          loading: false,
          refreshing: false
        });
      })
      .catch(err => {
        console.error('加载热门商品失败:', err);
        wx.showToast({
          title: err.message || '加载失败，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        this.setData({ 
          loading: false,
          refreshing: false 
        });
      });
  },

  /**
   * 平台选择器改变事件
   */
  onPlatformSelectorChange(e) {
    const index = e.detail.value;
    const platforms = this.data.platforms;
    
    if (index < 0 || index >= platforms.length) return;
    
    const selectedPlatform = platforms[index];
    console.log('选择平台:', selectedPlatform);
    
    this.setData({
      selectedPlatformIndex: index,
      currentPlatform: selectedPlatform.key,
      page: 1,
      hotGoods: [],
      hasMore: true
    });
    
    // 重新加载数据
    this.loadHotGoods(true);
  },

  /**
   * 下拉刷新
   */
  onPullDownRefresh() {
    this.loadIndexData();
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
    this.loadHotGoods();
  },

  /**
   * 轮播图点击
   */
  onBannerTap(e) {
    const banner = e.currentTarget.dataset.banner;
    if (banner.linkType === 'goods') {
      wx.navigateTo({
        url: `/pages/goods/detail/detail?id=${banner.linkId}`
      });
    } else if (banner.linkType === 'category') {
      wx.navigateTo({
        url: `/pages/goods/list/list?categoryId=${banner.linkId}`
      });
    }
  },

  /**
   * 分类点击
   */
  onCategoryTap(e) {
    const category = e.currentTarget.dataset.category;
    wx.navigateTo({
      url: `/pages/goods/list/list?categoryId=${category.id}`
    });
  },

  /**
   * 搜索框点击
   */
  onSearchTap() {
    // 点击搜索框不做任何操作，让用户可以直接输入
  },

  /**
   * 搜索确认
   */
  onSearchConfirm(e) {
    const keyword = e.detail.value;
    if (keyword && keyword.trim()) {
      // 设置搜索关键词并刷新商品列表
      this.setData({
        searchKeyword: keyword.trim(),
        page: 1,
        hotGoods: []
      });
      this.loadHotGoods(true);
    } else {
      // 如果关键词为空，清空搜索并重新加载热门商品
      this.setData({
        searchKeyword: '',
        page: 1,
        hotGoods: []
      });
      this.loadHotGoods(true);
    }
  },

  /**
   * 商品点击
   */
  onGoodsTap(e) {
    const goods = e.currentTarget.dataset.goods;
    
    // 将完整商品数据存入全局数据，供详情页使用
    // 这样详情页可以直接使用列表页已获取的 dgMaterialOptionalUpgrade 数据
    // 避免重复查询，提升性能和准确性
    app.globalData.currentGoods = goods;
    
    wx.navigateTo({
      url: `/pages/goods/detail/detail?id=${goods.id}&platform=${this.data.currentPlatform}`
    });
  },

  /**
   * 更多按钮点击
   */
  onMoreTap() {
    console.log('onMoreTap 被触发');
    console.log('searchKeyword:', this.data.searchKeyword);
    
    // 如果有搜索关键词，保存到全局数据供商品列表页使用
    if (this.data.searchKeyword && this.data.searchKeyword.trim()) {
      app.globalData.searchKeywordFromIndex = this.data.searchKeyword.trim();
      console.log('保存搜索关键词到globalData:', app.globalData.searchKeywordFromIndex);
    } else {
      // 清空全局搜索关键词
      app.globalData.searchKeywordFromIndex = '';
    }
    
    // 使用switchTab跳转到tabBar页面
    console.log('切换到商品列表tabBar');
    wx.switchTab({
      url: '/pages/goods/list/list',
      success: function() {
        console.log('切换成功');
      },
      fail: function(err) {
        console.error('切换失败:', err);
      }
    });
  },

  /**
   * 获取淘口令/京口令（使用统一的平台API）
   */
  onGetTpwd(e) {
    const goods = e.currentTarget.dataset.goods;
    const currentPlatform = this.data.currentPlatform;
    
    wx.showLoading({
      title: '生成中...',
      mask: true
    });
    
    // 构建基础请求参数
    const params = {
      platform: currentPlatform,
      goodsId: goods.id
    };
    
    // 根据平台类型添加特定参数
    if (currentPlatform === PLATFORM_TYPES.JD) {
      // 京东联盟需要itemId
      params.itemId = goods.id;
    } else if (currentPlatform === PLATFORM_TYPES.PDD) {
      // 拼多多需要goodsSign参数
      params.goodsSign = goods.goodsSign || goods.id;
    } else if (currentPlatform === PLATFORM_TYPES.DTK) {
      // 大淘客需要goodsId、couponId和商品名称（用于设置淘口令右符号）
      params.couponId = goods.couponId || '';
      params.goodsName = goods.title || '';
    } else {
      // 淘宝客需要文案、链接和图片
      let itemUrl = goods.clickUrl;
      if (goods.couponShareUrl) {
        itemUrl = goods.couponShareUrl;
      }
      
      // 确保URL以https:开头
      if (itemUrl && !itemUrl.startsWith('http')) {
        itemUrl = 'https:' + itemUrl;
      }
      
      params.text = goods.title;
      params.url = itemUrl;
      params.logo = goods.mainPic;
    }
    
    // 如果有推荐人邀请码，添加到请求参数
    if (app.globalData.referralCode) {
      params.invitationCode = app.globalData.referralCode;
    } else if (app.globalData.userInfo && app.globalData.userInfo.userId) {
      // 如果没有邀请码，但用户已登录，传入用户ID
      params.userId = app.globalData.userInfo.userId;
    }
    
    // 调用统一的getTpwd接口
    platformApi.getTpwd(currentPlatform, params)
      .then(res => {
        wx.hideLoading();
        
        // 处理不同平台的响应格式
        let tpwd = '';
        let tpwdType = '';
        
        if (currentPlatform === PLATFORM_TYPES.JD) {
          // 京东联盟返回推广链接
          if (res.data && res.data.clickURL) {
            tpwd = res.data.clickURL;
            tpwdType = 'jd';
          }
        } else if (currentPlatform === PLATFORM_TYPES.PDD) {
          // 拼多多返回推广短链
          if (res.data && res.data.mobileShortUrl) {
            tpwd = res.data.mobileShortUrl;
            tpwdType = 'pdd';
          } else if (res.data && res.data.promotionUrl) {
            tpwd = res.data.promotionUrl;
            tpwdType = 'pdd';
          }
        } else if (currentPlatform === PLATFORM_TYPES.DTK) {
          // 大淘客返回淘口令
          if (res.data && res.data.data && res.data.data.model) {
            tpwd = res.data.data.model;
            tpwdType = 'dtk';
          }
        } else {
          // 淘宝客返回淘口令
          if (res.data && res.data.data && res.data.data.model) {
            tpwd = res.data.data.model;
            tpwdType = 'tbk';
          }
        }
        
        if (tpwd) {
          // 复制到剪贴板并显示弹窗
          wx.setClipboardData({
            data: tpwd,
            success: () => {
              this.setData({
                showTpwdModal: true,
                currentTpwd: tpwd,
                tpwdType: tpwdType
              });
            }
          });
        } else {
          wx.showToast({
            title: '获取失败',
            icon: 'none'
          });
        }
      })
      .catch(err => {
        wx.hideLoading();
        console.error('获取淘口令失败:', err);
        wx.showToast({
          title: err.message || '获取失败，请重试',
          icon: 'none'
        });
      });
  },

  /**
   * 关闭淘口令弹窗
   */
  onCloseTpwdModal() {
    this.setData({
      showTpwdModal: false,
      currentTpwd: ''
    });
  },

  /**
   * 复制京东链接
   */
  onCopyTpwd() {
    const tpwd = this.data.currentTpwd;
    if (!tpwd) {
      wx.showToast({
        title: '链接内容为空',
        icon: 'none'
      });
      return;
    }

    wx.setClipboardData({
      data: tpwd,
      success: () => {
        wx.showToast({
          title: '复制成功',
          icon: 'success',
          duration: 1500
        });
        // 复制成功后关闭弹窗
        this.onCloseTpwdModal();
      },
      fail: () => {
        wx.showToast({
          title: '复制失败，请重试',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 阻止事件冒泡
   */
  stopPropagation() {
    // 阻止点击弹窗内容时关闭弹窗
  },

  /**
   * 申请代理
   */
  onApplyAgent() {
    if (!app.globalData.token) {
      wx.navigateTo({
        url: '/pages/auth/login/login'
      });
      return;
    }
    
    wx.navigateTo({
      url: '/pages/agent/apply/apply'
    });
  },

  /**
   * 分享
   */
  onShareAppMessage() {
    const shareData = {
      title: '实佣有你平台 - 优质商品·高额佣金',
      path: '/pages/index/index'
    };
    
    // 如果是代理，添加邀请码
    if (this.data.isAgent && this.data.invitationCode) {
      shareData.path += `?invitationCode=${this.data.invitationCode}`;
      shareData.title = '我在实佣有你发现好货，快来看看吧！';
    }
    
    return shareData;
  }
});
