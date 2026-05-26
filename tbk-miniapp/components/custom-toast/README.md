# 自定义提示框组件使用说明

## 组件简介

`custom-toast` 是一个美观、专业的自定义提示框组件，用于替代微信小程序原生的 `wx.showToast`，提供更好的用户体验。

## 特性

- ✨ 渐变色背景，视觉效果更佳
- 🎨 支持4种提示类型（成功、错误、警告、信息）
- 💬 支持标题和详细消息
- ⏱️ 自动关闭或手动关闭
- 🎭 平滑的动画效果
- 📱 响应式设计，适配不同屏幕

## 使用方法

### 1. 在页面配置中引入组件

在页面的 `.json` 文件中添加：

```json
{
  "usingComponents": {
    "custom-toast": "/components/custom-toast/custom-toast"
  }
}
```

### 2. 在页面数据中添加相关字段

在页面的 `.js` 文件的 `data` 中添加：

```javascript
data: {
  toastShow: false,
  toastType: 'info',
  toastTitle: '',
  toastMessage: ''
}
```

### 3. 在页面中添加组件

在页面的 `.wxml` 文件中添加：

```xml
<custom-toast 
  show="{{toastShow}}"
  type="{{toastType}}"
  title="{{toastTitle}}"
  message="{{toastMessage}}"
  duration="{{3500}}"
  bind:close="onToastClose"
/>
```

### 4. 添加显示和关闭方法

在页面的 `.js` 文件中添加方法：

```javascript
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
}
```

### 5. 调用示例

```javascript
// 成功提示
this.showToast('success', '操作成功', '您的信息已保存');

// 错误提示
this.showToast('error', '操作失败', '请检查网络连接后重试');

// 警告提示
this.showToast('warning', '注意', '此操作需要谨慎处理');

// 信息提示
this.showToast('info', '提示', '这是一条普通信息');

// 仅标题（不显示详细消息）
this.showToast('success', '操作成功');
```

## 组件属性

| 属性名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| show | Boolean | 是 | false | 是否显示提示框 |
| type | String | 否 | 'info' | 提示类型：success/error/warning/info |
| title | String | 是 | '' | 提示标题 |
| message | String | 否 | '' | 详细消息（可选） |
| showClose | Boolean | 否 | false | 是否显示关闭按钮 |
| duration | Number | 否 | 3000 | 自动关闭时间（毫秒），0表示不自动关闭 |

## 组件事件

| 事件名 | 说明 | 返回值 |
|--------|------|--------|
| close | 提示框关闭时触发 | 无 |

## 提示类型说明

### success（成功）
- 图标：✓
- 适用场景：操作成功、保存成功、提交成功等
- 示例：`this.showToast('success', '申请成功', '您的代理申请已提交')`

### error（错误）
- 图标：✕
- 适用场景：操作失败、验证失败、网络错误等
- 示例：`this.showToast('error', '上级邀请码不存在', '请确认邀请码输入正确')`

### warning（警告）
- 图标：!
- 适用场景：需要注意的操作、潜在风险提示等
- 示例：`this.showToast('warning', '操作不可逆', '删除后无法恢复')`

### info（信息）
- 图标：i
- 适用场景：一般信息提示、状态说明等
- 示例：`this.showToast('info', '加载中', '正在获取数据')`

## 最佳实践

### 1. 错误处理时的友好提示

```javascript
request.post('/api/apply', data)
  .then(res => {
    if (res.code === 200) {
      this.showToast('success', '申请成功', '您的申请已提交，请耐心等待审核');
    }
  })
  .catch(err => {
    const errorMsg = err.message || '操作失败';
    let detailMsg = '请检查网络连接后重试';
    
    // 针对特定错误提供更友好的提示
    if (errorMsg.includes('上级邀请码')) {
      detailMsg = '请确认邀请码输入正确';
    } else if (errorMsg.includes('已存在')) {
      detailMsg = '您已提交过申请，请勿重复提交';
    }
    
    this.showToast('error', errorMsg, detailMsg);
  });
```

### 2. 表单验证提示

```javascript
if (!formData.phone) {
  this.showToast('warning', '请输入手机号', '手机号是必填项');
  return false;
}

const phoneReg = /^1[3-9]\d{9}$/;
if (!phoneReg.test(formData.phone)) {
  this.showToast('error', '手机号格式不正确', '请输入11位有效手机号');
  return false;
}
```

### 3. 操作成功后的引导

```javascript
this.showToast('success', '支付成功', '订单已提交，请等待发货');
setTimeout(() => {
  wx.navigateTo({ url: '/pages/order/detail' });
}, 2000);
```

## 与原生 wx.showToast 对比

| 特性 | custom-toast | wx.showToast |
|------|--------------|--------------|
| 视觉效果 | 渐变色背景，更美观 | 纯色背景 |
| 消息长度 | 支持长文本（标题+详细消息） | 最多7个汉字 |
| 自定义性 | 高度可定制 | 定制性有限 |
| 图标 | 4种类型图标 | 仅success/loading/none |
| 动画效果 | 弹性动画 | 渐变动画 |

## 注意事项

1. 确保在页面的 `.json` 文件中正确引入组件
2. 组件使用了固定定位（z-index: 9999），确保不会被其他元素遮挡
3. 建议设置合理的 `duration` 值，避免提示框停留时间过长或过短
4. 详细消息（message）是可选的，简单提示可以只传标题
5. 如果需要用户手动关闭，可以设置 `showClose="{{true}}"` 和 `duration="{{0}}"`

## 样式定制

如需修改样式，可以编辑 `custom-toast.wxss` 文件：

- `.custom-toast-container`：主容器样式
- `.toast-icon-wrap`：图标样式
- `.toast-title`：标题样式
- `.toast-message`：消息样式

建议在修改样式时保持良好的视觉一致性。
