/**
 * 实佣有你适配器
 * 继承自BaseAdapter，实现淘宝客特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class TbkAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.TBK);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的淘宝客API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('淘宝客API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    if (!data.resultList || !Array.isArray(data.resultList)) {
      console.warn('后端返回的resultList为空或格式错误');
      return [];
    }

    return data.resultList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的淘宝客API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('淘宝客API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    if (!data.resultList || !Array.isArray(data.resultList) || data.resultList.length === 0) {
      console.warn('后端返回的resultList为空');
      return null;
    }

    const item = data.resultList[0];
    const basicInfo = this._transformGoodsItem(item);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this.normalizeImages(item.smallImages || []),
      description: item.description || '暂无商品描述'
    };
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('淘宝客分类API响应数据无效:', apiResponse);
      return [];
    }

    const categories = apiResponse.data || [];
    
    return categories.map(cat => ({
      id: cat.id,
      name: cat.name,
      iconUrl: cat.iconUrl || '',
      platform: this.platformType
    }));
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    const finalPrice = parseFloat(item.finalPromotionPrice || item.zkFinalPrice || 0);
    const zkPrice = parseFloat(item.zkFinalPrice || 0);
    const commission = parseFloat(item.commissionAmount || 0);
    
    return {
      id: item.itemId || '',
      platform: this.platformType,
      title: item.title || '商品标题',
      mainPic: this.normalizeImageUrl(item.pictUrl),
      smallImages: this.normalizeImages(item.smallImages),
      actualPrice: this.formatPrice(finalPrice),
      originalPrice: this.formatPrice(zkPrice),
      commission: this.formatPrice(commission),
      commissionRate: this.calculateCommissionRate(commission, finalPrice),
      monthSales: parseInt(item.annualVol || 0),
      shopTitle: item.shopTitle || '',
      couponAmount: this._extractCouponAmount(item),
      couponInfo: this._extractCouponInfo(item),
      categoryName: item.categoryName || '',
      brandName: item.brandName || '',
      clickUrl: item.clickUrl || '',
      couponShareUrl: item.couponShareUrl || '',
      finalPromotionPathList: item.finalPromotionPathList || [],
      promotionTagList: item.promotionTagList || []
    };
  }

  /**
   * 提取优惠券金额
   * @private
   */
  _extractCouponAmount(item) {
    // 从finalPromotionPathList中提取优惠券信息
    if (item.finalPromotionPathList && Array.isArray(item.finalPromotionPathList)) {
      for (const promotion of item.finalPromotionPathList) {
        if (promotion.promotionType === 'COUPON' && promotion.promotionAmount) {
          return parseFloat(promotion.promotionAmount);
        }
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
    return '';
  }
}

// 导出单例
module.exports = new TbkAdapter();
