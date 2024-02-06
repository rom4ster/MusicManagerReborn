package com.rom4ster.musicmanagerreborn.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.rom4ster.musicmanagerreborn.ui.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class Home(var state: MutableStateFlow<HomeState>) : Screen{



    @Composable
    override fun Content() {

        val state by state.collectAsState()

        Column() {
            Text(text = state.display)

            Button(
                onClick = {
                    this@Home.state.update { HomeState("YO I UPDATED SO U SHOULD SEE ME LOL") }
                }
            ) {
                Text("I AM BUTTON")
            }
        }
    }
}