package com.example.petapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R
import com.example.petapp.ui.addpet.PetFormsBottomNavButtons
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
    actions: @Composable (RowScope.() -> Unit),
    content: @Composable (PaddingValues) -> Unit

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
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.components_top_app_bar_navigation_content_description_back
                            )
                        )
                    }
                },
                actions = {
                    actions()
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
                        if (isListNotEmpty) {
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
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = navigateToAddDataScreen) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_add_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = stringResource(id = R.string.components_forms_dialog_buttons_input))
                }
            }
        }
    ) {
        content(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDataScaffold(
    @StringRes topAppBarTitleId: Int,
    onDoneButtonClicked: () -> Unit,
    navigateToDataDashboard: () -> Unit,
    navigateBack: () -> Unit,
    hideKeyboard: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = topAppBarTitleId)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.components_top_app_bar_navigation_content_description_back
                            )
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PetFormsBottomNavButtons(
                    leftButtonStringId = R.string.components_forms_dialog_buttons_cancel,
                    rightButtonStringId = R.string.components_forms_dialog_buttons_done,
                    onLeftButtonClicked = { navigateBack() },
                    onRightButtonClicked = onDoneButtonClicked
                    )
            }
        }
    ) { innerPadding ->
        FormDefaultColumn(
            headline = R.string.components_forms_title_measurement,
            columnOnClicked = hideKeyboard,
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}