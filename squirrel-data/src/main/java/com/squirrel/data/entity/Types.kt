package com.squirrel.data.entity

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.squirrel.utils.Categories
import com.squirrel.utils.types.Account
import com.squirrel.utils.types.Day
import com.squirrel.utils.types.DaysWithItems
import com.squirrel.utils.types.Transaction


@Keep
data class DayWithItem(
    @Embedded val day: DayEntity,
    @Relation(
        entity = TransactionEntity::class,
        parentColumn = "dayId",
        entityColumn = "dayId",
    )
    val items: List<TransactionEntity>
)


fun Transaction.toTransactionEntity(): TransactionEntity = TransactionEntity(
    itemId = itemId,
    dayId = dayId,
    year = year,
    month = month,
    day = day,
    remark = remark,
    time = time,
    amount = amount,
    account = account,
    category = category,
    timestamp = timestamp
)

fun TransactionEntity.toItem(): Transaction = Transaction(
    itemId = itemId,
    day = day,
    dayId = dayId,
    month = month,
    year = year,
    amount = amount,
    time = time,
    remark = remark,
    account = account,
    category = category,
    timestamp = timestamp
)


fun Day.toDayEntity(): DayEntity = DayEntity(
    dayId = dayId,
    year = year,
    month = month,
    day = day,
    totalAmount = dayExpenditure,
)

fun DayEntity.toDayData(): Day = Day(
    dayId = dayId, year = year, month = month, day = day, dayExpenditure = totalAmount
)

fun DayWithItem.toDaysWithItems(): DaysWithItems = DaysWithItems(
    day = day.toDayData(),
    items = items.map { it.toItem() },
)

fun AccountEntity.toAccount(): Account = Account(
    id = id,
    name = name,
    totalAmount = totalAmount
)

fun Account.toAccountEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    totalAmount = totalAmount
)

fun Transaction.toCSV(accounts: List<Account>): String {
    val csv = StringBuilder()

    csv.append(year.toString())
    csv.append(",")

    csv.append(month.toString())
    csv.append(",")

    csv.append(day.toString())
    csv.append(",")

    csv.append(time)
    csv.append(",")

    csv.append(Categories[category].name)
    csv.append(",")

    csv.append(amount.toString())
    csv.append(",")

    csv.append(remark)
    csv.append(",")

    csv.append(accounts[account].name)
    csv.append(",")


    return csv.toString()
}