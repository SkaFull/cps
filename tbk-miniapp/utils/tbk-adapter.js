/**
 * 适配器工厂
 * 根据平台类型获取相应的适配器
 * 为了保持向后兼容，保留原有函数名，但内部使用新的适配器架构
 */

const tbkAdapter = require('../adapters/tbk-adapter.js');
const jdAdapter = require('../adapters/jd-adapter.js');
const dyAdapter = require('../adapters/dy-adapter.js');
const dtkAdapter = require('../adapters/dtk-adapter.js');
const hdkAdapter = require('../adapters/hdk-adapter.js');
const pddAdapter = require('../adapters/pdd-adapter.js');
const { PLATFORM_TYPES } = require('../config/platform.js');

// 适配器映射表
const adapterMap = {
  [PLATFORM_TYPES.TBK]: tbkAdapter,
  [PLATFORM_TYPES.JD]: jdAdapter,
  [PLATFORM_TYPES.DY]: dyAdapter,
  [PLATFORM_TYPES.DTK]: dtkAdapter,
  [PLATFORM_TYPES.HDK]: hdkAdapter,
  [PLATFORM_TYPES.PDD]: pddAdapter
};

/**
 * 获取适配器
 * @param {String} platform - 平台类型，默认为淘宝客
 * @returns {Object} 对应平台的适配器
 */
function getAdapter(platform = PLATFORM_TYPES.TBK) {
  const adapter = adapterMap[platform];
  if (!adapter) {
    console.warn(`未找到平台 ${platform} 的适配器，使用默认淘宝客适配器`);
    return tbkAdapter;
  }
  return adapter;
}

/**
 * 适配商品列表数据（兼容旧接口）
 * @param {Object} apiResponse - 后端返回的API响应数据
 * @param {String} platform - 平台类型，默认为淘宝客
 * @returns {Array} 适配后的商品列表
 */
function adaptGoodsList(apiResponse, platform = PLATFORM_TYPES.TBK) {
  const adapter = getAdapter(platform);
  return adapter.adaptGoodsList(apiResponse);
}

/**
 * 适配商品列表数据（旧版本，保持向后兼容）
 * @deprecated 请使用 adaptGoodsList(apiResponse, platform)
 */
