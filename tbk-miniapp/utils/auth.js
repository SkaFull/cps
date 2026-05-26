// utils/auth.js
const TOKEN_KEY = 'token';

// 延迟加载 request 模块以避免循环依赖
let request = null;
function getRequest() {
  if (!request) {
    request = require('./request.js');
  }
  return request;
}

/**
 * 微信小程序登录
 * 注意：微信已废弃 getUserProfile，改用手机号快速验证+头像昵称填写的方式
 * 这里简化为只使用 wx.login 获取 code，由后端通过 code 换取 openid
 */
function wxLogin() {
  return new Promise((resolve, reject) => {
    console.log('开始微信登录流程...');
    
    // 1. 获取微信登录code
    wx.login({
      success: loginRes => {
        console.log('wx.login 成功:', loginRes);
        
        if (loginRes.code) {
          // 2. 直接使用code登录（不再使用已废弃的getUserProfile）
          // 后端通过code获取openid，首次登录创建账号
          doLogin(loginRes.code, null)
            .then(res => {
              console.log('登录成功:', res);
              resolve(res);
            })
            .catch(err => {
              console.error('登录失败:', err);
              reject(err);
            });
        } else {
          console.error('获取微信code失败:', loginRes.errMsg);
          reject(new Error('获取微信code失败：' + loginRes.errMsg));
        }
      },
      fail: loginErr => {
        console.error('微信登录失败:', loginErr);
        reject(new Error('微信登录失败：' + (loginErr.errMsg || '未知错误')));
      }
    });
  });
}

/**
 * 调用后端登录接口
 */
function doLogin(code, userInfo) {
  return new Promise((resolve, reject) => {
    console.log('调用后端登录接口, code:', code);
    wx.showLoading({ title: '登录中...' });
    
    // 构建请求参数（userInfo可能为null）
    const loginData = {
      code: code
    };
    
    if (userInfo) {
      loginData.nickName = userInfo.nickName;
      loginData.avatarUrl = userInfo.avatarUrl;
      loginData.gender = userInfo.gender;
      loginData.province = userInfo.province;
      loginData.city = userInfo.city;
      loginData.country = userInfo.country;
    }
    
    console.log('登录请求数据:', loginData);
    
    // 使用 publicPostForm 发送表单数据，后端使用 @RequestParam 接收
    getRequest().publicPostForm('/miniapp/auth/login', loginData)
      .then(res => {
        console.log('后端登录接口响应:', res);
        wx.hideLoading();
        
        // 详细记录响应数据用于调试
        console.log('=== 登录响应调试信息 ===');
        console.log('完整响应对象:', JSON.stringify(res, null, 2));
        console.log('res存在:', !!res);
        console.log('res.code:', res ? res.code : 'res为空');
        console.log('res.data:', res ? res.data : 'res为空');
        console.log('res.data类型:', res && res.data ? typeof res.data : 'N/A');
        console.log('========================');
        
        if (res && res.code === 200) {
          // 兼容两种返回格式：
          // 1. {code: 200, data: {token, user, wechatUser}}  标准格式
          // 2. {code: 200, token, user, wechatUser}  扁平格式
          const responseData = res.data || res;  // 如果有data字段则用data，否则用res本身
          
          // 提取token和用户信息
          const token = responseData.token;
          const user = responseData.user || responseData.userInfo;
          const wechatUser = responseData.wechatUser;
          
          if (!token) {
            console.error('登录失败: token为空');
            wx.showToast({
              title: '登录失败：未获取到token',
              icon: 'none',
              duration: 3000
            });
            reject(new Error('token为空'));
            return;
          }
          
          console.log('登录成功，token:', token);
          console.log('用户信息:', user);
          console.log('微信用户信息:', wechatUser);
          
          setToken(token);
          
          // 构建完整的用户信息对象，包含token、isAgent、invitationCode、boundPlatforms等
          const completeUserInfo = {
            ...user,
            token: token,
            isAgent: user.isAgent || responseData.isAgent || false,
            invitationCode: user.invitationCode || responseData.invitationCode || '',
            boundPlatforms: responseData.boundPlatforms || [] // 新增：传递绑定的联盟平台列表
          };
          
          console.log('完整用户信息（包含boundPlatforms）:', completeUserInfo);
          console.log('boundPlatforms数量:', completeUserInfo.boundPlatforms.length);
          
          // 更新 app 全局数据
          const app = getApp();
          if (app && app.login) {
            app.login(completeUserInfo);
          }
          
          // 返回完整数据
          resolve({
            token: token,
            user: user,
            wechatUser: wechatUser
          });
        } else {
          const errMsg = (res && (res.msg || res.message)) || '登录失败';
          console.error('登录失败:', errMsg, res);
          wx.showToast({
            title: errMsg,
            icon: 'none',
            duration: 3000
          });
          reject(new Error(errMsg));
        }
      })
      .catch(err => {
        console.error('登录请求异常:', err);
        wx.hideLoading();
        
        const errMsg = err.message || err.errMsg || '网络请求失败，请检查API地址配置';
        wx.showToast({
          title: errMsg,
          icon: 'none',
          duration: 3000
        });
        reject(err);
      });
  });
}

