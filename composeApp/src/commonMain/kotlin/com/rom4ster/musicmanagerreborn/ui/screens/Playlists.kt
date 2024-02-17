package com.rom4ster.musicmanagerreborn.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon
import com.rom4ster.musicmanagerreborn.ui.ModalDialog
import com.rom4ster.musicmanagerreborn.ui.actions.PlaylistsActionController
import com.rom4ster.musicmanagerreborn.ui.sqlib.SquircleShape
import com.rom4ster.musicmanagerreborn.ui.state.PlaylistState
import com.rom4ster.musicmanagerreborn.utils.*
import inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

class Playlists (val playlists: MutableStateFlow<List<PlaylistState>>) : Screen {


    private val actionController: PlaylistsActionController by inject()

     var initialized = false




    @Composable
     fun stupidComposeHack(playlist: PlaylistState)
     {
         actionController.doAction(this@Playlists, PlaylistsActionController.Actions.OPEN_PLAYLIST, playlist)
     }

    @Composable
    fun addPlaylist(playlistState: PlaylistState) {
        actionController.doAction(this, PlaylistsActionController.Actions.ADD_PLAYLIST, playlistState)
    }

    @Composable
    fun removePlaylist(playlistState: PlaylistState) {
        actionController.doAction(this, PlaylistsActionController.Actions.REMOVE_PLAYLIST, playlistState)
    }

    @Composable
    fun syncPlaylist(id: String) {
        actionController.doAction(this, PlaylistsActionController.Actions.SYNC_PLAYLIST, id)
    }

