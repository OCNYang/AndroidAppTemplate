package com.app.template

import com.app.base.BaseApplication

/**
 * 对于依赖的一些第三方库的初始化，推荐使用 startup 在每个使用到的模块单独实现，不要放到 Application 中
 */
class MyApplication : BaseApplication()