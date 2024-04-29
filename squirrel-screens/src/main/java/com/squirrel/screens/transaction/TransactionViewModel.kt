package com.squirrel.screens.transaction

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.toAccount
import com.squirrel.data.entity.toAccountEntity
import com.squirrel.data.entity.toDayData
import com.squirrel.data.entity.toDayEntity
import com.squirrel.data.entity.toTransactionEntity
import com.squirrel.data.repository.AccountRepository
import com.squirrel.data.repository.CombineRepository
import com.squirrel.data.repository.DayRepository
import com.squirrel.utils.Constants
import com.squirrel.utils.formatDate
import com.squirrel.utils.formatDouble
import com.squirrel.utils.getMillisFromDate
import com.squirrel.utils.randomId
import com.squirrel.utils.types.Account
import com.squirrel.utils.types.Day
import com.squirrel.utils.types.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dayRepository: DayRepository,
    private val combineRepository: CombineRepository,
) : ViewModel() {
    var transaction by mutableStateOf(Transaction())
        private set
    private var previousTransaction by mutableStateOf(Transaction())
    private var day by mutableStateOf(Day())
    private var previousDay by mutableStateOf(Day())
    var amount by mutableStateOf(formatDouble(Constants.INIT_DOUBLE))
        private set
    var remark by mutableStateOf(Constants.EMPTY_STRING)
        private set
    var accounts by mutableStateOf<List<Account>>(emptyList())
        private set
    private var previousAccount by mutableIntStateOf(Constants.ACCOUNT_VALUE)
    var accountIndex by mutableIntStateOf(Constants.ACCOUNT_VALUE)
    var transactionCategoryIcon by mutableStateOf<Int?>(null)
        private set
    var transactionDate by mutableStateOf(
        formatDate(
            formatter = Constants.YEAR_MONTH_DAY, time = null
        )
    )
        private set
    var transactionTime by mutableStateOf(Constants.EMPTY_STRING)
        private set

    /* modal 控制 */
    var customKeyBoardShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var categoryModalShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var datePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var timePickerShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var deleteTransactionAlertDialogShow by mutableStateOf(Constants.INIT_FALSE)


    init {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { it ->
                accounts = it.map {
                    it.toAccount()
                }
            }
        }
    }


    fun toggleTransaction(value: Transaction) {
        transaction = value
        previousTransaction = value
    }

    fun toggleDay(value: Day) {
        day = value
        previousDay = value
    }

    fun toggleAmount(value: String) {
        amount = value
        transaction = transaction.copy(amount = value.toDouble())
    }

    fun toggleRemark(value: String) {
        remark = value
        transaction = transaction.copy(remark = value)
    }

    fun toggleAccounts(value: List<Account>) {
        accounts = value
    }

    fun toggleAccountIndex(value: Int) {
        previousAccount = transaction.account
        accountIndex = value
        transaction = transaction.copy(account = value)
    }

    fun toggleTransactionCategory(value: Int?) {
        transactionCategoryIcon = value
        value?.let {
            transaction = transaction.copy(category = it)
        }
    }

    fun toggleTransactionDate(year: Int, month: Int, day: Int, formatter: String) {
        transactionDate = formatDate(year = year, month = month, day = day, formatter = formatter)
        transaction = transaction.copy(year = year, month = month, day = day)
    }

    fun toggleTransactionTime(hour: Int, minute: Int) {
        var time = "$hour:$minute"
        if (minute.toString().length == 1) {
            time = "$hour:0$minute"
        }
        transactionTime = time
        transaction = transaction.copy(time = time)
    }

    fun toggleTransactionTime(value: String) {
        transactionTime = value
        transaction = transaction.copy(time = value)
    }

    fun toggleTransactionTimestamp(value: Long) {
        transaction = transaction.copy(timestamp = value)
    }

    fun toggleTransactionTimestamp(hour: Int, minute: Int) {
        transaction = transaction.copy(
            timestamp = getMillisFromDate(
                year = transaction.year,
                month = transaction.month,
                day = transaction.day,
                hour = hour,
                minute = minute
            )
        )
    }

    fun reset() {
        day = Day()
        transaction = Transaction()
        previousTransaction = Transaction()
        previousDay = Day()
        amount = formatDouble(Constants.INIT_DOUBLE)
        remark = Constants.EMPTY_STRING
        accountIndex = Constants.ACCOUNT_VALUE
        transactionCategoryIcon = null
        transactionDate = formatDate(
            formatter = Constants.YEAR_MONTH_DAY, time = null
        )
        transactionTime = Constants.EMPTY_STRING
    }

    fun test() {
        Log.d("transaction", transaction.toString())
    }

    fun insertTransaction(onCallback: (() -> Unit) = {}) {
        if (transactionCategoryIcon == null) {
            handleCategoryModalShow(true)
            return
        }
        if (amount.toDouble() <= 0) {
            handleCustomKeyBoardShow(true)
            return
        }
        viewModelScope.launch {
            val containDay = dayRepository.getDayByDate(
                year = transaction.year, month = transaction.month, day = transaction.day
            )
            if (containDay == null) {
                val dayId = randomId(10)
                val itemId = randomId(9)
                transaction = transaction.copy(
                    itemId = itemId,
                    dayId = dayId,
                    amount = amount.toDouble(),
                )
                day = day.copy(
                    dayId = dayId,
                    dayExpenditure = transaction.amount,
                    year = transaction.year,
                    month = transaction.month,
                    day = transaction.day
                )

                accounts[transaction.account - 1].totalAmount += transaction.amount
                combineRepository.insertCombine(
                    day.toDayEntity(),
                    transaction.toTransactionEntity(),
                    accounts[transaction.account - 1].toAccountEntity()
                )
            } else {
                val itemId = randomId(9)
                transaction = transaction.copy(
                    dayId = containDay.dayId,
                    itemId = itemId,
                    amount = amount.toDouble(),
                )
                accounts[transaction.account - 1].totalAmount += transaction.amount

                day = containDay.toDayData()
                    .copy(dayExpenditure = containDay.totalAmount + transaction.amount)

                /* 用事务修改数据*/
                combineRepository.insertCombine(
                    day.toDayEntity(),
                    transaction.toTransactionEntity(),
                    accounts[transaction.account - 1].toAccountEntity()
                )
            }
            onCallback()
        }
    }

    fun updateTransaction(onCallback: (() -> Unit) = {}) {
        if (transaction == previousTransaction) onCallback()
        val dayListOf = mutableListOf<DayEntity>()
        val accountListOf = mutableListOf<AccountEntity>()
        var delectableDay: DayEntity? = null
        viewModelScope.launch {
            /* 判断是否修改了日期 */
            if (transaction.year != previousTransaction.year || transaction.month != previousTransaction.month || transaction.day != previousTransaction.day) {
                /* 查找数据库中是否已经有了修改后的这一天 */
                val containDay = dayRepository.getDayByDate(
                    year = transaction.year, month = transaction.month, day = transaction.day
                )
                if (containDay == null) {
                    val dayId = randomId(10)
                    day = day.copy(
                        dayId = dayId,
                        year = transaction.year,
                        month = transaction.month,
                        day = transaction.day,
                        dayExpenditure = transaction.amount
                    )
                    transaction = transaction.copy(dayId = dayId)
                } else {
                    day = containDay.toDayData()
                    day = day.copy(dayExpenditure = day.dayExpenditure + transaction.amount)
                    transaction = transaction.copy(dayId = day.dayId)
                }
                /* 因为修改了日期，所以修改前的那天的金额需要修改 */
                previousDay =
                    previousDay.copy(dayExpenditure = previousDay.dayExpenditure - previousTransaction.amount)

                /* 如果这天只有一个交易项，日期修改后，则可以删除这个日期 */
                val transactionCount = combineRepository.getTransactionCount(previousDay.dayId)
                if (transactionCount == 1) {
                    delectableDay = previousDay.toDayEntity()
                } else {
                    dayListOf.add(previousDay.toDayEntity())
                }
                dayListOf.add(day.toDayEntity())
            } else {
                day =
                    day.copy(dayExpenditure = day.dayExpenditure - previousTransaction.amount + transaction.amount)
                dayListOf.add(day.toDayEntity())
            }

            /* 修改对应账户金额 */
            if (transaction.account == previousAccount) {
                accounts[transaction.account - 1].totalAmount =
                    accounts[transaction.account - 1].totalAmount - previousTransaction.amount + transaction.amount
                accountListOf.add(accounts[transaction.account - 1].toAccountEntity())
            } else {
                accounts[previousAccount - 1].totalAmount -= previousTransaction.amount
                accounts[transaction.account - 1].totalAmount += transaction.amount
                accountListOf.add(accounts[transaction.account - 1].toAccountEntity())
                accountListOf.add(accounts[previousAccount - 1].toAccountEntity())
            }


            combineRepository.updateCombine(
                transaction.toTransactionEntity(),
                dayListOf,
                accountListOf,
                delectableDay
            )
            onCallback()
        }
    }

    fun deleteTransaction(onCallback: (() -> Unit) = {}) {
        viewModelScope.launch {
            val transactionCount = combineRepository.getTransactionCount(transaction.dayId)
            /* 修改对应账户金额 */
            accounts[transaction.account - 1].totalAmount -= transaction.amount

            if (transactionCount != 1) {
                /* 修改day金额 */
                day = day.copy(dayExpenditure = day.dayExpenditure - transaction.amount)

                combineRepository.deleteTransaction(
                    transaction.toTransactionEntity(),
                    day.toDayEntity(),
                    accounts[transaction.account - 1].toAccountEntity()
                )
            } else {
                /* 删除day */
                combineRepository.deleteDayWithTransaction(
                    day.toDayEntity(),
                    accounts[transaction.account - 1].toAccountEntity()
                )
            }

            onCallback()
        }
    }

    /* modal 控制 */
    fun handleCustomKeyBoardShow(show: Boolean) {
        customKeyBoardShow = show
    }

    fun handleCategoryModalShow(show: Boolean) {
        categoryModalShow = show
    }

    fun handleDatePickerShow(show: Boolean) {
        datePickerShow = show
    }

    fun handleTimePickerShow(show: Boolean) {
        timePickerShow = show
    }

    fun handleDeleteTransactionAlertDialogShow(show: Boolean) {
        deleteTransactionAlertDialogShow = show
    }
}