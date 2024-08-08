# Android App Template

```
.
├── app // 主项目：业务代码放在这里，需要分模块的话自己去分
├── base // 基础模块：基类、拓展类、常用第三方库
├── glance // 桌面小部件模块
├── room // 数据库模块：TODO: 需要时再创建
├── workmanger // TODO: 需要时再创建
├── network
│   ├── api // 数据接口声明模块
│   ├── network // 网络数据接口实现模块：okhttp3 & retrofit2 & moshi & flow
│   └── network-monitor // 网络请求监控模块：被 network 依赖
└── webview
    └── webview-x5 // 浏览器模块
```

```
.
├── aop
│   └── src/main/java
│       └── com.app.aop
│           ├── GITMPlugin.kt
│           └── click
│               ├── ComposeClickClassVisitorFactory.kt
│               └── ComposeClickConfig.kt
├── app
│   └── src/main/java
│       └── com.app.template
│           ├── MainActivity.kt
│           ├── MyApplication.kt
│           ├── comm
│           │   ├── $Const.kt
│           │   └── $KeyVal.kt
│           ├── page
│           │   ├── _dialog
│           │   ├── _page
│           │   ├── _test
│           │   │   ├── $LogcatTestScreen.kt
│           │   │   └── $TestScreen.kt
│           │   ├── _widget
│           │   │   ├── $Image.kt
│           │   │   └── $WebView.kt
│           │   ├── detail
│           │   │   └── DetailScreen.kt
│           │   └── home
│           │       ├── HomeScreen.kt
│           │       └── HomeView.kt
│           ├── ui
│           │   └── theme
│           │       ├── Color.kt
│           │       ├── Shapes.kt
│           │       ├── Theme.kt
│           │       └── Type.kt
│           └── viewmodel
│               ├── -LaunchedOnceViewModel.kt
│               ├── -LifecycleViewModel.kt
│               └── HomeViewModel.kt
├── base
│   └── src/main/java
│       └── com.app.base
│           ├── $ApplicationScopeViewModelProvider.kt
│           ├── $Coil.kt
│           ├── $Func.kt
│           ├── $Log.kt
│           ├── $MMKV.kt
│           ├── $NamedCodeBlock.kt
│           ├── $NotificationChannelCreator.kt
│           ├── $Toast.kt
│           ├── -AppLifecycleListener.kt
│           ├── -FontScale.kt
│           ├── -LogDumper.kt
│           ├── -ReactiveNetwork.kt
│           ├── -StartupInitializer.kt
│           ├── BaseActivity.kt
│           ├── BaseApplication.kt
│           └── logcat
│               └── $LogcatService.kt
├── glance
│   └── src/main/java
│       └── com.app.glance
│           ├── GlanceReceiver.kt
│           ├── MyGlanceAppWidget.kt
│           ├── shortcut
│           │   └── Shortcut.kt
│           └── tile_service
│               └── MyTileService.kt
├── network
│   ├── api
│   │   └── src/main/java
│   │       └── com.app.api
│   │           ├── CallListener.kt
│   │           ├── TemplateApi.kt
│   │           └── bean
│   │               └── $BaseBean.kt
│   └── network
│       └── src
│           └── main
│               ├── java
│               │   ├── com.app.network
│               │   │   ├── $Flow.kt
│               │   │   ├── $HTTPManager.kt
│               │   │   ├── $Report.kt
│               │   │   ├── $RequestBody.kt
│               │   │   ├── $Utils.kt
│               │   │   ├── TemplateApiImpl.kt
│               │   │   ├── adapter
│               │   │   │   ├── BigDecimalAdapter.kt
│               │   │   │   ├── DefaultIfNullFactory.kt
│               │   │   │   ├── DefaultOnDataMismatchAdapter.kt
│               │   │   │   └── FilterNullValuesFromListAdapter.kt
│               │   │   ├── api
│               │   │   │   ├── PATH.kt
│               │   │   │   └── TemplateService.kt
│               │   │   ├── error
│               │   │   │   ├── ApiException.kt
│               │   │   │   ├── ERROR.kt
│               │   │   │   └── ExceptionHandler.kt
│               │   │   ├── interceptor
│               │   │   │   ├── $HttpLoggingInterceptor.kt
│               │   │   │   ├── $NoNetWorkInterceptor.kt
│               │   │   │   ├── CookiesInterceptor.kt
│               │   │   │   ├── HeaderInterceptor.kt
│               │   │   │   ├── HostSelectionInterceptor.kt
│               │   │   │   ├── PublicParameterInterceptor.kt
│               │   │   │   └── RetryInterceptor.kt
│               │   │   └── monitor
│               │   │       ├── $TimelineEventListener.kt
│               │   │       ├── HttpData.kt
│               │   │       └── NetworkEventListener.kt
│               │   └── retrofit2
│               │       └── MonitorCallAdapterFactory.java
│               └── resources
│                   └── META-INF
│                       └── services
│                           └── com.app.api.TemplateApi
└── webview
    └── webview-x5
        └── src/main/java
            └── com.app.webview_x5
                └── $X5.kt
```