function adaptGoodsListLegacy(apiResponse) {
  // 检查数据有效性
  if (!apiResponse || !apiResponse.data) {
    console.error('淘宝客API响应数据无效:', apiResponse);
    return [];
  }

  const data = apiResponse.data;
  
  // 后端TbkSdkService已经将数据重组为resultList数组
  if (!data.resultList || !Array.isArray(data.resultList)) {
    console.warn('后端返回的resultList为空或格式错误');
    return [];
  }

  const resultList = data.resultList;
  
  // 转换数据格式
  return resultList.map(item => {
    // 计算佣金金额（后端已提供commissionAmount）
    const finalPrice = parseFloat(item.finalPromotionPrice || item.zkFinalPrice || 0);
    const zkPrice = parseFloat(item.zkFinalPrice || 0);
    const commission = parseFloat(item.commissionAmount || 0);
    
    // 处理图片URL - 确保使用https协议
    let pictUrl = item.pictUrl || '';
    if (pictUrl && !pictUrl.startsWith('http')) {
      pictUrl = 'https:' + pictUrl;
    }

    // 处理小图列表
    let smallImages = [];
    if (item.smallImages) {
      // smallImages 可能是字符串（逗号分隔）或数组
      let imagesData = [];
      if (typeof item.smallImages === 'string') {
        // 如果是字符串，尝试解析为JSON或按逗号分割
        try {
          imagesData = JSON.parse(item.smallImages);
          if (!Array.isArray(imagesData)) {
            imagesData = item.smallImages.split(',');
          }
        } catch (e) {
          imagesData = item.smallImages.split(',');
        }
      } else if (Array.isArray(item.smallImages)) {
        imagesData = item.smallImages;
      }
      
      // 处理每个URL：去除引号、补全协议、过滤空值
      smallImages = imagesData.map(url => {
        // 确保url是字符串
        let urlStr = String(url).trim();
        // 去除两边的引号（如果有）
        urlStr = urlStr.replace(/^["']|["']$/g, '');
        // 补全https协议
        if (urlStr && !urlStr.startsWith('http')) {
          urlStr = 'https:' + urlStr;
        }
        return urlStr;
      }).filter(url => url); // 过滤掉空值
    }
    return {
      id: item.itemId || '', // 商品ID
      title: item.title || '商品标题', // 商品标题
      mainPic: pictUrl, // 商品主图
      smallImages: smallImages, // 商品小图列表（已处理为数组）
      actualPrice: finalPrice.toFixed(2), // 最终促销价格
      originalPrice: zkPrice.toFixed(2), // 折扣价
      commission: commission.toFixed(2), // 预估佣金（后端已计算）
      commissionRate: commission > 0 && finalPrice > 0 ? ((commission / finalPrice) * 100).toFixed(2) : '0.00', // 佣金比例
      monthSales: parseInt(item.annualVol || 0), // 年销量
      shopTitle: item.shopTitle || '', // 店铺名称
      couponAmount: 0, // 优惠券金额（从finalPromotionPathList中提取）
      couponInfo: '', // 优惠券信息
      // 其他字段
      categoryName: item.categoryName || '',
      brandName: item.brandName || '',
      clickUrl: item.clickUrl || '',
      couponShareUrl: item.couponShareUrl || '',
      finalPromotionPathList: item.finalPromotionPathList || [],
      promotionTagList: item.promotionTagList || []
    };
  });
}

/**
 * 适配单个商品详情数据
 * @param {Object} apiResponse - 后端返回的API响应数据
 * @param {String} platform - 平台类型，默认为淘宝客
 * @returns {Object} 适配后的商品详情
 */
function adaptGoodsDetail(apiResponse, platform = PLATFORM_TYPES.TBK) {
  const adapter = getAdapter(platform);
  return adapter.adaptGoodsDetail(apiResponse);
}

/**
 * 适配单个商品详情数据（旧版本，保持向后兼容）
 * @deprecated 请使用 adaptGoodsDetail(apiResponse, platform)
 */
function adaptGoodsDetailLegacy(apiResponse) {
  // 检查数据有效性
  if (!apiResponse || !apiResponse.data) {
    console.error('淘宝客API响应数据无效:', apiResponse);
    return null;
  }

  const data = apiResponse.data;
  
  // 后端TbkSdkService已经将数据重组为resultList数组
  if (!data.resultList || !Array.isArray(data.resultList) || data.resultList.length === 0) {
    console.warn('后端返回的resultList为空');
    return null;
  }

  const item = data.resultList[0];
  
  // 计算价格和佣金
  const finalPrice = parseFloat(item.finalPromotionPrice || item.zkFinalPrice || 0);
  const zkPrice = parseFloat(item.zkFinalPrice || 0);
  const commission = parseFloat(item.commissionAmount || 0);
  
  // 处理图片URL
  let pictUrl = item.pictUrl || '';
  if (pictUrl && !pictUrl.startsWith('http')) {
    pictUrl = 'https:' + pictUrl;
  }

  // 处理小图列表
  let images = [];
  if (item.smallImages) {
    try {
      // smallImages可能是字符串或数组
      const smallImagesData = typeof item.smallImages === 'string' 
        ? JSON.parse(item.smallImages) 
        : item.smallImages;
      
      if (Array.isArray(smallImagesData)) {
        images = smallImagesData.map(url => {
          if (url && !url.startsWith('http')) {
            return 'https:' + url;
          }
          return url;
        });
      }
    } catch (e) {
      console.warn('解析smallImages失败:', e);
    }
  }
  
  // 如果没有小图，至少包含主图
  if (images.length === 0 && pictUrl) {
    images = [pictUrl];
  }

  return {
    id: item.itemId || '',
    title: item.title || '商品标题',
    mainPic: pictUrl,
    images: images, // 商品图片列表
    actualPrice: finalPrice.toFixed(2),
    originalPrice: zkPrice.toFixed(2),
    commission: commission.toFixed(2),
    commissionRate: commission > 0 && finalPrice > 0 ? ((commission / finalPrice) * 100).toFixed(2) : '0.00',
    monthSales: parseInt(item.annualVol || 0),
    shopTitle: item.shopTitle || '',
    couponAmount: 0,
    couponInfo: '',
    description: '暂无商品描述',
    // 详情页可能需要的额外字段
    categoryName: item.categoryName || '',
    brandName: item.brandName || '',
    clickUrl: item.clickUrl || '',
    couponShareUrl: item.couponShareUrl || ''
  };
}

/**
 * 适配分类列表数据
 * @param {Object} apiResponse - 后端返回的API响应数据
 * @param {String} platform - 平台类型，默认为淘宝客
 * @returns {Array} 适配后的分类列表
 */
function adaptCategoryList(apiResponse, platform = PLATFORM_TYPES.TBK) {
  const adapter = getAdapter(platform);
  return adapter.adaptCategoryList(apiResponse);
}

/**
 * 检查API响应是否成功
 * @param {Object} response - 后端返回的响应对象
 * @param {String} platform - 平台类型
 * @returns {Boolean} 是否成功
 */
function isApiSuccess(response, platform = PLATFORM_TYPES.TBK) {
  const adapter = getAdapter(platform);
  return adapter.isApiSuccess(response);
}

/**
 * 获取API错误信息
 * @param {Object} response - 后端返回的响应对象
 * @param {String} platform - 平台类型
 * @returns {String} 错误信息
 */
function getApiErrorMessage(response, platform = PLATFORM_TYPES.TBK) {
  const adapter = getAdapter(platform);
  return adapter.getApiErrorMessage(response);
}

module.exports = {
  // 新接口（支持多平台）
  getAdapter,
  adaptGoodsList,
  adaptGoodsDetail,
  adaptCategoryList,
  isApiSuccess,
  getApiErrorMessage,
  
  // 旧接口（向后兼容）
  adaptGoodsListLegacy,
  adaptGoodsDetailLegacy,
  
  // 导出平台类型常量，方便使用
  PLATFORM_TYPES
};
