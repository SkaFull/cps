/**
 * 大淘客适配器
 * 继承自BaseAdapter，实现大淘客特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class DtkAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.DTK);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的大淘客API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('大淘客API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    // 大淘客返回的数据结构：data.resultList
    let itemList = [];
    if (data.resultList && Array.isArray(data.resultList)) {
      itemList = data.resultList;
    } else if (data.list && Array.isArray(data.list)) {
      itemList = data.list;
    } else if (data.data && Array.isArray(data.data)) {
      itemList = data.data;
    } else if (Array.isArray(data)) {
      itemList = data;
    }
    
    if (itemList.length === 0) {
      console.warn('大淘客返回的商品列表为空');
      return [];
    }

    return itemList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的大淘客API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('大淘客API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    // 大淘客商品详情可能直接在data中，或在data.data中
    const item = data.data || data;
    
    if (!item || !item.id) {
      console.warn('大淘客返回的商品详情为空');
      return null;
    }

    const basicInfo = this._transformGoodsItem(item);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this.normalizeImages(item.mainPic || item.dtPic || []),
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
      console.error('大淘客分类API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    let categories = [];
    
    if (Array.isArray(data)) {
      categories = data;
    } else if (data.list && Array.isArray(data.list)) {
      categories = data.list;
    } else if (data.data && Array.isArray(data.data)) {
      categories = data.data;
    }
    
    return categories.map(cat => ({
      id: cat.cid || cat.id || cat.categoryId,
      name: cat.cname || cat.name || cat.categoryName,
      iconUrl: this.normalizeImageUrl(cat.pic || cat.icon || ''),
      platform: this.platformType
    }));
  }

  /**
   * 适配淘口令响应数据
   * @param {Object} apiResponse - 后端返回的淘口令API响应数据
   * @returns {Object} 适配后的淘口令数据
   */
  adaptTpwdResponse(apiResponse) {
    if (!apiResponse || apiResponse.code !== 200) {
      console.error('大淘客淘口令API响应失败:', apiResponse);
      return {
        code: apiResponse?.code || 500,
        msg: apiResponse?.msg || '获取淘口令失败',
        data: null
      };
    }

    const responseData = apiResponse.data;
    if (!responseData || responseData.code !== 0) {
      console.error('大淘客淘口令数据无效:', responseData);
      return {
        code: 500,
        msg: responseData?.msg || '淘口令数据无效',
        data: null
      };
    }

    const tpwdData = responseData.data;
    if (!tpwdData) {
      console.error('大淘客淘口令数据为空');
      return {
        code: 500,
        msg: '淘口令数据为空',
        data: null
      };
    }

    // 适配返回格式，保持与淘宝客一致的结构
    return {
      code: 200,
      msg: '操作成功',
      data: {
        data: {
          model: tpwdData.tpwd || tpwdData.longTpwd || '' // 优先使用短淘口令，如果没有则使用长淘口令
        },
        // 保留原始数据供需要时使用
        rawData: tpwdData
      }
    };
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    // 大淘客的价格字段（根据实际返回数据）
    const actualPrice = parseFloat(item.actualPrice || 0);
    const originalPrice = parseFloat(item.originalPrice || actualPrice);
    const commissionRate = parseFloat(item.commissionRate || 0);
    const commission = actualPrice * (commissionRate / 100);
    const couponPrice = parseFloat(item.couponPrice || 0);
    
    // 处理图片（大淘客返回的是完整URL）
    const mainPic = this.normalizeImageUrl(item.mainPic || '');
    
    return {
      id: String(item.goodsId || item.id || ''),  // 优先使用淘宝商品ID，而非大淘客内部ID
      platform: this.platformType,
      title: item.dtitle || item.title || '商品标题',
      mainPic: mainPic,
      smallImages: [mainPic], // 大淘客通常只返回主图
      actualPrice: this.formatPrice(actualPrice),
      originalPrice: this.formatPrice(originalPrice),
      commission: this.formatPrice(commission),
      commissionRate: commissionRate.toFixed(2),
      monthSales: parseInt(item.monthSales || 0),
      shopTitle: item.shopName || '',
      couponAmount: couponPrice,
      couponInfo: couponPrice > 0 ? `${couponPrice}元优惠券` : '',
      categoryName: item.brandName || '', // 大淘客用brandName
      brandName: item.brandName || '',
      // 大淘客特有字段
      couponLink: item.couponLink || '',
      couponEndTime: item.couponEndTime || '',
      couponStartTime: item.couponStartTime || '',
      twoHoursSales: parseInt(item.twoHoursSales || 0),
      dailySales: parseInt(item.dailySales || 0),
      // 商品链接
      itemLink: item.itemLink || '',
      // 商品描述
      desc: item.desc || ''
    };
  }
}

// 导出单例
module.exports = new DtkAdapter();
