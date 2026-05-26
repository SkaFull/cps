/**
 * 基础适配器（抽象类）
 * 定义所有平台适配器需要实现的标准接口
 */

class BaseAdapter {
  /**
   * 构造函数
   * @param {String} platformType - 平台类型
   */
  constructor(platformType) {
    if (new.target === BaseAdapter) {
      throw new Error('BaseAdapter是抽象类，不能直接实例化');
    }
    this.platformType = platformType;
  }

  /**
   * 适配商品列表数据
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的商品列表
   */
  adaptGoodsList(apiResponse) {
    throw new Error('子类必须实现adaptGoodsList方法');
  }

  /**
   * 适配单个商品详情数据
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Object} 适配后的商品详情
   */
  adaptGoodsDetail(apiResponse) {
    throw new Error('子类必须实现adaptGoodsDetail方法');
  }

  /**
   * 适配商品分类列表
   * @param {Object} apiResponse - 后端返回的API响应数据
   * @returns {Array} 适配后的分类列表
   */
  adaptCategoryList(apiResponse) {
    throw new Error('子类必须实现adaptCategoryList方法');
  }

  /**
   * 检查API响应是否成功
   * @param {Object} response - 后端返回的响应对象
   * @returns {Boolean} 是否成功
   */
  isApiSuccess(response) {
    return response && response.code === 200;
  }

  /**
   * 获取API错误信息
   * @param {Object} response - 后端返回的响应对象
   * @returns {String} 错误信息
   */
  getApiErrorMessage(response) {
    if (!response) {
      return '网络请求失败';
    }
    if (response.msg) {
      return response.msg;
    }
    if (response.message) {
      return response.message;
    }
    return '未知错误';
  }

  /**
   * 处理图片URL - 确保使用https协议
   * @param {String} url - 图片URL
   * @returns {String} 处理后的URL
   */
  normalizeImageUrl(url) {
    if (!url) return '';
    if (url.startsWith('http://')) {
      return url.replace('http://', 'https://');
    }
    if (!url.startsWith('http')) {
      return 'https:' + url;
    }
    return url;
  }

  /**
   * 处理图片数组
   * @param {Array|String} images - 图片数组或字符串
   * @returns {Array} 处理后的图片数组
   */
  normalizeImages(images) {
    if (!images) return [];
    
    let imageList = [];
    
    // 如果是字符串，尝试解析
    if (typeof images === 'string') {
      try {
        imageList = JSON.parse(images);
        if (!Array.isArray(imageList)) {
          imageList = images.split(',');
        }
      } catch (e) {
        imageList = images.split(',');
      }
    } else if (Array.isArray(images)) {
      imageList = images;
    }
    
    // 处理每个URL
    return imageList
      .map(url => {
        let urlStr = String(url).trim();
        urlStr = urlStr.replace(/^["']|["']$/g, '');
        return this.normalizeImageUrl(urlStr);
      })
      .filter(url => url);
  }

  /**
   * 格式化价格
   * @param {Number|String} price - 价格
   * @param {Number} decimals - 小数位数，默认2位
   * @returns {String} 格式化后的价格
   */
  formatPrice(price, decimals = 2) {
    const priceNum = parseFloat(price || 0);
    return priceNum.toFixed(decimals);
  }

  /**
   * 计算佣金比例
   * @param {Number} commission - 佣金金额
   * @param {Number} price - 商品价格
   * @returns {String} 佣金比例（百分比）
   */
  calculateCommissionRate(commission, price) {
    const commissionNum = parseFloat(commission || 0);
    const priceNum = parseFloat(price || 0);
    
    if (commissionNum > 0 && priceNum > 0) {
      return ((commissionNum / priceNum) * 100).toFixed(2);
    }
    return '0.00';
  }
}

module.exports = BaseAdapter;
