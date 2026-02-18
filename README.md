# 🎮 三消游戏 (Match-3 Game) - Java 版

## 项目概述
一个完整的 Android 三消游戏，使用 Java 开发，支持广告变现。

## 游戏特性
- ✅ 经典三消玩法
- ✅ 6x6 游戏棋盘
- ✅ 7 种宝石颜色
- ✅ 动画效果
- ✅ 关卡系统（基础版）
- ✅ AdMob 广告集成
- ✅ Google Play 分成（70%）

## 技术栈
- **语言**: Java
- **最低 SDK**: 21 (Android 5.0)
- **目标 SDK**: 34 (Android 14)
- **广告 SDK**: Google AdMob

## 项目结构
```
Match3Game/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/match3game/
│   │   │   │   ├── MainActivity.java       # 主界面
│   │   │   │   ├── GameActivity.java       # 游戏界面
│   │   │   │   ├── BoardView.java          # 棋盘视图
│   │   │   │   ├── GameEngine.java         # 游戏引擎
│   │   │   │   └── AdManager.java          # 广告管理
│   │   │   ├── res/
│   │   │   │   ├── layout/                 # 布局文件
│   │   │   │   ├── values/                 # 字符串、颜色
│   │   │   │   ├── drawable/               # 图片资源
│   │   │   │   └── mipmap/                 # 图标
│   │   │   └── AndroidManifest.xml         # 清单文件
│   └── build.gradle                         # 应用级构建文件
├── build.gradle                             # 项目级构建文件
├── settings.gradle                          # 设置文件
└── README.md                                # 项目说明
```

## 如何部署

### 1. 准备工作
- [ ] Android Studio Hedgehog (2023.1.1) 或更高版本
- [ ] JDK 11 或更高版本
- [ ] Android SDK
- [ ] Google Play Console 账号（$25 一次性费用）

### 2. 导入项目
```bash
# 下载项目
cd ~/Projects/Match3Game

# 用 Android Studio 打开
# File -> Open -> 选择 Match3Game 目录
```

### 3. 配置广告
1. 打开 `app/src/main/java/com/example/match3game/AdManager.java`
2. 替换 `YOUR_BANNER_AD_UNIT_ID` 为你的 AdMob 广告位 ID
3. 替换 `YOUR_INTERSTITIAL_AD_UNIT_ID` 为你的插屏广告 ID

### 4. 运行测试
- 连接 Android 设备或启动模拟器
- 点击 Run 按钮（▶️）
- 游戏将自动安装到设备上

### 5. 上架 Google Play
1. 登录 [Google Play Console](https://play.google.com/console)
2. 创建新应用
3. 上传 APK/AAB 文件
4. 填写应用信息
5. 提交审核

## 收益预估

### 收入模式
- **横幅广告**: 每次显示收益 $0.01 - $0.05
- **插屏广告**: 每次显示收益 $0.10 - $0.30
- **激励视频**: 每次观看收益 $0.05 - $0.20

### 预估收益（假设）
- 日活用户 (DAU): 1,000
- 每用户每天观看广告: 3 次
- 每次广告收益: $0.10
- **日收益**: 1,000 × 3 × $0.10 = **$30**
- **月收益**: $30 × 30 = **$900**
- **Google Play 分成**: 70% → **$630/月**

## 注意事项
1. ⚠️ 必须配置真实的 AdMob 广告位 ID 才能赚钱
2. ⚠️ Google Play 审核需要 1-7 天
3. ⚠️ 需要遵守 Google Play 政策
4. ⚠️ 建议先在模拟器测试，再上架

## 游戏玩法
1. 点击一个宝石选中它
2. 点击相邻的宝石交换位置
3. 如果交换后形成 3 个或更多相同颜色的宝石，它们会被消除
4. 上方的宝石会下落填补空缺
5. 顶部会生成新的宝石
6. 尽可能获得高分！

## 代码亮点
- ✅ 完整的三消算法
- ✅ 智能匹配检测
- ✅ 动画效果
- ✅ 响应式布局
- ✅ 广告集成
- ✅ 代码结构清晰

## 联系支持
有问题随时问我！🤨