/**
 * 检查登录状态，未登录则引导登录
 */
function requireLogin(callback) {
  const app = getApp();
  if (app && getToken()) {
    // 已登录，执行回调
    if (typeof callback === 'function') {
      callback();
    }
    return true;
  } else {
    // 未登录，引导登录
    wx.showModal({
      title: '提示',
      content: '此功能需要登录，是否立即登录？',
      success: res => {
        if (res.confirm) {
          wx.navigateTo({
            url: '/pages/auth/login/login'
          });
        }
      }
    });
    return false;
  }
}

/**
 * 退出登录
 */
function logout() {
  wx.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: res => {
      if (res.confirm) {
        removeToken();
        
        // 清除 app 全局数据
        const app = getApp();
        if (app) {
          app.logout();
        }
        
        wx.showToast({
          title: '已退出登录',
          icon: 'success'
        });
        
        // 跳转到首页
        wx.reLaunch({
          url: '/pages/index/index'
        });
      }
    }
  });
}

/**
 * 刷新Token
 */
function refreshToken() {
  return new Promise((resolve, reject) => {
    getRequest().post('/miniapp/auth/refresh', {})
      .then(res => {
        if (res.code === 200 && res.data.token) {
          setToken(res.data.token);
          resolve(res.data.token);
        } else {
          reject(new Error('刷新Token失败'));
        }
      })
      .catch(err => {
        reject(err);
      });
  });
}

/**
 * 检查是否需要更新用户信息
 */
function checkUserInfoUpdate() {
  // 检查用户信息是否过期（例如：7天更新一次）
  const app = getApp();
  const userInfo = app ? app.globalData.userInfo : null;
  if (!userInfo || !userInfo.updateTime) {
    return true;
  }
  
  const updateTime = new Date(userInfo.updateTime).getTime();
  const now = Date.now();
  const sevenDays = 7 * 24 * 60 * 60 * 1000;
  
  return (now - updateTime) > sevenDays;
}

/**
 * 获取 Token
 */
function getToken() {
  try {
    return wx.getStorageSync(TOKEN_KEY) || '';
  } catch (e) {
    console.error('获取 Token 失败:', e);
    return '';
  }
}

/**
 * 设置 Token
 */
function setToken(token) {
  try {
    wx.setStorageSync(TOKEN_KEY, token);
  } catch (e) {
    console.error('保存 Token 失败:', e);
  }
}

/**
 * 移除 Token
 */
function removeToken() {
  try {
    wx.removeStorageSync(TOKEN_KEY);
  } catch (e) {
    console.error('移除 Token 失败:', e);
  }
}

module.exports = {
  wxLogin,
  doLogin,
  requireLogin,
  logout,
  refreshToken,
  checkUserInfoUpdate,
  getToken,
  setToken,
  removeToken
};
