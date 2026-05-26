// utils/request.js
const auth = require('./auth.js');

// 默认的 API 地址，可以在 app.js 中配置覆盖
// 重要：请修改为你的实际后端API地址
// 开发环境示例：'http://localhost:8080' 或 'http://192.168.1.100:8080'
// 生产环境示例：'https://api.yourdomain.com'
const DEFAULT_API_URL = 'http://localhost:8080';

/**
 * 通用请求方法
 * 支持两种调用方式：
 * 1. request({url, method, data, needAuth}) - 对象参数
 * 2. request(url, method, data, needAuth) - 分离参数
 */
function request(urlOrOptions, method, data, needAuth = true) {
  return new Promise((resolve, reject) => {
    // 兼容两种调用方式
    let url, requestMethod, requestData, requireAuth;
    
    if (typeof urlOrOptions === 'object') {
      // 对象参数方式：request({url, method, data, needAuth})
      url = urlOrOptions.url;
      requestMethod = urlOrOptions.method || 'GET';
      requestData = urlOrOptions.data;
      requireAuth = urlOrOptions.needAuth !== undefined ? urlOrOptions.needAuth : true;
    } else {
      // 分离参数方式：request(url, method, data, needAuth)
      url = urlOrOptions;
      requestMethod = method;
      requestData = data;
      requireAuth = needAuth;
    }
    
    const header = {
      'Content-Type': 'application/json'
    };

    // 如果需要认证，添加token
    if (requireAuth) {
      const token = auth.getToken();
      if (token) {
        header['Authorization'] = 'Bearer ' + token;
      }
    }

    // 获取 API 地址
    const app = getApp();
    const apiUrl = (app && app.globalData.apiUrl) || DEFAULT_API_URL;
    
    const fullUrl = apiUrl + url;
    
    console.log('发起请求:', {
      url: fullUrl,
      method: requestMethod,
      needAuth: requireAuth,
      hasToken: !!header['Authorization']
    });
    
    // 检查API地址是否配置
    console.log('apiUrl' + apiUrl);
    if (apiUrl === '') {
      console.error('API地址未配置！请在 app.js 中设置 apiUrl');
      const errMsg = 'API地址未配置，请在 app.js 的 globalData 中设置 apiUrl';
      wx.showModal({
        title: '配置错误',
        content: errMsg,
        showCancel: false
      });
      reject(new Error(errMsg));
      return;
    }

    wx.request({
      url: fullUrl,
      method: requestMethod,
      data: requestData,
      header: header,
      timeout: 10000, // 10秒超时
      success: res => {
        console.log('请求响应:', {
          url: fullUrl,
          statusCode: res.statusCode,
          data: res.data
        });
        
        // 处理响应
        if (res.statusCode === 200) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          // Token过期或未登录
          handleUnauthorized();
          reject(new Error('未授权，请重新登录'));
        } else {
          const errMsg = (res.data && res.data.msg) || '请求失败';
          console.error('请求失败:', errMsg, res);
          wx.showToast({
            title: errMsg,
            icon: 'none',
            duration: 3000
          });
          reject(res.data || new Error(errMsg));
        }
      },
      fail: err => {
        console.error('请求异常:', {
          url: fullUrl,
          error: err
        });
        
        let errMsg = '网络请求失败';
        if (err.errMsg) {
          if (err.errMsg.includes('timeout')) {
            errMsg = '请求超时，请检查网络连接';
          } else if (err.errMsg.includes('fail')) {
            errMsg = '网络连接失败，请检查服务器地址';
          }
        }
        
        wx.showToast({
          title: errMsg,
          icon: 'none',
          duration: 3000
        });
        reject(err);
      }
    });
  });
}

/**
 * 处理未授权情况
 */
function handleUnauthorized() {
  // 清除登录信息
  auth.removeToken();
  
  const app = getApp();
  if (app && app.logout) {
    app.logout();
  }
  
  wx.showModal({
    title: '提示',
    content: '登录已过期，请重新登录',
    showCancel: false,
    success: () => {
      wx.reLaunch({
        url: '/pages/auth/login/login'
      });
    }
  });
}

/**
 * GET请求（需要认证）
 */
function get(url, data) {
  return request(url, 'GET', data, true);
}

/**
 * POST请求（需要认证）
 */
function post(url, data) {
  return request(url, 'POST', data, true);
}

/**
 * PUT请求（需要认证）
 */
function put(url, data) {
  return request(url, 'PUT', data, true);
}

