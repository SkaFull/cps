/**
 * 拼多多联盟平台适配器
 * 继承自BaseAdapter，实现拼多多联盟特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class PddAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.PDD);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的拼多多联盟API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('拼多多联盟API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    // 拼多多API返回格式: { data: { response: { goodsSearchResponse: { goodsList: [...] } } } }
    const goodsList = data.response?.goodsSearchResponse?.goodsList;
    
    if (!goodsList || !Array.isArray(goodsList)) {
      console.warn('拼多多联盟返回的goodsList为空或格式错误');
      return [];
    }

    return goodsList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的拼多多联盟API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('拼多多联盟API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    // 拼多多商品详情API返回格式
    const goodsDetail = data.response?.goodsDetailResponse || data.response?.goodsBasicDetailResponse;
    
    if (!goodsDetail) {
      console.warn('拼多多联盟返回的商品详情为空');
      return null;
    }

    const basicInfo = this._transformGoodsItem(goodsDetail);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this._extractImages(goodsDetail),
      description: goodsDetail.goodsDesc || goodsDetail.descTxt || '暂无商品描述',
      brandName: goodsDetail.brandName || '',
      categoryInfo: this._extractCategoryInfo(goodsDetail),
      serviceTags: goodsDetail.serviceTags || [],
      unifiedTags: goodsDetail.unifiedTags || []
    };
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('拼多多联盟分类API响应数据无效:', apiResponse);
      return [];
    }

    const categories = apiResponse.data.response?.goodsOptGetResponse?.goodsOptList || [];
    
    return categories.map(cat => ({
      id: cat.optId,
      name: cat.optName,
      iconUrl: cat.iconUrl || '',
      platform: this.platformType,
      parentId: cat.parentOptId || 0,
      level: cat.level || 1
    }));
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    // 拼多多商品数据字段映射
    // goodsSign: 商品标识（用作ID）
    // goodsName: 商品名称
    // goodsImageUrl: 商品主图
    // goodsThumbnailUrl: 商品缩略图
    // minGroupPrice: 最低拼团价（单位：分）
    // minNormalPrice: 最低单买价（单位：分）
    // promotionRate: 佣金比例（千分比）
    // couponDiscount: 优惠券面额（单位：分）
    // salesTip: 销量文案
    
    const minGroupPrice = parseFloat(item.minGroupPrice || 0) / 100; // 分转元
    const minNormalPrice = parseFloat(item.minNormalPrice || 0) / 100; // 分转元
    const couponDiscount = parseFloat(item.couponDiscount || item.extraCouponAmount || 0) / 100; // 分转元
    const promotionRate = parseFloat(item.promotionRate || 0) / 10; // 千分比转百分比
    
    // 计算券后价
    const finalPrice = minGroupPrice - couponDiscount;
    
    // 计算预估佣金
    const commission = finalPrice * (promotionRate / 100);
    
    return {
      id: item.goodsSign || String(item.goodsId || ''),
      platform: this.platformType,
      title: item.goodsName || '商品标题',
      mainPic: this.normalizeImageUrl(item.goodsThumbnailUrl || item.goodsImageUrl),
      smallImages: this._extractImages(item),
      actualPrice: this.formatPrice(finalPrice),
      originalPrice: this.formatPrice(minNormalPrice),
      commission: this.formatPrice(commission),
      commissionRate: this.formatPrice(promotionRate),
      monthSales: this._parseSalesTip(item.salesTip),
      shopTitle: item.mallName || '',
      couponAmount: couponDiscount,
      couponInfo: this._extractCouponInfo(item),
      categoryName: item.optName || '',
      brandName: item.brandName || '',
      clickUrl: '', // 需要通过推广链接生成接口获取
      couponShareUrl: '', // 需要通过推广链接生成接口获取
      // 拼多多特有字段
      goodsSign: item.goodsSign,
      minGroupPrice: this.formatPrice(minGroupPrice),
      mallCps: item.mallCps || 0,
      merchantType: item.merchantType,
      hasCoupon: item.hasCoupon || false,
      couponStartTime: item.couponStartTime,
      couponEndTime: item.couponEndTime,
      couponRemainQuantity: item.couponRemainQuantity || 0,
      serviceTags: item.serviceTags || [],
      unifiedTags: item.unifiedTags || [],
      activityTags: item.activityTags || [],
      descTxt: item.descTxt || '',
      lgstTxt: item.lgstTxt || '',
      servTxt: item.servTxt || ''
    };
  }

  /**
   * 提取商品图片列表
   * @private
   */
  _extractImages(item) {
    const images = [];
    
    // 主图
    if (item.goodsImageUrl) {
      images.push(this.normalizeImageUrl(item.goodsImageUrl));
    }
    
    // 缩略图
    if (item.goodsThumbnailUrl && item.goodsThumbnailUrl !== item.goodsImageUrl) {
      images.push(this.normalizeImageUrl(item.goodsThumbnailUrl));
    }
    
    // 如果有商品图片列表
    if (item.goodsGalleryUrls && Array.isArray(item.goodsGalleryUrls)) {
      item.goodsGalleryUrls.forEach(imgUrl => {
        if (imgUrl) {
          images.push(this.normalizeImageUrl(imgUrl));
        }
      });
    }
    
    // 如果有详情图片列表
    if (item.goodsImageUrls && Array.isArray(item.goodsImageUrls)) {
      item.goodsImageUrls.forEach(imgUrl => {
        if (imgUrl) {
          images.push(this.normalizeImageUrl(imgUrl));
        }
      });
    }
    
    // 去重
    return [...new Set(images)];
  }

  /**
   * 提取优惠券信息文本
   * @private
   */
  _extractCouponInfo(item) {
    const couponDiscount = parseFloat(item.couponDiscount || item.extraCouponAmount || 0) / 100;
    
    if (couponDiscount > 0) {
      const couponMinOrderAmount = parseFloat(item.couponMinOrderAmount || 0) / 100;
      if (couponMinOrderAmount > 0) {
        return `满${couponMinOrderAmount}元减${couponDiscount}元`;
      }
      return `${couponDiscount}元优惠券`;
    }
    
    return '';
  }

  /**
   * 解析销量文案
   * @private
   */
  _parseSalesTip(salesTip) {
    if (!salesTip) return 0;
    
    // salesTip格式示例: "8.4万+", "1722", "2000+"
    let salesStr = String(salesTip).replace(/\+/g, '').trim();
    
    // 处理万的情况
    if (salesStr.includes('万')) {
      const num = parseFloat(salesStr.replace('万', ''));
      return Math.floor(num * 10000);
    }
    
    // 直接转换数字
    return parseInt(salesStr) || 0;
  }

  /**
   * 提取分类信息
   * @private
   */
  _extractCategoryInfo(item) {
    if (!item.catIds || !Array.isArray(item.catIds)) {
      return null;
    }
    
    return {
      catIds: item.catIds,
      optId: item.optId,
      optName: item.optName,
      optIds: item.optIds || []
    };
  }

  /**
   * 适配推广链接生成响应
   * @param {Object} apiResponse - 后端返回的推广链接生成API响应
   * @returns {Object} 适配后的推广链接数据
   */
  adaptPromotionUrl(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('拼多多推广链接API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    const urlResponse = data.response?.goodsPromotionUrlGenerateResponse;
    
    if (!urlResponse) {
      console.warn('拼多多推广链接生成失败');
      return null;
    }

    // 优先使用后端适配后的数据
    const adaptedData = urlResponse.adaptedData;
    if (adaptedData) {
      return {
        goodsSign: adaptedData.goodsSign,
        promotionUrl: adaptedData.promotionUrl,
        mobileShortUrl: adaptedData.mobileShortUrl,
        mobileUrl: adaptedData.mobileUrl,
        shortUrl: adaptedData.shortUrl,
        url: adaptedData.url,
        weAppWebViewUrl: adaptedData.weAppWebViewUrl,
        weAppWebViewShortUrl: adaptedData.weAppWebViewShortUrl,
        schemaUrl: adaptedData.schemaUrl,
        qrCodeUrl: adaptedData.qrcodeUrl
      };
    }

    // 兼容旧版本响应格式
    const urlList = urlResponse.goodsPromotionUrlList;
    if (!urlList || !Array.isArray(urlList) || urlList.length === 0) {
      console.warn('拼多多推广链接列表为空');
      return null;
    }

    const urlData = urlList[0];
    return {
      goodsSign: urlData.goodsSign,
      promotionUrl: urlData.mobileShortUrl || urlData.mobileUrl || urlData.shortUrl || urlData.url,
      mobileShortUrl: urlData.mobileShortUrl,
      mobileUrl: urlData.mobileUrl,
      shortUrl: urlData.shortUrl,
      url: urlData.url,
      weAppWebViewUrl: urlData.weAppWebViewUrl,
      weAppWebViewShortUrl: urlData.weAppWebViewShortUrl,
      schemaUrl: urlData.schemaUrl,
      qrCodeUrl: urlData.qrcodeUrl
    };
  }

  /**
   * 适配推广链接响应（与其他平台统一接口）
   * 拼多多没有淘口令，使用推广URL作为 model 字段
   * 优先使用mobileShortUrl短链，适配小程序分享场景
   * @param {Object} apiResponse - 后端返回的推广链接生成API响应
   * @returns {Object} 统一格式的响应数据，与DTK/TBK保持一致
   */
  adaptTpwdResponse(apiResponse) {
    if (!apiResponse || apiResponse.code !== 200) {
      console.error('拼多多推广链接API响应失败:', apiResponse);
      return {
        code: apiResponse?.code || 500,
        msg: apiResponse?.msg || '获取推广链接失败',
        data: null
      };
    }

    const promotionData = this.adaptPromotionUrl(apiResponse);
    
    if (!promotionData) {
      return {
        code: 500,
        msg: '推广链接数据无效',
        data: null
      };
    }

    // 拼多多优先使用mobileShortUrl短链（适配小程序场景），其次mobileUrl，再次shortUrl，最后普通url
    const promotionUrl = promotionData.mobileShortUrl 
      || promotionData.mobileUrl 
      || promotionData.shortUrl
      || promotionData.promotionUrl 
      || '';

    return {
      code: 200,
      msg: '拼多多推广链接已生成，请复制短链分享给用户',
      data: {
        data: {
          // 与DTK/TBK保持一致：model字段存放推广链接（拼多多无淘口令，使用短链URL）
          model: promotionUrl
        },
        // 保留完整推广链接数据
        promotionUrl: promotionData.promotionUrl,
        mobileShortUrl: promotionData.mobileShortUrl,
        shortUrl: promotionData.shortUrl,
        mobileUrl: promotionData.mobileUrl,
        weAppWebViewUrl: promotionData.weAppWebViewUrl,
        weAppWebViewShortUrl: promotionData.weAppWebViewShortUrl,
        schemaUrl: promotionData.schemaUrl,
        qrCodeUrl: promotionData.qrCodeUrl,
        goodsSign: promotionData.goodsSign,
        // 拼多多特有提示信息
        tips: '拼多多推广链接已生成，直接分享短链即可，无需淘口令',
        platform: 'pdd',
        rawData: promotionData
      }
    };
  }
}

// 导出单例
module.exports = new PddAdapter();
