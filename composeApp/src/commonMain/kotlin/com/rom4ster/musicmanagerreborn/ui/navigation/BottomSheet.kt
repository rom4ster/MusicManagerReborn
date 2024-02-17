package com.rom4ster.musicmanagerreborn.ui.navigation;

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rom4ster.musicmanagerreborn.ui.actions.PlaylistsActionController
import com.rom4ster.musicmanagerreborn.ui.screens.Home
import com.rom4ster.musicmanagerreborn.ui.screens.Playlists
import com.rom4ster.musicmanagerreborn.ui.state.HomeState
import inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking


val playlistActionController: PlaylistsActionController by inject()
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun BottomSheet() {



    var selection: Int by remember { mutableStateOf(0) }
    var newScreen: Int by remember { mutableStateOf(-1) }



    if (newScreen >= 0) {
        LocalNavigator.currentOrThrow += initTablist[newScreen].newScreen()
        newScreen = -1
    }


    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(2.dp, 2.dp, 0.dp, 0.dp)
            )
            .fillMaxHeight(0.08F),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        initTablist.forEachIndexed  {  index, (type, icon) ->
            if (type(LocalNavigator.currentOrThrow.lastItem)) {
                selection = index
            }
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(modifier = Modifier.clickable(interactionSource,null) {
                newScreen = index
                selection = index
            }.fillMaxWidth(rowBoxWidth).border(2.dp,Color.Red ) ){
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    interactionSource = interactionSource,
                    onClick = {
                        newScreen = index
                        selection = index
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.takeIf { index == selection || isPressed }
                            ?: Color.Transparent
                    )
                ) {
                    Icon(icon, "ActionIcon")
                }
            }
        }
    }
        }

val initTablist =  listOf(
    BottomSheetElement(
        {item: Screen -> item is Home},
        Icons.Outlined.Home,
        { Home(MutableStateFlow(HomeState("Yo"))) }
    ),
    BottomSheetElement(
        {item: Screen ->  item is Playlists},
        Icons.Outlined.QueueMusic,
        {
            Playlists(MutableStateFlow(listOf())).apply {
                playlistActionController.doAction(this, PlaylistsActionController.Actions.LOAD_FROM_DB)
            }
        }
    )
)

val itemsCount = initTablist.size
val rowBoxWidth = 1f / itemsCount


data class BottomSheetElement(
    val func: (item: Screen) -> Boolean,
    val icon: ImageVector,
    val newScreen: @Composable () -> Screen
)
