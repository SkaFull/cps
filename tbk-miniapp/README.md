# 淘宝客微信小程序

## 快速开始

### 1. 配置后端API地址

在开始使用前，你需要配置后端API地址。有两种方式：

#### 方式一：在 app.js 中配置（推荐）

打开 `app.js` 文件，找到 `globalData` 配置，修改 `apiUrl`：

```javascript
this.globalData = {
  userInfo: null,
  token: null,
  isAgent: false,
  invitationCode: '',
  referralCode: '',
  // 修改为你的实际API地址
  apiUrl: 'http://localhost:8080' // 开发环境
  // apiUrl: 'https://api.yourdomain.com' // 生产环境
};
```

#### 方式二：在 utils/request.js 中配置

打开 `utils/request.js` 文件，修改 `DEFAULT_API_URL` 常量：

```javascript
const DEFAULT_API_URL = 'http://localhost:8080'; // 修改为你的实际API地址
```

### 2. 配置小程序AppID

打开 `project.config.json` 文件，修改 `appid` 为你的小程序AppID：

```json
{
  "appid": "你的小程序AppID",
  ...
}
```

### 3. 配置合法域名

在微信公众平台的小程序管理后台，配置服务器域名：

1. 登录 [微信公众平台](https://mp.weixin.qq.com/)
2. 进入「开发」-「开发管理」-「开发设置」
3. 在「服务器域名」中添加你的后端API域名

**注意**：开发阶段可以在微信开发者工具中勾选「不校验合法域名」选项。

### 4. 启动小程序

1. 使用微信开发者工具打开项目
2. 点击「编译」按钮
3. 点击登录按钮进行测试

## 常见问题

### 1. 点击登录没有反应？

**可能原因：**
- API地址未配置或配置错误
- 后端服务未启动
- 网络连接问题

**解决方法：**
1. 检查控制台日志，查看详细错误信息
2. 确认 `app.js` 或 `utils/request.js` 中的API地址配置正确
3. 确认后端服务已启动并可访问
4. 在微信开发者工具中打开「调试器」查看网络请求

### 2. 提示"API地址未配置"？

请按照上面的「配置后端API地址」步骤进行配置。

### 3. 提示"网络连接失败"？

**可能原因：**
- 后端服务未启动
- API地址配置错误
- 网络不通

**解决方法：**
1. 确认后端服务已启动
2. 检查API地址是否正确（注意http/https协议）
3. 如果是局域网IP，确保手机和电脑在同一网络
4. 在微信开发者工具的「详情」-「本地设置」中勾选「不校验合法域名」

### 4. getUserProfile API已废弃？

本项目已更新登录逻辑，不再使用已废弃的 `getUserProfile` API。现在使用 `wx.login` 获取 code，由后端通过 code 换取 openid 完成登录。

## 项目结构

```
tbk-miniapp/
├── pages/              # 页面文件
│   ├── auth/          # 认证相关页面
│   │   └── login/     # 登录页面
│   ├── index/         # 首页
│   ├── goods/         # 商品相关页面
│   └── user/          # 用户中心
├── utils/             # 工具类
│   ├── auth.js        # 认证工具
│   └── request.js     # 网络请求工具
├── app.js             # 小程序入口
├── app.json           # 小程序配置
└── app.wxss           # 全局样式
```

## 开发说明

### 登录流程

1. 用户点击登录按钮
2. 调用 `wx.login()` 获取临时登录凭证 code
3. 将 code 发送到后端接口 `/miniapp/auth/login`
4. 后端通过 code 换取 openid 并创建/更新用户信息
5. 后端返回 token 和用户信息
6. 小程序保存 token 和用户信息到本地存储
7. 登录完成

### 调试技巧

1. 打开微信开发者工具的「调试器」-「Console」查看日志
2. 打开「Network」标签查看网络请求详情
3. 使用 `console.log()` 输出调试信息
4. 检查「Storage」标签查看本地存储数据

## 技术支持

如有问题，请查看：
1. 微信开发者文档：https://developers.weixin.qq.com/miniprogram/dev/framework/
2. 项目issue：提交到项目的issue页面
