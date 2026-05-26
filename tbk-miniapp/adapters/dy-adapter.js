/**
 * 抖音联盟平台适配器
 * 继承自BaseAdapter，实现抖音联盟特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class DyAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.DY);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的抖音联盟API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('抖音联盟API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    // 抖音联盟API返回格式: { data: { resultList: [...] } }
    if (!data.resultList || !Array.isArray(data.resultList)) {
      console.warn('抖音联盟返回的resultList为空或格式错误');
      return [];
    }

    return data.resultList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的抖音联盟API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('抖音联盟API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    // 如果是数组，取第一个元素
    const item = Array.isArray(data) ? data[0] : data;
    
    if (!item) {
      console.warn('抖音联盟返回的商品详情为空');
      return null;
    }

    const basicInfo = this._transformGoodsItem(item);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this._extractImages(item),
      description: item.description || item.detail || '暂无商品描述',
      brandName: item.brandName || '',
      shopInfo: this._extractShopInfo(item)
    };
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('抖音联盟分类API响应数据无效:', apiResponse);
      return [];
    }

    const categories = Array.isArray(apiResponse.data) ? apiResponse.data : [];
    
    return categories.map(cat => ({
      id: cat.categoryId || cat.id,
      name: cat.categoryName || cat.name,
      iconUrl: cat.iconUrl || '',
      platform: this.platformType,
      parentId: cat.parentId || 0
    }));
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    // 抖音联盟商品数据字段映射
    // productId: 商品ID
    // productName/title: 商品名称
    // productImg: 商品主图
    // price: 商品价格
    // cosRatio: 佣金比例
    // commission: 佣金金额
    // salesTip: 销量提示
    
    const price = parseFloat(item.price || item.productPrice || 0);
    const commission = parseFloat(item.commission || 0);
    const cosRatio = parseFloat(item.cosRatio || item.commissionRate || 0);
    
    // 计算实际佣金（如果后端没有返回commission，通过价格和比例计算）
    const actualCommission = commission > 0 ? commission : (price * cosRatio / 100);
    
    return {
      id: String(item.productId || item.id || ''),
      platform: this.platformType,
      title: item.productName || item.title || '商品标题',
      mainPic: this.normalizeImageUrl(item.productImg || item.mainPic),
      smallImages: this._extractImages(item),
      actualPrice: this.formatPrice(price),
      originalPrice: this.formatPrice(item.originalPrice || item.marketPrice || price),
      commission: this.formatPrice(actualCommission),
      commissionRate: cosRatio > 0 ? this.formatPrice(cosRatio) : this.calculateCommissionRate(actualCommission, price),
      monthSales: this._extractSales(item),
      shopTitle: item.shopName || item.shopTitle || '',
      couponAmount: this._extractCouponAmount(item),
      couponInfo: this._extractCouponInfo(item),
      categoryName: item.categoryName || '',
      brandName: item.brandName || '',
      clickUrl: item.productUrl || item.clickUrl || '',
      couponShareUrl: item.couponUrl || '',
      // 抖音特有字段
      salesTip: item.salesTip || '',
      videoUrl: item.videoUrl || '',
      imageList: item.imageList || []
    };
  }

  /**
   * 提取商品图片列表
   * @private
   */
  _extractImages(item) {
    const images = [];
    
    // 主图
    if (item.productImg || item.mainPic) {
      images.push(this.normalizeImageUrl(item.productImg || item.mainPic));
    }
    
    // 如果后端已经返回了处理好的smallImages数组，直接使用
    if (item.smallImages && Array.isArray(item.smallImages)) {
      item.smallImages.forEach(imgUrl => {
        if (imgUrl) {
          images.push(this.normalizeImageUrl(imgUrl));
        }
      });
      return [...new Set(images)];
    }
    
    // 抖音API返回的imageList字段（字符串数组）
    if (item.imageList && Array.isArray(item.imageList)) {
      item.imageList.forEach(imgUrl => {
        if (imgUrl) {
          images.push(this.normalizeImageUrl(imgUrl));
        }
      });
    }
    
    // 备用：从字符串解析
    if (item.images) {
      const parsedImages = this.normalizeImages(item.images);
      images.push(...parsedImages);
    }
    
    // 去重
    return [...new Set(images)];
  }

  /**
   * 提取销量
   * @private
   */
  _extractSales(item) {
    // 优先使用明确的销量字段
    if (item.monthSales !== undefined) {
      return parseInt(item.monthSales);
    }
    
    if (item.sales !== undefined) {
      return parseInt(item.sales);
    }
    
    // 从销量提示中提取
    if (item.salesTip) {
      const match = item.salesTip.match(/(\d+\.?\d*)(万|w)?/i);
      if (match) {
        let sales = parseFloat(match[1]);
        if (match[2] && (match[2] === '万' || match[2].toLowerCase() === 'w')) {
          sales = sales * 10000;
        }
        return Math.floor(sales);
      }
    }
    
    return 0;
  }

  /**
   * 提取优惠券金额
   * @private
   */
  _extractCouponAmount(item) {
    // 抖音优惠券信息可能在 couponAmount 或 couponDiscount 字段
    if (item.couponAmount) {
      return parseFloat(item.couponAmount);
    }
    
    if (item.couponDiscount) {
      return parseFloat(item.couponDiscount);
    }
    
    // 从couponInfo中提取
    if (item.couponInfo && typeof item.couponInfo === 'string') {
      const match = item.couponInfo.match(/(\d+\.?\d*)/);
      if (match) {
        return parseFloat(match[1]);
      }
    }
    
    return 0;
  }

  /**
   * 提取优惠券信息文本
   * @private
   */
  _extractCouponInfo(item) {
    const couponAmount = this._extractCouponAmount(item);
    if (couponAmount > 0) {
      return `${couponAmount}元优惠券`;
    }
    
    // 如果有优惠券文本信息
    if (item.couponInfo && typeof item.couponInfo === 'string') {
      return item.couponInfo;
    }
    
    return '';
  }

  /**
   * 提取店铺信息
   * @private
   */
  _extractShopInfo(item) {
    if (!item.shopInfo && !item.shopName) {
      return null;
    }
    
    return {
      shopId: item.shopId || '',
      shopName: item.shopName || item.shopTitle || '',
      shopType: item.shopType || '',
      shopUrl: item.shopUrl || ''
    };
  }
}

// 导出单例
module.exports = new DyAdapter();