    private fun KType.resolve() = this resolve listOf()
    private infix fun KType.resolve(kTypeProjection: List<KTypeProjection>) = when(this) {
        String::class.createType() -> {
            InputTypes.TEXT_FIELD
        }
        Set::class.createType(kTypeProjection) -> {
            InputTypes.TEXT_FIELD_MULTILINE
        }
        else -> { throw IllegalArgumentException("Unable to Resolve Ktype to input field")}
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun addForm(export: MutableStateFlow<Map<String, String>>)  {

        var textMap by remember { mutableStateOf(mapOf<String, String>()) }
        Column {
            (
                    PlaylistState.names including listOf(
                        PlaylistState::name.name,
                        PlaylistState::links.name
                    )
            ).forEach {fieldName ->
                val rawType = (PlaylistState.types get fieldName)
                val type = rawType resolve rawType.arguments


                    //Text("${fieldName}: ")
                    TextField(
                        value = textMap[fieldName] ?: "",
                        supportingText = {Text("")},
                        label = { Text(fieldName) },
                        isError = false,
                        onValueChange = {
                            textMap += (fieldName to it)
                            export.compareAndSet(export.value, textMap)
                                        },
                        singleLine = type == InputTypes.TEXT_FIELD
                    )

            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {

        var recomposer by remember { mutableStateOf(PlaylistState("", setOf())) }
        var addition by remember { mutableStateOf(PlaylistState("", setOf())) }
        var removal by remember { mutableStateOf(PlaylistState("", setOf())) }
        var sync by remember {  mutableStateOf("") }
        val scope = rememberCoroutineScope()

        var showModal by remember { mutableStateOf(false) }
        if (!initialized) {
            actionController.doAction(this, PlaylistsActionController.Actions.LOAD_FROM_DB)
            initialized = true
        }

        val state by playlists.collectAsState()
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().fillMaxSize(),
            verticalArrangement = Arrangement.Center

        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("THE GRID")
            }
           val dims =  getScreenDim().width.takeIf { it < GRID_MIN_CELL_WIDTH.dp }
                ?: GRID_MIN_CELL_WIDTH.dp
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(dims),
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    items(state.size) { index ->

                        var showContextMenu by remember { mutableStateOf(false) }

                        val playlist = state[index]
                        if (recomposer != PlaylistState("", setOf())) {
                            stupidComposeHack(recomposer)
                            recomposer = PlaylistState("", setOf())
                        }
                        if (addition.name != "") {
                            addPlaylist(addition)
                            addition = PlaylistState("", setOf())
                        }
                        if (removal.name != "") {
                            removal = PlaylistState("", setOf())
                            removePlaylist(removal)
                        }
                        if (sync != "") {
                            syncPlaylist(sync)
                            sync = ""
                        }
                        var pointerInput: DpOffset? = null
                        Card(
                            modifier = Modifier.padding(
                                horizontal = CARD_PADDING_PLAYLISTS_HORIZONTAL.dp,
                                vertical = CARD_PADDING_PLAYLISTS_VERTICAL.dp
                            ).pointerInput(0) {
                                awaitEachGesture {
                                    awaitPointerEvent().let { event ->
                                        if (
                                            event.type == PointerEventType.Press &&
                                            event.buttons.isSecondaryPressed
                                            ) {
                                            waitForUpOrCancellation()
                                            val coord = event.changes.first().position
                                            pointerInput = DpOffset(coord.x.dp, coord.y.dp)
                                            showContextMenu = true
                                        }
                                    }
                                }
                            }.combinedClickable (
                                onLongClick = {
                                    showContextMenu = true
                                }
                            ) {
                                recomposer = playlist
                            }
                        ) {
                            SwipeBox(
                                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                                swipeDirection = SwipeDirection.Both,
                                endContentWidth = (dims * CARD_SWIPE_WIDTH_PLAYLISTS_PERCENT),
                                startContentWidth = (dims * CARD_SWIPE_WIDTH_PLAYLISTS_PERCENT),
                                endContent = {swipeableState, endSwipeProgress ->
                                    SwipeIcon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        background = Color(0xFFFA1E32),
                                        weight = 1f,
                                        iconSize = 20.dp
                                    ) {
                                        scope.launch {
                                            swipeableState.animateTo(0)
                                            removal = playlist
                                        }
                                    }

                                             },
                                startContent = {swipeableState, endSwipeProgress ->
                                    Row {
                                        SwipeIcon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.White,
                                            background = MaterialTheme.colorScheme.primary,
                                            weight = 1f,
                                            iconSize = 20.dp
                                        ) {
                                            scope.launch {
                                                swipeableState.animateTo(0)
                                                recomposer = playlist
                                            }
                                        }
                                        SwipeIcon(
                                            imageVector = Icons.Outlined.Sync,
                                            contentDescription = "Sync",
                                            tint = Color.White,
                                            background = MaterialTheme.colorScheme.secondary,
                                            weight = 1f,
                                            iconSize = 20.dp
                                        ) {
                                            sync = playlist.id
                                        }
                                    }
                                }
                            ) {_,_,_ ->
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = PLAYLISTS_GRID_INTERNAL_CARD_PADDING_HORIZONTAL.dp)

                                ) {
                                    Text(playlist.name)
                                    Text("${playlist.songs.size} ${"song" pluralizeWithCount playlist.songs.size}")
                                }
                                val interactionSource = remember { MutableInteractionSource() }
                                println(pointerInput)
                                DropdownMenu(
                                    expanded = showContextMenu,
                                    modifier = Modifier.align(Alignment.BottomEnd),
                                    offset = {
                                        println(pointerInput)
                                        pointerInput ?: DpOffset(0.dp, 0.dp)
                                    }(),
                                    onDismissRequest = { showContextMenu = false },
                                ) {
                                    println(pointerInput)
                                    DropdownMenuItem(
                                        onClick = {
                                            recomposer = playlist
                                        },
                                        text = {
                                            Text("Edit")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            sync = playlist.id
                                        },
                                        text = {
                                            Text("Sync")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            removal = playlist
                                        },
                                        text =  {
                                            Text("Delete")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(
                            end = PLAYLISTS_GRID_ADD_BUTTON_END_PADDING.dp,
                            bottom = PLAYLISTS_GRID_ADD_BUTTON_BOTTOM_PADDING.dp
                        )
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.End).size(50.dp),
                        shape = SquircleShape(), //TODO hopefully this guy https://github.com/stoyan-vuchev/squircle-shape/issues can update his lib to actually work until then custom impl
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            showModal = true
                        }
                    ) {
                        Icon(Icons.Default.Add,"addIcon", modifier = Modifier)
                    }
                }
            }

        }

        if (showModal) {
            val imp = MutableStateFlow(mapOf<String, String>())
            ModalDialog(
                onDismissRequest = { showModal = false },
                onSubmit = {
                     GlobalScope.launch {
                             imp.collect { map ->
                                println(map)
                                 addition = PlaylistState(
                                        map get PlaylistState::name.name,
                                        (map get PlaylistState::links.name).split("\n").toSet()
                                    )

                            }


                    }
                    showModal =false

                }
            ) {
                addForm(imp)
            }
        }
    }
}