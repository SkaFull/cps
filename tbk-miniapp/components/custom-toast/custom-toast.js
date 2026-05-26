// components/custom-toast/custom-toast.js
Component({
  properties: {
    // 是否显示
    show: {
      type: Boolean,
      value: false
    },
    // 提示类型：success, error, warning, info
    type: {
      type: String,
      value: 'info'
    },
    // 标题
    title: {
      type: String,
      value: ''
    },
    // 详细消息
    message: {
      type: String,
      value: ''
    },
    // 是否显示关闭按钮
    showClose: {
      type: Boolean,
      value: false
    },
    // 自动关闭时间（毫秒），0表示不自动关闭
    duration: {
      type: Number,
      value: 3000
    }
  },

  data: {
    timer: null
  },

  lifetimes: {
    detached() {
      // 组件销毁时清除定时器
      if (this.data.timer) {
        clearTimeout(this.data.timer);
      }
    }
  },

  observers: {
    'show': function(show) {
      if (show && this.properties.duration > 0) {
        // 清除之前的定时器
        if (this.data.timer) {
          clearTimeout(this.data.timer);
        }
        
        // 设置新的定时器
        const timer = setTimeout(() => {
          this.hideToast();
        }, this.properties.duration);
        
        this.setData({ timer });
      }
    }
  },

  methods: {
    // 阻止遮罩层滚动穿透
    preventTouchMove() {
      return false;
    },

    // 关闭提示框
    onClose() {
      this.hideToast();
    },

    // 隐藏提示框
    hideToast() {
      if (this.data.timer) {
        clearTimeout(this.data.timer);
        this.setData({ timer: null });
      }
      
      this.triggerEvent('close');
    }
  }
});
