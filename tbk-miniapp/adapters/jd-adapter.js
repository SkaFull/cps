/**
 * 京东联盟平台适配器
 * 继承自BaseAdapter，实现京东联盟特定的数据转换逻辑
 */

const BaseAdapter = require('./base-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

class JdAdapter extends BaseAdapter {
  constructor() {
    super(PLATFORM_TYPES.JD);
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的京东联盟API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('京东联盟API响应数据无效:', apiResponse);
      return [];
    }

    const data = apiResponse.data;
    
    // 京东联盟API返回格式: { data: { resultList: [...] } }，与淘宝客保持一致
    if (!data.resultList || !Array.isArray(data.resultList)) {
      console.warn('京东联盟返回的resultList为空或格式错误');
      return [];
    }

    return data.resultList.map(item => this._transformGoodsItem(item));
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的京东联盟API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('京东联盟API响应数据无效:', apiResponse);
      return null;
    }

    const data = apiResponse.data;
    
    // 如果是数组，取第一个元素
    const item = Array.isArray(data) ? data[0] : data;
    
    if (!item) {
      console.warn('京东联盟返回的商品详情为空');
      return null;
    }

    const basicInfo = this._transformGoodsItem(item);
    
    // 详情页额外字段
    return {
      ...basicInfo,
      images: this._extractImages(item),
      description: item.wdis || item.wareInfo || '暂无商品描述',
      brandName: item.brandName || '',
      categoryInfo: this._extractCategoryInfo(item)
    };
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    if (!apiResponse || !apiResponse.data) {
      console.error('京东联盟分类API响应数据无效:', apiResponse);
      return [];
    }

    const categories = Array.isArray(apiResponse.data) ? apiResponse.data : [];
    
    return categories.map(cat => ({
      id: cat.categoryId || cat.id,
      name: cat.categoryName || cat.name,
      iconUrl: cat.iconUrl || '',
      platform: this.platformType,
      parentId: cat.parentId || 0,
      grade: cat.grade || 1
    }));
  }

  /**
   * 转换单个商品数据项
   * @private
   */
  _transformGoodsItem(item) {
    // 京东联盟商品数据字段映射
    // skuId: 商品ID
    // skuName: 商品名称
    // imageUrl: 商品主图
    // price: 商品价格
    // commissionShare: 佣金比例
    // commission: 佣金金额
    // inOrderCount30Days: 30天引单数量
    // comments: 评论数
    // goodCommentsShare: 好评率
    
    const price = parseFloat(item.price || 0);
    const commission = parseFloat(item.commission || 0);
    const commissionShare = parseFloat(item.commissionShare || 0);
    
    // 计算实际佣金（如果后端没有返回commission，通过价格和比例计算）
    const actualCommission = commission > 0 ? commission : (price * commissionShare / 100);
    
    return {
      id: String(item.skuId || item.id || ''),
      platform: this.platformType,
      title: item.skuName || item.name || '商品标题',
      mainPic: this.normalizeImageUrl(item.imageUrl || item.mainPic),
      smallImages: this._extractImages(item),
      actualPrice: this.formatPrice(price),
      originalPrice: this.formatPrice(item.originalPrice || price),
      commission: this.formatPrice(actualCommission),
      commissionRate: commissionShare > 0 ? this.formatPrice(commissionShare) : this.calculateCommissionRate(actualCommission, price),
      monthSales: parseInt(item.inOrderCount30Days || item.monthSales || 0),
      shopTitle: item.shopName || item.shopTitle || '',
      couponAmount: this._extractCouponAmount(item),
      couponInfo: this._extractCouponInfo(item),
      categoryName: this._extractCategoryName(item),
      brandName: item.brandName || '',
      clickUrl: item.materialUrl || item.clickUrl || '',
      couponShareUrl: item.couponUrl || '',
      // 京东特有字段
      comments: parseInt(item.comments || 0),
      goodCommentsShare: parseFloat(item.goodCommentsShare || 0),
      owner: item.owner || 'g', // g-自营, p-pop
      isJdSale: item.owner === 'g', // 是否京东自营
      couponUrls: item.couponUrls || [],
      promotionInfo: item.promotionInfo || ''
    };
  }

  /**
   * 提取商品图片列表
   * @private
   */
  _extractImages(item) {
    const images = [];
    
    // 主图
    if (item.imageUrl || item.mainPic) {
      images.push(this.normalizeImageUrl(item.imageUrl || item.mainPic));
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
    
    // 京东API返回的imgList字段（字符串数组）
    if (item.imgList && Array.isArray(item.imgList)) {
      item.imgList.forEach(imgUrl => {
        if (imgUrl) {
          images.push(this.normalizeImageUrl(imgUrl));
        }
      });
    }
    
    // 京东商品详情的imageInfo结构
    if (item.imageInfo && item.imageInfo.imageList) {
      const imageList = Array.isArray(item.imageInfo.imageList) 
        ? item.imageInfo.imageList 
        : [item.imageInfo.imageList];
      
      imageList.forEach(img => {
        if (img.url) {
          images.push(this.normalizeImageUrl(img.url));
        } else if (typeof img === 'string') {
          images.push(this.normalizeImageUrl(img));
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
   * 提取优惠券金额
   * @private
   */
  _extractCouponAmount(item) {
    // 京东优惠券信息在 couponInfo 字段
    if (item.couponInfo && item.couponInfo.couponList) {
      const coupons = Array.isArray(item.couponInfo.couponList) 
        ? item.couponInfo.couponList 
        : [item.couponInfo.couponList];
      
      // 取第一个优惠券的金额
      if (coupons.length > 0 && coupons[0].discount) {
        return parseFloat(coupons[0].discount);
      }
    }
    
    // 备用字段
    if (item.couponDiscount) {
      return parseFloat(item.couponDiscount);
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
    
    // 如果有促销信息
    if (item.promotionInfo) {
      return item.promotionInfo;
    }
    
    return '';
  }

  /**
   * 提取分类名称
   * @private
   */
  _extractCategoryName(item) {
    // 京东分类信息可能在不同字段
    if (item.categoryInfo && item.categoryInfo.cid3Name) {
      return item.categoryInfo.cid3Name;
    }
    if (item.categoryName) {
      return item.categoryName;
    }
    return '';
  }

  /**
   * 提取分类信息
   * @private
   */
  _extractCategoryInfo(item) {
    if (!item.categoryInfo) {
      return null;
    }
    
    return {
      cid1: item.categoryInfo.cid1,
      cid1Name: item.categoryInfo.cid1Name,
      cid2: item.categoryInfo.cid2,
      cid2Name: item.categoryInfo.cid2Name,
      cid3: item.categoryInfo.cid3,
      cid3Name: item.categoryInfo.cid3Name
    };
  }
}

// 导出单例
module.exports = new JdAdapter();
