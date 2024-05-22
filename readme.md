# Android App Template

```
.
├── app
├── base
├── glance
├── network
│   ├── api
│   └── network
└── webview
    └── webview-x5
```

* 网络请求整体框架
* WebView


- [x] 桌面小部件
- [ ] 应用图标小角标
- [x] 应用图标长按菜单
- [x] 系统快捷设置
- [ ] 分享图片、文本接收
- [ ] 网站打开接收


## 多语言
如果需要在应用内更改语言，参考：[https://stackoverflow.com/a/40704077/7072452](https://stackoverflow.com/a/40704077/7072452)

## AndroidUtilCode doc

[文档](./docs/android_util_code/README.md)

## 权限申请：XXPermissions

[文档](https://github.com/getActivity/XXPermissions)

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