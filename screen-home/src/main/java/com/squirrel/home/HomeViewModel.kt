package com.squirrel.home

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirrel.data.entity.toDayData
import com.squirrel.data.entity.toDayEntity
import com.squirrel.data.entity.toDaysWithItems
import com.squirrel.data.entity.toItemEntity
import com.squirrel.data.repository.DayRepository
import com.squirrel.data.repository.EntireRepository
import com.squirrel.data.repository.ItemRepository
import com.squirrel.utils.Constants
import com.squirrel.utils.CurrentDate
import com.squirrel.utils.formatDateForCurrentTime
import com.squirrel.utils.randomId
import com.squirrel.utils.types.Date
import com.squirrel.utils.types.Day
import com.squirrel.utils.types.DaysWithItems
import com.squirrel.utils.types.Item
import com.squirrel.utils.types.SnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.sql.Timestamp
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val dayRepository: DayRepository,
    private val entireRepository: EntireRepository,
) : ViewModel() {
    var date by mutableStateOf(Date())
        private set
    var categoryModalShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var additionItemModalShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var itemDetailShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var item by mutableStateOf(Item())
        private set
    var _item by mutableStateOf(Item())
    private var dayData by mutableStateOf(Day())
    var daysWithItems by mutableStateOf<List<DaysWithItems>>(emptyList())
        private set
    var monthTotalExp by mutableDoubleStateOf(Constants.INIT_DOUBLE)
    var temporaryMoney by mutableStateOf(Constants.EMPTY_STRING)
    var snackBarState by mutableStateOf(SnackBar())
        private set
    var deleteDialogShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var settingModalShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var datePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var homeDetailDatePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var timePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var deleteItemForLongClickDialogShow by mutableStateOf(Constants.INIT_FALSE)


    fun toggleCategoryModalShow(value: Boolean) {
        categoryModalShow = value
    }

    fun toggleAdditionItemModalShow(value: Boolean) {
        if (value) {
            item = item.copy(
                time = formatDateForCurrentTime(formatter = Constants.HOUR_MINUTE)
            )

            /* 创建副本*/
            _item = item

        } else {
            resetItemData()
            resetDayData()
        }
        additionItemModalShow = value
    }

    fun toggleItemDetailShow(value: Boolean) {
        itemDetailShow = value
        if (!value) {
            resetItemData()
            resetDayData()
        }
    }

    fun toggleDeleteDialogShow(show: Boolean) {
        deleteDialogShow = show
    }

    fun toggleSettingModalShow(show: Boolean) {
        settingModalShow = show
    }

    fun toggleDatePickerShow(show: Boolean) {
        datePickerShow = show
    }

    fun toggleHomeDetailDatePickerShow(show: Boolean) {
        homeDetailDatePickerShow = show
    }

    fun toggleTimePickerShow(show: Boolean) {
        timePickerShow = show
    }

    fun toggleDeleteItemForLongClickDialogShow(show: Boolean) {
        deleteItemForLongClickDialogShow = show
        if (!show) {
            resetItemData()
            resetItemData()
        }
    }

    fun toggleItemCategory(category: Int) {
        item = item.copy(category = category)
    }

    fun toggleItemMoney(value: String) {
        temporaryMoney = value
        value.toDoubleOrNull()?.let {
            item = item.copy(exp = it)
        }
    }

    fun toggleItemRemark(value: String) {
        item = item.copy(remark = value)
    }

    fun toggleItemData(item: Item) {
        this.item = item/* 创建副本 */
        _item = item
    }

    fun toggleItemDate(year: Int, month: Int, day: Int) {
        item = item.copy(year = year, month = month, day = day)
    }

    fun toggleItemTime(time: String) {
        item = item.copy(time = time)
    }

    fun toggleItemTimestamp(timestamp: Long) {
        item = item.copy(timestamp = timestamp)
    }

    fun toggleDayData(day: Day) {
        dayData = day
    }


    fun toggleSnackBarState(snackBarHostState: SnackbarHostState) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message = "hello", duration = SnackbarDuration.Short)
        }
    }

    fun toggleAccount(value: Int) {
        item = item.copy(account = value)
    }

    fun toggleDate(date: Date) {
        this.date = date
        getItemByMonth(year = date.year, month = date.month)
    }


    private fun resetItemData() {
        item = Item()
        _item = Item()
        temporaryMoney = Constants.EMPTY_STRING
    }

    private fun resetDayData() {
        dayData = Day()
        temporaryMoney = Constants.EMPTY_STRING
    }


    init {
        getItemByMonth(year = CurrentDate.YEAR, month = CurrentDate.MONTH)
    }

    private fun getItemByMonth(year: Int, month: Int) {
        println(month)
        viewModelScope.launch {
            val result = dayRepository.getDayByMonth(year = year, month = month)
                .map { it -> it.map { it.toDaysWithItems() } }
            result.collect { it ->
                daysWithItems = it.map { di ->
                    di.copy(items = di.items.sortedByDescending { item -> item.timestamp })
                }
                monthTotalExp = it.sumOf {
                    it.day.dayExpenditure
                }
            }
        }
    }


    fun insertItem(onCallback: (() -> Unit) = {}) {
        if (item.exp == _item.exp) return
        viewModelScope.launch {
            val containDay = dayRepository.getDayByDate(
                year = item.year, month = item.month, day = item.day
            )
            if (containDay == null) {
                val dayId = randomId(10)
                val itemId = randomId(9)
                item = item.copy(
                    itemId = itemId,
                    dayId = dayId,
                    exp = temporaryMoney.toDouble(),
                )
                dayData = dayData.copy(
                    dayId = dayId,
                    dayExpenditure = item.exp,
                    year = item.year,
                    month = item.month,
                    day = item.day
                )
                entireRepository.insertTwo(
                    dayData.toDayEntity(),
                    item.toItemEntity(),
                )
            } else {
                val itemId = randomId(9)
                item = item.copy(
                    dayId = containDay.dayId,
                    itemId = itemId,
                    exp = temporaryMoney.toDouble(),
                )

                dayData = containDay.toDayData()
                    .copy(dayExpenditure = containDay.dayExpenditure + item.exp)/* 用事务修改数据*/
                entireRepository.insertTwo(
                    dayData.toDayEntity(),
                    item.toItemEntity(),
                )
            }
            onCallback()
        }
    }

    fun updateItem(onCallback: (() -> Unit) = {}) {
        if (item == _item) return

        if (item.exp != _item.exp) {
            val diffValue = item.exp - _item.exp
            dayData = if (diffValue < 0) {
                dayData.copy(dayExpenditure = dayData.dayExpenditure - (_item.exp - item.exp))
            } else {
                dayData.copy(dayExpenditure = dayData.dayExpenditure + (item.exp - _item.exp))
            }
        }
        viewModelScope.launch {
            /* 启用事务修改数据*/
            entireRepository.updateItemWithDay(
                item = item.toItemEntity(), day = dayData.toDayEntity()
            )
            onCallback()
        }
    }

    fun deleteItem(onCallback: (() -> Unit) = {}) {
        viewModelScope.launch {
            val count = entireRepository.getItemCount(dayData.dayId)
            if (count == 1) {
                entireRepository.deleteItemWithDay(dayData.toDayEntity())
            } else {
                dayData = dayData.copy(dayExpenditure = dayData.dayExpenditure - _item.exp)
                entireRepository.deleteItemWithToggleDay(
                    _item.toItemEntity(), dayData.toDayEntity()
                )
            }
            onCallback()
        }
    }
}

