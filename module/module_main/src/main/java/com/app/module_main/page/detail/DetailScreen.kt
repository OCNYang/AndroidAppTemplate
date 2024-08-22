package com.app.module_main.page.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.module_main.page.MainGraph
import com.ocnyang.status_box.StateContainer
import com.ocnyang.status_box.StatusBox
import com.ocnyang.status_box.UIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<MainGraph>
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val pageStateContainer = StateContainer(state = UIState.Success(""))

    Scaffold(
        modifier = modifier,
    ) { contentPadding ->
        StatusBox(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            stateContainer = pageStateContainer,
            contentScrollEnabled = true
        ) {

            Button(onClick = {
            }) {
                Text(text = "hi yang!")
            }


        }
    }

}

