package com.squirrel.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.squirrel.home.modal.BottomSheetModal
import com.squirrel.setting.SettingModalLayout
import com.squirrel.utils.Constants
import com.squirrel.utils.formatDateForCurrentTime
import com.squirrel.utils.formatDateForYearMonthDay
import com.squirrel.utils.getMillisFromDate
import com.squirrel.utils.ui.SquirrelAlertDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    parentEntry: NavBackStackEntry,
    homeViewModel: HomeViewModel = hiltViewModel(parentEntry),
) {
    val categoryBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val additionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val itemDetailBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val settingBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val coroutineScope = rememberCoroutineScope()



    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
    ) {
        HomeHeader(
            date = homeViewModel.date,
            monthTotalExp = homeViewModel.monthTotalExp,
            toggleCategoryModalShow = homeViewModel::toggleCategoryModalShow,
            toggleSettingModalShow = homeViewModel::toggleSettingModalShow,
            toggleDatePickerShow = homeViewModel::toggleDatePickerShow
        )


        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            homeViewModel.daysWithItems.forEach { dayWithItems ->
                stickyHeader(key = dayWithItems.day.dayId) {
                    DayItem(day = dayWithItems.day)
                }

                items(items = dayWithItems.items, key = { it.itemId }) { item ->
                    HomeItem(
                        category = item.category,
                        item = item,
                        onLongClick = {
                            homeViewModel.toggleDayData(dayWithItems.day)
                            homeViewModel.toggleItemData(item)
                            homeViewModel.toggleDeleteItemForLongClickDialogShow(true)
                        },
                    ) {
                        homeViewModel.toggleItemData(item)
                        homeViewModel.toggleDayData(dayWithItems.day)
                        homeViewModel.toggleItemDetailShow(true)
                    }
                }
            }
        }
    }
    if (homeViewModel.deleteItemForLongClickDialogShow) {
        SquirrelAlertDialog(
            title = "删除支出项",
            text = "此操作会永久删除支出项，确定吗？",
            onDismissRequest = {
                homeViewModel.toggleDeleteItemForLongClickDialogShow(false)
            },
            onConfirmRequest = {
                homeViewModel.deleteItem {
                    homeViewModel.toggleDeleteItemForLongClickDialogShow(false)
                }
            },
        )
    }

    if (homeViewModel.categoryModalShow) {
        BottomSheetModal(
            sheetState = categoryBottomSheetState,
            coroutineScope = coroutineScope,
            onClose = homeViewModel::toggleCategoryModalShow
        ) {
            CategoryLayout(
                toggleItemCategory = homeViewModel::toggleItemCategory,
                toggleAdditionItemModalShow = homeViewModel::toggleAdditionItemModalShow
            )
        }
    }

    if (homeViewModel.additionItemModalShow) {
        BottomSheetModal(
            sheetState = additionBottomSheetState,
            coroutineScope = coroutineScope,
            onClose = homeViewModel::toggleAdditionItemModalShow
        ) {
            HomeItemDetail(item = homeViewModel.item,
                previousItem = homeViewModel._item,
                temporaryMoney = homeViewModel.temporaryMoney,
                toggleAccount = homeViewModel::toggleAccount,
                toggleItemMoney = homeViewModel::toggleItemMoney,
                toggleItemRemark = homeViewModel::toggleItemRemark,
                toggleDatePickerShow = homeViewModel::toggleHomeDetailDatePickerShow,
                onSave = {
                    homeViewModel.insertItem {
                        coroutineScope.launch {
                            additionBottomSheetState.hide()
                            homeViewModel.toggleAdditionItemModalShow(false)
                        }
                    }
                })
        }
    }

    if (homeViewModel.itemDetailShow) {
        BottomSheetModal(
            sheetState = itemDetailBottomSheetState,
            coroutineScope = coroutineScope,
            onClose = homeViewModel::toggleItemDetailShow
        ) {
            HomeItemDetail(item = homeViewModel.item,
                previousItem = homeViewModel._item,
                alertDialogShow = homeViewModel.deleteDialogShow,
                temporaryMoney = homeViewModel.temporaryMoney,
                toggleAccount = homeViewModel::toggleAccount,
                toggleItemMoney = homeViewModel::toggleItemMoney,
                toggleAlertDialogShow = homeViewModel::toggleDeleteDialogShow,
                toggleItemRemark = homeViewModel::toggleItemRemark,
                onDelete = {
                    homeViewModel.deleteItem {
                        coroutineScope.launch {
                            homeViewModel.toggleDeleteDialogShow(false)
                            itemDetailBottomSheetState.hide()
                            homeViewModel.toggleItemDetailShow(false)
                        }
                    }
                },
                onSave = {
                    homeViewModel.updateItem {
                        coroutineScope.launch {
                            itemDetailBottomSheetState.hide()
                            homeViewModel.toggleItemDetailShow(false)
                        }
                    }
                })
        }
    }


    if (homeViewModel.settingModalShow) {
        BottomSheetModal(
            sheetState = settingBottomSheetState,
            coroutineScope = coroutineScope,
            onClose = homeViewModel::toggleSettingModalShow
        ) {
            SettingModalLayout(
                navController = navController, parentEntry = parentEntry
            )
        }
    }

    if (homeViewModel.datePickerShow) {
        HomeDatePicker(
            toggleDatePickerShow = homeViewModel::toggleDatePickerShow,
        ) {
            homeViewModel.toggleDate(formatDateForYearMonthDay(it))
            homeViewModel.toggleDatePickerShow(false)

        }
    }

    if (homeViewModel.homeDetailDatePickerShow) {
        HomeDatePicker(
            toggleDatePickerShow = homeViewModel::toggleHomeDetailDatePickerShow,
        ) {
            val date = formatDateForYearMonthDay(it)
            homeViewModel.toggleItemDate(year = date.year, month = date.month, day = date.day)
            homeViewModel.toggleTimePickerShow(true)
        }
    }

    if (homeViewModel.timePickerShow) {
        HomeTimePicker(onCancel = { homeViewModel.toggleTimePickerShow(false) }) {
            val millis = getMillisFromDate(
                year = homeViewModel.item.year,
                month = homeViewModel.item.month,
                day = homeViewModel.item.day,
                hour = it.hour,
                minute = it.minute
            )
            homeViewModel.toggleItemTime(
                time = formatDateForCurrentTime(
                    millis = millis, formatter = Constants.HOUR_MINUTE
                )
            )
            homeViewModel.toggleItemTimestamp(millis)
            homeViewModel.toggleTimePickerShow(false)
            homeViewModel.toggleHomeDetailDatePickerShow(false)
        }
    }

}
