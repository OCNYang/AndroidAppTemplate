# Android App Template

```
.
├── app
├── base
├── network
│   ├── api
│   └── network
└── webview
    └── webview-x5
```

* 网络请求整体框架
* WebView

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
