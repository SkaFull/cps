/**
 * 多平台API统一封装
 * 提供统一的接口调用方式，支持淘宝客、京东联盟等多个平台
 */

const request = require('../utils/request.js');
const { PLATFORM_TYPES, getPlatformConfig } = require('../config/platform.js');
const { getAdapter } = require('../utils/tbk-adapter.js');

/**
 * 获取商品列表
 * @param {String} platform - 平台类型 (tbk/jd/pdd)，可选
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @param {String} params.keyword - 搜索关键词
 * @param {String} params.categoryId - 分类ID
 * @param {String} params.sortType - 排序类型
 * @returns {Promise}
 */
function getGoodsList(platform, params) {
  // 兼容两种调用方式：
  // 1. getGoodsList(platform, params) - 推荐方式
  // 2. getGoodsList(params) - 兼容旧代码
  if (typeof platform === 'object') {
    params = platform;
    platform = params.platform || PLATFORM_TYPES.TBK;
  }
  
  if (!params) {
    params = {};
  }
  
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  // 构建API路径
  const apiPath = `${platformConfig.apiPrefix}/goods/list`;
  
  // 调用接口
  return request.publicGet(apiPath, params)
    .then(res => {
      // 使用对应平台的适配器转换数据
      const adapter = getAdapter(platform);
      // adaptGoodsList已返回商品数组，直接返回即可
      return adapter.adaptGoodsList(res);
    });
}

/**
 * 获取热门商品
 * @param {String} platform - 平台类型，可选
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @returns {Promise}
 */
function getHotGoods(platform, params) {
  // 兼容两种调用方式
  if (typeof platform === 'object') {
    params = platform;
    platform = params.platform || PLATFORM_TYPES.TBK;
  }
  
  if (!params) {
    params = {};
  }
  
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  const apiPath = `${platformConfig.apiPrefix}/goods/hot`;
  
  return request.publicGet(apiPath, params)
    .then(res => {
      const adapter = getAdapter(platform);
      // adaptGoodsList已返回商品数组，直接返回即可
      return adapter.adaptGoodsList(res);
    });
}

/**
 * 获取商品详情
 * @param {String} platform - 平台类型，可选
 * @param {Object} params - 查询参数
 * @param {String} params.id - 商品ID
 * @returns {Promise}
 */
function getGoodsDetail(platform, params) {
  // 兼容两种调用方式
  if (typeof platform === 'object') {
    params = platform;
    platform = params.platform || PLATFORM_TYPES.TBK;
  }
  
  if (!params) {
    params = {};
  }
  
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  if (!params.id) {
    return Promise.reject(new Error('商品ID不能为空'));
  }
  
  const apiPath = `${platformConfig.apiPrefix}/goods/detail`;
  
  return request.publicGet(apiPath, { id: params.id })
    .then(res => {
      const adapter = getAdapter(platform);
      return {
        ...res,
        data: adapter.adaptGoodsDetail(res)
      };
    });
}

/**
 * 获取分类列表
 * @param {Object} params - 查询参数
 * @param {String} params.platform - 平台类型
 * @returns {Promise}
 */
function getCategoryList(params = {}) {
  const platform = params.platform || PLATFORM_TYPES.TBK;
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  const apiPath = `${platformConfig.apiPrefix}/categories`;
  
  return request.publicGet(apiPath, params)
    .then(res => {
      const adapter = getAdapter(platform);
      return {
        ...res,
        data: adapter.adaptCategoryList(res)
      };
    });
}

/**
 * 获取推广链接
 * @param {Object} params - 查询参数
 * @param {String} params.platform - 平台类型
 * @param {String} params.id - 商品ID
 * @param {String} params.positionId - 推广位ID（可选）
 * @returns {Promise}
 */
function getPromotionLink(params = {}) {
  const platform = params.platform || PLATFORM_TYPES.TBK;
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  if (!params.id) {
    return Promise.reject(new Error('商品ID不能为空'));
  }
  
  const apiPath = `${platformConfig.apiPrefix}/promotion/link`;
  
  return request.get(apiPath, params);
}

/**
 * 生成淘口令/京口令
 * @param {Object} params - 参数
 * @param {String} params.platform - 平台类型
 * @param {String} params.text - 文案
 * @param {String} params.url - 推广链接
 * @param {String} params.logo - 商品图片（可选）
 * @returns {Promise}
 */
function generateCode(params = {}) {
  const platform = params.platform || PLATFORM_TYPES.TBK;
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  // 淘宝客使用淘口令，京东使用京口令
  const apiPath = platform === PLATFORM_TYPES.TBK 
    ? `${platformConfig.apiPrefix}/getTpwd`
    : `${platformConfig.apiPrefix}/getJdCode`;
  
  return request.publicGet(apiPath, params);
}

