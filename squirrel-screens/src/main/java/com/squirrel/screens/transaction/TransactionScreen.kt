package com.squirrel.screens.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.squirrel.screens.home.TransactionCategories
import com.squirrel.utils.Categories
import com.squirrel.utils.Constants
import com.squirrel.utils.R
import com.squirrel.utils.formatDateForYearMonthDay
import com.squirrel.utils.ui.CustomKeyBoard
import com.squirrel.utils.ui.ItemAccounts
import com.squirrel.utils.ui.MyConfirmButton
import com.squirrel.utils.ui.PageHeader
import com.squirrel.utils.ui.SquirrelAlertDialog
import com.squirrel.utils.ui.SquirrelDatePicker
import com.squirrel.utils.ui.SquirrelTimePicker

@Composable
fun TransactionScreen(
    tittle: String,
    transactionViewModel: TransactionViewModel,
    navController: NavHostController
) {
    DisposableEffect(Unit) {
        onDispose {
            transactionViewModel.reset()
        }
    }

    Scaffold(
        topBar = {
            PageHeader(
                title = tittle,
                action = {
                    if (transactionViewModel.transaction.itemId.isNotEmpty()) {
                        IconButton(onClick = {
                            transactionViewModel.deleteTransactionAlertDialogShow =
                                !transactionViewModel.deleteTransactionAlertDialogShow
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }) {
                navController.popBackStack()
            }
        }
    ) { it ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            /* 顶部显示分类和金额*/
            TransactionScreenHeader(
                inputValue = transactionViewModel.amount,
                category = transactionViewModel.transactionCategoryIcon,
                handleClickRight = {
                    transactionViewModel.handleCustomKeyBoardShow(true)
                },
                handleClickLeft = {
                    transactionViewModel.handleCategoryModalShow(true)
                })

            /* 日期 */
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable {
                        transactionViewModel.handleDatePickerShow(true)
                    }) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${transactionViewModel.transactionDate} ${transactionViewModel.transactionTime}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            /* 账户 */
            ItemAccounts(
                accounts = transactionViewModel.accounts,
                accountIndex = transactionViewModel.accountIndex,
                toggleAccount = {
                    transactionViewModel.toggleAccountIndex(it)
                })

            /* 备注输入框 */
            TransactionBasicTextFiled(text = transactionViewModel.remark) {
                transactionViewModel.toggleRemark(it)
            }

            /* 按钮 */
            Row(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp)) {
                MyConfirmButton(text = "确定") {
                    if (transactionViewModel.transaction.itemId.isEmpty()) {
                        transactionViewModel.insertTransaction {
                            navController.popBackStack()
                        }
                    } else {
                        transactionViewModel.updateTransaction {
                            navController.popBackStack()
                        }
                    }

                }
            }
        }
        /* 调起键盘 */
        if (transactionViewModel.customKeyBoardShow) {
            CustomKeyBoard(initialValue = transactionViewModel.amount,
                handleClose = {
                    transactionViewModel.handleCustomKeyBoardShow(false)
                }) {
                transactionViewModel.toggleAmount(it)
            }
        }

        /* 调起分类选择 */
        if (transactionViewModel.categoryModalShow) {
            TransactionCategories(handleClose = {
                transactionViewModel.handleCategoryModalShow(false)
            }) {
                transactionViewModel.toggleTransactionCategory(it)
            }
        }

        /* 日期选择器 */
        if (transactionViewModel.datePickerShow) {
            SquirrelDatePicker(onDismiss = {
                transactionViewModel.handleDatePickerShow(false)
            }, onConfirm = {
                val yearMonthDay = formatDateForYearMonthDay(it)
                transactionViewModel.toggleTransactionDate(
                    year = yearMonthDay.year,
                    month = yearMonthDay.month,
                    day = yearMonthDay.day,
                    formatter = Constants.YEAR_MONTH_DAY
                )
                transactionViewModel.handleTimePickerShow(true)
            })
        }

        /* 时间选择器 */
        if (transactionViewModel.timePickerShow) {
            SquirrelTimePicker(onDismiss = {
                transactionViewModel.handleTimePickerShow(false)
            }, onConfirm = {
                transactionViewModel.toggleTransactionTime(it.hour, it.minute)
                transactionViewModel.toggleTransactionTimestamp(it.hour, it.minute)
                transactionViewModel.handleTimePickerShow(false)
                transactionViewModel.handleDatePickerShow(false)
            })
        }

        if (transactionViewModel.deleteTransactionAlertDialogShow) {
            SquirrelAlertDialog(
                title = "删除支出项",
                text = "此操作会永久删除支出项，确定吗？",
                onDismissRequest = {
                    transactionViewModel.deleteTransactionAlertDialogShow =
                        !transactionViewModel.deleteTransactionAlertDialogShow
                },
                onConfirmRequest = {
                    transactionViewModel.deleteTransaction {
                        transactionViewModel.deleteTransactionAlertDialogShow =
                            !transactionViewModel.deleteTransactionAlertDialogShow
                        navController.popBackStack()
                    }
                },
            )
        }
    }
}


@Composable
fun TransactionScreenHeader(
    inputValue: String,
    category: Int?,
    handleClickRight: () -> Unit,
    handleClickLeft: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .heightIn(180.dp)
                .height(IntrinsicSize.Max)
                .background(MaterialTheme.colorScheme.onTertiary)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxHeight()
                    .widthIn(140.dp)
                    .width(IntrinsicSize.Max)
                    .clickable { handleClickLeft() }) {

                Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    if (category != null) {
                        Box(
                            Modifier
                                .border(1.dp, Color.White, CircleShape)
                                .padding(10.dp), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(55.dp),
                                imageVector = ImageVector.vectorResource(id = Categories[category].icon),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    } else {
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(37.dp)
                        )
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { handleClickRight() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "¥$inputValue",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.W800,
                        color = Color.White
                    )
                    category?.let {
                        Text(text = Categories[it].name, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionBasicTextFiled(text: String, handleChangeValue: (it: String) -> Unit) {
    Row(
        Modifier
            .heightIn(150.dp)
            .height(IntrinsicSize.Max)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val focusManager = LocalFocusManager.current
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                value = text,
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.primary,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = "备注",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    innerTextField()
                },
                onValueChange = { handleChangeValue(it) },
            )
        }
    }
}