/**
 * DELETE请求（需要认证）
 */
function del(url, data) {
  return request(url, 'DELETE', data, true);
}

/**
 * GET请求（公开接口，不需要认证）
 */
function publicGet(url, data) {
  return request(url, 'GET', data, false);
}

/**
 * POST请求（公开接口，不需要认证）
 */
function publicPost(url, data) {
  return request(url, 'POST', data, false);
}

/**
 * POST请求，发送表单数据（application/x-www-form-urlencoded）
 * 适用于后端使用 @RequestParam 接收参数的场景
 */
function publicPostForm(url, data) {
  return new Promise((resolve, reject) => {
    const header = {
      'Content-Type': 'application/x-www-form-urlencoded'
    };

    // 获取 API 地址
    const app = getApp();
    const apiUrl = (app && app.globalData.apiUrl) || DEFAULT_API_URL;
    
    const fullUrl = apiUrl + url;
    
    // 将对象转换为URL编码格式的字符串
    let formData = '';
    if (data && typeof data === 'object') {
      const params = [];
      for (const key in data) {
        if (data.hasOwnProperty(key) && data[key] !== undefined && data[key] !== null) {
          params.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
        }
      }
      formData = params.join('&');
    }
    
    console.log('发起表单请求:', {
      url: fullUrl,
      method: 'POST',
      originalData: data,
      formData: formData
    });
    
    // 检查API地址是否配置
    if (apiUrl === '') {
      console.error('API地址未配置！请在 app.js 中设置 apiUrl');
      const errMsg = 'API地址未配置，请在 app.js 的 globalData 中设置 apiUrl';
      wx.showModal({
        title: '配置错误',
        content: errMsg,
        showCancel: false
      });
      reject(new Error(errMsg));
      return;
    }

    wx.request({
      url: fullUrl,
      method: 'POST',
      data: formData,
      header: header,
      timeout: 10000,
      success: res => {
        console.log('请求响应:', {
          url: fullUrl,
          statusCode: res.statusCode,
          data: res.data
        });
        
        // 详细调试信息
        console.log('=== publicPostForm 响应详情 ===');
        console.log('响应状态码:', res.statusCode);
        console.log('响应数据类型:', typeof res.data);
        console.log('响应数据:', JSON.stringify(res.data, null, 2));
        if (res.data) {
          console.log('res.data.code:', res.data.code);
          console.log('res.data.msg:', res.data.msg);
          console.log('res.data.data:', res.data.data);
        }
        console.log('===============================');
        
        if (res.statusCode === 200) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          handleUnauthorized();
          reject(new Error('未授权，请重新登录'));
        } else {
          const errMsg = (res.data && res.data.msg) || '请求失败';
          console.error('请求失败:', errMsg, res);
          wx.showToast({
            title: errMsg,
            icon: 'none',
            duration: 3000
          });
          reject(res.data || new Error(errMsg));
        }
      },
      fail: err => {
        console.error('请求异常:', {
          url: fullUrl,
          error: err
        });
        
        let errMsg = '网络请求失败';
        if (err.errMsg) {
          if (err.errMsg.includes('timeout')) {
            errMsg = '请求超时，请检查网络连接';
          } else if (err.errMsg.includes('fail')) {
            errMsg = '网络连接失败，请检查服务器地址';
          }
        }
        
        wx.showToast({
          title: errMsg,
          icon: 'none',
          duration: 3000
        });
        reject(err);
      }
    });
  });
}

/**
 * 上传文件
 */
function upload(url, filePath, name = 'file', formData = {}) {
  return new Promise((resolve, reject) => {
    const header = {};
    
    // 如果需要认证，添加token
    const token = auth.getToken();
    if (token) {
      header['Authorization'] = 'Bearer ' + token;
    }

    // 获取 API 地址
    const app = getApp();
    const apiUrl = (app && app.globalData.apiUrl) || DEFAULT_API_URL;

    wx.uploadFile({
      url: apiUrl + url,
      filePath: filePath,
      name: name,
      formData: formData,
      header: header,
      success: res => {
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          resolve(data);
        } else {
          wx.showToast({
            title: data.msg || '上传失败',
            icon: 'none'
          });
          reject(data);
        }
      },
      fail: err => {
        console.error('上传失败:', err);
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

module.exports = {
  request,
  get,
  post,
  put,
  del,
  publicGet,
  publicPost,
  publicPostForm,
  upload
};
