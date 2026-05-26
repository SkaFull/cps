// pages/agent/apply/apply.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');
const app = getApp();

// 联盟平台类型选项（保留作为默认值，实际会从字典表动态加载）
const DEFAULT_PLATFORM_TYPE_OPTIONS = [
  { value: 'tbk', label: '淘宝客', checked: false },
  { value: 'jd', label: '京东', checked: false },
  { value: 'pdd', label: '拼多多', checked: false },
  { value: 'dtk', label: '大淘客', checked: false }
];

Page({
  data: {
    formData: {
      realName: '',
      phone: '',
      wechat: '',
      applyPlatformTypes: '',  // 逗号分隔的多个联盟类型，如 "tbk,jd"
      referrerInvitationCode: '',  // 上级邀请码（推荐人邀请码）
      remark: ''
    },
    // 深拷贝选项，避免修改常量
    platformTypeOptions: DEFAULT_PLATFORM_TYPE_OPTIONS.map(o => Object.assign({}, o)),
    selectedPlatformTypes: [],  // 已选中的联盟类型数组
    loading: false,
    hasApplied: false,
    applyStatus: '', // pending, approved, rejected
    applyInfo: null,
    platformsLoaded: false,  // 标记联盟类型是否已加载
    // 自定义提示框
    toastShow: false,
    toastType: 'info',
    toastTitle: '',
    toastMessage: ''
  },

  onLoad() {
    // 检查登录状态
    if (!app.globalData.token) {
      wx.showModal({
        title: '提示',
        content: '请先登录',
        showCancel: false,
        success: () => {
          wx.navigateTo({
            url: '/pages/auth/login/login'
          });
        }
      });
      return;
    }
    
    // 检查是否已申请
    this.checkApplyStatus();
    
    // 加载系统默认的联盟类型（如果当前不是代理）
    this.loadPlatformTypes();
  },

  onShow() {
    // 更新登录状态
    if (app.globalData.token) {
      this.checkApplyStatus();
    }
  },

  /**
   * 加载联盟平台类型
   * 从字典表获取系统默认的联盟类型
   */
  loadPlatformTypes() {
    console.log('加载联盟平台类型...');
    
    request.publicGet('/system/dict/data/type/union_platform_type')
      .then(res => {
        if (res.code === 200 && res.data && res.data.length > 0) {
          // 将字典数据转换为平台选择器需要的格式
          const platformTypeOptions = res.data.map(dict => {
            return {
              value: dict.dictValue,
              label: dict.dictLabel,
              checked: false
            };
          });
          
          console.log('从字典表获取到', platformTypeOptions.length, '个联盟类型:', platformTypeOptions);
          
          this.setData({
            platformTypeOptions: platformTypeOptions,
            platformsLoaded: true
          });
        } else {
          console.warn('字典表中没有联盟平台类型数据，使用默认配置');
          // 使用默认配置
          this.setData({
            platformTypeOptions: DEFAULT_PLATFORM_TYPE_OPTIONS.map(o => Object.assign({}, o)),
            platformsLoaded: true
          });
        }
      })
      .catch(err => {
        console.error('从字典表获取联盟平台类型失败:', err);
        // 出错时使用默认配置
        this.setData({
          platformTypeOptions: DEFAULT_PLATFORM_TYPE_OPTIONS.map(o => Object.assign({}, o)),
          platformsLoaded: true
        });
      });
  },

  /**
   * 检查申请状态
   */
  checkApplyStatus() {
    request.get('/miniapp/tbk/agent/status')
      .then(res => {
        if (res.code === 200) {
          const data = res.data;
          
          if (data && data.status) {
            // 已申请，回填联盟类型标签
          // 支持多个联盟类型（逗号分隔）
            const platformType = data.applyPlatformTypes || '';
            const labels = platformType.split(',')
              .map(t => {
                const opt = this.data.platformTypeOptions.find(o => o.value === t.trim());
                return opt ? opt.label : t;
              })
              .filter(l => l);
            data.platformTypeLabel = labels.join('、');

            this.setData({
              hasApplied: true,
              applyStatus: data.status,
              applyInfo: data
            });
            
            // 如果已通过，更新全局状态
            if (data.status === 'approved') {
              app.globalData.isAgent = true;
              app.globalData.invitationCode = data.invitationCode;
            }
          } else {
            // 未申请
            this.setData({
              hasApplied: false
            });
          }
        }
      })
      .catch(err => {
        console.error('检查申请状态失败:', err);
        // 如果返回404或其他错误，说明未申请
        this.setData({
          hasApplied: false
        });
      });
  },

  /**
   * 输入处理
   */
  onInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`formData.${field}`]: e.detail.value
    });
  },

  /**
   * 联盟类型多选切换
   */
  onPlatformTypeToggle(e) {
    const value = e.currentTarget.dataset.value;
    // 找到对应选项并切换 checked 状态
    const options = this.data.platformTypeOptions.map(o => {
      if (o.value === value) {
        return Object.assign({}, o, { checked: !o.checked });
      }
      return o;
    });
    // 重新计算选中列表
    const selectedTypes = options.filter(o => o.checked).map(o => o.value);
    this.setData({
      platformTypeOptions: options,
      selectedPlatformTypes: selectedTypes,
      'formData.applyPlatformTypes': selectedTypes.join(',')
    });
  },

  /**
   * 提交申请
   */
  onSubmit() {
    // 验证表单
    if (!this.validateForm()) {
      return;
    }
    
    this.setData({ loading: true });
    
    request.post('/miniapp/tbk/agent/apply', this.data.formData)
      .then(res => {
        if (res.code === 200) {
          this.showToast('success', '申请成功', '您的代理申请已提交，请耐心等待审核');
          
          // 延迟刷新状态
          setTimeout(() => {
            this.checkApplyStatus();
          }, 2000);
        } else {
          throw new Error(res.msg || '申请失败');
        }
      })
      .catch(err => {
        // 使用自定义提示框显示错误
        const errorMsg = err.message || '申请失败';
        let detailMsg = '请检查网络连接后重试';
        
        // 针对特定错误提供更友好的提示
        if (errorMsg.includes('上级邀请码')) {
          detailMsg = '请确认邀请码输入正确';
        } else if (errorMsg.includes('已存在')) {
          detailMsg = '您已提交过申请，请勿重复提交';
        }
        
        this.showToast('error', errorMsg, detailMsg);
      })
      .finally(() => {
        this.setData({ loading: false });
      });
  },

  /**
   * 验证表单
   */
  validateForm() {
    const { realName, phone, wechat, applyPlatformTypes } = this.data.formData;
    
    // 验证真实姓名
    if (!realName || !realName.trim()) {
      wx.showToast({
        title: '请输入真实姓名',
        icon: 'none'
      });
      return false;
    }

    if (realName.trim().length < 2) {
      wx.showToast({
        title: '姓名至少2个字符',
        icon: 'none'
      });
      return false;
    }
    
    // 验证手机号
    if (!phone || !phone.trim()) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return false;
    }
    
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(phone)) {
      wx.showToast({
        title: '手机号格式不正确',
        icon: 'none'
      });
      return false;
    }
    
    // 验证微信号
    if (!wechat || !wechat.trim()) {
      wx.showToast({
        title: '请输入微信号',
        icon: 'none'
      });
      return false;
    }

    if (wechat.trim().length < 3) {
      wx.showToast({
        title: '微信号至少3个字符',
        icon: 'none'
      });
      return false;
    }

    // 验证联盟类型（至少选择一个）
    if (!applyPlatformTypes || applyPlatformTypes.trim() === '') {
      wx.showToast({
        title: '请至少选择一个联盟类型',
        icon: 'none'
      });
      return false;
    }

    const validTypes = this.data.platformTypeOptions.map(o => o.value);
    const selectedTypes = applyPlatformTypes.split(',').map(t => t.trim());
    const invalidTypes = selectedTypes.filter(t => !validTypes.includes(t));
    if (invalidTypes.length > 0) {
      wx.showToast({
        title: '存在无效的联盟类型',
        icon: 'none'
      });
      return false;
    }
    
    return true;
  },

  /**
   * 重新申请
   */
  onReapply() {
    const prevApplyPlatformTypes = this.data.applyInfo?.applyPlatformTypes || '';
    // 解析之前选择的联盟类型（逗号分隔）
    const prevSelected = prevApplyPlatformTypes
      ? prevApplyPlatformTypes.split(',').map(t => t.trim()).filter(t => t)
      : [];
    // 恢复选项的 checked 状态
    const options = this.data.platformTypeOptions.map(o =>
      Object.assign({}, o, { checked: prevSelected.includes(o.value) })
    );
    this.setData({
      hasApplied: false,
      applyStatus: '',
      applyInfo: null,
      platformTypeOptions: options,
      selectedPlatformTypes: prevSelected,
      formData: {
        realName: this.data.applyInfo?.realName || '',
        phone: this.data.applyInfo?.phone || '',
        wechat: this.data.applyInfo?.wechat || '',
        applyPlatformTypes: prevApplyPlatformTypes,
        referrerInvitationCode: '',  // 重新申请时清空上级邀请码
        remark: ''
      }
    });
  },

  /**
   * 复制邀请码
   */
  onCopyInvitationCode() {
    if (!this.data.applyInfo || !this.data.applyInfo.invitationCode) {
      return;
    }
    
    wx.setClipboardData({
      data: this.data.applyInfo.invitationCode,
      success: () => {
        wx.showToast({
          title: '邀请码已复制',
          icon: 'success'
        });
      }
    });
  },

  /**
   * 显示自定义提示框
   */
  showToast(type, title, message = '') {
    this.setData({
      toastShow: true,
      toastType: type,
      toastTitle: title,
      toastMessage: message
    });
  },

  /**
   * 关闭自定义提示框
   */
  onToastClose() {
    this.setData({
      toastShow: false
    });
  },

  /**
   * 分享
   */
  onShareAppMessage() {
    const shareData = {
      title: '实佣有你 - 邀您一起赚钱',
      path: '/pages/index/index'
    };
    
    // 如果是代理，添加邀请码
    if (this.data.applyStatus === 'approved' && this.data.applyInfo && this.data.applyInfo.invitationCode) {
      shareData.path += `?invitationCode=${this.data.applyInfo.invitationCode}`;
    }
    
    return shareData;
  }
});