/**
 * 获取淘口令/京口令/推广链接
 * 根据不同平台调用对应的接口
 * @param {String} platform - 平台类型
 * @param {Object} params - 参数
 * @param {String} params.goodsId - 商品ID（大淘客需要）
 * @param {String} params.couponId - 优惠券ID（大淘客可选）
 * @param {String} params.goodsName - 商品名称（大淘客可选，用于设置淘口令右符号）
 * @param {String} params.text - 文案（淘宝客需要）
 * @param {String} params.url - 推广链接（淘宝客需要）
 * @param {String} params.logo - 商品图片（可选）
 * @param {String} params.itemId - 商品ID（京东需要）
 * @returns {Promise}
 */
function getTpwd(platform, params) {
  // 兼容两种调用方式
  if (typeof platform === 'object') {
    params = platform;
    platform = params.platform || PLATFORM_TYPES.TBK;
  }
  
  if (!params) {
    params = {};
  }
  
  console.log('========== getTpwd 调用信息 ==========');
  console.log('传入的平台类型:', platform);
  console.log('传入的参数:', params);
  console.log('PLATFORM_TYPES.DTK:', PLATFORM_TYPES.DTK);
  console.log('platform === PLATFORM_TYPES.DTK:', platform === PLATFORM_TYPES.DTK);
  console.log('====================================');
  
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  console.log('平台配置:', platformConfig);
  
  // 根据平台类型调用不同的API
  let apiPath;
  let requestParams = { ...params };
  
  if (platform === PLATFORM_TYPES.JD) {
    // 京东：获取推广链接
    console.log('→ 匹配到京东平台');
    apiPath = `${platformConfig.apiPrefix}/getPromotionUrl`;
    requestParams = {
      itemId: params.goodsId || params.itemId
    };
  } else if (platform === PLATFORM_TYPES.DTK) {
    // 大淘客：调用高效转链接口获取淘口令
    console.log('→ 匹配到大淘客平台');
    apiPath = `${platformConfig.apiPrefix}/goods/tpwd`;
    requestParams = {
      goodsId: params.goodsId,
      couponId: params.couponId || '',
      goodsName: params.goodsName || ''
    };
  } else if (platform === PLATFORM_TYPES.PDD) {
    // 拼多多：生成推广链接
    console.log('→ 匹配到拼多多平台');
    apiPath = `${platformConfig.apiPrefix}/promotion/url/generate`;
    requestParams = {
      goodsSign: params.goodsSign || params.goodsId,
      generateShortUrl: params.generateShortUrl || false
    };
  } else {
    // 淘宝客和其他平台：使用淘口令接口
    console.log('→ 匹配到淘宝客或其他平台');
    apiPath = `${platformConfig.apiPrefix}/getTpwd`;
    requestParams = {
      text: params.text,
      url: params.url,
      logo: params.logo
    };
  }
  
  console.log('最终API路径:', apiPath);
  console.log('最终请求参数:', requestParams);
  
  // 传递邀请码和用户ID（如果有）
  if (params.invitationCode) {
    requestParams.invitationCode = params.invitationCode;
  }
  if (params.userId) {
    requestParams.userId = params.userId;
  }
  
  return request.publicGet(apiPath, requestParams)
    .then(res => {
      // 使用对应平台的适配器转换数据
      const adapter = getAdapter(platform);
      if (adapter.adaptTpwdResponse) {
        return adapter.adaptTpwdResponse(res);
      }
      // 如果适配器没有提供淘口令适配方法，直接返回原始数据
      return res;
    });
}

/**
 * 搜索商品
 * @param {String} platform - 平台类型，可选
 * @param {Object} params - 查询参数
 * @param {String} params.keyword - 搜索关键词
 * @param {Number} params.page - 页码
 * @param {Number} params.pageSize - 每页数量
 * @returns {Promise}
 */
function searchGoods(platform, params) {
  // 兼容两种调用方式
  if (typeof platform === 'object') {
    params = platform;
    platform = params.platform || PLATFORM_TYPES.TBK;
  }
  
  if (!params) {
    params = {};
  }
  
  const platformConfig = getPlatformConfig(platform);
  
  if (!platformConfig) {
    return Promise.reject(new Error(`不支持的平台: ${platform}`));
  }
  
  if (!params.keyword) {
    return Promise.reject(new Error('搜索关键词不能为空'));
  }
  
  const apiPath = `${platformConfig.apiPrefix}/goods/search`;
  
  return request.publicGet(apiPath, params)
    .then(res => {
      const adapter = getAdapter(platform);
      // adaptGoodsList已返回商品数组，直接返回即可
      return adapter.adaptGoodsList(res);
    });
}

module.exports = {
  getGoodsList,
  getHotGoods,
  getGoodsDetail,
  getCategoryList,
  getPromotionLink,
  generateCode,
  getTpwd,
  searchGoods
};
