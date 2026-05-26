// pages/goods/detail/detail.js
const request = require('../../../utils/request.js');
const { PLATFORM_TYPES } = require('../../../config/platform.js');
const platformApi = require('../../../api/platform-api.js');
const app = getApp();

Page({
  data: {
    goodsId: '',
    goods: null,
    loading: true,
    currentImageIndex: 0,
    isAgent: false,
    invitationCode: '',
    showTpwdModal: false,
    currentTpwd: '',
    currentPlatform: PLATFORM_TYPES.TBK // 当前平台，默认淘宝客
  },

  onLoad(options) {
    if (options.id) {
      // 接收平台参数，如果没有传递则默认为淘宝客
      const platform = options.platform || PLATFORM_TYPES.TBK;
      
      this.setData({
        goodsId: options.id,
        currentPlatform: platform,
        isAgent: app.globalData.isAgent,
        invitationCode: app.globalData.invitationCode
      });
      
      // 检查是否从分享链接传递了关键数据
      if (options.price || options.monthSales || options.commission) {
        console.log('检测到分享链接传递的数据，使用URL参数构建商品信息');
        // 从URL参数构建商品基本信息，避免API查询
        const shareGoods = {
          id: options.id,
          title: decodeURIComponent(options.title || ''),
          mainPic: decodeURIComponent(options.pic || ''),
          actualPrice: options.price || '0',
          originalPrice: options.originalPrice || options.price || '0',
          monthSales: options.monthSales || '0',
          commission: options.commission || '0',
          commissionRate: options.commissionRate || '0',
          platform: platform
        };
        
        // 从URL参数中获取推广链接（如果有）
        if (options.couponShareUrl) {
          shareGoods.couponShareUrl = decodeURIComponent(options.couponShareUrl);
          console.log('从分享链接获取到couponShareUrl');
        }
        if (options.clickUrl) {
          shareGoods.clickUrl = decodeURIComponent(options.clickUrl);
          console.log('从分享链接获取到clickUrl');
        }
        
        // 将分享数据存入全局数据，供loadGoodsDetail使用
        app.globalData.currentGoods = shareGoods;
      }
      
      this.loadGoodsDetail();
    }
  },

  /**
   * 加载商品详情
   */
  loadGoodsDetail() {
    // 优先使用全局数据中的商品信息（从列表页传递过来）
    let cachedGoods = null; // 保存缓存数据用于兜底
    if (app.globalData.currentGoods) {
      cachedGoods = app.globalData.currentGoods;
      const cachedPlatform = cachedGoods.platform || this.data.currentPlatform;
      
      // 京东商品：必须调用后端接口获取detailImages等大字段信息
      if (cachedPlatform === PLATFORM_TYPES.JD) {
        console.log('检测到京东商品，调用后端接口获取完整详情（包括detailImages）');
        console.log('保留列表页数据作为价格、月销、佣金的兜底数据');
        // 保留缓存数据供后续使用，不清除
        // 在构建goods对象时，如果后端返回数据为空，使用缓存数据作为兜底
      } else {
        // 淘宝客商品：使用缓存数据，无需查询后端
        console.log('使用列表页传递的淘宝客商品数据，无需查询后端');
        const cachedSmallImages = cachedGoods.smallImages
        // 清除全局数据，避免影响下次访问
        app.globalData.currentGoods = null;
      // 将列表页数据适配为详情页需要的格式
      // 组合主图和小图列表
      let images = [];
      if (cachedGoods.mainPic) {
        images.push(cachedGoods.mainPic);
      }
      // 如果有 smallImages，添加到图片列表中（去重）
      if (cachedSmallImages) {
        // 将 smallImages 转换为数组（可能是字符串或数组）
        let smallImagesArray = [];
        if (typeof cachedSmallImages === 'string') {
          // 如果是字符串，按逗号分割
          smallImagesArray = cachedSmallImages.split(',').map(url => url.trim()).filter(url => url);
        } else if (Array.isArray(cachedSmallImages)) {
          // 如果已经是数组，展平后使用（防止嵌套数组）
          smallImagesArray = cachedSmallImages.flat();
        }
        
        // 遍历数组并添加到 images（去重）
        smallImagesArray.forEach(img => {
          // 确保 img 是字符串类型，不是数组
          if (img && typeof img === 'string' && !images.includes(img)) {
            images.push(img);
          }
        });
      }
      // 如果最终没有图片，至少保留主图
      if (images.length === 0 && cachedGoods.mainPic) {
        images = [cachedGoods.mainPic];
      }
      const goods = {
        id: cachedGoods.id,
        numIid: cachedGoods.id,
        title: cachedGoods.title || '',
        shortTitle: cachedGoods.title || '',
        mainPic: cachedGoods.mainPic || '',
        images: images, // 包含主图和小图的完整列表
        originalPrice: cachedGoods.originalPrice || '0',
        actualPrice: cachedGoods.actualPrice || '0',
        monthSales: cachedGoods.monthSales || '0',
        shopTitle: cachedGoods.shopTitle || '',
        nick: '',
        provcity: '',
        clickUrl: cachedGoods.clickUrl || '',
        couponShareUrl: cachedGoods.couponShareUrl || '',
        categoryName: cachedGoods.categoryName || '',
        commission: cachedGoods.commission || '0',
        commissionRate: cachedGoods.commissionRate || '0',
        // 新增字段 - 提取文本内容
        couponInfo: this.extractTextFromArray(cachedGoods.finalPromotionPathList),
        promotionTags: this.extractTextFromArray(cachedGoods.promotionTagList),
        commissionAmount: cachedGoods.commissionAmount || '0',
        tmallActivityInfo: cachedGoods.tmallPlayActivityInfo || '',
        // 保留列表页的原始数据
        sourceData: cachedGoods
      };
      
        this.setData({
          goods: goods,
          loading: false
        });
        return;
      }
    }
    
    // 如果没有全局数据（直接访问详情页或分享进入），则调用后端API查询
    console.log('全局数据为空,调用后端API查询商品详情，当前平台:', this.data.currentPlatform);
    this.setData({ loading: true });
    
    // 根据平台类型选择不同的API
    if (this.data.currentPlatform === PLATFORM_TYPES.JD) {
      // 京东商品：调用京东商品详情API
      console.log('调用京东商品详情API');
      const params = {
        skuIds: this.data.goodsId
      };

      // 如果有推荐人邀请码，添加到请求参数
      if (app.globalData.referralCode) {
        params.invitationCode = app.globalData.referralCode;
      } else if (app.globalData.userInfo && app.globalData.userInfo.userId) {
        // 如果没有邀请码，但用户已登录，传入用户ID
        params.userId = app.globalData.userInfo.userId;
      }
      
      request.publicGet('/miniapp/jd/getGoodsDetail', params)
        .then(res => {
          if (res.code === 200 && res.data) {
            const jdGoods = res.data;
            console.log('京东商品原始数据:', jdGoods);
            
            // 处理轮播图片列表
            let images = [];
            
            // 优先级1：从 sourceData.imageInfo.imageList 获取轮播图
            if (jdGoods.sourceData && jdGoods.sourceData.imageInfo && jdGoods.sourceData.imageInfo.imageList) {
              const imageList = jdGoods.sourceData.imageInfo.imageList;
              if (Array.isArray(imageList)) {
                imageList.forEach(imgObj => {
                  if (imgObj && imgObj.url) {
                    images.push(imgObj.url);
                  }
                });
                console.log('从sourceData.imageInfo.imageList获取轮播图，数量:', images.length);
              }
            }
            
            // 优先级2：从顶层 images 字段获取
            if (images.length === 0 && jdGoods.images && Array.isArray(jdGoods.images)) {
              images = jdGoods.images.filter(img => img);
              console.log('从顶层images获取轮播图，数量:', images.length);
            }
            
            // 优先级3：使用主图作为兜底
            if (images.length === 0 && jdGoods.imageUrl) {
              images.push(jdGoods.imageUrl);
              console.log('使用主图作为轮播图');
            }
            
            // 处理商品详情图片列表
            let detailImages = [];
            
            // 优先级1：从顶层 detailImages 字段获取（后端已处理为数组）
            if (jdGoods.detailImages) {
              if (Array.isArray(jdGoods.detailImages)) {
                detailImages = jdGoods.detailImages;
                console.log('从顶层detailImages获取（数组），长度:', detailImages.length);
              } else if (typeof jdGoods.detailImages === 'string') {
                detailImages = jdGoods.detailImages.split(',').map(url => url.trim()).filter(url => url);
                console.log('从顶层detailImages获取（字符串），解析后长度:', detailImages.length);
              }
            }
            
            // 优先级2：从 sourceData.detailImages 获取（兼容处理）
            if (detailImages.length === 0 && jdGoods.sourceData && jdGoods.sourceData.detailImages) {
              const sourceDetailImages = jdGoods.sourceData.detailImages;
              if (Array.isArray(sourceDetailImages)) {
                detailImages = sourceDetailImages;
              } else if (typeof sourceDetailImages === 'string') {
                detailImages = sourceDetailImages.split(',').map(url => url.trim()).filter(url => url);
              }
              console.log('从sourceData.detailImages获取，长度:', detailImages.length);
            }
            
            console.log('最终轮播图数量:', images.length);
            console.log('最终详情图数量:', detailImages.length);
            
            // 处理视频列表 - 从多个来源提取
            let videos = [];
            
            // 优先级1：从顶层 videos 字段获取（后端已处理）
            if (jdGoods.videos && Array.isArray(jdGoods.videos)) {
              videos = jdGoods.videos.map(v => ({
                url: v.videoUrl || v.url || '',
                cover: v.cover || v.url || jdGoods.imageUrl || ''
              })).filter(v => v.url);
              console.log('从顶层videos获取，数量:', videos.length);
            }
            
            // 优先级2：从 sourceData.imageInfo.imageList 中提取视频
            if (videos.length === 0 && jdGoods.sourceData && jdGoods.sourceData.imageInfo && jdGoods.sourceData.imageInfo.imageList) {
              const imageList = jdGoods.sourceData.imageInfo.imageList;
              if (Array.isArray(imageList)) {
                imageList.forEach(imgObj => {
                  if (imgObj && imgObj.videoUrl) {
                    videos.push({
                      url: imgObj.videoUrl,
                      cover: imgObj.url || jdGoods.imageUrl || ''
                    });
                  }
                });
                console.log('从imageInfo.imageList提取视频，数量:', videos.length);
              }
            }
            
            // 处理商品属性信息
            let propGroups = [];
            
            // 优先级1：从顶层 propGroups 字段获取（后端已处理为数组）
            if (jdGoods.propGroups && Array.isArray(jdGoods.propGroups)) {
              propGroups = jdGoods.propGroups;
              console.log('从顶层propGroups获取，数量:', propGroups.length);
            }
            
            // 优先级2：从 sourceData.baseBigFieldInfo.propGroups 解析（字符串格式）
            if (propGroups.length === 0 && jdGoods.sourceData && jdGoods.sourceData.baseBigFieldInfo && jdGoods.sourceData.baseBigFieldInfo.propGroups) {
              try {
                const propGroupsStr = jdGoods.sourceData.baseBigFieldInfo.propGroups;
                if (typeof propGroupsStr === 'string') {
                  propGroups = JSON.parse(propGroupsStr);
                  console.log('从baseBigFieldInfo.propGroups解析，数量:', propGroups.length);
                } else if (Array.isArray(propGroupsStr)) {
                  propGroups = propGroupsStr;
                }
              } catch (e) {
                console.warn('解析propGroups失败:', e);
              }
            }
            
            // 智能获取商品标题 - 多级优先级
            let skuName = '';
            
            // 优先级1：从顶层 skuName 字段获取
            if (jdGoods.skuName) {
              skuName = jdGoods.skuName;
              console.log('从顶层skuName获取标题:', skuName);
            }
            // 优先级2：从 sourceData.skuName 获取
            else if (jdGoods.sourceData && jdGoods.sourceData.skuName) {
              skuName = jdGoods.sourceData.skuName;
              console.log('从sourceData.skuName获取标题:', skuName);
            }
            // 优先级3：从 sourceData.skuInfo.skuName 获取
            else if (jdGoods.sourceData && jdGoods.sourceData.skuInfo && jdGoods.sourceData.skuInfo.skuName) {
              skuName = jdGoods.sourceData.skuInfo.skuName;
              console.log('从sourceData.skuInfo.skuName获取标题:', skuName);
            }
            // 兜底：使用空字符串
            else {
              console.warn('未找到商品标题，使用空字符串');
            }
            
            // 构建统一的商品数据结构
            const goods = {
              // 基础信息
              id: jdGoods.skuId || this.data.goodsId,
              numIid: jdGoods.skuId || this.data.goodsId,
              title: skuName,
              shortTitle: skuName,
              
              // 图片信息
              mainPic: jdGoods.imageUrl || (images.length > 0 ? images[0] : ''),
              images: images.length > 0 ? images : (jdGoods.imageUrl ? [jdGoods.imageUrl] : []),
              detailImages: detailImages,  // 商品详情图片列表
              
              // 价格信息 - 多级优先级处理（后端数据 -> sourceData -> 缓存数据 -> 默认值）
              originalPrice: (jdGoods.priceInfo && jdGoods.priceInfo.price) 
                || (jdGoods.sourceData && jdGoods.sourceData.priceInfo && jdGoods.sourceData.priceInfo.price)
                || (cachedGoods && cachedGoods.originalPrice)
                || '0',
              actualPrice: (jdGoods.priceInfo && (jdGoods.priceInfo.lowestCouponPrice || jdGoods.priceInfo.lowestPrice || jdGoods.priceInfo.price))
                || (jdGoods.sourceData && jdGoods.sourceData.priceInfo && (jdGoods.sourceData.priceInfo.lowestCouponPrice || jdGoods.sourceData.priceInfo.lowestPrice || jdGoods.sourceData.priceInfo.price))
                || (cachedGoods && cachedGoods.actualPrice)
                || '0',
              
              // 销量信息 - 多级优先级处理（后端数据 -> sourceData -> 缓存数据 -> 默认值）
              monthSales: jdGoods.inOrderCount30Days 
                || (jdGoods.sourceData && jdGoods.sourceData.skuInfo && jdGoods.sourceData.skuInfo.inOrderCount30Days)
                || (cachedGoods && cachedGoods.monthSales)
                || '0',
              
              // 店铺信息
              shopTitle: jdGoods.shopName || '',
              nick: '',
              provcity: '',
              
              // 推广链接
              clickUrl: jdGoods.clickURL || '',
              couponShareUrl: jdGoods.shortUrl || '',
              
              // 分类信息
              categoryName: jdGoods.categoryInfo ? (jdGoods.categoryInfo.cid1Name || '') : '',
              
              // 优惠和促销信息
              couponInfo: jdGoods.couponInfo ? [jdGoods.couponInfo.discount] : [],
              promotionTags: [],
              
              // 佣金信息 - 多级优先级处理（后端数据 -> 缓存数据 -> 默认值）
              commission: (jdGoods.commissionInfo && jdGoods.commissionInfo.commission) 
                || (cachedGoods && cachedGoods.commission)
                || '0',
              commissionRate: (jdGoods.commissionInfo && jdGoods.commissionInfo.commissionShare) 
                || (cachedGoods && cachedGoods.commissionRate)
                || '0',
              commissionAmount: (jdGoods.commissionInfo && jdGoods.commissionInfo.commission) 
                || (cachedGoods && cachedGoods.commissionAmount)
                || '0',
              
              // 其他信息
              tmallActivityInfo: '',
              platform: PLATFORM_TYPES.JD,
              
              // 京东特有字段
              videos: videos,              // 商品视频列表
              propGroups: propGroups,      // 商品属性分组
              detailHtml: jdGoods.detailHtml || '',  // 商品详情HTML
              brandName: jdGoods.brandName || '',
              
              // 保留原始数据供调试
              sourceData: jdGoods
            };
            
            this.setData({
              goods: goods,
              loading: false
            });
          } else {
            throw new Error(res.msg || '京东商品信息获取失败');
          }
        })
        .catch(err => {
          console.error('加载京东商品详情失败:', err);
          this.setData({ loading: false });
          wx.showToast({
            title: err.message || '加载失败',
            icon: 'none'
          });
        });
    } else {
      // 淘宝客商品：调用淘宝客商品详情API
      console.log('调用淘宝客商品详情API');
      let url = `/miniapp/tbk/goods/${this.data.goodsId}`;
      const params = {};
      
      // 如果有推荐人邀请码，添加到请求参数
      if (app.globalData.referralCode) {
        params.invitationCode = app.globalData.referralCode;
      } else if (app.globalData.userInfo && app.globalData.userInfo.userId) {
        // 如果没有邀请码，但用户已登录，传入用户ID
        params.userId = app.globalData.userInfo.userId;
      }
      
      request.publicGet(url, params)
        .then(res => {
          if (res.code === 200) {
            // 适配后端返回的数据结构（dgMaterialOptionalUpgrade接口）
            const itemInfo = res.data;
            const itemBasicInfo = itemInfo.itemBasicInfo || {};
            const pricePromotionInfo = itemInfo.pricePromotionInfo || {};
            const publishInfo = itemInfo.publishInfo || {};
            
            const goods = {
              id: itemInfo.itemId,
              numIid: itemInfo.itemId,
              title: itemBasicInfo.title || '',
              shortTitle: itemBasicInfo.shortTitle || itemBasicInfo.title || '',
              mainPic: itemBasicInfo.pictUrl || '',
              images: itemBasicInfo.smallImages ? itemBasicInfo.smallImages.split(',') : [itemBasicInfo.pictUrl],
              originalPrice: pricePromotionInfo.zkFinalPrice || '0',
              actualPrice: pricePromotionInfo.finalPromotionPrice || pricePromotionInfo.zkFinalPrice || '0',
              monthSales: itemBasicInfo.volume || itemBasicInfo.annualVol || '0',
              shopTitle: itemBasicInfo.shopTitle || '',
              nick: itemBasicInfo.nick || '',
              provcity: itemBasicInfo.provcity || '',
              // 使用推广链接：优先使用优惠券链接，其次使用点击链接
              clickUrl: publishInfo.clickUrl || '',
              couponShareUrl: publishInfo.couponShareUrl || '',
              categoryName: itemBasicInfo.categoryName || '',
              // 新增字段 - 提取文本内容
              couponInfo: this.extractTextFromArray(pricePromotionInfo.finalPromotionPathList),
              promotionTags: this.extractTextFromArray(pricePromotionInfo.promotionTagList),
              commissionAmount: publishInfo.incomeInfo ? publishInfo.incomeInfo.commissionAmount : '0',
              tmallActivityInfo: itemBasicInfo.tmallPlayActivityInfo || '',
              platform: PLATFORM_TYPES.TBK,
              // 保留原始数据以备后续使用
              sourceData: itemInfo
            };
            
            this.setData({
              goods: goods,
              loading: false
            });
          } else {
            throw new Error(res.msg || '加载失败');
          }
        })
        .catch(err => {
          console.error('加载商品详情失败:', err);
          this.setData({ loading: false });
          wx.showToast({
            title: err.message || '加载失败',
            icon: 'none'
          });
        });
    }
  },

  /**
   * 轮播图切换
   */
  onSwiperChange(e) {
    this.setData({
      currentImageIndex: e.detail.current
    });
  },

  /**
   * 图片预览
   */
  onImagePreview() {
    if (!this.data.goods || !this.data.goods.images) return;
    
    wx.previewImage({
      current: this.data.goods.images[this.data.currentImageIndex],
      urls: this.data.goods.images
    });
  },

  /**
   * 获取淘口令/京口令
   */
  onGetTpwd() {
    if (!this.data.goods) {
      wx.showToast({
        title: '商品信息加载中',
        icon: 'none'
      });
      return;
    }
    
    const goods = this.data.goods;
    const currentPlatform = goods.platform || this.data.currentPlatform;
    
    wx.showLoading({
      title: '生成中...',
      mask: true
    });
    
    // 构建请求参数
    const params = {
      platform: currentPlatform,
      goodsId: goods.id
    };
    
    // 根据平台添加特定参数
    if (currentPlatform === PLATFORM_TYPES.JD) {
      // 京东只需要商品ID
      params.itemId = goods.id;
    } else if (currentPlatform === PLATFORM_TYPES.PDD) {
      // 拼多多需要goodsSign参数
      params.goodsSign = goods.goodsSign || goods.id;
    } else if (currentPlatform === PLATFORM_TYPES.DTK) {
      // 大淘客需要商品ID、优惠券ID（如果有）和商品名称（用于设置淘口令右符号）
      params.couponId = goods.couponId || '';
      params.goodsName = goods.title || '';
    } else {
      // 淘宝客需要文案、链接和图片
      let itemUrl = goods.couponShareUrl || goods.clickUrl;
      
      if (!itemUrl) {
        wx.hideLoading();
        wx.showToast({
          title: '商品推广链接获取失败',
          icon: 'none'
        });
        return;
      }
      
      // 确保URL以https:开头
      if (itemUrl && !itemUrl.startsWith('http')) {
        itemUrl = 'https:' + itemUrl;
      }
      
      params.text = goods.title;
      params.url = itemUrl;
      params.logo = goods.mainPic;
    }
    
    // 添加邀请码或用户ID
    if (app.globalData.referralCode) {
      params.invitationCode = app.globalData.referralCode;
    } else if (app.globalData.userInfo && app.globalData.userInfo.userId) {
      params.userId = app.globalData.userInfo.userId;
    }
    
    // 调用统一的getTpwd接口
    platformApi.getTpwd(currentPlatform, params)
      .then(res => {
        wx.hideLoading();
        
        // 处理响应
        let tpwd = '';
        let tpwdType = '';
        
        if (currentPlatform === PLATFORM_TYPES.JD) {
          // 京东返回推广链接
          if (res.code === 200 && res.data && res.data.clickURL) {
            tpwd = res.data.clickURL;
            tpwdType = 'jd';
          }
        } else if (currentPlatform === PLATFORM_TYPES.PDD) {
          // 拼多多返回推广短链
          if (res.code === 200 && res.data) {
            tpwd = res.data.mobileShortUrl || res.data.promotionUrl || '';
            tpwdType = 'pdd';
          }
        } else {
          // 淘宝客和大淘客返回淘口令
          if (res.code === 200 && res.data && res.data.data && res.data.data.model) {
            tpwd = res.data.data.model;
            tpwdType = currentPlatform === PLATFORM_TYPES.DTK ? 'dtk' : 'tbk';
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
            title: res.msg || '获取失败',
            icon: 'none'
          });
        }
      })
      .catch(err => {
        wx.hideLoading();
        console.error('获取淘口令/京口令失败:', err);
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
   * 进店功能
   */
  onEnterShop() {
    if (!this.data.goods) {
      wx.showToast({
        title: '商品信息加载中',
        icon: 'none'
      });
      return;
    }

    const shopTitle = this.data.goods.shopTitle || this.data.goods.nick;
    
    if (!shopTitle) {
      wx.showToast({
        title: '店铺信息不完整',
        icon: 'none'
      });
      return;
    }

    // 显示操作选项
    wx.showActionSheet({
      itemList: ['搜索店铺商品', '复制店铺名称'],
      success: (res) => {
        if (res.tapIndex === 0) {
          // 跳转到商品列表页，搜索该店铺的商品
          wx.navigateTo({
            url: `/pages/goods/list/list?keyword=${encodeURIComponent(shopTitle)}`
          });
        } else if (res.tapIndex === 1) {
          // 复制店铺名称到剪贴板
          wx.setClipboardData({
            data: shopTitle,
            success: () => {
              wx.showToast({
                title: '店铺名称已复制',
                icon: 'success'
              });
            }
          });
        }
      }
    });
  },

  /**
   * 分享
   */
  onShareAppMessage() {
    const goods = this.data.goods;
    const shareData = {
      title: goods ? goods.title : '淘宝客优选好货',
      path: `/pages/goods/detail/detail?id=${this.data.goodsId}&platform=${this.data.currentPlatform}`,
      imageUrl: goods ? goods.mainPic : ''
    };
    
    // 附加关键数据到分享链接，避免接收者需要额外API查询
    if (goods) {
      // URL编码商品标题和图片，避免特殊字符导致参数解析错误
      shareData.path += `&title=${encodeURIComponent(goods.title || '')}`;
      shareData.path += `&pic=${encodeURIComponent(goods.mainPic || '')}`;
      // 附加价格、月销量、佣金等关键数据
      shareData.path += `&price=${goods.actualPrice || '0'}`;
      shareData.path += `&originalPrice=${goods.originalPrice || '0'}`;
      shareData.path += `&monthSales=${goods.monthSales || '0'}`;
      shareData.path += `&commission=${goods.commission || '0'}`;
      shareData.path += `&commissionRate=${goods.commissionRate || '0'}`;
      
      // 添加推广链接参数（用于淘口令生成）
      // 注意：这些链接可能包含特殊字符，需要URL编码
      if (goods.couponShareUrl) {
        shareData.path += `&couponShareUrl=${encodeURIComponent(goods.couponShareUrl)}`;
      }
      if (goods.clickUrl) {
        shareData.path += `&clickUrl=${encodeURIComponent(goods.clickUrl)}`;
      }
    }
    
    // 如果是代理，添加邀请码
    if (this.data.isAgent && this.data.invitationCode) {
      shareData.path += `&invitationCode=${this.data.invitationCode}`;
    }
    
    return shareData;
  },

  /**
   * 分享到朋友圈
   */
  onShareTimeline() {
    const goods = this.data.goods;
    const shareData = {
      title: goods ? goods.title : '淘宝客优选好货',
      query: `id=${this.data.goodsId}&platform=${this.data.currentPlatform}`,
      imageUrl: goods ? goods.mainPic : ''
    };
    
    // 附加关键数据到分享链接
    if (goods) {
      shareData.query += `&title=${encodeURIComponent(goods.title || '')}`;
      shareData.query += `&pic=${encodeURIComponent(goods.mainPic || '')}`;
      shareData.query += `&price=${goods.actualPrice || '0'}`;
      shareData.query += `&originalPrice=${goods.originalPrice || '0'}`;
      shareData.query += `&monthSales=${goods.monthSales || '0'}`;
      shareData.query += `&commission=${goods.commission || '0'}`;
      shareData.query += `&commissionRate=${goods.commissionRate || '0'}`;
      
      // 添加推广链接参数（用于淘口令生成）
      if (goods.couponShareUrl) {
        shareData.query += `&couponShareUrl=${encodeURIComponent(goods.couponShareUrl)}`;
      }
      if (goods.clickUrl) {
        shareData.query += `&clickUrl=${encodeURIComponent(goods.clickUrl)}`;
      }
    }
    
    // 如果是代理，添加邀请码
    if (this.data.isAgent && this.data.invitationCode) {
      shareData.query += `&invitationCode=${this.data.invitationCode}`;
    }
    
    return shareData;
  },

  /**
   * 返回首页
   */
  goBack() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  },

  /**
   * 从对象数组中提取文本内容
   * @param {Array} dataArray - 可能包含对象或字符串的数组
   * @returns {Array} 文本字符串数组
   */
  extractTextFromArray(dataArray) {
    if (!dataArray || !Array.isArray(dataArray)) {
      return [];
    }
    
    return dataArray.map(item => {
      if (typeof item === 'string') {
        return item;
      }
      
      if (typeof item === 'object' && item !== null) {
        // 尝试提取常见的文本字段
        return item.promotionTitle || 
               item.title || 
               item.text || 
               item.tagName ||
               item.name || 
               item.desc || 
               item.description ||
               '';
      }
      
      return '';
    }).filter(text => text); // 过滤掉空字符串
  }
});
