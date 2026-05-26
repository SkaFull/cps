// pages/auth/login/login.js
const auth = require('../../../utils/auth.js');
const app = getApp();

Page({
  data: {
    loading: false,
    agreeChecked: false
  },

  onLoad(options) {
    // 如果已登录，直接返回上一页或首页
    if (app.globalData.token) {
      this.navigateBack();
    }
  },

  /**
   * 切换协议勾选状态
   */
  toggleAgreement() {
    this.setData({
      agreeChecked: !this.data.agreeChecked
    });
  },

  /**
   * 打开用户协议页面
   */
  openUserAgreement() {
    wx.navigateTo({
      url: '/pages/agreement/user-agreement/user-agreement'
    });
  },

  /**
   * 打开隐私政策页面
   */
  openPrivacyPolicy() {
    wx.navigateTo({
      url: '/pages/agreement/privacy-policy/privacy-policy'
    });
  },

  /**
   * 微信一键登录
   */
  handleLogin() {
    if (this.data.loading) return;

    // 检查是否同意用户协议
    if (!this.data.agreeChecked) {
      wx.showModal({
        title: '温馨提示',
        content: '请先阅读并同意《用户协议》和《隐私政策》',
        confirmText: '去查看',
        cancelText: '取消',
        success: (res) => {
          if (res.confirm) {
            this.openUserAgreement();
          }
        }
      });
      return;
    }
    
    this.setData({ loading: true });
    
    auth.wxLogin()
      .then(res => {
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });
        
        // 延迟返回，让用户看到成功提示
        setTimeout(() => {
          this.navigateBack();
        }, 1500);
      })
      .catch(err => {
        this.setData({ loading: false });
        wx.showToast({
          title: err.message || '登录失败',
          icon: 'none',
          duration: 2000
        });
      });
  },

  /**
   * 返回上一页或首页
   */
  navigateBack() {
    const pages = getCurrentPages();
    if (pages.length > 1) {
      wx.navigateBack();
    } else {
      wx.switchTab({
        url: '/pages/index/index'
      });
    }
  },

  /**
   * 取消登录
   */
  handleCancel() {
    this.navigateBack();
  }
});
