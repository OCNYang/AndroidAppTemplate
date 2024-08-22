package com.app.template

import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.devtools.navgraphs.DevToolsNavGraph
import com.ramcosta.composedestinations.generated.main.navgraphs.MainNavGraph


@NavHostGraph()
annotation class AppGraph {
    @ExternalNavGraph<MainNavGraph>(start = true)
    @ExternalNavGraph<DevToolsNavGraph>
    companion object Includes
}

