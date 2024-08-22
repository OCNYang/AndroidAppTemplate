package com.app.template

import com.app.module_dev_tools.SPIDevTools
import com.app.module_main.SPIMain
import com.app.spi.default0.DefaultSPIs
import com.google.auto.service.AutoService

/**
 * 此类是各业务模块 交互的枢纽
 * 因各业务模块之间没有依赖关系，所以无法相互调用，可以通过此类作为传递的纽带
 *
 * 各业务模块的声明接口实现类
 * 1. 理论上要在这里实现逻辑：因为每个 app 需要的模块不同
 * 2. 为了方便，可以直接继承 SPI 的默认实现类大集合库：此库实现了所有模块的声明接口，但是对这些模块的依赖只在编译期，所以放心使用
 * 3. 需要自定义实现的，直接重写对应方法即可
 */
@AutoService(value = [SPIMain::class, SPIDevTools::class])
class _SPI : DefaultSPIs()