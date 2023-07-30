package com.example.petapp.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.components.forms.FormDefaultColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureScaffold(
    topAppBarTitle: String,
    topAppBarMenuExpanded: Boolean,
    navigateToAddDataScreen: () -> Unit,
    navigateToUpdateDataScreen: () -> Unit,
    deleteDataItem: () -> Unit,
    navigateBack: () -> Unit,
    onDropdownMenuItemClicked: () -> Unit,
    dropdownMenuOnDismissRequest: () -> Unit,
    isListNotEmpty: Boolean,
    itemsSelected: Boolean = true,
    clearSelectedItems: (Long) -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    content: @Composable (PaddingValues) -> Unit,

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topAppBarTitle
                    )
                },
                navigationIcon = {
                    if (!itemsSelected) {
                        IconButton(onClick = { clearSelectedItems(0) }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    } else {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(
                                    id = R.string.components_top_app_bar_navigation_content_description_back
                                )
                            )
                        }
                    }
                },
                actions = {
                    actions()
                    if (itemsSelected && isListNotEmpty) {
                        IconButton(onClick = onDropdownMenuItemClicked) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.components_top_app_bar_menu_content_description_open_menu)
                            )
                        }
                        DropdownMenu(
                            expanded = topAppBarMenuExpanded,
                            onDismissRequest = dropdownMenuOnDismissRequest
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.components_top_app_bar_navigation_menu_add)) },
                                onClick = {
                                    navigateToAddDataScreen()
                                    dropdownMenuOnDismissRequest()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Add,
                                        contentDescription = null
                                    )
                                })

                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.components_top_app_bar_navigation_menu_update)) },
                                    onClick = {
                                        navigateToUpdateDataScreen()
                                        dropdownMenuOnDismissRequest()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Edit,
                                            contentDescription = null
                                        )
                                    })
                                Divider()
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = R.string.components_top_app_bar_navigation_menu_delete)) },
                                    onClick = {
                                        deleteDataItem()
                                        dropdownMenuOnDismissRequest()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null
                                        )
                                    })

                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AddSimpleFab(onClick = navigateToAddDataScreen)
        }
    ) {
        content(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDataScaffold(
    @StringRes topAppBarTitleId: Int,
    onRightButtonClicked: () -> Unit,
    navigateBack: () -> Unit,
    onLeftButtonClicked: () -> Unit,
    hideKeyboard: () -> Unit,
    @StringRes headline: Int = R.string.components_forms_title_measurement,
    @StringRes supportingText: Int = R.string.components_forms_description_fill_out,
    @StringRes leftButtonStringId: Int = R.string.components_forms_dialog_buttons_cancel,
    @StringRes rightButtonStringId: Int = R.string.components_forms_dialog_buttons_done,
    @DrawableRes iconId: Int? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = topAppBarTitleId)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(
                                id = R.string.components_top_app_bar_navigation_content_description_back
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        FormDefaultColumn(
            headline = headline,
            supportingText = supportingText,
            columnOnClicked = hideKeyboard,
            iconId = iconId,
            navigation = {
                PetFormsBottomNavButtons(
                    leftButtonStringId = leftButtonStringId,
                    rightButtonStringId = rightButtonStringId,
                    onLeftButtonClicked = onLeftButtonClicked,
                    onRightButtonClicked = onRightButtonClicked
                )
            },
            modifier = Modifier.padding(innerPadding),
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScaffoldWithNavButton(
    navigateBack: () -> Unit,
    onColumnClicked: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes topAppBarTitleId: Int = R.string.util_blank,
    content: @Composable (ColumnScope.() -> Unit)

) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll((scrollBehavior.nestedScrollConnection)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = topAppBarTitleId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.components_top_app_bar_navigation_content_description_back
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    onClick = onColumnClicked,
                    interactionSource = interactionSource,
                    indication = null
                )
                .padding(paddingValues = it)
        ) {
            Column(
                modifier = Modifier.widthIn(200.dp, 300.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
fun PetFormsBottomNavButtons(
    @StringRes leftButtonStringId: Int,
    @StringRes rightButtonStringId: Int,
    onLeftButtonClicked: () -> Unit,
    onRightButtonClicked: () -> Unit
) {
    Spacer(modifier = Modifier.padding(20.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Button(onClick = onLeftButtonClicked, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(id = leftButtonStringId))
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(onClick = onRightButtonClicked, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(id = rightButtonStringId))
        }
    }
}
