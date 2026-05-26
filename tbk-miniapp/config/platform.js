/**
 * 多平台配置
 * 支持淘宝客、京东联盟、多多进宝等多个平台
 */

// 平台类型常量
const PLATFORM_TYPES = {
  TBK: 'tbk',      // 淘宝客
  JD: 'jd',        // 京东联盟
  PDD: 'pdd',      // 多多进宝
  DTK: 'dtk',      // 大淘客
  HDK: 'hdk',      // 好单库
  DY: 'dy'         // 抖音联盟
};

// 平台配置
const PLATFORM_CONFIG = {
  // 淘宝客配置
  [PLATFORM_TYPES.TBK]: {
    name: '淘宝客',
    enabled: true,
    color: '#ff6600',
    icon: '/images/platform/taobao.png',
    // API路径前缀
    apiPrefix: '/miniapp/tbk'
  },
  
  // 京东联盟配置
  [PLATFORM_TYPES.JD]: {
    name: '京东联盟',
    enabled: true,
    color: '#e4393c',
    icon: '/images/platform/jd.png',
    // API路径前缀
    apiPrefix: '/miniapp/jd'
  },
  
  // 多多进宝配置
  [PLATFORM_TYPES.PDD]: {
    name: '多多进宝',
    enabled: true, // 已启用PDD联盟
    color: '#e02e24',
    icon: '/images/platform/pdd.png',
    // API路径前缀
    apiPrefix: '/miniapp/pdd'
  },
  
  // 大淘客配置
  [PLATFORM_TYPES.DTK]: {
    name: '大淘客',
    enabled: true,
    color: '#1890ff',
    icon: '/images/platform/dtk.png',
    // API路径前缀
    apiPrefix: '/miniapp/dtk'
  },

  // 好单库配置
  [PLATFORM_TYPES.HDK]: {
    name: '好单库',
    enabled: true,
    color: '#52c41a',
    icon: '/images/platform/hdk.png',
    // API路径前缀
    apiPrefix: '/miniapp/hdk'
  },

  // 抖音联盟配置
  [PLATFORM_TYPES.DY]: {
    name: '抖音联盟',
    enabled: true,
    color: '#000000',
    icon: '/images/platform/douyin.png',
    // API路径前缀
    apiPrefix: '/miniapp/dy'
  }
};

/**
 * 获取已启用的平台列表
 */
function getEnabledPlatforms() {
  return Object.entries(PLATFORM_CONFIG)
    .filter(([key, config]) => config.enabled)
    .map(([key, config]) => ({
      type: key,
      ...config
    }));
}

/**
 * 获取平台配置
 * @param {String} platformType - 平台类型
 */
function getPlatformConfig(platformType) {
  return PLATFORM_CONFIG[platformType] || null;
}

/**
 * 检查平台是否启用
 * @param {String} platformType - 平台类型
 */
function isPlatformEnabled(platformType) {
  const config = PLATFORM_CONFIG[platformType];
  return config && config.enabled;
}

module.exports = {
  PLATFORM_TYPES,
  PLATFORM_CONFIG,
  getEnabledPlatforms,
  getPlatformConfig,
  isPlatformEnabled
};
