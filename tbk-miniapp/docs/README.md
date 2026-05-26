# 微信小程序多平台电商架构

## 项目简介

本项目是一个支持多电商联盟平台的微信小程序，目前已集成淘宝客(TBK)和京东联盟(JD)，并预留了多多进宝(PDD)和大淘客(DTK)等平台的扩展接口。

## 核心特性

- ✅ **多平台支持** - 统一接口支持多个电商联盟平台
- ✅ **数据适配** - 自动将不同平台API响应转换为统一格式
- ✅ **易于扩展** - 通过适配器模式轻松添加新平台
- ✅ **向后兼容** - 保留旧代码的同时支持新架构
- ✅ **类型安全** - 统一的数据格式和接口定义

## 架构概览

```
┌─────────────────────────────────────────────────────────┐
│                      小程序页面层                          │
│              (pages/goods/list.js, detail.js...)        │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 统一API调用层                             │
│              (api/platform-api.js)                      │
│  - getGoodsList()  - getGoodsDetail()                   │
│  - getHotGoods()   - generateCode()                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                  适配器工厂                               │
│              (utils/tbk-adapter.js)                     │
│         根据平台类型返回对应适配器                          │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┼────────────┬────────────┐
        ▼            ▼            ▼            ▼
   ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐
   │淘宝客    │  │京东联盟  │  │多多进宝  │  │大淘客    │
   │适配器    │  │适配器    │  │适配器    │  │适配器    │
   └─────────┘  └─────────┘  └─────────┘  └─────────┘
        │            │            │            │
        └────────────┴────────────┴────────────┘
                     │
                     ▼
          将不同平台数据转换为统一格式
```

## 快速开始

### 1. 安装依赖

```bash
# 无需额外依赖，使用小程序原生能力
```

### 2. 配置平台

编辑 `config/platform.js`，启用需要的平台：

```javascript
const PLATFORM_CONFIG = {
  [PLATFORM_TYPES.TBK]: {
    name: '淘宝客',
    enabled: true,  // 设置为 true 启用
    // ...
  },
  [PLATFORM_TYPES.JD]: {
    name: '京东联盟',
    enabled: true,  // 设置为 true 启用
    // ...
  }
};
```

### 3. 在页面中使用

```javascript
import { getGoodsList, PLATFORM_TYPES } from '/api/platform-api';

// 获取淘宝客商品
getGoodsList({
  platform: PLATFORM_TYPES.TBK,
  pageNo: 1,
  pageSize: 20
}).then(res => {
  console.log('商品列表:', res.data);
});
```

## 文档索引

- 📖 [详细使用指南](./multi-platform-guide.md) - 完整的API文档和使用示例
- 🎨 [平台图标准备说明](./platform-icons.md) - 平台图标资源准备指南
- 🏗️ [架构设计说明](./architecture.md) - 详细的架构设计文档

## 目录结构

```
tbk-miniapp/
├── config/
│   └── platform.js              # 平台配置（修改这里启用/禁用平台）
├── adapters/
│   ├── base-adapter.js          # 适配器基类
│   ├── tbk-adapter.js           # 淘宝客适配器 ✅
│   ├── jd-adapter.js            # 京东联盟适配器 ✅
│   ├── pdd-adapter.js           # 多多进宝适配器 🔜
│   └── dtk-adapter.js           # 大淘客适配器 🔜
├── api/
│   └── platform-api.js          # 统一API封装
├── utils/
│   └── tbk-adapter.js           # 适配器工厂
├── pages/
│   ├── goods/                   # 商品相关页面
│   ├── index/                   # 首页
│   └── user/                    # 用户中心
├── images/
│   └── platform/                # 平台图标目录
│       ├── tbk.png
│       ├── jd.png
│       ├── pdd.png
│       └── dtk.png
└── docs/                        # 文档目录
    ├── README.md                # 本文档
    ├── multi-platform-guide.md  # 详细使用指南
    ├── platform-icons.md        # 图标准备说明
    └── architecture.md          # 架构设计文档
```

## 支持的平台

| 平台 | 状态 | 适配器 | 说明 |
|------|------|--------|------|
| 淘宝客 | ✅ 已完成 | `tbk-adapter.js` | 支持商品搜索、详情、优惠券等 |
| 京东联盟 | ✅ 已完成 | `jd-adapter.js` | 支持商品搜索、详情、京东自营标识 |
| 多多进宝 | 🔜 待开发 | `pdd-adapter.js` | 预留接口，参考文档可快速接入 |
| 大淘客 | 🔜 待开发 | `dtk-adapter.js` | 预留接口，参考文档可快速接入 |

## 统一数据格式

所有平台的商品数据都会被转换为统一格式：

```javascript
{
  id: String,              // 商品ID
  platform: String,        // 平台类型：'tbk'/'jd'/'pdd'/'dtk'
  title: String,           // 商品标题
  mainPic: String,         // 主图URL
  originalPrice: Number,   // 原价
  actualPrice: Number,     // 券后价
  couponAmount: Number,    // 优惠券金额
  commissionRate: Number,  // 佣金比例
  commission: Number,      // 预估佣金
  monthSales: Number,      // 月销量
  shopName: String,        // 店铺名称
  // ... 更多字段
}
```

## 核心API

### getGoodsList(params)
获取商品列表，支持分页和搜索。

### getGoodsDetail(params)
获取商品详情，包含完整的商品信息。

### getHotGoods(params)
获取热门推荐商品。

### generateCode(params)
生成推广口令（淘口令/京口令等）。

### getCategoryList(params)
获取商品分类列表。

详细API文档请查看 [完整使用指南](./multi-platform-guide.md)。

## 开发流程

### 添加新平台的步骤

1. **启用平台配置** - 在 `config/platform.js` 中设置 `enabled: true`
2. **创建适配器** - 在 `adapters/` 目录创建新的适配器文件
3. **实现适配方法** - 继承 `BaseAdapter` 并实现必要方法
4. **注册到工厂** - 在 `utils/tbk-adapter.js` 中注册新适配器
5. **测试验证** - 调用API确认数据格式正确

详细步骤请参考 [如何添加新平台](./multi-platform-guide.md#如何添加新平台)。

## 设计模式

本项目采用以下设计模式：

1. **适配器模式** - 统一不同平台的数据格式
2. **工厂模式** - 根据平台类型创建对应适配器
3. **策略模式** - 不同平台的转换策略独立实现

## 性能优化

- ✅ 图片懒加载
- ✅ 数据缓存机制
- ✅ 请求节流防抖
- ✅ 分页加载

## 常见问题

**Q: 如何切换不同平台？**
A: 在调用API时传入不同的 `platform` 参数即可。

**Q: 如何测试新平台？**
A: 可以创建测试页面，使用模拟数据测试适配器。

**Q: 数据格式不一致怎么办？**
A: 适配器会自动将不同平台的数据转换为统一格式。

更多问题请查看 [常见问题解答](./multi-platform-guide.md#常见问题)。

## 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/new-platform`)
3. 提交更改 (`git commit -am 'Add new platform'`)
4. 推送到分支 (`git push origin feature/new-platform`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 邮箱: support@example.com
- 💬 微信: 扫描二维码加入技术交流群
- 🐛 问题反馈: 在 Issues 中提交

---

最后更新时间: 2026-03-31
