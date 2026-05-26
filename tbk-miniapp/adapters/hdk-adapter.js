/**
 * 好单库适配器
 * 继承自BaseAdapter，实现好单库特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class HdkAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.HDK);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的好单库API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('好单库API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    // 好单库返回的数据结构：data.data.list 或 data.list
    let itemList = [];
    if (data.data && data.data.list && Array.isArray(data.data.list)) {
      itemList = data.data.list;
    } else if (data.list && Array.isArray(data.list)) {
      itemList = data.list;
    } else if (data.data && Array.isArray(data.data)) {
      itemList = data.data;
    } else if (Array.isArray(data)) {
      itemList = data;
    }
    
    if (itemList.length === 0) {
      console.warn('好单库返回的商品列表为空');
      return [];
    }

    return itemList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的好单库API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('好单库API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    // 好单库商品详情在data.data中或直接在data中
    const item = (data.data && data.data.info) || data.data || data;
    
    if (!item || !item.id) {
      console.warn('好单库返回的商品详情为空');
      return null;
    }

    const basicInfo = this._transformGoodsItem(item);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this.normalizeImages(item.imgs || item.smallImages || [item.mainPic]),
      description: item.desc || item.title || '暂无商品描述',
      detailPics: this.normalizeImages(item.descImgs || [])
    };
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('好单库分类API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    let categories = [];
    
    // 好单库分类数据结构
    if (data.data && Array.isArray(data.data)) {
      categories = data.data;
    } else if (data.list && Array.isArray(data.list)) {
      categories = data.list;
    } else if (Array.isArray(data)) {
      categories = data;
    }
    
    return categories.map(cat => ({
      id: cat.cid || cat.id || cat.categoryId,
      name: cat.cname || cat.name || cat.categoryName,
      iconUrl: this.normalizeImageUrl(cat.pic || cat.icon || ''),
      platform: this.platformType
    }));
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    // 好单库的价格字段
    const actualPrice = parseFloat(item.actualPrice || item.quanHoujia || item.finalPrice || 0);
    const originalPrice = parseFloat(item.originalPrice || item.reservePrice || actualPrice);
    const commissionRate = parseFloat(item.commissionRate || item.tkRate || 0);
    const commission = parseFloat(item.commission || (actualPrice * commissionRate / 100));
    const couponPrice = parseFloat(item.couponPrice || item.couponAmount || 0);
    
    return {
      id: String(item.id || item.goodsId || ''),
      platform: this.platformType,
      title: item.title || item.dtitle || '商品标题',
      mainPic: this.normalizeImageUrl(item.mainPic || item.pic || item.imgUrl),
      smallImages: this.normalizeImages(item.imgs || item.smallImages || [item.mainPic]),
      actualPrice: this.formatPrice(actualPrice),
      originalPrice: this.formatPrice(originalPrice),
      commission: this.formatPrice(commission),
      commissionRate: commissionRate.toFixed(2),
      monthSales: parseInt(item.monthSales || item.sales || item.volume || 0),
      shopTitle: item.shopName || item.shopTitle || '',
      couponAmount: couponPrice,
      couponInfo: couponPrice > 0 ? `${couponPrice}元优惠券` : '',
      categoryName: item.cname || item.categoryName || '',
      brandName: item.brandName || '',
      // 好单库特有字段
      couponLink: item.couponLink || '',
      couponEndTime: item.couponEndTime || '',
      couponStartTime: item.couponStartTime || '',
      isFreeShipping: item.isFreeShipping || false
    };
  }
}

// 导出单例
module.exports = new HdkAdapter();
