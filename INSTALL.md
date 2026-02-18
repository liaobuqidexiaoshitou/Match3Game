# 安装指南

## 方法 1: 使用 Android Studio（推荐）

1. **下载 Android Studio**
   - 访问 https://developer.android.com/studio
   - 下载并安装最新版本

2. **导入项目**
   ```bash
   # 打开 Android Studio
   # File -> Open -> 选择 ~/Projects/Match3Game
   ```

3. **等待 Gradle 同步**
   - 首次打开会自动同步 Gradle
   - 可能需要几分钟，请耐心等待

4. **配置广告 ID**
   - 打开 `app/src/main/java/com/example/match3game/AdManager.java`
   - 找到 `YOUR_BANNER_AD_UNIT_ID` 和 `YOUR_INTERSTITIAL_AD_UNIT_ID`
   - 替换为你的 AdMob 广告位 ID

5. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 Run 按钮（▶️）
   - 等待构建完成

## 方法 2: 命令行编译

1. **安装依赖**
   ```bash
   cd ~/Projects/Match3Game
   ./gradlew build
   ```

2. **生成 APK**
   ```bash
   ./gradlew assembleDebug
   ```

3. **安装到设备**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 配置 AdMob

### 1. 注册 Google AdMob 账号
1. 访问 https://apps.admob.com/
2. 注册账号并登录
3. 添加应用
4. 创建广告位

### 2. 获取广告位 ID
- **横幅广告**: 创建 Banner 广告位
- **插屏广告**: 创建插屏广告位

### 3. 替换代码中的 ID
打开 `AdManager.java`，替换：
```java
private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
```

## 测试广告

### 使用测试设备
在 `AdManager.java` 中配置测试设备：
```java
String testDeviceIds = "YOUR_TEST_DEVICE_ID";
```

### 使用测试广告位
使用 Google 提供的测试广告位：
- Banner: `ca-app-pub-3940256099942544/6300978111`
- 插屏: `ca-app-pub-3940256099942544/1033173712`

## 故障排除

### Gradle 同步失败
1. 检查网络连接
2. 清理缓存: File -> Invalidate Caches / Restart
3. 更新 Gradle 版本

### 构建失败
1. 检查 JDK 版本（需要 JDK 11+）
2. 更新 Android SDK
3. 清理项目: Build -> Clean Project

### 广告不显示
1. 检查网络连接
2. 确认广告位 ID 配置正确
3. 使用测试设备或测试广告位
4. 查看日志: Logcat 过滤 "AdManager"

## 下一步

1. ✅ 本地测试完成
2. ⏳ 配置 AdMob 广告
3. ⏳ 优化游戏体验
4. ⏳ 上架 Google Play
5. ⏳ 赚取广告收入！

有问题随时问我！🤨
