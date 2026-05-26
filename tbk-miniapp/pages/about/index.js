// pages/about/index.js
Page({
  data: {
    
  },

  onLoad(options) {
    // 设置页面标题
    wx.setNavigationBarTitle({
      title: '关于我们'
    });
  },

  onShareAppMessage() {
    return {
      title: '全域CPS推广平台 - 真实·透明·公平·共赢',
      path: '/pages/about/index'
    };
  }
});