* 网络请求整体框架
* WebView


- [x] 桌面小部件 // 比如小米，需要在小米开放平台注册成功后，小部件才能显示，不然入口将会很深。
- [ ] 应用图标小角标
- [x] 应用图标长按菜单
- [x] 系统快捷设置
- [ ] 分享图片、文本接收
- [ ] 网站打开接收


## 多语言
如果需要在应用内更改语言，参考：[https://stackoverflow.com/a/40704077/7072452](https://stackoverflow.com/a/40704077/7072452)

## Navigation 导航库

Compose Navigation 封装库：[https://github.com/raamcosta/compose-destinations](https://github.com/raamcosta/compose-destinations)


## AndroidUtilCode doc

[文档](./docs/android_util_code/README.md)

## 权限申请：XXPermissions

[文档](https://github.com/getActivity/XXPermissions)

**compose 官方权限库**：[com.google.accompanist:accompanist-permissions](https://google.github.io/accompanist/permissions/)

## google.accompanist 库不错的库

此库目前弃用和迁移说明：[https://github.com/google/accompanist](https://github.com/google/accompanist)

* 占位符：UI 渲染完成前，显示占位UI
* 自定义布局：屏幕上定位两个插槽、窗格

## 网络监控

[文档](https://github.com/pwittchen/ReactiveNetwork)

潜力用法没发掘

## 状态栏

下面两者都可以控制：
1. BaseActivity -> ImmersionBar.xxx.init()
2. Theme -> LocalView.current.window

具体可以查看代码；需要注意的是，根据需要来选择：
如果是全屏：使用1；禁用2；
如果不是全屏：可以两者一起使用，但要主要不要有冲突；也可以使用一种。

第三种的方式：Compose 控制：https://stackoverflow.com/questions/69688138/how-to-hide-navigationbar-and-statusbar-in-jetpack-compose

另外一种透明方式：enableEdgeToEdge()

## 启动页 SplashScreen

修改 themes/Theme.SplashScreen.MySplash


## 网络请求监控

～～对所有的网络请求进行监控，当请求失败时，进行上报；
我们的愿景是能将请求的信息，请求参数，错误信息，都进行上报。
发现寻找一个切入点是比较困难的。

现在的思路有三种方式：
1. 在网络请求接口类中，返回值依然采用原来的老方式，返回 Call<BaseResult<T>>，这样在处理响应数据时，对 Call 进行统一的处理，同时 Call 中有请求相关的信息；
2. 通过 addCallAdapterFactory(XXCallAdapter.Factory) 进行自定义返回类型，在自定义 CallAdapter 中对返回类型进行统一封装，同时统一处理网络错误，同时进行错误上报；
3. 通过对 Retrofit 中默认的 CallAdapter 进行 hook，进行统一的错误上报。

最推荐的时第 2 种方式，但对目前改动较大；现在可以考虑采用第 3 种方式。

第三种方式分析：
默认的 CallAdapter 可以查看 [retrofit2.Platform] 类。

```java
  List<? extends CallAdapter.Factory> defaultCallAdapterFactories(
      @Nullable Executor callbackExecutor) {
    DefaultCallAdapterFactory executorFactory = new DefaultCallAdapterFactory(callbackExecutor);
    return hasJava8Types
        ? asList(CompletableFutureCallAdapterFactory.INSTANCE, executorFactory)
        : singletonList(executorFactory);
  }
```
通过上面的方法知道，默认采用的是 CompletableFutureCallAdapterFactory.INSTANCE ，且是单例类。～～

* 最终方案
 ``.addCallAdapterFactory(MonitorCallAdapterFactory(null, Report))``


## 云真机列表：

● OPPO 远程真机平台：[https://open.oppomobile.com/octpcloud/octpcloud/index.html#/?source=open_manage_zhenji](https://open.oppomobile.com/octpcloud/octpcloud/index.html#/?source=open_manage_zhenji)  
● vivo 云真机：[https://dev.vivo.com.cn/vcl/#/remote/device/list](https://dev.vivo.com.cn/vcl/#/remote/device/list)  
● 华为 云调试：[https://developer.huawei.com/consumer/cn/service/josp/agc/handleAllianceLogin.html?_=20240422113136](https://developer.huawei.com/consumer/cn/service/josp/agc/handleAllianceLogin.html?_=20240422113136)  
● 荣耀 云调试（远程真机）：[https://developer.honor.com/cn/manageCenter/app/E00012?~id=41](https://developer.honor.com/cn/manageCenter/app/E00012?~id=41)  
● 小米 远程真机租用：[https://testit.miui.com/remote?cUserId=CIyBxfZ2GKa8cupuUvn_V7G-4BE](https://testit.miui.com/remote?cUserId=CIyBxfZ2GKa8cupuUvn_V7G-4BE)  

## 关于 Notification 的坑
消息通知现在需要设置 channel ,且设置后再次变更无效（即和第一次设置的配置保持一致），需卸载重装后才能生效。
这一点要注意。所以，和通道相关的，比如名字、声音、震动等等配置，不会因重新运行代码而重置，需卸载重装或采用新的通道 ID。

## 页面里只执行一次的副作用

封装方法：LaunchedOnce

## 应用在空闲时执行任务

Looper.myQueue().addIdleHandler(MyIdleHandler())

日志上报和收集放在这里；
应用先版本更新检测放在这里；

## 可组合项生命周期的说明

* 可组合项只要不再显示时生命周期即结束，比如退出当前可组合项页面、打开下一个页面；
* 可组合项中的副作用代码，独立于可组合项重组之外执行，但是当可组合项重新创建时（比如退回到当前页），会重新执行；但是多次调用可组合项不会重新执行副作用。
* 可组合项，打开下一页面时只会走 stop; 退出页面时，会走 destroy；但是每次都会重新走一遍 create start resume
* 可组合项绑定的 viewModel ：打开下一个页面时，viewModel 不会销毁；当退出当前可组合项页面时，viewModel 随后销毁；

上面这些逻辑，如果充分理解 @Composable 的话，理解起来就会很容易。

工具方法，可组合项生命周期的监听： LifecycleEffect

## 规范化：第三方平台 key

这里建议所有第三方平台账号的 key 通过接口下发，然后动态初始化注册，这里主要是为了防止中间换账号。

## 规范化：图标都通过 AS 导入

所有图标，尤其应用图标、通知图标，都要求通过 AS `右键 -> new -> Image Asset/Vector Asset` 的方式导入；
这里主要是为了避免图标的不规范造成的问题。

## 规范化：最小可用版本控制

通过接口下发配置：最小可用版本；如果当前版本小于最小可用版本；强制提醒用户升级，不升级不可再用。
如果前期不采用这种方式，只能通过删除掉原接口采用新接口才能达到效果，且这种效果不太友好，用户不知道是因为版本低不能使用，会认为应用下架了。


## 消息推送策略

目前基本上所有的国内安卓系统都严控了后台自启动和保活；
并且消息推送都进行了自家系统化，且各家良莠不齐，且支持的能力也不相同；
同时，消息推送后的送达情况并不详细和准确（送达情况，一般是指消息推送平台上的回执，对接服务端提供的接口，这样服务端就知道送达情况了）；

为了提高送达率，和确保用户能一定收到消息，规划以下策略：
1. 消息推送发送消息；// 如果 Notification 的消息可以做成不可忽略的，而在消息内容上采用大消息，提供“忽略/知道了”按钮才能消除，如果这个按钮的事件可以上报给服务端也是可以的；
2. 服务端对接消息推送的回执，这里只做为送达的参考；
3. 消息推送的消息，在手机端被用户点击打开后，上报给服务端，这里做为确切送达标识；
4. 如果用户没有点击消息推送，而是直接的打开应用，这时，可以在应用内上方悬浮展示消息，用户不点击不消失；用户点击的话，做为确切送达标识，用户滑动清除，作为确切忽略标识；
5. 如果消息推送后，用户长时间没有点消息，也没有打开应用处理消息；也就是说多方标识都不能确认用户以收到消息，则采用电话通知的形式；


## Activity 的销毁和恢复

1. viewModel 本身是可以恢复“屏幕旋转”造成的销毁；
2. viewModel 结合 SavedStateHandle 可以实现内存不足造成的销毁后的恢复；// 实现原理其实是对 onSaveInstanceState(Bundle) 和 onRestoreInstanceState(Bundle) 的封装；

## Compose 库用法例子

[material3](https://github.com/JetBrains/compose-multiplatform-core/tree/3065673e475673db51517136ad1523e51bd455da/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples)
