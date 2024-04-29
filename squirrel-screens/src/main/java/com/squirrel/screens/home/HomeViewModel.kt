package com.squirrel.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirrel.data.entity.toDaysWithItems
import com.squirrel.data.repository.DayRepository
import com.squirrel.utils.Constants
import com.squirrel.utils.CurrentDate
import com.squirrel.utils.types.Date
import com.squirrel.utils.types.DaysWithItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dayRepository: DayRepository,
) : ViewModel() {
    var date by mutableStateOf(Date())
        private set
    var daysWithItems by mutableStateOf<List<DaysWithItems>>(emptyList())
        private set
    var monthTotalExp by mutableDoubleStateOf(Constants.INIT_DOUBLE)
    var datePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set


    fun toggleDatePickerShow(show: Boolean) {
        datePickerShow = show
    }

    fun toggleDate(date: Date) {
        this.date = date
        getItemByMonth(year = date.year, month = date.month)
    }


    init {
        getItemByMonth(year = CurrentDate.YEAR, month = CurrentDate.MONTH)
    }

    private fun getItemByMonth(year: Int, month: Int) {
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
}

